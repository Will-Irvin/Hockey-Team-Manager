public class Line {
    private Center center;
    private Skater leftWing;
    private Skater rightWing;
    private Defenseman leftDe;
    private Defenseman rightDe;

    public Line(Center center, Skater leftWing, Skater rightWing, Defenseman leftDe, Defenseman rightDe) {
        if (center == null || leftDe == null || leftWing == null || rightWing == null || rightDe == null) {
            throw new NullPointerException("Inputted players must not be null");
        }
        this.center = center;
        this.leftWing = leftWing;
        this.rightWing = rightWing;
        this.leftDe = leftDe;
        this.rightDe = rightDe;
    }
    // TODO
    public void score(int scorerNumber) {
        if (center.getPlayerNumber() == scorerNumber) {
            center.score();
        } else {
            center.scoredOnIce();
        }
        if (leftWing.getPlayerNumber() == scorerNumber) {
            leftWing.score();
        } else {
            leftWing.scoredOnIce();
        }
        if (rightWing.getPlayerNumber() == scorerNumber) {
            rightWing.score();
        } else {
            rightWing.scoredOnIce();
        }
        if (leftDe.getPlayerNumber() == scorerNumber) {
            leftDe.score();
        } else {
            leftDe.scoredOnIce();
        }
        if (rightDe.getPlayerNumber() == scorerNumber) {
            rightDe.scoredOnIce();
        } else {
            rightDe.scoredOnIce();
        }
    }
    public void score(int scorerNumber, int assistNumber) {
        if (center.getPlayerNumber() == scorerNumber) {
            center.score();
        } else if(center.getPlayerNumber() == assistNumber) {
            center.assist();
        } else {
            center.scoredOnIce();
        }

        if (leftWing.getPlayerNumber() == scorerNumber) {
            leftWing.score();
        } else if (leftWing.getPlayerNumber() == scorerNumber) {
            leftWing.assist();
        } else {
            leftWing.scoredOnIce();
        }

        if (rightWing.getPlayerNumber() == scorerNumber) {
            rightWing.score();
        } else if (rightWing.getPlayerNumber() == assistNumber) {
            rightWing.assist();
        } else {
            rightWing.scoredOnIce();
        }

        if (leftDe.getPlayerNumber() == scorerNumber) {
            leftDe.score();
        } else if (leftDe.getPlayerNumber() == assistNumber) {
            leftDe.assist();
        } else {
                leftDe.scoredOnIce();
        }

        if (rightDe.getPlayerNumber() == scorerNumber) {
            rightDe.score();
        } else if (rightDe.getPlayerNumber() == assistNumber) {
            rightDe.assist();
        } else {
            rightDe.scoredOnIce();
        }
    }
}
