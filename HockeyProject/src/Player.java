public class Player {
    private String name;
    private int playerNumber;
    private int goals;
    private int assists;
    private int plusMinus;

    public Player(String name, int playerNumber) {
        this.name = name;
        this.playerNumber = playerNumber;
        goals = 0;
        assists = 0;
        plusMinus = 0;
    }

    public String getName() {
        return name;
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

    public int getPlusMinus() {
        return plusMinus;
    }

    public void score() {
        goals++;
        plusMinus++;
    }

    public void assist() {
        assists++;
        plusMinus++;
    }

    public void scoredAgainst() {
        plusMinus--;
    }

    public void scoredOnIce() {
        plusMinus++;
    }
}
