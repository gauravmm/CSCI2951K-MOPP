/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.util.xml;

import edu.brown.csci2951k.corpus.Corpus;
import edu.brown.csci2951k.corpus.XMLCorpusAdapter;
import edu.brown.csci2951k.models.data.MObject;
import edu.brown.csci2951k.models.data.MObjectImpl;
import edu.brown.csci2951k.models.data.MObjectSet;
import edu.brown.csci2951k.models.language.ObjectLanguageModel;
import edu.brown.csci2951k.models.language.ObjectLanguageModelUnigram;
import edu.brown.csci2951k.models.language.XMLAdapterObjectLanguageModelUnigram;
import edu.brown.csci2951k.models.space.Coords2D;
import edu.brown.csci2951k.models.space.SpatialModel;
import edu.brown.csci2951k.models.space.XMLAdapterCoords2D;
import edu.brown.csci2951k.util.Pair;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Gaurav Manek
 */
public class XMLSerializationTest {

    public XMLSerializationTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    private void toXML() {
        List<String> l = Arrays.asList("Red Spoon", "Blue Spoon", "Pink Bowl", "Blue Bowl");

        MObjectSet set = new MObjectSet(l.stream().map(s -> this.getObject(s)).collect(Collectors.toList()));
        SpatialModel<Coords2D> model = new SpatialModel<>(set, (m) -> new Coords2D(Math.random(), Math.random()));
        model.addListener((s) -> System.err.println(s.getId()));

        //System.out.println(set.toXML("objects"));
        //System.out.println(model.toXML("model"));
        List<Pair<String, MObject>> asList = Arrays.asList(new Pair<>("This is a test", set.get("red_spoon").get()));
        Corpus<Coords2D> corpus = new Corpus<>(set, model, asList);
        System.out.println(corpus.toXML("corpus"));
    }

    @Test
    public void fromAndToXML() {
        String in = "<corpus>\n"
                + "	<objects>\n"
                + "		<element>\n"
                + "			<id>red_spoon</id>\n"
                + "			<language_model>\n"
                + "				<element>red</element>\n"
                + "				<element>spoon</element>\n"
                + "			</language_model>\n"
                + "		</element>\n"
                + "		<element>\n"
                + "			<id>pink_bowl</id>\n"
                + "			<language_model>\n"
                + "				<element>pink</element>\n"
                + "				<element>bowl</element>\n"
                + "			</language_model>\n"
                + "		</element>\n"
                + "		<element>\n"
                + "			<id>blue_bowl</id>\n"
                + "			<language_model>\n"
                + "				<element>blue</element>\n"
                + "				<element>bowl</element>\n"
                + "			</language_model>\n"
                + "		</element>\n"
                + "		<element>\n"
                + "			<id>blue_spoon</id>\n"
                + "			<language_model>\n"
                + "				<element>blue</element>\n"
                + "				<element>spoon</element>\n"
                + "			</language_model>\n"
                + "		</element>\n"
                + "	</objects>\n"
                + "	<model>\n"
                + "		<element id=\"red_spoon\">\n"
                + "			<coords x=\"0.38085303294643225\" y=\"0.577481346439875\" />\n"
                + "		</element>\n"
                + "		<element id=\"pink_bowl\">\n"
                + "			<coords x=\"0.28256668172233046\" y=\"0.0540260956899099\" />\n"
                + "		</element>\n"
                + "		<element id=\"blue_bowl\">\n"
                + "			<coords x=\"0.6530228500824846\" y=\"0.6332352761873828\" />\n"
                + "		</element>\n"
                + "		<element id=\"blue_spoon\">\n"
                + "			<coords x=\"0.37957390064188856\" y=\"0.2832656650038078\" />\n"
                + "		</element>\n"
                + "	</model>\n"
                + "	<corpus>\n"
                + "		<element val=\"red_spoon\" key=\"This is a test\" />\n"
                + "	</corpus>\n"
                + "</corpus>\n"
                + "";
        XMLObject parseXML = XMLParser.parseXML(in);

        XMLCorpusAdapter<Coords2D, ObjectLanguageModelUnigram> xmlCorpusAdapter = new XMLCorpusAdapter<>(XMLAdapterCoords2D.getInstance(), XMLAdapterObjectLanguageModelUnigram.getInstance());
        Corpus<Coords2D> fromXML = xmlCorpusAdapter.fromXML(parseXML.get("corpus"));

        Assert.assertEquals(in, fromXML.toXML("corpus").toString());
    }

    private ObjectLanguageModel getModel(String s) {
        List<String> collect = Arrays.stream(s.split(" ")).map(w -> w.toLowerCase()).collect(Collectors.toList());
        return new ObjectLanguageModelUnigram(collect);
    }

    private MObject getObject(String s) {
        String id = s.toLowerCase().replace(" ", "_");
        return new MObjectImpl(id, getModel(s));
    }

}
