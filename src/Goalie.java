import java.security.spec.PSSParameterSpec;

/**
 * Goalie
 *
 * A class storing relevant stats for a goalie on a hockey team as well as methods to manipulate these stats.
 */
public class Goalie extends Player {
    private int shotsBlocked;
    private int shotsAgainst;
    private int wins;
    private int losses;
    private int otLosses;
    private int shutouts;

    public static final String SAVE_PER_RANGE = "Save % must be between 0 and 1";
    public static final String INVALID_SHUTOUT = "Cannot have more shutouts than wins";

    /**
     * Initializes instance variables to their respective arguments, initializes stats to 0
     * @throws IllegalArgumentException If player number is invalid or if name is blank
     * @throws NullPointerException If given name is null
     */
    public Goalie(String name, int playerNumber) throws IllegalArgumentException, NullPointerException {
        super(name, playerNumber);
        wins = 0;
        shotsBlocked = 0;
        shotsAgainst = 0;
        losses = 0;
        otLosses = 0;
        shutouts = 0;
    }

    /**
     * Initializes instance variables to their respective arguments
     * @throws IllegalArgumentException If name is blank, if given stats are negative, if save percentage is invalid, if
     * number of shutouts is greater than number of wins
     * @throws NullPointerException If given name is null
     */
    public Goalie(String name, int playerNumber, double savePercentage,
                  int shotsAgainst, int wins, int losses, int otLosses, int shutouts)
            throws IllegalArgumentException, NullPointerException {
        super(name, playerNumber);
        if (savePercentage < 0 || savePercentage > 1) {
            throw new IllegalArgumentException(SAVE_PER_RANGE);
        }
        if (shotsAgainst < 0) {
            throw new IllegalArgumentException("Shots Against " + Skater.POS_STAT);
        }
        shotsBlocked = (int) (savePercentage * shotsAgainst);
        this.shotsAgainst = shotsAgainst;
        if (wins < 0) {
            throw new IllegalArgumentException("Wins " + Skater.POS_STAT);
        }
        this.wins = wins;
        if (losses < 0) {
            throw new IllegalArgumentException("Losses " + Skater.POS_STAT);
        }
        this.losses = losses;
        if (otLosses < 0) {
            throw new IllegalArgumentException("OT Losses " + Skater.POS_STAT);
        }
        this.otLosses = otLosses;
        if (shutouts < 0) {
            throw new IllegalArgumentException("Shutouts " + Skater.POS_STAT);
        } else if (shutouts > wins) {
            throw new IllegalArgumentException(INVALID_SHUTOUT);
        }
        this.shutouts = shutouts;
    }

    // Shallow Copy Constructor
    public Goalie(Goalie goalie) {
        super(goalie.getName(), goalie.getPlayerNumber());
        this.wins = goalie.wins;
        this.losses = goalie.losses;
        this.otLosses = goalie.otLosses;
        this.shotsBlocked = goalie.shotsBlocked;
        this.shotsAgainst = goalie.shotsAgainst;
        this.shutouts = goalie.shutouts;
    }

    // Getter Methods

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

    public int getShotsAgainst() {
        return shotsAgainst;
    }

    /**
     * Calculates and returns the goalie's save percentage based on the shotsBlocked and shotsAgainst stat (returns 0 if
     * no shots have been entered)
     * @return Goalie's save percentage
     */
    public double getSavePercent() {
        if (shotsAgainst == 0) {
            return 0;
        }
        return (double) shotsBlocked / shotsAgainst;
    }

    /**
     * Calculates and returns the goalie's goals against average based on shotsBlocked and shotsAgainst stats as well as
     * number of games played based on wins, losses, and lossesOT (returns 0 if no games have been played)
     * @return Goalie's goals against average
     */
    public double getGAA() {
        if (wins + losses + otLosses == 0) {
            return 0;
        }
        return (double) (shotsAgainst - shotsBlocked) / (wins + losses + otLosses);
    }

    /**
     * @throws IllegalArgumentException If given argument is negative or larger than number of wins
     */
    public void setShutouts(int shutouts) throws IllegalArgumentException {
        if (shutouts < 0) {
            throw new IllegalArgumentException("Shutouts " + Skater.POS_STAT);
        } else if (shutouts > wins) {
            throw new IllegalArgumentException(INVALID_SHUTOUT);
        }
        this.shutouts = shutouts;
    }

