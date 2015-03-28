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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Gaurav Manek
 */
public class ObjectLanguageModelUnigram extends ObjectLanguageModel {

    private static final HashMap<String, Integer> wordCounts = new HashMap<>();
    private static Integer wordCountTotal = 0;
    private static Integer objCountTotal = 0;
    private static final double alpha = 0.1;
    private static final Object wordsMutex = new Object();

    private static void updateWords(Collection<String> words) {
        //synchronized (wordsMutex) {
        for (String s : words) {
            wordCounts.compute(s, (String w, Integer v) -> v == null ? 1 : (v + 1));
            wordCountTotal++;
        }
        objCountTotal++;
        //}
    }

    public static Collection<String> getWords() {
        return Collections.unmodifiableCollection(wordCounts.keySet());
    }

    private final Set<String> words;
    private static final double correctPoints = 0.5;
    private static final double wrongPoints = -0.15;

    private static double logistic(double in) {
        return 1.0 / (1.0 + Math.exp(0.5 - in));
    }

    public ObjectLanguageModelUnigram(Collection<String> words) {
        this.words = new HashSet<>(words);
        updateWords(words);
    }

    @Override
    protected Double probOfImpl(String in) {
        //synchronized (wordsMutex) {
        return Arrays.stream(in.split("\\s+")).mapToDouble((w) -> {
            return alpha * (1.0 / objCountTotal) + (1 - alpha) * (words.contains(w) ? (1.0 / wordCounts.get(w)) : 0);
        }).sum();
        //}
    }

    @Override
    public XMLElement toXML(String xmlObjectName) {
        return new XMLCollection(xmlObjectName, XMLString.getInstance(), words);
    }

}
