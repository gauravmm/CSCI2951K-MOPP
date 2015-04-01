package edu.brown.csci2951k.models.features;

import edu.brown.csci2951k.models.data.MObject;
import edu.brown.csci2951k.models.data.MObjectSet;
import edu.brown.csci2951k.models.features.SpatialFeature;
import edu.brown.csci2951k.models.space.Coords2D;
import edu.brown.csci2951k.models.space.SpatialCoords;
import edu.brown.csci2951k.models.space.SpatialModel;
import edu.brown.csci2951k.util.Pair;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Gaurav Manek
 */
public class SpatialFeature2OrdinalWrapper<T extends SpatialCoords> extends SpatialFeature<T> {

    private final SpatialFeature<T> wrapped;

    public SpatialFeature2OrdinalWrapper(SpatialFeature<T> wrapped) {
        if (!wrapped.bindsTo(2)) {
            throw new RuntimeException();
        }

        this.wrapped = wrapped;
    }

    @Override
    public boolean bindsTo(int numChildren) {
        return numChildren == 2;
    }

    @Override
    protected Double checkedApply(SpatialModel<T> model, List<MObject> objs) {
        MObjectSet objects = model.getObjects();
        MObject main = objs.get(0);
        MObject ordinal = objs.get(1);

        Iterator<Pair<MObject, Double>> collect = objects.stream()
                .map((o) -> new Pair<>(o, wrapped.apply(model, Arrays.asList(main, o))))
                .sorted((p1, p2) -> p1.getValue().compareTo(p2.getValue())).iterator();

        int i = 1;
        while (collect.hasNext()) {
            if (collect.next().equals(ordinal)) {
                break;
            }
            i++;
        }

        return i * 1.0;
    }

}
