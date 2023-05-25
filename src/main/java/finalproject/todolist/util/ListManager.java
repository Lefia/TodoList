package finalproject.todolist.util;

import finalproject.todolist.Globe;
import finalproject.todolist.component.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;

public class ListManager {
    private VBox taskContainer;

    private VBox categoryContainer;

    public void setTaskContainer(VBox taskContainer) {
        this.taskContainer = taskContainer;
    }

    public void setCategoryContainer(VBox categoryContainer) {
        this.categoryContainer = categoryContainer;
    }

    // 顯示並刷新指定的任務清單
    public void showTaskList() throws SQLException {
        taskContainer.getChildren().clear();
        String currentCategory = (String) Globe.getInstance().get("currentCategory");
        ArrayList<Task> taskList = DatabaseManager.getInstance().queryTask(currentCategory);

        // 排序
        String sortType = (String) Globe.getInstance().get("sortType");
        if (sortType.equals("日期")) {
            taskList.sort(new DateComparator());
        }
        else if (sortType.equals("名稱")) {
            taskList.sort(new NameComparator());
        }

        boolean showDone = (boolean) Globe.getInstance().get("showDone");

        for (Task task : taskList) {
            if (!task.isDone()) {
                taskContainer.getChildren().add(ListManager.getInstance().createTask(task));
            }
        }
        if (showDone) {
            for (Task task : taskList) {
                if (task.isDone()) {
                    taskContainer.getChildren().add(ListManager.getInstance().createTask(task));
                }
            }
        }
    }

    // 顯示並刷新類別清單
    public void showCategoryList() throws SQLException {
        categoryContainer.getChildren().clear();
        ArrayList<String> categoryList = DatabaseManager.getInstance().getAllCategories();
        categoryContainer.getChildren().add(ListManager.getInstance().createCategory("收件箱"));
        for (String category : categoryList) {
            if (!category.equals("收件箱")) {
                categoryContainer.getChildren().add(ListManager.getInstance().createCategory(category));
            }
        }
    }

    // 在 taskList 上顯示任務
    public GridPane createTask(Task task) {
        // GridPane 樣式
        GridPane root = new GridPane();
        addColumnConstrains(root, 10);
        addColumnConstrains(root, 50);
        addColumnConstrains(root, 40);
        root.setPadding(new Insets(5));
        root.prefWidthProperty().bind(taskContainer.widthProperty());
        root.setHgap(3);
        root.setVgap(1);

        HBox hbox_1 = new HBox();
        hbox_1.setSpacing(20);
        hbox_1.setAlignment(Pos.CENTER_LEFT);

        RadioButton radioButton = new RadioButton();
        if (task.isDone()) {
            radioButton.setSelected(true);
        }
        radioButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            try {
                DatabaseManager.getInstance().editTask(task, new Task(task.getId(), task.getName(), task.getDescription(), task.getDate(), newValue, task.getCategory()));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        GridPane.setValignment(radioButton, VPos.BOTTOM);

        VBox labelsContainer = new VBox();
        Label name = new Label(task.getName());
        Label description = new Label(task.getDescription());
        description.getStyleClass().add("wrap-text");
        labelsContainer.getChildren().addAll(name, description);

        hbox_1.getChildren().addAll(radioButton, labelsContainer);
        root.add(hbox_1, 1, 0, 1, 1);

        HBox hbox_2 = new HBox();
        Label date = new Label(task.getDate());
        Label category = new Label(task.getCategory());
        hbox_2.getChildren().addAll(category, date);
        hbox_2.setSpacing(30);
        root.add(hbox_2, 2, 0, 1, 1);

        // 右鍵選單
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
        });

        contextMenu.getItems().addAll(editMenuItem, deleteMenuItem);
        contextMenu.setOnHidden(event -> root.setStyle(""));

        root.setOnContextMenuRequested(event -> {
            contextMenu.show(root, event.getScreenX(), event.getScreenY());
            root.setStyle("-fx-background-color: #5eb9ff;" +
                          "-fx-border-width: 2px;" +
                          "-fx-border-radius: 5px");
        });

        if (task.isDone()) {
            root.setStyle("-fx-opacity: 0.5");
        }

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
                ListManager.getInstance().showTaskList();
                ListManager.getInstance().showCategoryList();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        if (Globe.getInstance().get("currentCategory").equals(category)) {
            button.setStyle("-fx-background-color: #8accff;" +
                            "-fx-border-width: 2px;" +
                            "-fx-border-radius: 5px");
        }

        if (!category.equals("收件箱")) {
            ContextMenu contextMenu = new ContextMenu();

            MenuItem editMenuItem = new MenuItem("編輯");
            editMenuItem.setOnAction(event -> DialogManager.getInstance().editCategory(category));

            MenuItem deleteMenuItem = new MenuItem("刪除");
            deleteMenuItem.setOnAction(event ->
                DialogManager.getInstance().showWarningAlertWithButton("確定要刪除？", actionHandler -> {
                    try {
                        DatabaseManager.getInstance().deleteCategory(category);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                })
            );

            contextMenu.getItems().addAll(editMenuItem, deleteMenuItem);
            contextMenu.setOnHidden(event -> button.setStyle(""));

            button.setOnContextMenuRequested(event -> {
                contextMenu.show(button, event.getScreenX(), event.getScreenY());
                button.setStyle("-fx-background-color: #5eb9ff;" +
                                "-fx-border-width: 2px;" +
                                "-fx-border-radius: 5px");
            });
        }

        return button;
    }

    // 對日期進行排序
    static class DateComparator implements Comparator<Task> {
        @Override
        public int compare(Task t1, Task t2) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(t1.getDate(), formatter).compareTo(LocalDate.parse(t2.getDate(), formatter));
        }
    }

    // 對名字進行排序
    static class NameComparator implements Comparator<Task> {
        @Override
        public int compare(Task t1, Task t2) {
            return t1.getName().compareTo(t2.getName());
        }
    }

    // 新增 column constrains
    private void addColumnConstrains(GridPane root, double width) {
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(width);
        columnConstraints.setHgrow(Priority.SOMETIMES);
        root.getColumnConstraints().add(columnConstraints);
    }

    /**** 單例 *****/
    private ListManager() {}

    private static ListManager instance;

    public static ListManager getInstance() {
        if (instance == null) {
            instance = new ListManager();
        }
        return instance;
    }
}
