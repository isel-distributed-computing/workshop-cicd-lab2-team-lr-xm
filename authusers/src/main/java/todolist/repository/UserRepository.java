package todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import todolist.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    User getReferenceByUsername(String username);
}
