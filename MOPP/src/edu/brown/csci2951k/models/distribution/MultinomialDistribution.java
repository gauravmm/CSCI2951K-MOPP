package edu.brown.csci2951k.models.distribution;

import edu.brown.csci2951k.models.data.MObject;
import edu.brown.csci2951k.models.data.MObjectSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Gaurav Manek
 * @param <T>
 */
public class MultinomialDistribution {
    
    private final MObjectSet objs;
    
    private final Map<MObject,Double> vals;

    public MObjectSet getObjects() {
        return objs;
    }

    public MultinomialDistribution(MObjectSet objs, Map<MObject,Double> vals) {
        this.objs = objs;
        this.vals = vals;
    }

    public Set<Map.Entry<MObject, Double>> get() {
        return Collections.unmodifiableSet(vals.entrySet());
    }

    public Double get(MObject o) {
        return vals.get(o);
    }
    
    public MultinomialDistribution map(BiFunction<MObject, Double, Double> fn) {
        Map<MObject, Double> rv = new HashMap<>();
        vals.forEach((k, v) -> rv.put(k, fn.apply(k, v)));
        return new MultinomialDistribution(objs, rv);
    }
    
    public MultinomialDistribution normalize() {
        double sum = vals.entrySet().stream().mapToDouble(ke -> ke.getValue()).sum();
        return this.map((k, v) -> v/sum);
    }

    @Override
    public String toString() {
        StringBuilder rv = new StringBuilder();
        
        rv.append("MultinomialDistribution{\n");
        for(Map.Entry<MObject, Double> ke : vals.entrySet()) {
            rv.append("\t").append(ke.getKey().toString()).append("\t: ").append(ke.getValue()).append("\n");
        }
        rv.append('}');
        
        return rv.toString();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.objs);
        hash = 47 * hash + Objects.hashCode(this.vals);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MultinomialDistribution other = (MultinomialDistribution) obj;
        if (!Objects.equals(this.objs, other.objs)) {
            return false;
        }
        return Objects.equals(this.vals, other.vals);
    }

}
