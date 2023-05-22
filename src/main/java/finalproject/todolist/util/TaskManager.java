package finalproject.todolist.util;

import finalproject.todolist.component.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class TaskManager {
    // 刷新任務清單
    public void refreshList(VBox list) throws SQLException {
        if (DatabaseManager.getInstance().checkTableExists("Inbox")) {
            list.getChildren().clear();
            ArrayList<Task> taskList = DatabaseManager.getInstance().query();
            for (Task task : taskList) {
                list.getChildren().add(TaskManager.getInstance().createTask(list, task));
            }
        }
    }

    // 在 list 上顯示任務
    public GridPane createTask(VBox list, Task task) {
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
        Label name = new Label(task.getName());
        Label description = new Label(task.getDescription());
        labelsContainer.getChildren().addAll(name, description);

        hbox.getChildren().addAll(radioButton, labelsContainer);

        root.add(hbox, 1, 0, 2, 1);

        Label date = new Label(task.getDate());
        GridPane.setValignment(radioButton, VPos.BOTTOM);
        root.add(date, 3, 0, 1, 1);

        ContextMenu contextMenu = new ContextMenu();

        MenuItem editMenuItem = new MenuItem("編輯");
        editMenuItem.setOnAction(event -> {
            DialogManager.getInstance().editTask(task);
        });

        MenuItem deleteMenuItem = new MenuItem("刪除");
        deleteMenuItem.setOnAction(event -> {
            try {
                DatabaseManager.getInstance().deleteTask(task);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            list.getChildren().remove(root);
        });

        MenuItem showIdMenuItem = new MenuItem("輸出ID");
        showIdMenuItem.setOnAction(event -> {
            System.out.println(task.getId());
        });

        contextMenu.getItems().addAll(editMenuItem, deleteMenuItem, showIdMenuItem);
        contextMenu.setOnHidden(event -> {
            root.setStyle("");
        });

        root.setOnContextMenuRequested(event -> {
            contextMenu.show(root, event.getScreenX(), event.getScreenY());
            root.setStyle("-fx-background-color: #5eb9ff;" +
                          "-fx-border-width: 2px;" +
                          "-fx-border-radius: 5px");
        });

        return root;
    }

    // 新增 column constrains
    private void addColumnConstrains(GridPane root, double width) {
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(width);
        columnConstraints.setHgrow(Priority.SOMETIMES);
        root.getColumnConstraints().add(columnConstraints);
    }

    /* Singleton */
    private TaskManager() {}

    private static TaskManager instance;

    public static TaskManager getInstance() {
        if (instance == null) {
            instance = new TaskManager();
        }
        return instance;
    }
}
