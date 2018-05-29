package org.rb.cvlv.keeper;

import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainApp extends Application {

     private static MainApp app;
     private static Stage primaryStage; // **Declare static Stage**

    private void setPrimaryStage(Stage stage) {
        MainApp.primaryStage = stage;
    }
    private void setApp(MainApp app){
      MainApp.app = app;
    }
    
    public static Stage getPrimaryStage(){
        return primaryStage;
    }
    public static MainApp getMainApp(){
       return app;
    }
    
    private void loadScene(Stage stage) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/FXMLDocument.fxml"));
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        
        stage.setTitle("JavaFX CV.LV keeper");
       
        stage.setScene(scene);
        stage.show();
    }
    
    public void loadMainStage() throws IOException{
        loadScene(getPrimaryStage());
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        setPrimaryStage(stage); // **Set the Stage**
        setApp(this);
        loadScene(stage);
        }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
