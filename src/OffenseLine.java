/**
 * OffenseLine
 *
 * A class for a line composed of three offensive players. Methods will ensure that the stats of all three players are
 * manipulated together
 */
public class OffenseLine extends Line {
    private Center center;
    private Skater leftWing;
    private Skater rightWing;

    /**
     * Assigns given name to the line and given players to their respective positions, leaves defensive positions null.
     * @throws NullPointerException If any of the given players are null
     * @throws IllegalArgumentException If any of the given players are duplicates
     */
    public OffenseLine(String name, Center center, Skater leftWing, Skater rightWing) throws NullPointerException,
            IllegalArgumentException {
        super(name);
        if (leftWing == null || center == null || rightWing == null) {
            throw new NullPointerException(EMPTY_POSITIONS);
        }
        if (center.equals(leftWing) || center.equals(rightWing) || rightWing.equals(leftWing)) {
            throw new IllegalArgumentException(PLAYER_DUPLICATES_ERROR);
        }
        this.center = center;
        this.leftWing = leftWing;
        this.rightWing = rightWing;
    }

    /**
     * Creates a copy of the given line with the same parameters and name as the given argument.
     * @param line The given line to copy
     */
    public OffenseLine(OffenseLine line) {
        super(line.getName());
        this.center = line.center;
        this.leftWing = line.leftWing;
        this.rightWing = line.rightWing;
    }

    // Setter Methods

    /**
     * @throws NullPointerException If given argument is null
     * @throws IllegalArgumentException If given argument is equal to another player on the line
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
     * @throws IllegalArgumentException If given argument is equal to another player on the line
     */
    public void setLeftWing(Skater leftWing) throws NullPointerException, IllegalArgumentException {
        if (leftWing == null) {
            throw new NullPointerException(EMPTY_POSITIONS);
        }
        if (leftWing.equals(center) || leftWing.equals(rightWing)) {
            throw new NullPointerException(PLAYER_DUPLICATES_ERROR);
        }
        this.leftWing = leftWing;
    }

    /**
     * @throws NullPointerException If given argument is null
     * @throws IllegalArgumentException If given argument is equal to another player on the line
     */
    public void setRightWing(Skater rightWing) throws NullPointerException, IllegalArgumentException {
        if (rightWing == null) {
            throw new NullPointerException(EMPTY_POSITIONS);
        }
        if (leftWing.equals(center) || leftWing.equals(rightWing)) {
            throw new IllegalArgumentException(PLAYER_DUPLICATES_ERROR);
        }
        this.rightWing = rightWing;
    }

    /**
     * Updates stats for all players on this line and given DefenseLine when a goal is scored
     * @param scorer Position of player who scored
     * @param de Defense line on the ice when the goal was scored
     * @throws NullPointerException If given arguments are null
     */
    public void score(Position scorer, DefenseLine de) throws NullPointerException {
        if (de == null || scorer == null) {
            throw new NullPointerException(Player.NULL_ERROR);
        }

        scoreOffense(scorer, center, leftWing, rightWing);

        if (scorer == Position.Right_Defense) {
            de.getRightDe().score();
        } else {
            de.getRightDe().scoredOnIce();
        }

        if (scorer == Position.Left_Defense) {
            de.getLeftDe().score();
        } else {
            de.getLeftDe().scoredOnIce();
        }
    }

    /**
     * Updates stats for all players on this line and given DefenseLine when a goal is scored
     * @param scorer Position of player who scored
     * @param assist Position of player who assisted
     * @param de Defense line on the ice when the goal was scored
     * @throws NullPointerException If given arguments are null
     * @throws IllegalArgumentException If given positions are the same
     */
    public void score(Position scorer, Position assist, DefenseLine de) throws NullPointerException,
            IllegalArgumentException {
        if (de == null || scorer == null || assist == null) {
            throw new NullPointerException(EMPTY_POSITIONS);
        }
        if (scorer == assist) {
            throw new IllegalArgumentException(POSITION_DUPLICATES_ERROR);
        }

        scoreOffense(scorer, assist, center, leftWing, rightWing);
        scoreDefense(scorer, assist, de.getRightDe(), de.getLeftDe());
    }

    /**
     * Updates stats for all players on this line and given DefenseLine when a goal is scored
     * @param scorer Position of player who scored
     * @param assist1 Position of player who assisted
     * @param assist2 Position of player who assisted
     * @param de The defense line on the ice when the goal was scored
     * @throws NullPointerException If given arguments are null
     * @throws IllegalArgumentException If given positions are the same
     */
    public void score(Position scorer, Position assist1, Position assist2, DefenseLine de) throws NullPointerException,
            IllegalArgumentException {
        if (de == null || scorer == null || assist1 == null || assist2 == null) {
            throw new NullPointerException(EMPTY_POSITIONS);
        }
        if (scorer == assist1 || scorer == assist2 || assist1 == assist2) {
            throw new IllegalArgumentException(POSITION_DUPLICATES_ERROR);
        }

        scoreOffense(scorer, assist1, assist2, center, leftWing, rightWing);
        scoreDefense(scorer, assist1, assist2, de.getRightDe(), de.getLeftDe());
    }

    // Used to make a center win a face off
    public void winFaceOff() {
        center.winFaceOff();
    }

    // Used to make center lose face off
    public void loseFaceOff() {
        center.loseFaceOff();
    }

    // Updates stats for the entire line when they are scored on
    @Override
    public void lineScoredOn() {
        rightWing.scoredAgainst();
        center.scoredAgainst();
        leftWing.scoredAgainst();
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
                Right Wing: %s""", getName(), center.getName(), leftWing.getName(), rightWing.getName());
    }

    /**
     * @return An array of the skaters on this line
     */
    @Override
    public Skater[] getSkaters() {
        return new Skater[]{center, leftWing, rightWing};
    }
}
