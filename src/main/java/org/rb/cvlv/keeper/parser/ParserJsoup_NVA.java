package org.rb.cvlv.keeper.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author raitis
 */
public class ParserJsoup_NVA implements IParserJsoup {
 
    private static final Logger LOG = Logger.getLogger(ParserJsoup_CVLV.class.getName());
    
    private static final String LOCATION_LABEL="Darba vietas adrese";
    private static final String DEADLINE_LABEL="Aktuāla līdz";
    
    private final Document doc;
    private String title;
    private String location;
    private Date published;
    private Date deadline;
    
    private Elements formGroupPart;
    private String formGroupPartHtml;
    private Elements descriptionPart;
    private Elements skillsPart;
    
    public ParserJsoup_NVA(String docString) {
        //JSoup by default uses html doc output settings
        //Itext for PDF convertion uses XML imput document
        //To make Jsoup generate xml valid document the xmlParser is used
        //Parser.xmlParser()
        //Reference : https://github.com/jhy/jsoup/issues/511
        // In HTML the img tag allowed do not have end tag.
        // In XHTML the tag must be properly closed.
        
        //ugly html document. use xmlParser before pdf printing
        //doc = Jsoup.parse(docString, "",org.jsoup.parser.Parser.xmlParser());
        
        doc= Jsoup.parse(docString);
        //doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        //String xmlString = doc.html();
        LOG.log(Level.INFO, doc.title());
    }

    @Override
    public String getBodyHTML() {
       
     return  "<h1>"+title+"</h1>"+
      descriptionPart.html()+
      skillsPart.html()+
       "<hr/>"+      
       //formGroupPart.html();    
       formGroupPartHtml;      
    }

    @Override
    public String getBodyTxt() {
        //TODO change...
        return descriptionPart.text()+"\n"+skillsPart.text()+formGroupPart.text();
    }

    @Override
    public Date getDeadline() {
       return deadline;  
    }

    @Override
    public String getLocation() {
     return location;  
    }

    @Override
    public Date getPublished() {
     return null;  
    }

    @Override
    public String getTitle() {
     return title;    
    }

    
    @Override
    public void process() {
        
        try {
            getTitleDateLocation();
        } catch (ParseException ex) {
            Logger.getLogger(ParserJsoup_NVA.class.getName()).log(Level.SEVERE, null, ex);
        }
        //String htmlDoc = doc.toString();
        this.descriptionPart= doc.select("div[un2group='Darba apraksts']");
        this.skillsPart = doc.select("div[un2group='Prasmes']");
        
    }

    

    private void getTitleDateLocation() throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat("dd.MM.yyyy");
        /*
        The output will be a little bit ugly (removed scripts etc)
        Let's make the output more accurate.
        */
        String fmt= "<p><b>%s:</b> %s</p>";
        StringBuilder sb = new StringBuilder();
        
        this.title = doc.select("h1").text();
        Elements divsi = doc.select("div.form-group");
        for (Element element : divsi) {
            String label = element.select("LABEL").text();
            String val = element.select("div > div").text();
            System.out.println("Label: "+label+" value: "+val);
            if(label.equalsIgnoreCase(LOCATION_LABEL)){
               this.location= val;
            }
            if(label.equalsIgnoreCase(DEADLINE_LABEL)){
             this.deadline= sf.parse(val);
            }
            sb.append(String.format(fmt, label,val));
        }
        this.formGroupPartHtml = sb.toString();
        this.formGroupPart = divsi;
       // System.out.println("formGroupHtml= "+formGroupPart.html());
    }
    
}
