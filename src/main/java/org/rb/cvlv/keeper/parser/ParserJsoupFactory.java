package org.rb.cvlv.keeper.parser;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author raitis
 */
public class ParserJsoupFactory {

    private static final Map<Parser,String> parserUrl= new HashMap<>();

    static {
        parserUrl.put(Parser.Home, "https://www.cv.lv");
        parserUrl.put(Parser.NVA, "https://cvvp.nva.gov.lv/#/pub/vakances/saraksts");
    }

    public static ParserJsoupFactory init() {
        return new ParserJsoupFactory();
    }
    
    private ParserJsoupFactory() {
       
    }
    
    
    public IParserJsoup getJsoupParser(Parser parser, String docString){
        IParserJsoup iparser ;
        switch(parser){
            case Home:
                iparser = new ParserJsoup_CVLV(docString);
                break;
            case NVA:
                iparser = new ParserJsoup_NVA(docString);
                break;
            default:
                iparser= new ParserJsoup_CVLV(docString);
        }
        return iparser;
    }
    
    public String getUrl(Parser parser){
     return parserUrl.get(parser);
    }
}
