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
import org.rb.cvlv.keeper.model.XStatus;

/**
 *
 * @author raitis
 */
public class SimpleXMLParserTest {
    
    public SimpleXMLParserTest() {
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
     * Test of deserializeXML method, of class SimpleXMLParser.
     */
    @Test
    public void testDeserializeXML() throws Exception {
        System.out.println("deserializeXML");
        
    }

    /**
     * Test of deserializeXMLFile method, of class SimpleXMLParser.
     */
    @Test
    public void testDeserializeXMLFile() throws Exception {
        System.out.println("deserializeXMLFile");
        
    }

    private void compareResult(List<XKeep> k1, List<XKeep> k2){
      
        assertTrue(k1.size()==k2.size());
        for (int i=0; i<k1.size(); i++) {
            String u1 = (k1.get(i).getPageUrl()==null)?"":k1.get(i).getPageUrl();
            String u2 = (k2.get(i).getPageUrl()==null)?"":k2.get(i).getPageUrl();
            Keep el1 = new Keep(k1.get(i).getTitle(), k1.get(i).getLocation(), 
                    k1.get(i).getPublished(), k1.get(i).getDeadline(), k1.get(i).getPageUrl());
            Keep el2 = new Keep(k2.get(i).getTitle(), k2.get(i).getLocation(), 
                    k2.get(i).getPublished(), k2.get(i).getDeadline(), k2.get(i).getPageUrl());
            el1.setPageUrl(u1);
            el2.setPageUrl(u2);
            assertEquals(el1.toString(), el2.toString());
        }
    }
    /**
     * Test of serializeXML method, of class SimpleXMLParser.
     */
    @Test
    public void testSerializeDeserializeXML() throws Exception {
        System.out.println("testSerializeDeserializeXML");
        SimpleDateFormat sf = new SimpleDateFormat("dd.MM.yyyy");
        List<XKeep> keep =  Arrays.asList(
          new XKeep("Title1", "location1", sf.parse("29.11.2017"), sf.parse("29.12.2017")),
          new XKeep("Title2", "location2", sf.parse("28.11.2017"), sf.parse("28.12.2017")),
          new XKeep("Title3", "location3", sf.parse("27.11.2017"), sf.parse("27.12.2017"))
        );
        SimpleXMLParser parser = new SimpleXMLParser();
        String expResult = "";
        String result = parser.serializeXML(keep);
        System.out.println("result->\n"+result);
        assertTrue(result.length()>0);
        List<XKeep> keep2 = parser.deserializeXML(result);
        for (Keep keep1 : keep2) {
            System.out.println("->"+keep1);
        }
        //assertEquals(keep2.toString(),keep.toString());
        compareResult(keep, keep2);
    }

    @Test
    public void testSerializeDeserializeXMLUTF8() throws Exception {
        System.out.println("testSerializeDeserializeXMLUTF8");
        SimpleDateFormat sf = new SimpleDateFormat("dd.MM.yyyy");
        List<XKeep> keep =  Arrays.asList(
          new XKeep("Title1Ā", "location1ā", sf.parse("29.11.2017"), sf.parse("29.12.2017")),
          new XKeep("Title2Č", "location2č", sf.parse("28.11.2017"), sf.parse("28.12.2017")),
          new XKeep("Title3Ņ", "location3ņ", sf.parse("27.11.2017"), sf.parse("27.12.2017"))
        );
        SimpleXMLParser parser = new SimpleXMLParser();
        String expResult = "";
        String result = parser.serializeXML(keep);
        System.out.println("result->\n"+result);
        assertTrue(result.length()>0);
        List<XKeep> keep2 = parser.deserializeXML(result);
        for (Keep keep1 : keep2) {
            System.out.println("->"+keep1);
        }
        //assertEquals(keep2.toString(),keep.toString());
        compareResult(keep, keep2);
    }
    
    /**
     * Test of serializeXMLFile method, of class SimpleXMLParser.
     */
    @Test
    public void testSerializeDeserializeXMLFile() throws Exception {
        System.out.println("testSerializeDeserializeXMLFile");
       SimpleDateFormat sf = new SimpleDateFormat("dd.MM.yyyy");
        List<XKeep> keep =  Arrays.asList(
          new XKeep("Title1", "location1", sf.parse("29.11.2017"), sf.parse("29.12.2017")),
          new XKeep("Title2", "location2", sf.parse("28.11.2017"), sf.parse("28.12.2017")),
          new XKeep("Title3", "location3", sf.parse("27.11.2017"), sf.parse("27.12.2017"))
        );
        String filePath = "testXML.xml";
        SimpleXMLParser parser = new SimpleXMLParser();
        parser.serializeXMLFile(keep, filePath);
        
        List<XKeep> keep2 = parser.deserializeXMLFile(filePath);
        for (Keep keep1 : keep2) {
            System.out.println("->"+keep1);
        }
        compareResult(keep, keep2);
    }
    
    /**
     * Test of serializeXMLFile method, of class SimpleXMLParser.
     */
    @Test
    public void testSerializeDeserializeXMLFileUTF8() throws Exception {
        System.out.println("testSerializeDeserializeXMLFileUTF8");
       SimpleDateFormat sf = new SimpleDateFormat("dd.MM.yyyy");
        List<XKeep> keep =  Arrays.asList(
          new XKeep("Title1Ā", "location1ā", sf.parse("29.11.2017"), sf.parse("29.12.2017")),
          new XKeep("Title2Š", "location2š", sf.parse("28.11.2017"), sf.parse("28.12.2017")),
          new XKeep("Title3Ņ", "location3ņ", sf.parse("27.11.2017"), sf.parse("27.12.2017"))
        );
        String filePath = "testXML.xml";
        SimpleXMLParser parser = new SimpleXMLParser();
        parser.serializeXMLFile(keep, filePath);
        
        List<XKeep> keep2 = parser.deserializeXMLFile(filePath);
        for (Keep keep1 : keep2) {
            System.out.println("->"+keep1);
        }
        compareResult(keep, keep2);
    }
    
    @Test
    public void testSerializeDeserializeXMLFileUTF8_1() throws Exception {
        System.out.println("testSerializeDeserializeXMLFileUTF8");
       SimpleDateFormat sf = new SimpleDateFormat("dd.MM.yyyy");
        String htmlScrap = "<div><h1>Header</h1><p>paragraph ĀāČčŅņ</p></div>";
        List<XKeep> keep =  Arrays.asList(
          new XKeep("Title1Ā", "location1ā", sf.parse("29.11.2017"), sf.parse("29.12.2017"),
                        "http://test1.lv", "comments1", htmlScrap),
          new XKeep("Title2Š", "location2š", sf.parse("28.11.2017"), sf.parse("28.12.2017"),
                        "http://test2.lv", "comments2", htmlScrap),
          new XKeep("Title3Ņ", "location3ņ", sf.parse("27.11.2017"), sf.parse("27.12.2017"),
                        "http://test3.lv", "comments3", htmlScrap)
        );
        String filePath = "testXML.xml";
        SimpleXMLParser parser = new SimpleXMLParser();
        parser.serializeXMLFile(keep, filePath);
        
        List<XKeep> keep2 = parser.deserializeXMLFile(filePath);
        for (Keep keep1 : keep2) {
            System.out.println("->"+keep1);
        }
        //compareResult(keep, keep2);
        assertEquals(keep.toString(), keep2.toString());
    }
    
    @Test
    public void testSerializeDeserializeXMLFileUTF8_2() throws Exception {
        System.out.println("testSerializeDeserializeXMLFileUTF8");
       SimpleDateFormat sf = new SimpleDateFormat("dd.MM.yyyy");
        String htmlScrap = "<div><h1>Header</h1><p>paragraph ĀāČčŅņ</p></div>";
        List<XKeep> keep =  Arrays.asList(
         new XKeep("Title1Ā", "location1ā", sf.parse("29.11.2017"), sf.parse("29.12.2017"),
                        "http://test1.lv", "comments1", htmlScrap,
                        XStatus.Canceled,sf.parse("01.12.2017"),sf.parse("02.12.2017"),
                        sf.parse("03.12.2017"),sf.parse("04.12.2017") 
                    ),
          new XKeep("Title2Š", "location2š", sf.parse("28.11.2017"), sf.parse("28.12.2017"),
                        "http://test2.lv", "comments2", htmlScrap,
                        XStatus.Canceled,sf.parse("01.12.2017"),sf.parse("02.12.2017"),
                        sf.parse("03.12.2017"),sf.parse("04.12.2017")
                    ),
          new XKeep("Title3Ņ", "location3ņ", sf.parse("27.11.2017"), sf.parse("27.12.2017"),
                        "http://test3.lv", "comments3", htmlScrap,
                    XStatus.Canceled,sf.parse("01.12.2017"),sf.parse("02.12.2017"),
                        sf.parse("03.12.2017"),sf.parse("04.12.2017")
                    )
        );
        String filePath = "testXML.xml";
        SimpleXMLParser parser = new SimpleXMLParser();
        parser.serializeXMLFile(keep, filePath);
        
        List<XKeep> keep2 = parser.deserializeXMLFile(filePath);
        for (Keep keep1 : keep2) {
            System.out.println("->"+keep1);
        }
        //compareResult(keep, keep2);
        assertEquals(keep.toString(), keep2.toString());
    }
}
