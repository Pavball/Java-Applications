package hr.servis.iznimke;

public class PrazanTextException extends Exception{

    public PrazanTextException() {
    }

    public PrazanTextException(String message) {
        super(message);
    }

    public PrazanTextException(String message, Throwable cause) {
        super(message, cause);
    }

    public PrazanTextException(Throwable cause) {
        super(cause);
    }

    public PrazanTextException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
