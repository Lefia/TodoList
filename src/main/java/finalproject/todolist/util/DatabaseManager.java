package finalproject.todolist.util;

import finalproject.todolist.component.Task;
import java.sql.*;
import java.util.ArrayList;

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

    private void connect() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + filePath);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void addTask(String taskName, String taskDescription, String date) throws SQLException {
        connect();
        Statement statement = connection.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS Inbox (Name string, Description string, Date string)");
        statement.execute("INSERT INTO Inbox VALUES ('%s', '%s', '%s')".formatted(taskName, taskDescription, date));
        disconnect();
    }

    public ArrayList<Task> query() throws SQLException {
        connect();
        ArrayList<Task> taskList = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM Inbox");
        while (rs.next()) {
            taskList.add(new Task(rs.getString("Name"),
                                  rs.getString("Description"),
                                  rs.getString("Date")));
        }
        return  taskList;

    }

    // Singleton
    private DatabaseManager() {}

    public static DatabaseManager instance = new DatabaseManager();

    public static DatabaseManager getInstance() {
        return instance;
    }
}
