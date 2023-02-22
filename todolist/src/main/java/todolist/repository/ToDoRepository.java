package todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import todolist.model.ToDo;

import java.util.List;

public interface ToDoRepository extends JpaRepository<ToDo, Long> {
    List<ToDo> findAllByUsername(String user);
}
