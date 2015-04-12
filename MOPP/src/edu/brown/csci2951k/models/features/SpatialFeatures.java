 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.models.features;

import edu.brown.csci2951k.models.space.Coords2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Gaurav Manek
 */
public final class SpatialFeatures {

    private static List<SpatialFeature<Coords2D>> features2d = null;

    public static synchronized List<SpatialFeature<Coords2D>> get2DFeatures() {
        if (features2d == null) {
            List<SpatialFeature<Coords2D>> rawl = Arrays.asList(
                    new SpatialFeature2Norm(),
                    new SpatialFeatureDX(),
                    new SpatialFeatureDY(),
                    new SpatialFeature3PerpendicularDistance(),
                    new SpatialFeature3PerpendicularDistanceFrac(),
                    new SpatialFeature3ProjectionBetweenPoints());
            ArrayList<SpatialFeature<Coords2D>> rv = new ArrayList<>();
            rv.addAll(rawl);
            rawl.stream().map((f) -> new SpatialFeatureOrdinalWrapper<>(f)).forEachOrdered(rv::add);
            
            features2d = rv;
        }

        return features2d;
    }
    
    public static List<SpatialFeature<Coords2D>> get2DFeatures(int child) {
        return get2DFeatures().stream().filter((f) -> f.bindsTo(child)).collect(Collectors.toList());
    }

}
