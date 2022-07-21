/**
 * Defenseman
 * Class contains information and methods specific to a player with a defensive position.
 */

public class Defenseman extends Skater {
    private int shotsBlocked;

    public static final String DEFENSE_POS = "Position must be a defensive position";
    /**
     * Acts like basic Skater position and initializes variables with their respective arguments or to 0.
     * @throws IllegalArgumentException Thrown if the given position is not a defensive position
     */
    public Defenseman(String name, int playerNumber, String stickHand, Position side) throws IllegalArgumentException {
        super(name, playerNumber, stickHand, side);

        if (side != Position.Left_Defense && side != Position.Right_Defense) {
            throw new IllegalArgumentException(DEFENSE_POS);
        }
        shotsBlocked = 0;
    }

    /**
     * Initializes variables with the values of their respective arguments.
     * @throws IllegalArgumentException Thrown if given position is not a defensive position or if shotsBlocked argument
     * is negative
     */
    public Defenseman(String name, int playerNumber, String stickHand, Position side,
                      int goals, int assists, int plusMinus, int hits, double penaltyMinutes, int shotsBlocked)
            throws IllegalArgumentException {
        super(name, playerNumber, stickHand, side, goals, assists, plusMinus, hits, penaltyMinutes);

        if (side != Position.Left_Defense && side != Position.Right_Defense) {
            throw new IllegalArgumentException(DEFENSE_POS);
        }
        if (shotsBlocked < 0) {
            throw new IllegalArgumentException("Shots Blocked " + POS_STAT);
        } else {
            this.shotsBlocked = shotsBlocked;
        }
    }

    // Shallow Copy Constructor
    public Defenseman(Skater s) {
        super(s);
        if (s instanceof Defenseman d) {
            this.shotsBlocked = d.shotsBlocked;
        } else {
            shotsBlocked = 0;
        }
    }

    public int getShotsBlocked() {
        return shotsBlocked;
    }

    // Increments shots blocked up by 1
    public void blockedShot() {
        shotsBlocked++;
    }

    /**
     * Adds given argument to the defenseman's shot block total
     * @throws IllegalArgumentException If given argument is negative
     */
    public void addShotsBlocked(int shotsBlocked) throws IllegalArgumentException {
        if (shotsBlocked < 0) {
            throw new IllegalArgumentException("Shots Blocked " + POS_STAT);
        }
        this.shotsBlocked += shotsBlocked;
    }

    /**
     * @throws IllegalArgumentException Thrown if given argument is negative
     */
    public void setShotsBlocked(int shotsBlocked) throws IllegalArgumentException {
        if (shotsBlocked < 0) {
            throw new IllegalArgumentException("Shots Blocked " + POS_STAT);
        } else {
            this.shotsBlocked = shotsBlocked;
        }
    }

    // Resets stats to 0
    @Override
    public void resetStats() {
        super.resetStats();
        shotsBlocked = 0;
    }

    /**
     * @return A formatted String that contains all relevant information about the player
     */
    @Override
    public String statsDisplay() {
        return super.statsDisplay() +
                String.format("\nShots Blocked: %d", shotsBlocked);
    }
}
