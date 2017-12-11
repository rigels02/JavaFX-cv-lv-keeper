package org.rb.cvlv.keeper.xmlparser;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 *
 * @author raitis
 */
public class XStreamTransformer<T> {
    private static final XStream XSTREAM_Json_INSTANCE = null;
    private static final XStream XSTREAM_Stax_INSTANCE = null;
    private static final XStream XSTREAM_Dom_INSTANCE = null;
    
    public T getObjectFromJSON(String json){
        return (T) getInstance().fromXML(json);
    }

    public String getJSONFromObject(T t){
        return getInstance().toXML(t);
    }

    private XStream getInstance(){
        if(XSTREAM_Json_INSTANCE==null){
            return new XStream(new JettisonMappedXmlDriver());
        }  else {
            return XSTREAM_Json_INSTANCE;
        }
    }

    //----- stax driver ---//
    public T getObjectFromXML(String xml){
        return (T)getStaxDriverInstance().fromXML(xml);
    }

    public String getXMLFromObject(T t){
        return getStaxDriverInstance().toXML(t);
    }
    
    public XStream getStaxDriverInstance(){
        if(XSTREAM_Stax_INSTANCE==null) {
            return new XStream(new StaxDriver());
        }else{
            return XSTREAM_Stax_INSTANCE;
        }
    }
    //----- Dom driver ----//
    public T getObjectFromXMLUsingDomDriver(String xml){
        return (T)getDomDriverInstance().fromXML(xml);
    }

    public String getXMLFromObjectUsingDomDriver(T t){
        return getDomDriverInstance().toXML(t);
    }
    
    public XStream getDomDriverInstance(){
        if(XSTREAM_Dom_INSTANCE==null){
            return new XStream(new DomDriver());
        }else{
            return XSTREAM_Dom_INSTANCE;
        }
    }

/**
 * Used for Xml output formatting.
 * With DomDriver the output is pretty printed out by default
 * In case With StaxDriver this method can be applied to get pretty output
 * @param xml
 * @return prettified  output
 */    
    public static String transformerToXml(String xml) {
   
      try {
          Transformer transform = TransformerFactory.newInstance().newTransformer();
          transform.setOutputProperty(OutputKeys.INDENT, "yes");
          transform.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

          StringWriter sw = new StringWriter();
          Result result = new StreamResult(sw);
          transform.transform(new StreamSource(new StringReader(xml)), result);
          return sw.getBuffer().toString();
      } catch(IllegalArgumentException | TransformerException e) {
         return xml;
      }
   }
}
