package todolist.service;

public class UnknownUserException extends Exception {
    public UnknownUserException(String userNotFound) {
        super(userNotFound);
    }

    public UnknownUserException() {
    }
}
