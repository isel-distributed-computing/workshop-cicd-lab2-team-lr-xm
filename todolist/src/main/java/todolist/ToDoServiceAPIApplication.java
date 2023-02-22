package todolist;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "UserService API",
                version = "1.0",
                description = "UserService API"
        )
)
@ComponentScan(basePackages = "todolist.*")
public class ToDoServiceAPIApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToDoServiceAPIApplication.class, args);
    }

}
