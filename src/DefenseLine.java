import java.io.Serializable;

/**
 * DefenseLine
 *
 * A class specifically composed of two defenseman. Coaches will often have offensive lines and defensive pairs that do
 * not necessarily match up for the entirety of play.
 */

public class DefenseLine extends Line implements Serializable {
    /**
     * Assigns given players to their position and given name. Leaves offensive positions null.
     * @throws NullPointerException If any of the given players are null
     */
    public DefenseLine(String name, Defenseman leftDe, Defenseman rightDe) throws NullPointerException {
        super(name, null, null, null, leftDe, rightDe);
        if (leftDe == null || rightDe == null) {
            throw new NullPointerException("Players in a defensive pair cannot be left blank");
        }
    }
}
