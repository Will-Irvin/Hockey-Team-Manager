public class Defenseman extends Skater {
    private int shotsBlocked;

    public Defenseman(String name, int playerNumber, String stickHand, Position side) throws IllegalArgumentException {
        super(name, playerNumber, stickHand, side);

        if (side != Position.LEFT_DEFENSE && side != Position.RIGHT_DEFENSE) {
            throw new IllegalArgumentException("Position must be a defensive position");
        }

        shotsBlocked = 0;
    }

    public Defenseman(String name, int playerNumber, String stickHand, Position side,
                      int goals, int assists, int plusMinus, int shotsBlocked) throws IllegalArgumentException {
        super(name, playerNumber, stickHand, side, goals, assists, plusMinus);

        if (side != Position.LEFT_DEFENSE && side != Position.RIGHT_DEFENSE) {
            throw new IllegalArgumentException("Position must be a defensive position");
        }
        if (shotsBlocked < 0) {
            throw new IllegalArgumentException("Shots Blocked stat must be positive");
        } else {
            this.shotsBlocked = shotsBlocked;
        }
    }

    public int getShotsBlocked() {
        return shotsBlocked;
    }

    public void blockedShot() {
        shotsBlocked++;
    }

    public void setShotsBlocked(int shotsBlocked) throws IllegalArgumentException {
        if (shotsBlocked < 0) {
            throw new IllegalArgumentException("Shots Blocked stat must be positive");
        } else {
            this.shotsBlocked = shotsBlocked;
        }
    }

    public String toString() {
        return super.toString() +
                String.format("\n    Shots Blocked: %d", shotsBlocked);
    }
}
