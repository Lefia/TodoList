package finalproject.todolist.controller;

import finalproject.todolist.util.*;

import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.fxml.FXML;

import java.io.File;


public class ChoosingFileController {
    @FXML
    private AnchorPane root;

    // 新增檔案 (切換到 CreateNewFile)
    @FXML
    public void create(ActionEvent ignoredEvent) {
        SceneManager.getInstance().activate("CreateNewFile");
    }

    // 開啟檔案
    @FXML
    public void open(ActionEvent ignoredEvent){
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