/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.models.features;

import edu.brown.csci2951k.models.data.MObject;
import edu.brown.csci2951k.models.space.SpatialCoords;
import edu.brown.csci2951k.models.space.SpatialModel;
import java.util.List;

/**
 *
 * @author Gaurav Manek
 */
public abstract class SpatialFeature<T extends SpatialCoords> {

    public abstract boolean bindsTo(int numChildren);

    public final Double apply(SpatialModel<T> model, List<MObject> objs) {
        if (!this.bindsTo(objs.size())) {
            throw new RuntimeException();
        }
        return this.checkedApply(model, objs);
    }

    protected abstract Double checkedApply(SpatialModel<T> model, List<MObject> objs);

}
