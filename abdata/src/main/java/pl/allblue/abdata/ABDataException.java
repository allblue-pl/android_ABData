package pl.allblue.abdata;

public class ABDataException extends Exception {

    public ABDataException(String message) {
        super(message);
    }

    public ABDataException(String message, Throwable cause) {
        super(message, cause);
    }

}
