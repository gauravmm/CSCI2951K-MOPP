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
public class SpatialFeature3PerpendicularDistance extends SpatialFeature<Coords2D> {

    @Override
    public boolean bindsTo(int numChildren) {
        return numChildren == 3;
    }

    @Override
    protected Double checkedApply(SpatialModel<Coords2D> model, List<MObject> objs) {
        // http://geomalgorithms.com/a02-_lines.html
        Coords2D p = model.get(objs.get(0));
        Coords2D p0 = model.get(objs.get(1));
        Coords2D p1 = model.get(objs.get(2));

        return ((p0.getY() - p1.getY()) * p.getX() + (p1.getX() - p0.getX()) * p.getY() + (p0.getX() * p1.getY() - p1.getX() * p0.getY())) / p1.getDistanceTo(p0);
    }

    @Override
    public String getName() {
        return "perpendicular_distance";
    }

}
