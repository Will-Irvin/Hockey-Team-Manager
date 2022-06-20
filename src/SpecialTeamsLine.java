
public abstract class SpecialTeamsLine extends Line {
    private int successes;
    private int attempts;

    // Error Strings
    private final static String percentRangeError = "Percentage must be in the range 0-100";
    private final static String attemptsError = "Number of attempts cannot be negative";

    public SpecialTeamsLine(String name) {
        super(name);
        successes = 0;
        attempts = 0;
    }

    public SpecialTeamsLine(String name, double successPercent, int attempts) {
        super(name);
        if (successPercent < 0 || successPercent > 100) {
            throw new IllegalArgumentException(percentRangeError);
        }
        if (attempts < 0) {
            throw new IllegalArgumentException(attemptsError);
        }
        successes = (int) ((successPercent / 100) * attempts);
        this.attempts = attempts;
    }

    // Getter Methods

    public int getSuccesses() {
        return successes;
    }

    public int getAttempts() {
        return attempts;
    }

    public double getSuccessPercent() {
        if (attempts == 0) {
            return 0;
        }
        return (double) successes / attempts;
    }

    // Setter Method

    /**
     * @throws IllegalArgumentException If successPercent is not within valid range, or attempts is negative
     */
    public void setSuccessStats(double successPercent, int attempts) throws IllegalArgumentException {
        if (successPercent < 0 || successPercent > 100) {
            throw new IllegalArgumentException(percentRangeError);
        }
        if (attempts < 0) {
            throw new IllegalArgumentException(attemptsError);
        }
        this.successes = (int) ((successPercent / 100) * attempts);
        this.attempts = attempts;
    }

    // Increment both successes and attempts up by 1
    public void success() {
        successes++;
        attempts++;
    }

    // Increments only attempts up by 1
    public void failure() {
        attempts++;
    }

    // Score Methods

    public abstract void score(Position position);

    public abstract void score(Position scorer, Position assist);

    public abstract void score(Position scorer, Position assist1, Position assist2);
}
