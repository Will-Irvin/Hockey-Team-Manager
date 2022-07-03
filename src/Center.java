/**
 * Center
 * Class that extends from skater to include information and methods relevant to a player at Center. This information
 * mostly relates to faceoffs.
 */

public class Center extends Skater {
    private int faceOffWins;
    private int faceOffTotal;    // Used to calculate face off percentage

    /**
     * Behaves like basic Skater constructor and initializes stats to 0. Assigns position as CENTER
     */
    public Center(String name, int playerNumber, String stickHand) {
        super(name, playerNumber, stickHand, Position.Center);
        faceOffWins = 0;
        faceOffTotal = 0;
    }

    /**
     * Assigns arguments to their respective variables, assigns position to CENTER
     * @throws IllegalArgumentException Thrown when faceOffPercentage argument is not in range 0-100 or when
     * faceOffTotal is negative
     */
    public Center(String name, int playerNumber, String stickHand, int goals, int assists, int plusMinus, int hits,
                  double penaltyMinutes, double faceOffPercentage, int faceOffTotal) throws IllegalArgumentException {
        super(name, playerNumber, stickHand, Position.Center, goals, assists, plusMinus, hits, penaltyMinutes);
        if (faceOffPercentage < 0 || faceOffPercentage > 100) {
            throw new IllegalArgumentException("Face Off % must be between 0-100");
        }
        if (faceOffTotal < 1) {
            throw new IllegalArgumentException("Face Off Total must be at least 1");
        }
        faceOffWins = (int) (faceOffTotal * (faceOffPercentage/100));
        this.faceOffTotal = faceOffTotal;
    }

    // Shallow Copy Constructor
    public Center(Skater s) {
        super(s);
        setPosition(Position.Center);
        if (s instanceof Center c) {
            this.faceOffWins = c.faceOffWins;
            this.faceOffTotal = c.faceOffTotal;
        } else {
            faceOffWins = 0;
            faceOffTotal = 0;
        }
    }

    // Getter Methods

    public int getFaceOffWins() {
        return faceOffWins;
    }

    public int getFaceOffTotal() {
        return faceOffTotal;
    }

    /**
     * Calculates face off win percentage using the instance variables
     * @return The calculated percentage
     */
    public double getFaceoffPercent() {
        return ((double) faceOffWins / faceOffTotal) * 100;
    }

    // Increments wins and total up by 1
    public void winFaceoff() {
        faceOffTotal++;
        faceOffWins++;
    }

    // Only increments total up by 1
    public void loseFaceoff() {
        faceOffTotal++;
    }

    /**
     * Update wins and total variables using the given arguments
     * @param faceoffPercent Percentage of face offs won, used to calculate the faceoffWin variable
     * @param faceoffTotal Total number of face offs taken
     * @throws IllegalArgumentException Thrown if percent is not in range 0-100, or if face off total is negative
     */
    public void setFaceoffPercent(double faceoffPercent, int faceoffTotal) throws IllegalArgumentException {
        if (faceoffPercent < 0 || faceoffPercent > 100) {
            throw new IllegalArgumentException("Face Off % must be between 0-100");
        } else if (faceoffTotal < 0) {
            throw new IllegalArgumentException("Face Off total cannot be negative");
        } else {
            faceOffWins = (int)((faceoffPercent/100) * faceoffTotal);
            this.faceOffTotal = faceoffTotal;
        }
    }

    // Resets stats to 0
    @Override
    public void resetStats() {
        super.resetStats();
        faceOffTotal = 0;
        faceOffWins = 0;
    }

    /**
     * @return A formatted String that contains all relevant information on the player
     */
    @Override
    public String statsDisplay() {
        return super.statsDisplay()
                + String.format("\nFaceoff Percentage: %.2f%%", getFaceoffPercent());
    }
}
