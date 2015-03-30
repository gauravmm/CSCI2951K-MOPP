/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.util.iterator;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Predicate;

/**
 *
 * @author Gaurav Manek
 */
public class FilteredIterator<T> implements Iterator<T> {
    private final Iterator<T> wrapped;
    Optional<T> next;
    private final Predicate<T> p;

    public FilteredIterator(Iterator<T> wrapped, Predicate<T> p) {
        this.wrapped = wrapped;
        prepareNext();
        this.p = p;
    }

    @Override
    public boolean hasNext() {
        return next.isPresent();
    }

    @Override
    public T next() {
        T rv = next.get();
        prepareNext();
        return rv;
    }

    private void prepareNext() {
        while (wrapped.hasNext()) {
            T n = wrapped.next();
            if (p.test(n)) {
                next = Optional.of(n);
                return;
            }
        }
        next = Optional.empty();
    }
    
}
