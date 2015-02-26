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
public class XMLPrimitiveDouble implements XMLPrimitiveTypeAdapter<Double> {

    private static final XMLPrimitiveDouble instance = new XMLPrimitiveDouble();

    public static XMLPrimitiveDouble getInstance() {
        return instance;
    }
    
    public static XMLPrimitiveDouble get() {
        return getInstance();
    }

    @Override
    public String toXMLContents(Double input) {
        return input.toString();
    }

    @Override
    public Double fromXMLContents(String input) {
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            throw new XMLSerializingException(e);
        }
    }

}
