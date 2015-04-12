/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.models.features;

import edu.brown.csci2951k.models.data.MObject;
import edu.brown.csci2951k.models.space.Coords2D;
import edu.brown.csci2951k.models.space.SpatialCoords;
import edu.brown.csci2951k.models.space.SpatialModel;
import java.util.List;

/**
 *
 * @author Gaurav Manek
 */
public class SpatialFeatureDY extends SpatialFeature<Coords2D> {

    @Override
    public boolean bindsTo(int numChildren) {
        return numChildren == 2;
    }

    @Override
    protected Double checkedApply(SpatialModel<Coords2D> model, List<MObject> objs) {
        MObject o1 = objs.get(0);
        MObject o2 = objs.get(1);

        return model.get(o1).getY() - model.get(o2).getY();
    }

    @Override
    public String getName() {
        return "delta_y";
    }

}
