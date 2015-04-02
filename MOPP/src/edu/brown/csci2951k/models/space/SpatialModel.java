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
import edu.brown.csci2951k.util.xml.XMLObject;
import edu.brown.csci2951k.util.xml.XMLSerializable;
import edu.brown.csci2951k.util.xml.XMLSerializingException;
import edu.brown.csci2951k.util.xml.XMLTypeAdapter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
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

    final MObjectSet objects;
    private final ConcurrentHashMap<MObject, T> locationMap = new ConcurrentHashMap<>();

    public SpatialModel(MObjectSet objects, Function<MObject, T> fn) {
        this.objects = objects;
        objects.forEach((o) -> locationMap.put(o, fn.apply(o)));
    }

    public SpatialModel(MObjectSet objects, Collection<Pair<MObject, T>> coords) {
        this.objects = objects;

        // Verify that coords has everything in objects and nothing else;
        if (!objects.verify(coords.stream().map((p) -> p.getKey()).collect(Collectors.toList()))) {
            throw new IllegalArgumentException("Coordinate set does not match objects.");
        }
        
        coords.forEach((c) -> locationMap.put(c.getKey(), c.getValue()));
    }

    public T get(MObject o) {
        return Optional.of(locationMap.get(o)).get();
    }

    public Stream<Pair<MObject, T>> stream() {
        return locationMap.entrySet().stream().map((e) -> new Pair<>(e.getKey(), e.getValue()));
    }

    public Iterator<Pair<MObject, T>> iterator() {
        return this.stream().iterator();
    }

    public void move(MObject k, T v) {
        if (!objects.contains(k)) {
            throw new IllegalArgumentException("This SpatialModel does not include this MObject.");
        }
        locationMap.put(k, v);

        fireListeners(k);
    }

    public MObjectSet getObjects() {
        return objects;
    }

    private final List<SpatialChangeListener> listeners = new LinkedList<>();

    private void fireListeners(MObject k) {
        listeners.stream().forEach((l) -> l.handleMovement(k));
    }

    public boolean addListener(SpatialChangeListener e) {
        return listeners.add(e);
    }

    public boolean removeListener(SpatialChangeListener o) {
        return listeners.remove(o);
    }

    @Override
    public XMLElement toXML(String xmlObjectName) {
        XMLObject rv = new XMLObject(xmlObjectName);

        Iterator<Pair<String, XMLElement>> itr = locationMap.entrySet().stream().map((e) -> new Pair<>(e.getKey().getId(), e.getValue().toXML("coords"))).iterator();

        while (itr.hasNext()) {
            Pair<String, XMLElement> elt = itr.next();
            rv.add(new XMLObject("element", Arrays.asList(new Pair<>("id", elt.getKey())), Arrays.asList(elt.getValue())));
        }

        return rv;
    }

    public static <R extends SpatialCoords> SpatialModel<R> fromXML(XMLElement get, XMLTypeAdapter<R> coordAdapter, MObjectSet set) {
        XMLObject o = get.toXMLObject();
        List<Pair<MObject, R>> coordMap = new LinkedList<>();
        
        for(int i = 0; i < o.size(); ++i) {
            XMLElement elt = o.get(i);
            
            if(elt == null)
                throw new IllegalStateException();
            
            String id = elt.getAttr("id").orElseThrow(() -> new XMLSerializingException("ID not found in XML source"));
            MObject obj = set.get(id).orElseThrow(() -> new XMLSerializingException("ID not found in set"));
            
            R coords = coordAdapter.fromXML(elt.toXMLObject().get("coords"));
            coordMap.add(new Pair<>(obj, coords));
        }
        
        return new SpatialModel<>(set, coordMap);
    }

}
