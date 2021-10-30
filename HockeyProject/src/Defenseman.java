public class Defenseman extends Skater {
    private int shotsBlocked;

    public Defenseman(String name, int playerNumber, String stickHand) {
        super(name, playerNumber, stickHand, "Defense");
        shotsBlocked = 0;
    }

    public Defenseman(String name, int playerNumber, String stickHand,
                      int goals, int assists, int plusMinus, int shotsBlocked) {
        super(name, playerNumber, stickHand, "Defense", goals, assists, plusMinus);
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

    public void setShotsBlocked(int shotsBlocked) {
        if (shotsBlocked < 0) {
            System.out.println("Shots Blocked stat must be positive");
        } else {
            shotsBlocked = shotsBlocked;
        }
    }

    public String toString() {
        return super.toString() +
                String.format("\n    Shots Blocked: %d", shotsBlocked);
    }
}
