import java.io.Serializable;

/**
 * Line
 * Class that serves as a baseline for any Line class. Only variable is name which will be built on in subclasses.
 */
public abstract class Line implements Serializable {
    private String name;

    // Error messages
    public static final String EMPTY_POSITIONS = "Positions cannot be left empty";
    public static final String PLAYER_DUPLICATES_ERROR = "The same player cannot be at two different positions.";
    public static final String POSITION_DUPLICATES_ERROR = "Given positions must be different";

    /**
     * Initializes instance variables with their respective arguments. A player can be left null if the user so chooses.
     * @throws NullPointerException Thrown if the given name is null
     * @throws IllegalArgumentException Thrown if the given name is blank
     */
    public Line(String name) throws NullPointerException, IllegalArgumentException {
        if (name == null) {
            throw new NullPointerException(Player.NULL_ERROR);
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException(Player.NAME_BLANK);
        } else if (name.indexOf('|') >= 0) {
            throw new IllegalArgumentException(Player.SPECIAL_CHAR);
        }
        this.name = name;
    }

    // Getter Method

    public String getName() {
        return name;
    }

    /**
     * Setter method for name
     * @throws NullPointerException Thrown if the given name is null
     * @throws IllegalArgumentException Thrown if the given name is blank
     */
    public void setName(String name) throws NullPointerException, IllegalArgumentException {
        if (name == null) {
            throw new NullPointerException(Player.NULL_ERROR);
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException(Player.NAME_BLANK);
        } else if (name.indexOf('|') >= 0) {
            throw new IllegalArgumentException(Player.SPECIAL_CHAR);
        }
        this.name = name;
    }

    // Abstract methods

    public abstract void lineScoredOn();

    public abstract String lineRoster();

    public abstract Skater[] getSkaters();

    public abstract boolean contains(Skater s);

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

    // Helper Methods for Score Methods, Reduce Duplication

    public static void scoreWingers(Position scorer, Position assist, Skater offense1, Skater offense2) {
        if (scorer == Position.Left_Wing) {
            offense1.score();
        } else if (assist == Position.Left_Wing) {
            offense1.assist();
        } else {
            offense1.scoredOnIce();
        }

        if (scorer == Position.Right_Wing) {
            offense2.score();
        } else if (assist == Position.Right_Wing) {
            offense2.assist();
        } else {
            offense2.scoredOnIce();
        }
    }

    public static void scoreWingers(Position scorer, Position assist1, Position assist2, Skater offense1,
                                    Skater offense2) {
        if (scorer == Position.Left_Wing) {
            offense1.score();
        } else if (assist1 == Position.Left_Wing || assist2 == Position.Left_Wing) {
            offense1.assist();
        } else {
            offense1.scoredOnIce();
        }

        if (scorer == Position.Right_Wing) {
            offense2.score();
        } else if (assist1 == Position.Right_Wing || assist2 == Position.Right_Wing) {
            offense2.assist();
        } else {
            offense2.scoredOnIce();
        }
    }

    public static void scoreOffense(Position scorer, Center center, Skater leftWing, Skater rightWing) {
        if (scorer == Position.Center) {
            center.score();
        } else {
            center.scoredOnIce();
        }

        if (scorer == Position.Left_Wing) {
            leftWing.score();
        } else {
            leftWing.scoredOnIce();
        }

        if (scorer == Position.Right_Wing) {
            rightWing.score();
        } else {
            rightWing.scoredOnIce();
        }
    }

    public static void scoreOffense(Position scorer, Position assist, Center center, Skater leftWing,
                                    Skater rightWing) {
        if (scorer == Position.Center) {
            center.score();
        } else if (assist == Position.Center) {
            center.assist();
        } else {
            center.scoredOnIce();
        }

        PKLine.scoreWingers(scorer, assist, leftWing, rightWing);
    }

    public static void scoreOffense(Position scorer, Position assist1, Position assist2, Center center, Skater leftWing,
                                    Skater rightWing) {
        if (scorer == Position.Center) {
            center.score();
        } else if (assist1 == Position.Center || assist2 == Position.Center) {
            center.assist();
        } else {
            center.scoredOnIce();
        }

        PKLine.scoreWingers(scorer, assist1, assist2, leftWing, rightWing);
    }

    public static void scoreDefense(Position scorer, Defenseman rightDe, Defenseman leftDe) {
        if (scorer == Position.Left_Defense) {
            leftDe.score();
        } else {
            leftDe.scoredOnIce();
        }
        if (scorer == Position.Right_Defense) {
            rightDe.score();
        } else {
            rightDe.scoredOnIce();
        }
    }

    public static void scoreDefense(Position scorer, Position assist, Defenseman rightDe, Defenseman leftDe) {
        if (scorer == Position.Right_Defense) {
            rightDe.score();
        } else if (assist == Position.Right_Defense) {
            rightDe.assist();
        } else {
            rightDe.scoredOnIce();
        }

        if (scorer == Position.Left_Defense) {
            leftDe.score();
        } else if (assist == Position.Left_Defense) {
            leftDe.assist();
        } else {
            leftDe.scoredOnIce();
        }
    }

    public static void scoreDefense(Position scorer, Position assist1, Position assist2, Defenseman rightDe,
                                    Defenseman leftDe) {
        if (scorer == Position.Right_Defense) {
            rightDe.score();
        } else if (assist1 == Position.Right_Defense || assist2 == Position.Right_Defense) {
            rightDe.assist();
        } else {
            rightDe.scoredOnIce();
        }

        if (scorer == Position.Left_Defense) {
            leftDe.score();
        } else if (assist1 == Position.Left_Defense || assist2 == Position.Left_Defense) {
            leftDe.assist();
        } else {
            leftDe.scoredOnIce();
        }
    }

    // toString Method
    @Override
    public String toString() {
        return name;
    }
}
