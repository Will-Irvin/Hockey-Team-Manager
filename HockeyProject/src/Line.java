public class Line {
    private String name;
    private Center center;
    private Skater leftWing;
    private Skater rightWing;
    private Defenseman leftDe;
    private Defenseman rightDe;

    public Line(String name, Center center, Skater leftWing, Skater rightWing, Defenseman leftDe, Defenseman rightDe) {
        if (center == null || leftDe == null || leftWing == null || rightWing == null || rightDe == null) {
            throw new NullPointerException("Inputted players must not be null");
        }
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

    public void score(Position position) {
        switch (position) {
            case CENTER:
                center.score();
                leftWing.scoredOnIce();
                rightWing.scoredOnIce();
                leftDe.scoredOnIce();
                rightDe.scoredOnIce();
                break;
            case LEFT_WING:
                leftWing.score();
                center.scoredOnIce();
                rightWing.scoredOnIce();
                leftDe.scoredOnIce();
                rightDe.scoredOnIce();
                break;
            case RIGHT_WING:
                rightWing.score();
                leftWing.scoredOnIce();
                center.scoredOnIce();
                leftDe.scoredOnIce();
                rightDe.scoredOnIce();
                break;
            case LEFT_DEFENSE:
                leftDe.score();
                leftWing.scoredOnIce();
                rightWing.scoredOnIce();
                center.scoredOnIce();
                rightDe.scoredOnIce();
                break;
            case RIGHT_DEFENSE:
                rightDe.score();
                leftWing.scoredOnIce();
                rightWing.scoredOnIce();
                leftDe.scoredOnIce();
                center.scoredOnIce();
                break;
        }
    }

    public void score(Position scorer, Position assist) throws IllegalArgumentException {
        if (scorer == assist) {
            throw new IllegalArgumentException("Player that scored cannot have the same" +
                    " position as the player who assisted");
        }

        // Center
        if (scorer == Position.CENTER) {
            center.score();
        } else if (assist == Position.CENTER) {
            center.assist();
        } else {
            center.scoredOnIce();
        }

        // Left Wing
        if (scorer == Position.LEFT_WING) {
            leftWing.score();
        } else if (assist == Position.LEFT_WING) {
            leftWing.assist();
        } else {
            leftWing.scoredOnIce();
        }

        // Right Wing
        if (scorer == Position.RIGHT_WING) {
            rightWing.score();
        } else if (assist == Position.RIGHT_WING) {
            rightWing.assist();
        } else {
            rightWing.scoredOnIce();
        }

        // Left Defense
        if (scorer == Position.LEFT_DEFENSE) {
            leftDe.score();
        } else if (assist == Position.LEFT_DEFENSE) {
            leftDe.assist();
        } else {
            leftDe.scoredOnIce();
        }

        // Right Defense
        if (scorer == Position.RIGHT_DEFENSE) {
            rightDe.score();
        } else if (assist == Position.RIGHT_DEFENSE) {
            rightDe.assist();
        } else {
            rightDe.scoredOnIce();
        }
    }

    public void score(Position scorer, Position assist1, Position assist2) throws IllegalArgumentException {
        if (scorer == assist1 || scorer == assist2 || assist1 == assist2) {
            throw new IllegalArgumentException("None of the given positions can match");
        }

        // Center
        if (scorer == Position.CENTER) {
            center.score();
        } else if (assist1 == Position.CENTER || assist2 == Position.CENTER) {
            center.assist();
        } else {
            center.scoredOnIce();
        }

        // Left Wing
        if (scorer == Position.LEFT_WING) {
            leftWing.score();
        } else if (assist1 == Position.LEFT_WING || assist2 == Position.LEFT_WING) {
            leftWing.assist();
        } else {
            leftWing.scoredOnIce();
        }

        // Right Wing
        if (scorer == Position.RIGHT_WING) {
            rightWing.score();
        } else if (assist1 == Position.RIGHT_WING || assist2 == Position.RIGHT_WING) {
            rightWing.assist();
        } else {
            rightWing.scoredOnIce();
        }

        // Left Defense
        if (scorer == Position.LEFT_DEFENSE) {
            leftDe.score();
        } else if (assist1 == Position.LEFT_DEFENSE || assist2 == Position.LEFT_DEFENSE) {
            leftDe.assist();
        } else {
            leftDe.scoredOnIce();
        }

        // Right Defense
        if (scorer == Position.RIGHT_DEFENSE) {
            rightDe.score();
        } else if (assist1 == Position.RIGHT_DEFENSE || assist2 == Position.RIGHT_DEFENSE) {
            rightDe.assist();
        } else {
            rightDe.scoredOnIce();
        }
    }

    public void lineScoredOn() {
        center.scoredAgainst();
        leftDe.scoredAgainst();
        rightDe.scoredAgainst();
        leftWing.scoredAgainst();
        rightWing.scoredAgainst();
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
