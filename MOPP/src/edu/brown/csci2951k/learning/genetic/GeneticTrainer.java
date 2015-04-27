/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.learning.genetic;

import edu.brown.csci2951k.corpus.Corpus;
import edu.brown.csci2951k.models.data.MObject;
import edu.brown.csci2951k.models.features.SpatialFeatures;
import edu.brown.csci2951k.models.meaning.MeaningConverter;
import edu.brown.csci2951k.models.meaning.MeaningNode;
import edu.brown.csci2951k.models.meaning.Meanings;
import edu.brown.csci2951k.models.space.Coords2D;
import edu.brown.csci2951k.models.space.SpatialModel;
import edu.brown.csci2951k.models.space.normalized.Coords2DNormalizer;
import edu.brown.csci2951k.models.space.normalized.SpatialCoordsNormalizer;
import edu.brown.csci2951k.util.Pair;
import edu.brown.csci2951k.util.support.TokenizedSentence;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import ontopt.pen.EarleyParser;
import ontopt.pen.SemanticNode;

/**
 *
 * @author Gaurav Manek
 */
public final class GeneticTrainer {

    public static void train(List<Corpus<Coords2D>> cList, EarleyParser eP, String prefix) {

        // Prepare the training data.
        ConcurrentHashMap<Meanings.PP, TrainingExamples> trainingData = prepare(cList, eP);

        trainingData.forEach((pp, ex) -> {
            try {
                Files.write(Paths.get(String.format("%s_%s.csv", prefix, pp.toString())), ex.toCSV(true), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
            } catch (IOException ex1) {
                System.err.println(ex1.toString());
            }
        });
    }

    private static ConcurrentHashMap<Meanings.PP, TrainingExamples> prepare(List<Corpus<Coords2D>> cList, EarleyParser eP) {
        ConcurrentHashMap<Meanings.PP, TrainingExamples> rv = new ConcurrentHashMap<>();
        Arrays.stream(Meanings.PP.values()).forEach((Meanings.PP pp) -> rv.put(pp, new TrainingExamples<>(SpatialFeatures.get2DFeatures(pp.getNumChildren()))));

        SpatialCoordsNormalizer<Coords2D, Coords2D> normalizer = new Coords2DNormalizer();

        for (Corpus c : cList) {
            Iterator<Pair<String, MObject>> itr = c.getCorpus().iterator();
            SpatialModel<Coords2D> spModel = normalizer.normalize(c.getSM());

            while (itr.hasNext()) {
                Pair<String, MObject> ex = itr.next();

                ArrayList<SemanticNode> parses = eP.parseSentence(new TokenizedSentence(ex.getKey()));
                if (parses.isEmpty()) {
                    System.err.println("Cannot parse ".concat(ex.getKey()));
                    continue;
                }
                MeaningNode mean = MeaningConverter.convert(parses.get(0));
                TrainingExamples examples = rv.get(mean.getPP().get());

                examples.add(spModel, c.getObj(), mean.getPriors(c.getObj()), ex.getValue());
            }
        }

        return rv;
    }

}
