package org.rb.cvlv.keeper.utils;

import java.io.File;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author raitis
 */
public class Utils {

    private Utils() {
    }

    public static File saveFileDlg(String title) {
        if (title == null || title.isEmpty()) {
            title = "Target File";
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open " + title);
        Stage stage = new Stage();
        File selection = fileChooser.showSaveDialog(stage);
        System.out.println("Selection = " + selection);
        return selection;
    }

    public static File openFileDlg(String title) {
        if (title == null || title.isEmpty()) {
            title = "Open File";
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open " + title);
        Stage stage = new Stage();
        File selection = fileChooser.showOpenDialog(stage);
        System.out.println("Selection = " + selection);
        return selection;
    }

}
