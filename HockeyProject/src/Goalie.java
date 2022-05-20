public class Goalie {
    private final int playerNumber;
    private final String name;
    private int shotsBlocked;
    private int shotsAgainst;
    private int wins;
    private int losses;
    private int otLosses;

    public Goalie(int playerNumber, String name) {
        if (playerNumber < 0 || playerNumber > 99) {
            throw new IllegalArgumentException("Player Number must be between 0-99");
        } else {
            this.playerNumber = playerNumber;
        }
        if (name == null) {
            throw new NullPointerException("Name cannot be null");
        } else if (name.length() == 0) {
            throw new IllegalArgumentException("Name must have length greater than 0");
        } else {
            this.name = name;
        }
        wins = 0;
        shotsBlocked = 0;
        shotsAgainst = 0;
        losses = 0;
        otLosses = 0;
    }

    public Goalie(int playerNumber, String name, double savePercentage,
                  int shotsAgainst, int wins, int losses, int otLosses) {
        if (playerNumber < 0 || playerNumber > 99) {
            throw new IllegalArgumentException("Player Number must be between 0-99");
        } else {
            this.playerNumber = playerNumber;
        }
        if (name == null) {
            throw new NullPointerException("Name cannot be null");
        } else if (name.length() == 0) {
            throw new IllegalArgumentException("Name must have length greater than 0");
        } else {
            this.name = name;
        }
        if (savePercentage < 0 || savePercentage > 100) {
            throw new IllegalArgumentException("Save % Must be between 0 and 100");
        }
        if (shotsAgainst < 0) {
            throw new IllegalArgumentException("Shots Against stat must be positive");
        }
        shotsBlocked = (int) ((savePercentage/100) * shotsAgainst);
        this.shotsAgainst = shotsAgainst;
        if (wins < 0) {
            throw new IllegalArgumentException("Wins stat must be positive");
        }
        this.wins = wins;
        if (losses < 0) {
            throw new IllegalArgumentException("Losses stat must be positive");
        }
        this.losses = losses;
        if (otLosses < 0) {
            throw new IllegalArgumentException("OT Losses stat must be positive");
        }
        this.otLosses = otLosses;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public String getName() {
        return name;
    }

    public double getSavePercent() {
        if (shotsAgainst == 0) {
            return 0;
        }
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

    public void setWins(int wins) {
        if (wins < 0) {
            System.out.println("Wins stat must be positive");
        } else {
            this.wins = wins;
        }
    }

    public void setLosses(int losses) {
        if (losses < 0) {
            System.out.println("Losses stat must be positive");
        } else {
            this.losses = losses;
        }
    }

    public void setOtLosses(int otLosses) {
        if (otLosses < 0) {
            System.out.println("OT Losses stat must be positive");
        } else {
            this.otLosses = otLosses;
        }
    }

    public void setSavePercentage(double savePercentage, int shotsAgainst) {
        if (savePercentage < 0 || savePercentage > 100) {
            System.out.println("Save Percentage must be between 0-100");
        } else if (shotsAgainst < 0) {
            System.out.println("Shots against stat must be positive");
        } else {
            shotsBlocked = (int) ((savePercentage/100) * shotsAgainst);
        }
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

    public boolean equals(Object o) {
        if (o instanceof Goalie g) {
            return g.getPlayerNumber() == this.getPlayerNumber();
        }
        return false;
    }

    public void enterSaves(int numgoals, int numshots) {
        if (numgoals < 0 || numshots < 0) {
            throw new IllegalArgumentException("Entered values cannot be negative");
        }
        if (numgoals > numshots) {
            throw new IllegalArgumentException("Cannot have more goals scored than shots taken");
        }
        shotsBlocked += numshots - numgoals;
        shotsAgainst += numshots;
    }

    public String toString() {
        return String.format("%s #%d\n" +
                "    Record: %d-%d-%d\n" +
                "    Save Percentage: %.2f",
                name, playerNumber, wins, losses, otLosses, getSavePercent());
    }
}
