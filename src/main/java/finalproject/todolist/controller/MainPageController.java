package finalproject.todolist.controller;

import finalproject.todolist.Globe;
import finalproject.todolist.util.DialogManager;
import finalproject.todolist.util.ListManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.sql.SQLException;

public class MainPageController {
    @FXML
    private ScrollPane scrollpane;

    @FXML
    private VBox taskList;

    @FXML
    private VBox categoryList;

    @FXML
    private void addTask(ActionEvent event) throws SQLException {
        DialogManager.getInstance().addTask();
    }

    @FXML
    private void addCategory(ActionEvent event) throws SQLException {
        DialogManager.getInstance().addCategory();
    }

    @FXML
    private void refresh(ActionEvent e) throws SQLException {
        ListManager.getInstance().showTaskList(taskList, (String) Globe.getInstance().get("currentCategory"));
    }

    @FXML
    private void initialize() {
        taskList.prefWidthProperty().bind(scrollpane.widthProperty());
        Globe.getInstance().put("taskList", taskList);
        Globe.getInstance().put("categoryList", categoryList);
    }
}