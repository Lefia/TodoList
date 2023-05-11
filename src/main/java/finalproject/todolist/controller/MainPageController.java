package finalproject.todolist.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class MainPageController {
    @FXML
    private ScrollPane scrollpane;

    @FXML
    private VBox list;

    @FXML
    public void initialize() {
        list.prefWidthProperty().bind(scrollpane.widthProperty());
    }

}