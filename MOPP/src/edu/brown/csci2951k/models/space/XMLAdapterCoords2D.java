/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.models.space;

import edu.brown.csci2951k.util.xml.XMLElement;
import edu.brown.csci2951k.util.xml.XMLSerializingException;
import edu.brown.csci2951k.util.xml.XMLTypeAdapter;
import edu.brown.csci2951k.util.xml.adapters.XMLPrimitiveDouble;

/**
 *
 * @author Gaurav Manek
 */
public class XMLAdapterCoords2D implements XMLTypeAdapter<Coords2D> {

    private final static XMLAdapterCoords2D instance = new XMLAdapterCoords2D();

    public static XMLAdapterCoords2D getInstance() {
        return instance;
    }

    private XMLAdapterCoords2D() {
    }

    @Override
    public XMLElement toXML(String name, Coords2D input) {
        return input.toXML(name);
    }

    @Override
    public Coords2D fromXML(XMLElement input) {
        XMLPrimitiveDouble d = XMLPrimitiveDouble.getInstance();

        Double x = d.fromXMLContents(input.getAttr("x").orElseThrow(XMLSerializingException::new));
        Double y = d.fromXMLContents(input.getAttr("y").orElseThrow(XMLSerializingException::new));
        
        return new Coords2D(x, y);
    }
}
