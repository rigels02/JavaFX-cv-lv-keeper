package org.rb.cvlv.keeper.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Parse date string
 * it is in format:
 * 
 * "Label: date"
 * date is in different format: dd/MM/yyyy etc.
 * @author raitis
 */
public class GetDate{

    private final static String[] PUBDATELAB={"Published:","Publicēts:","Опубликовано:"};
    private final static String[] DEADDATELAB={"Deadline:", "Beigu termiņš:","Срок конкурса:"};
    private final static String[] DATEPATTERNS={"dd/MM/yyyy", "yyyy.MM.dd","dd.MM.yyyy"};
    
    private final List<String> pubDateLabels;
    private final List<String> deadDateLabels;
    private final List<SimpleDateFormat> sf;

    public GetDate() {
        pubDateLabels = new ArrayList<>(3);
        deadDateLabels= new ArrayList<>(3);
        sf = new ArrayList<>(3);
        pubDateLabels.addAll(Arrays.asList(PUBDATELAB));
        deadDateLabels.addAll(Arrays.asList(DEADDATELAB));
        for (String datePattern : DATEPATTERNS) {
            sf.add(new SimpleDateFormat(datePattern));
        }
        
    }
    
   
    public boolean isPubDate(String dateString){
        for (String pubDateLabel : pubDateLabels) {
            if(dateString.contains(pubDateLabel))
                return true;
        }
        return false;
    }

    public boolean isDeadDate(String dateString){
        for (String deadDateLabel : deadDateLabels) {
            if(dateString.contains(deadDateLabel))
                return true;
        }
        return false;
    }

    public Date getDate(String dateString) throws ParseException {
        for(int i=0; i< pubDateLabels.size(); i++){
           
         if(dateString.contains(pubDateLabels.get(i))){
             String label = pubDateLabels.get(i);
             String sPubDate = dateString.substring(dateString.indexOf(label) + label.length());
             Date pubDate = sf.get(i).parse(sPubDate);
             return pubDate;
         }
        }
        for(int i=0; i< pubDateLabels.size(); i++){
           
         if(dateString.contains(deadDateLabels.get(i))){
             String label = deadDateLabels.get(i);
             String sDeadDate = dateString.substring(dateString.indexOf(label) + label.length());
             Date deadDate = sf.get(i).parse(sDeadDate);
             return deadDate;
         }
        }
        return null;
    }
    
    public String getDateLabel(String dateString){
        for (String pubDateLabel : pubDateLabels) {
            if(dateString.contains(pubDateLabel)){
              return pubDateLabel;
            }
        }
        for (String deadDateLabel : deadDateLabels) {
            if(dateString.contains(deadDateLabel)){
             return deadDateLabel;
            }
        }
        return null;
    }
}
