/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.util.xml.adapters;

import edu.brown.csci2951k.util.Pair;
import edu.brown.csci2951k.util.xml.XMLElement;
import edu.brown.csci2951k.util.xml.XMLObject;
import edu.brown.csci2951k.util.xml.XMLPrimitiveTypeAdapter;
import edu.brown.csci2951k.util.xml.XMLSerializingException;
import edu.brown.csci2951k.util.xml.XMLTypeAdapter;

/**
 *
 * @author Gaurav Manek
 */
public class XMLPrimitiveObjectPair<T, R> implements XMLTypeAdapter<Pair<T, R>> {

    private final XMLPrimitiveTypeAdapter<T> aKey;
    private final XMLTypeAdapter<R> aVal;

    public XMLPrimitiveObjectPair(XMLPrimitiveTypeAdapter<T> aKey, XMLTypeAdapter<R> aVal) {
        this.aKey = aKey;
        this.aVal = aVal;
    }

    @Override
    public XMLElement toXML(String name, Pair<T, R> input) {
        XMLObject rv = new XMLObject(name);
        rv.setAttr("key", aKey.toXMLContents(input.getKey()));
        rv.add(aVal.toXML("value", input.getValue()));
        return rv;
    }

    @Override
    public Pair<T, R> fromXML(XMLElement input) {
        String k = input.getAttr("key").orElseThrow(XMLSerializingException::new);
        R vVal = aVal.fromXML(input.toXMLObject().get(0));
        return new Pair<>(aKey.fromXMLContents(k), vVal);
    }

}
