/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.util.xml;

import edu.brown.csci2951k.util.Pair;
import java.util.List;

/**
 *
 * @author Gaurav Manek
 */
public class XMLPrimitive extends XMLElement {

    String contents;

    public <T> XMLPrimitive(String tag, XMLPrimitiveTypeAdapter<T> adapter, T data) {
        super(tag);
        this.contents = adapter.toXMLContents(data);
    }

    <T> XMLPrimitive(String tag, List<Pair<String, String>> attrs, String contents) {
        super(tag, attrs);
        this.contents = contents;
    }
    
    <T> XMLPrimitive(String tag, String contents) {
        super(tag);
        this.contents = contents;
    }

    public <T> T get(XMLPrimitiveTypeAdapter<T> adapter) {
        return adapter.fromXMLContents(contents);
    }

    @Override
    @Deprecated
    public XMLCollection toXMLCollection() {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public XMLPrimitive toXMLPrimitive() {
        return super.toXMLPrimitive(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Deprecated
    public XMLObject toXMLObject() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString(String line_prefix) {
        return line_prefix + tagO() + contents + tagC() + n;
    }
}
