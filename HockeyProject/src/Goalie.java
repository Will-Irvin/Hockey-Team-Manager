public class Goalie {
    private int playerNumber;
    private String name;
    private double savePercent;
    private int wins;
    private int losses;
    private int otLosses;

    public Goalie(int playerNumber, String name) {
        this.playerNumber = playerNumber;
        this.name = name;
        wins = 0;
        savePercent = 0;
        losses = 0;
        otLosses = 0;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public String getName() {
        return name;
    }

    public double getSavePercent() {
        return savePercent;
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
}
