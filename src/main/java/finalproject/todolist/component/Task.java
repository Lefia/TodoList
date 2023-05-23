package finalproject.todolist.component;

public class Task {
    private String id;

    private String name;

    private String description;

    private String date;

    private String category;

    private boolean done;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    public boolean isDone() {
        return done;
    }

    public Task(String id, String name, String description, String date, boolean done, String category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
        this.category = category;
        this.done = done;
    }


}
