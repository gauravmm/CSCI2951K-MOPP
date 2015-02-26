/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.models.language;

import edu.brown.csci2951k.util.xml.XMLSerializable;
import java.util.function.Function;

/**
 *
 * @author Gaurav Manek
 */
public abstract class ObjectLanguageModel implements Function<String,Double>, XMLSerializable {

    @Override
    public final Double apply(String t) {
        return this.probabilityOf(t);
    }
    
    public final Double probabilityOf(String in) {
        Double d = probOfImpl(in);
        if(d < 0.0 || d > 1.0) {
            throw new AssertionError("result of probabilityOf out of bounds.");
        }
        return d;
    }

    protected abstract Double probOfImpl(String in);
    
}
