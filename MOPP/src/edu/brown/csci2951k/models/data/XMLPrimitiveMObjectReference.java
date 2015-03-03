/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.models.data;

import edu.brown.csci2951k.util.xml.XMLPrimitiveTypeAdapter;
import edu.brown.csci2951k.util.xml.XMLSerializingException;
import edu.brown.csci2951k.util.xml.adapters.XMLPrimitiveString;

/**
 *
 * @author Gaurav Manek
 */
public class XMLPrimitiveMObjectReference implements XMLPrimitiveTypeAdapter<MObject> {

    private final MObjectSet set;
    private final XMLPrimitiveString str = XMLPrimitiveString.getInstance();

    public XMLPrimitiveMObjectReference(MObjectSet set) {
        this.set = set;
    }

    @Override
    public String toXMLContents(MObject input) {
        return str.toXMLContents(input.getId());
    }

    @Override
    public MObject fromXMLContents(String input) {
        return set.get(str.fromXMLContents(input)).orElseThrow(() -> new XMLSerializingException("Cannot find MObject in set."));
    }

}
