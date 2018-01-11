package org.rb.cvlv.keeper.model;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * XKeep extended by additional fields 9Jan2018
 * @author raitis
 */
public class XKeep extends Keep{
    public final static String TITLE="Title";
    public final static String PD="Published";
    public final static String DL="DeadLine";
    public final static String LOC="Location";
    public final static String COM="Comments";
    //-- XKeep extended by additional fields 9Jan2018
    public final static String STATUS="Status: ";
    public final static String APPLDATE="Applyed: ";
    public final static String INT1DATE="1st Interview: ";
    public final static String INT2DATE="2nd Interview: ";
    public final static String CANCELED="Canceled: ";
    
    private String comments;
    private String htmlScrap;
    //-- XKeep extended by additional fields 9Jan2018
     private XStatus status = XStatus.NoApply;
    private Date applyDate;
    private Date int1Date;
    private Date int2Date;
    private Date cancelDate;
    

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

    public XKeep( 
            String title, String location, 
            Date published, Date deadline, String pageUrl,
            String comments, String htmlScrap,
            XStatus status,Date applyDate, Date int1Date, 
            Date int2Date, Date cancelDate) {
        
        super(title, location, published, deadline, pageUrl);
        
        this.comments = comments;
        this.htmlScrap = htmlScrap;
        this.status = status;
        this.applyDate = applyDate;
        this.int1Date = int1Date;
        this.int2Date = int2Date;
        this.cancelDate = cancelDate;
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

    public XStatus getStatus() {
        return status;
    }

    public void setStatus(XStatus status) {
        this.status = status;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public Date getInt1Date() {
        return int1Date;
    }

    public void setInt1Date(Date int1Date) {
        this.int1Date = int1Date;
    }

    public Date getInt2Date() {
        return int2Date;
    }

    public void setInt2Date(Date int2Date) {
        this.int2Date = int2Date;
    }

    public Date getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(Date cancelDate) {
        this.cancelDate = cancelDate;
    }

    @Override
    public String toString() {
        return "XKeep{" +super.toString()+ "comments=" + comments + ", htmlScrap=" + htmlScrap + 
                ", status=" + status + ", applyDate=" + applyDate + ", int1Date=" + int1Date + 
                ", int2Date=" + int2Date + ", cancelDate=" + cancelDate + '}';
    }

   
    
    public String printItem(final SimpleDateFormat sf){
     String sitem="";   
     switch(status){
            case NoApply:
               break;
            case Apply:
                sitem= String.format("\n%s%s", APPLDATE,sf.format(applyDate));
                break;
            case Interv_1:
                sitem= String.format("\n%s%s",INT1DATE,sf.format(int1Date));
                break;    
            case Interv_2:
                sitem= String.format("\n%s%s",INT2DATE,sf.format(int2Date));
                break;    
            case Canceled:
                sitem= String.format("\n%s%s",CANCELED,sf.format(cancelDate));
                break;    
                
        }   
    return String.format("%s\n%s: %s, %s: %s\n%s\n%s\n%s", 
           super.getTitle(),
           PD,
           (super.getPublished()==null)?"":sf.format(super.getPublished()),
           DL,
           (super.getDeadline()==null)?"":sf.format(super.getDeadline()),
           super.getLocation(),
           super.getPageUrl(),
           sitem
        );
    }
    
    
}
