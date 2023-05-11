module finalproject.todolist {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;

    opens finalproject.todolist to javafx.fxml;
    exports finalproject.todolist;
    exports finalproject.todolist.controller;
    opens finalproject.todolist.controller to javafx.fxml;
}