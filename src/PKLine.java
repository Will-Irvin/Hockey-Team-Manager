import java.io.Serializable;

/**
 * PKLine
 * A class that extends from the line class to be specific to the penalty kill special teams lines.
 */

public class PKLine extends Line implements Serializable {
    private int numberKilled;
    private int numberAttempts;    // Used to calculate the PK percentage

    /**
     * Assigns given arguments to their respective instance variables, initializes Center to null and stats to 0
     * @throws NullPointerException Thrown if any of the given players are null
     */
    public PKLine(String name, Skater winger1, Skater winger2, Defenseman leftDe, Defenseman rightDe)
            throws NullPointerException {
        super(name, null, winger1, winger2, leftDe, rightDe);
        if (winger1 == null || winger2 == null || leftDe == null || rightDe == null) {
            throw new NullPointerException("Penalty Kill positions cannot be empty");
        }
        numberKilled = 0;
        numberAttempts = 0;
    }

    /**
     * Assigns given arguments to their respective instance variables, initializes Center to null
     * @throws NullPointerException Thrown if any of the given players are null
     * @throws IllegalArgumentException Thrown if any of the given stats are negative
     */
    public PKLine(String name, Skater winger1, Skater winger2, Defenseman leftDe, Defenseman rightDe,
                  int numberKilled, int numberAttempts) throws NullPointerException, IllegalArgumentException {
        super(name, null, winger1, winger2, leftDe, rightDe);
        if (winger1 == null || winger2 == null || leftDe == null || rightDe == null) {
            throw new NullPointerException("Penalty Kill positions cannot be empty");
        }
        if (numberKilled < 0 || numberAttempts < 0) {
            throw new IllegalArgumentException("Given totals cannot be negative");
        }
        if (numberKilled > numberAttempts) {
            throw new IllegalArgumentException("Number of successfully killed penalties cannot be greater than " +
                    "number of penalties the line has faced");
        }
        this.numberKilled = numberKilled;
        this.numberAttempts = numberAttempts;
    }

    /**
     * Works almost identically to previous constructor, but uses a percentage to initialize the numberKilled instead of
     * the specific number
     * @throws NullPointerException If any of the given players are null
     * @throws IllegalArgumentException If the percentage is not in a valid range or if numberAttempts is negative
     */
    public PKLine(String name, Skater winger1, Skater winger2, Defenseman leftDe, Defenseman rightDe, double pkPercent,
                  int numberAttempts) throws NullPointerException, IllegalArgumentException {
        super(name, null, winger1, winger2, leftDe, rightDe);
        if (winger1 == null || winger2 == null || leftDe == null || rightDe == null) {
            throw new NullPointerException("Penalty Kill positions cannot be empty");
        }
        if (pkPercent < 0 || pkPercent > 100) {
            throw new IllegalArgumentException("Percentage must be between 0-100");
        }
        if (numberAttempts < 0) {
            throw new IllegalArgumentException("Number of attempts cannot be negative");
        }

        numberKilled = (int) ((pkPercent / 100) * numberAttempts);
        this.numberAttempts = numberAttempts;
    }

    // Getter methods

    public int getNumberKilled() {
        return numberKilled;
    }

    public int getNumberAttempts() {
        return numberAttempts;
    }

    /**
     * Calculates percentage of penalties that have successfully been killed based off numberKilled and numberAttempts
     * @return The calculated percentage
     */
    public double getKillPercent() {
        if (numberAttempts == 0) {
            return 0;
        }
        return (double) numberKilled / numberAttempts * 100;
    }

    /**
     * @throws IllegalArgumentException Thrown if numberKilled is larger than numberAttempts or if either value is
     * negative
     */
    public void setKillStats(int numberKilled, int numberAttempts) throws IllegalArgumentException {
        if (numberKilled > numberAttempts) {
            throw new IllegalArgumentException("Number of successfully killed penalties cannot be greater than " +
                    "number of penalties the line has faced");
        }
        if (numberAttempts < 0 || numberKilled < 0) {
            throw new IllegalArgumentException("Given totals cannot be negative");
        }
        this.numberKilled = numberKilled;
        this.numberAttempts = numberAttempts;
    }

    /**
     * Uses a percentage to calculate and assign numberKilled
     * @throws IllegalArgumentException Thrown if successPercent is not a valid percent or if numberAttempts is negative
     */
    public void setKillStatsWithPercentage(double successPercent, int numberAttempts) throws IllegalArgumentException {
        if (successPercent < 0 || successPercent > 100) {
            throw new IllegalArgumentException("Percentage must be in the range 0-100");
        }
        if (numberAttempts < 0) {
            throw new IllegalArgumentException("Number of attempts cannot be negative");
        }
        numberKilled = (int) ((successPercent / 100) * numberAttempts);
        this.numberAttempts = numberAttempts;
    }

    // Increments numberKilled and numberAttempts up by 1
    public void killedPenalty() {
        numberKilled++;
        numberAttempts++;
    }

    // Increments only numberAttempts up by 1
    public void scoredOn() {
        numberAttempts++;
    }

    /**
     * @return A formatted String containing each player in the line and the success percentage of the line
     */
    public String lineRoster() {
        return String.format("%s\n" +
                "Offense: %s\n" +
                "Offense: %s\n" +
                "Left Defense: %s\n" +
                "Right Defense: %s\n" +
                "PK%%: %.2f\n", getName(), getLeftWing().getName(), getRightWing().getName(),
                getLeftDe().getName(), getRightDe().getName(), getKillPercent());
    }
}
