import java.io.Serializable;

/**
 * Skater
 * Class that stores information for a basic skater/player on a team along with methods to manipulate and retrieve said
 * information.
 */

public class Skater implements Serializable {
    private String name;
    private int playerNumber;
    private int goals;
    private int assists;
    private int plusMinus;
    private int hits;
    private double penaltyMinutes;
    private String stickHand;
    private final Position position;

    /**
     * Sets given arguments to their respective instance variables. Initializes stats such as goals/assists to 0.
     * @throws NullPointerException If a given object argument is null
     * @throws IllegalArgumentException If a given argument is blank or if the player number is invalid
     */
    public Skater(String name, int playerNumber, String stickHand, Position position)
            throws NullPointerException, IllegalArgumentException{
        if (name == null) {
            throw new NullPointerException("Name cannot be null");
        } else if (name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        } else {
            this.name = name;
        }
        if (playerNumber < 1 || playerNumber > 99) {
            throw new IllegalArgumentException("Player number must be between 1-99");
        } else {
            this.playerNumber = playerNumber;
        }
        if (stickHand == null) {
            throw new IllegalArgumentException("Stick Hand cannot be null");
        } else if (!(stickHand.equals("Left")) && !(stickHand.equals("Right"))) {
            throw new IllegalArgumentException("Stick Hand must be Left or Right");
        } else {
            this.stickHand = stickHand;
        }
        if (position == null) {
            throw new NullPointerException("Position cannot be null");
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
        if (name == null) {
            throw new NullPointerException("Name cannot be null");
        } else if (name.length() == 0) {
            throw new IllegalArgumentException("Name must have length greater than 0");
        } else {
            this.name = name;
        }
        if (playerNumber < 1 || playerNumber > 99) {
            throw new IllegalArgumentException("Player number must be between 1-99");
        } else {
            this.playerNumber = playerNumber;
        }
        if (stickHand == null) {
            throw new IllegalArgumentException("Stick Hand cannot be null");
        } else if (!(stickHand.equals("Left")) && !(stickHand.equals("Right"))) {
            throw new IllegalArgumentException("Stick Hand must be Left or Right");
        } else {
            this.stickHand = stickHand;
        }
        this.position = position;
        if (goals < 0) {
            throw new IllegalArgumentException("Goals stat must be positive");
        } else {
            this.goals = goals;
        }
        if (assists < 0) {
            throw new IllegalArgumentException("Assists stat must be positive");
        }
        if (hits < 0) {
            throw new IllegalArgumentException("Hits stat must be positive");
        }
        if (penaltyMinutes < 0) {
            throw new IllegalArgumentException("Penalty Minutes cannot be negative");
        }
        this.assists = assists;
        this.plusMinus = plusMinus;
        this.hits = hits;
        this.penaltyMinutes = penaltyMinutes;
    }

    public String getName() {
        return name;
    }

    /**
     * @throws NullPointerException If the given name is null
     * @throws IllegalArgumentException If the given name is blank
     */
    public void setName(String name) throws NullPointerException, IllegalArgumentException {
        if (name == null) {
            throw new NullPointerException("Name cannot be null");
        } else if (name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        } else {
            this.name = name;
        }
    }

    public int getPlayerNumber() {
        return playerNumber;
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

    /**
     * @throws IllegalArgumentException If the given player number is not in range 1-99
     */
    public void setPlayerNumber(int playerNumber) throws IllegalArgumentException {
        if (playerNumber < 1 || playerNumber > 99) {
            throw new IllegalArgumentException("Player number must be between 1-99");
        }
        this.playerNumber = playerNumber;
    }

    /**
     * @throws IllegalArgumentException If the given stat is negative
     */
    public void setGoals(int goals) throws IllegalArgumentException {
        if (goals < 0) {
            throw new IllegalArgumentException("Goals stat must be positive");
        } else {
            this.goals = goals;
        }
    }

    /**
     * @throws IllegalArgumentException If the given stat is negative
     */
    public void setAssists(int assists) throws IllegalArgumentException {
        if (goals < 0) {
            throw new IllegalArgumentException("Assists stat must be positive");
        } else {
            this.assists = assists;
        }
    }

    public void setPlusMinus(int plusMinus) {
        this.plusMinus = plusMinus;
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
            throw new IllegalArgumentException("Penalty Minutes cannot be negative");
        }
        this.penaltyMinutes = penaltyMinutes;
    }

    /**
     * @throws NullPointerException If the given argument is null
     * @throws IllegalArgumentException If the given argument is not left or right
     */
    public void setStickHand(String stickHand) throws NullPointerException, IllegalArgumentException {
        if (stickHand == null) {
            throw new NullPointerException("Stick hand cannot be null");
        } else if (!stickHand.equals("Left") && !stickHand.equals("Right")) {
            throw new NullPointerException("Stick hand must be Left or Right");
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

    /**
     * Returns true if the given object is a skater, and the two skaters have the same player number
     * @param o Given object to compare
     * @return True if the skaters have the same player number
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Skater s) {
            return s.getPlayerNumber() == this.playerNumber;
        }
        return false;
    }

    // Increments plusMinus up by 1
    public void scoredOnIce() {
        plusMinus++;
    }

    /**
     * Resets the players stats to 0 (for something like a new season)
     */
    public void resetStats() {
        goals = 0;
        assists = 0;
        plusMinus = 0;
    }

    /**
     * @return A formatted String that displays all valid information on the player
     */
    public String statsDisplay() {
        String result = String.format("%s #%d\n    ", name, playerNumber);
        switch (this.position) {
            case CENTER -> result += "Center\n";
            case LEFT_WING -> result += "Left Wing\n";
            case RIGHT_WING -> result += "Right Wing\n";
            case LEFT_DEFENSE -> result += "Left Defense\n";
            case RIGHT_DEFENSE -> result += "Right Defense\n";
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

    // toString Method
    @Override
    public String toString() {
        return String.format("%s %d", name, playerNumber);
    }

}
