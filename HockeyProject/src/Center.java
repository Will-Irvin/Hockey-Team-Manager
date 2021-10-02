public class Center extends Player{
    private double faceOffPercent;

    public Center(String name, int playerNumber) {
        super(name, playerNumber);
        faceOffPercent = 0;
    }

    public double getFaceOffPercent() {
        return faceOffPercent;
    }
}
