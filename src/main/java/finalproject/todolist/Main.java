package finalproject.todolist;

import finalproject.todolist.util.*;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        SceneManager.getInstance().setStage(stage);
        SceneManager.getInstance().activate("ChoosingFile");
        stage.sceneProperty().addListener(SceneListener.getInstance());
        stage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResource("icons/todo-list.png")).toString()));
        stage.setTitle("TodoList");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}