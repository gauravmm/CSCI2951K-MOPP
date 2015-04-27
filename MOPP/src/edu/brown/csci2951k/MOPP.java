/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k;

import edu.brown.csci2951k.corpus.Corpus;
import edu.brown.csci2951k.corpus.XMLCorpusAdapter;
import edu.brown.csci2951k.models.data.MObject;
import edu.brown.csci2951k.models.distribution.MultinomialDistribution;
import edu.brown.csci2951k.models.features.SpatialFeature;
import edu.brown.csci2951k.models.features.SpatialFeature2Norm;
import edu.brown.csci2951k.models.features.SpatialFeature3MiddleDistance;
import edu.brown.csci2951k.models.features.SpatialFeature3PerpendicularDistance;
import edu.brown.csci2951k.models.features.SpatialFeature3PerpendicularDistanceFrac;
import edu.brown.csci2951k.models.features.SpatialFeature3ProjectionBetweenPoints;
import edu.brown.csci2951k.models.features.SpatialFeatureOrdinalWrapper;
import edu.brown.csci2951k.models.features.SpatialFeatureDX;
import edu.brown.csci2951k.models.features.SpatialFeatureDY;
import edu.brown.csci2951k.models.features.WeightedFeature;
import edu.brown.csci2951k.models.language.ObjectLanguageModel;
import edu.brown.csci2951k.models.language.ObjectLanguageModelUnigram;
import edu.brown.csci2951k.models.language.XMLAdapterObjectLanguageModelUnigram;
import edu.brown.csci2951k.models.meaning.MeaningConverter;
import edu.brown.csci2951k.models.meaning.MeaningNode;
import edu.brown.csci2951k.models.meaning.Meanings;
import edu.brown.csci2951k.models.meaning.NonTerminalNode;
import edu.brown.csci2951k.models.space.Coords2D;
import edu.brown.csci2951k.models.space.SpatialCoords;
import edu.brown.csci2951k.models.space.SpatialModel;
import edu.brown.csci2951k.models.space.XMLAdapterCoords2D;
import edu.brown.csci2951k.models.space.normalized.Coords2DNormalizer;
import edu.brown.csci2951k.models.space.normalized.SpatialCoordsNormalizer;
import edu.brown.csci2951k.util.Pair;
import edu.brown.csci2951k.util.support.TokenizedSentence;
import edu.brown.csci2951k.util.xml.XMLObject;
import edu.brown.csci2951k.util.xml.XMLParser;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import ontopt.pen.EarleyParser;
import ontopt.pen.GrammarException;
import ontopt.pen.SemanticNode;
import ontopt.pen.Sentence;

/**
 *
 * @author Gaurav Manek, Zachary Loery
 */
