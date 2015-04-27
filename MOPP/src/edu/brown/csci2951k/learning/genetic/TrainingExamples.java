/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.learning.genetic;

import edu.brown.csci2951k.models.data.MObject;
import edu.brown.csci2951k.models.data.MObjectSet;
import edu.brown.csci2951k.models.distribution.MultinomialDistribution;
import edu.brown.csci2951k.models.features.SpatialFeature;
import edu.brown.csci2951k.models.space.SpatialCoords;
import edu.brown.csci2951k.models.space.SpatialModel;
import edu.brown.csci2951k.util.Pair;
import edu.brown.csci2951k.util.iterator.FilteredIterator;
import edu.brown.csci2951k.util.iterator.PowerSetIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 * @author Gaurav Manek
 */
public class TrainingExamples<S extends SpatialCoords> {

    private final List<SpatialFeature<S>> features;
    private final List<Pair<List<Double>, Boolean>> values = Collections.synchronizedList(new ArrayList<>());

    public TrainingExamples(List<SpatialFeature<S>> features) {
        this.features = features;
    }

    public void add(SpatialModel<S> model, MObjectSet objectSet, List<MultinomialDistribution> dists, MObject correctObj) {
        Iterator<List<MObject>> psItr = new FilteredIterator<>(new PowerSetIterator<>(objectSet, dists.size()), (List<MObject> l) -> {
            for (int i = 0; i < l.size(); ++i) {
                for (int j = i + 1; j < l.size(); ++j) {
                    if (l.get(i).equals(l.get(j))) {
                        return false;
                    }
                }
            }
            return true;
        });

        ConcurrentHashMap<MObject, List<Double>> joint = new ConcurrentHashMap<>();
        objectSet.forEach((o) -> joint.put(o, features.stream().map((f) -> 0.0).collect(Collectors.toList())));

        while (psItr.hasNext()) {
            List<MObject> n = psItr.next();
            MObject t = n.get(0);

            // Apply the feature to the objects to get a score:
            List<Double> featureValues = features.stream().map((f) -> f.apply(model, n)).collect(Collectors.toList());

            // Calculate the independent probability of all items on the right hand side:
            double weight = IntStream.range(1, n.size()).mapToDouble((i) -> dists.get(i).get(n.get(i))).reduce(1.0, (a, b) -> a * b);

            // Mulitply the feature score by the weight:
            joint.compute(t, (MObject obj, List<Double> v) -> IntStream.range(0, features.size())
                    .mapToObj((i) -> v.get(i) + featureValues.get(i) * weight).collect(Collectors.toList()));
        }

        MultinomialDistribution priors = dists.get(0);

        joint.forEach((MObject obj, List<Double> v) -> {
            values.add(new Pair<>(v.stream().map((d) -> d * priors.get(obj)).collect(Collectors.toList()), correctObj.equals(obj)));
        });
    }

    public List<String> toCSV(boolean header) {
        List<String> rv = new ArrayList<>();
        if (header) {
            rv.add(features.stream().map((f) -> f.getName()).collect(Collectors.joining(", ", "", ", correct")));
        }
        values.forEach((p) -> rv.add(p.getKey().stream().map((d) -> d.toString()).collect(Collectors.joining(", ", "",", " + (p.getValue()?"1":"0")))));
        
        return rv;
    }

}
