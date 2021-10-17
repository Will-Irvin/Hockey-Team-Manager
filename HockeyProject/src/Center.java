public class Center extends Skater{
    private int faceoffWins;
    private int faceoffTotal;

    public Center(String name, int playerNumber, String stickHand) {
        super(name, playerNumber, stickHand);
        faceoffWins = 0;
        faceoffTotal = 0;
    }

    public Center(String name, int playerNumber, String stickHand, int goals,
                  int assists, int plusMinus, double faceOffPercentage, int faceOffTotal) {
        super(name, playerNumber, stickHand, goals, assists, plusMinus);
        faceoffWins = (int) (faceOffTotal * faceOffPercentage);
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

    public String toString() {
        return super.toString()
                + String.format("    Faceoff Percentage: %.2f\n", getFaceoffPercent());
    }
}
