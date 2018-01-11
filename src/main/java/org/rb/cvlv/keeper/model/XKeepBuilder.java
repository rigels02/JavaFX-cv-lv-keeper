package org.rb.cvlv.keeper.model;

import java.util.Date;


public class XKeepBuilder {

    private String title;
    private String location;
    private Date published;
    private Date deadline;
    private String pageUrl;
    private String comments;
    private String htmlScrap;
    private XStatus status;
    private Date applyDate;
    private Date int1Date;
    private Date int2Date;
    private Date cancelDate;

    public XKeepBuilder() {
    }

    public XKeepBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public XKeepBuilder setLocation(String location) {
        this.location = location;
        return this;
    }

    public XKeepBuilder setPublished(Date published) {
        this.published = published;
        return this;
    }

    public XKeepBuilder setDeadline(Date deadline) {
        this.deadline = deadline;
        return this;
    }

    public XKeepBuilder setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
        return this;
    }

    public XKeepBuilder setComments(String comments) {
        this.comments = comments;
        return this;
    }

    public XKeepBuilder setHtmlScrap(String htmlScrap) {
        this.htmlScrap = htmlScrap;
        return this;
    }

    public XKeepBuilder setStatus(XStatus status) {
        this.status = status;
        return this;
    }

    public XKeepBuilder setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
        return this;
    }

    public XKeepBuilder setInt1Date(Date int1Date) {
        this.int1Date = int1Date;
        return this;
    }

    public XKeepBuilder setInt2Date(Date int2Date) {
        this.int2Date = int2Date;
        return this;
    }

    public XKeepBuilder setCancelDate(Date cancelDate) {
        this.cancelDate = cancelDate;
        return this;
    }

    public XKeep createXKeep() {
        return new XKeep(title, location, published, deadline, pageUrl, comments, htmlScrap, status, applyDate, int1Date, int2Date, cancelDate);
    }
    
}
