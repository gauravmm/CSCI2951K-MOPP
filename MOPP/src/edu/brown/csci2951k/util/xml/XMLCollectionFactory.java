/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.util.xml;

import java.util.Collection;

/**
 *
 * @author Gaurav Manek
 */
public interface XMLCollectionFactory<T> {

    public Collection<T> getNewCollection();
}
