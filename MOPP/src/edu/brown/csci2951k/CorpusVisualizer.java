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
import edu.brown.csci2951k.ui.Frm2DViz;
import edu.brown.csci2951k.util.xml.XMLObject;
import edu.brown.csci2951k.util.xml.XMLParser;
import edu.brown.csci2951k.util.xml.XMLSerializingException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author Gaurav Manek, Zachary Loery
 */
public class CorpusVisualizer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        args = new String[] {"data\\corpora source\\corpus_11.xml"};
        if (args.length == 0) {
            System.out.println("Usage: java -cp MOPP.jar edu.brown.csci2951k.CorpusVisualizer FILE_1 [FILE_2 [...]] ");
            System.exit(1);
        }

        XMLCorpusAdapter<Coords2D, ObjectLanguageModelUnigram> xmlCorpusAdapter = new XMLCorpusAdapter<>(XMLAdapterCoords2D.getInstance(), XMLAdapterObjectLanguageModelUnigram.getInstance());

        for (String a : args) {
            try {
                String collect = Files.readAllLines(Paths.get(a)).stream().collect(Collectors.joining("\n"));
                XMLObject parseXML = XMLParser.parseXML(collect);
                Corpus<Coords2D> fromXML = xmlCorpusAdapter.fromXML(parseXML.get(0));
                
                (new Frm2DViz(fromXML.getSM())).setTitle(a);
            } catch(XMLSerializingException e){
                System.out.println("[ERROR] Exception parsing ".concat(a).concat(": ").concat(e.getMessage()));
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("[ERROR] Cannot open ".concat(a).concat(": ").concat(e.getMessage()));
            }
        }
    }

}
