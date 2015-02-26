/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.util.xml;

import edu.brown.csci2951k.util.Pair;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Gaurav Manek
 */
public class XMLCollection<T> extends XMLElement {

    protected List<XMLElement> contents;

    protected XMLCollection(String tag) {
        super(tag);
    }

    public XMLCollection(List<XMLElement> contents, String tag) {
        super(tag);
        this.contents = contents;
    }

    XMLCollection(String tag, List<Pair<String, String>> attr, List<XMLElement> contents) {
        super(tag, attr);
        this.contents = contents;
    }

    XMLCollection(String tag, List<XMLElement> contents) {
        super(tag);
        this.contents = contents;
    }

    public XMLCollection(String tag, XMLTypeAdapter<T> adapter, Collection<T> data) {
        super(tag);

        this.contents = new LinkedList<>();
        for (T elt : data) {
            contents.add(adapter.toXML(elt));
        }
    }

    public <T> Collection<T> get(XMLCollectionFactory<T> factory, XMLTypeAdapter<T> adapter) {
        Collection<T> rv = factory.getNewCollection();
        for (XMLElement c  : contents) {
            rv.add(adapter.fromXML(c));
        }
        return rv;
    }

    @Override
    @Deprecated
    public XMLCollection toXMLCollection() {
        return super.toXMLCollection(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Deprecated
    public XMLPrimitive toXMLPrimitive() {
        return super.toXMLPrimitive(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Deprecated
    public XMLObject toXMLObject() {
        return super.toXMLObject(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toString(String ind) {
        StringBuilder rv = new StringBuilder();
        rv.append(ind).append(tagO()).append(n);
        for (XMLElement e : contents) {
            rv.append(e.toString(ind + t));
        }
        return rv.append(ind).append(tagC()).append(n).toString();
    }

}
