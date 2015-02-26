/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.cs.cs032.util.xml.adapters;

import edu.brown.csci2951k.util.xml.XMLPrimitiveTypeAdapter;
import edu.brown.cs.cs032.util.xml.XMLSerializingException;

/**
 * Double wrapper for XMLPrimitive. Singleton class.
 * @author Gaurav Manek
 */
public class XMLPrimitiveInteger implements XMLPrimitiveTypeAdapter<Integer> {

    private static final XMLPrimitiveInteger instance = new XMLPrimitiveInteger();

    public static XMLPrimitiveInteger getInstance() {
        return instance;
    }
    
    public static XMLPrimitiveInteger get() {
        return getInstance();
    }

    @Override
    public String toXMLContents(Integer input) {
        return input.toString();
    }

    @Override
    public Integer fromXMLContents(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new XMLSerializingException(e);
        }
    }
}
