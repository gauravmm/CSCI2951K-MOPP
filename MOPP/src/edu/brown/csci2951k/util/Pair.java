/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.util;

import java.util.Map;

/**
 *
 * @author Gaurav Manek
 */
public class Pair<X, Y> implements Map.Entry<X, Y> {

    public static <R, T> Pair<R, T> to(Map.Entry<R, T> e) {
        return new Pair<>(e.getKey(), e.getValue());
    }

    private final X key;
    private final Y value;

    public Pair(X key, Y value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public X getKey() {
        return key;
    }

    @Override
    public Y getValue() {
        return value;
    }

    @Override
    public Y setValue(Y value) {
        throw new UnsupportedOperationException("These are immutable");
    }

}
