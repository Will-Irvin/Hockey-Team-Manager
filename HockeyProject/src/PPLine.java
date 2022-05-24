public class PPLine extends Line {
    private int numberScored;  // Successful power plays
    private int numberOpps;    // Total power play attempts (opportunities)

    /**
     * Initializes given arguments in their respective variables, sets stats to 0
     * @throws NullPointerException Thrown if any of the given players are null
     */
    public PPLine(String name, Center center, Skater leftWing, Skater rightWing, Defenseman leftDe, Defenseman rightDe)
            throws NullPointerException {
        super(name, center, leftWing, rightWing, leftDe, rightDe);
        if (center == null || leftWing == null || rightWing == null || leftDe == null || rightDe == null) {
            throw new NullPointerException("All positions must be filled for a Power Play line");
        }
        numberScored = 0;
        numberOpps = 0;
    }

    /**
     * Initializes given arguments in their respective variables
     * @throws NullPointerException Thrown if any of the given players are null
     * @throws IllegalArgumentException Thrown if the given stats are negative or if numberScored is greater than
     * numberOpps
     */
    public PPLine(String name, Center center, Skater leftWing, Skater rightWing, Defenseman leftDe, Defenseman rightDe,
                  int numberScored, int numberOpps) throws NullPointerException, IllegalArgumentException {
        super(name, center, leftWing, rightWing, leftDe, rightDe);
        if (center == null || leftWing == null || rightWing == null || leftDe == null || rightDe == null) {
            throw new NullPointerException("All positions must be filled for a Power Play line");
        }
        if (numberScored < 0 || numberOpps < 0) {
            throw new IllegalArgumentException("The given stats cannot be negative");
        }
        if (numberScored > numberOpps) {
            throw new IllegalArgumentException("The number of successes must be fewer than the number of attempts");
        }

        this.numberScored = numberScored;
        this.numberOpps = numberOpps;
    }

    /**
     * Works almost identically to previous constructor, but initializes numberScored using the given percentage
     * @throws NullPointerException Thrown if given players are null
     * @throws IllegalArgumentException Thrown if given percentage is invalid or if numberOpps is negative
     */
    public PPLine(String name, Center center, Skater leftWing, Skater rightWing, Defenseman leftDe, Defenseman rightDe,
                  double ppPercent, int numberOpps) throws NullPointerException, IllegalArgumentException {
        super(name, center, leftWing, rightWing, leftDe, rightDe);
        if (center == null || leftWing == null || rightWing == null || leftDe == null || rightDe == null) {
            throw new NullPointerException("All positions must be filled for a Power Play line");
        }
        if (ppPercent < 0 || ppPercent > 100) {
            throw new IllegalArgumentException("Percentage must be between 0-100");
        }
        if (numberOpps < 0) {
            throw new IllegalArgumentException("Number of opportunities cannot be negative");
        }
        numberScored = (int) ((ppPercent / 100) * numberOpps);
        this.numberOpps = numberOpps;
    }

    // Getter Methods

    public int getNumberScored() {
        return numberScored;
    }

    public int getNumberOpps() {
        return numberOpps;
    }

    /**
     * Utilize Line methods to apply stats to players, also updates numberScored and numberOpps
     * @param position Position of the player who scored the goal.
     * @throws IllegalArgumentException See super class
     */
    public void score(Position position) throws IllegalArgumentException {
        super.score(position);
        numberScored++;
        numberOpps++;
    }

    /**
     * Utilize Line methods to apply stats to players, also updates numberScored and numberOpps
     * @param score Position of the player who scored the goal
     * @param assist The position of the player who assisted the goal
     * @throws IllegalArgumentException See super class
     */
    public void score(Position score, Position assist) throws IllegalArgumentException {
        super.score(score, assist);
        numberScored++;
        numberOpps++;
    }

    /**
     * Utilize Line methods to apply stats to players, also updates numberScored and numberOpps
     * @param score Position of the player who scored the goal
     * @param assist1 Position of player who assisted the goal
     * @param assist2 Position of other player who assisted the goal
     * @throws IllegalArgumentException See super class
     */
    public void score(Position score, Position assist1, Position assist2) throws IllegalArgumentException {
        super.score(score, assist1, assist2);
        numberScored++;
        numberOpps++;
    }

    // Only increments numberOpps
    public void missedOpp() {
        numberOpps++;
    }

    /**
     * Calculates power player percentage using numberScored and numberOpps
     * @return The calculated percentage
     */
    public double getSuccessRate() {
        if (numberOpps == 0) {
            return 0;
        }
        return (double) numberScored / numberOpps * 100;
    }

    /**
     * @param numberScored Updated number scored
     * @param numberOpps Updated number of opportunities
     * @throws IllegalArgumentException Thrown if given stats are negative or if numberScored is greater than numberOpps
     */
    public void setStats(int numberScored, int numberOpps) throws IllegalArgumentException {
        if (numberScored < 0 || numberOpps < 0) {
            throw new IllegalArgumentException("The given stats cannot be negative");
        }
        if (numberScored > numberOpps) {
            throw new IllegalArgumentException("The number of successes must be fewer than the number of attempts");
        }
        this.numberScored = numberScored;
        this.numberOpps = numberOpps;
    }

    /**
     * Updates numberScored stat using given percentage
     * @param successPercent Given power play percentage
     * @param numberOpps Updated number of opportunities
     * @throws IllegalArgumentException Thrown if given percentage is invalive or if numberOpps is negative
     */
    public void setStatsWithPercentage(double successPercent, int numberOpps) throws IllegalArgumentException {
        if (successPercent < 0 || successPercent > 100) {
            throw new IllegalArgumentException("Percentage must be in range 0-100");
        }
        if (numberOpps < 0) {
            throw new IllegalArgumentException("Number of opportunities cannot be negative");
        }
        numberScored = (int) ((successPercent / 100) * numberOpps);
        this.numberOpps = numberOpps;
    }

    // toString Method
    public String toString() {
        String result = super.toString();
        result += String.format("PP%%: %.2f", getSuccessRate());
        return result;
    }
}
