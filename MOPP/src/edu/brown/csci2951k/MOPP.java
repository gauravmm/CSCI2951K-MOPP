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
import edu.brown.csci2951k.models.features.SpatialFeature2OrdinalWrapper;
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

        HashMap<Meanings.PP, Integer> correct = new HashMap<>();
        HashMap<Meanings.PP, Integer> total = new HashMap<>();

        for (Meanings.PP pp : Meanings.PP.values()) {
            correct.put(pp, 0);
            total.put(pp, 0);
        }

        Function<Meanings.PP, SpatialFeature<Coords2D>> mapping = new LearnedMapping();
        
        // Train:
        for (Corpus c : corpusTrain) {
            //processCorpus(c, eP, mapping, correct, total);
        }
        // Test:
        for (Corpus c : corpusTest) {
            processCorpus(c, eP, mapping, correct, total);
        }

        try {
            Thread.sleep(100L);
        } catch (InterruptedException ex) {
            Logger.getLogger(MOPP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for (Meanings.PP pp : Meanings.PP.values()) {
            System.out.format("%s\t%d\t%d\t%.3f\n", pp, total.get(pp), correct.get(pp), correct.get(pp) / 1.0 / total.get(pp));
        }

    }

    private static <R extends SpatialCoords, O extends ObjectLanguageModel> List<Corpus<R>> loadCorpus(XMLCorpusAdapter<R, O> adapter, List<String> file) {
        XMLObject parseXML = XMLParser.parseXML(file.stream().collect(Collectors.joining("\n")));
        return IntStream.range(0, parseXML.size()).mapToObj((i) -> adapter.fromXML(parseXML.get(i))).collect(Collectors.toList());
    }

    private static void processCorpus(Corpus c, EarleyParser eP, Function<Meanings.PP, SpatialFeature<Coords2D>> mapping, HashMap<Meanings.PP, Integer> correct, HashMap<Meanings.PP, Integer> total) {
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

    private static void processCorpusPrior(Corpus c, EarleyParser eP, HashMap<Meanings.PP, Integer> correct, HashMap<Meanings.PP, Integer> total) {
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
                correct.compute(((NonTerminalNode) convert).getType(), (k, v) -> v + 1);
            }

            // System.err.format("%d\t%s\n", numGreater, convert.toString());
            total.compute(((NonTerminalNode) convert).getType(), (k, v) -> v + 1);
        }
    }

    private static class LearnedMapping implements Function<Meanings.PP, SpatialFeature<Coords2D>> {

        private HashMap<Meanings.PP, SpatialFeature<Coords2D>> map = new HashMap<>();
        
        public LearnedMapping() {
            super();
            
            map.put(Meanings.PP.RIGHT, getWV(new double[]{.774e-4, .197e-4, .023e-4, .249e-4, -.265e-4, .213e-4}));
            map.put(Meanings.PP.LEFT, getWV(new double[] {-.149e-3, -.0034e-3, .0092e-3, .0219e-3, .0181e-3, .0146e-3}));
            map.put(Meanings.PP.NEAR, getWV(new double[] {.0529e-3, .0827e-3, -.1047e-3, .0552e-3, .0645e-3, .0601e-3}));
            map.put(Meanings.PP.FRONT, getWV(new double[] {.553e-4, .827e-4, -.616e-4, .808e-4, .602e-4, -.519e-4}));
            map.put(Meanings.PP.BETWEEN, new SpatialFeature<Coords2D>(){

                @Override
                public boolean bindsTo(int numChildren) {
                    return numChildren == 3;
                }

                @Override
                protected Double checkedApply(SpatialModel<Coords2D> model, List<MObject> objs) {
                    return 1.0;
                }
                
            });
        }
        
        private SpatialFeature<Coords2D> getWV(double[] c){
                return new WeightedFeature(Arrays.asList(
                        new Pair<>(  new SpatialFeatureDX(), c[0]),
                        new Pair<>(  new SpatialFeatureDY(), c[1]),
                        new Pair<>(  new SpatialFeature2Norm(), c[2]),
                        new Pair<>(w(new SpatialFeatureDX()), c[3]),
                        new Pair<>(w(new SpatialFeatureDY()), c[4]),
                        new Pair<>(w(new SpatialFeature2Norm()), c[5])));
                        
        }
        
        private SpatialFeature<Coords2D> w (SpatialFeature<Coords2D> i){
            return new SpatialFeature2OrdinalWrapper<>(i);
        }
            

        public SpatialFeature<Coords2D> apply(Meanings.PP pp) {
            return map.get(pp);
        }
    }
}
