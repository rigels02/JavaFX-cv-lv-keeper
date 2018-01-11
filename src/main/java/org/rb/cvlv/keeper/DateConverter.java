package org.rb.cvlv.keeper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.util.StringConverter;

/**
 *
 * @author raitis
 */
public class DateConverter extends StringConverter<LocalDate>{

     DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            @Override
            public String toString(LocalDate object) {
              if(object==null) return "";
              return object.format(df);
            }

            @Override
            public LocalDate fromString(String string) {
                if(string.isEmpty()) return null;
                LocalDate date=null;
                date =LocalDate.parse(string,df);
                return date;
            }
    
}
