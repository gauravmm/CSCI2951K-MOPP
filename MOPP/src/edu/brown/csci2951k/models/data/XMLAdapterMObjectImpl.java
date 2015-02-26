/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.models.data;

import edu.brown.csci2951k.models.language.ObjectLanguageModel;
import edu.brown.csci2951k.util.xml.XMLElement;
import edu.brown.csci2951k.util.xml.XMLObject;
import edu.brown.csci2951k.util.xml.XMLTypeAdapter;
import edu.brown.csci2951k.util.xml.adapters.XMLPrimitiveString;
import java.util.Optional;

/**
 *
 * @author Gaurav Manek
 * @param <L> The ObjectLanguageModel type
 */
public class XMLAdapterMObjectImpl<L extends ObjectLanguageModel> implements XMLTypeAdapter<MObject> {

    private final XMLTypeAdapter<L> langModAdapter;

    public XMLAdapterMObjectImpl(XMLTypeAdapter<L> langModAdapter) {
        this.langModAdapter = langModAdapter;
    }
    
    @Override
    public XMLElement toXML(String name, MObject input) {
        return input.toXML(name);
    }

    @Override
    public MObject fromXML(XMLElement input) {
        XMLObject o = input.toXMLObject();
        
        String id = o.get("id").getPrimitiveValue(XMLPrimitiveString.getInstance());
        L lM = langModAdapter.fromXML(Optional.of(o.get("language_model")).get());
        return new MObjectImpl(id, lM);
    }
    
}
