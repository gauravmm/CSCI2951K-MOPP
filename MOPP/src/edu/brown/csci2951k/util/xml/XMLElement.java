/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.util.xml;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import edu.brown.csci2951k.util.Pair;
import java.util.Optional;

/**
 * Represents an abstract class.
 *
 * @author Gaurav Manek
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class XMLElement {

    protected static final String t = "\t";
    protected static final String n = "\n";

    private final String tag;
    private final List<Pair<String, String>> attr;

    public XMLElement(String tag) {
        this.tag = tag;
        attr = new LinkedList<>();
    }
    
    protected XMLElement(String tag, Collection<Pair<String, String>> attr){
        this.tag = tag;
        this.attr = new LinkedList<>(attr);
    }

    public final String getTag() {
        return tag;
    }

    public final Optional<String> getAttr(String s) {
        for (Pair<String, String> a : attr) {
            if (a.getKey().equals(s)) {
                return Optional.of(a.getValue());
            }
        }
        return Optional.empty();
    }

    public List<Pair<String, String>> getAttrs() {
        return Collections.unmodifiableList(attr);
    }

    public final void setAttr(String s, String n) {
        ListIterator<Pair<String, String>> it = attr.listIterator();
        while (it.hasNext()) {
            Pair<String, String> a = it.next();
            if (a.getKey().equals(s)) {
                it.remove();
            }
        }
        attr.add(new Pair<>(s, n));
    }

    public XMLObject toXMLObject() {
        try {
            return (XMLObject) this;
        } catch (ClassCastException e) {
            throw new XMLSerializingException(e);
        }
    }

    public XMLPrimitive toXMLPrimitive() {
        try {
            return (XMLPrimitive) this;
        } catch (ClassCastException e) {
            throw new XMLSerializingException(e);
        }
    }

    public XMLCollection toXMLCollection() {
        try {
            return (XMLCollection) this;
        } catch (ClassCastException e) {
            throw new XMLSerializingException(e);
        }
    }

    public <T> T getPrimitiveValue(XMLPrimitiveTypeAdapter<T> adapter) {
        return this.toXMLPrimitive().get(adapter);
    }

    public <T> Collection<T> getCollectionValue(XMLCollectionFactory<T> factory, XMLTypeAdapter<T> adapter) {
        return this.toXMLCollection().get(factory, adapter);
    }

    @Override
    public final String toString() {
        return toString("");
    }

    public abstract String toString(String line_prefix);

    // Convenience methods:
    protected String tagO() {
        return tagOC(tag, attr);
    }

    protected String tagC() {
        return tagCC(tag);
    }

    protected static String tagOC(String c, List<Pair<String, String>> attrs) {
        if (attrs == null || attrs.isEmpty()) {
            return "<" + c + ">";
        } else {
            StringBuilder sB = new StringBuilder();
            sB.append("<").append(c);
            for (Pair<String, String> p : attrs) {
                sB.append(" ").append(p.getKey()).append("=\"").append(XMLAttributeEscaper.escape(p.getValue())).append("\"");
            }
            return sB.append(">").toString();
        }
    }

    protected static String tagCC(String c) {
        return "</" + c + ">";
    }

}
