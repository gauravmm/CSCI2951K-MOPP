/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.util;

import java.util.List;
import java.util.ListIterator;

/**
 * A spectacularly inelegant String escaper. Singleton class.
 *
 * @author Gaurav Manek
 */
public class StringEscaper {

    private final List<Pair<String, String>> escape;

    /**
     * Escape Strings using a set of predefined mapping rules.
     *
     * In XML->String, these are applied, in the order provided, Key->Value. In
     * String->XML, these are applied, in reverse order, Value->Key.
     *
     * @param mapping The list of mapping instructions.
     */
    public StringEscaper(List<Pair<String, String>> mapping) {
        escape = mapping;
    }

    public String escape(String input) {
        ListIterator<Pair<String, String>> it = escape.listIterator();
        while (it.hasNext()) {
            Pair<String, String> esc = it.next();
            input = input.replace(esc.getKey(), esc.getValue());
        }
        return input;
    }

    public String unescape(String input) {
        ListIterator<Pair<String, String>> it = escape.listIterator(escape.size());
        while (it.hasPrevious()) {
            Pair<String, String> esc = it.previous();
            input = input.replace(esc.getValue(), esc.getKey());
        }
        return input;
    }

}
