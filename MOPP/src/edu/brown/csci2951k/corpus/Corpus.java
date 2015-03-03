/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.corpus;

import edu.brown.csci2951k.models.data.MObject;
import edu.brown.csci2951k.models.data.MObjectSet;
import edu.brown.csci2951k.models.data.XMLPrimitiveMObjectReference;
import edu.brown.csci2951k.models.space.SpatialCoords;
import edu.brown.csci2951k.models.space.SpatialModel;
import edu.brown.csci2951k.util.Pair;
import edu.brown.csci2951k.util.xml.XMLCollection;
import edu.brown.csci2951k.util.xml.XMLElement;
import edu.brown.csci2951k.util.xml.XMLObject;
import edu.brown.csci2951k.util.xml.XMLSerializable;
import edu.brown.csci2951k.util.xml.adapters.XMLPrimitivePair;
import edu.brown.csci2951k.util.xml.adapters.XMLPrimitiveString;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Gaurav Manek
 * @param <T> The type of coordinate system to use.
 */
public class Corpus<T extends SpatialCoords> implements XMLSerializable {

    private final MObjectSet obj;
    private final SpatialModel<T> sM;
    private final List<Pair<String, MObject>> corpus;
    private final XMLPrimitivePair<String, MObject> xmlPrimitivePairAdapter;

    public Corpus(MObjectSet obj, SpatialModel<T> sM, Collection<Pair<String, MObject>> corpus) {
        this.obj = obj;
        this.sM = sM;
        this.corpus = new ArrayList<>(corpus);

        xmlPrimitivePairAdapter = new XMLPrimitivePair(XMLPrimitiveString.getInstance(), new XMLPrimitiveMObjectReference(obj));
    }

    public MObjectSet getObj() {
        return obj;
    }

    public SpatialModel<T> getsM() {
        return sM;
    }

    public List<Pair<String, MObject>> getCorpus() {
        return corpus;
    }

    public Pair<List<Pair<String, MObject>>, List<Pair<String, MObject>>> getSplitCorpus(double frac) {
        if (frac < 0 || frac > 1) {
            throw new IllegalArgumentException("This shouldn't happen.");
        }

        int sz = (int) (corpus.size() * frac);
        sz = Math.min(sz, corpus.size() - 1);
        sz = Math.max(sz, 1);

        ArrayList<Pair<String, MObject>> c = new ArrayList<>(corpus);
        Collections.shuffle(c);
        return new Pair<>(c.subList(0, sz), c.subList(sz, corpus.size()));
    }

    @Override
    public XMLElement toXML(String xmlObjectName) {
        XMLObject rv = new XMLObject(xmlObjectName);
        rv.add("objects", obj);
        rv.add("model", sM);
        rv.add(new XMLCollection("corpus", xmlPrimitivePairAdapter, this.corpus));
        return rv;
    }

}
