/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.models.meaning;

import edu.brown.csci2951k.models.data.MObjectSet;
import edu.brown.csci2951k.models.distribution.MultinomialDistribution;
import edu.brown.csci2951k.models.features.SpatialFeature;
import edu.brown.csci2951k.models.space.SpatialCoords;
import edu.brown.csci2951k.models.space.SpatialModel;
import edu.brown.csci2951k.util.xml.XMLElement;
import java.util.function.Function;

/**
 *
 * @author Gaurav Manek
 */
public interface MeaningNode {

    <S extends SpatialCoords> MultinomialDistribution apply(SpatialModel<S> model, MObjectSet objectSet, Function<Meanings.PP, SpatialFeature<S>> featureMapping);

    XMLElement toXML(String name, MObjectSet objectSet);
    
    @Override
    public String toString();
}
