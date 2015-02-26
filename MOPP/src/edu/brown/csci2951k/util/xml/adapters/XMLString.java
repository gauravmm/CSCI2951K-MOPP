/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.util.xml.adapters;

import edu.brown.csci2951k.util.xml.XMLElement;
import edu.brown.csci2951k.util.xml.XMLPrimitive;
import edu.brown.csci2951k.util.xml.XMLTypeAdapter;

/**
 *
 * @author Gaurav Manek
 */
public class XMLString implements XMLTypeAdapter<String> {

    private static final XMLString instance = new XMLString();

    public static XMLString getInstance() {
        return instance;
    }

    private XMLString() {
    }

    @Override
    public XMLElement toXML(String name, String input) {
        return new XMLPrimitive(name, XMLPrimitiveString.getInstance(), input);
    }

    @Override
    public String fromXML(XMLElement input) {
        return input.getPrimitiveValue(XMLPrimitiveString.getInstance());
    }
}
