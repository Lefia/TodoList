package finalproject.todolist.util;

import finalproject.todolist.component.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.*;

import java.sql.SQLException;
import java.util.ArrayList;

public class TaskManager {
    public void refreshList(VBox list) throws SQLException {
        list.getChildren().clear();
        ArrayList<Task> taskList = DatabaseManager.getInstance().query();
        for (Task task : taskList) {
            list.getChildren().add(TaskManager.getInstance().createTask(list, task.getName(), task.getDescription(), task.getDate()));
        }
    }
    public GridPane createTask(VBox list, String taskName, String taskDescription, String taskDate) {
        GridPane root = new GridPane();
        addColumnConstrains(root, 15);
        addColumnConstrains(root, 30);
        addColumnConstrains(root, 30);
        addColumnConstrains(root, 25);
        root.setPadding(new Insets(5));
        root.prefWidthProperty().bind(list.widthProperty());
        root.setHgap(4);
        root.setVgap(1);

        HBox hbox = new HBox();
        hbox.setSpacing(20);
        hbox.setAlignment(Pos.CENTER_LEFT);

        RadioButton radioButton = new RadioButton();
        radioButton.setOnAction(event -> {
            if (radioButton.isSelected()) {
                list.getChildren().remove(root);
            }
        });
        radioButton.setAlignment(Pos.CENTER_LEFT);

        VBox labelsContainer = new VBox();
        Label name = new Label(taskName);
        Label description = new Label(taskDescription);
        labelsContainer.getChildren().addAll(name, description);

        hbox.getChildren().addAll(radioButton, labelsContainer);

        root.add(hbox, 1, 0, 2, 1);

        Label date = new Label(taskDate);
        GridPane.setValignment(radioButton, VPos.BOTTOM);
        root.add(date, 3, 0, 1, 1);


        return root;
    }

    private void addColumnConstrains(GridPane root, double width) {
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(width);
        columnConstraints.setHgrow(Priority.SOMETIMES);
        root.getColumnConstraints().add(columnConstraints);
    }

    private TaskManager() {}

    private static TaskManager instance;

    public static TaskManager getInstance() {
        if (instance == null) {
            instance = new TaskManager();
        }
        return instance;
    }
}
