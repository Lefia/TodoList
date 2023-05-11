package finalproject.todolist.controller;

import finalproject.todolist.util.DatabaseController;
import finalproject.todolist.util.ScreenController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChoosingFileController {
    @FXML
    private AnchorPane root;
    @FXML
    private Label message;
    @FXML
    public void create(ActionEvent event) {
        ScreenController screenController = ScreenController.getInstance();
        screenController.activate("CreateNewFile");
    }

    @FXML
    public void open(ActionEvent event) {
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
            DatabaseController databaseController = DatabaseController.getInstance();
            databaseController.setLocation(filePath);
            System.out.println(databaseController.getLocation());

            ScreenController screenController = ScreenController.getInstance();
            screenController.activate("MainPage");
        }
    }
    private Boolean isDatabase(String filePath) {
        Pattern pattern = Pattern.compile("\\.db$");
        Matcher matcher = pattern.matcher(filePath);
        return matcher.find();
    }
}