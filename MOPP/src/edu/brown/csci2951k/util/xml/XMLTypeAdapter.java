/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.util.xml;

/**
 * Convert an element to an XMLElement tree.
 * 
 * @author Gaurav Manek
 */
public interface XMLTypeAdapter<T> {
   
    public XMLElement toXML(String name, T input);

    public T fromXML(XMLElement input);
}
