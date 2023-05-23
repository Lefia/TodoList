package finalproject.todolist.util;

import finalproject.todolist.Globe;
import finalproject.todolist.component.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.sql.SQLException;
import java.util.ArrayList;

public class ListManager {
    // 顯示並刷新指定的任務清單
    public void showTaskList(VBox list, String currentCategory) throws SQLException {
        list.getChildren().clear();
        ArrayList<Task> taskList = DatabaseManager.getInstance().queryTask(currentCategory);
        for (Task task : taskList) {
            list.getChildren().add(ListManager.getInstance().createTask(list, task));
        }
    }

    // 顯示並刷新全部的任務
    public void showAllTaskList(VBox list) throws SQLException {
        list.getChildren().clear();
        ArrayList<String> categoryList = DatabaseManager.getInstance().getAllCategories();
        for (String category : categoryList) {
            ArrayList<Task> taskList = DatabaseManager.getInstance().queryTask(category);
            for (Task task : taskList) {
                list.getChildren().add(ListManager.getInstance().createTask(list, task));
            }

        }
    }

    // 顯示並刷新類別清單
    public void showCategoryList(VBox list) throws SQLException {
        list.getChildren().clear();
        ArrayList<String> categoryList = DatabaseManager.getInstance().getAllCategories();
        for (String category : categoryList) {
            list.getChildren().add(ListManager.getInstance().createCategory(category));
        }
    }

    // 在 taskList 上顯示任務
    public GridPane createTask(VBox list, Task task) {
        GridPane root = new GridPane();
        addColumnConstrains(root, 10);
        addColumnConstrains(root, 50);
        addColumnConstrains(root, 40);
        root.setPadding(new Insets(5));
        root.prefWidthProperty().bind(list.widthProperty());
        root.setHgap(3);
        root.setVgap(1);

        HBox hbox_1 = new HBox();
        hbox_1.setSpacing(20);
        hbox_1.setAlignment(Pos.CENTER_LEFT);

        RadioButton radioButton = new RadioButton();
        radioButton.setOnAction(event -> {
            if (radioButton.isSelected()) {
                list.getChildren().remove(root);
            }
        });
        GridPane.setValignment(radioButton, VPos.BOTTOM);

        VBox labelsContainer = new VBox();
        Label name = new Label(task.getName());
        Label description = new Label(task.getDescription());
        labelsContainer.getChildren().addAll(name, description);

        hbox_1.getChildren().addAll(radioButton, labelsContainer);
        root.add(hbox_1, 1, 0, 1, 1);

        HBox hbox_2 = new HBox();
        Label date = new Label(task.getDate());
        Label category = new Label(task.getCategory());
        hbox_2.getChildren().addAll(category, date);
        hbox_2.setSpacing(30);
        root.add(hbox_2, 2, 0, 1, 1);

        ContextMenu contextMenu = new ContextMenu();

        MenuItem editMenuItem = new MenuItem("編輯");
        editMenuItem.setOnAction(event -> {
            try {
                DialogManager.getInstance().editTask(task);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
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

    // 在  categoryList 上顯示類別
    public Button createCategory(String category) {
        Button button = new Button();
        button.setText(category);
        button.setPrefWidth(100);
        button.setOnAction(event -> {
            try {
                Globe.getInstance().put("currentCategory", category);
                ListManager.getInstance().showTaskList((VBox) Globe.getInstance().get("taskList"), (String) Globe.getInstance().get("currentCategory"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        if (!category.equals("Inbox")) {
            ContextMenu contextMenu = new ContextMenu();

            MenuItem editMenuItem = new MenuItem("編輯");
            editMenuItem.setOnAction(event -> {
                DialogManager.getInstance().editCategory(category);
            });

            MenuItem deleteMenuItem = new MenuItem("刪除");
            deleteMenuItem.setOnAction(event -> {
                DialogManager.getInstance().showWarningAlertWithButton("確定要刪除？", actionHandler -> {
                    try {
                        DatabaseManager.getInstance().deleteCategory(category);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            });

            contextMenu.getItems().addAll(editMenuItem, deleteMenuItem);
            contextMenu.setOnHidden(event -> {
                button.setStyle("");
            });

            button.setOnContextMenuRequested(event -> {
                contextMenu.show(button, event.getScreenX(), event.getScreenY());
                button.setStyle("-fx-background-color: #5eb9ff;" +
                        "-fx-border-width: 2px;" +
                        "-fx-border-radius: 5px");
            });
        }

        return button;
    }

    // 新增 column constrains
    private void addColumnConstrains(GridPane root, double width) {
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(width);
        columnConstraints.setHgrow(Priority.SOMETIMES);
        root.getColumnConstraints().add(columnConstraints);
    }

    /* Singleton */
    private ListManager() {}

    private static ListManager instance;

    public static ListManager getInstance() {
        if (instance == null) {
            instance = new ListManager();
        }
        return instance;
    }
}