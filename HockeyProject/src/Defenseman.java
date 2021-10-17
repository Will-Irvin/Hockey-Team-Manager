public class Defenseman extends Skater{
    private int shotsBlocked;

    public Defenseman(String name, int playerNumber, String stickHand) {
        super(name, playerNumber, stickHand);
        shotsBlocked = 0;
    }

    public Defenseman(String name, int playerNumber, String stickHand,
                      int goals, int assists, int plusMinus, int shotsBlocked) {
        super(name, playerNumber, stickHand, goals, assists, plusMinus);
        this.shotsBlocked = shotsBlocked;
    }

    public int getShotsBlocked() {
        return shotsBlocked;
    }

    public void blockedShot() {
        shotsBlocked++;
    }

    public String toString() {
        return super.toString() +
                String.format("    Shots Blocked: %d\n", shotsBlocked);
    }
}
