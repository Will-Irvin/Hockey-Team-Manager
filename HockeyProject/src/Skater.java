public class Skater {
    private String name;
    private int playerNumber;
    private int goals;
    private int assists;
    private int plusMinus;
    private String stickHand;

    public Skater(String name, int playerNumber, String stickHand) {
        this.name = name;
        this.playerNumber = playerNumber;
        this.stickHand = stickHand;
        goals = 0;
        assists = 0;
        plusMinus = 0;
    }

    public Skater(String name, int playerNumber, String stickHand, int goals, int assists, int plusMinus) {
        this.name = name;
        this.playerNumber = playerNumber;
        this.stickHand = stickHand;
        this.goals = goals;
        this.assists = assists;
        this.plusMinus = plusMinus;
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

    public String getStickHand() {
        return stickHand;
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

    public String toString() {
        return String.format("%s #%d, Stick: %s: \n" +
                "    Goals: %d\n" +
                "    Assists: %d\n" +
                "    +/-: %d\n",
                name, playerNumber, stickHand, goals, assists, plusMinus);
    }
}
