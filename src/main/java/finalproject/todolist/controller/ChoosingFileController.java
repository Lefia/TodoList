package finalproject.todolist.controller;

import finalproject.todolist.util.*;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
        fileChooser.setTitle("Open File");
        File file = fileChooser.showOpenDialog(root.getScene().getWindow());
        if (file != null) {
            String filePath = file.getAbsolutePath();
            if (!isDatabase(filePath)) {
                message.setText("Select a database file!");
                message.setTextFill(Color.RED);
                message.setVisible(true);
                return;
            }
            DatabaseManager.getInstance().setFilePath(filePath);
            SceneManager.getInstance().activate("MainPage");
        }
    }
    private Boolean isDatabase(String filePath) {
        Pattern pattern = Pattern.compile("\\.db$");
        Matcher matcher = pattern.matcher(filePath);
        return matcher.find();
    }
}