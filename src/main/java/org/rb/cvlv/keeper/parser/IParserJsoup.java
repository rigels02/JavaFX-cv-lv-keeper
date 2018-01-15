package org.rb.cvlv.keeper.parser;

import java.util.Date;

/**
 *
 * @author raitis
 */
public interface IParserJsoup {

    String getBodyHTML();

    String getBodyTxt();

    Date getDeadline();

    String getLocation();

    Date getPublished();

    String getTitle();

    void process();
    
}
