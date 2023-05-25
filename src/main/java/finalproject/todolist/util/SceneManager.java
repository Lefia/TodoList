package finalproject.todolist.util;

import finalproject.todolist.Main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class SceneManager {
    private Stage stage;

    private final HashMap<String, Scene> sceneMap = new HashMap<>();

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void addScene(String name) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(name + ".fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        sceneMap.put(name, scene);
    }

    public void addScene(String name, String stylesheet) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(name + ".fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        scene.getStylesheets().add(Objects.requireNonNull(Main.class.getResource(stylesheet + ".css")).toExternalForm());
        sceneMap.put(name, scene);
    }

    public Scene getScene(String name) {
        return sceneMap.get(name);
    }

    // 切換場景
    public void activate(String name) {
        stage.setScene(sceneMap.get(name));
    }

    /**** 單例 ****/
    private SceneManager() throws IOException {
        addScene("ChoosingFile");
        addScene("CreateNewFile");
        addScene("MainPage", "stylesheet");
    }

    public static SceneManager instance;

    public static SceneManager getInstance() {
        try {
            if (instance == null) {
                instance = new SceneManager();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return instance;
    }
}
