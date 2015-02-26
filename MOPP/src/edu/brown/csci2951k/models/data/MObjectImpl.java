/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.models.data;

import edu.brown.csci2951k.models.language.ObjectLanguageModel;
import edu.brown.csci2951k.util.xml.XMLElement;
import edu.brown.csci2951k.util.xml.XMLObject;
import edu.brown.csci2951k.util.xml.XMLPrimitive;
import edu.brown.csci2951k.util.xml.adapters.XMLPrimitiveString;
import java.util.Objects;

/**
 *
 * @author Gaurav Manek
 */
public class MObjectImpl implements MObject {

    private final String id;
    private final ObjectLanguageModel langMod;

    public MObjectImpl(String id, ObjectLanguageModel langMod) {
        this.id = id;
        this.langMod = langMod;
    }

    @Override
    public ObjectLanguageModel getLanguageModel() {
        return this.langMod;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public XMLElement toXML(String xmlObjectName) {
        XMLObject rv = new XMLObject(xmlObjectName);
        rv.add(new XMLPrimitive("id", XMLPrimitiveString.getInstance(), id));
        rv.add(langMod.toXML("language_model"));
        return rv;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.id);
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
        final MObjectImpl other = (MObjectImpl) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
