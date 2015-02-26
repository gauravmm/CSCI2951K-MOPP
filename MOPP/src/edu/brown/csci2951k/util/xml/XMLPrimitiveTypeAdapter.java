/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.util.xml;

/**
 * Convert an element to a String.
 * @author Gaurav Manek
 */
public interface XMLPrimitiveTypeAdapter<T> {

    public String toXMLContents(T input);

    public T fromXMLContents(String input);
}
