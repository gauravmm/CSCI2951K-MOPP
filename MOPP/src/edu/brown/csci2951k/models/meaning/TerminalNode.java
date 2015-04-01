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
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @author Gaurav Manek
 */
public final class TerminalNode implements MeaningNode {

    private final List<String> simpleNP;

    public TerminalNode(List<String> simpleNP) {
        this.simpleNP = simpleNP;
    }

    @Override
    public <S extends SpatialCoords> MultinomialDistribution apply(SpatialModel<S> model, MObjectSet objectSet, Function<Meanings.PP, SpatialFeature> featureMapping) {
        return MultinomialDistribution.getLanguageDistribution(objectSet, simpleNP);
    }

    @Override
    public String toString() {
        return simpleNP.stream().collect(Collectors.joining(" ", "\"", "\""));
    }

    @Override
    public XMLElement toXML(String name, MObjectSet objectSet) {
        return MultinomialDistribution.getLanguageDistribution(objectSet, simpleNP).toXML(name);
    }
}
