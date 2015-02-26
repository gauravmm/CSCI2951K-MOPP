/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.util.xml;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Convenience class for creating XMLCollections of Serializable things.
 *
 * @author Gaurav Manek
 */
public final class XMLCollectionSerializable<T extends XMLSerializable> extends XMLCollection<T> {

    public XMLCollectionSerializable(String tag, Collection<T> data) {
        super(tag);

        this.contents = new LinkedList<>();
        for (T elt : data) {
            contents.add(elt.toXML());
        }
    }

}
