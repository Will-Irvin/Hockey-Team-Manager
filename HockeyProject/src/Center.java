import java.io.Serializable;

/**
 * Center
 * Class that extends from skater to include information and methods relevant to a player at Center. This information
 * mostly relates to faceoffs.
 */

public class Center extends Skater implements Serializable {
    private int faceoffWins;
    private int faceoffTotal;    // Used to calculate faceoff percentage

    /**
     * Behaves like basic Skater constructor and initializes stats to 0. Assigns position as CENTER
     */
    public Center(String name, int playerNumber, String stickHand) {
        super(name, playerNumber, stickHand, Position.CENTER);
        faceoffWins = 0;
        faceoffTotal = 0;
    }

    /**
     * Assigns arguments to their respective variables, assigns position to CENTER
     * @throws IllegalArgumentException Thrown when faceOffPercentage argument is not in range 0-100 or when
     * faceOffTotal is negative
     */
    public Center(String name, int playerNumber, String stickHand, int goals,
                  int assists, int plusMinus, double faceOffPercentage, int faceOffTotal)
                  throws IllegalArgumentException {
        super(name, playerNumber, stickHand, Position.CENTER, goals, assists, plusMinus);
        if (faceOffPercentage < 0 || faceOffPercentage > 100) {
            throw new IllegalArgumentException("Face Off % must be between 0-100");
        }
        if (faceOffTotal < 1) {
            throw new IllegalArgumentException("Face Off Total must be at least 1");
        }
        faceoffWins = (int) (faceOffTotal * (faceOffPercentage/100));
        this.faceoffTotal = faceOffTotal;
    }

    // Getter Methods

    public int getFaceoffWins() {
        return faceoffWins;
    }

    public int getFaceoffTotal() {
        return faceoffTotal;
    }

    /**
     * Calculates face off win percentage using the instance variables
     * @return The calculated percentage
     */
    public double getFaceoffPercent() {
        return ((double) faceoffWins/faceoffTotal) * 100;
    }

    // Increments wins and total up by 1
    public void winFaceoff() {
        faceoffTotal++;
        faceoffWins++;
    }

    // Only increments total up by 1
    public void loseFaceoff() {
        faceoffTotal++;
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
            faceoffWins = (int)((faceoffPercent/100) * faceoffTotal);
            this.faceoffTotal = faceoffTotal;
        }
    }

    // Resets stats to 0
    public void resetStats() {
        super.resetStats();
        faceoffTotal = 0;
        faceoffWins = 0;
    }

    /**
     * @return A formatted String that contains all relevant information on the player
     */
    public String statsDisplay() {
        return super.statsDisplay()
                + String.format("\n    Faceoff Percentage: %.2f%%", getFaceoffPercent());
    }
}
