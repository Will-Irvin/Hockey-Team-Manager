public class Goalie {
    private int playerNumber;
    private String name;
    private int shotsBlocked;
    private int shotsAgainst;
    private int wins;
    private int losses;
    private int otLosses;

    public Goalie(int playerNumber, String name) {
        this.playerNumber = playerNumber;
        this.name = name;
        wins = 0;
        shotsBlocked = 0;
        shotsAgainst = 0;
        losses = 0;
        otLosses = 0;
    }

    public Goalie(int playerNumber, String name, double savePercentage,
                  int shotsAgainst, int wins, int losses, int otLosses) {
        this.playerNumber = playerNumber;
        this.name = name;
        shotsBlocked = (int) ((savePercentage/100) * shotsAgainst);
        this.shotsAgainst = shotsAgainst;
        this.wins = wins;
        this.losses = losses;
        this.otLosses = otLosses;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public String getName() {
        return name;
    }

    public double getSavePercent() {
        return ((double) shotsBlocked/shotsAgainst) * 100;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getOtLosses() {
        return otLosses;
    }

    public void win() {
        wins++;
    }

    public void lose() {
        losses++;
    }

    public void loseOT() {
        otLosses++;
    }

    public void save() {
        shotsAgainst++;
        shotsBlocked++;
    }

    public void scoredOn() {
        shotsAgainst++;
    }

    public String toString() {
        return String.format("%s #%d\n" +
                "    Record: %d-%d-%d\n" +
                "    Save Percentage: %.2f\n",
                name, playerNumber, wins, losses, otLosses, getSavePercent());
    }
}
