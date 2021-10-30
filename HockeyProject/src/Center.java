public class Center extends Skater{
    private int faceoffWins;
    private int faceoffTotal;

    public Center(String name, int playerNumber, String stickHand) {
        super(name, playerNumber, stickHand, "Center");
        faceoffWins = 0;
        faceoffTotal = 0;
    }

    public Center(String name, int playerNumber, String stickHand, int goals,
                  int assists, int plusMinus, double faceOffPercentage, int faceOffTotal) {
        super(name, playerNumber, stickHand, "Center", goals, assists, plusMinus);
        if (faceOffPercentage < 0 || faceOffPercentage > 100) {
            throw new IllegalArgumentException("Face Off % must be between 0-100");
        }
        if (faceOffTotal < 1) {
            throw new IllegalArgumentException("Face Off Total must be at least 1");
        }
        faceoffWins = (int) (faceOffTotal * (faceOffPercentage/100));
        this.faceoffTotal = faceOffTotal;
    }

    public double getFaceoffPercent() {
        return ((double) faceoffWins/faceoffTotal) * 100;
    }

    public void winFaceoff() {
        faceoffTotal++;
        faceoffWins++;
    }

    public void loseFaceoff() {
        faceoffTotal++;
    }

    public void setFaceoffPercent(double faceoffPercent, int faceoffTotal) {
        if (faceoffPercent < 0 || faceoffPercent > 100) {
            System.out.println("Face Off % must be between 0-100");
        } else if (faceoffTotal < 1) {
            System.out.println("Face Off total must be at least 1");
        } else {
            faceoffWins = (int)((faceoffPercent/100) * faceoffTotal);
            this.faceoffTotal = faceoffTotal;
        }
    }

    public String toString() {
        return super.toString()
                + String.format("\n    Faceoff Percentage: %.2f", getFaceoffPercent());
    }
}
