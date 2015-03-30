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
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @author Gaurav Manek
 */
public class NonTerminalNode implements MeaningNode {

    private final Meanings.PP type;
    private final Collection<MeaningNode> children;

    protected NonTerminalNode(Meanings.PP type, Collection<MeaningNode> children) {
        this.type = type;
        this.children = children;
    }

    @Override
    public <S extends SpatialCoords> MultinomialDistribution apply(SpatialModel<S> model, MObjectSet objectSet, Function<Meanings.PP, SpatialFeature> featureMapping) {
        SpatialFeature apply = featureMapping.apply(type);
        if (!apply.bindsTo(children.size())) {
            throw new RuntimeException("This feature cannot be applied to these many children.");
        }

        List<MultinomialDistribution> dists = children.stream().map((c) -> c.apply(model, objectSet, featureMapping)).collect(Collectors.toList());

        // ???? I'm not so sure anymore.
        // Apply each permutation of the distributions to the feature, and come
        // up with a final distribution.
        throw new UnsupportedOperationException("Not yet.");
        // return apply.apply(model, );
    }

    @Override
    public String toString() {
        return String.format("(%s %s)", type.toString(), children.stream().map((c) -> c.toString()).collect(Collectors.joining(" ")));
    }

}
