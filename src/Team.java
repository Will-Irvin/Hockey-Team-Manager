import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Team
 * Class used to store information and methods related to a hockey team including its players, lines, and stats
 */
public class Team implements Serializable {
    private String name;
    private final ArrayList<Skater> skaters;
    private final ArrayList<Line> lines;
    private final ArrayList<Goalie> goalies;
    private int wins;
    private int losses;
    private int otLosses;

    private static final int NUM_BASIC_STATS = 7;

    /**
     * Initializes team with given name, empty lists, and a record of 0-0-0
     * @param name Given name
     * @throws NullPointerException If given name is null
     * @throws IllegalArgumentException If given name is blank
     */
    public Team(String name) throws NullPointerException, IllegalArgumentException {
        if (name == null) {
            throw new NullPointerException(Player.NULL_ERROR);
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException(Player.NAME_BLANK);
        } else if (name.indexOf('|') >= 0) {
            throw new IllegalArgumentException(Player.SPECIAL_CHAR);
        }
        this.name = name;
        this.skaters = new ArrayList<>();
        this.lines = new ArrayList<>();
        this.goalies = new ArrayList<>();
        this.wins = 0;
        this.losses = 0;
        this.otLosses = 0;
    }

    /**
     * Initializes team with given arguments assigned to their respective instance variables, and empty lists
     * @throws NullPointerException If given name is null
     * @throws IllegalArgumentException If name is blank or if stats are negative
     */
    public Team(String name, int wins, int losses, int lossesOT) throws NullPointerException, IllegalArgumentException {
        if (name == null) {
            throw new NullPointerException(Player.NULL_ERROR);
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException(Player.NAME_BLANK);
        } else if (name.indexOf('|') >= 0) {
            throw new IllegalArgumentException(Player.SPECIAL_CHAR);
        }
        this.name = name;
        this.skaters = new ArrayList<>();
        this.lines = new ArrayList<>();
        this.goalies = new ArrayList<>();
        if (wins < 0 || losses < 0 || lossesOT < 0) {
            throw new IllegalArgumentException("Record " + Skater.POS_STAT);
        }
        this.wins = wins;
        this.losses = losses;
        this.otLosses = lossesOT;
    }

    // Creates shallow copy of the given team
    public Team(Team team) throws NullPointerException {
        if (team == null) {
            throw new NullPointerException(Player.NULL_ERROR);
        }
        name = team.getName();
        wins = team.getWins();
        losses = team.getLosses();
        otLosses = team.getOtLosses();
        skaters = new ArrayList<>(List.of(team.getSkaters()));
        lines = new ArrayList<>(List.of(team.getLines()));
        goalies = new ArrayList<>(List.of(team.getGoalies()));
    }

    // Getter Methods

    public String getName() {
        return name;
    }

    public Skater[] getSkaters() {
        return skaters.toArray(new Skater[]{});
    }

    public Line[] getLines() {
        return lines.toArray(new Line[]{});
    }

