/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.models.features;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Gaurav Manek
 */
public final class SpatialFeatures {
    
    public static List<SpatialFeature> getFeatures(){
        return Arrays.asList(new SpatialFeature2Norm());
    }
    
}
