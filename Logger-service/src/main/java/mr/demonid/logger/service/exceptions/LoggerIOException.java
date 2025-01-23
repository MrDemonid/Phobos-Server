package mr.demonid.logger.service.exceptions;

public class LoggerIOException extends RuntimeException {
    private final String title;

    public LoggerIOException(String title, String message) {
        super(message);
        this.title = title;
    }

    @Override
    public String getMessage() {
        return title + ": " + super.getMessage();
    }
}

