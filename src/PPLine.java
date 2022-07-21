/**
 * PPLine
 * A class that extends from the SpecialTeamLine class to be specific to a power play line.
 */
public class PPLine extends SpecialTeamsLine {
    private Center center;
    private Skater leftWing;
    private Skater rightWing;
    private Defenseman leftDe;
    private Defenseman rightDe;

    /**
     * Initializes given arguments in their respective variables, sets stats to 0
     * @throws NullPointerException Thrown if any of the given players are null
     * @throws IllegalArgumentException If any of the given players are the same
     */
    public PPLine(String name, Center center, Skater leftWing, Skater rightWing, Defenseman leftDe, Defenseman rightDe)
            throws NullPointerException, IllegalArgumentException {
        super(name);
        if (center == null || leftWing == null || rightWing == null || leftDe == null || rightDe == null) {
            throw new NullPointerException(EMPTY_POSITIONS);
        }
        if (center.equals(leftWing) || center.equals(rightWing) || leftWing.equals(rightWing) ||
                leftWing.equals(rightDe) || leftWing.equals(leftDe) || rightWing.equals(rightDe) ||
                rightWing.equals(leftDe) || leftDe.equals(rightDe)) {
            throw new IllegalArgumentException(PLAYER_DUPLICATES_ERROR);
        }
        this.center = center;
        this.leftWing = leftWing;
        this.rightWing = rightWing;
        this.leftDe = leftDe;
        this.rightDe = rightDe;
    }

    /**
     * Initializes given arguments to their respective variables, initializes successes using a percentage
     * @throws NullPointerException Thrown if given players are null
     * @throws IllegalArgumentException Thrown if given percentage is invalid, if numberOpps is negative, or if any of
     *                                  the players are the same
     */
    public PPLine(String name, Center center, Skater leftWing, Skater rightWing, Defenseman leftDe, Defenseman rightDe,
                  double ppPercent, int numberOpps) throws NullPointerException, IllegalArgumentException {
        super(name, ppPercent, numberOpps);
        if (center == null || leftWing == null || rightWing == null || leftDe == null || rightDe == null) {
            throw new NullPointerException(EMPTY_POSITIONS);
        }
        if (center.equals(leftWing) || center.equals(rightWing) || leftWing.equals(rightWing) ||
                leftWing.equals(rightDe) || leftWing.equals(leftDe) || rightWing.equals(rightDe) ||
                rightWing.equals(leftDe) || leftDe.equals(rightDe)) {
            throw new IllegalArgumentException(PLAYER_DUPLICATES_ERROR);
        }
        this.center = center;
        this.leftWing = leftWing;
        this.rightWing = rightWing;
        this.leftDe = leftDe;
        this.rightDe = rightDe;
    }

    /**
     * Creates a copy of the given line with the same parameters and name as the given argument.
     * @param line The given line to copy
     */
    public PPLine(PPLine line) {
        super(line.getName(), line.getSuccessPercent(), line.getAttempts());
        this.center = line.center;
        this.leftWing = line.leftWing;
        this.rightWing = line.rightWing;
        this.leftDe = line.leftDe;
        this.rightDe = line.rightDe;
    }

    // Setter Methods

    /**
     * @throws NullPointerException If given argument is null
     * @throws IllegalArgumentException If given argument is the same as another player
     */
    public void setCenter(Center center) throws NullPointerException, IllegalArgumentException {
        if (center == null) {
            throw new NullPointerException(EMPTY_POSITIONS);
        }
        if (center.equals(leftWing) || center.equals(rightWing)) {
            throw new IllegalArgumentException(PLAYER_DUPLICATES_ERROR);
        }
        this.center = center;
    }

    /**
     * @throws NullPointerException If given argument is null
     * @throws IllegalArgumentException If given argument is the same as another player
     */
    public void setLeftWing(Skater leftWing) throws NullPointerException, IllegalArgumentException {
        if (leftWing == null) {
            throw new NullPointerException(EMPTY_POSITIONS);
        }
        if (leftWing.equals(center) || leftWing.equals(rightWing) || leftWing.equals(leftDe) ||
                leftWing.equals(rightDe)) {
            throw new IllegalArgumentException(PLAYER_DUPLICATES_ERROR);
        }
        this.leftWing = leftWing;
    }

    /**
     * @throws NullPointerException If given argument is null
     * @throws IllegalArgumentException If given argument is the same as another player
     */
    public void setRightWing(Skater rightWing) throws NullPointerException, IllegalArgumentException {
        if (rightWing == null) {
            throw new NullPointerException(EMPTY_POSITIONS);
        }
        if (rightWing.equals(center) || rightWing.equals(leftWing) || rightWing.equals(rightDe) ||
                rightWing.equals(leftDe)) {
            throw new IllegalArgumentException(PLAYER_DUPLICATES_ERROR);
        }
        this.rightWing = rightWing;
    }

