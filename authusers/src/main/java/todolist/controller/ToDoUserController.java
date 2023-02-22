package todolist.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import todolist.model.LoginRequest;
import todolist.service.PasswordMismatchException;
import todolist.service.ToDoUserService;
import todolist.service.UnknownUserException;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/user")
@Tag(name = "ToDoUser", description = "Operations to manager users")
public class ToDoUserController {
    @Autowired
    ToDoUserService toDoUserService;
    private Logger logger = LoggerFactory.getLogger(ToDoUserController.class);

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody LoginRequest request) {
        boolean result = false;
        try {
            result = toDoUserService.register(request.getUsername(), request.getPassword());
        } catch (NoSuchAlgorithmException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User registration failed");
        }
        return result ?
                ResponseEntity.ok("User registered successfully") :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User registration failed");
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        try {
            String jwt = toDoUserService.login(request.getUsername(), request.getPassword());
            return ResponseEntity.ok().body(jwt);
        } catch (PasswordMismatchException | UnknownUserException | NoSuchAlgorithmException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<ResponseDetails> validate(@RequestBody String token) {
        logger.info("Validating token <"+token+">");
        if (toDoUserService.validateToken(token)) {
            logger.info("Token validation successful <"+token+">");
            ResponseDetails responseDetails = new ResponseDetails(
                    "Ok",
                    "Token validation was successful",
                    "The provided token is valid");
            return ResponseEntity.status(HttpStatus.OK).body(responseDetails);
        } else {
            logger.info("Token validation failed <"+token+">");
            ResponseDetails responseDetails = new ResponseDetails(
                    "Error",
                    "Token validation failed",
                    "The provided token is not valid");
            return ResponseEntity.status(HttpStatus.OK).body(responseDetails);
        }
    }

}
