import java.io.Serializable;

/**
 * Line
 * Class that stores information and methods to manipulate a line containing five players, a center, two wingers, and
 * two defensemen.
 */

public class Line implements Serializable {
    private String name;
    private Center center;
    private Skater leftWing;
    private Skater rightWing;
    private Defenseman leftDe;
    private Defenseman rightDe;

    // Error message for score methods
    private static final String scoreError = "A given position is not assigned to this line";

    /**
     * Initializes instance variables with their respective arguments. A player can be left null if the user so chooses.
     * @throws NullPointerException Thrown if the given name is null
     * @throws IllegalArgumentException Thrown if the given name is blank
     */
    public Line(String name, Center center, Skater leftWing, Skater rightWing, Defenseman leftDe, Defenseman rightDe)
            throws NullPointerException, IllegalArgumentException {
        if (name == null) {
            throw new NullPointerException("Name cannot be null");
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        this.name = name;
        this.center = center;
        this.leftWing = leftWing;
        this.rightWing = rightWing;
        this.leftDe = leftDe;
        this.rightDe = rightDe;
    }

    // Getter Methods

    public String getName() {
        return name;
    }

    public Center getCenter() {
        return center;
    }

    public Skater getLeftWing() {
        return leftWing;
    }

    public Skater getRightWing() {
        return rightWing;
    }

    public Defenseman getLeftDe() {
        return leftDe;
    }

    public Defenseman getRightDe() {
        return rightDe;
    }

    /**
     * @throws NullPointerException Thrown if the given name is null
     * @throws IllegalArgumentException Thrown if the given name is blank
     */
    public void setName(String name) throws NullPointerException, IllegalArgumentException {
        if (name == null) {
            throw new NullPointerException("Name cannot be null");
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        this.name = name;
    }

    // Position setter methods

    public void setCenter(Center center) {
        this.center = center;
    }

    public void setLeftWing(Skater leftWing) {
        this.leftWing = leftWing;
    }

    public void setRightWing(Skater rightWing) {
        this.rightWing = rightWing;
    }

    public void setLeftDe(Defenseman leftDe) {
        this.leftDe = leftDe;
    }

    public void setRightDe(Defenseman rightDe) {
        this.rightDe = rightDe;
    }

    /**
     * Updates the stats for each player on the line after a goal is scored.
     * @param position Position of the player who scored the goal.
     * @throws IllegalArgumentException Thrown if the given position is not assigned to the line
     */
    public void score(Position position) throws IllegalArgumentException {
        if (center == null && position == Position.CENTER ) {
            throw new IllegalArgumentException(scoreError);
        }
        if (leftWing == null && position == Position.LEFT_WING) {
            throw new IllegalArgumentException(scoreError);
        }
        if (rightWing == null && position == Position.RIGHT_WING ) {
            throw new IllegalArgumentException(scoreError);
        }
        if (leftDe == null && position == Position.LEFT_DEFENSE) {
            throw new IllegalArgumentException(scoreError);
        }
        if (rightDe == null && position == Position.RIGHT_DEFENSE) {
            throw new IllegalArgumentException(scoreError);
        }
        // Center
        if (center != null) {
            if (position == Position.CENTER) {
                center.score();
            } else {
                center.scoredOnIce();
            }
        }

        // Left Wing
        if (leftWing != null) {
            if (position == Position.LEFT_WING) {
                leftWing.score();
            } else {
                leftWing.scoredOnIce();
            }
        }

        // Right Wing
        if (rightWing != null) {
            if (position == Position.RIGHT_WING) {
                rightWing.score();
            } else {
                rightWing.scoredOnIce();
            }
        }

        // Left Defense
        if (leftDe != null) {
            if (position == Position.LEFT_DEFENSE) {
                leftDe.score();
            } else {
                leftDe.scoredOnIce();
            }
        }

        // Right Defense
        if (rightDe != null) {
            if (position == Position.RIGHT_DEFENSE) {
                rightDe.score();
            } else {
                rightDe.scoredOnIce();
            }
        }
    }

    /**
     * Works like the earlier score method; however, used if the goal was assisted
     * @param scorer The position of the player who scored the goal
     * @param assist The position of the player who assisted the goal
     * @throws IllegalArgumentException Thrown if the two given positions match or if either of the given positions are
     * not assigned to the line
     */
    public void score(Position scorer, Position assist) throws IllegalArgumentException {
        if (scorer == assist) {
            throw new IllegalArgumentException("Player that scored cannot have the same" +
                    " position as the player who assisted");
        }

        if (center == null && (scorer == Position.CENTER || assist == Position.CENTER)) {
            throw new IllegalArgumentException(scoreError);
        }
        if (leftWing == null && (scorer == Position.LEFT_WING || assist == Position.LEFT_WING)) {
            throw new IllegalArgumentException(scoreError);
        }
        if (rightWing == null && (scorer == Position.RIGHT_WING || assist == Position.RIGHT_WING)) {
            throw new IllegalArgumentException(scoreError);
        }
        if (leftDe == null && (scorer == Position.LEFT_DEFENSE || assist == Position.LEFT_DEFENSE)) {
            throw new IllegalArgumentException(scoreError);
        }
        if (rightDe == null && (scorer == Position.RIGHT_DEFENSE || assist == Position.RIGHT_DEFENSE)) {
            throw new IllegalArgumentException(scoreError);
        }

        // Center
        if (center != null) {
            if (scorer == Position.CENTER) {
                center.score();
            } else if (assist == Position.CENTER) {
                center.assist();
            } else {
                center.scoredOnIce();
            }
        }

        // Left Wing
        if (leftWing != null) {
            if (scorer == Position.LEFT_WING) {
                leftWing.score();
            } else if (assist == Position.LEFT_WING) {
                leftWing.assist();
            } else {
                leftWing.scoredOnIce();
            }
        }

        // Right Wing
        if (rightWing != null) {
            if (scorer == Position.RIGHT_WING) {
                rightWing.score();
            } else if (assist == Position.RIGHT_WING) {
                rightWing.assist();
            } else {
                rightWing.scoredOnIce();
            }
        }

        // Left Defense
        if (leftDe != null) {
            if (scorer == Position.LEFT_DEFENSE) {
                leftDe.score();
            } else if (assist == Position.LEFT_DEFENSE) {
                leftDe.assist();
            } else {
                leftDe.scoredOnIce();
            }
        }

        // Right Defense
        if (rightDe != null) {
            if (scorer == Position.RIGHT_DEFENSE) {
                rightDe.score();
            } else if (assist == Position.RIGHT_DEFENSE) {
                rightDe.assist();
            } else {
                rightDe.scoredOnIce();
            }
        }
    }

    /**
     * Does the role of earlier score methods; however, used if there are two assisters
     * @param scorer Position of player who scored the goal
     * @param assist1 Position of player who assisted the goal
     * @param assist2 Position of other player who assisted the goal
     * @throws IllegalArgumentException Thrown if any of the given positions match or if any of the given positions are
     * not assigned to the line
     */
    public void score(Position scorer, Position assist1, Position assist2) throws IllegalArgumentException {
        if (scorer == assist1 || scorer == assist2 || assist1 == assist2) {
            throw new IllegalArgumentException("None of the given positions can match");
        }

        if (center == null && (scorer == Position.CENTER || assist1 == Position.CENTER || assist2 == Position.CENTER)) {
            throw new IllegalArgumentException(scoreError);
        }
        if (leftWing == null && (scorer == Position.LEFT_WING || assist1 == Position.LEFT_WING ||
                                 assist2 == Position.LEFT_WING)) {
            throw new IllegalArgumentException(scoreError);
        }
        if (rightWing == null && (scorer == Position.RIGHT_WING || assist1 == Position.RIGHT_WING ||
                                  assist2 == Position.RIGHT_WING)) {
            throw new IllegalArgumentException(scoreError);
        }
        if (leftDe == null && (scorer == Position.LEFT_DEFENSE || assist1 == Position.LEFT_DEFENSE ||
                               assist2 == Position.LEFT_DEFENSE)) {
            throw new IllegalArgumentException(scoreError);
        }
        if (rightDe == null && (scorer == Position.RIGHT_DEFENSE || assist1 == Position.RIGHT_DEFENSE ||
                                assist2 == Position.RIGHT_DEFENSE)) {
            throw new IllegalArgumentException(scoreError);
        }
        // Center
        if (center != null) {
            if (scorer == Position.CENTER) {
                center.score();
            } else if (assist1 == Position.CENTER || assist2 == Position.CENTER) {
                center.assist();
            } else {
                center.scoredOnIce();
            }
        }

        // Left Wing
        if (leftWing != null) {
            if (scorer == Position.LEFT_WING) {
                leftWing.score();
            } else if (assist1 == Position.LEFT_WING || assist2 == Position.LEFT_WING) {
                leftWing.assist();
            } else {
                leftWing.scoredOnIce();
            }
        }

        // Right Wing
        if (rightWing != null) {
            if (scorer == Position.RIGHT_WING) {
                rightWing.score();
            } else if (assist1 == Position.RIGHT_WING || assist2 == Position.RIGHT_WING) {
                rightWing.assist();
            } else {
                rightWing.scoredOnIce();
            }
        }

        // Left Defense
        if (leftDe != null) {
            if (scorer == Position.LEFT_DEFENSE) {
                leftDe.score();
            } else if (assist1 == Position.LEFT_DEFENSE || assist2 == Position.LEFT_DEFENSE) {
                leftDe.assist();
            } else {
                leftDe.scoredOnIce();
            }
        }

        // Right Defense
        if (rightDe != null) {
            if (scorer == Position.RIGHT_DEFENSE) {
                rightDe.score();
            } else if (assist1 == Position.RIGHT_DEFENSE || assist2 == Position.RIGHT_DEFENSE) {
                rightDe.assist();
            } else {
                rightDe.scoredOnIce();
            }
        }
    }

    /**
     * Calls the scored against method on every player assigned to the line
     */
    public void lineScoredOn() {
        if (center != null) {
            center.scoredAgainst();
        }
        if (leftDe != null) {
            leftDe.scoredAgainst();
        }
        if (rightDe != null) {
            rightDe.scoredAgainst();
        }
        if (leftWing != null) {
            leftWing.scoredAgainst();
        }
        if (rightWing != null) {
            rightWing.scoredAgainst();
        }
    }

    /**
     * @param o Object being compared to the line
     * @return True if the object is a line, has the same name as the current line, and has the same players as the
     * current line
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Line l) {
            return l.getName().equals(this.name);
        }
        return false;
    }

    /**
     * @return A formatted String that lists every player included in the line
     */
    public String lineRoster() {
        String result = String.format("%s:\n", this.name);
        if (center != null) {
            result += String.format("Center: %s\n", center.getName());
        }
        if (leftWing != null) {
            result += String.format("Left Wing: %s\n", leftWing.getName());
        }
        if (rightWing != null) {
            result += String.format("Right Wing: %s\n", rightWing.getName());
        }
        if (leftDe != null) {
            result += String.format("Left Defense: %s\n", leftDe.getName());
        }
        if (rightDe != null) {
            result += String.format("Right Defense: %s", rightDe.getName());
        }
        return result;
    }

    // toString Method
    @Override
    public String toString() {
        return name;
    }
}
