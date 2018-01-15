package org.rb.cvlv.keeper.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

/**
 *
 * @author raitis
 */
public class ParserJsoup_CVLV implements IParserJsoup {

    private static final Logger LOG = Logger.getLogger(ParserJsoup_CVLV.class.getName());
    
    private final String pubDate = "Published:";
    private final String deadDate = "Deadline:";
    private final String pubDateLV = "Publicēts:";
    private final String deadDateLV = "Beigu termiņš:";

    private Document doc;
    private String title;
    private String location;
    private Date published;
    private Date deadline;

    public ParserJsoup_CVLV(String docString) {
        //JSoup by default uses html doc output settings
        //Itext for PDF convertion uses XML imput document
        //To make Jsoup generate xml valid document the xmlParser is used
        //Parser.xmlParser()
        //Reference : https://github.com/jhy/jsoup/issues/511
        // In HTML the img tag allowed do not have end tag.
        // In XHTML the tag must be properly closed.
        doc = Jsoup.parse(docString, "",Parser.xmlParser());
        //doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        //String xmlString = doc.html();
        LOG.log(Level.INFO, doc.title());

    }

    @Override
    public void process() {
        getDates();
        getTitleAndLocation();
    }

    private void getDates() {
        
        GetDate gDate = new GetDate();
        Elements elements = doc.getElementsByClass("application-date");
        for (Element element : elements) {
            System.out.println("date: " + element.text());
            String txt = element.text();
            if(gDate.isPubDate(txt)){
                try {
                    published = gDate.getDate(txt);
                } catch (ParseException ex) {
                    Logger.getLogger(ParserJsoup_CVLV.class.getName()).log(Level.SEVERE, null, ex);
                }
              continue;
            }
            if(gDate.isDeadDate(txt)){
                try {
                    deadline = gDate.getDate(txt);
                } catch (ParseException ex) {
                    Logger.getLogger(ParserJsoup_CVLV.class.getName()).log(Level.SEVERE, null, ex);
                }
                //continue;
                //continue;
            }
            
        }

    }

    private void getTitleAndLocation() {
        title = doc.selectFirst("[itemprop=title]").text();

        location = doc.select("[itemprop=jobLocation]").text();//elements.attr("jobLocation");
        System.out.println("title= " + title);
        System.out.println("location= " + location);
        System.out.println("published: " + published);
        System.out.println("DeadTime: " + deadline);
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public Date getPublished() {
        return published;
    }

    @Override
    public Date getDeadline() {
        return deadline;
    }

    
    @Override
    public String getBodyHTML(){
        Elements elements = doc.select("div#page-main-content");
       elements.select("ul#page-breadcrumbs").remove();
       elements.select("div.application-logo").remove();
       elements.select("div.application-apply-bttn").remove();
       elements.select("div#application-video").remove(); //remove video frame
       // Elements elements2 = elements1.select("div#application-title > div").remove();
        return elements.html();
        //doc.getElementsByTag("body").outerHtml();
    }
    @Override
    public String getBodyTxt(){
     return doc.getElementsByTag("body").text();
    }
}
