/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.models.language;

import edu.brown.csci2951k.util.Pair;
import edu.brown.csci2951k.util.xml.XMLElement;
import edu.brown.csci2951k.util.xml.XMLSerializingException;
import edu.brown.csci2951k.util.xml.XMLTypeAdapter;
import edu.brown.csci2951k.util.xml.adapters.XMLPrimitiveDouble;
import edu.brown.csci2951k.util.xml.adapters.XMLPrimitiveInteger;
import edu.brown.csci2951k.util.xml.adapters.XMLPrimitivePair;
import edu.brown.csci2951k.util.xml.adapters.XMLPrimitiveString;
import edu.brown.csci2951k.util.xml.adapters.XMLString;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Gaurav Manek
 */
public class XMLAdapterObjectLanguageModelUnigram implements XMLTypeAdapter<ObjectLanguageModelUnigram> {

    private final static XMLAdapterObjectLanguageModelUnigram instance = new XMLAdapterObjectLanguageModelUnigram();
    private double defaultAlpha = 0.1;

    public double getDefaultAlpha() {
        return defaultAlpha;
    }

    public void setDefaultAlpha(double defaultAlpha) {
        this.defaultAlpha = defaultAlpha;
    }

    private XMLAdapterObjectLanguageModelUnigram() {
    }

    public static XMLAdapterObjectLanguageModelUnigram getInstance() {
        return instance;
    }

    @Override
    public XMLElement toXML(String name, ObjectLanguageModelUnigram input) {
        return input.toXML(name);
    }

    @Override
    public ObjectLanguageModelUnigram fromXML(XMLElement input) {
        Collection<Pair<String, Integer>> collectionValue = input.getCollectionValue(ArrayList::new, new XMLPrimitivePair<String, Integer>(XMLPrimitiveString.getInstance(), XMLPrimitiveInteger.getInstance()));
        return new ObjectLanguageModelUnigram(collectionValue, 
                XMLPrimitiveInteger.getInstance().fromXMLContents(input.getAttr("object_count").orElseThrow(XMLSerializingException::new)),
                XMLPrimitiveDouble.getInstance().fromXMLContents(input.getAttr("alpha").orElseGet(() -> XMLPrimitiveDouble.getInstance().toXMLContents(defaultAlpha))));
    }
    
}
