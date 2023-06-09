package finalproject.todolist.util;

import finalproject.todolist.component.Task;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.function.Consumer;

public class DialogManager {

    /**** 任務 ****/

    // 新增任務
    public void addTask() throws SQLException {
        // DialogPane 樣式
        DialogPane dialogPane = new DialogPane();
        dialogPane.setPrefWidth(250);
        dialogPane.setHeaderText("新增任務");

        // GridPane 樣式
        GridPane root = new GridPane();
        addColumnConstrains(root);
        addColumnConstrains(root);
        root.setVgap(4);
        root.setHgap(2);
        root.setPadding(new Insets(10));
        root.prefWidthProperty().bind(dialogPane.widthProperty());

        // 任務名稱
        Label nameLabel = new Label("任務名稱");
        GridPane.setValignment(nameLabel, VPos.TOP);
        root.add(nameLabel, 0, 0);
        TextField nameTextField = new TextField("新任務");
        root.add(nameTextField, 1, 0);

        // 任務日期
        Label dateLabel = new Label("日期");
        GridPane.setValignment(dateLabel, VPos.TOP);
        root.add(dateLabel, 0, 1);
        DatePicker datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now());
        root.add(datePicker, 1, 1);

        // 任務類別
        Label categoryLabel = new Label("類別");
        GridPane.setValignment(categoryLabel, VPos.TOP);
        root.add(categoryLabel, 0, 2);
        ComboBox<String> combobox= new ComboBox<>();
        for (String category : DatabaseManager.getInstance().getAllCategories()) {
            combobox.getItems().add(category);
        }
        combobox.setValue("收件箱");
        combobox.setVisibleRowCount(5);
        root.add(combobox, 1, 2);

        // 任務描述
        Label descriptionLabel = new Label("描述");
        GridPane.setValignment(descriptionLabel, VPos.TOP);
        root.add(descriptionLabel, 0, 3);
        TextArea descriptionTextArea = new TextArea();
        descriptionTextArea.setPromptText("這是一個描述");
        descriptionTextArea.setPrefHeight(50);
        root.add(descriptionTextArea, 1, 3);

        dialogPane.setContent(root);

