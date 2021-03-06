package org.rb.cvlv.keeper.xmlparser;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.rb.cvlv.keeper.files.TxtFiles;
import org.rb.cvlv.keeper.model.XKeep;
import org.rb.cvlv.keeper.model.XStatus;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *<pre>
 * &lt;bookmarks&gt;
 *  &lt;keep&gt;
 *      &lt;title&gt;&lt;/title&gt;
 *      &lt;location&gt;&lt;/location&gt;
 *      &lt;published&gt;&lt;/published&gt;
 *      &lt;deadline&gt;&lt;/deadline&gt;
 *      &lt;pageUrl&gt;&lt;/pageUrl&gt;
 * ---- added fields 9Jan2018 ---
 *      &lt;status&gt; &lt;status&gt;
 *      &lt;Applyed&gt; &lt;Applyed&gt;
 *      &lt;1stInterview&gt; &lt;1stInterview&gt;
 *      &lt;2ndInterview&gt; &lt;2ndInterview&gt;
 *      &lt;Canceled&gt; &lt;Canceled&gt;
 *  &lt;/keep&gt;
 * &lt;/bookmarks&gt;
 * </pre>
 * @author raitis
 */
public class DomParser implements IXmlParser {
    
    public final static String BOOKMARKS_EL="bookmarks";
    public final static String KEEP_EL="keep";
    public final static String TITLE_EL="title";
    public final static String LOCATION_EL="location";
    public final static String PUBLISHED_EL="published";
    public final static String DEADLINE_EL="deadline";
    public final static String PAGEURL_EL="pageUrl";
    public final static String COMMENT_EL="comments";
    public final static String HTMLSCRAP_EL="htmlScrap";
    //-- XKeep extended by additional fields 9Jan2018
    public final static String STATUS_EL="Status";
    public final static String APPLDATE_EL="Applyed";
    public final static String INT1DATE_EL="Interview_1";
    public final static String INT2DATE_EL="Interview_2";
    public final static String CANCELED_EL="Canceled";
    
     
    private List<XKeep> keeps;
    private final SimpleDateFormat sf;

    public DomParser() {
        this.sf = new SimpleDateFormat("dd.MM.yyyy");
        keeps = new ArrayList<>();
    }

   

    public List<XKeep> getKeeps() {
        return keeps;
    }

    @Override
    public String toString() {
        return "DomParser{" + "keeps=" + keeps + '}';
    }
    
    @Override
    public String serializeXML(List<XKeep> keep)
            throws ParserConfigurationException,
            TransformerConfigurationException,
            TransformerException {

       
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // root elements
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement(BOOKMARKS_EL);
        doc.appendChild(rootElement);

        // bookmark elements
        for (XKeep keep1 : keep) {
            buildKeepXml(doc, rootElement, keep1);
        }

        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        //StreamResult result = new StreamResult(new File("C:\\file.xml"));
        StringWriter stringWriter=null;
        
        StreamResult result = new StreamResult(stringWriter = new StringWriter());
        // Output to console for testing
        // StreamResult result = new StreamResult(System.out);

        transformer.transform(source, result);
        return stringWriter.getBuffer().toString();
    }

    private void buildKeepXml(Document doc, Element rootElement, XKeep keep) {
       Element keepEL = doc.createElement(KEEP_EL);
		rootElement.appendChild(keepEL);
                Element title = doc.createElement(TITLE_EL);
		title.appendChild(doc.createTextNode(keep.getTitle()));
		keepEL.appendChild(title);
                
                Element location = doc.createElement(LOCATION_EL);
		location.appendChild(doc.createTextNode(keep.getLocation()));
		keepEL.appendChild(location);
                
                 Element published = doc.createElement(PUBLISHED_EL);
		published.appendChild(doc.createTextNode(sf.format(keep.getPublished())));
		keepEL.appendChild(published);
                
                 Element deadline = doc.createElement(DEADLINE_EL);
		deadline.appendChild(doc.createTextNode(sf.format(keep.getDeadline())));
		keepEL.appendChild(deadline);
                
                Element pageUrl = doc.createElement(PAGEURL_EL);
		pageUrl.appendChild(doc.createTextNode(keep.getPageUrl()));
		keepEL.appendChild(pageUrl);
                
                Element comments = doc.createElement(COMMENT_EL);
		comments.appendChild(doc.createTextNode(keep.getComments()));
		keepEL.appendChild(comments);
                
                Element htmlScrap = doc.createElement(HTMLSCRAP_EL);
		htmlScrap.appendChild(doc.createTextNode(keep.getHtmlScrap()));
		keepEL.appendChild(htmlScrap);
                //-- XKeep extended by additional fields 9Jan2018
                Element status = doc.createElement(STATUS_EL);
		status.appendChild(doc.createTextNode(keep.getStatus().name()));
		keepEL.appendChild(status);
                if(keep.getApplyDate()!=null){
                Element applDate = doc.createElement(APPLDATE_EL);
		applDate.appendChild(doc.createTextNode(sf.format(keep.getApplyDate())));
		keepEL.appendChild(applDate);
                }
                if(keep.getInt1Date()!=null){
                Element int1 = doc.createElement(INT1DATE_EL);
		int1.appendChild(doc.createTextNode(sf.format(keep.getInt1Date())));
		keepEL.appendChild(int1);
                }
                if(keep.getInt2Date()!=null){
                Element int2 = doc.createElement(INT2DATE_EL);
		int2.appendChild(doc.createTextNode(sf.format(keep.getInt2Date())));
		keepEL.appendChild(int2);
                }
                if(keep.getCancelDate()!=null){
                Element cancel = doc.createElement(CANCELED_EL);
		cancel.appendChild(doc.createTextNode(sf.format(keep.getCancelDate())));
		keepEL.appendChild(cancel);
                }
    }
    
