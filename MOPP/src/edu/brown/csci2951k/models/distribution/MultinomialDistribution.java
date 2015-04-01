package edu.brown.csci2951k.models.distribution;

import edu.brown.csci2951k.models.data.MObject;
import edu.brown.csci2951k.models.data.MObjectSet;
import edu.brown.csci2951k.models.data.XMLPrimitiveMObjectReference;
import edu.brown.csci2951k.util.Pair;
import edu.brown.csci2951k.util.xml.XMLCollection;
import edu.brown.csci2951k.util.xml.XMLElement;
import edu.brown.csci2951k.util.xml.XMLSerializable;
import edu.brown.csci2951k.util.xml.adapters.XMLPrimitiveDouble;
import edu.brown.csci2951k.util.xml.adapters.XMLPrimitivePair;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

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
public class MultinomialDistribution implements XMLSerializable {
    
    private final MObjectSet objs;
    
    private final Map<MObject,Double> vals;

    public static MultinomialDistribution getLanguageDistribution(MObjectSet set, List<String> target) {
        return (new MultinomialDistribution(set, (MObject o) -> o.getLanguageModel().probabilityOf(target))).normalize();
    }
    
    public MObjectSet getObjects() {
        return objs;
    }

    public MultinomialDistribution(MObjectSet objs, Map<MObject,Double> vals) {
        this.objs = objs;
        this.vals = vals;
    }
    
    public MultinomialDistribution(MObjectSet objs, Function<MObject,Double> mappingFunc) {
        this.objs = objs;
        this.vals = new HashMap<>();
        
        objs.forEach((o) -> vals.put(o, mappingFunc.apply(o)));
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

    @Override
    public XMLElement toXML(String xmlObjectName) {
        return new XMLCollection(xmlObjectName, new XMLPrimitivePair(new XMLPrimitiveMObjectReference(objs), XMLPrimitiveDouble.getInstance()), vals.entrySet().stream().map(Pair::to).collect(Collectors.toList()));
    }

}
