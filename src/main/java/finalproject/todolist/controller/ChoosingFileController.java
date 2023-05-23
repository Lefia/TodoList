package finalproject.todolist.controller;

import finalproject.todolist.util.*;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.fxml.FXML;

import java.io.File;


public class ChoosingFileController {
    @FXML
    private AnchorPane root;
    @FXML
    private Label message;
    @FXML
    public void create(ActionEvent event) {
        SceneManager.getInstance().activate("CreateNewFile");
    }

    @FXML
    public void open(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("資料庫 (*.db)", "*.db");
        fileChooser.getExtensionFilters().add(fileExtensions);
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setTitle("Open File");
        File file = fileChooser.showOpenDialog(root.getScene().getWindow());
        if (file != null) {
            String filePath = file.getAbsolutePath();
            DatabaseManager.getInstance().setFilePath(filePath);
            SceneManager.getInstance().activate("MainPage");
        }
    }
}