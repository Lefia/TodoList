package finalproject.todolist.util;

import java.sql.*;

public class DatabaseManager {
    private String filePath;
    private Connection connection;

    public void setFilePath(String fullpath) {
        this.filePath = fullpath;
    }

    public void setFilePath(String folderPath, String name) {
        this.filePath = folderPath + '\\' + name + ".db";
    }

    // 測試用
    public String getFilePath() {
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
    private DatabaseManager() {}
    public static DatabaseManager instance = new DatabaseManager();
    public static DatabaseManager getInstance() {
        return instance;
    }
}