    /**
     * @throws NullPointerException If given argument is null
     * @throws IllegalArgumentException If given argument is the same as another player
     */
    public void setLeftDe(Defenseman leftDe) throws NullPointerException, IllegalArgumentException {
        if (leftDe == null) {
            throw new NullPointerException(EMPTY_POSITIONS);
        }
        if (leftDe.equals(leftWing) || leftDe.equals(rightWing) || leftDe.equals(rightDe)) {
            throw new IllegalArgumentException(PLAYER_DUPLICATES_ERROR);
        }
        this.leftDe = leftDe;
    }

    /**
     * @throws NullPointerException If given argument is null
     * @throws IllegalArgumentException If given argument is the same as another player
     */
    public void setRightDe(Defenseman rightDe) throws NullPointerException, IllegalArgumentException {
        if (rightDe == null) {
            throw new NullPointerException(EMPTY_POSITIONS);
        }
        if (rightDe.equals(leftWing) || rightDe.equals(rightWing) || rightDe.equals(leftDe)) {
            throw new IllegalArgumentException(PLAYER_DUPLICATES_ERROR);
        }
        this.rightDe = rightDe;
    }

    /**
     * Updates stats for every player on the line when a goal is scored
     * @param position Position of the player who scored the goal.
     * @throws NullPointerException If given position is null
     */
    @Override
    public void score(Position position) throws NullPointerException {
        if (position == null) {
            throw new NullPointerException(EMPTY_POSITIONS);
        }
        scoreOffense(position, center, leftWing, rightWing);
        scoreDefense(position, rightDe, leftDe);
        success();
    }

    /**
     * Updates stats for every player when a goal is scored
     * @param scorer Position of the player who scored the goal
     * @param assist The position of the player who assisted the goal
     * @throws NullPointerException If given arguments are null
     * @throws IllegalArgumentException If given positions are equal
     */
    @Override
    public void score(Position scorer, Position assist) throws NullPointerException, IllegalArgumentException {
        if (scorer == null || assist == null) {
            throw new NullPointerException(EMPTY_POSITIONS);
        }
        if (scorer == assist) {
            throw new IllegalArgumentException(Line.POSITION_DUPLICATES_ERROR);
        }

        scoreOffense(scorer, assist, center, leftWing, rightWing);
        scoreDefense(scorer, assist, rightDe, leftDe);
        success();
    }

    /**
     * Updates stats for every player when a goal is scored
     * @param scorer Position of the player who scored the goal
     * @param assist1 Position of player who assisted the goal
     * @param assist2 Position of other player who assisted the goal
     * @throws NullPointerException If given arguments are null
     * @throws IllegalArgumentException If given positions are equal
     */
    @Override
    public void score(Position scorer, Position assist1, Position assist2) throws NullPointerException,
            IllegalArgumentException {
        if (scorer == null || assist1 == null || assist2 == null) {
            throw new NullPointerException(EMPTY_POSITIONS);
        }
        if (scorer == assist1 || scorer == assist2 || assist1 == assist2) {
            throw new IllegalArgumentException(Line.POSITION_DUPLICATES_ERROR);
        }

        scoreOffense(scorer, assist1, assist2, center, leftWing, rightWing);
        scoreDefense(scorer, assist1, assist2, rightDe, leftDe);
        success();
    }

    // Used to have center win face off
    public void winFaceOff() {
        center.winFaceOff();
    }

    // Used to have center lose face off
    public void loseFaceOff() {
        center.loseFaceOff();
    }

    // Updates stats for every player when they are scored on
    public void lineScoredOn() {
        center.scoredAgainst();
        leftWing.scoredAgainst();
        rightWing.scoredAgainst();
        leftDe.scoredAgainst();
        rightDe.scoredAgainst();
    }

    /**
     * @return A formatted String containing the players on the line and the power play success percentage of the line
     */
    @Override
    public String lineRoster() {
        return String.format("""
                        %s
                        Center: %s
                        Left Wing: %s
                        Right Wing: %s
                        Left Defense: %s
                        Right Defense: %s
                        PP%%: %.2f""", getName(), center.getName(), leftWing.getName(), rightWing.getName(),
                leftDe.getName(), rightDe.getName(), getSuccessPercent());
    }

    /**
     * @return An array of the skaters on this line
     */
    @Override
    public Skater[] getSkaters() {
        return new Skater[]{center, leftWing, rightWing, leftDe, rightDe};
    }
}
