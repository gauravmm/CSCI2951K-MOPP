/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.models.data;

import edu.brown.csci2951k.util.xml.XMLCollectionSerializable;
import edu.brown.csci2951k.util.xml.XMLElement;
import edu.brown.csci2951k.util.xml.XMLSerializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 *
 * @author Gaurav Manek
 */
public class MObjectSet implements Set<MObject>, XMLSerializable {

    private final Set<MObject> mset;

    public MObjectSet(Collection<MObject> mset) {
        if (mset.isEmpty()) {
            throw new IllegalArgumentException("MObjectSet cannot be empty.");
        }

        this.mset = Collections.unmodifiableSet(new HashSet<>(mset));

        if (mset.size() != this.mset.size()) {
            throw new IllegalArgumentException("MObjectSet cannot include duplicate objects.");
        }
    }

    @Override
    public int size() {
        return mset.size();
    }

    @Override
    public boolean isEmpty() {
        return mset.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return mset.contains(o);
    }

    @Override
    public Iterator<MObject> iterator() {
        return mset.iterator();
    }

    @Override
    public boolean containsAll(Collection<?> clctn) {
        return mset.containsAll(clctn);
    }

    @Override
    public boolean equals(Object o) {
        return mset.equals(o);
    }

    @Override
    public int hashCode() {
        return mset.hashCode();
    }

    @Override
    public Spliterator<MObject> spliterator() {
        return mset.spliterator();
    }

    @Override
    public Stream<MObject> stream() {
        return mset.stream();
    }

    @Override
    public Stream<MObject> parallelStream() {
        return mset.parallelStream();
    }

    @Override
    public void forEach(Consumer<? super MObject> cnsmr) {
        mset.forEach(cnsmr);
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("Read-only set.");
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        throw new UnsupportedOperationException("Read-only set.");
    }

    @Override
    public boolean add(MObject e) {
        throw new UnsupportedOperationException("Read-only set.");
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Read-only set.");
    }

    @Override
    public boolean addAll(Collection<? extends MObject> clctn) {
        throw new UnsupportedOperationException("Read-only set.");
    }

    @Override
    public boolean retainAll(Collection<?> clctn) {
        throw new UnsupportedOperationException("Read-only set.");
    }

    @Override
    public boolean removeAll(Collection<?> clctn) {
        throw new UnsupportedOperationException("Read-only set.");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Read-only set.");
    }

    public boolean verify(Collection<MObject> collect) {
        List<MObject> m = new LinkedList<>(collect);

        for (MObject t : mset) {
            if (!m.remove(t)) {
                return false;
            }
        }

        return m.isEmpty();
    }

    @Override
    public XMLElement toXML(String xmlObjectName) {
        return new XMLCollectionSerializable(xmlObjectName, this.mset);
    }

}
