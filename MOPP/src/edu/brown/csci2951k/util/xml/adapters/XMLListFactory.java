/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.util.xml.adapters;

import edu.brown.csci2951k.util.xml.XMLCollectionFactory;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gaurav Manek
 */
public class XMLListFactory<T> implements XMLCollectionFactory<T> {

    @Override
    public List<T> getNewCollection() {
        return new ArrayList<>();
    }

}
