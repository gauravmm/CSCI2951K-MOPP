/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.models.space;

import edu.brown.csci2951k.util.xml.XMLSerializable;

/**
 * Cartesian points. Immutable.
 * @author Gaurav Manek
 */
public interface SpatialCoords extends XMLSerializable {
    public int getDimensions();
    
    public double get(int dim);
}
