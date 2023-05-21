package finalproject.todolist.util;

import finalproject.todolist.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.HashMap;

public class SceneManager {
    private Stage stage;

    public String currentScene = "MainPage";

    private final HashMap<String, Scene> sceneMap = new HashMap<>();

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void addScene(String name) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(name + ".fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        sceneMap.put(name, scene);
    }

    public Scene getScene(String name) {
        return sceneMap.get(name);
    }

    public void removeScene(String name){
        sceneMap.remove(name);
    }

    public void activate(String name) {
        stage.setScene(sceneMap.get(name));
        currentScene = name;
    }

    /* 單例 Singleton */

    // Constructor
    private SceneManager() throws IOException {
        addScene("ChoosingFile");
        addScene("CreateNewFile");
        addScene("MainPage");
    }

    // Instance for singleton
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
