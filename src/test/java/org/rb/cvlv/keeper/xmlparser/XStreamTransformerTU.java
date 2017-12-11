package org.rb.cvlv.keeper.xmlparser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.rb.cvlv.keeper.model.Keep;
import org.rb.cvlv.keeper.model.XKeep;
import org.xml.sax.InputSource;

/**
 *
 * @author raitis
 */
public class XStreamTransformerTU {
    
    public XStreamTransformerTU() {
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
     * Test of getJSONFromObject method, of class XStreamTransformer.
     */
    @Test
    public void testGetJSONFromToObject() throws ParseException {
        System.out.println("testGetJSONFromToObject");
        SimpleDateFormat sf = new SimpleDateFormat("dd.MM.yyyy");
        List<XKeep> keep =  Arrays.asList(
          new XKeep("Title1Ā", "location1ā", sf.parse("29.11.2017"), sf.parse("29.12.2017")),
          new XKeep("Title2Š", "location2š", sf.parse("28.11.2017"), sf.parse("28.12.2017")),
          new XKeep("Title3Ņ", "location3ņ", sf.parse("27.11.2017"), sf.parse("27.12.2017"))
        );
        Bookmarks t = new Bookmarks(keep);
        XStreamTransformer instance = new XStreamTransformer();
        String json = instance.getJSONFromObject(t);
        
        
        Bookmarks bookmarks2 = (Bookmarks) instance.getObjectFromJSON(json);
        compareResult(t.getKeep(),bookmarks2.getKeep());
        
    }

    /**
     * Test of getObjectFromXML method, of class XStreamTransformer.
     */
    @Test
    public void testGetObjectFromToXMLForStax() throws ParseException {
        System.out.println("testGetObjectFromToXMLForStax");
        SimpleDateFormat sf = new SimpleDateFormat("dd.MM.yyyy");
        List<XKeep> keep =  Arrays.asList(
          new XKeep("Title1Ā", "location1ā", sf.parse("29.11.2017"), sf.parse("29.12.2017")),
          new XKeep("Title2Š", "location2š", sf.parse("28.11.2017"), sf.parse("28.12.2017")),
          new XKeep("Title3Ņ", "location3ņ", sf.parse("27.11.2017"), sf.parse("27.12.2017"))
        );
        Bookmarks t = new Bookmarks(keep);
        XStreamTransformer instance = new XStreamTransformer();
        String xml = instance.getXMLFromObject(t);
        Bookmarks bookmarks2 = (Bookmarks) instance.getObjectFromXML(xml);
        
        compareResult(t.getKeep(), bookmarks2.getKeep());
        
    }


    /**
     * Test of getObjectFromXMLUsingDomDriver method, of class XStreamTransformer.
     */
    @Test
    public void testGetObjectFromToXMLUsingDomDriver() throws ParseException {
        System.out.println("testGetObjectFromToXMLUsingDomDriver");
        SimpleDateFormat sf = new SimpleDateFormat("dd.MM.yyyy");
        List<XKeep> keep =  Arrays.asList(
          new XKeep("Title1Ā", "location1ā", sf.parse("29.11.2017"), sf.parse("29.12.2017")),
          new XKeep("Title2Š", "location2š", sf.parse("28.11.2017"), sf.parse("28.12.2017")),
          new XKeep("Title3Ņ", "location3ņ", sf.parse("27.11.2017"), sf.parse("27.12.2017"))
        );
        Bookmarks t = new Bookmarks(keep);
        XStreamTransformer instance = new XStreamTransformer();
        String xml = instance.getXMLFromObjectUsingDomDriver(t);
        Bookmarks bookmarks2 = (Bookmarks) instance.getObjectFromXMLUsingDomDriver(xml);
        
        compareResult(t.getKeep(), bookmarks2.getKeep());
        
    }

    
   private String formatXml(String xml) {
   
      try {
         Transformer serializer = SAXTransformerFactory.newInstance().newTransformer();
         
         serializer.setOutputProperty(OutputKeys.INDENT, "yes");
         serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
         
         Source xmlSource = new SAXSource(new InputSource(
            new ByteArrayInputStream(xml.getBytes())));
         StreamResult res = new StreamResult(new ByteArrayOutputStream());            
         
         serializer.transform(xmlSource, res);
         
         return new String(((ByteArrayOutputStream)res.getOutputStream()).toByteArray());
         
      } catch(Exception e) {
         return xml;
      }
   }


    @Test
    public void testAliasingUsingStax() throws ParseException{
        System.out.println("testAliasingUsingStax()");
        SimpleDateFormat sf = new SimpleDateFormat("dd.MM.yyyy");
        List<XKeep> keep =  Arrays.asList(
          new XKeep("Title1Ā", "location1ā", sf.parse("29.11.2017"), sf.parse("29.12.2017")),
          new XKeep("Title2Š", "location2š", sf.parse("28.11.2017"), sf.parse("28.12.2017")),
          new XKeep("Title3Ņ", "location3ņ", sf.parse("27.11.2017"), sf.parse("27.12.2017"))
        );
        Bookmarks t = new Bookmarks(keep);
        XStreamTransformer instance = new XStreamTransformer();
        instance.getStaxDriverInstance().alias("bookmarks", Bookmarks.class);
        instance.getStaxDriverInstance().alias("XKeep", XKeep.class);
        instance.getStaxDriverInstance().addImplicitCollection(Bookmarks.class, "keep");
        String xml = instance.getXMLFromObject(t);
        String xml1 = formatXml(xml);
        
        Bookmarks bookmarks2 = (Bookmarks) instance.getObjectFromXML(xml);
        
    }
    
    @Test
    public void testAliasingUsingStax_1() throws ParseException{
        System.out.println("testAliasingUsingStax_1()");
        SimpleDateFormat sf = new SimpleDateFormat("dd.MM.yyyy");
        List<XKeep> keep =  Arrays.asList(
          new XKeep("Title1Ā", "location1ā", sf.parse("29.11.2017"), sf.parse("29.12.2017")),
          new XKeep("Title2Š", "location2š", sf.parse("28.11.2017"), sf.parse("28.12.2017")),
          new XKeep("Title3Ņ", "location3ņ", sf.parse("27.11.2017"), sf.parse("27.12.2017"))
        );
       
        XStreamTransformer instance = new XStreamTransformer();
        
        instance.getStaxDriverInstance().alias("XKeep", XKeep.class);
        //instance.getStaxDriverInstance().addImplicitCollection(Bookmarks.class, "keep");
        String xml = instance.getXMLFromObject(keep);
        String xml1 = formatXml(xml);
        
        List<XKeep> keep2 =  (List<XKeep>) instance.getObjectFromXML(xml);
        
    }
    
}
