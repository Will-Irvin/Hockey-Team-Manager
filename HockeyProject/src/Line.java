public class Line {
    private String name;
    private Center center;
    private Skater leftWing;
    private Skater rightWing;
    private Defenseman leftDe;
    private Defenseman rightDe;
    private static final String scoreError = "A given position is not assigned to this line";

    public Line(String name, Center center, Skater leftWing, Skater rightWing, Defenseman leftDe, Defenseman rightDe) {
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
        try {
            if (position == Position.CENTER) {
                center.score();
            } else {
                center.scoredOnIce();
            }
        } catch (NullPointerException ignored) {}

        // Left Wing
        try {
            if (position == Position.LEFT_WING) {
                leftWing.score();
            } else {
                leftWing.scoredOnIce();
            }
        } catch (NullPointerException ignored) {}

        // Right Wing
        try {
            if (position == Position.RIGHT_WING) {
                rightWing.score();
            } else {
                rightWing.scoredOnIce();
            }
        } catch (NullPointerException ignored) {}

        // Left Defense
        try {
            if (position == Position.LEFT_DEFENSE) {
                leftDe.score();
            } else {
                leftDe.scoredOnIce();
            }
        } catch (NullPointerException ignored) {}

        // Right Defense
        try {
            if (position == Position.RIGHT_DEFENSE) {
                rightDe.score();
            } else {
                rightDe.scoredOnIce();
            }
        } catch (NullPointerException ignored) {}
    }

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
        try {
            if (scorer == Position.CENTER) {
                center.score();
            } else if (assist == Position.CENTER) {
                center.assist();
            } else {
                center.scoredOnIce();
            }
        } catch (NullPointerException ignored) {}

        // Left Wing
        try {
            if (scorer == Position.LEFT_WING) {
                leftWing.score();
            } else if (assist == Position.LEFT_WING) {
                leftWing.assist();
            } else {
                leftWing.scoredOnIce();
            }
        } catch (NullPointerException ignored) {}

        // Right Wing
        try {
            if (scorer == Position.RIGHT_WING) {
                rightWing.score();
            } else if (assist == Position.RIGHT_WING) {
                rightWing.assist();
            } else {
                rightWing.scoredOnIce();
            }
        } catch (NullPointerException ignored) {}

        // Left Defense
        try {
            if (scorer == Position.LEFT_DEFENSE) {
                leftDe.score();
            } else if (assist == Position.LEFT_DEFENSE) {
                leftDe.assist();
            } else {
                leftDe.scoredOnIce();
            }
        } catch (NullPointerException ignored) {}

        // Right Defense
        try {
            if (scorer == Position.RIGHT_DEFENSE) {
                rightDe.score();
            } else if (assist == Position.RIGHT_DEFENSE) {
                rightDe.assist();
            } else {
                rightDe.scoredOnIce();
            }
        } catch (NullPointerException ignored) {}
    }

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
        try {
            if (scorer == Position.CENTER) {
                center.score();
            } else if (assist1 == Position.CENTER || assist2 == Position.CENTER) {
                center.assist();
            } else {
                center.scoredOnIce();
            }
        } catch (NullPointerException ignored) {}

        // Left Wing
        try {
            if (scorer == Position.LEFT_WING) {
                leftWing.score();
            } else if (assist1 == Position.LEFT_WING || assist2 == Position.LEFT_WING) {
                leftWing.assist();
            } else {
                leftWing.scoredOnIce();
            }
        } catch (NullPointerException ignored) {}

        // Right Wing
        try {
            if (scorer == Position.RIGHT_WING) {
                rightWing.score();
            } else if (assist1 == Position.RIGHT_WING || assist2 == Position.RIGHT_WING) {
                rightWing.assist();
            } else {
                rightWing.scoredOnIce();
            }
        } catch (NullPointerException ignored) {}

        // Left Defense
        try {
            if (scorer == Position.LEFT_DEFENSE) {
                leftDe.score();
            } else if (assist1 == Position.LEFT_DEFENSE || assist2 == Position.LEFT_DEFENSE) {
                leftDe.assist();
            } else {
                leftDe.scoredOnIce();
            }
        } catch (NullPointerException ignored) {}

        // Right Defense
        try {
            if (scorer == Position.RIGHT_DEFENSE) {
                rightDe.score();
            } else if (assist1 == Position.RIGHT_DEFENSE || assist2 == Position.RIGHT_DEFENSE) {
                rightDe.assist();
            } else {
                rightDe.scoredOnIce();
            }
        } catch (NullPointerException ignored) {}
    }

    public void lineScoredOn() {
        try {
            center.scoredAgainst();
        } catch (NullPointerException ignored) {}
        try {
            leftDe.scoredAgainst();
        } catch (NullPointerException ignored) {}
        try {
            rightDe.scoredAgainst();
        } catch (NullPointerException ignored) {}
        try {
            leftWing.scoredAgainst();
        } catch (NullPointerException ignored) {}
        try {
            rightWing.scoredAgainst();
        } catch (NullPointerException ignored) {}
    }

    public String toString() {
        return String.format("%s:\n" +
                "Center: %s\n" +
                "Left Wing: %s\n" +
                "Right Wing: %s\n" +
                "Left Defense: %s\n" +
                "Right Defense: %s", this.name, center.getName(), leftWing.getName(), rightWing.getName(),
                leftDe.getName(), rightDe.getName());
    }
}
