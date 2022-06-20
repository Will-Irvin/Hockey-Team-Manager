/**
 * PKLine
 * A class that extends from the line class to be specific to the penalty kill special teams lines.
 */

public class PKLine extends SpecialTeamsLine {
    private Skater offense1;
    private Skater offense2;
    private Defenseman leftDe;
    private Defenseman rightDe;

    // Error String
    private static final String centerError = "Cannot choose center for a penalty kill line. Choose a wing instead.";

    /**
     * Assigns given arguments to their respective instance variables, assigns numberKilled/numberAttempts to 0
     * @throws NullPointerException Thrown if any of the given players are null
     * @throws IllegalArgumentException If the same player is given as two different arguments
     */
    public PKLine(String name, Skater offense1, Skater offense2, Defenseman leftDe, Defenseman rightDe)
            throws NullPointerException {
        super(name);
        if (offense1 == null || offense2 == null || leftDe == null || rightDe == null) {
            throw new NullPointerException(Line.nullError);
        }
        if (offense1.equals(offense2) || offense1.equals(leftDe) || offense1.equals(rightDe) || offense2.equals(leftDe)
                || offense2.equals(rightDe) || leftDe.equals(rightDe)) {
            throw new IllegalArgumentException(Line.duplicatesError);
        }

        this.offense1 = offense1;
        this.offense2 = offense2;
        this.leftDe = leftDe;
        this.rightDe = rightDe;
    }

    /**
     * Works almost identically to previous constructor, but uses a percentage to initialize the numberKilled instead of
     * the specific number
     * @throws NullPointerException If any of the given players are null
     * @throws IllegalArgumentException If the percentage is not in a valid range or if numberAttempts is negative
     */
    public PKLine(String name, Skater offense1, Skater offense2, Defenseman leftDe, Defenseman rightDe, double pkPercent,
                  int numberAttempts) throws NullPointerException, IllegalArgumentException {
        super(name, pkPercent, numberAttempts);
        if (offense1 == null || offense2 == null || leftDe == null || rightDe == null) {
            throw new NullPointerException(Line.nullError);
        }
        if (offense1.equals(offense2) || offense1.equals(leftDe) || offense1.equals(rightDe) || offense2.equals(leftDe)
                || offense2.equals(rightDe) || leftDe.equals(rightDe)) {
            throw new IllegalArgumentException(Line.duplicatesError);
        }

        this.offense1 = offense1;
        this.offense2 = offense2;
        this.leftDe = leftDe;
        this.rightDe = rightDe;
    }

    // Setter Methods

    /**
     * @throws NullPointerException If a null argument is given
     * @throws IllegalArgumentException If a player at another position is the same as the given player
     */
    public void setOffense1(Skater offense1) throws NullPointerException, IllegalArgumentException {
        if (offense1 == null) {
            throw new NullPointerException(Line.nullError);
        }
        if (offense1.equals(offense2) || offense1.equals(leftDe) || offense1.equals(rightDe)) {
            throw new IllegalArgumentException(Line.duplicatesError);
        }
        this.offense1 = offense1;
    }

    /**
     * @throws NullPointerException If a null argument is given
     * @throws IllegalArgumentException If a player at another position is the same as the given player
     */
    public void setOffense2(Skater offense2) throws NullPointerException, IllegalArgumentException {
        if (offense2 == null) {
            throw new NullPointerException(Line.nullError);
        }
        if (offense2.equals(offense1) || offense2.equals(leftDe) || offense2.equals(rightDe)) {
            throw new IllegalArgumentException(Line.duplicatesError);
        }
        this.offense2 = offense2;
    }

    /**
     * @throws NullPointerException If a null argument is given
     * @throws IllegalArgumentException If a player at another position is the same as the given player
     */
    public void setLeftDe(Defenseman leftDe) throws NullPointerException, IllegalArgumentException {
        if (leftDe == null) {
            throw new NullPointerException(Line.nullError);
        }
        if (leftDe.equals(offense2) || leftDe.equals(offense1) || leftDe.equals(rightDe)) {
            throw new IllegalArgumentException(Line.duplicatesError);
        }
        this.leftDe = leftDe;
    }

    /**
     * @throws NullPointerException If a null argument is given
     * @throws IllegalArgumentException If a player at another position is the same as the given player
     */
    public void setRightDe(Defenseman rightDe) {
        if (rightDe == null) {
            throw new NullPointerException(Line.nullError);
        }
        if (rightDe.equals(offense2) || rightDe.equals(offense1) || rightDe.equals(leftDe)) {
            throw new IllegalArgumentException(Line.duplicatesError);
        }
        this.rightDe = rightDe;
    }

    /**
     * @return A formatted String containing each player in the line and the success percentage of the line
     */
    @Override
    public String lineRoster() {
        return String.format("""
                        %s
                        Offense: %s
                        Offense: %s
                        Left Defense: %s
                        Right Defense: %s
                        PK%%: %.2f
                        """, getName(), offense1.getName(), offense2.getName(),
                leftDe.getName(), rightDe.getName(), getSuccessPercent());
    }

    @Override
    public void score(Position position, DefenseLine deLine) {
        if (position == Position.CENTER) {
            throw new IllegalArgumentException(centerError);
        }
        if (position == Position.LEFT_WING) {
            offense1.score();
        } else {
            offense1.scoredOnIce();
        }
        if (position == Position.RIGHT_WING) {
            offense2.score();
        } else {
            offense2.scoredOnIce();
        }
        if (position == Position.LEFT_DEFENSE) {
            leftDe.score();
        } else {
            leftDe.scoredOnIce();
        }
        if (position == Position.RIGHT_DEFENSE) {
            rightDe.score();
        } else {
            rightDe.scoredOnIce();
        }
    }

    public void lineScoredOn() {
        offense1.scoredAgainst();
        offense2.scoredAgainst();
        leftDe.scoredAgainst();
        rightDe.scoredAgainst();
        failure();
    }
}
