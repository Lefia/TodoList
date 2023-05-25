package finalproject.todolist.util;

import finalproject.todolist.Globe;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class SceneListener implements ChangeListener<Scene> {
    @Override
    public void changed(ObservableValue<? extends Scene> observable, Scene oldScene, Scene newScene) {
        if (newScene.equals(SceneManager.getInstance().getScene("MainPage"))) {
            try {
                DatabaseManager.getInstance().initialize();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else if (newScene.equals(SceneManager.getInstance().getScene("CreateNewFile"))
                && oldScene.equals(SceneManager.getInstance().getScene("ChoosingFile"))) {
            Label createFileMessage = (Label) Globe.getInstance().get("createFileMessage");
            createFileMessage.setText("");
            TextField createFileName = (TextField) Globe.getInstance().get("createFileName");
            createFileName.setText("");
        }

    }

    private SceneListener() {}

    private static SceneListener instance;

    public static SceneListener getInstance() {
        if (instance == null) {
            instance = new SceneListener();
        }
        return instance;
    }
}