    /**
     * @throws IllegalArgumentException If given argument is negative or smaller than number of shutouts
     */
    public void setWins(int wins) throws IllegalArgumentException {
        if (wins < 0) {
            throw new IllegalArgumentException("Wins " + Skater.POS_STAT);
        } else if (shutouts > wins) {
            throw new IllegalArgumentException(INVALID_SHUTOUT);
        }
        this.wins = wins;
    }

    /**
     * @throws IllegalArgumentException If given argument is negative
     */
    public void setLosses(int losses) throws IllegalArgumentException {
        if (losses < 0) {
            throw new IllegalArgumentException("Losses " + Skater.POS_STAT);
        }
        this.losses = losses;
    }

    /**
     * @throws IllegalArgumentException If given argument is negative
     */
    public void setOtLosses(int otLosses) throws IllegalArgumentException {
        if (otLosses < 0) {
            throw new IllegalArgumentException("OT Losses " + Skater.POS_STAT);
        }
        this.otLosses = otLosses;
    }

    /**
     * Sets shotsBlocked using given percentage
     * @param savePercentage Given percentage
     * @param shotsAgainst Updated shotsAgainst stat
     * @throws IllegalArgumentException If given percentage is not valid or if shotsAgainst is negative
     */
    public void setSavePercentage(double savePercentage, int shotsAgainst) throws IllegalArgumentException {
        if (savePercentage < 0 || savePercentage > 1) {
            throw new IllegalArgumentException(SAVE_PER_RANGE);
        } else if (shotsAgainst < 0) {
            throw new IllegalArgumentException("Shots against " + Skater.POS_STAT);
        }
        shotsBlocked = (int) ((savePercentage) * shotsAgainst);
        this.shotsAgainst = shotsAgainst;
    }

    // Increments wins up by 1
    public void win() {
        wins++;
    }

    // Increments both wins and shutouts up by 1
    public void shutoutWin() {
        wins++;
        shutouts++;
    }

    // Increments losses up by 1
    public void lose() {
        losses++;
    }

    // Increments ot losses up by 1
    public void loseOT() {
        otLosses++;
    }

    // Increments both shotsBlocked and shotsAgainst up by 1
    public void save() {
        shotsAgainst++;
        shotsBlocked++;
    }

    // Increments shotsAgainst up by 1
    public void scoredOn() {
        shotsAgainst++;
    }

    /**
     * Used to enter a goalie's stats (shotsBlocked and shotsAgainst) after a game
     * @param numGoals Number of goals scored on a goalie
     * @param numShots Number of shots the goalie faced
     * @throws IllegalArgumentException If given arguments are negative or if numGoals larger than numShots
     */
    public void enterSaves(int numGoals, int numShots) throws IllegalArgumentException {
        if (numGoals < 0 || numShots < 0) {
            throw new IllegalArgumentException("Goals and shots against " + Skater.POS_STAT);
        }
        if (numGoals > numShots) {
            throw new IllegalArgumentException("Cannot have more goals scored than shots taken");
        }
        shotsBlocked += numShots - numGoals;
        shotsAgainst += numShots;
    }

    @Override
    public void resetStats() {
        shotsAgainst = 0;
        shotsBlocked = 0;
        wins = 0;
        losses = 0;
        otLosses = 0;
        shutouts = 0;
    }

    /**
     * @return A formatted String containing the goalie's relevant stats
     */
    @Override
    public String statsDisplay() {
        return String.format("""
                        %s #%d
                            Record: %d-%d-%d
                            Shutouts: %d
                            Goals Against Average: %.2f
                            Save Percentage: %.3f""",
                getName(), getPlayerNumber(), wins, losses, otLosses, shutouts, getGAA(), getSavePercent());
    }

    @Override
    public Object[] getStatsArray() {
        return new Object[]{getName(), getPlayerNumber(), wins, losses, otLosses, String.format("%.2f", getGAA()),
                String.format("%.3f", getSavePercent())};
    }
}
