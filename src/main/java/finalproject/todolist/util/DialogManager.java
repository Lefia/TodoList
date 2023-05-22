package finalproject.todolist.util;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import finalproject.todolist.component.Task;

public class DialogManager {
    public void addTask() {
        DialogPane dialogPane = new DialogPane();
        dialogPane.setPrefWidth(250);

        dialogPane.setHeaderText("新增任務");

        GridPane root = new GridPane();
        addColumnConstrains(root, 50);
        addColumnConstrains(root, 50);
        root.setVgap(3);
        root.setHgap(2);
        root.setPadding(new Insets(5));
        root.prefWidthProperty().bind(dialogPane.widthProperty());

        Label nameLabel = new Label("任務名稱");
        root.add(nameLabel, 0, 0);
        TextField nameTextField = new TextField("新任務");
        root.add(nameTextField, 1, 0);

        Label descLabel = new Label("描述");
        root.add(descLabel, 0, 1);
        TextField descTextField = new TextField("描述");
        root.add(descTextField, 1, 1);

        Label dateLabel = new Label("日期");
        root.add(dateLabel, 0, 2);
        DatePicker datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now());
        root.add(datePicker, 1, 2);

        dialogPane.setContent(root);

        ButtonType applyButton = new ButtonType("確認", ButtonBar.ButtonData.APPLY);
        ButtonType closeButton = new ButtonType("取消", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialogPane.getButtonTypes().addAll(closeButton, applyButton);

        dialogPane.lookupButton(applyButton).addEventHandler(ActionEvent.ACTION, e -> {
            try {
                Task task = new Task(randomString(10), nameTextField.getText(), descTextField.getText(), datePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")), false);
                DatabaseManager.getInstance().addTask(task);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        Alert dialog = new Alert(Alert.AlertType.NONE);
        dialog.setDialogPane(dialogPane);

        dialog.show();
    }

    public void editTask(Task oldTask) {
        DialogPane dialogPane = new DialogPane();
        dialogPane.setPrefWidth(250);

        dialogPane.setHeaderText("編輯任務");

        GridPane root = new GridPane();
        addColumnConstrains(root, 50);
        addColumnConstrains(root, 50);
        root.setVgap(3);
        root.setHgap(2);
        root.setPadding(new Insets(5));
        root.prefWidthProperty().bind(dialogPane.widthProperty());

        Label nameLabel = new Label("任務名稱");
        root.add(nameLabel, 0, 0);
        TextField nameTextField = new TextField(oldTask.getName());
        root.add(nameTextField, 1, 0);

        Label descLabel = new Label("描述");
        root.add(descLabel, 0, 1);
        TextField descTextField = new TextField(oldTask.getDescription());
        root.add(descTextField, 1, 1);

        Label dateLabel = new Label("日期");
        root.add(dateLabel, 0, 2);
        DatePicker datePicker = new DatePicker();
        datePicker.setValue(LocalDate.parse(oldTask.getDate(), DateTimeFormatter.ofPattern("yyyy/MM/dd")));
        root.add(datePicker, 1, 2);

        dialogPane.setContent(root);

        ButtonType applyButton = new ButtonType("確認", ButtonBar.ButtonData.APPLY);
        ButtonType closeButton = new ButtonType("取消", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialogPane.getButtonTypes().addAll(closeButton, applyButton);

        dialogPane.lookupButton(applyButton).addEventHandler(ActionEvent.ACTION, e -> {
            try {
                Task newTask = new Task(oldTask.getId(), nameTextField.getText(), descTextField.getText(), datePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")), oldTask.isDone());
                DatabaseManager.getInstance().editTask(newTask);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        Alert dialog = new Alert(Alert.AlertType.NONE);
        dialog.setDialogPane(dialogPane);

        dialog.show();
    }

    private String randomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder stringBuilder = new StringBuilder(length);
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            char randomChar = characters.charAt(index);
            stringBuilder.append(randomChar);
        }

        return stringBuilder.toString();
    }

    private void addColumnConstrains(GridPane root, double width) {
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(width);
        columnConstraints.setHgrow(Priority.SOMETIMES);
        root.getColumnConstraints().add(columnConstraints);
    }

    // singleton
    private DialogManager() {};

    public static DialogManager instance;

    public static DialogManager getInstance() {
        if (instance == null) {
            instance = new DialogManager();
        }
        return instance;
    }
}
