package todolist.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import todolist.model.User;
import todolist.repository.UserRepository;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class ToDoUserService {
    private UserRepository userRepository;
    private String secret;
    private Logger logger = LoggerFactory.getLogger(ToDoUserService.class);

    public ToDoUserService(@Value("${app.jwt_secret}") String secret, UserRepository userRepository) {
        this.secret = secret;
        this.userRepository = userRepository;
    }

    public boolean register(String username, String pwd) throws NoSuchAlgorithmException {
        // real DB
        if (userRepository.findByUsername(username).isPresent())
            return false;
        // -- securely save password
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String pwdSalted = encoder.encode(pwd);
        // -- save to DB
        userRepository.save(new User(username, pwdSalted));
        return true;
    }

    public String login(String username, String pwd) throws PasswordMismatchException, UnknownUserException, NoSuchAlgorithmException {
        // check pwd
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty())
            throw new UnknownUserException();
        // check pwd
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(pwd, user.get().getSaltedPwd()))
            throw new PasswordMismatchException();
        // create JWT
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withClaim("username", username)
                .sign(algorithm);
    }

    public boolean validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWT.require(algorithm)
                    .build()
                    .verify(token);
            return true;
        } catch (Exception e) {
            logger.error("Invalid token: " + token);
            logger.error("Exception was ", e);
            return false;
        }
    }
}
