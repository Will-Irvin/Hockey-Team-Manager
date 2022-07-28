/**
 * Skater
 * Class that stores information for a basic skater/player on a team along with methods to manipulate and retrieve said
 * information.
 */

public class Skater extends Player {
    private int goals;
    private int assists;
    private int plusMinus;
    private int hits;
    private double penaltyMinutes;
    private String stickHand;
    private Position position;

    public static final String STICK_LEFT = "Left";
    public static final String STICK_RIGHT = "Right";
    public static final String STICK_HAND_ERROR = "Stick Hand must be Left or Right";
    public static final String POS_STAT = "stat cannot be negative";

    /**
     * Sets given arguments to their respective instance variables. Initializes stats such as goals/assists to 0.
     * @throws NullPointerException If a given object argument is null
     * @throws IllegalArgumentException If a given argument is blank or if the player number is invalid
     */
    public Skater(String name, int playerNumber, String stickHand, Position position)
            throws NullPointerException, IllegalArgumentException{
        super(name, playerNumber);
        if (stickHand == null) {
            throw new IllegalArgumentException(NULL_ERROR);
        } else if (!(stickHand.equals(STICK_LEFT)) && !(stickHand.equals(STICK_RIGHT))) {
            throw new IllegalArgumentException(STICK_HAND_ERROR);
        } else {
            this.stickHand = stickHand;
        }
        if (position == null) {
            throw new NullPointerException(NULL_ERROR);
        }
        this.position = position;
        goals = 0;
        assists = 0;
        plusMinus = 0;
        hits = 0;
        penaltyMinutes = 0;
    }

    /**
     * Sets given arguments to their respective instance variables.
     * @throws NullPointerException If a given object is null.
     * @throws IllegalArgumentException If a given argument is invalid, if the player number is not within the
     * specified range, or if the given stats are negative.
     */
    public Skater(String name, int playerNumber, String stickHand, Position position,
                  int goals, int assists, int plusMinus, int hits, double penaltyMinutes)
            throws NullPointerException, IllegalArgumentException {
        super(name, playerNumber);
        if (stickHand == null) {
            throw new IllegalArgumentException(NULL_ERROR);
        } else if (!(stickHand.equals(STICK_LEFT)) && !(stickHand.equals(STICK_RIGHT))) {
            throw new IllegalArgumentException(STICK_HAND_ERROR);
        } else {
            this.stickHand = stickHand;
        }
        this.position = position;
        if (goals < 0) {
            throw new IllegalArgumentException("Goals " + POS_STAT);
        }
        if (assists < 0) {
            throw new IllegalArgumentException("Assists " + POS_STAT);
        }
        if (hits < 0) {
            throw new IllegalArgumentException("Hits " + POS_STAT);
        }
        if (penaltyMinutes < 0) {
            throw new IllegalArgumentException("Penalty Minutes " + POS_STAT);
        }
        this.goals = goals;
        this.assists = assists;
        this.plusMinus = plusMinus;
        this.hits = hits;
        this.penaltyMinutes = penaltyMinutes;
    }

    // Shallow copy constructor
    public Skater(Skater s) {
        super(s.getName(), s.getPlayerNumber());
        this.stickHand = s.stickHand;
        this.position = s.position;
        this.goals = s.goals;
        this.assists = s.assists;
        this.plusMinus = s.plusMinus;
        this.hits = s.hits;
        this.penaltyMinutes = s.penaltyMinutes;
    }

    public int getGoals() {
        return goals;
    }

    public int getAssists() {
        return assists;
    }

    public int getPoints() {
        return goals + assists;
    }

    public int getPlusMinus() {
        return plusMinus;
    }

    public String getStickHand() {
        return stickHand;
    }

    public Position getPosition() {
        return position;
    }

    public int getHits() {
        return hits;
    }

