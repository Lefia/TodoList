package finalproject.todolist.controller;

import finalproject.todolist.util.DatabaseController;
import finalproject.todolist.util.ScreenController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import java.io.File;

public class CreateNewFileController {
    private static String filePath;
    @FXML
    private Label path;
    @FXML
    private Label message;
    @FXML
    private AnchorPane root;
    @FXML
    private TextField name;
    @FXML
    public void back(ActionEvent event) {
        ScreenController screenController = ScreenController.getInstance();
        screenController.activate("ChoosingFile");
    }
    @FXML
    public void browse(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Folder");
        File directory = directoryChooser.showDialog(root.getScene().getWindow());
        if (directory != null) {
            filePath = directory.getAbsolutePath();
            path.setText(filePath);

        }
    }
    @FXML
    public void create(ActionEvent event) {
        if (filePath != null) {
            DatabaseController databaseController = DatabaseController.getInstance();
            if (!name.getText().trim().isEmpty()) {
                databaseController.setLocation(filePath, name.getText());
            }
            else {
                databaseController.setLocation(filePath, "NewList");
            }
            System.out.println(databaseController.getLocation());
            ScreenController screenController = ScreenController.getInstance();
            screenController.activate("MainPage");
        } else {
            message.setText("Please select the folder!");
            message.setTextFill(Color.RED);
        }
    }
}
