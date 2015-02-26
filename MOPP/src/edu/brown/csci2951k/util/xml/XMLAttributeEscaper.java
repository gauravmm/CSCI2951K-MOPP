/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.util.xml;

import edu.brown.csci2951k.util.Pair;
import edu.brown.csci2951k.util.StringEscaper;
import java.util.LinkedList;
import java.util.List;

/**
 * A spectacularly inelegant String escaper. Singleton class.
 *
 * @author Gaurav Manek
 */
public class XMLAttributeEscaper {

    private static final XMLAttributeEscaper instance = new XMLAttributeEscaper();

    private final StringEscaper stringEscaper;

    private XMLAttributeEscaper() {
        List<Pair<String, String>> escape = new LinkedList<>();
        escape.add(new Pair<>("&", "&amp;"));
        escape.add(new Pair<>("<", "&lt;"));
        escape.add(new Pair<>(">", "&gt;"));
        escape.add(new Pair<>("'", "&#39;"));
        escape.add(new Pair<>("\"", "&#34;"));
        stringEscaper = new StringEscaper(escape);
    }

    private String e(String input) {
        return stringEscaper.escape(input);
    }

    private String u(String input) {
        return stringEscaper.unescape(input);
    }

    public static String escape(String input) {
        return instance.e(input);
    }

    public static String unescape(String input) {
        return instance.u(input);
    }

    

}
