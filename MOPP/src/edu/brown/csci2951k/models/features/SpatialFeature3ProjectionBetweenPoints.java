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
public class SpatialFeature3ProjectionBetweenPoints extends SpatialFeature<Coords2D> {

    @Override
    public boolean bindsTo(int numChildren) {
        return numChildren == 3;
    }

    @Override
    protected Double checkedApply(SpatialModel<Coords2D> model, List<MObject> objs) {
        Coords2D p = model.get(objs.get(0));
        Coords2D v1 = model.get(objs.get(1));
        Coords2D v2 = model.get(objs.get(2));

        // http://www.sunshine2k.de/coding/java/PointOnLine/PointOnLine.html
        
        // get dotproduct |e1| * |e2|
        Coords2D e1 = new Coords2D(v2.getX() - v1.getX(), v2.getY() - v1.getY());
        double recArea = Math.abs(dotProduct(e1, e1));
        // dot product of |e1| * |e2|
        Coords2D e2 = new Coords2D(p.getX() - v1.getX(), p.getY() - v1.getY());
        double val = dotProduct(e1, e2);
        return (val > 0 && val < recArea)?1.0:-1.0;
    }

    private double dotProduct(Coords2D e1, Coords2D e2) {
        return (e1.getX()*e2.getX() + e1.getY()*e2.getY());
    }

    @Override
    public String getName() {
        return "projection_between_points";
    }

}
