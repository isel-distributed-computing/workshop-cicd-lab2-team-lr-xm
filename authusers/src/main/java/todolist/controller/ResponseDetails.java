package todolist.controller;

public class ResponseDetails {
    private String code;
    private String message;
    private String details;

    public ResponseDetails() {
    }

    public ResponseDetails(String error, String message, String details) {
        this.code = error;
        this.message = message;
        this.details = details;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "ErrorDetails{" +
                "message='" + message + '\'' +
                ", details='" + details + '\'' +
                '}';
    }
}

