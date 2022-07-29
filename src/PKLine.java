/**
 * PKLine
 * A class that extends from the SpecialTeamLine class to be specific to a penalty kill line.
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
            throw new NullPointerException(EMPTY_POSITIONS);
        }
        if (offense1.equals(offense2) || offense1.equals(leftDe) || offense1.equals(rightDe) || offense2.equals(leftDe)
                || offense2.equals(rightDe) || leftDe.equals(rightDe)) {
            throw new IllegalArgumentException(PLAYER_DUPLICATES_ERROR);
        }

        this.offense1 = offense1;
        this.offense2 = offense2;
        this.leftDe = leftDe;
        this.rightDe = rightDe;
    }

    /**
     * Assigns given arguments to their respective variables, sets successes using a percentage
     * @throws NullPointerException If any of the given players are null
     * @throws IllegalArgumentException If the percentage is not in a valid range or if numberAttempts is negative
     */
    public PKLine(String name, Skater offense1, Skater offense2, Defenseman leftDe, Defenseman rightDe, double pkPercent,
                  int numberAttempts) throws NullPointerException, IllegalArgumentException {
        super(name, pkPercent, numberAttempts);
        if (offense1 == null || offense2 == null || leftDe == null || rightDe == null) {
            throw new NullPointerException(EMPTY_POSITIONS);
        }
        if (offense1.equals(offense2) || offense1.equals(leftDe) || offense1.equals(rightDe) || offense2.equals(leftDe)
                || offense2.equals(rightDe) || leftDe.equals(rightDe)) {
            throw new IllegalArgumentException(PLAYER_DUPLICATES_ERROR);
        }

        this.offense1 = offense1;
        this.offense2 = offense2;
        this.leftDe = leftDe;
        this.rightDe = rightDe;
    }

    /**
     * Creates a copy of the given line with the same parameters and name as the given argument.
     * @param line The given line to copy
     */
    public PKLine(PKLine line) {
        super(line.getName(), line.getSuccessPercent(), line.getAttempts());
        this.offense1 = line.offense1;
        this.offense2 = line.offense2;
        this.leftDe = line.leftDe;
        this.rightDe = line.rightDe;
    }

    // Setter Methods

    /**
     * @throws NullPointerException If a null argument is given
     * @throws IllegalArgumentException If a player at another position is the same as the given player
     */
    public void setOffense1(Skater offense1) throws NullPointerException, IllegalArgumentException {
        if (offense1 == null) {
            throw new NullPointerException(EMPTY_POSITIONS);
        }
        if (offense1.equals(offense2) || offense1.equals(leftDe) || offense1.equals(rightDe)) {
            throw new IllegalArgumentException(PLAYER_DUPLICATES_ERROR);
        }
        this.offense1 = offense1;
    }

    /**
     * @throws NullPointerException If a null argument is given
     * @throws IllegalArgumentException If a player at another position is the same as the given player
     */
    public void setOffense2(Skater offense2) throws NullPointerException, IllegalArgumentException {
        if (offense2 == null) {
            throw new NullPointerException(EMPTY_POSITIONS);
        }
        if (offense2.equals(offense1) || offense2.equals(leftDe) || offense2.equals(rightDe)) {
            throw new IllegalArgumentException(PLAYER_DUPLICATES_ERROR);
        }
        this.offense2 = offense2;
    }

    /**
     * @throws NullPointerException If a null argument is given
     * @throws IllegalArgumentException If a player at another position is the same as the given player
     */
    public void setLeftDe(Defenseman leftDe) throws NullPointerException, IllegalArgumentException {
        if (leftDe == null) {
            throw new NullPointerException(EMPTY_POSITIONS);
        }
        if (leftDe.equals(offense2) || leftDe.equals(offense1) || leftDe.equals(rightDe)) {
            throw new IllegalArgumentException(PLAYER_DUPLICATES_ERROR);
        }
        this.leftDe = leftDe;
    }

    /**
     * @throws NullPointerException If a null argument is given
     * @throws IllegalArgumentException If a player at another position is the same as the given player
     */
    public void setRightDe(Defenseman rightDe) {
        if (rightDe == null) {
            throw new NullPointerException(EMPTY_POSITIONS);
        }
        if (rightDe.equals(offense2) || rightDe.equals(offense1) || rightDe.equals(leftDe)) {
            throw new IllegalArgumentException(PLAYER_DUPLICATES_ERROR);
        }
        this.rightDe = rightDe;
    }

    /**
     * Properly updates stats of every player on the line after a goal is scored.
     * @param position The position of the player who scored on the line
     * @throws NullPointerException If given position is null
     * @throws IllegalArgumentException If given position is the Center
     */
    @Override
    public void score(Position position) throws NullPointerException, IllegalArgumentException {
        if (position == null) {
            throw new NullPointerException(EMPTY_POSITIONS);
        }
        if (position == Position.Center) {
            throw new IllegalArgumentException(centerError);
        }

        if (position == Position.Left_Wing) {
            offense1.score();
        } else {
            offense1.scoredOnIce();
        }
        if (position == Position.Right_Wing) {
            offense2.score();
        } else {
            offense2.scoredOnIce();
        }
        scoreDefense(position, rightDe, leftDe);
    }

    /**
     * Properly updates stats of every player on the line after a goal is scored.
     * @param scorer The position of the player who scored.
     * @param assist The position of the player who assisted the scorer
     * @throws NullPointerException If either of the given positions are null
     * @throws IllegalArgumentException If the positions given are the same or if either of the given positions are
     *                                  center
     */
    @Override
    public void score(Position scorer, Position assist) throws NullPointerException, IllegalArgumentException {
        if (scorer == null || assist == null) {
            throw new NullPointerException(EMPTY_POSITIONS);
        }
        if (scorer == Position.Center || assist == Position.Center) {
            throw new IllegalArgumentException(centerError);
        }
        if (scorer == assist) {
            throw new IllegalArgumentException(Line.POSITION_DUPLICATES_ERROR);
        }

        scoreWingers(scorer, assist, offense1, offense2);
        scoreDefense(scorer, assist, rightDe, leftDe);
    }

    /**
     * Properly updates stats of every player on the line after a goal is scored.
     * @param scorer The position of the player who scored.
     * @param assist1 The position of the player who assisted the scorer
     * @param assist2 The position of the second assist
     * @throws NullPointerException If any of the given positions are null
     * @throws IllegalArgumentException If the positions given are the same or if any of the given positions are
     *                                  center
     */
    @Override
    public void score(Position scorer, Position assist1, Position assist2) throws NullPointerException,
            IllegalArgumentException {
        if (scorer == null || assist1 == null || assist2 == null) {
            throw new NullPointerException(EMPTY_POSITIONS);
        }
        if (scorer == Position.Center || assist1 == Position.Center || assist2 == Position.Center) {
            throw new IllegalArgumentException(centerError);
        }
        if (scorer == assist1 || scorer == assist2 || assist1 == assist2) {
            throw new IllegalArgumentException(Line.POSITION_DUPLICATES_ERROR);
        }

        scoreWingers(scorer, assist1, assist2, offense1, offense2);
        scoreDefense(scorer, assist1, assist2, rightDe, leftDe);
    }

    // Updates stats for when the line is scored on
    @Override
    public void lineScoredOn() {
        offense1.scoredAgainst();
        offense2.scoredAgainst();
        leftDe.scoredAgainst();
        rightDe.scoredAgainst();
        failure();
    }

    /**
     * @return A formatted String containing each player in the line and the success percentage of the line
     */
    @Override
    public String lineRoster() {
        return String.format("""
                        %s
                        Offense 1 (LW): %s
                        Offense 2 (RW): %s
                        Left Defense: %s
                        Right Defense: %s
                        PK%%: %.2f""", getName(), offense1.getName(), offense2.getName(), leftDe.getName(),
                rightDe.getName(), getSuccessPercent());
    }

    /**
     * @return An array of the skaters on this line
     */
    @Override
    public Skater[] getSkaters() {
        return new Skater[]{offense1, offense2, leftDe, rightDe};
    }

    /**
     * @param s Checks the line to see if one of the players is this skater
     * @return Whether the line contains this skater or not
     */
    @Override
    public boolean contains(Skater s) {
        return s.equals(offense1) || s.equals(offense2) || s.equals(leftDe) || s.equals(rightDe);
    }
}
