/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.models.space;

import edu.brown.csci2951k.util.xml.XMLElement;

/**
 *
 * @author Gaurav Manek
 */
public class Coords2D implements SpatialCoords {

    private final double x;
    private final double y;

    public Coords2D(double x, double y) {
        if(!(Double.isFinite(x) && Double.isFinite(y)))
            throw new IllegalArgumentException("X and Y must be finite.");
        
        this.x = x;
        this.y = y;
    }

    @Override
    public int getDimensions() {
        return 2;
    }

    @Override
    public double get(int dim) {
        if (dim == 1) {
            return y;
        } else if(dim == 0){
            return x;
        }
        
        throw new IllegalArgumentException("dim out of bounds");
    }

    @Override
    public XMLElement toXML() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