        ButtonType applyButton = new ButtonType("確認", ButtonBar.ButtonData.APPLY);
        ButtonType closeButton = new ButtonType("取消", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialogPane.getButtonTypes().addAll(closeButton, applyButton);

        dialogPane.lookupButton(applyButton).addEventHandler(ActionEvent.ACTION, e -> {
            try {
                if (nameTextField.getText().trim().equals("")) {
                    showWarningAlert("新增任務失敗：任務名稱不得為空白");
                }
                else {
                    Task task = new Task(randomString(),
                                         nameTextField.getText().trim(),
                                         descriptionTextArea.getText().trim(),
                                         datePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                                    false,
                                         combobox.getValue());
                    DatabaseManager.getInstance().addTask(task);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        Alert dialog = new Alert(Alert.AlertType.NONE);
        dialog.setDialogPane(dialogPane);

        dialog.show();
    }

    // 編輯任務
    public void editTask(Task oldTask) throws SQLException {
        // DialogPane 樣式
        DialogPane dialogPane = new DialogPane();
        dialogPane.setPrefWidth(250);
        dialogPane.setHeaderText("編輯任務");

        // GridPane 樣式
        GridPane root = new GridPane();
        addColumnConstrains(root);
        addColumnConstrains(root);
        root.setVgap(3);
        root.setHgap(2);
        root.setPadding(new Insets(5));
        root.prefWidthProperty().bind(dialogPane.widthProperty());

        // 任務名稱
        Label nameLabel = new Label("任務名稱");
        GridPane.setValignment(nameLabel, VPos.TOP);
        root.add(nameLabel, 0, 0);
        TextField nameTextField = new TextField(oldTask.getName());
        root.add(nameTextField, 1, 0);

        // 任務日期
        Label dateLabel = new Label("日期");
        GridPane.setValignment(dateLabel, VPos.TOP);
        root.add(dateLabel, 0, 1);
        DatePicker datePicker = new DatePicker();
        datePicker.setValue(LocalDate.parse(oldTask.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        root.add(datePicker, 1, 1);

        // 任務類別
        Label categoryLabel = new Label("類別");
        GridPane.setValignment(categoryLabel, VPos.TOP);
        root.add(categoryLabel, 0, 2);
        ComboBox<String> combobox= new ComboBox<>();
        for (String category : DatabaseManager.getInstance().getAllCategories()) {
            combobox.getItems().add(category);
        }
        combobox.setValue(oldTask.getCategory());
        combobox.setVisibleRowCount(5);
        root.add(combobox, 1, 2);

        // 任務描述
        Label descriptionLabel = new Label("描述");
        GridPane.setValignment(descriptionLabel, VPos.TOP);
        root.add(descriptionLabel, 0, 3);
        TextArea descriptionTextArea = new TextArea(oldTask.getDescription());
        descriptionTextArea.setPrefHeight(50);
        root.add(descriptionTextArea, 1, 3);

        dialogPane.setContent(root);

        ButtonType applyButton = new ButtonType("確認", ButtonBar.ButtonData.APPLY);
        ButtonType closeButton = new ButtonType("取消", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialogPane.getButtonTypes().addAll(closeButton, applyButton);

        dialogPane.lookupButton(applyButton).addEventHandler(ActionEvent.ACTION, e -> {
            try {
                if (nameTextField.getText().trim().equals("")) {
                    showWarningAlert("修改任務名稱失敗：任務名稱不得為空白");
                }
                else {
                    Task newTask = new Task(oldTask.getId(),
                            nameTextField.getText(),
                            descriptionTextArea.getText(),
                            datePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                            oldTask.isDone(),
                            combobox.getValue());
                    DatabaseManager.getInstance().editTask(oldTask, newTask);

                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        Alert dialog = new Alert(Alert.AlertType.NONE);
        dialog.setDialogPane(dialogPane);

        dialog.show();
    }

    /**** 類別 ****/

    // 新增類別
    public void addCategory() {
        // DialogPane 樣式
        DialogPane dialogPane = new DialogPane();
        dialogPane.setPrefWidth(250);
        dialogPane.setHeaderText("新增類別");

        // GridPane 樣式
        GridPane root = new GridPane();
        addColumnConstrains(root);
        addColumnConstrains(root);
        root.setVgap(2);
        root.setHgap(2);
        root.setPadding(new Insets(5));
        root.prefWidthProperty().bind(dialogPane.widthProperty());

        // 類別名稱
        Label nameLabel = new Label("類別名稱");
        root.add(nameLabel, 0, 0);
        TextField nameTextField = new TextField("新類別");
        root.add(nameTextField, 1, 0);

        dialogPane.setContent(root);

        ButtonType applyButton = new ButtonType("確認", ButtonBar.ButtonData.APPLY);
        ButtonType closeButton = new ButtonType("取消", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialogPane.getButtonTypes().addAll(closeButton, applyButton);

        dialogPane.lookupButton(applyButton).addEventHandler(ActionEvent.ACTION, event -> {
            try {
                if (nameTextField.getText().trim().equals("")) {
                    showWarningAlert("新增類別失敗：類別名稱不得為空白");
                }
                else if (DatabaseManager.getInstance().checkCategoryExists(nameTextField.getText().trim())) {
                    showWarningAlert("新增類別失敗：重複的類別名稱");
                }
                else if (nameTextField.getText().trim().matches("^[0-9].*$")) {
                    showWarningAlert("新增類別失敗：類別名稱不能以數字開頭");
                }
                else {
                    DatabaseManager.getInstance().createCategory(nameTextField.getText().trim());
                    ListManager.getInstance().showCategoryList();
                }

            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        Alert dialog = new Alert(Alert.AlertType.NONE);
        dialog.setDialogPane(dialogPane);

        dialog.show();
    }

    // 編輯類別
    public void editCategory(String category) {
        // DialogPane 樣式
        DialogPane dialogPane = new DialogPane();
        dialogPane.setPrefWidth(250);
        dialogPane.setHeaderText("修改類別名稱");

        // GridPane 樣式
        GridPane root = new GridPane();
        addColumnConstrains(root);
        addColumnConstrains(root);
        root.setVgap(2);
        root.setHgap(2);
        root.setPadding(new Insets(5));
        root.prefWidthProperty().bind(dialogPane.widthProperty());

        // 類別名稱
        Label nameLabel = new Label("類別名稱");
        root.add(nameLabel, 0, 0);
        TextField nameTextField = new TextField(category);
        root.add(nameTextField, 1, 0);

        dialogPane.setContent(root);

        ButtonType applyButton = new ButtonType("確認", ButtonBar.ButtonData.APPLY);
        ButtonType closeButton = new ButtonType("取消", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialogPane.getButtonTypes().addAll(closeButton, applyButton);

        dialogPane.lookupButton(applyButton).addEventHandler(ActionEvent.ACTION, event -> {
            try {
                System.out.println(System.getProperty("user.home"));
                if (nameTextField.getText().trim().equals("")) {
                    showWarningAlert("修改類別名稱失敗：類別名稱不得為空白");
                }
                else if (DatabaseManager.getInstance().checkCategoryExists(nameTextField.getText().trim())) {
                    showWarningAlert("修改類別失敗：重複的類別名稱");
                }
                else if (nameTextField.getText().trim().matches("^[0-9].*$")) {
                    showWarningAlert("新增類別失敗：類別名稱不能以數字開頭");
                }
                else {
                    DatabaseManager.getInstance().renameCategory(category, nameTextField.getText().trim());
                    ListManager.getInstance().showCategoryList();
                }

            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        Alert dialog = new Alert(Alert.AlertType.NONE);
        dialog.setDialogPane(dialogPane);

        dialog.show();
    }

    /**** 警告視窗 ****/

    // 顯示警告視窗
    public void showWarningAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("警告");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // 顯示有按鈕的警告視窗
    public void showWarningAlertWithButton(String message, Consumer<ButtonType> actionHandler) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("警告");
        alert.setHeaderText(null);
        alert.setContentText(message);

        ButtonType applyButton = new ButtonType("確認");
        ButtonType closeButton = new ButtonType("取消");
        alert.getButtonTypes().setAll(closeButton, applyButton);

        alert.showAndWait().ifPresent(button -> {
            if (button == applyButton) {
                actionHandler.accept(button);
            }
        });
    }

    /**** 私有方法 ****/

    // 隨機生成由英文字母和數自組成的字串
    private String randomString() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder stringBuilder = new StringBuilder(10);
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(characters.length());
            char randomChar = characters.charAt(index);
            stringBuilder.append(randomChar);
        }

        return stringBuilder.toString();
    }

    // 新增 ColumnConstrains 到 root，用於上面的 GridPane
    private void addColumnConstrains(GridPane root) {
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(50);
        columnConstraints.setHgrow(Priority.SOMETIMES);
        root.getColumnConstraints().add(columnConstraints);
    }

    /**** 單例 ****/
    private DialogManager() {}

    public static DialogManager instance;

    public static DialogManager getInstance() {
        if (instance == null) {
            instance = new DialogManager();
        }
        return instance;
    }
}
