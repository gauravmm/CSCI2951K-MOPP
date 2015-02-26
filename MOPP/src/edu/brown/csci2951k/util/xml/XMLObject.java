/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.util.xml;

import edu.brown.csci2951k.util.Pair;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Gaurav Manek
 */
public class XMLObject extends XMLElement {

    List<XMLElement> elt;

    public XMLObject(String tag) {
        super(tag);
        elt = new LinkedList<>();
    }
    
    XMLObject(String tag, List<Pair<String, String>> attr, List<XMLElement> children) {
        super(tag, attr);
        elt = children;
    }

    XMLObject(String tag, List<XMLElement> children) {
        super(tag);
        elt = children;
    }

    public void add(XMLSerializable e) {
        elt.add(e.toXML());
    }

    public void add(XMLElement e) {
        elt.add(e);
    }

    public XMLElement get(String tag, int index) {
        Iterator<XMLElement> itr = elt.iterator();
        while (itr.hasNext()) {
            XMLElement e = itr.next();
            if (e.getTag().equals(tag)) {
                if (index > 0) {
                    index--;
                } else {
                    return e;
                }
            }
        }
        throw new IndexOutOfBoundsException("Can't find the " + index + " instance of " + tag);
    }
    
    public int size(String tag){
        Iterator<XMLElement> itr = elt.iterator();
        int rv = 0;
        while (itr.hasNext()) {
            XMLElement e = itr.next();
            if (e.getTag().equals(tag)) {
                rv++;
            }
        }
        return rv;
    }
    
    public int size() {
        return elt.size();
    }

    public XMLElement get(String tag) {
        return this.get(tag, 0);
    }
    
    public XMLElement get(int idx) {
        return elt.get(idx);
    }

    @Override
    @Deprecated
    public XMLCollection<?> toXMLCollection() {
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
        for (XMLElement e : elt) {
            rv.append(e.toString(ind + t));
        }
        return rv.append(ind).append(tagC()).append(n).toString();
    }

}
