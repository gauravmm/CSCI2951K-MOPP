/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.util.iterator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Gaurav Manek
 */
public class PowerSetIterator<T> implements Iterator<List<T>> {
    private int[] curr;
    private final int num;
    List<T> l;
    boolean done = false;

    public PowerSetIterator(Collection<T> osrc, int num) {
        l = new ArrayList<>(osrc);
        this.num = num;
        curr = new int[num];
        for (int i = 0; i < num; ++i) {
            curr[i] = 0;
        }
    }

    @Override
    public boolean hasNext() {
        return !done;
    }

    @Override
    public List<T> next() {
        if (done) {
            throw new RuntimeException();
        }
        // Prepare the current set;
        ArrayList<T> rv = new ArrayList<>();
        for (int i = 0; i < num; ++i) {
            rv.add(l.get(curr[i]));
        }
        int carry = 1;
        for (int i = num - 1; i >= 0 && carry == 1; --i) {
            curr[i] += carry;
            if (curr[i] >= l.size()) {
                curr[i] = 0;
                carry = 1;
            } else {
                carry = 0;
            }
        }
        done = carry == 1;
        return rv;
    }
    
}