    public Goalie[] getGoalies() {
        return goalies.toArray(new Goalie[]{});
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getOtLosses() {
        return otLosses;
    }

    /**
     * @throws NullPointerException If given name is null
     * @throws IllegalArgumentException If given name is blank
     */
    public void setName(String name) throws NullPointerException, IllegalArgumentException {
        if (name == null) {
            throw new NullPointerException(Player.NULL_ERROR);
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException(Player.NAME_BLANK);
        } else if (name.indexOf('|') >= 0) {
            throw new IllegalArgumentException(Player.SPECIAL_CHAR);
        }
        this.name = name;
    }

    /**
     * @throws IllegalArgumentException If given stats are negative
     */
    public void setWins(int wins) {
        if (wins < 0) {
            throw new IllegalArgumentException("Wins " + Skater.POS_STAT);
        }
        this.wins = wins;
    }

    /**
     * @throws IllegalArgumentException If given stats are negative
     */
    public void setLosses(int losses) {
        if (losses < 0) {
            throw new IllegalArgumentException("Losses " + Skater.POS_STAT);
        }
        this.losses = losses;
    }

    /**
     * @throws IllegalArgumentException If given stats are negative
     */
    public void setOtLosses(int otLosses) {
        if (otLosses < 0) {
            throw new IllegalArgumentException("Overtime loss " + Skater.POS_STAT);
        }
        this.otLosses = otLosses;
    }

    // Increments wins up by 1
    public void win() {
        wins++;
    }

    // Increments losses up by 1
    public void lose() {
        losses++;
    }

    // Increments lossesOT up by 1
    public void tie() {
        otLosses++;
    }

    /**
     * Adds given player to the appropriate ArrayList and returns the index where the player was added.
     * @param player The player being added to the team
     * @return The index of the player in the array list where it was added or -1 if there is an issue
     */
    public int addPlayer(Player player) {
        if (skaters.contains(player) || goalies.contains(player)) {
            return -1;
        }
        int index;
        if (player instanceof Skater s) {
            index = findIndex(player, skaters.toArray(new Player[]{}));
            skaters.add(index, s);
            return index;
        } else if (player instanceof Goalie g) {
            index = findIndex(player, goalies.toArray(new Player[]{}));
            goalies.add(index, g);
            return index;
        } else {
            return -1;
        }
    }

    /**
     * This method is used to determine at which index a player should be added to a given teams skater list or goalie
     * list. It will return that index which will be used in the addPlayer method.
     * @param player The player being added to the team
     * @param list The list that the player should be added to: skaters for Skater class and goalies for Goalie class
     * @return The index where the player was added
     */
    private static int findIndex(Player player, Player[] list) {
        if (list.length == 0) {
            return 0;
        }
        int low = 0;
        int high = list.length;
        while (true) {
            int i = (low + high) / 2;
            if (player.getPlayerNumber() < list[i].getPlayerNumber()) {
                if (i == 0) {
                    return i;
                }
                if (player.getPlayerNumber() > list[i - 1].getPlayerNumber()) {
                    return i;
                } else {
                    high = i;
                }
            } else {
                if (i == list.length - 1) {
                    return i + 1;
                }
                if (player.getPlayerNumber() < list[i + 1].getPlayerNumber()) {
                    return i + 1;
                } else {
                    low = i;
                }
            }
        }
    }

    // Adds a line to the teams list, applies same method as addPlayer/addGoalie
    public int addLine(Line line) {
        if (lines.contains(line)) {
            return -1;
        }
        if (lines.size() == 0) {
            lines.add(line);
            return 0;
        }
        int low = 0;
        int high = lines.size();
        while (true) {
            int i = (low + high) / 2;
            if (line.getName().compareTo(lines.get(i).getName()) <= 0) {
                if (i == 0 || line.getName().compareTo(lines.get(i - 1).getName()) >= 0) {
                    lines.add(i, line);
                    return i;
                } else {
                    high = i;
                }
            } else {
                if (i == lines.size() - 1) {
                    lines.add(line);
                    return i + 1;
                }
                if (line.getName().compareTo(lines.get(i + 1).getName()) <= 0) {
                    lines.add(i + 1, line);
                    return i + 1;
                } else {
                    low = i;
                }
            }
        }
    }

    /**
     * Updates a given player already on the team. Ensures that the new player can be safely added to the team before
     * removing the old player
     * @param oldPlayer Player being changed
     * @param newPlayer New player being added
     * @return True if the change was made false otherwise
     */
    public int changePlayer(Player oldPlayer, Player newPlayer) {
        if (!newPlayer.equals(oldPlayer) && (skaters.contains(newPlayer) || goalies.contains(newPlayer))) {
            return -1;
        }
        if (oldPlayer instanceof Skater) {
            if (!removePlayer(oldPlayer)) {
                return -2;
            }
        } else if (oldPlayer instanceof Goalie){
            if (!goalies.remove(oldPlayer)) {
                return -2;
            }
        }
        return addPlayer(newPlayer);
    }

    // Edit a line on the list, works like changePlayer/Goalie for lines
    public int changeLine(Line oldLine, Line newLine) {
        if (!oldLine.equals(newLine) && lines.contains(newLine)) {
            return -1;
        }
        if (!lines.remove(oldLine)) {
            return -2;
        }
        return addLine(newLine);
    }

    /**
     * @param player Player removed from the appropriate list
     * @return Whether the removal was successful or not
     */
    public boolean removePlayer(Player player) {
        if (player instanceof Skater s) {
            if (skaters.remove(player)) {
                for (int i = 0; i < lines.size(); i++) {
                    if (lines.get(i).contains(s)) {
                        lines.remove(i);
                        i--;
                    }
                }
                return true;
            } else {
                return false;
            }
        } else if (player instanceof Goalie) {
            return goalies.remove(player);
        }
        return false;
    }

    // Like removePlayer but for lines
    public boolean removeLine(Line line) {
        return lines.remove(line);
    }

    /**
     * Finds a skater from the team's list based on the given player number
     * @param playerNumber Player number of the skater the code is seeking
     * @return The skater if there is a match or null if there is no skater with that number
     */
    public Skater getSkater(int playerNumber) {
        if (skaters.isEmpty()) {
            return null;
        }
        int high = skaters.size();
        int low = 0;
        while (true) {
            if (high < low) {
                return null;
            }
            int i = (high + low) / 2;
            if (skaters.get(i).getPlayerNumber() == playerNumber) {
                return skaters.get(i);
            } else if (skaters.get(i).getPlayerNumber() < playerNumber) {
                low = i;
            } else {
                high = i;
            }
        }
    }

    /**
     * Calculates team's overall face off percentage win rate by looking through the list of players for each center
     * and adding their stats to a running total.
     * @return The calculated percentage
     */
    public double getFaceOffPercent() {
        int totalFaceOffs = 0;
        int faceOffWins = 0;
        for (Skater player: skaters) {
            if (player instanceof Center center) {
                totalFaceOffs += center.getFaceOffTotal();
                faceOffWins += center.getFaceOffWins();
            }
        }
        if (totalFaceOffs == 0) {
            return 0;
        }
        return ((double) faceOffWins / totalFaceOffs) * 100;
    }

    /**
     * Calculates the teams overall penalty kill success rate by looking through the list of lines for Penalty Kill
     * lines and using their stats to calculate the overall success rate
     * @return Overall penalty kill success rate of the team
     */
    public double getPKSuccess() {
        int tryTotal = 0;
        double successTotal = 0;
        for (Line line: lines) {
            if (line instanceof PKLine pk) {
                tryTotal += pk.getAttempts();
                successTotal += pk.getSuccesses();
            }
        }
        if (tryTotal == 0) {
            return 0;
        }
        return (successTotal / tryTotal) * 100;
    }

    /**
     * Calculates the teams overall power play success rate by looking through the list of lines for Power Play lines
     * and using their stats to calculate the overall success rate
     * @return Overall power play success rate of the team
     */
    public double getPPSuccess() {
        int tryTotal = 0;
        double successTotal = 0;
        for (Line line: lines) {
            if (line instanceof PPLine pp) {
                tryTotal += pp.getAttempts();
                successTotal += pp.getSuccesses();
            }
        }
        if (tryTotal == 0) {
            return 0;
        }
        return (successTotal / tryTotal) * 100;
    }

    /**
     * Calculates the average number of shots that a team lets up per game using the team's record and the total number
     * of shots that each goalie has faced
     * @return Average number of shots that the team lets up per game
     */
    public double getAverageShots() {
        int totalShots = 0;
        for (Goalie goalie: goalies) {
            totalShots += goalie.getShotsAgainst();
        }
        int matches = wins + losses + otLosses;
        if (matches == 0) {
            return 0;
        }
        return (double) totalShots / matches;
    }

    /**
     * Resets the team stats and the stats for the entire roster of the team (if there is a new season for example)
     * as well as any special teams stats
     */
    public void resetTeamStats() {
        wins = 0;
        losses = 0;
        otLosses = 0;
        for (Skater player: skaters) {
            player.resetStats();
        }
        for (Goalie goalie: goalies) {
            goalie.resetStats();
        }
        for (Line line: lines) {
            if (line instanceof SpecialTeamsLine specialTeams) {
                specialTeams.setSuccessStats(0, 0);
            }
        }
    }

    /**
     * @return A string that lists every player on the team
     */
    public String generateRoster() {
        StringBuilder result = new StringBuilder(String.format("%s\nSkaters:\n", name));
        for (Skater player: skaters) {
            result.append(String.format("%s %d\n", player.getName(), player.getPlayerNumber()));
        }
        result.append("\nGoalies:\n");
        for (Goalie goalie: goalies) {
            result.append(String.format("%s %d\n", goalie.getName(), goalie.getPlayerNumber()));
        }
        return result.substring(0, result.length() - 1);
    }

    /**
     * @return A string containing every player and line listed on the team as well as their stats that will be included
     * in a formatted file to save data
     */
    public String writeToFile() {
        StringBuilder result = new StringBuilder(String.format("%s|%d|%d|%d\n%d\n", name, wins, losses, otLosses,
                skaters.size()));

        for (Skater player: skaters) {
            result.append(String.format("%s|%d|%s|", player.getName(), player.getPlayerNumber(),
                    player.getStickHand()));

            switch (player.getPosition()) {
                case Center -> result.append("C");
                case Left_Wing -> result.append("LW");
                case Right_Wing -> result.append("RW");
                case Left_Defense -> result.append("LD");
                case Right_Defense -> result.append("RD");
            }
            result.append(String.format("|%d|%d|%d|%d|%.1f", player.getGoals(), player.getAssists(),
                    player.getPlusMinus(), player.getHits(), player.getPenaltyMinutes()));
            if (player instanceof Center c) {
                result.append(String.format("|%.2f|%d", c.getFaceOffPercent(), c.getFaceOffTotal()));
            } else if (player instanceof Defenseman d) {
                result.append(String.format("|%d", d.getShotsBlocked()));
            }
            result.append('\n');
        }
        result.append(String.format("%d\n", goalies.size()));
        for (Goalie goalie: goalies) {
            result.append(String.format("%s|%d|%d|%d|%d|%d|%.3f|%d\n", goalie.getName(), goalie.getPlayerNumber(),
                    goalie.getWins(), goalie.getLosses(), goalie.getOtLosses(), goalie.getShutouts(),
                    goalie.getSavePercent(), goalie.getShotsAgainst()));
        }
        result.append(String.format("%d\n", lines.size()));
        for (Line line: lines) {
            result.append(String.format("%s", line.getName()));
            for (Skater skater: line.getSkaters()) {
                result.append(String.format("|%d", skater.getPlayerNumber()));
            }
            if (line instanceof SpecialTeamsLine specialTeams) {
                result.append(String.format("|%.2f|%d", specialTeams.getSuccessPercent(), specialTeams.getAttempts()));
            }
            result.append('\n');
        }
        return result.substring(0, result.length() - 1);
    }

    /**
     * @return A 2D object array where each row corresponds to one skater, and each element corresponds to one of that
     * skater's stats to display in a JTable
     */
    public Object[][] generateSkaterRosterWithStats() {
        Object[][] result = new Object[skaters.size()][NUM_BASIC_STATS];
        for (int i = 0; i < skaters.size(); i++) {
            result[i] = skaters.get(i).getStatsArray();
        }
        return result;
    }

    /**
     * @return A 2D object array where each row corresponds to one goalie, and each element corresponds to one of that
     * skater's stats that will be displayed in a JTable.
     */
    public Object[][] generateGoalieRosterWithStats() {
        Object[][] result = new Object[goalies.size()][NUM_BASIC_STATS];
        for (int i = 0; i < goalies.size(); i++) {
            result[i] = goalies.get(i).getStatsArray();
        }
        return result;
    }

    /**
     * @return A formatted String containing the team's overall stats
     */
    public String displayTeamStats() {
        return String.format("""
                        %s
                        Record: %d-%d-%d
                        Face Off %%: %.2f
                        Power Play %%: %.2f
                        Penalty Kill %%: %.2f
                        Average Shots Against Per Game: %.2f""", name, wins, losses, otLosses, getFaceOffPercent(),
                getPPSuccess(), getPKSuccess(), getAverageShots());
    }

    /**
     * @param o Object being compared to the current team
     * @return True if the object is a team with the exact same name, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Team t) {
            return t.getName().equals(this.name);
        }
        return false;
    }

    // toString Method
    @Override
    public String toString() {
        return name;
    }
}
