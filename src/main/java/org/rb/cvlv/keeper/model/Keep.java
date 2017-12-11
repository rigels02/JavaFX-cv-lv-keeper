package org.rb.cvlv.keeper.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author raitis
 */
public class Keep {

     private final static String DATEFMT="dd/MM/yyyy";
    
    private String title;
    private String location;
    private Date published;
    private Date deadline;
    private String pageUrl;
   

    public Keep() {
    }

    public Keep(Keep other){
      this.setTitle(other.getTitle());
      this.setLocation(other.getLocation());
      this.setPublished(other.getPublished());
      this.setDeadline(other.getDeadline());
      this.setPageUrl(other.getPageUrl());
    }
    
    public Keep(String title, String location, Date published, Date deadline) {
       this(title, location, published, deadline, "");
    }

    public Keep(String title, String location, Date published, Date deadline, String pageUrl) {
        this.title = title;
        this.location = location;
        this.published = published;
        this.deadline = deadline;
        this.pageUrl = pageUrl;
    }

    
    public String getTitle() {
        return title;
    }

    public final void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public final void setLocation(String location) {
        this.location = location;
    }

    public Date getPublished() {
        return published;
    }

    public final void setPublished(Date published) {
        this.published = published;
    }

    public Date getDeadline() {
        return deadline;
    }

    public final void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public final void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    @Override
    public String toString() {
        return "Keep{" + "title=" + title + ", location=" + location + ", published=" + published + ", deadline=" + deadline + ", pageUrl=" + pageUrl + '}';
    }
    
    public String print(){
       
        SimpleDateFormat sf = new SimpleDateFormat(DATEFMT);
        StringBuilder sb = new StringBuilder();
        sb.append("Title: ").append(title).append("\n")
          .append("Published: ")
                .append((published==null)? "":sf.format(published))
          .append(", Deadline: ")
                .append((deadline==null)? "":sf.format(deadline)).append("\n")
          .append("Location: ").append(location);
        
        return sb.toString();
    }
    
    
}
