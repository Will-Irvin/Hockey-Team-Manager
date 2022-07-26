import java.io.IOException;

public class FormatException extends IOException {
    private final int lineNumber;

    public FormatException(String message, int lineNumber) {
        super(message);
        this.lineNumber = lineNumber;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + String.format(" - line %d", lineNumber);
    }
}
