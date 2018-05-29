package org.rb.cvlv.keeper;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.rb.cvlv.keeper.model.Keep;
import org.rb.cvlv.keeper.model.XKeep;
import org.rb.cvlv.keeper.model.XStatus;
import org.rb.cvlv.keeper.parser.*;

import org.rb.cvlv.keeper.xmlparser.SimpleXMLParser;
import org.w3c.dom.Document;

/**
 *
 * @author raitis
 */
public class FXMLDocumentController implements Initializable {
    public final static String FILEPATH= "bookmarks.xml";
    
    private Parser currentParser;
    //private final static String HOME="https://www.cv.lv";
    //private final static String NVALV="https://cvvp.nva.gov.lv/#/pub/vakances/saraksts";
    private final static String DATEFMT="dd/MM/yyyy";
    
    @FXML
    private ListView<XKeep> fxListView;


    @FXML
    private TextField fxTextUrl;

    @FXML
    private WebView fxWebView;
    
     @FXML
    private ProgressBar fxProgressBar;

     @FXML
    private Button fxBtnSave;
    
     private List<XKeep> keeps;
    
     private SimpleDateFormat sf;
   
//icons for listview
private Image i_noapp,i_apply, i_int1, i_int2, i_canceled;     
private boolean saveAfterReload;
     
     
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
        i_noapp = new Image(getClass().getResourceAsStream("/images/notapplyt.png"));
        i_apply = new Image(getClass().getResourceAsStream("/images/applyt.png"));
        i_int1 = new Image(getClass().getResourceAsStream("/images/int1t.png"));
        i_int2 = new Image(getClass().getResourceAsStream("/images/int2t.png"));
        i_canceled = new Image(getClass().getResourceAsStream("/images/cancelt.png"));
        sf = new SimpleDateFormat(DATEFMT);
        loadData();
        
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
                 fxTextUrl.setText(webEngine.getLocation());
                 fxBtnSave.setDisable(false);
                 if(saveAfterReload){
                  saveAfterReload=false;
                  saveLoadedPage();
                 }
            } 
             if(newValue == Worker.State.RUNNING){
                System.out.println("loading...");
                fxBtnSave.setDisable(true);
                fxProgressBar.setVisible(true);
             }
             }

             
         });
         fxProgressBar.setVisible(true);
         
         currentParser= Parser.Home;
         webEngine.load(ParserJsoupFactory.init().getUrl(Parser.Home));
         fxTextUrl.setText(ParserJsoupFactory.init().getUrl(Parser.Home));
         fxListView.setCellFactory(new Callback<ListView<XKeep>, ListCell<XKeep>>() {
            @Override
             public ListCell<XKeep> call(ListView<XKeep> param) {
                 return new ListCell<XKeep>() {
                     @Override
                     protected void updateItem(XKeep item, boolean empty) {
                         super.updateItem(item, empty);
                         if (item == null || empty) {
                             setText(null);
                             setStyle("");
                             setGraphic(null);
                         } else {
                             
                             setText(item.printItem(sf));
                             if(deadLineDateNotExpired(item.getDeadline()))
                               setStyle("-fx-padding:4px;-fx-border-width:1;-fx-border-color: green;-fx-border-radius: 5;-fx-border-insets:1");
                             else
                                setStyle("-fx-padding:4px;-fx-border-width:1px;-fx-border-color: red;-fx-border-radius: 5;-fx-border-insets:1");
                             
                             ImageView icon = getIcon(item.getStatus());
                             //resizeImageView(icon);
                             setGraphic(icon);
                         }
                     }

                }
         ;
        }});
        
    }
        
private ImageView getIcon(XStatus status) {
    ImageView rect = null;
    switch(status){
        case Apply:
            rect = new ImageView(i_apply);
            break;
        case Interv_1:
            rect = new ImageView(i_int1);
            break;
        case Interv_2:
            rect = new ImageView(i_int2);
            break;
        case Canceled:
            rect = new ImageView(i_canceled);
            break;
        default:
            rect = new ImageView(i_noapp);
            
    }
    return rect;
}

