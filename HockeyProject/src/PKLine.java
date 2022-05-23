public class PKLine extends Line {
    private int numberKilled;
    private int numberAttempts;

    public PKLine(String name, Skater winger1, Skater winger2, Defenseman leftDe, Defenseman rightDe) {
        super(name, null, winger1, winger2, leftDe, rightDe);
        if (winger1 == null || winger2 == null || leftDe == null || rightDe == null) {
            throw new NullPointerException("Penalty Kill positions cannot be empty");
        }
        numberKilled = 0;
        numberAttempts = 0;
    }

    public PKLine(String name, Skater winger1, Skater winger2, Defenseman leftDe, Defenseman rightDe,
                  int numberKilled, int numberAttempts) {
        super(name, null, winger1, winger2, leftDe, rightDe);
        if (winger1 == null || winger2 == null || leftDe == null || rightDe == null) {
            throw new NullPointerException("Penalty Kill positions cannot be empty");
        }
        if (numberKilled < 0 || numberAttempts < 0) {
            throw new IllegalArgumentException("Given totals cannot be negative");
        }
        if (numberKilled > numberAttempts) {
            throw new IllegalArgumentException("Number of successfully killed penalties cannot be greater than " +
                    "number of penalties the line has faced");
        }
        this.numberKilled = numberKilled;
        this.numberAttempts = numberAttempts;
    }

    public double getKillPercent() {
        if (numberAttempts == 0) {
            return 0;
        }
        return (double) numberKilled / numberAttempts * 100;
    }

    public void setKillStats(int numberKilled, int numberAttempts) throws IllegalArgumentException {
        if (numberKilled > numberAttempts) {
            throw new IllegalArgumentException("Number of successfully killed penalties cannot be greater than " +
                    "number of penalties the line has faced");
        }
        if (numberAttempts < 0 || numberKilled < 0) {
            throw new IllegalArgumentException("Given totals cannot be negative");
        }
        this.numberKilled = numberKilled;
        this.numberAttempts = numberAttempts;
    }

    public void setKillStatsWithPercentage(double successPercent, int numberAttempts) {
        if (successPercent < 0 || successPercent > 100) {
            throw new IllegalArgumentException("Percentage must be in the range 0-100");
        }
        if (numberAttempts < 0) {
            throw new IllegalArgumentException("Number of attempts cannot be negative");
        }
        numberKilled = (int) ((successPercent / 100) * numberAttempts);
        this.numberAttempts = numberAttempts;
    }

    public void killedPenalty() {
        numberKilled++;
        numberAttempts++;
    }

    public void scoredOn() {
        numberAttempts++;
    }

    public String toString() {
        return String.format("%s\n" +
                "Offense 1: %s\n" +
                "Offense 2: %s\n" +
                "Left Defense: %s\n" +
                "Right Defense: %s\n", getName(), getLeftWing().getName(), getRightWing().getName(),
                getLeftDe().getName(), getRightDe().getName());
    }
}
