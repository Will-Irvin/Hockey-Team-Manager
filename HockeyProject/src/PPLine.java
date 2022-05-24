public class PPLine extends Line {
    private int numberScored;
    private int numberOpps;

    public PPLine(String name, Center center, Skater leftWing, Skater rightWing, Defenseman leftDe, Defenseman rightDe)
            throws NullPointerException {
        super(name, center, leftWing, rightWing, leftDe, rightDe);
        if (center == null || leftWing == null || rightWing == null || leftDe == null || rightDe == null) {
            throw new NullPointerException("All positions must be filled for a Power Play line");
        }
        numberScored = 0;
        numberOpps = 0;
    }

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

    public int getNumberScored() {
        return numberScored;
    }

    public int getNumberOpps() {
        return numberOpps;
    }

    public void score(Position position) throws IllegalArgumentException {
        super.score(position);
        numberScored++;
        numberOpps++;
    }

    public void score(Position score, Position assist) throws IllegalArgumentException {
        super.score(score, assist);
        numberScored++;
        numberOpps++;
    }

    public void score(Position score, Position assist1, Position assist2) throws IllegalArgumentException {
        super.score(score, assist1, assist2);
        numberScored++;
        numberOpps++;
    }

    public void missedOpp() {
        numberOpps++;
    }

    public double getSuccessRate() {
        if (numberOpps == 0) {
            return 0;
        }
        return (double) numberScored / numberOpps * 100;
    }

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

    public void setStatsWithPercentage(double successPercent, int numberOpps) throws IllegalArgumentException {
        if (successPercent < 0 || successPercent > 100) {
            throw new IllegalArgumentException("Percentage must be in range 0-100");
        }
        if (numberOpps < 0) {
            throw new IllegalArgumentException("Number of opportunities cannot be negative");
        }
        numberScored = (int) ((successPercent / 100) * numberOpps);
    }

    public String toString() {
        String result = super.toString();
        result += String.format("PP%%: %.2f", getSuccessRate());
        return result;
    }
}