    public double getPenaltyMinutes() {
        return penaltyMinutes;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * @throws IllegalArgumentException If the given stat is negative
     */
    public void setGoals(int goals) throws IllegalArgumentException {
        if (goals < 0) {
            throw new IllegalArgumentException("Goals " + POS_STAT);
        } else {
            this.goals = goals;
        }
    }

    /**
     * @throws IllegalArgumentException If the given stat is negative
     */
    public void setAssists(int assists) throws IllegalArgumentException {
        if (assists < 0) {
            throw new IllegalArgumentException("Assists " + POS_STAT);
        } else {
            this.assists = assists;
        }
    }

    public void setPlusMinus(int plusMinus) {
        this.plusMinus = plusMinus;
    }

    /**
     * Adds given argument to the skater's hit total
     * @throws IllegalArgumentException If given argument is negative
     */
    public void addHits(int hits) throws IllegalArgumentException {
        if (hits < 0) {
            throw new IllegalArgumentException("Hits " + POS_STAT);
        }
        this.hits += hits;
    }

    /**
     * @throws IllegalArgumentException If the given argument is negative
     */
    public void setHits(int hits) throws IllegalArgumentException {
        if (hits < 0) {
            throw new IllegalArgumentException("Hits stat cannot be negative");
        }
        this.hits = hits;
    }

    /**
     * @throws IllegalArgumentException If given argument is negative
     */
    public void setPenaltyMinutes(double penaltyMinutes) throws IllegalArgumentException {
        if (penaltyMinutes < 0) {
            throw new IllegalArgumentException("Penalty Minutes " + POS_STAT);
        }
        this.penaltyMinutes = penaltyMinutes;
    }

    /**
     * @throws NullPointerException If the given argument is null
     * @throws IllegalArgumentException If the given argument is not left or right
     */
    public void setStickHand(String stickHand) throws NullPointerException, IllegalArgumentException {
        if (stickHand == null) {
            throw new NullPointerException(NULL_ERROR);
        } else if (!stickHand.equals(STICK_LEFT) && !stickHand.equals(STICK_RIGHT)) {
            throw new NullPointerException(STICK_HAND_ERROR);
        }
        this.stickHand = stickHand;
    }

    // Increments goals and plusMinus up by 1
    public void score() {
        goals++;
        plusMinus++;
    }

    // Increments assists and plusMinus up by 1
    public void assist() {
        assists++;
        plusMinus++;
    }

    // Subtracts 1 from plusMinus
    public void scoredAgainst() {
        plusMinus--;
    }

    // Increments hit up by 1
    public void hit() {
        hits++;
    }

    // Adds given number to penalty minutes
    public void penalty(double minutes) throws IllegalArgumentException {
        if (minutes < 0) {
            throw new IllegalArgumentException("Penalty length cannot be negative");
        }
        penaltyMinutes += minutes;
    }

    // Increments plusMinus up by 1
    public void scoredOnIce() {
        plusMinus++;
    }

    /**
     * Resets the players stats to 0 (for something like a new season)
     */
    @Override
    public void resetStats() {
        goals = 0;
        assists = 0;
        plusMinus = 0;
        hits = 0;
        penaltyMinutes = 0;
    }

    /**
     * @return A formatted String that displays all valid information on the player
     */
    @Override
    public String statsDisplay() {
        String result = String.format("%s #%d\n", getName(), getPlayerNumber());
        switch (this.position) {
            case Center -> result += "Center\n";
            case Left_Wing -> result += "Left Wing\n";
            case Right_Wing -> result += "Right Wing\n";
            case Left_Defense -> result += "Left Defense\n";
            case Right_Defense -> result += "Right Defense\n";
        }
        result += String.format("""
                            Stick Hand: %s
                            Goals: %d
                            Assists: %d
                            Points: %d
                            +/-: %d
                            Hits: %d
                            Penalty Minutes: %.1f""",
                stickHand.charAt(0), goals, assists, getPoints(), plusMinus, hits, penaltyMinutes);
        return result;
    }

    /**
     * @return An object array where each element is one of the key stats of the skater
     */
    @Override
    public Object[] getStatsArray() {
        Object[] result = new Object[]{getName(), getPlayerNumber(), "", goals, assists, getPoints(), plusMinus};
        switch (position) {
            case Center -> result[2] = "C";
            case Left_Wing -> result[2] = "LW";
            case Right_Wing -> result[2] = "RW";
            case Right_Defense -> result[2] = "RD";
            case Left_Defense -> result[2] = "LD";
        }
        return result;
    }
}
