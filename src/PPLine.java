
public class PPLine extends SpecialTeamsLine {
    private Center center;
    private Skater leftWing;
    private Skater rightWing;
    private Defenseman leftDe;
    private Defenseman rightDe;

    /**
     * Initializes given arguments in their respective variables, sets stats to 0
     * @throws NullPointerException Thrown if any of the given players are null
     */
    public PPLine(String name, Center center, Skater leftWing, Skater rightWing, Defenseman leftDe, Defenseman rightDe)
            throws NullPointerException, IllegalArgumentException {
        super(name);
        if (center == null || leftWing == null || rightWing == null || leftDe == null || rightDe == null) {
            throw new NullPointerException(nullError);
        }
        if (center.equals(leftWing) || center.equals(rightWing) || leftWing.equals(rightWing) ||
                leftWing.equals(rightDe) || leftWing.equals(leftDe) || rightWing.equals(rightDe) ||
                rightWing.equals(leftDe) || leftDe.equals(rightDe)) {
            throw new IllegalArgumentException(playerDuplicatesError);
        }
        this.center = center;
        this.leftWing = leftWing;
        this.rightWing = rightWing;
        this.leftDe = leftDe;
        this.rightDe = rightDe;
    }

    /**
     * Works almost identically to previous constructor, but initializes numberScored using the given percentage
     * @throws NullPointerException Thrown if given players are null
     * @throws IllegalArgumentException Thrown if given percentage is invalid or if numberOpps is negative
     */
    public PPLine(String name, Center center, Skater leftWing, Skater rightWing, Defenseman leftDe, Defenseman rightDe,
                  double ppPercent, int numberOpps) throws NullPointerException, IllegalArgumentException {
        super(name, ppPercent, numberOpps);
        if (center == null || leftWing == null || rightWing == null || leftDe == null || rightDe == null) {
            throw new NullPointerException(nullError);
        }
        if (center.equals(leftWing) || center.equals(rightWing) || leftWing.equals(rightWing) ||
                leftWing.equals(rightDe) || leftWing.equals(leftDe) || rightWing.equals(rightDe) ||
                rightWing.equals(leftDe) || leftDe.equals(rightDe)) {
            throw new IllegalArgumentException(playerDuplicatesError);
        }
        this.center = center;
        this.leftWing = leftWing;
        this.rightWing = rightWing;
        this.leftDe = leftDe;
        this.rightDe = rightDe;
    }

    // Setter Methods

    /**
     * @throws NullPointerException If given argument is null
     * @throws IllegalArgumentException If given argument is the same as another player
     */
    public void setCenter(Center center) throws NullPointerException, IllegalArgumentException {
        if (center == null) {
            throw new NullPointerException(nullError);
        }
        if (center.equals(leftWing) || center.equals(rightWing)) {
            throw new IllegalArgumentException(playerDuplicatesError);
        }
        this.center = center;
    }

    /**
     * @throws NullPointerException If given argument is null
     * @throws IllegalArgumentException If given argument is the same as another player
     */
    public void setLeftWing(Skater leftWing) throws NullPointerException, IllegalArgumentException {
        if (leftWing == null) {
            throw new NullPointerException(nullError);
        }
        if (leftWing.equals(center) || leftWing.equals(rightWing) || leftWing.equals(leftDe) ||
                leftWing.equals(rightDe)) {
            throw new IllegalArgumentException(playerDuplicatesError);
        }
        this.leftWing = leftWing;
    }

    /**
     * @throws NullPointerException If given argument is null
     * @throws IllegalArgumentException If given argument is the same as another player
     */
    public void setRightWing(Skater rightWing) throws NullPointerException, IllegalArgumentException {
        if (rightWing == null) {
            throw new NullPointerException(nullError);
        }
        if (rightWing.equals(center) || rightWing.equals(leftWing) || rightWing.equals(rightDe) ||
                rightWing.equals(leftDe)) {
            throw new IllegalArgumentException(playerDuplicatesError);
        }
        this.rightWing = rightWing;
    }

    /**
     * @throws NullPointerException If given argument is null
     * @throws IllegalArgumentException If given argument is the same as another player
     */
    public void setLeftDe(Defenseman leftDe) throws NullPointerException, IllegalArgumentException {
        if (leftDe == null) {
            throw new NullPointerException(nullError);
        }
        if (leftDe.equals(leftWing) || leftDe.equals(rightWing) || leftDe.equals(rightDe)) {
            throw new IllegalArgumentException(playerDuplicatesError);
        }
        this.leftDe = leftDe;
    }

