
package org.rb.cvlv.keeper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
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
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.rb.cvlv.keeper.files.PdfWriter;
import org.rb.cvlv.keeper.model.XKeep;
import org.rb.cvlv.keeper.xmlparser.SimpleXMLParser;

/**
 * FXML Controller class
 *
 * @author raitis
 */
public class FXMLDocViewController implements Initializable {

    @FXML
    private Label fxLblTitle;

    @FXML
    private Label fxLblPublished;

    @FXML
    private Label fxLblDeadline;

    @FXML
    private Label fxLblLocation;

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
        //TODO
        List<XKeep> keeps = (List<XKeep>) MainApp.getPrimaryStage().getProperties().get("xkeeps");
        int idx = (int) MainApp.getPrimaryStage().getProperties().get("selectedIdx");
        keeps.get(idx).setComments(fxTxtComments.getText());
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
    
    private File saveFile(String title){
    if(title==null || title.isEmpty()){
          title = "Target File";
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open "+ title);
        Stage stage = new Stage();
        File selection = fileChooser.showSaveDialog(stage);
        System.out.println("Selection = "+selection);
        return selection;
    }
    
    @FXML
    void onBtnSavePDF(ActionEvent event) {
        File file = saveFile(null);
        if(file==null) return;
         XKeep keep = (XKeep)MainApp.getPrimaryStage().getProperties().get("xkeep");
        try {
            String xmlDoc = keep.getHtmlScrap();
            String xmlDoc1 = String.format("<div>Link: <a href=\"%s\">%s</a></div>%s",
                    keep.getPageUrl(),keep.getPageUrl(),xmlDoc );
            //String xmlDoc = PdfWriter.tidyUp(htmlDoc);
            PdfWriter.writePDF(xmlDoc1, file);
        } catch (Exception ex) {
            Logger.getLogger(FXMLDocViewController.class.getName()).log(Level.SEVERE, null, ex);
            new Alert(Alert.AlertType.ERROR, ex.getMessage(),ButtonType.OK).showAndWait();
            return;
        }
        new Alert(Alert.AlertType.INFORMATION,"Pdf file "+file.getName()+" saved").showAndWait();
    }
    
}
