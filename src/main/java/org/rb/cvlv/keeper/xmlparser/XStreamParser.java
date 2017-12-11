package org.rb.cvlv.keeper.xmlparser;

import java.util.ArrayList;
import java.util.List;
import org.rb.cvlv.keeper.files.TxtFiles;
import org.rb.cvlv.keeper.model.XKeep;

/**
 *
 * @author raitis
 */
public class XStreamParser implements IXmlParser{

    private XStreamTransformer<List<XKeep>> transformer;

    public XStreamParser() {
        this.transformer = new XStreamTransformer<>();
    }
    
    
    @Override
    public List<XKeep> deserializeXML(String xmlDoc) throws Exception {
     return transformer.getObjectFromXMLUsingDomDriver(xmlDoc);
    
    }

    @Override
    public List<XKeep> deserializeXMLFile(String filePath) throws Exception {
        String xmlDoc = TxtFiles.readFile(filePath);
        if(xmlDoc.isEmpty()) return new ArrayList<>();
        return deserializeXML(xmlDoc);
    }

    @Override
    public String serializeXML(List<XKeep> keep) throws Exception {
      return transformer.getXMLFromObjectUsingDomDriver(keep);
    }

    @Override
    public void serializeXMLFile(List<XKeep> keep, String filePath) throws Exception {
        String xmlStr = serializeXML(keep);
        TxtFiles.writeFile(filePath, xmlStr);
    }
    
}
