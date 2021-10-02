public class Defenseman extends Player{
    private int shotsBlocked;

    public Defenseman(String name, int playerNumber) {
        super(name, playerNumber);
        shotsBlocked = 0;
    }

    public int getShotsBlocked() {
        return shotsBlocked;
    }

    public void blockedShot() {
        shotsBlocked++;
    }
}
