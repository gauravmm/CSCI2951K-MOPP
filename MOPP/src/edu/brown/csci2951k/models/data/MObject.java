/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.models.data;

import edu.brown.csci2951k.models.language.ObjectLanguageModel;
import edu.brown.csci2951k.util.xml.XMLSerializable;

/**
 *
 * @author Gaurav Manek
 */
public interface MObject extends XMLSerializable {

    /**
     * Get the language model associated with this object.
     *
     * @return An ObjectLanguageModel that computes the probability that any
     * noun phrase refers to this object.
     */
    public ObjectLanguageModel getLanguageModel();

    /**
     * A unique identifier that represents this object.
     * @return A string that is globally unique that refers to this object.
     */
    public String getId();
    

}
