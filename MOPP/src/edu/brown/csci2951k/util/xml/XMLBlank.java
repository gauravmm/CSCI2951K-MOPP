/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.util.xml;

/**
 * Represents the lack of any data to serialize.
 *
 * @author Gaurav Manek
 */
public class XMLBlank extends XMLElement {

    public XMLBlank() {
        super("");
    }

    @Override
    public String toString(String line_prefix) {
        return "";
    }

}
