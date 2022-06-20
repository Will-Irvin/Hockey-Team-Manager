/**
 * OffenseLine
 *
 * A class for a line specifically composed of three offensive players. Coaches will often have offense lines and
 * defense lines and while they sometimes line up in full lines, this is not always the case.
 */

public class OffenseLine extends Line {
    private Center center;
    private Skater leftWing;
    private Skater rightWing;

    /**
     * Assigns given name to the line and given players to their respective positions, leaves defensive positions null.
     * @throws NullPointerException If any of the given players are null
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
    }

    /**
     * Similar to score methods in full line class; however, accounts for having offense lines and defense lines that
     * do not necessarily match up.
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
     * Like score method for full line class, but for separate offense and defense lines
     * @throws NullPointerException If given arguments are null
     * @throws IllegalArgumentException If given positions match
     */
    public void score(Position scorer, Position assist, DefenseLine de) throws NullPointerException,
            IllegalArgumentException {
        if (de == null || scorer == null || assist == null) {
            throw new NullPointerException("Please enter all given arguments");
        }
        if (scorer == assist) {
            throw new IllegalArgumentException("Assist and scorer cannot have the same position");
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
     * Like score method for full line class, but for separate offense and defense lines
     * @throws NullPointerException If given arguments are null
     * @throws IllegalArgumentException If any positions match
     */
    public void score(Position scorer, Position assist1, Position assist2, DefenseLine de) throws NullPointerException,
            IllegalArgumentException {
        if (de == null || scorer == null || assist1 == null || assist2 == null) {
            throw new NullPointerException("Please enter all given arguments");
        }
        if (scorer == assist1 || scorer == assist2 || assist1 == assist2) {
            throw new IllegalArgumentException("All players must have a different position");
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

    @Override
    public void lineScoredOn() {
        rightWing.scoredAgainst();
        center.scoredAgainst();
        leftWing.scoredAgainst();
    }

    @Override
    public String lineRoster() {
        return String.format("""
                %s
                Center: %s
                Left Wing: %s
                Right Wing: %s""", getName(), center.getName(), leftWing.getName(), rightWing.getName());
    }
}
