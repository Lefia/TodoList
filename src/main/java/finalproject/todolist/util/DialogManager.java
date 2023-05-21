package finalproject.todolist.util;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

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
        TextField nameTextField = new TextField("New Task");
        root.add(nameTextField, 1, 0);

        Label descLabel = new Label("描述");
        root.add(descLabel, 0, 1);
        TextField descTextField = new TextField("Description");
        root.add(descTextField, 1, 1);

        Label dateLabel = new Label("日期");
        root.add(dateLabel, 0, 2);
        DatePicker datePicker = new DatePicker();
        root.add(datePicker, 1, 2);

        dialogPane.setContent(root);

        ButtonType applyButton = new ButtonType("確認", ButtonBar.ButtonData.APPLY);
        ButtonType closeButton = new ButtonType("取消", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialogPane.getButtonTypes().addAll(applyButton, closeButton);

        dialogPane.lookupButton(applyButton).addEventHandler(ActionEvent.ACTION, e -> {
            try {
                DatabaseManager.getInstance().addTask(nameTextField.getText(), descTextField.getText(), datePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        Alert dialog = new Alert(Alert.AlertType.NONE);
        dialog.setDialogPane(dialogPane);

        dialog.show();
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