public class MOPP {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            CorpusParsing.main(args);
        } catch (Exception ex) {
            Logger.getLogger(MOPP.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.exit(0);

        if (args.length != 2) {
            System.out.println("Usage: java -cp MOPP.jar FILE_TRAIN FILE_TEST");
            System.exit(1);
        }

        EarleyParser eP;
        try {
            eP = new EarleyParser("data/MOPP_grammar.txt");
        } catch (GrammarException ex) {
            System.err.println("Cannot construct EarleyParser. ".concat(ex.getMessage()));
            System.exit(2);
            return;
        }

        XMLCorpusAdapter<Coords2D, ObjectLanguageModelUnigram> xmlCorpusAdapter = new XMLCorpusAdapter<>(XMLAdapterCoords2D.getInstance(), XMLAdapterObjectLanguageModelUnigram.getInstance());

        List<Corpus<Coords2D>> corpusTrain;
        List<Corpus<Coords2D>> corpusTest;
        try {
            corpusTrain = loadCorpus(xmlCorpusAdapter, Files.readAllLines(Paths.get(args[0])));
            corpusTest = loadCorpus(xmlCorpusAdapter, Files.readAllLines(Paths.get(args[1])));
        } catch (IOException ex) {
            System.err.println("Cannot open input corpus. ".concat(ex.getMessage()));
            System.exit(2);
            return;
        }

        HashMap<Meanings.PP, Double> correct = new HashMap<>();
        HashMap<Meanings.PP, Double> total = new HashMap<>();

        for (Meanings.PP pp : Meanings.PP.values()) {
            correct.put(pp, 0.0);
            total.put(pp, 0.0);
        }

        //Function<Meanings.PP, SpatialFeature<Coords2D>> mapping = new LearnedMapping();
        Function<Meanings.PP, SpatialFeature<Coords2D>> mapping = new HandTunedMapping();

        // Train:
        for (Corpus c : corpusTrain) {
            //processCorpus(c, eP, mapping, correct, total);
        }
        // Test:
        for (Corpus c : corpusTest) {
            //processCorpusPrior(c, eP, correct, total);
            //processCorpusRandom(c, eP, correct, total);
            //processCorpus(c, eP, mapping, correct, total);
            processCorpusUnigram(c, eP, correct, total);
        }

        try {
            Thread.sleep(100L);
        } catch (InterruptedException ex) {
            Logger.getLogger(MOPP.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (Meanings.PP pp : Meanings.PP.values()) {
            System.out.format("%s\t%.7f\t%.7f\t%.3f\n", pp, total.get(pp), correct.get(pp), correct.get(pp) / 1.0 / total.get(pp));
        }

    }

    private static <R extends SpatialCoords, O extends ObjectLanguageModel> List<Corpus<R>> loadCorpus(XMLCorpusAdapter<R, O> adapter, List<String> file) {
        XMLObject parseXML = XMLParser.parseXML(file.stream().collect(Collectors.joining("\n")));
        return IntStream.range(0, parseXML.size()).mapToObj((i) -> adapter.fromXML(parseXML.get(i))).collect(Collectors.toList());
    }

    private static void processCorpus(Corpus c, EarleyParser eP, Function<Meanings.PP, SpatialFeature<Coords2D>> mapping, HashMap<Meanings.PP, Double> correct, HashMap<Meanings.PP, Double> total) {
        Iterator<Pair<String, MObject>> itr = c.getCorpus().iterator();

        SpatialCoordsNormalizer<Coords2D, Coords2D> normalizer = new Coords2DNormalizer();

        while (itr.hasNext()) {
            Pair<String, MObject> cp = itr.next();

            Sentence sen = new TokenizedSentence(cp.getKey());
            ArrayList<SemanticNode> parses = eP.parseSentence(sen);
            MeaningNode convert = MeaningConverter.convert(parses.get(0));

            MultinomialDistribution dist = convert.apply(normalizer.normalize(c.getSM()), c.getObj(), mapping);

            Double correctProb = dist.get(cp.getValue());
            int numGreater = dist.get().stream().mapToInt((p) -> (p.getValue() >= correctProb) ? 1 : 0).sum();
            if (numGreater == 1) {
                correct.compute(((NonTerminalNode) convert).getType(), (k, v) -> v + 1);
            }

            System.err.println(cp.getValue());
            System.err.println(cp.getKey());
            System.err.println(dist);
            System.err.format("%d\t%s\n", numGreater, convert.toString());
            System.err.println();

            total.compute(((NonTerminalNode) convert).getType(), (k, v) -> v + 1);
        }
    }

    private static void processCorpusRandom(Corpus c, EarleyParser eP, HashMap<Meanings.PP, Double> correct, HashMap<Meanings.PP, Double> total) {
        Iterator<Pair<String, MObject>> itr = c.getCorpus().iterator();

        while (itr.hasNext()) {
            Pair<String, MObject> cp = itr.next();

            Sentence sen = new TokenizedSentence(cp.getKey());
            ArrayList<SemanticNode> parses = eP.parseSentence(sen);
            MeaningNode convert = MeaningConverter.convert(parses.get(0));

            // System.err.format("%d\t%s\n", numGreater, convert.toString());
            correct.compute(((NonTerminalNode) convert).getType(), (k, v) -> v + 1.00 / c.getObj().size());
            total.compute(((NonTerminalNode) convert).getType(), (k, v) -> v + 1);
        }
    }

    private static void processCorpusPrior(Corpus c, EarleyParser eP, HashMap<Meanings.PP, Double> correct, HashMap<Meanings.PP, Double> total) {
        Iterator<Pair<String, MObject>> itr = c.getCorpus().iterator();

        while (itr.hasNext()) {
            Pair<String, MObject> cp = itr.next();

            Sentence sen = new TokenizedSentence(cp.getKey());
            ArrayList<SemanticNode> parses = eP.parseSentence(sen);
            MeaningNode convert = MeaningConverter.convert(parses.get(0));

            // For Prior:
            MultinomialDistribution prior = convert.getPrior(c.getObj());

            Double correctProb = prior.get(cp.getValue());
            int numGreater = prior.get().stream().mapToInt((p) -> (p.getValue() >= correctProb) ? 1 : 0).sum();
            if (numGreater == 1) {
                // correct.compute(((NonTerminalNode) convert).getType(), (k, v) -> v + correctProb);
            }

            // System.err.format("%d\t%s\n", numGreater, convert.toString());
            correct.compute(((NonTerminalNode) convert).getType(), (k, v) -> v + correctProb);
            total.compute(((NonTerminalNode) convert).getType(), (k, v) -> v + 1);
        }
    }

    private static void processCorpusUnigram(Corpus c, EarleyParser eP, HashMap<Meanings.PP, Double> correct, HashMap<Meanings.PP, Double> total) {
        Iterator<Pair<String, MObject>> itr = c.getCorpus().iterator();

        while (itr.hasNext()) {
            Pair<String, MObject> cp = itr.next();

            Sentence sen = new TokenizedSentence(cp.getKey());
            ArrayList<SemanticNode> parses = eP.parseSentence(sen);
            MeaningNode convert = MeaningConverter.convert(parses.get(0));

            // For Prior:
            MultinomialDistribution unigram = MultinomialDistribution.getLanguageDistribution(c.getObj(), Arrays.asList(cp.getKey().toLowerCase().split("[\\W]")));

            Double correctProb = unigram.get(cp.getValue());
            int numGreater = unigram.get().stream().mapToInt((p) -> (p.getValue() >= correctProb) ? 1 : 0).sum();
            if (numGreater == 1) {
                // correct.compute(((NonTerminalNode) convert).getType(), (k, v) -> v + correctProb);
            }

            // System.err.format("%d\t%s\n", numGreater, convert.toString());
            correct.compute(((NonTerminalNode) convert).getType(), (k, v) -> v + correctProb);
            total.compute(((NonTerminalNode) convert).getType(), (k, v) -> v + 1);
        }
    }

    private static class LearnedMapping implements Function<Meanings.PP, SpatialFeature<Coords2D>> {

        private HashMap<Meanings.PP, SpatialFeature<Coords2D>> map = new HashMap<>();

        public LearnedMapping() {
            super();

            map.put(Meanings.PP.NEAR, getWV(new double[]{-.0478e-3, -.0182e-3, -.1092e-3, .0412e-3, .1064e-3, .0847e-3}));
            map.put(Meanings.PP.LEFT, getWV(new double[]{-.1325e-3, -.0285e-3, .0418e-3, .0155e-3, .0321e-3, .0241e-3}));
            map.put(Meanings.PP.RIGHT, getWV(new double[]{.0229e-3, .0335e-3, -.0468e-3, .0306e-3, .0495e-3, .0427e-3}));
            map.put(Meanings.PP.FRONT, getWV(new double[]{.0120e-3, .0305e-3, -.0363e-3, .0356e-3, .0291e-3, .0471e-3}));
            map.put(Meanings.PP.BETWEEN, new WeightedFeature(Arrays.asList(
                    new Pair<>(new SpatialFeature3PerpendicularDistance(), -0.0140e-3),
                    new Pair<>(new SpatialFeature3ProjectionBetweenPoints(), -0.0060e-3),
                    new Pair<>(new SpatialFeature3PerpendicularDistanceFrac(), -0.0284e-3),
                    new Pair<>(new SpatialFeature3MiddleDistance(), 0.2220e-3),
                    new Pair<>(w(new SpatialFeature3PerpendicularDistance()), 0.4630e-3),
                    new Pair<>(w(new SpatialFeature3ProjectionBetweenPoints()), 0.0484e-3),
                    new Pair<>(w(new SpatialFeature3PerpendicularDistanceFrac()), 0.0393e-3),
                    new Pair<>(w(new SpatialFeature3MiddleDistance()), 0.0305e-3))));
        }

        private SpatialFeature<Coords2D> getWV(double[] c) {
            return new WeightedFeature(Arrays.asList(
                    new Pair<>(new SpatialFeatureDX(), c[0]),
                    new Pair<>(new SpatialFeatureDY(), c[1]),
                    new Pair<>(new SpatialFeature2Norm(), c[2]),
                    new Pair<>(w(new SpatialFeatureDX()), c[3]),
                    new Pair<>(w(new SpatialFeatureDY()), c[4]),
                    new Pair<>(w(new SpatialFeature2Norm()), c[5])));
        }

        private SpatialFeature<Coords2D> w(SpatialFeature<Coords2D> i) {
            return new SpatialFeatureOrdinalWrapper<>(i);
        }

        public SpatialFeature<Coords2D> apply(Meanings.PP pp) {
            return map.get(pp);
        }
    }

    private static class HandTunedMapping implements Function<Meanings.PP, SpatialFeature<Coords2D>> {

        private HashMap<Meanings.PP, SpatialFeature<Coords2D>> map = new HashMap<>();

        public HandTunedMapping() {
            super();

            map.put(Meanings.PP.NEAR, getWV(new double[]{-1, 0, 0, 0, 0, 0}));
            map.put(Meanings.PP.LEFT, getWV(new double[]{-3, 0, -1, 0, 0, 0}));
            map.put(Meanings.PP.RIGHT, getWV(new double[]{1, 0, -1, 0, 0, 0}));
            map.put(Meanings.PP.FRONT, getWV(new double[]{0, 1, -1, 0, 0, 0}));
            map.put(Meanings.PP.BETWEEN, new WeightedFeature(Arrays.asList(
                    new Pair<>(new SpatialFeature3PerpendicularDistance(), 0.0),
                    new Pair<>(new SpatialFeature3ProjectionBetweenPoints(), 1.0),
                    new Pair<>(new SpatialFeature3PerpendicularDistanceFrac(), -1.0),
                    new Pair<>(new SpatialFeature3MiddleDistance(), 0.0))));
            //        new Pair<>(w(new SpatialFeature3PerpendicularDistance()), 0.0),
            //        new Pair<>(w(new SpatialFeature3ProjectionBetweenPoints()), 0.0),
            //        new Pair<>(w(new SpatialFeature3PerpendicularDistanceFrac()), 0.0),
            //        new Pair<>(w(new SpatialFeature3MiddleDistance()), 0.0))));
        }

        private SpatialFeature<Coords2D> getWV(double[] c) {
            return new WeightedFeature(Arrays.asList(
                    new Pair<>(new SpatialFeatureDX(), c[0]),
                    new Pair<>(new SpatialFeatureDY(), c[1]),
                    new Pair<>(new SpatialFeature2Norm(), c[2]),
                    new Pair<>(w(new SpatialFeatureDX()), c[3]),
                    new Pair<>(w(new SpatialFeatureDY()), c[4]),
                    new Pair<>(w(new SpatialFeature2Norm()), c[5])));
        }

        private SpatialFeature<Coords2D> w(SpatialFeature<Coords2D> i) {
            return new SpatialFeatureOrdinalWrapper<>(i);
        }

        public SpatialFeature<Coords2D> apply(Meanings.PP pp) {
            return map.get(pp);
        }
    }
}
