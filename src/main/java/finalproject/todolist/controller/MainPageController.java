package finalproject.todolist.controller;

import finalproject.todolist.Globe;
import finalproject.todolist.util.DatabaseManager;
import finalproject.todolist.util.DialogManager;
import finalproject.todolist.util.ListManager;
import finalproject.todolist.util.SceneManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.sql.SQLException;

public class MainPageController {
    @FXML
    private ScrollPane scrollpane;

    @FXML
    private VBox taskList;

    @FXML
    private VBox categoryList;

    @FXML
    private ToggleGroup sortGroup;

    @FXML
    private RadioMenuItem showDoneItem;

    @FXML
    private void addTask(ActionEvent event) throws SQLException {
        DialogManager.getInstance().addTask();
    }

    @FXML
    private void addCategory(ActionEvent event) {
        DialogManager.getInstance().addCategory();
    }

    @FXML
    private void refresh(ActionEvent event) throws SQLException {
        ListManager.getInstance().showTaskList();
    }

    @FXML
    private void showDone(ActionEvent event) throws SQLException {
        if (showDoneItem.isSelected()) {
            Globe.getInstance().put("showDone", true);
        }
        else {
            Globe.getInstance().put("showDone", false);
        }
        ListManager.getInstance().showTaskList();
    }

    @FXML
    private void sort() throws SQLException {
        RadioMenuItem selectedItem = (RadioMenuItem) sortGroup.getSelectedToggle();
        Globe.getInstance().put("sortType", selectedItem.getText());
        ListManager.getInstance().showTaskList();
    }

    @FXML
    private void backToMenu(ActionEvent event) {
        SceneManager.getInstance().activate("ChoosingFile");
    }

    @FXML
    private void openFile(ActionEvent event) throws SQLException {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("資料庫 (*.db)", "*.db");
        fileChooser.getExtensionFilters().add(fileExtensions);
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setTitle("Open File");
        File file = fileChooser.showOpenDialog(scrollpane.getScene().getWindow());
        if (file != null) {
            String filePath = file.getAbsolutePath();
            DatabaseManager.getInstance().setFilePath(filePath);
            SceneManager.getInstance().activate("MainPage");
            DatabaseManager.getInstance().initialize();
        }
    }

    @FXML
    private void initialize() {
        taskList.prefWidthProperty().bind(scrollpane.widthProperty());
        Globe.getInstance().put("taskList", taskList);
        Globe.getInstance().put("categoryList", categoryList);
    }
}