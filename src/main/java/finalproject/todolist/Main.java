package finalproject.todolist;

import finalproject.todolist.util.*;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        SceneManager.getInstance().setStage(stage);
        SceneManager.getInstance().activate("ChoosingFile");
        stage.sceneProperty().addListener(SceneListener.getInstance());
        stage.setTitle("TodoList");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}