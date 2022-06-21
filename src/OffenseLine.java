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
            throw new NullPointerException(nullError);
        }
        if (center.equals(leftWing) || center.equals(rightWing) || rightWing.equals(leftWing)) {
            throw new IllegalArgumentException(playerDuplicatesError);
        }
        this.center = center;
        this.leftWing = leftWing;
        this.rightWing = rightWing;
    }

    // Setter Methods

    /**
     * @throws NullPointerException If given argument is null
     * @throws IllegalArgumentException If given argument is equal to another player on the line
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
     * @throws IllegalArgumentException If given argument is equal to another player on the line
     */
    public void setLeftWing(Skater leftWing) throws NullPointerException, IllegalArgumentException {
        if (leftWing == null) {
            throw new NullPointerException(nullError);
        }
        if (leftWing.equals(center) || leftWing.equals(rightWing)) {
            throw new NullPointerException(playerDuplicatesError);
        }
        this.leftWing = leftWing;
    }

    /**
     * @throws NullPointerException If given argument is null
     * @throws IllegalArgumentException If given argument is equal to another player on the line
     */
    public void setRightWing(Skater rightWing) throws NullPointerException, IllegalArgumentException {
        if (rightWing == null) {
            throw new NullPointerException(nullError);
        }
        if (leftWing.equals(center) || leftWing.equals(rightWing)) {
            throw new IllegalArgumentException(playerDuplicatesError);
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
            throw new NullPointerException("Please enter all given arguments");
        }

        if (scorer == Position.CENTER) {
            center.score();
        } else {
            center.scoredOnIce();
        }

        if (scorer == Position.LEFT_WING) {
            leftWing.score();
        } else {
            leftWing.scoredOnIce();
        }

        if (scorer == Position.RIGHT_WING) {
            rightWing.score();
        } else {
            rightWing.scoredOnIce();
        }

        if (scorer == Position.RIGHT_DEFENSE) {
            de.getRightDe().score();
        } else {
            de.getRightDe().scoredOnIce();
        }

        if (scorer == Position.LEFT_DEFENSE) {
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
            throw new NullPointerException(nullError);
        }
        if (scorer == assist) {
            throw new IllegalArgumentException(positionDuplicatesError);
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
            de.getRightDe().score();
        } else if (assist == Position.RIGHT_DEFENSE) {
            de.getRightDe().assist();
        } else {
            de.getRightDe().scoredOnIce();
        }

        if (scorer == Position.LEFT_DEFENSE) {
            de.getLeftDe().score();
        } else if (assist == Position.LEFT_DEFENSE) {
            de.getLeftDe().assist();
        } else {
            de.getLeftDe().scoredOnIce();
        }
    }

    /**
     * Updates stats for all players on this line and given DefenseLine when a goal is scored
     * @param scorer Position of player who scored
     * @param assist1 Position of player who assisted
     * @param assist2 Position of player who assisted
     * @param de Defense line on the ice when the goal was scored
     * @throws NullPointerException If given arguments are null
     * @throws IllegalArgumentException If given positions are the same
     */
    public void score(Position scorer, Position assist1, Position assist2, DefenseLine de) throws NullPointerException,
            IllegalArgumentException {
        if (de == null || scorer == null || assist1 == null || assist2 == null) {
            throw new NullPointerException(nullError);
        }
        if (scorer == assist1 || scorer == assist2 || assist1 == assist2) {
            throw new IllegalArgumentException(positionDuplicatesError);
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
            de.getRightDe().score();
        } else if (assist1 == Position.RIGHT_DEFENSE || assist2 == Position.RIGHT_DEFENSE) {
            de.getRightDe().assist();
        } else {
            de.getRightDe().scoredOnIce();
        }

        if (scorer == Position.LEFT_DEFENSE) {
            de.getLeftDe().score();
        } else if (assist1 == Position.LEFT_DEFENSE || assist2 == Position.LEFT_DEFENSE) {
            de.getLeftDe().assist();
        } else {
            de.getLeftDe().scoredOnIce();
        }
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
}
