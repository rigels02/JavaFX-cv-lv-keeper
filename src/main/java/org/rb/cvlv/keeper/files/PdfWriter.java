package org.rb.cvlv.keeper.files;

import com.itextpdf.text.Document;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import org.jsoup.Jsoup;

/**
 *
 * @author raitis
 */
public class PdfWriter {

    private PdfWriter() {
    }


    
    public static void writePDF(String xmlDoc0, File targetFile) throws Exception{
        //Some non-english chars are not visible in PDF doc.
        // It looks , the reason are default fonts used by PdfWriter where utf chars are missing. 
        //To work-arround this problem, I have put xmlDoc inside of font-styled "div" block, like:
        // style="font-family:Verdana,Geneva,sans-serif"
        //In this way I force to use fonts containing utf chars.
        String xmlDoc = 
            String.format("<div style=\"font-family:Verdana,Geneva,sans-serif\">%s</div>", xmlDoc0);
        OutputStream file = new FileOutputStream(targetFile);
        InputStream is = new ByteArrayInputStream(xmlDoc.getBytes("UTF-8"));
        
        Document document = new Document();
    
         //document.open();
        com.itextpdf.text.pdf.PdfWriter writer = com.itextpdf.text.pdf.PdfWriter.getInstance(document, file);
        
        document.open(); //Important open put here!!! After getInstance
        
        XMLWorkerHelper.getInstance().parseXHtml(writer, document, is,StandardCharsets.UTF_8);
       
        //add to  document some additional information
        /***
        Paragraph p1 = new Paragraph();
        p1.add("This is my paragraph 1");
        p1.setAlignment(Element.ALIGN_CENTER);
        document.add(p1);
        ****/
        
        document.close();
        is.close(); 
    }
    public static String tidyUp(String htmlDoc){
        //String xmlDoc = Jsoup.parseBodyFragment(htmlDoc).html();
        String xmlDoc = Jsoup.parse(htmlDoc).html();
        return xmlDoc;
    }
}
