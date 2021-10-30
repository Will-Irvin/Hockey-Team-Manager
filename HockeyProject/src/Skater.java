public class Skater {
    private final String name;
    private final int playerNumber;
    private int goals;
    private int assists;
    private int plusMinus;
    private final String stickHand;
    private String position;

    public Skater(String name, int playerNumber, String stickHand, String position) {
        if (name == null) {
            throw new NullPointerException("Name cannot be null");
        } else if (name.length() == 0) {
            throw new IllegalArgumentException("Name must have length greater than 0");
        } else {
            this.name = name;
        }
        if (playerNumber < 1 || playerNumber > 99) {
            throw new IllegalArgumentException("Player number must be between 1-99");
        } else {
            this.playerNumber = playerNumber;
        }
        if (stickHand == null) {
            throw new IllegalArgumentException("Stick Hand cannot be null");
        } else if (!(stickHand.equals("Left")) && !(stickHand.equals("Right"))) {
            throw new IllegalArgumentException("Stick Hand must be Left or Right");
        } else {
            this.stickHand = stickHand;
        }
        if (position == null) {
            throw new NullPointerException("Position cannot be null");
        }
        this.position = position;
        goals = 0;
        assists = 0;
        plusMinus = 0;
    }

    public Skater(String name, int playerNumber, String stickHand, String position, int goals, int assists, int plusMinus) {
        if (name == null) {
            throw new NullPointerException("Name cannot be null");
        } else if (name.length() == 0) {
            throw new IllegalArgumentException("Name must have length greater than 0");
        } else {
            this.name = name;
        }
        if (playerNumber < 1 || playerNumber > 99) {
            throw new IllegalArgumentException("Player number must be between 1-99");
        } else {
            this.playerNumber = playerNumber;
        }
        if (stickHand == null) {
            throw new IllegalArgumentException("Stick Hand cannot be null");
        } else if (!(stickHand.equals("Left")) && !(stickHand.equals("Right"))) {
            throw new IllegalArgumentException("Stick Hand must be Left or Right");
        } else {
            this.stickHand = stickHand;
        }
        if (position == null) {
            throw new NullPointerException("Position cannot be null");
        }
        this.position = position;
        if (goals < 0) {
            throw new IllegalArgumentException("Goals stat must be positive");
        } else {
            this.goals = goals;
        }
        if (assists < 0) {
            throw new IllegalArgumentException("Assists stat must be positive");
        }
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

    public String getPosition() {
        return position;
    }

    public void setGoals(int goals) {
        if (goals < 0) {
            System.out.println("Goals stat must be positive");
        } else {
            this.goals = goals;
        }
    }

    public void setAssists(int assists) {
        if (goals < 0) {
            System.out.println("Assists stat must be positive");
        } else {
            this.assists = assists;
        }
    }

    public void setPlusMinus(int plusMinus) {
        this.plusMinus = plusMinus;
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
        return String.format("%s #%d\n" +
                        "    Position: %s\n" +
                        "    Stick Hand: %s \n" +
                        "    Goals: %d\n" +
                        "    Assists: %d\n" +
                        "    +/-: %d",
                name, playerNumber, position, stickHand.charAt(0), goals, assists, plusMinus);
    }
}
