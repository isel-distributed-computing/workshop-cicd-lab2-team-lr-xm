package todolist.notificationservice.model;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;


@Getter @Setter @ToString @NoArgsConstructor @AllArgsConstructor
@Entity
@EnableAutoConfiguration
@Table(name = "eventlog")
public class EventModel {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    String id;
    String username;
    String action;
}
