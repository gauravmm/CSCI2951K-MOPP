package edu.brown.csci2951k.models.features;

import edu.brown.csci2951k.models.data.MObject;
import edu.brown.csci2951k.models.data.MObjectSet;
import edu.brown.csci2951k.models.space.SpatialCoords;
import edu.brown.csci2951k.models.space.SpatialModel;
import edu.brown.csci2951k.util.Pair;
import edu.brown.csci2951k.util.iterator.FilteredIterator;
import edu.brown.csci2951k.util.iterator.PowerSetIterator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Gaurav Manek
 */
public class SpatialFeatureOrdinalWrapper<T extends SpatialCoords> extends SpatialFeature<T> {

    private final SpatialFeature<T> wrapped;

    public SpatialFeatureOrdinalWrapper(SpatialFeature<T> wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public boolean bindsTo(int numChildren) {
        return wrapped.bindsTo(numChildren);
    }

    @Override
    protected Double checkedApply(SpatialModel<T> model, List<MObject> objs) {
        MObjectSet objects = model.getObjects();
        MObject main = objs.get(0);
        List<MObject> ordinal = objs.subList(1, objs.size());

        Iterator<List<MObject>> psItr = new FilteredIterator<>(new PowerSetIterator<>(objs, objs.size()), (List<MObject> l) -> {
            if (!l.get(0).equals(main)) {
                return false;
            }

            for (int i = 0; i < l.size(); ++i) {
                for (int j = i + 1; j < l.size(); ++j) {
                    if (l.get(i).equals(l.get(j))) {
                        return false;
                    }
                }
            }

            return true;
        });
        List<List<MObject>> poss = new ArrayList<>();
        psItr.forEachRemaining(poss::add);

        Iterator<Pair<List<MObject>, Double>> collect = poss.stream()
                .map((o) -> new Pair<>(o, wrapped.apply(model, o)))
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
