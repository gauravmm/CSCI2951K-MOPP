/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.models.language;

import edu.brown.csci2951k.util.xml.XMLCollection;
import edu.brown.csci2951k.util.xml.XMLElement;
import edu.brown.csci2951k.util.xml.adapters.XMLString;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Gaurav Manek
 */
public class ObjectLanguageModelUnigram extends ObjectLanguageModel {

    private final Set<String> words;
    private static final double correctPoints = 0.5;
    private static final double wrongPoints = -0.15;

    private static double logistic(double in) {
        return 1.0 / (1.0 + Math.exp(0.5 - in));
    }

    public ObjectLanguageModelUnigram(Collection<String> words) {
        this.words = new HashSet<>(words);
    }

    @Override
    protected Double probOfImpl(String in) {
        return Arrays.stream(in.split("\\s+")).mapToDouble((w) -> words.contains(w) ? correctPoints : wrongPoints).sum();
    }

    @Override
    public XMLElement toXML(String xmlObjectName) {
        return new XMLCollection(xmlObjectName, XMLString.getInstance(), words);
    }

}
