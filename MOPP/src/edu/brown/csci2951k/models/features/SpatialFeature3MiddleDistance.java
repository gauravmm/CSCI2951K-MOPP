/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.models.features;

import edu.brown.csci2951k.models.data.MObject;
import edu.brown.csci2951k.models.space.Coords2D;
import edu.brown.csci2951k.models.space.SpatialModel;
import java.util.List;

/**
 *
 * @author Gaurav Manek
 */
public class SpatialFeature3MiddleDistance extends SpatialFeature<Coords2D> {

    @Override
    public boolean bindsTo(int numChildren) {
        return numChildren == 3;
    }

    @Override
    protected Double checkedApply(SpatialModel<Coords2D> model, List<MObject> objs) {
        Coords2D p = model.get(objs.get(0));
        Coords2D p0 = model.get(objs.get(1));
        Coords2D p1 = model.get(objs.get(2));

        Coords2D midpt = new Coords2D((p0.getX() + p1.getX()) / 2, (p0.getY() + p1.getY()) / 2);

        return p.getDistanceTo(midpt);
    }

    @Override
    public String getName() {
        return "midpt_distance";
    }

}
