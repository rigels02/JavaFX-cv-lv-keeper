package org.rb.cvlv.keeper.xmlparser;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import org.rb.cvlv.keeper.files.TxtFiles;
import org.rb.cvlv.keeper.model.XKeep;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

/**
 *
 * @author raitis
 */
public class SimpleXMLParser implements IXmlParser{

    
    @Override
    public List<XKeep> deserializeXML(String xmlDoc) throws Exception {
        Bookmarks bookmarks = new Bookmarks();
    
        Persister deserializer = new Persister();
        
        Bookmarks bookmarks1 = deserializer.read(bookmarks, xmlDoc);
        return bookmarks.getKeep();
    }

    @Override
    public List<XKeep> deserializeXMLFile(String filePath) throws Exception {
    
        String xmlDoc = TxtFiles.readFile(filePath);
        if(xmlDoc.isEmpty()) return new ArrayList<>();
        return deserializeXML(xmlDoc);
    }

    @Override
    public String serializeXML(List<XKeep> keep) throws Exception {
    
        Serializer serializer = new Persister();
        Bookmarks bookmarks = new Bookmarks(keep);
        StringWriter stringWriter = new StringWriter();
        serializer.write(bookmarks, stringWriter);
        return stringWriter.getBuffer().toString();
    }

    @Override
    public void serializeXMLFile(List<XKeep> keep, String filePath) throws Exception {
    
        String xmlStr = serializeXML(keep);
        TxtFiles.writeFile(filePath, xmlStr);
        
    }
    
}
