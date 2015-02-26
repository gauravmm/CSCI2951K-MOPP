/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.models.space;

import edu.brown.csci2951k.models.data.MObject;
import edu.brown.csci2951k.models.data.MObjectSet;
import edu.brown.csci2951k.util.Pair;
import edu.brown.csci2951k.util.xml.XMLElement;
import edu.brown.csci2951k.util.xml.XMLSerializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Gaurav Manek
 * @param <T> The type of coordinate system being used.
 */
public class SpatialModel<T extends SpatialCoords> implements XMLSerializable {
    
    private final MObjectSet objects;
    private final ConcurrentHashMap<MObject, T> locationMap = new ConcurrentHashMap<>();

    public SpatialModel(MObjectSet objects, Function<MObject, T> fn) {
        this.objects = objects;
        objects.forEach((o) -> locationMap.put(o, fn.apply(o)));
    }
    
    protected SpatialModel(MObjectSet objects, Collection<Pair<MObject, T>> coords) {
        this.objects = objects;
        
        // Verify that coords has everything in objects and nothing else;
        if(!objects.verify(coords.stream().map((p) -> p.getKey()).collect(Collectors.toList())))
            throw new IllegalArgumentException("Coordinate set does not match objects.");
    }
    
    @Override
    public XMLElement toXML() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public Stream<Pair<MObject, T>> stream() {
        return locationMap.entrySet().stream().map((e) -> new Pair<>(e.getKey(), e.getValue()));
    }

    public Iterator<Pair<MObject, T>> iterator() {
        return this.stream().iterator();
    }

    public void move(MObject k, T v) {
        if(!objects.contains(k)) {
            throw new IllegalArgumentException("This SpatialModel does not include this MObject.");
        }
        locationMap.put(k, v);
        
        fireListeners(k);
    }

    
    private final List<SpatialChangeListener> listeners = new LinkedList<>();
    
    private void fireListeners(MObject k) {
        listeners.stream().forEach((l) -> l.handleMovement(k));
    }

    public boolean addListener(SpatialChangeListener e) {
        return listeners.add(e);
    }

    public boolean removeListener(Object o) {
        return listeners.remove(o);
    }
    
}
