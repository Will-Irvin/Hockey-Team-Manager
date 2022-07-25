import java.io.Serializable;

/**
 * Player
 *
 * A class containing basic info for a player on a hockey team such as their number and name.
 */
public abstract class Player implements Serializable {
    private String name;
    private int playerNumber;

    public static final int PLAYER_NUM_MAX = 99;
    public static final String NULL_ERROR = "Given arguments cannot be null";
    public static final String NAME_BLANK = "Name cannot be blank";
    public static final String PLAYER_NUMBER_RANGE = "Player number must be between 1-99";
    public static final String SPECIAL_CHAR = "Names cannot contain '|' character";

    public Player(String name, int playerNumber) throws IllegalArgumentException, NullPointerException {
        if (name == null) {
            throw new NullPointerException(NULL_ERROR);
        } else if (name.isBlank()) {
            throw new IllegalArgumentException(NAME_BLANK);
        } else if (name.indexOf('|') >= 0) {
            throw new IllegalArgumentException(SPECIAL_CHAR);
        }
        this.name = name;
        if (playerNumber < 1 || playerNumber > PLAYER_NUM_MAX) {
            throw new IllegalArgumentException(PLAYER_NUMBER_RANGE);
        }
        this.playerNumber = playerNumber;
    }

    // Getter Methods

    public String getName() {
        return name;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    /**
     * @throws NullPointerException If the given name is null
     * @throws IllegalArgumentException If the given name is blank
     */
    public void setName(String name) throws NullPointerException, IllegalArgumentException {
        if (name == null) {
            throw new NullPointerException(NULL_ERROR);
        } else if (name.isBlank()) {
            throw new IllegalArgumentException(NAME_BLANK);
        } else if (name.indexOf('|') >= 0) {
            throw new IllegalArgumentException(SPECIAL_CHAR);
        } else {
            this.name = name;
        }
    }

    /**
     * @throws IllegalArgumentException If the given player number is not in range 1-99
     */
    public void setPlayerNumber(int playerNumber) throws IllegalArgumentException {
        if (playerNumber < 1 || playerNumber > 99) {
            throw new IllegalArgumentException(PLAYER_NUMBER_RANGE);
        }
        this.playerNumber = playerNumber;
    }

    /**
     * Returns true if the given object is a player, and the two players have the same player number
     * @param o Given object to compare
     * @return True if the skaters have the same player number
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Player p) {
            return p.getPlayerNumber() == this.playerNumber;
        }
        return false;
    }

    // toString Method
    @Override
    public String toString() {
        return String.format("%s %d", name, playerNumber);
    }

    public abstract String statsDisplay();

    public abstract Object[] getStatsArray();

    public abstract void resetStats();
}
