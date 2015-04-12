/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k;

import edu.brown.csci2951k.corpus.Corpus;
import edu.brown.csci2951k.corpus.XMLCorpusAdapter;
import edu.brown.csci2951k.learning.genetic.GeneticTrainer;
import edu.brown.csci2951k.models.language.ObjectLanguageModel;
import edu.brown.csci2951k.models.language.ObjectLanguageModelUnigram;
import edu.brown.csci2951k.models.language.XMLAdapterObjectLanguageModelUnigram;
import edu.brown.csci2951k.models.space.Coords2D;
import edu.brown.csci2951k.models.space.SpatialCoords;
import edu.brown.csci2951k.models.space.XMLAdapterCoords2D;
import edu.brown.csci2951k.util.xml.XMLObject;
import edu.brown.csci2951k.util.xml.XMLParser;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import ontopt.pen.EarleyParser;
import ontopt.pen.GrammarException;

/**
 *
 * @author Gaurav Manek, Zachary Loery
 */
public class CorpusTraining {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

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

        // Train:
        runCorpus(corpusTrain, eP, "corpus_train_parsed");
        // Test:
        runCorpus(corpusTest, eP, "corpus_test_parsed");

    }

    private static void runCorpus(List<Corpus<Coords2D>> co, EarleyParser eP, String n) throws IOException {
        GeneticTrainer.train(co, eP, n);
    }

    private static <R extends SpatialCoords, O extends ObjectLanguageModel> List<Corpus<R>> loadCorpus(XMLCorpusAdapter<R, O> adapter, List<String> file) {
        XMLObject parseXML = XMLParser.parseXML(file.stream().collect(Collectors.joining("\n")));
        return IntStream.range(0, parseXML.size()).mapToObj((i) -> adapter.fromXML(parseXML.get(i))).collect(Collectors.toList());
    }

}
