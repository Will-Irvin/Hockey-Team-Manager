/**
 * SpecialTeamsLine
 *
 * This class serves the basis for a special teams line (power play or penalty kill). These lines are evaluated based
 * on their success percentage (scored on a power play or killed a penalty) and this class contains variables and
 * methods to monitor this percentage.
 */
public abstract class SpecialTeamsLine extends Line {
    private int successes;
    private int attempts;

    // Error Strings
    private final static String percentRangeError = "Percentage must be in the range 0-100";
    private final static String attemptsError = "Number of attempts cannot be negative";

    // Sets name as given by argument and sets stats to 0
    public SpecialTeamsLine(String name) {
        super(name);
        successes = 0;
        attempts = 0;
    }

    /**
     * Assigns given parameters to their respective variables. Assigns successes by taking the given percentage and
     * multiplying it by the number of attempts.
     * @throws IllegalArgumentException If the percentage is not in a valid range, or if attempts is negative
     */
    public SpecialTeamsLine(String name, double successPercent, int attempts) throws IllegalArgumentException {
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

    // Returns percentage of successful uses of the line by dividing successes by attempts
    public double getSuccessPercent() {
        if (attempts == 0) {
            return 0;
        }
        return (double) successes / attempts;
    }

    /**
     * Sets attempts with the given argument, and sets successes by multiplying the given percentage by attempts
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

    // Score Abstract Methods

    public abstract void score(Position position);

    public abstract void score(Position scorer, Position assist);

    public abstract void score(Position scorer, Position assist1, Position assist2);
}
