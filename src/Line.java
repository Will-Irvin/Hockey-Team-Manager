import java.io.Serializable;

/**
 * Line
 * Class that serves as a baseline for any Line class. Only variable is name which will be built on in subclasses.
 */
public abstract class Line implements Serializable {
    private String name;

    // Error messages
    public static final String nullError = "Positions cannot be left empty";
    public static final String playerDuplicatesError = "The same player cannot be at two different positions";
    public static final String positionDuplicatesError = "Given positions must be different";

    /**
     * Initializes instance variables with their respective arguments. A player can be left null if the user so chooses.
     * @throws NullPointerException Thrown if the given name is null
     * @throws IllegalArgumentException Thrown if the given name is blank
     */
    public Line(String name) throws NullPointerException, IllegalArgumentException {
        if (name == null) {
            throw new NullPointerException("Name cannot be null");
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        this.name = name;
    }

    // Getter Method

    public String getName() {
        return name;
    }

    /**
     * Setter method for name
     * @throws NullPointerException Thrown if the given name is null
     * @throws IllegalArgumentException Thrown if the given name is blank
     */
    public void setName(String name) throws NullPointerException, IllegalArgumentException {
        if (name == null) {
            throw new NullPointerException("Name cannot be null");
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        this.name = name;
    }

    // Abstract methods

    public abstract void lineScoredOn();

    public abstract String lineRoster();

    /**
     * @param o Object being compared to the line
     * @return True if the object is a line, has the same name as the current line, and has the same players as the
     * current line
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Line l) {
            return l.getName().equals(this.name);
        }
        return false;
    }

    // toString Method
    @Override
    public String toString() {
        return name;
    }
}
