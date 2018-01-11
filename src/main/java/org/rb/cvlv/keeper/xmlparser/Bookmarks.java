package org.rb.cvlv.keeper.xmlparser;

import java.util.ArrayList;
import java.util.List;
import org.rb.cvlv.keeper.model.XKeep;
import org.simpleframework.xml.Default;

/**
 *
 * XML doc model for SimpleXml
 * @author raitis
 */
@Default
public class Bookmarks {
        private List<XKeep> keep;

    public Bookmarks() {
        keep = new ArrayList<>();
    }

        
        public Bookmarks(List<XKeep> keep) {
            this.keep = keep;
        }

    public List<XKeep> getKeep() {
        return keep;
    }
        
    
}

