package finalproject.todolist;

import finalproject.todolist.util.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("CreateNewFile.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        ScreenController screenController = ScreenController.getInstance();
        screenController.setScene(scene);
        screenController.activate("ChoosingFile");
        stage.setTitle("TodoList");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}