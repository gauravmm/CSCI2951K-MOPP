/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.corpus;

import edu.brown.csci2951k.models.data.MObject;
import edu.brown.csci2951k.models.data.MObjectSet;
import edu.brown.csci2951k.models.data.XMLPrimitiveMObjectReference;
import edu.brown.csci2951k.models.language.ObjectLanguageModel;
import edu.brown.csci2951k.models.space.SpatialCoords;
import edu.brown.csci2951k.models.space.SpatialModel;
import edu.brown.csci2951k.util.Pair;
import edu.brown.csci2951k.util.xml.XMLElement;
import edu.brown.csci2951k.util.xml.XMLObject;
import edu.brown.csci2951k.util.xml.XMLTypeAdapter;
import edu.brown.csci2951k.util.xml.adapters.XMLPrimitivePair;
import edu.brown.csci2951k.util.xml.adapters.XMLPrimitiveString;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Gaurav Manek
 * @param <C> The type of SpatialCoords.
 * @param <L> The type of LanguageModel to use.
 */
public class XMLCorpusAdapter<C extends SpatialCoords, L extends ObjectLanguageModel> implements XMLTypeAdapter<Corpus<C>> {

    private final XMLTypeAdapter<C> coordAdapter;
    private final XMLTypeAdapter<L> langModAdapter;

    public XMLCorpusAdapter(XMLTypeAdapter<C> coordAdapter, XMLTypeAdapter<L> langModAdapter) {
        this.coordAdapter = coordAdapter;
        this.langModAdapter = langModAdapter;
    }
    
    @Override
    public XMLElement toXML(String name, Corpus input) {
        return input.toXML(name);
    }

    @Override
    public Corpus fromXML(XMLElement input) {
        XMLObject o = input.toXMLObject();
        MObjectSet obj = MObjectSet.fromXML(o.get("objects"), this.langModAdapter);
        SpatialModel<C> sM = SpatialModel.fromXML(o.get("model"), coordAdapter, obj);
        Collection<Pair<String, MObject>> corpus = o.get("corpus").getCollectionValue(ArrayList::new, new XMLPrimitivePair<>(XMLPrimitiveString.getInstance(), new XMLPrimitiveMObjectReference(obj)));

        return new Corpus(obj, sM, corpus);
    }

}
