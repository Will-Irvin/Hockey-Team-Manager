import java.io.Serializable;

/**
 * Player
 *
 * A class containing basic info for a player on a hockey team such as their number and name.
 */
public abstract class Player implements Serializable {
    private String name;
    private int playerNumber;

    public Player(String name, int playerNumber) throws IllegalArgumentException, NullPointerException {
        if (name == null) {
            throw new NullPointerException("Name cannot be null");
        } else if (name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        this.name = name;
        if (playerNumber < 1 || playerNumber > 99) {
            throw new IllegalArgumentException("Player number must be between 1-99");
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
            throw new NullPointerException("Name cannot be null");
        } else if (name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        } else {
            this.name = name;
        }
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

    public abstract void resetStats();
}
