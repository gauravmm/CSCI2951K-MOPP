/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.util.xml;

import edu.brown.csci2951k.models.data.MObject;
import edu.brown.csci2951k.models.data.MObjectImpl;
import edu.brown.csci2951k.models.data.MObjectSet;
import edu.brown.csci2951k.models.language.ObjectLanguageModel;
import edu.brown.csci2951k.models.language.ObjectLanguageModelUnigram;
import edu.brown.csci2951k.models.language.XMLAdapterObjectLanguageModelUnigram;
import edu.brown.csci2951k.models.space.Coords2D;
import edu.brown.csci2951k.models.space.SpatialModel;
import edu.brown.csci2951k.models.space.XMLAdapterCoords2D;
import java.util.Arrays;
import java.util.List;
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

        System.out.println(set.toXML("objects"));
        System.out.println(model.toXML("model"));
    }

    @Test
    public void fromAndToXML() {
        String in = "<object_test>\n"
                + "	<element>\n"
                + "		<id>red_spoon</id>\n"
                + "		<language_model>\n"
                + "			<element>red</element>\n"
                + "			<element>spoon</element>\n"
                + "		</language_model>\n"
                + "	</element>\n"
                + "	<element>\n"
                + "		<id>pink_bowl</id>\n"
                + "		<language_model>\n"
                + "			<element>pink</element>\n"
                + "			<element>bowl</element>\n"
                + "		</language_model>\n"
                + "	</element>\n"
                + "	<element>\n"
                + "		<id>blue_bowl</id>\n"
                + "		<language_model>\n"
                + "			<element>blue</element>\n"
                + "			<element>bowl</element>\n"
                + "		</language_model>\n"
                + "	</element>\n"
                + "	<element>\n"
                + "		<id>blue_spoon</id>\n"
                + "		<language_model>\n"
                + "			<element>blue</element>\n"
                + "			<element>spoon</element>\n"
                + "		</language_model>\n"
                + "	</element>\n"
                + "</object_test>\n"
                + "<spatial_test>\n"
                + "	<element id=\"red_spoon\">\n"
                + "		<coords x=\"0.2345534167264518\" y=\"0.10230000108511605\">\n"
                + "		</coords>\n"
                + "	</element>\n"
                + "	<element id=\"pink_bowl\">\n"
                + "		<coords x=\"0.18594561076390148\" y=\"0.029521530892470227\">\n"
                + "		</coords>\n"
                + "	</element>\n"
                + "	<element id=\"blue_bowl\">\n"
                + "		<coords x=\"0.1133620674063841\" y=\"0.08286184575822109\">\n"
                + "		</coords>\n"
                + "	</element>\n"
                + "	<element id=\"blue_spoon\">\n"
                + "		<coords x=\"0.3714566050120225\" y=\"0.5933750007699079\">\n"
                + "		</coords>\n"
                + "	</element>\n"
                + "</spatial_test>\n"
                + "";
        XMLObject parseXML = XMLParser.parseXML(in);

        MObjectSet a = MObjectSet.fromXML(parseXML.get("object_test"), XMLAdapterObjectLanguageModelUnigram.getInstance());
        String s1 = a.toXML("object_test").toString();
        String s2 = SpatialModel.fromXML(parseXML.get("spatial_test"), XMLAdapterCoords2D.getInstance(), a).toXML("spatial_test").toString();

        Assert.assertEquals(in, s1.concat(s2));
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
