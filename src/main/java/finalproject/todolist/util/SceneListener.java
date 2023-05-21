package finalproject.todolist.util;

import finalproject.todolist.Globe;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

import java.sql.SQLException;

public class SceneListener implements ChangeListener<Scene> {
    @Override
    public void changed(ObservableValue<? extends Scene> observable, Scene oldScene, Scene newScene) {
        System.out.println("Scene changed from: " + oldScene + " to: " + newScene);
        if (newScene.equals(SceneManager.getInstance().getScene("MainPage"))) {
            try {
                TaskManager.getInstance().refreshList((VBox) Globe.getInstance().get("List"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private SceneListener() {};

    private static SceneListener instance;

    public static SceneListener getInstance() {
        if (instance == null) {
            instance = new SceneListener();
        }
        return instance;
    }
}
