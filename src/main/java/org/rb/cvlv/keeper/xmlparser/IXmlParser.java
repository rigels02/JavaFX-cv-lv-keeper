package org.rb.cvlv.keeper.xmlparser;

import java.util.List;
import org.rb.cvlv.keeper.model.XKeep;

/**
 *
 * @author raitis
 */
public interface IXmlParser {

    List<XKeep> deserializeXML(String xmlDoc) throws Exception;

    List<XKeep> deserializeXMLFile(String filePath) throws Exception;

    String serializeXML(List<XKeep> keep) throws Exception;

    void serializeXMLFile(List<XKeep> keep, String filePath) throws Exception;
    
}
