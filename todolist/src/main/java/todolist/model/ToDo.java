package todolist.model;

import jakarta.persistence.*;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

@Entity
@EnableAutoConfiguration
@Table(name = "to_dos")
public class ToDo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String description;

    // getters and setters
    public Long getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public String getDescription() {
        return description;
    }
    public ToDo(String user, String description) {
        this.username = user;
        this.description = description;
    }
    public ToDo() {}
}
