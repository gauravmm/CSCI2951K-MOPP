/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.models.meaning;

import edu.brown.csci2951k.learning.genetic.TrainingExamples;
import edu.brown.csci2951k.util.iterator.PowerSetIterator;
import edu.brown.csci2951k.util.iterator.FilteredIterator;
import edu.brown.csci2951k.models.data.MObject;
import edu.brown.csci2951k.models.data.MObjectSet;
import edu.brown.csci2951k.models.distribution.MultinomialDistribution;
import edu.brown.csci2951k.models.features.SpatialFeature;
import edu.brown.csci2951k.models.space.SpatialCoords;
import edu.brown.csci2951k.models.space.SpatialModel;
import edu.brown.csci2951k.util.xml.XMLCollection;
import edu.brown.csci2951k.util.xml.XMLElement;
import edu.brown.csci2951k.util.xml.XMLObject;
import edu.brown.csci2951k.util.xml.XMLTypeAdapter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.DoubleSummaryStatistics;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 * @author Gaurav Manek
 */
public class NonTerminalNode implements MeaningNode {

    private final Meanings.PP type;
    private final List<MeaningNode> children;

    protected NonTerminalNode(Meanings.PP type, Collection<MeaningNode> children) {
        this.type = type;
        this.children = new ArrayList<>(children);
    }

    @Override
    public <S extends SpatialCoords> MultinomialDistribution apply(SpatialModel<S> model, MObjectSet objectSet, Function<Meanings.PP, SpatialFeature<S>> featureMapping) {
        SpatialFeature feature = featureMapping.apply(type);
        if (!feature.bindsTo(children.size())) {
            throw new RuntimeException("This feature cannot be applied to these many children.");
        }

        List<MultinomialDistribution> dists = children.stream().map((c) -> c.apply(model, objectSet, featureMapping)).collect(Collectors.toList());

        Iterator<List<MObject>> psItr = new FilteredIterator<>(new PowerSetIterator<>(objectSet, children.size()), (List<MObject> l) -> {
            MObject t = l.get(0);
            Boolean anySimilarObjects = l.subList(1, l.size()).stream().map(t::equals).reduce(Boolean.FALSE, (b0, b1) -> b0 || b1);
            return !anySimilarObjects;
        });
        ConcurrentHashMap<MObject, Double> conditional = new ConcurrentHashMap<>();
        objectSet.forEach((o) -> conditional.put(o, 0.0));

        while (psItr.hasNext()) {
            List<MObject> n = psItr.next();
            MObject t = n.get(0);
            // Apply the feature to the objects to get a score:
            Double featureScore = feature.apply(model, n);
            // Calculate the independent probability of all items on the right hand side:
            double weight = IntStream.range(1, n.size()).mapToDouble((i) -> dists.get(i).get(n.get(i))).reduce(1.0, (a, b) -> a * b);
            // Mulitply the feature score by the weight:
            conditional.compute(t, (MObject obj, Double v) -> v + featureScore * weight);
        }

        // Now we normalize the summed feature-scores into a probability.
        DoubleSummaryStatistics stats = conditional.values().stream().mapToDouble((d) -> d).summaryStatistics();

        // By convention, the leftmost child is the target. We marginalize w.r.t. that
        MultinomialDistribution target = dists.get(0);
        MultinomialDistribution rv = target.map((obj, prior) -> {
            return prior * (conditional.get(obj) - stats.getMin()) / (stats.getMax() - stats.getMin());
        });

        return rv;
    }

    @Override
    public String toString() {
        return String.format("(%s %s)", type.toString(), children.stream().map((c) -> c.toString()).collect(Collectors.joining(" ")));
    }

    @Override
    public XMLElement toXML(String name, MObjectSet objectSet) {
        XMLCollection rv = new XMLCollection(name, new XMLTypeAdapter<MeaningNode>() {

            @Override
            public XMLElement toXML(String name, MeaningNode input) {
                return input.toXML(name, objectSet);
            }

            @Override
            public MeaningNode fromXML(XMLElement input) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        }, children);

        rv.setAttr("type", type.name());

        return rv;
    }

    public Meanings.PP getType() {
        return type;
    }
    
    @Override
    public MultinomialDistribution getPrior(MObjectSet objectSet) {
        return children.get(0).getPrior(objectSet);
    }

    @Override
    public List<MultinomialDistribution> getPriors(MObjectSet objectSet) {
        return children.stream().map((o) -> o.getPrior(objectSet)).collect(Collectors.toList());
    }

    @Override
    public Optional<Meanings.PP> getPP() {
        return Optional.of(type);
    }

}
