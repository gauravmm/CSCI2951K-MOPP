/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.util.xml.adapters;

import edu.brown.csci2951k.util.Pair;
import edu.brown.csci2951k.util.StringEscaper;
import edu.brown.csci2951k.util.xml.XMLPrimitiveTypeAdapter;
import java.util.LinkedList;
import java.util.List;

/**
 * A spectacularly inelegant String escaper. Singleton class.
 *
 * @author Gaurav Manek
 */
public class XMLPrimitiveString implements XMLPrimitiveTypeAdapter<String> {

    private static final XMLPrimitiveString instance = new XMLPrimitiveString();

    public static XMLPrimitiveString getInstance() {
        return instance;
    }

    // In XML->String, these are applied, in order, Key->Value
    // In String->XML, these are applied, in reverse order, Value->Key
    private final StringEscaper stringEscaper;

    private XMLPrimitiveString() {
        List<Pair<String, String>> escape = new LinkedList<>();
        escape.add(new Pair<>("&", "&amp;"));
        escape.add(new Pair<>("<", "&lt;"));
        escape.add(new Pair<>(">", "&gt;"));
        stringEscaper = new StringEscaper(escape);
    }

    @Override
    public String toXMLContents(String input) {
        return stringEscaper.escape(input);
    }

    @Override
    public String fromXMLContents(String input) {
        return stringEscaper.unescape(input);
    }

}
