/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.models.language;

import edu.brown.csci2951k.util.Pair;
import edu.brown.csci2951k.util.xml.XMLCollection;
import edu.brown.csci2951k.util.xml.XMLElement;
import edu.brown.csci2951k.util.xml.adapters.XMLPrimitiveDouble;
import edu.brown.csci2951k.util.xml.adapters.XMLPrimitiveInteger;
import edu.brown.csci2951k.util.xml.adapters.XMLPrimitivePair;
import edu.brown.csci2951k.util.xml.adapters.XMLPrimitiveString;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 *
 * @author Gaurav Manek
 */
public class ObjectLanguageModelUnigram extends ObjectLanguageModel {

    private final HashMap<String, Integer> wordCounts = new HashMap<>();
    private final Integer objCountTotal;
    private final Double alpha;
  
    public ObjectLanguageModelUnigram(Collection<Pair<String,Integer>> words, Integer objCountTotal, Double alpha) {
        words.forEach((w) -> wordCounts.put(w.getKey(), w.getValue()));
        this.objCountTotal = objCountTotal;
        this.alpha = alpha;
    }

    @Override
    protected Double probOfImpl(String in) {
        return Arrays.stream(in.split("\\s+")).mapToDouble((w) -> {
            return alpha * (1.0 / objCountTotal) + (1 - alpha) * (wordCounts.containsKey(w) ? (1.0 / wordCounts.get(w)) : 0);
        }).sum();
    }

    @Override
    public XMLElement toXML(String xmlObjectName) {
        XMLPrimitivePair<String, Integer> xmlPPAdapter = new XMLPrimitivePair<>(XMLPrimitiveString.getInstance(), XMLPrimitiveInteger.getInstance());
        
        XMLCollection rv = new XMLCollection(xmlObjectName, xmlPPAdapter, wordCounts.entrySet().stream()
                .map((a) -> new Pair<>(a.getKey(), a.getValue()))
                .collect(Collectors.toList()));
        rv.setAttr("object_count", XMLPrimitiveInteger.getInstance().toXMLContents(objCountTotal));
        rv.setAttr("alpha", XMLPrimitiveDouble.getInstance().toXMLContents(alpha));
        
        return rv;
    }

}