private void resizeImageView(ImageView icon) {
    icon.setFitWidth(24);
    icon.setPreserveRatio(true);
}

    private String transformXML(Document xmlDoc) {
        
        String xml =null;
            TransformerFactory transformerFactory = TransformerFactory
                    .newInstance();
            Transformer transformer;
        
            try {    
            transformer = transformerFactory.newTransformer();
            
            StringWriter stringWriter = new StringWriter();
            /**
             * transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
             * transformer.setOutputProperty(OutputKeys.METHOD, "xml");
             * transformer.setOutputProperty(OutputKeys.INDENT, "yes");
             * transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
             * transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
             */
            
            transformer.transform(new DOMSource(xmlDoc),
                    new StreamResult(stringWriter));
                xml = stringWriter.getBuffer().toString();
                System.out.println("Transform completed...");
            //System.out.println(xml);
            
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return xml;    
    }
    
    private void saveLoadedPage(){
        final WebEngine webEngine = fxWebView.getEngine();
        org.w3c.dom.Document xmlDoc = webEngine.getDocument();
        
        String url = webEngine.getLocation();
        String xmlString = transformXML(xmlDoc);
        
        IParserJsoup parser = ParserJsoupFactory.init().getJsoupParser(currentParser, xmlString);
        //ParserJsoup_CVLV parser = new ParserJsoup_CVLV(xmlString);    
        parser.process();
       
        String scrapHtml = parser.getBodyHTML();
        //test
        //System.out.println("onBtnSave()"+scrapHtml);
        XKeep xkeep = new XKeep(parser.getTitle(), parser.getLocation(), parser.getPublished(),
                parser.getDeadline(), fxTextUrl.getText(), "", scrapHtml);
        
        addItem(xkeep);
        
        saveData();
    }
    
    @FXML
    void onBtnSave(ActionEvent event) {
        final WebEngine webEngine = fxWebView.getEngine();
        if(currentParser.equals(Parser.NVA)){
          //reload selected page to get its correct location url
          //
          saveAfterReload =true;
          webEngine.reload();
         
          //save is done after reload of page completed. See webengine load worker handler
          //stm at line #103
          return;
          
        }
        saveLoadedPage();
    }

    private void loadData() {
       
            SimpleXMLParser xmlParser = new SimpleXMLParser();
        try {    
            keeps = xmlParser.deserializeXMLFile(FILEPATH);
        } catch (Exception ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            new Alert(Alert.AlertType.ERROR, ex.getMessage(),ButtonType.OK).showAndWait();
            return;
        }
        updateListView(keeps);
    }

    private void saveData(){
        
         SimpleXMLParser xmlParser = new SimpleXMLParser();
        try {
            xmlParser.serializeXMLFile(this.keeps, FILEPATH);
        } catch (Exception ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            new Alert(Alert.AlertType.ERROR, "Error:\n"+ex.getMessage(),ButtonType.CLOSE).showAndWait();
        }
    }
    
    private void updateListView(List<XKeep> xkeeps) {
        /**
        List<Keep> keeps= new ArrayList<>();
        for (XKeep xkeep : xkeeps) {
            //Keep keep = new Keep(xkeep);
            Keep keep = xkeep;
            keeps.add(keep);
        }
        */
        ObservableList<XKeep> olist = FXCollections.observableArrayList(xkeeps);
        fxListView.setItems(olist);
        Integer idx_o = (Integer) MainApp.getPrimaryStage().getProperties().get(PropertyConst.SELIDX_P);
        int idx = -1;
        if(idx_o != null && (idx= idx_o) >= 0){
            fxListView.getSelectionModel().select(idx);
        }
        
    }
    
    
     @FXML
    void onBtnComments(ActionEvent event) throws IOException {
        if(fxListView.getSelectionModel().getSelectedIndex()< 0) return;
        int idx = fxListView.getSelectionModel().getSelectedIndex();
        XKeep keep = fxListView.getSelectionModel().getSelectedItem();
        MainApp.getPrimaryStage().getProperties().put(PropertyConst.XKEEP_P, keep);
        MainApp.getPrimaryStage().getProperties().put(PropertyConst.XKEEPS_P,this.keeps);
        MainApp.getPrimaryStage().getProperties().put(PropertyConst.SELIDX_P,idx);
        Stage stage = MainApp.getPrimaryStage();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/FXMLDocView.fxml"));
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/fxmldocview.css");
        
        stage.setTitle("Comments");
       
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    void onBtnDelete(ActionEvent event) {
        if(fxListView.getSelectionModel().getSelectedIndex()< 0) return;
        int idx = fxListView.getSelectionModel().getSelectedIndex();
        XKeep selectedItem = fxListView.getSelectionModel().getSelectedItem();
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Delete selected Item? :\n"+selectedItem.print(),
                ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> selected = confirm.showAndWait();
        if(selected.get().equals(ButtonType.NO)) return;
        //delete
        removeItem(idx);
       
        saveData();
    }
    
    @FXML
    void onBtnSelect(ActionEvent event) {
        if(fxListView.getSelectionModel().getSelectedIndex()< 0) return;
        int idx = fxListView.getSelectionModel().getSelectedIndex();
        //test
        //System.out.println("onBtnSelect:\n"+keeps.get(idx));
        Keep selectedItem = fxListView.getSelectionModel().getSelectedItem();
        fxWebView.getEngine().load(selectedItem.getPageUrl());
    }
    
     @FXML
    void onBtnHome(ActionEvent event) {
        fxTextUrl.setText(ParserJsoupFactory.init().getUrl(currentParser));
        fxWebView.getEngine().load(ParserJsoupFactory.init().getUrl(currentParser));
    }

    private void printbody(IParserJsoup parser) {
        System.out.println(">>>>----------printbody() HTML-------");
         System.out.println(parser.getBodyHTML());
        System.out.println("----------printbody() HTML-------<<<<");
       
    }

    private boolean deadLineDateNotExpired(Date deadLineDate){
    
        if(deadLineDate==null) return true;
        return deadLineDate.after(new Date());
    }
    
    private void removeItem(int idx) {
        fxListView.getItems().remove(idx);
        keeps.remove(idx);
    }   

    private void addItem(XKeep xkeep) {
        //Keep keep = new Keep(xkeep);
    fxListView.getItems().add(xkeep);
    keeps.add(xkeep);
    }
    
    //--- On menu item selection ---//
     @FXML
    void onCVLVselected(ActionEvent event) {
        fxTextUrl.setText(ParserJsoupFactory.init().getUrl(Parser.Home));
        fxWebView.getEngine().load(ParserJsoupFactory.init().getUrl(Parser.Home));
        currentParser = Parser.Home;
    }

    @FXML
    void onNVASelected(ActionEvent event) {
        fxTextUrl.setText(ParserJsoupFactory.init().getUrl(Parser.NVA));
        fxWebView.getEngine().load(ParserJsoupFactory.init().getUrl(Parser.NVA));
        currentParser = Parser.NVA;
    }
}
