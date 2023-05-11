package finalproject.todolist.util;

import finalproject.todolist.Main;
import java.sql.Connection;
import java.sql.DriverManager;
// import java.sql.ResultSet;
import java.sql.SQLException;
// java.sql.Statement;

public class DatabaseController {
    private String filePath;
    private Connection connection;

    public void setLocation(String fullpath) {
        this.filePath = fullpath;
    }
    public void setLocation(String path, String name) {
        this.filePath = path + name + ".db";
    }
    public String getLocation() {
        return filePath;
    }
    public void connect() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + filePath);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    // Singleton
    private DatabaseController() {}
    public static DatabaseController instance = new DatabaseController();
    public static DatabaseController getInstance() {
        return instance;
    }
}
