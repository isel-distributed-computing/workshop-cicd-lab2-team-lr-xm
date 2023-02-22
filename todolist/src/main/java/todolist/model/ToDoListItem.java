package todolist.model;

public class ToDoListItem {
    private String description;
    private String username;

    public ToDoListItem() {}

    public ToDoListItem(String username, String description) {
        this.username = username;
        this.description = description;
    }

    public String getUsername() {
        return username;
    }
    public String getDescription() {
        return description;
    }

    public String toString() {
        return "user: " + username + " / ToDo: " + description;
    }
}
