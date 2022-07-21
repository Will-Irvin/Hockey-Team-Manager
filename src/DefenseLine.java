/**
 * DefenseLine
 *
 * A class specifically composed of two defenseman. Methods will manipulate the stats for both players.
 */
public class DefenseLine extends Line {
    private Defenseman leftDe;
    private Defenseman rightDe;

    /**
     * Assigns given players to their position and given name
     * @throws NullPointerException If any of the given players are null
     * @throws IllegalArgumentException If the given players are equal
     */
    public DefenseLine(String name, Defenseman leftDe, Defenseman rightDe) throws NullPointerException,
            IllegalArgumentException {
        super(name);
        if (leftDe == null || rightDe == null) {
            throw new NullPointerException(EMPTY_POSITIONS);
        }
        if (leftDe.equals(rightDe)) {
            throw new NullPointerException(PLAYER_DUPLICATES_ERROR);
        }
        this.leftDe = leftDe;
        this.rightDe = rightDe;
    }

    public DefenseLine(DefenseLine line) {
        super(line.getName());
        this.leftDe = line.leftDe;
        this.rightDe = line.rightDe;
    }

    // Getter Methods

    protected Defenseman getLeftDe() {
        return leftDe;
    }

    protected Defenseman getRightDe() {
        return rightDe;
    }

    // Setter Methods

    /**
     * @throws NullPointerException If given argument is null
     * @throws IllegalArgumentException If given argument is equal to other argument
     */
    public void setLeftDe(Defenseman leftDe) throws NullPointerException, IllegalArgumentException {
        if (leftDe == null) {
            throw new NullPointerException(EMPTY_POSITIONS);
        }
        if (leftDe.equals(rightDe)) {
            throw new IllegalArgumentException(PLAYER_DUPLICATES_ERROR);
        }
        this.leftDe = leftDe;
    }

    /**
     * @throws NullPointerException If given argument is null
     * @throws IllegalArgumentException If given argument is equal to other argument
     */
    public void setRightDe(Defenseman rightDe) throws NullPointerException, IllegalArgumentException {
        if (rightDe == null) {
            throw new NullPointerException(EMPTY_POSITIONS);
        }
        if (rightDe.equals(leftDe)) {
            throw new IllegalArgumentException(PLAYER_DUPLICATES_ERROR);
        }
        this.rightDe = rightDe;
    }

    // Updates stats for the entire line when they are scored on
    @Override
    public void lineScoredOn() {
        leftDe.scoredAgainst();
        rightDe.scoredAgainst();
    }

    /**
     * @return A formatted String containing the players on the line and the power play success percentage of the line
     */
    @Override
    public String lineRoster() {
        return String.format("""
                %s
                Left Defenseman: %s
                Right Defenseman: %s""", getName(), leftDe.getName(), rightDe.getName());
    }

    /**
     * @return An array of the skaters on this line
     */
    @Override
    public Skater[] getSkaters() {
        return new Skater[]{leftDe, rightDe};
    }

    /**
     * @param s Checks the line to see if one of the players is this skater
     * @return Whether the line contains this skater or not
     */
    @Override
    public boolean contains(Skater s) {
        return s.equals(leftDe) || s.equals(rightDe);
    }
}
