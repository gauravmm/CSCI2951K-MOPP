/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.ui;

import edu.brown.csci2951k.models.data.MObject;
import edu.brown.csci2951k.models.space.SpatialChangeListener;

/**
 *
 * @author Zach
 */
public class Viz2DSpatialChangeListener implements SpatialChangeListener{
    private Frm2DViz parent;

    public Viz2DSpatialChangeListener(Frm2DViz parent) {
        this.parent = parent;
    }
    
    
    @Override
    public void handleMovement(MObject objectMoved) {
        parent.onUpdatedMovement(objectMoved);
    }
    
}
