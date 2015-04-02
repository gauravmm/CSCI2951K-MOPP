/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k;

import edu.brown.csci2951k.corpus.Corpus;
import edu.brown.csci2951k.corpus.XMLCorpusAdapter;
import edu.brown.csci2951k.models.language.ObjectLanguageModelUnigram;
import edu.brown.csci2951k.models.language.XMLAdapterObjectLanguageModelUnigram;
import edu.brown.csci2951k.models.space.Coords2D;
import edu.brown.csci2951k.models.space.XMLAdapterCoords2D;
import edu.brown.csci2951k.util.Pair;
import edu.brown.csci2951k.util.xml.XMLObject;
import edu.brown.csci2951k.util.xml.XMLParser;
import edu.brown.csci2951k.util.xml.XMLSerializingException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 * @author Gaurav Manek, Zachary Loery
 */
public class CorpusSplitter {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        args = IntStream.range(0, 20).mapToObj((i) -> String.format("data/corpora source/corpus_%02d.xml", i)).toArray(String[]::new);

        double corpus_split_frac = 0.3;
        
        XMLCorpusAdapter<Coords2D, ObjectLanguageModelUnigram> xmlCorpusAdapter = new XMLCorpusAdapter<>(XMLAdapterCoords2D.getInstance(), XMLAdapterObjectLanguageModelUnigram.getInstance());

        List<String> testCorpora = new ArrayList<>();
        List<String> trainingCorpora = new ArrayList<>();
        List<String> corpusSplit = new ArrayList<>();
        int corpus_max = 13;
        
        for (String a : args) {
            try {
                String collect = Files.readAllLines(Paths.get(a)).stream().collect(Collectors.joining("\n"));
                XMLObject parseXML = XMLParser.parseXML(collect);
                Corpus<Coords2D> fromXML = xmlCorpusAdapter.fromXML(parseXML.get(0));
                String name = parseXML.get(0).getTag();
                
                Pair<List<Integer>, Pair<Corpus<Coords2D>, Corpus<Coords2D>>> splitCorpus = fromXML.getSplitCorpus(corpus_split_frac);
                
                testCorpora.add(splitCorpus.getValue().getKey().toXML(name).toString());
                trainingCorpora.add(splitCorpus.getValue().getValue().toXML(name).toString());
                List<Integer> idxes = splitCorpus.getKey();
                corpusSplit.add(IntStream.range(0, corpus_max).mapToObj((i) -> idxes.contains(i)? "test":"train").collect(Collectors.joining(", ", name + ", ", "")));
            } catch(XMLSerializingException e){
                System.out.println("[ERROR] Exception parsing ".concat(a).concat(": ").concat(e.getMessage()));
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("[ERROR] Cannot open ".concat(a).concat(": ").concat(e.getMessage()));
            }
        }
        
        Files.write(Paths.get("data/corpus_train.xml"), trainingCorpora);
        Files.write(Paths.get("data/corpus_test.xml"), testCorpora);
        Files.write(Paths.get("data/corpus_split.csv"), corpusSplit);
    }

}
