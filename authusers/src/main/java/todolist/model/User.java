package todolist.model;

import jakarta.persistence.*;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

@Entity
@EnableAutoConfiguration
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Column(name = "salted_password")
    String saltedPwd;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.saltedPwd = password;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getSaltedPwd() {
        return saltedPwd;
    }

}

