public class Goalie {
    private int playerNumber;
    private String name;
    private int shotsBlocked;
    private int shotsAgainst;
    private int wins;
    private int losses;
    private int otLosses;
    private int shutouts;

    public Goalie(int playerNumber, String name) {
        if (playerNumber < 0 || playerNumber > 99) {
            throw new IllegalArgumentException("Player Number must be between 0-99");
        } else {
            this.playerNumber = playerNumber;
        }
        if (name == null) {
            throw new NullPointerException("Name cannot be null");
        } else if (name.isBlank()) {
            throw new IllegalArgumentException("Name must have length greater than 0");
        } else {
            this.name = name;
        }
        wins = 0;
        shotsBlocked = 0;
        shotsAgainst = 0;
        losses = 0;
        otLosses = 0;
        shutouts = 0;
    }

    public Goalie(int playerNumber, String name, double savePercentage,
                  int shotsAgainst, int wins, int losses, int otLosses, int shutouts) {
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
        if (savePercentage < 0 || savePercentage > 1) {
            throw new IllegalArgumentException("Save % Must be between 0 and 1");
        }
        if (shotsAgainst < 0) {
            throw new IllegalArgumentException("Shots Against stat must be positive");
        }
        shotsBlocked = (int) (savePercentage * shotsAgainst);
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
        if (shutouts < 0) {
            throw new IllegalArgumentException("Shutouts stat must be positive");
        } else if (shutouts > wins) {
            throw new IllegalArgumentException("Cannot have more shutouts than wins");
        }
        this.shutouts = shutouts;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public String getName() {
        return name;
    }

    public void setPlayerNumber(int playerNumber) throws IllegalArgumentException {
        if (playerNumber < 0 || playerNumber > 99) {
            throw new IllegalArgumentException("Player Number must be between 0-99");
        }
        this.playerNumber = playerNumber;
    }

    public void setName(String name) throws NullPointerException, IllegalArgumentException {
        if (name == null) {
            throw new NullPointerException("Name cannot be null");
        } else if (name.isBlank()) {
            throw new IllegalArgumentException("Name must have length greater than 0");
        }
        this.name = name;
    }

    public double getSavePercent() {
        if (shotsAgainst == 0) {
            return 0;
        }
        return ((double) shotsBlocked/shotsAgainst);
    }

    public double getGAA() {
        if (wins + losses + otLosses == 0) {
            return -1;
        }
        return (double) (shotsAgainst - shotsBlocked) / (wins + losses + otLosses);
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

    public int getShutouts() {
        return shutouts;
    }

    public void setShutouts(int shutouts) throws IllegalArgumentException {
        if (shutouts < 0) {
            throw new IllegalArgumentException("Shutouts stat must be positive");
        } else if (shutouts > wins) {
            throw new IllegalArgumentException("Cannot have more shutouts than wins");
        }
        this.shutouts = shutouts;
    }

    public void setWins(int wins) throws IllegalArgumentException {
        if (wins < 0) {
            throw new IllegalArgumentException("Wins stat must be positive");
        } else if (shutouts > wins) {
            throw new IllegalArgumentException("Cannot have more shutouts than wins");
        }
        this.wins = wins;
    }

    public void setLosses(int losses) throws IllegalArgumentException {
        if (losses < 0) {
            throw new IllegalArgumentException("Losses stat must be positive");
        }
        this.losses = losses;
    }

    public void setOtLosses(int otLosses) throws IllegalArgumentException {
        if (otLosses < 0) {
            throw new IllegalArgumentException("OT Losses stat must be positive");
        }
        this.otLosses = otLosses;
    }

    public void setSavePercentage(double savePercentage, int shotsAgainst) throws IllegalArgumentException {
        if (savePercentage < 0 || savePercentage > 1) {
            throw new IllegalArgumentException("Save Percentage must be between 0-1");
        } else if (shotsAgainst < 0) {
            throw new IllegalArgumentException("Shots against stat must be positive");
        }
        shotsBlocked = (int) ((savePercentage) * shotsAgainst);
        this.shotsAgainst = shotsAgainst;
    }

    public void win() {
        wins++;
    }

    public void shutoutWin() {
        wins++;
        shutouts++;
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
                "    Shutouts: %d\n" +
                "    Goals Against Average: %.3f\n" +
                "    Save Percentage: %.2f",
                name, playerNumber, wins, losses, otLosses, shutouts, getGAA(), getSavePercent());
    }
}
