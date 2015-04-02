/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.models.space.normalized;

import edu.brown.csci2951k.models.data.MObject;
import edu.brown.csci2951k.models.space.Coords2D;
import edu.brown.csci2951k.models.space.SpatialModel;
import edu.brown.csci2951k.util.Pair;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @author Gaurav Manek
 */
public class Coords2DNormalizer implements SpatialCoordsNormalizer<Coords2D, Coords2D> {

    @Override
    public SpatialModel<Coords2D> normalize(SpatialModel<Coords2D> coords) {
        final DoubleSummaryStatistics statX = coords.stream().mapToDouble((d) -> d.getValue().getX()).summaryStatistics();
        final DoubleSummaryStatistics statY = coords.stream().mapToDouble((d) -> d.getValue().getY()).summaryStatistics();
        
        final double scale = Math.max(statX.getMax() - statX.getMin(), statY.getMax() - statY.getMin());
        
        Function<Coords2D, Coords2D> n = (c2d) -> new Coords2D((c2d.getX() - statX.getMin())/scale, (c2d.getY() - statY.getMin())/scale);
        
        List<Pair<MObject, Coords2D>> collect = coords.stream().map((p) -> new Pair<>(p.getKey(), n.apply(p.getValue()))).collect(Collectors.toList());
        
        return new SpatialModel<>(coords.getObjects(), collect);
    }
    
}
