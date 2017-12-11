package org.rb.cvlv.keeper.xmlparser;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.rb.cvlv.keeper.model.Keep;
import org.rb.cvlv.keeper.model.XKeep;

/**
 *
 * @author raitis
 */
public class DomParserTest {
    
    public DomParserTest() {
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

    /**
     * Test of getKeeps method, of class DomParser.
     */
    @Test
    public void testGetKeeps() {
        System.out.println("getKeeps");
        
    }

    /**
     * Test of toString method, of class DomParser.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        
    }

    /**
     * Test of buildXML method, of class DomParser.
     */
    @Test
    public void testBuildXMLAndParseXML() throws Exception {
        System.out.println("testBuildXMLAndParseXML");
        SimpleDateFormat sf = new SimpleDateFormat("dd.MM.yyyy");
        List<XKeep> keep = Arrays.asList(
                new XKeep("Title1", "location1", sf.parse("29.11.2017"), sf.parse("29.12.2017")),
                new XKeep("Title2", "location2", sf.parse("28.11.2017"), sf.parse("28.12.2017")),
                new XKeep("Title3", "location3", sf.parse("27.11.2017"), sf.parse("27.12.2017"))
        );
        DomParser instance = new DomParser();
        String expResult = "";
        String result = instance.serializeXML(keep);
        //assertEquals(expResult, result);
        assertTrue( !result.isEmpty() );
        System.out.println("Result->\n"+result);
        List<XKeep> keep1 = instance.deserializeXML(result);
        for (Keep keep2 : keep1) {
            System.out.println("->"+keep2);
        }
        assertEquals(keep.toString(), keep1.toString());
    }
    
    @Test
    public void testWriteReadFile() throws  Exception{
        
        System.out.println("testWriteReadFile()");
        SimpleDateFormat sf = new SimpleDateFormat("dd.MM.yyyy");
        List<XKeep> keep = Arrays.asList(
          new XKeep("Title1Ā", "location1ā", sf.parse("29.11.2017"), sf.parse("29.12.2017")),
          new XKeep("Title2Š", "location2š", sf.parse("28.11.2017"), sf.parse("28.12.2017")),
          new XKeep("Title3Ņ", "location3ņ", sf.parse("27.11.2017"), sf.parse("27.12.2017"))
        );
        DomParser domPar = new DomParser();
        String filePath = "xmlTest.xml";
        domPar.serializeXMLFile(keep, filePath);
        List<XKeep> keep2 = domPar.deserializeXMLFile(filePath);
        for (Keep keep1 : keep2) {
            System.out.println("->"+keep1);
        }
        assertEquals(keep.toString(), keep2.toString());
    }
    
    @Test
    public void testWriteReadFile_1() throws  Exception{
        
        System.out.println("testWriteReadFile_1()");
        
        SimpleDateFormat sf = new SimpleDateFormat("dd.MM.yyyy");
        String htmlScrap="<div><h1>Header</h1><p>paragraph</p></div>";
        List<XKeep> keep = Arrays.asList(
          new XKeep("Title1Ā", "location1ā", sf.parse("29.11.2017"), sf.parse("29.12.2017"),
                        "http://test1.lv", "comments1", htmlScrap),
          new XKeep("Title2Š", "location2š", sf.parse("28.11.2017"), sf.parse("28.12.2017"),
                        "http://test2.lv", "comments2", htmlScrap),
          new XKeep("Title3Ņ", "location3ņ", sf.parse("27.11.2017"), sf.parse("27.12.2017"),
                        "http://test3.lv", "comments3", htmlScrap)
        );
        DomParser domPar = new DomParser();
        String filePath = "xmlTest.xml";
        domPar.serializeXMLFile(keep, filePath);
        List<XKeep> keep2 = domPar.deserializeXMLFile(filePath);
        for (Keep keep1 : keep2) {
            System.out.println("->"+keep1);
        }
        assertEquals(keep.toString(), keep2.toString());
    }
}
