/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.util.xml;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Gaurav Manek
 */
class XMLRaw extends XMLElement {

    private String contents;
    private final List<XMLElement> children;

    XMLRaw(String tag, String contents) {
        super(tag);
        this.contents = contents;
        this.children = null;
    }

    XMLRaw(String tag, List<XMLElement> children) {
        super(tag);
        this.children = children;
        this.contents = null;
    }

    public XMLRaw(String tag) {
        super(tag);
        this.children = new LinkedList<>();
    }

    public void add(XMLElement e) {
        children.add(e);
    }

    public void setText(String s) {
        contents = s;
    }

    @Override
    public XMLCollection toXMLCollection() {
        return new XMLCollection(this.getTag(), this.getAttrs(), children);
    }

    @Override
    public XMLPrimitive toXMLPrimitive() {
        return new XMLPrimitive(this.getTag(), this.getAttrs(), contents);
    }

    @Override
    public XMLObject toXMLObject() {
        return new XMLObject(this.getTag(), this.getAttrs(), children);
    }

    @Override
    public String toString(String line_prefix) {
        return line_prefix + tagO() + n + contents + tagC() + n;
    }

}