    /**
     * @throws NullPointerException If given argument is null
     * @throws IllegalArgumentException If given argument is the same as another player
     */
    public void setRightDe(Defenseman rightDe) throws NullPointerException, IllegalArgumentException {
        if (rightDe == null) {
            throw new NullPointerException(nullError);
        }
        if (rightDe.equals(leftWing) || rightDe.equals(rightWing) || rightDe.equals(leftDe)) {
            throw new IllegalArgumentException(playerDuplicatesError);
        }
        this.rightDe = rightDe;
    }

    /**
     * Utilize Line methods to apply stats to players, also updates numberScored and numberOpps
     * @param position Position of the player who scored the goal.
     * @throws IllegalArgumentException See super class
     */
    @Override
    public void score(Position position) throws NullPointerException, IllegalArgumentException {
        if (position == null) {
            throw new NullPointerException(nullError);
        }
        if (position == Position.CENTER) {
            center.score();
        } else {
            center.scoredOnIce();
        }
        if (position == Position.LEFT_WING) {
            leftWing.score();
        } else {
            leftWing.scoredOnIce();
        }
        if (position == Position.RIGHT_WING) {
            rightWing.score();
        } else {
            rightWing.scoredOnIce();
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
        success();
    }

    /**
     * Utilize Line methods to apply stats to players, also updates numberScored and numberOpps
     * @param scorer Position of the player who scored the goal
     * @param assist The position of the player who assisted the goal
     * @throws IllegalArgumentException See super class
     */
    @Override
    public void score(Position scorer, Position assist) throws NullPointerException, IllegalArgumentException {
        if (scorer == null || assist == null) {
            throw new NullPointerException(nullError);
        }
        if (scorer == assist) {
            throw new IllegalArgumentException(Line.positionDuplicatesError);
        }

        if (scorer == Position.CENTER) {
            center.score();
        } else if (assist == Position.CENTER) {
            center.assist();
        } else {
            center.scoredOnIce();
        }

        if (scorer == Position.LEFT_WING) {
            leftWing.score();
        } else if (assist == Position.LEFT_WING) {
            leftWing.assist();
        } else {
            leftWing.scoredOnIce();
        }

        if (scorer == Position.RIGHT_WING) {
            rightWing.score();
        } else if (assist == Position.RIGHT_WING) {
            rightWing.assist();
        } else {
            rightWing.scoredOnIce();
        }

        if (scorer == Position.RIGHT_DEFENSE) {
            rightDe.score();
        } else if (assist == Position.RIGHT_DEFENSE) {
            rightDe.assist();
        } else {
            rightDe.scoredOnIce();
        }

        if (scorer == Position.LEFT_DEFENSE) {
            leftDe.score();
        } else if (assist == Position.LEFT_DEFENSE) {
            leftDe.assist();
        } else {
            leftDe.scoredOnIce();
        }

        success();
    }

    /**
     * Utilize Line methods to apply stats to players, also updates numberScored and numberOpps
     * @param scorer Position of the player who scored the goal
     * @param assist1 Position of player who assisted the goal
     * @param assist2 Position of other player who assisted the goal
     * @throws IllegalArgumentException See super class
     */
    @Override
    public void score(Position scorer, Position assist1, Position assist2) throws NullPointerException,
            IllegalArgumentException {
        if (scorer == null || assist1 == null || assist2 == null) {
            throw new NullPointerException(nullError);
        }
        if (scorer == assist1 || scorer == assist2 || assist1 == assist2) {
            throw new IllegalArgumentException(Line.positionDuplicatesError);
        }

        if (scorer == Position.CENTER) {
            center.score();
        } else if (assist1 == Position.CENTER || assist2 == Position.CENTER) {
            center.assist();
        } else {
            center.scoredOnIce();
        }

        if (scorer == Position.LEFT_WING) {
            leftWing.score();
        } else if (assist1 == Position.LEFT_WING || assist2 == Position.LEFT_WING) {
            leftWing.assist();
        } else {
            leftWing.scoredOnIce();
        }

        if (scorer == Position.RIGHT_WING) {
            rightWing.score();
        } else if (assist1 == Position.RIGHT_WING || assist2 == Position.RIGHT_WING) {
            rightWing.assist();
        } else {
            rightWing.scoredOnIce();
        }

        if (scorer == Position.RIGHT_DEFENSE) {
            rightDe.score();
        } else if (assist1 == Position.RIGHT_DEFENSE || assist2 == Position.RIGHT_DEFENSE) {
            rightDe.assist();
        } else {
            rightDe.scoredOnIce();
        }

        if (scorer == Position.LEFT_DEFENSE) {
            leftDe.score();
        } else if (assist1 == Position.LEFT_DEFENSE || assist2 == Position.LEFT_DEFENSE) {
            leftDe.assist();
        } else {
            leftDe.scoredOnIce();
        }

        success();
    }

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
                        PP%%: %.2f
                        """, getName(), center.getName(), leftWing.getName(), rightWing.getName(), leftDe.getName(),
                rightDe.getName(), getSuccessPercent());
    }
}
