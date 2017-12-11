package org.rb.cvlv.keeper.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author raitis
 */
public class XKeep extends Keep{
    public final static String TITLE="Title";
    public final static String PD="Published";
    public final static String DL="DeadLine";
    public final static String LOC="Location";
    public final static String COM="Comments";
    
    private String comments;
    private String htmlScrap;

    public XKeep() {
        super();
        comments = "";
        htmlScrap = "";
    }

    public XKeep(String title, String location, Date published, Date deadline) {
        super(title, location, published, deadline);
        comments = "";
        htmlScrap = "";
    }

    
    public XKeep(String title, String location, Date published, Date deadline, String pageUrl) {
        super(title, location, published, deadline, pageUrl);
        comments = "";
        htmlScrap = "";
    }

    public XKeep(Keep keep){
        super(keep.getTitle(), keep.getLocation(), keep.getPublished(), keep.getDeadline(),keep.getPageUrl());
        comments = "";
        htmlScrap = "";
    }
    
    public XKeep( String title, String location, 
            Date published, Date deadline, String pageUrl,
            String comments, String htmlScrap) {
        super(title, location, published, deadline, pageUrl);
        this.comments = comments;
        this.htmlScrap = htmlScrap;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getHtmlScrap() {
        return htmlScrap;
    }

    public void setHtmlScrap(String htmlScrap) {
        this.htmlScrap = htmlScrap;
    }

    
    @Override
    public String toString() {
        return "XKeep{"+super.toString() + "comments=" + comments + ", htmlScrap=" + htmlScrap + '}';
    }
    
    public String printItem(final SimpleDateFormat sf){
    return String.format("%s\n%s: %s, %s: %s\n%s\n%s", 
           super.getTitle(),
           PD,
           (super.getPublished()==null)?"":sf.format(super.getPublished()),
           DL,
           (super.getDeadline()==null)?"":sf.format(super.getDeadline()),
           super.getLocation(),
           super.getPageUrl()
        );
    }
    
    
}
