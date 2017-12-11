package org.rb.cvlv.keeper.files;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author raitis
 */
public class TxtFiles {

    public static String readFile(String filePath) throws Exception {
     Path path = Paths.get(filePath);
        if( !Files.exists(path) ) return "";
        
        if(Files.size(path)>= Integer.MAX_VALUE){
           throw new IOException("MAX size of text file reached!");
        }
        List<String> stringList = Files.readAllLines(path, StandardCharsets.UTF_8);
        StringBuilder sb = new StringBuilder();
        for (String line : stringList) {
            sb.append(line).append("\n");
        }  
        return sb.toString();
    }

    
    private TxtFiles() {
    }
    
    public static void writeFile(String filePath, String xmlStr) throws Exception {
      Writer fos = new BufferedWriter(
                        new OutputStreamWriter(
                        new FileOutputStream(filePath),"UTF-8")
        );
        fos.write(xmlStr);
        fos.close();  
    }

    
}
