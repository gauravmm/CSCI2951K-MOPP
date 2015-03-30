/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.models.features;

import edu.brown.csci2951k.models.data.MObject;
import edu.brown.csci2951k.models.space.SpatialCoords;
import edu.brown.csci2951k.models.space.SpatialModel;
import edu.brown.csci2951k.util.Pair;
import java.util.List;

/**
 *
 * @author Gaurav Manek
 */
public class WeightedFeature<T extends SpatialCoords> extends SpatialFeature<T> {

    private final List<Pair<SpatialFeature<T>, Double>> featureWeights;

    public WeightedFeature(List<Pair<SpatialFeature<T>, Double>> featureWeights) {
        this.featureWeights = featureWeights;
    }
    
    @Override
    public boolean bindsTo(int numChildren) {
        // Return true iff all features bind to numChildren children.
        return featureWeights.stream().map((p) -> p.getKey().bindsTo(numChildren)).reduce(Boolean.TRUE, (b0, b1) -> b0 && b1);
    }

    @Override
    protected Double checkedApply(SpatialModel<T> model, List<MObject> objs) {
        // Perform the mapping to all features, and multiply by the value.
        return featureWeights.stream().mapToDouble((p) -> p.getKey().apply(model, objs) * p.getValue()).sum();
    }
    
}
