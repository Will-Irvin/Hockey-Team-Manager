import java.io.IOException;

/**
 * FormatException
 *
 * This exception class is used to report file format issues when reading from a text data file. If there is an issue
 * the exception will take in the appropriate error message and the line number where the issue occurred.
 */
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
