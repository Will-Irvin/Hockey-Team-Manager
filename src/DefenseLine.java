/**
 * DefenseLine
 *
 * A class specifically composed of two defenseman. Coaches will often have offensive lines and defensive pairs that do
 * not necessarily match up for the entirety of play.
 */

public class DefenseLine extends Line {
    private Defenseman leftDe;
    private Defenseman rightDe;

    /**
     * Assigns given players to their position and given name
     * @throws NullPointerException If any of the given players are null
     */
    public DefenseLine(String name, Defenseman leftDe, Defenseman rightDe) throws NullPointerException {
        super(name);
        if (leftDe == null || rightDe == null) {
            throw new NullPointerException(nullError);
        }
        if (leftDe.equals(rightDe)) {
            throw new NullPointerException(playerDuplicatesError);
        }
        this.leftDe = leftDe;
        this.rightDe = rightDe;
    }

    // Getter Methods

    public Defenseman getLeftDe() {
        return leftDe;
    }

    public Defenseman getRightDe() {
        return rightDe;
    }

    // Setter Methods

    /**
     * @throws NullPointerException If given argument is null
     * @throws IllegalArgumentException If given argument is equal to other argument
     */
    public void setLeftDe(Defenseman leftDe) throws NullPointerException, IllegalArgumentException {
        if (leftDe == null) {
            throw new NullPointerException(nullError);
        }
        if (leftDe.equals(rightDe)) {
            throw new IllegalArgumentException(playerDuplicatesError);
        }
        this.leftDe = leftDe;
    }

    /**
     * @throws NullPointerException If given argument is null
     * @throws IllegalArgumentException If given argument is equal to other argument
     */
    public void setRightDe(Defenseman rightDe) throws NullPointerException, IllegalArgumentException {
        if (rightDe == null) {
            throw new NullPointerException(nullError);
        }
        if (rightDe.equals(leftDe)) {
            throw new IllegalArgumentException(playerDuplicatesError);
        }
        this.rightDe = rightDe;
    }

    @Override
    public void lineScoredOn() {
        leftDe.scoredAgainst();
        rightDe.scoredAgainst();
    }

    @Override
    public String lineRoster() {
        return String.format("""
                %s
                Left Defenseman: %s
                Right Defenseman: %s""", getName(), leftDe.getName(), rightDe.getName());
    }
}
