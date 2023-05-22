package finalproject.todolist.util;

import finalproject.todolist.Globe;
import finalproject.todolist.component.Task;
import javafx.scene.layout.VBox;

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

    public void createTable(String name) throws SQLException {
        connect();
        String createQuery = "CREATE TABLE IF NOT EXISTS " + name + " (Id STRING, Name STRING, Description STRING, Date STRING, Done BOOLEAN)";
        PreparedStatement statement = connection.prepareStatement(createQuery);
        statement.executeUpdate();
        disconnect();
    }

    public void addTask(Task task) throws SQLException {
        connect();
        String insertQuery = "INSERT INTO Inbox (Id, Name, Description, Date, Done) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(insertQuery);

        statement.setString(1, task.getId());
        statement.setString(2, task.getName());
        statement.setString(3, task.getDescription());
        statement.setString(4, task.getDate());
        statement.setBoolean(5, task.isDone());

        statement.executeUpdate();
        disconnect();

        TaskManager.getInstance().refreshList((VBox) Globe.getInstance().get("List"));
    }

    public void editTask(Task task) throws SQLException {
        connect();
        String updateQuery = "UPDATE Inbox SET Name = ?, Description = ?, Date = ?, Done = ? WHERE Id = ?";

        PreparedStatement statement = connection.prepareStatement(updateQuery);
        statement.setString(1, task.getName());
        statement.setString(2, task.getDescription());
        statement.setString(3, task.getDate());
        statement.setBoolean(4, task.isDone());
        statement.setString(5, task.getId());

        int rowsAffected = statement.executeUpdate();

        if (rowsAffected == 0) {
            System.out.println("資料更新失敗");
        }

        disconnect();
        TaskManager.getInstance().refreshList((VBox) Globe.getInstance().get("List"));
    }

    public void deleteTask(Task task) throws SQLException {
        connect();
        Statement statement = connection.createStatement();
        statement.execute("DELETE FROM Inbox WHERE Id='%s'".formatted(task.getId()));
        disconnect();
        TaskManager.getInstance().refreshList((VBox) Globe.getInstance().get("List"));
    }

    public ArrayList<Task> query() throws SQLException {
        connect();
        ArrayList<Task> taskList = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM Inbox");
        while (rs.next()) {
            taskList.add(new Task(rs.getString("Id"),
                                  rs.getString("Name"),
                                  rs.getString("Description"),
                                  rs.getString("Date"),
                                  rs.getBoolean("Done")
            ));
        }
        return  taskList;
    }

    public boolean checkTableExists(String tableName) throws SQLException{
        connect();
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet tables = metaData.getTables(null, null, tableName, null);
        disconnect();
        return tables.next();
    }

    // Singleton
    private DatabaseManager() {}

    public static DatabaseManager instance = new DatabaseManager();

    public static DatabaseManager getInstance() {
        return instance;
    }
}
