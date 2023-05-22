package finalproject.todolist.controller;

import finalproject.todolist.Globe;
import finalproject.todolist.util.DialogManager;
import finalproject.todolist.util.TaskManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.sql.SQLException;

public class MainPageController {
    @FXML
    private ScrollPane scrollpane;

    @FXML
    private VBox list;

    @FXML
    public void add(ActionEvent e) {
        DialogManager.getInstance().addTask();
    }

    @FXML
    public void refresh(ActionEvent e) throws SQLException {
        TaskManager.getInstance().refreshList(list);
    }

    @FXML
    public void initialize() {
        list.prefWidthProperty().bind(scrollpane.widthProperty());
        Globe.getInstance().add("List", list);
    }
}