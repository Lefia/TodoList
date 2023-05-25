package finalproject.todolist.util;

import finalproject.todolist.Globe;
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

    // 與資料庫建立連線
    private void connect() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + filePath);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    // 關閉與資料庫的連線
    private void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    // 用於在場景切換至 MainPage 時做初始化
    public void initialize() throws SQLException {
        DatabaseManager.getInstance().createCategory("收件箱");
        Globe.getInstance().put("currentCategory", "收件箱");
        Globe.getInstance().put("sortType", "預設");
        Globe.getInstance().put("showDone", false);
        ListManager.getInstance().showTaskList();
        ListManager.getInstance().showCategoryList();
    }

    /**** 任務 ****/

    // 新增任務到資料庫
    public void addTask(Task task) throws SQLException {
        connect();
        String insertQuery = "INSERT INTO %s (Id, Name, Description, Date, Done) VALUES (?, ?, ?, ?, ?)".formatted(task.getCategory());
        PreparedStatement statement = connection.prepareStatement(insertQuery);
        statement.setString(1, task.getId());
        statement.setString(2, task.getName());
        statement.setString(3, task.getDescription());
        statement.setString(4, task.getDate());
        statement.setBoolean(5, task.isDone());

        statement.executeUpdate();
        disconnect();

        ListManager.getInstance().showTaskList();
    }

    // 編輯資料庫中的任務
    public void editTask(Task oldTask, Task newTask) throws SQLException {
        if (!newTask.getCategory().equals(oldTask.getCategory())) {
            deleteTask(oldTask);
            addTask(newTask);
        }
        else {
            connect();
            String updateQuery = "UPDATE %s SET Name = ?, Description = ?, Date = ?, Done = ? WHERE Id = ?".formatted(newTask.getCategory());
            PreparedStatement statement = connection.prepareStatement(updateQuery);
            statement.setString(1, newTask.getName());
            statement.setString(2, newTask.getDescription());
            statement.setString(3, newTask.getDate());
            statement.setBoolean(4, newTask.isDone());
            statement.setString(5, newTask.getId());
            statement.executeUpdate();
            disconnect();
        }
        ListManager.getInstance().showTaskList();
    }

    // 從資料庫中刪除任務
    public void deleteTask(Task task) throws SQLException {
        connect();
        String deleteQuery = "DELETE FROM %s WHERE Id = ?".formatted(task.getCategory());
        PreparedStatement statement = connection.prepareStatement(deleteQuery);
        statement.setString(1, task.getId());
        statement.executeUpdate();
        disconnect();
        ListManager.getInstance().showTaskList();
    }

    // 查詢同一類別中的所有任務
    public ArrayList<Task> queryTask(String category) throws SQLException {
        connect();
        ArrayList<Task> taskList = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM " + category);
        while (rs.next()) {
            taskList.add(new Task(rs.getString("Id"),
                                  rs.getString("Name"),
                                  rs.getString("Description"),
                                  rs.getString("Date"),
                                  rs.getBoolean("Done"),
                                  category
            ));
        }
        return  taskList;
    }

    /**** 類別 ****/

    // 建立新的類別
    public void createCategory(String category) throws SQLException {
        connect();
        String createQuery = "CREATE TABLE IF NOT EXISTS %s (Id STRING, Name STRING, Description STRING, Date STRING, Done BOOLEAN)".formatted(category);
        PreparedStatement statement = connection.prepareStatement(createQuery);
        statement.executeUpdate();
        disconnect();
    }

    // 確認類別是否存在
    public boolean checkCategoryExists(String category) throws SQLException{
        connect();
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet tables = metaData.getTables(null, null, category, new String[]{"TABLE"});
        disconnect();
        return tables.next();
    }

    // 回傳在資料庫中的所有類別
    public ArrayList<String> getAllCategories() throws SQLException {
        connect();
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet tables = metaData.getTables(null, null, null, new String[]{"TABLE"});

        ArrayList<String> tableList = new ArrayList<>();
        while (tables.next()) {
            String tableName = tables.getString("TABLE_NAME");
            tableList.add(tableName);
        }

        disconnect();
        return tableList;
    }

    // 刪除類別
    public void deleteCategory(String category) throws SQLException {
        connect();
        Statement statement = connection.createStatement();
        statement.executeUpdate("DROP TABLE  %s".formatted(category));
        disconnect();
        ListManager.getInstance().showCategoryList();
        Globe.getInstance().put("currentCategory", "收件箱");
        ListManager.getInstance().showTaskList();
    }

    // 編輯類別
    public void renameCategory(String oldName, String newName) throws SQLException {
        connect();
        Statement statement = connection.createStatement();
        statement.executeUpdate("ALTER TABLE %s RENAME TO %s".formatted(oldName, newName));
        disconnect();
        ListManager.getInstance().showCategoryList();
    }

    /**** 單例 ****/
    private DatabaseManager() {}

    public static DatabaseManager instance = new DatabaseManager();

    public static DatabaseManager getInstance() {
        return instance;
    }
}