    @Override
    public List<XKeep> deserializeXML(String xmlDoc) 
            throws ParserConfigurationException, 
            SAXException, 
            IOException, 
            ParseException{
        keeps.clear();
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xmlDoc));
	Document doc = dBuilder.parse(is);
        NodeList nList = doc.getElementsByTagName(KEEP_EL);
        for(int i=0; i< nList.getLength(); i++){
          Node nNode = nList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

			Element eElement = (Element) nNode;
             // XKeep keep = new XKeepBuilder().setTitle(eElement.getElementsByTagName(TITLE_EL).item(0).getTextContent()).setLocation(eElement.getElementsByTagName(LOCATION_EL).item(0).getTextContent()).setPublished(sf.parse(eElement.getElementsByTagName(PUBLISHED_EL).item(0).getTextContent())).setDeadline(sf.parse(eElement.getElementsByTagName(DEADLINE_EL).item(0).getTextContent())).setPageUrl(eElement.getElementsByTagName(PAGEURL_EL).item(0).getTextContent()).setComments(eElement.getElementsByTagName(COMMENT_EL).item(0).getTextContent()).setHtmlScrap(eElement.getElementsByTagName(HTMLSCRAP_EL).item(0).getTextContent()).createXKeep();
              XKeep keep = new XKeep(eElement.getElementsByTagName(TITLE_EL).item(0).getTextContent(), 
                      eElement.getElementsByTagName(LOCATION_EL).item(0).getTextContent(), 
                      sf.parse(eElement.getElementsByTagName(PUBLISHED_EL).item(0).getTextContent()), 
                      sf.parse(eElement.getElementsByTagName(DEADLINE_EL).item(0).getTextContent()), 
                      eElement.getElementsByTagName(PAGEURL_EL).item(0).getTextContent(), 
                      eElement.getElementsByTagName(COMMENT_EL).item(0).getTextContent(), 
                      eElement.getElementsByTagName(HTMLSCRAP_EL).item(0).getTextContent(),
                      parseXStatus(eElement.getElementsByTagName(STATUS_EL).item(0).getTextContent()),
                      parseDateElByTagName(eElement,sf,APPLDATE_EL),
                       parseDateElByTagName(eElement,sf,INT1DATE_EL),
                       parseDateElByTagName(eElement,sf,INT2DATE_EL),
                       parseDateElByTagName(eElement,sf,CANCELED_EL)
                     );
              keeps.add(keep);
            }
        }
        return keeps;
    }
    @Override
    public void serializeXMLFile(List<XKeep> keep, String filePath) 
            throws Exception{
        
        String xmlStr = serializeXML(keep);
        TxtFiles.writeFile(filePath, xmlStr);
    }
    
    @Override
    public List<XKeep> deserializeXMLFile(String filePath) 
            throws  Exception{
      
        String xmlDoc = TxtFiles.readFile(filePath);
        if(xmlDoc.isEmpty()) return new ArrayList<>();
        return deserializeXML(xmlDoc);
        
    }

    private Date parseDateElByTagName(Element eElement, SimpleDateFormat sf, String EL_Name) throws ParseException {
       if(eElement !=null && eElement.getElementsByTagName(EL_Name).item(0)!=null){
        return sf.parse(eElement.getElementsByTagName(EL_Name).item(0).getTextContent());
       }
       return null;
    }

    private XStatus parseXStatus(String textContent) {
       return XStatus.valueOf(textContent);
    }
}
