/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.util.xml;

/**
 *
 * @author Gaurav Manek
 */
public interface XMLSerializable {

    /**
     * Create an XMLElement that represents the contents of this object, with an
     * assigned name.
     *
     * @param xmlObjectName The name to assign the root XMLElement of this
     * object.
     * @return An XMLElement instance that contains enough information to
     * reconstruct this object.
     */
    public XMLElement toXML(String xmlObjectName);

}
