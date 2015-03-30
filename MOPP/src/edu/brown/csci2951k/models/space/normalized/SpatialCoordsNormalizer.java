/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.models.space.normalized;

import edu.brown.csci2951k.models.space.SpatialCoords;
import edu.brown.csci2951k.models.space.SpatialModel;

/**
 *
 * @author Gaurav Manek
 */
public interface SpatialCoordsNormalizer<T extends SpatialCoords, R extends SpatialCoords> {

    SpatialModel<R> normalize(SpatialModel<R> coords);

}
