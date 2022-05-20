public class Skater {
    private String name;
    private final int playerNumber;
    private int goals;
    private int assists;
    private int plusMinus;
    private final String stickHand;
    private final Position position;

    public Skater(String name, int playerNumber, String stickHand, Position position)
            throws NullPointerException, IllegalArgumentException{
        if (name == null) {
            throw new NullPointerException("Name cannot be null");
        } else if (name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
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

    public Skater(String name, int playerNumber, String stickHand, Position position,
                  int goals, int assists, int plusMinus) throws NullPointerException, IllegalArgumentException {
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

    public void setName() {
        if (name == null) {
            throw new NullPointerException("Name cannot be null");
        } else if (name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        } else {
            this.name = name;
        }
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

    public Position getPosition() {
        return position;
    }

    public void setGoals(int goals) throws IllegalArgumentException {
        if (goals < 0) {
            throw new IllegalArgumentException("Goals stat must be positive");
        } else {
            this.goals = goals;
        }
    }

    public void setAssists(int assists) {
        if (goals < 0) {
            throw new IllegalArgumentException("Assists stat must be positive");
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

    public boolean equals(Object o) {
        if (o instanceof Skater s) {
            return s.getPlayerNumber() == this.playerNumber;
        }
        return false;
    }

    public void scoredOnIce() {
        plusMinus++;
    }

    public String toString() {
        String result = String.format("%s #%d\n    ", name, playerNumber);
        switch (this.position) {
            case CENTER:
                result += "Center\n";
                break;
            case LEFT_WING:
                result += "Left Wing\n";
                break;
            case RIGHT_WING:
                result += "Right Wing\n";
                break;
            case LEFT_DEFENSE:
                result += "Left Defense";
                break;
            case RIGHT_DEFENSE:
                result += "Right Defense";
                break;
        }
        result += String.format("Stick Hand: %s \n" +
                        "    Goals: %d\n" +
                        "    Assists: %d\n" +
                        "    +/-: %d",
                stickHand.charAt(0), goals, assists, plusMinus);
        return result;
    }
}
