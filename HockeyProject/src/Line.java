public class Line {
    private Center center;
    private Skater leftWing;
    private Skater rightWing;
    private Defenseman leftDe;
    private Defenseman rightDe;

    public Line(Center center, Skater leftWing, Skater rightWing, Defenseman leftDe, Defenseman rightDe) {
        if (center == null || leftDe == null || leftWing == null || rightWing == null || rightDe == null) {
            throw new NullPointerException("Inputted players must not be null");
        }
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

        if (scorer == Position.CENTER) {
            switch (assist) {
                case LEFT_WING:
                    center.score();
                    leftWing.assist();
                    rightWing.scoredOnIce();
                    leftDe.scoredOnIce();
                    rightDe.scoredOnIce();
                    break;
                case RIGHT_WING:
                    center.score();
                    rightWing.assist();
                    leftWing.scoredOnIce();
                    leftDe.scoredOnIce();
                    rightDe.scoredOnIce();
                    break;
                case LEFT_DEFENSE:
                    center.score();
                    leftDe.assist();
                    rightWing.scoredOnIce();
                    leftWing.scoredOnIce();
                    rightDe.scoredOnIce();
                    break;
                case RIGHT_DEFENSE:
                    center.score();
                    rightDe.assist();
                    rightWing.scoredOnIce();
                    leftDe.scoredOnIce();
                    leftWing.scoredOnIce();
                    break;
            }
        } else if (scorer == Position.LEFT_WING) {
            switch (assist) {
                case CENTER:
                    leftWing.score();
                    center.assist();
                    rightWing.scoredOnIce();
                    leftDe.scoredOnIce();
                    rightDe.scoredOnIce();
                    break;
                case RIGHT_WING:
                    leftWing.score();
                    rightWing.assist();
                    center.scoredOnIce();
                    leftDe.scoredOnIce();
                    rightDe.scoredOnIce();
                    break;
                case LEFT_DEFENSE:
                    leftWing.score();
                    leftDe.assist();
                    rightWing.scoredOnIce();
                    center.scoredOnIce();
                    rightDe.scoredOnIce();
                    break;
                case RIGHT_DEFENSE:
                    leftWing.score();
                    rightDe.assist();
                    rightWing.scoredOnIce();
                    leftDe.scoredOnIce();
                    center.scoredOnIce();
                    break;
            }
        } else if (scorer == Position.RIGHT_WING) {
            switch (assist) {
                case CENTER:
                    rightWing.score();
                    center.assist();
                    leftWing.scoredOnIce();
                    rightDe.scoredOnIce();
                    leftDe.scoredOnIce();
                    break;
                case LEFT_WING:
                    rightWing.score();
                    leftWing.assist();
                    center.scoredOnIce();
                    rightDe.scoredOnIce();
                    leftDe.scoredOnIce();
                    break;
                case RIGHT_DEFENSE:
                    rightWing.score();
                    rightDe.assist();
                    leftWing.scoredOnIce();
                    center.scoredOnIce();
                    leftDe.scoredOnIce();
                    break;
                case LEFT_DEFENSE:
                    rightWing.score();
                    leftDe.assist();
                    leftWing.scoredOnIce();
                    rightDe.scoredOnIce();
                    center.scoredOnIce();
                    break;
            }
        } else if (scorer == Position.LEFT_DEFENSE) {
            switch (assist) {
                case CENTER:
                    leftDe.score();
                    center.assist();
                    leftWing.scoredOnIce();
                    rightWing.scoredOnIce();
                    rightDe.scoredOnIce();
                    break;
                case LEFT_WING:
                    leftDe.score();
                    leftWing.assist();
                    center.scoredOnIce();
                    rightWing.scoredOnIce();
                    rightDe.scoredOnIce();
                    break;
                case RIGHT_WING:
                    leftDe.score();
                    rightWing.assist();
                    leftWing.scoredOnIce();
                    center.scoredOnIce();
                    rightDe.scoredOnIce();
                    break;
                case RIGHT_DEFENSE:
                    leftDe.score();
                    rightDe.assist();
                    leftWing.scoredOnIce();
                    rightWing.scoredOnIce();
                    center.scoredOnIce();
                    break;
            }
        } else if (scorer == Position.RIGHT_DEFENSE) {
            switch (assist) {
                case CENTER:
                    rightDe.score();
                    center.assist();
                    leftWing.scoredOnIce();
                    rightWing.scoredOnIce();
                    leftDe.scoredOnIce();
                    break;
                case LEFT_WING:
                    rightDe.score();
                    leftWing.assist();
                    center.scoredOnIce();
                    rightWing.scoredOnIce();
                    leftDe.scoredOnIce();
                    break;
                case RIGHT_WING:
                    rightDe.score();
                    rightWing.assist();
                    leftWing.scoredOnIce();
                    center.scoredOnIce();
                    leftDe.scoredOnIce();
                    break;
                case LEFT_DEFENSE:
                    rightDe.score();
                    leftDe.assist();
                    leftWing.scoredOnIce();
                    rightWing.scoredOnIce();
                    center.scoredOnIce();
                    break;
            }
        }
    }

    public void score(Position scorer, Position assist1, Position assist2) throws IllegalArgumentException {
        if (scorer == assist1 || scorer == assist2 || assist1 == assist2) {
            throw new IllegalArgumentException("None of the given positions can match");
        }

        if (scorer == Position.CENTER) {
            if (assist1 == Position.LEFT_WING) {
                switch (assist2) {
                    case RIGHT_WING:
                        center.score();
                        leftWing.assist();
                        rightWing.assist();
                        leftDe.scoredOnIce();
                        rightDe.scoredOnIce();
                        break;
                    case RIGHT_DEFENSE:
                        center.score();
                        leftWing.assist();
                        rightDe.assist();
                        leftDe.scoredOnIce();
                        rightWing.scoredOnIce();
                        break;
                    case LEFT_DEFENSE:
                        center.score();
                        leftWing.assist();
                        leftDe.assist();
                        rightWing.scoredOnIce();
                        rightDe.scoredOnIce();
                        break;
                }
            } else if (assist1 == Position.RIGHT_WING) {
                switch (assist2) {
                    case LEFT_WING:
                        center.score();
                        rightWing.assist();
                        leftWing.assist();
                        leftDe.scoredOnIce();
                        rightDe.scoredOnIce();
                        break;
                    case LEFT_DEFENSE:
                        center.score();
                        rightWing.assist();
                        leftDe.assist();
                        leftWing.scoredOnIce();
                        rightDe.scoredOnIce();
                        break;
                    case RIGHT_DEFENSE:
                        center.score();
                        rightWing.assist();
                        rightDe.assist();
                        leftDe.scoredOnIce();
                        leftWing.scoredOnIce();
                        break;
                }
            } else if (assist1 == Position.LEFT_DEFENSE) {
                switch (assist2) {
                    case LEFT_WING:
                        center.score();
                        leftDe.assist();
                        leftWing.assist();
                        rightDe.scoredOnIce();
                        rightWing.scoredOnIce();
                        break;
                    case RIGHT_WING:
                        center.score();
                        leftDe.assist();
                        rightWing.assist();
                        rightDe.scoredOnIce();
                        leftWing.scoredOnIce();
                        break;
                    case RIGHT_DEFENSE:
                        center.score();
                        leftDe.assist();
                        rightDe.assist();
                        leftWing.scoredOnIce();
                        rightWing.scoredOnIce();
                        break;
                }
            } else if (assist1 == Position.RIGHT_DEFENSE) {
                switch (assist2) {
                    case LEFT_WING:
                        center.score();
                        rightDe.assist();
                        leftWing.assist();
                        leftDe.scoredOnIce();
                        rightWing.scoredOnIce();
                        break;
                    case RIGHT_WING:
                        center.score();
                        rightDe.assist();
                        rightWing.assist();
                        leftDe.scoredOnIce();
                        leftWing.scoredOnIce();
                        break;
                    case LEFT_DEFENSE:
                        center.score();
                        rightDe.assist();
                        leftDe.assist();
                        leftWing.scoredOnIce();
                        rightWing.scoredOnIce();
                        break;
                }
            }
        } else if (scorer == Position.LEFT_WING) {
            if (assist1 == Position.CENTER) {

            } else if (assist1 == Position.RIGHT_WING) {

            } else if (assist1 == Position.LEFT_DEFENSE) {

            } else if (assist1 == Position.RIGHT_DEFENSE) {

            }
        } else if (scorer == Position.RIGHT_WING) {
            if (assist1 == Position.LEFT_WING) {

            } else if (assist1 == Position.CENTER) {

            } else if (assist1 == Position.LEFT_DEFENSE) {

            } else if (assist1 == Position.RIGHT_DEFENSE) {

            }
        } else if (scorer == Position.LEFT_DEFENSE) {
            if (assist1 == Position.LEFT_WING) {

            } else if (assist1 == Position.RIGHT_WING) {

            } else if (assist1 == Position.CENTER) {

            } else if (assist1 == Position.RIGHT_DEFENSE) {

            }
        } else if (scorer == Position.RIGHT_DEFENSE) {
            if (assist1 == Position.LEFT_WING) {

            } else if (assist1 == Position.RIGHT_WING) {

            } else if (assist1 == Position.LEFT_DEFENSE) {

            } else if (assist1 == Position.CENTER) {

            }
        }
    }

    public void lineScoredOn() {
        center.scoredAgainst();
        leftDe.scoredAgainst();
        rightDe.scoredAgainst();
        leftWing.scoredAgainst();
        rightWing.scoredAgainst();
    }
}
