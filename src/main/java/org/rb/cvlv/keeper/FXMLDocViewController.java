
package org.rb.cvlv.keeper;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.LocalDateStringConverter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.rb.cvlv.keeper.files.PdfWriter;
import org.rb.cvlv.keeper.model.XKeep;
import org.rb.cvlv.keeper.model.XStatus;
import org.rb.cvlv.keeper.utils.Utils;
import org.rb.cvlv.keeper.xmlparser.SimpleXMLParser;

/**
 * FXML Controller class
 *
 * @author raitis
 */
public class FXMLDocViewController implements Initializable {
    
    private final static String HTMLTMPL="<!DOCTYPE html>\n" +
"<html>\n" +
"    <head>\n" +
"        <title>Html</title>\n" +
"        <meta charset=\"UTF-8\">\n" +
"        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
"    </head>\n" +
"    <body> %s  \n" +
"    </body>\n" +
"</html>";

    @FXML
    private Label fxLblTitle;

    @FXML
    private Label fxLblPublished;

    @FXML
    private Label fxLblDeadline;

    @FXML
    private Label fxLblLocation;

    
    @FXML
    private CheckBox fxApply;

    @FXML
    private CheckBox fx1stInt;

    @FXML
    private CheckBox fx2ndInt;

    @FXML
    private CheckBox fxCanceled;

    @FXML
    private DatePicker fxApplyDate;

    @FXML
    private DatePicker fx1stIntDate;

    @FXML
    private DatePicker fx2ndIntDate;

    @FXML
    private DatePicker fxCanceledDate;
    
    @FXML
    private TextArea fxTxtComments;

    @FXML
    private ProgressBar fxProgressBar;

    @FXML
    private WebView fxWebView;

    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        XKeep keep = (XKeep)MainApp.getPrimaryStage().getProperties().get("xkeep");
        SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
        fxLblTitle.setText(String.format("%s: %s", XKeep.TITLE,keep.getTitle()));
        fxLblPublished.setText(String.format("%s: %s",XKeep.PD, 
               (keep.getPublished()==null)?"": sf.format(keep.getPublished())
        
        ));
        fxLblDeadline.setText(String.format("%s: %s",XKeep.DL,
               (keep.getDeadline()==null)? "": sf.format(keep.getDeadline())
        
        ));
        fxLblLocation.setText(String.format("%s: %s",XKeep.LOC,keep.getLocation()));
        fxTxtComments.setText(keep.getComments());
        //--- Added fields controls 09Jan2018
        fxApply.setSelected(keep.getApplyDate()!=null);
        fx1stInt.setSelected(keep.getInt1Date()!=null);
        fx2ndInt.setSelected(keep.getInt2Date()!=null);
        fxCanceled.setSelected(keep.getCancelDate()!=null);
        setDateFields(keep);
        
        //--- 09Jan2018 --//
        final WebEngine webEngine = fxWebView.getEngine();
         
         webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
             @Override
             public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
             if (newValue == Worker.State.SUCCEEDED) {
                 System.out.println("load finished...");
                 /**
                 org.w3c.dom.Document xmlDoc = webEngine.getDocument();
                 transformXML(xmlDoc);
                 */
                 fxProgressBar.setVisible(false);
                
            } 
             if(newValue == Worker.State.RUNNING){
                System.out.println("loading...");
                
                fxProgressBar.setVisible(true);
             }
             }

             
         });
         fxProgressBar.setVisible(true);
         //Note: IFrames are not going to be shown
         webEngine.loadContent(keep.getHtmlScrap());
         
    }    
    
     @FXML
    void onBtnBack(ActionEvent event) throws IOException {
        MainApp.getMainApp().loadMainStage();
    }

 @FXML
    void onBtnUpdate(ActionEvent event) throws IOException {
        //update comments
        
        if(!isValidDates()) return;
        List<XKeep> keeps = (List<XKeep>) MainApp.getPrimaryStage().getProperties().get("xkeeps");
        int idx = (int) MainApp.getPrimaryStage().getProperties().get("selectedIdx");
        keeps.get(idx).setComments(fxTxtComments.getText());
        updateStatusAndDateFields(keeps.get(idx));
        //Test
        System.out.println("XKeep=> "+keeps.get(idx));
        saveData(keeps);
        //load mainStage
        MainApp.getMainApp().loadMainStage();
    }    

    private void saveData(List<XKeep> keeps) {
    SimpleXMLParser xmlParser = new SimpleXMLParser();
        try {
            xmlParser.serializeXMLFile(keeps, FXMLDocumentController.FILEPATH);
        } catch (Exception ex) {
            Logger.getLogger(FXMLDocViewController.class.getName()).log(Level.SEVERE, null, ex);
            new Alert(Alert.AlertType.ERROR, "Error:\n"+ex.getMessage(),ButtonType.CLOSE).showAndWait();
        }   
    }
    
    
    @FXML
    void onBtnSaveHTML(ActionEvent event) {

        File file = Utils.saveFileDlg(null);
        if(file==null) return;
         XKeep keep = (XKeep)MainApp.getPrimaryStage().getProperties().get("xkeep");
        try {
            String xstatus = "Status: "+getStatusWithDate(keep);
            String dDoc = keep.getHtmlScrap();
           
            String htmlDoc1 = String.format("<div>Link: <a href=\"%s\">%s</a><br/><h2>%s</h2></div>%s",
                    keep.getPageUrl(),keep.getPageUrl(),xstatus,dDoc );
            String htmlDoc2 = String.format(HTMLTMPL, htmlDoc1);
            PrintWriter writer = new PrintWriter(file,"UTF-8");
            writer.write(htmlDoc2);
            writer.close();
            
        } catch (Exception ex) {
            Logger.getLogger(FXMLDocViewController.class.getName()).log(Level.SEVERE, null, ex);
            new Alert(Alert.AlertType.ERROR, ex.getMessage(),ButtonType.OK).showAndWait();
            return;
        }
        
        new Alert(Alert.AlertType.INFORMATION,"HTML file "+file.getName()+" saved").showAndWait();
    }
    
    @FXML
    void onBtnSavePDF(ActionEvent event) {
        File file = Utils.saveFileDlg(null);
        if(file==null) return;
         XKeep keep = (XKeep)MainApp.getPrimaryStage().getProperties().get("xkeep");
        try {
            String xstatus = "Status: "+getStatusWithDate(keep);
            String dDoc = keep.getHtmlScrap();
            /**
             * Html can contains <br> , but for xml valid is <br/>
             * So, we need to replace all <br> with <br/>
             */
            String rdoc = dDoc.replaceAll("<br>", "<br/>");
            
            //make again valid xml document from html doc for pdf convertion
            Document xmlDoc = Jsoup.parse(rdoc, "",org.jsoup.parser.Parser.xmlParser());
            
            String xmlDoc1 = String.format("<div>Link: <a href=\"%s\">%s</a><br/><h2>%s</h2></div>%s",
                    keep.getPageUrl(),keep.getPageUrl(),xstatus,xmlDoc );
            //String xmlDoc = PdfWriter.tidyUp(htmlDoc);
            PdfWriter.writePDF(xmlDoc1, file);
        } catch (Exception ex) {
            Logger.getLogger(FXMLDocViewController.class.getName()).log(Level.SEVERE, null, ex);
            new Alert(Alert.AlertType.ERROR, ex.getMessage(),ButtonType.OK).showAndWait();
            return;
        }
        new Alert(Alert.AlertType.INFORMATION,"Pdf file "+file.getName()+" saved").showAndWait();
    }

   
    private void manageChkBoxAndDatePicker(CheckBox chkBox, DatePicker datePicker){
    
         if(chkBox.isSelected()){
             datePicker.setValue(LocalDate.now());
             datePicker.setDisable(false);
         }
         else{
             datePicker.setValue(null);
             datePicker.setDisable(true);
         }
    }
    
    @FXML
    void onApplChk(ActionEvent event) {
        manageChkBoxAndDatePicker(fxApply, fxApplyDate);
         
    }
    
      @FXML
    void on1stIntChk(ActionEvent event) {
          manageChkBoxAndDatePicker(fx1stInt, fx1stIntDate);
        
    }

    @FXML
    void on2ndIntChk(ActionEvent event) {
        manageChkBoxAndDatePicker(fx2ndInt, fx2ndIntDate);
    }

     @FXML
    void onCanceled(ActionEvent event) {
         manageChkBoxAndDatePicker(fxCanceled, fxCanceledDate);
    }

    
    private void setDateFields(XKeep keep) {
       // setLocaleOrConverterForDatePickers(new Locale("lv_LV"));
       setLocaleOrConverterForDatePickers(null); 
        
    if(keep.getApplyDate()!=null)   
       
        fxApplyDate.setValue(new java.sql.Date(keep.getApplyDate().getTime()).toLocalDate());   
      else
        fxApplyDate.setDisable(true);
    if(keep.getInt1Date()!=null)
        fx1stIntDate.setValue(new java.sql.Date(keep.getInt1Date().getTime()).toLocalDate());
     else
        fx1stIntDate.setDisable(true);
    if(keep.getInt2Date()!=null)
        fx2ndIntDate.setValue(new java.sql.Date(keep.getInt2Date().getTime()).toLocalDate());
     else
        fx2ndIntDate.setDisable(true);
    if(keep.getCancelDate()!=null)
        fxCanceledDate.setValue(new java.sql.Date(keep.getCancelDate().getTime()).toLocalDate());
      else
        fxCanceledDate.setDisable(true);
    }

    private void setLocaleOrConverterForDatePickers(Locale slocale) {
        /**
        Locale[] locales = Locale.getAvailableLocales();
        for (Locale locale : locales) {
            System.out.println("setConverterForDatePickers():Locales: "+locale);
        }
        System.out.println("Default Locale: "+Locale.getDefault());
        */
        if(slocale==null){
        fxApplyDate.setConverter(new DateConverter());
        fx1stIntDate.setConverter(new DateConverter());
        fx2ndIntDate.setConverter(new DateConverter());
        fxCanceledDate.setConverter(new DateConverter());
        }else{
        Locale.setDefault(slocale);
         System.out.println("Default Locale: "+Locale.getDefault());
        }
        
    }
    private LocalDate nulableDatePicker(DatePicker dpicker){
       
      return dpicker.getValue()==null? LocalDate.MAX: dpicker.getValue();
    }
    private void showError(String msg){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(msg);
        alert.showAndWait();
    }
    /**
     * Date should be:
     * applyDate<= 1stIntervDate<= 2ndIntervDate<= cancelDate
     * with any Date!= LocalDate.MAX (max future)
     * @return 
     */
    private boolean isValidDates(){
        List<String> labels= new ArrayList<>();
        List<LocalDate> dates= new ArrayList<>();
        labels.add("applyDate");
        dates.add(nulableDatePicker(fxApplyDate));
         labels.add("1st Interview Date");
        dates.add(nulableDatePicker(fx1stIntDate));
        labels.add("2nd Interview Date");
        dates.add(nulableDatePicker(fx2ndIntDate));
        labels.add("canceled Date");
        dates.add(nulableDatePicker(fxCanceledDate));
        boolean valid;
        for(int i=0; i<dates.size()-1;i++){
          if(dates.get(i)==LocalDate.MAX) continue;
           for(int j= i+1; j< dates.size(); j++){
             if(dates.get(i).compareTo(dates.get(j)) > 0){
                 showError(labels.get(j)+" is before "+labels.get(i)+" !");
               return false;
             }
                 
           }
        }
        return true;
    }

    private XStatus getStatus(){
      if(fxCanceled.isSelected()) return XStatus.Canceled;
      if(fx2ndInt.isSelected()) return XStatus.Interv_2;
      if(fx1stInt.isSelected()) return XStatus.Interv_1;
      if(fxApply.isSelected()) return XStatus.Apply;
      return XStatus.NoApply;
    }
    private void updateStatusAndDateFields(XKeep keep) {
        keep.setStatus(getStatus());
        keep.setApplyDate( fxApplyDate.getValue()==null ? null : Date.valueOf(fxApplyDate.getValue()));
        keep.setInt1Date(fx1stIntDate.getValue()==null ? null : Date.valueOf(fx1stIntDate.getValue()));
        keep.setInt2Date(fx2ndIntDate.getValue()==null ? null : Date.valueOf(fx2ndIntDate.getValue()));
        keep.setCancelDate(fxCanceledDate.getValue()==null ? null : Date.valueOf(fxCanceledDate.getValue()));
    }

    private String getStatusWithDate(XKeep keep) {
        String mstatus;
        SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
       switch(keep.getStatus()){
          
           case Apply: 
               mstatus = "Applyed "+sf.format(keep.getApplyDate());
           break;
           case Interv_1: 
               mstatus = "1st Interview "+sf.format(keep.getInt1Date());
           break;
           case Interv_2: 
               mstatus = "2nd Interview "+sf.format(keep.getInt2Date());
               break;
           case Canceled:
               mstatus = "Canceled "+sf.format(keep.getCancelDate());
               break;
           default:
               mstatus = "Not applyed";
       }
       return mstatus;
    }
    
}
