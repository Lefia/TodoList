package finalproject.todolist.controller;

import finalproject.todolist.Globe;
import finalproject.todolist.util.DatabaseManager;
import finalproject.todolist.util.SceneManager;

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
    public void back(ActionEvent ignoredEvent) {
        SceneManager.getInstance().activate("ChoosingFile");
    }
    @FXML
    public void browse(ActionEvent ignoredEvent) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        directoryChooser.setTitle("選擇資料夾");
        File directory = directoryChooser.showDialog(root.getScene().getWindow());
        if (directory != null) {
            filePath = directory.getAbsolutePath();
            path.setText(filePath);

        }
    }
    @FXML
    public void create(ActionEvent ignoredEvent){
        if (filePath != null) {
            if (!name.getText().trim().isEmpty()) {
                DatabaseManager.getInstance().setFilePath(filePath, name.getText());
            }
            else {
                DatabaseManager.getInstance().setFilePath(filePath, "NewList");
            }
            SceneManager.getInstance().activate("MainPage");
        } else {
            message.setText("請選擇資料夾！");
            message.setTextFill(Color.RED);
        }
    }

    @FXML
    private void initialize() {
        Globe.getInstance().put("createFileMessage", message);
        Globe.getInstance().put("createFileName", name);
    }
}
