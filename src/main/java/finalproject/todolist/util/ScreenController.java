package finalproject.todolist.util;

import finalproject.todolist.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.HashMap;

public class ScreenController {
    private Scene scene;
    public String currentScreen = "MainPage";
    private final HashMap<String, Pane> screenMap = new HashMap<>() {{
        try {
            // 由於 ScreenController 在 subpackage 中，因此在 getResource 時可以將其改成絕對路徑，以免發生抓不到檔案的錯誤
            put("MainPage", new FXMLLoader(ScreenController.class.getResource("/finalproject/todolist/MainPage.fxml")).load());
            put("ChoosingFile", new FXMLLoader(ScreenController.class.getResource("/finalproject/todolist/ChoosingFile.fxml")).load());
            put("CreateNewFile", new FXMLLoader(Main.class.getResource("CreateNewFile.fxml")).load());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }};
    public void setScene(Scene scene) {
        this.scene = scene;
    }
    public void addScreen(String name, Pane pane){
        screenMap.put(name, pane);
    }
    public void removeScreen(String name){
        screenMap.remove(name);
    }
    public void activate(String name) {
        scene.setRoot(screenMap.get(name));
        currentScreen = name;
    }

    // Singleton
    private ScreenController() {}
    public static ScreenController instance = new ScreenController();
    public static ScreenController getInstance() {
        return instance;
    }
}
