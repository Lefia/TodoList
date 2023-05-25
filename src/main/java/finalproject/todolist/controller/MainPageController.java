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
    private RadioMenuItem defaultSortItem;

    @FXML
    private void addTask(ActionEvent ignoredEvent) throws SQLException {
        DialogManager.getInstance().addTask();
    }

    @FXML
    private void addCategory(ActionEvent ignoredEvent) {
        DialogManager.getInstance().addCategory();
    }

    @FXML
    private void refresh(ActionEvent ignoredEvent) throws SQLException {
        ListManager.getInstance().showTaskList();
    }

    @FXML
    private void showDone(ActionEvent ignoredEvent) throws SQLException {
        if (showDoneItem.isSelected()) {
            Globe.getInstance().put("showDone", true);
        }
        else {
            Globe.getInstance().put("showDone", false);
        }
        ListManager.getInstance().showTaskList();
    }

    @FXML
    private void sort(ActionEvent ignoredEvent) throws SQLException {
        RadioMenuItem selectedItem = (RadioMenuItem) sortGroup.getSelectedToggle();
        Globe.getInstance().put("sortType", selectedItem.getText());
        ListManager.getInstance().showTaskList();
    }

    // 切換場景至 ChoosingFile
    @FXML
    private void backToMenu(ActionEvent ignoredEvent) {
        SceneManager.getInstance().activate("ChoosingFile");
        showDoneItem.setSelected(false);
        defaultSortItem.setSelected(true);
    }

    // 開啟檔案
    @FXML
    private void openFile(ActionEvent ignoredEvent) throws SQLException {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("資料庫 (*.db)", "*.db");
        fileChooser.getExtensionFilters().add(fileExtensions);
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setTitle("Open File");
        File file = fileChooser.showOpenDialog(scrollpane.getScene().getWindow());
        if (file != null) {
            String filePath = file.getAbsolutePath();
            DatabaseManager.getInstance().setFilePath(filePath);
            showDoneItem.setSelected(false);
            defaultSortItem.setSelected(true);
            DatabaseManager.getInstance().initialize();
            SceneManager.getInstance().activate("MainPage");
        }
    }

    @FXML
    private void initialize() {
        taskList.prefWidthProperty().bind(scrollpane.widthProperty());
        ListManager.getInstance().setTaskContainer(taskList);
        ListManager.getInstance().setCategoryContainer(categoryList);
    }
}