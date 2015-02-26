/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.models.language;

import edu.brown.csci2951k.models.data.MObject;
import edu.brown.csci2951k.models.data.MObjectImpl;
import edu.brown.csci2951k.models.language.ObjectLanguageModel;
import edu.brown.csci2951k.util.xml.XMLElement;
import edu.brown.csci2951k.util.xml.XMLObject;
import edu.brown.csci2951k.util.xml.XMLTypeAdapter;
import edu.brown.csci2951k.util.xml.adapters.XMLPrimitiveString;
import edu.brown.csci2951k.util.xml.adapters.XMLString;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 *
 * @author Gaurav Manek
 * @param <L> The ObjectLanguageModel type
 */
public class XMLAdapterObjectLanguageModelUnigram implements XMLTypeAdapter<ObjectLanguageModelUnigram> {

    private final static XMLAdapterObjectLanguageModelUnigram instance = new XMLAdapterObjectLanguageModelUnigram();

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
        return new ObjectLanguageModelUnigram(input.getCollectionValue(ArrayList::new, XMLString.getInstance()));
    }
    
}
