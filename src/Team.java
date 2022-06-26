import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Team
 * Class used to store information and methods related to a hockey team including its players, lines, and stats
 */
public class Team implements Serializable {
    private String name;
    private final ArrayList<Skater> players;
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
            throw new NullPointerException("Name cannot be null");
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name;
        this.players = new ArrayList<>();
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
            throw new NullPointerException("Name cannot be null");
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name;
        this.players = new ArrayList<>();
        this.lines = new ArrayList<>();
        this.goalies = new ArrayList<>();
        if (wins < 0 || losses < 0 || lossesOT < 0) {
            throw new IllegalArgumentException("Win and loss totals cannot be negative");
        }
        this.wins = wins;
        this.losses = losses;
        this.otLosses = lossesOT;
    }

    /**
     * Initializes team with given arguments assigned to their respective instance variables
     * @throws NullPointerException Thrown if given name or lists are null
     * @throws IllegalArgumentException Thrown if name is blank or given stats are negative
     */
    public Team(String name, ArrayList<Skater> players, ArrayList<Line> lines, ArrayList<Goalie> goalies, int wins,
                int losses, int lossesOT)
            throws NullPointerException, IllegalArgumentException {
        if (name == null || players == null || lines == null || goalies == null) {
            throw new NullPointerException("Given arguments cannot be null");
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name;
        this.players = players;
        this.lines = lines;
        this.goalies = goalies;
        if (wins < 0 || losses < 0 || lossesOT < 0) {
            throw new IllegalArgumentException("Win and loss totals cannot be negative");
        }
        this.wins = wins;
        this.losses = losses;
        this.otLosses = lossesOT;
    }

    // Creates shallow copy of the given team
    public Team(Team team) throws NullPointerException {
        if (team == null) {
            throw new NullPointerException("Given team cannot be null");
        }
        name = team.getName();
        wins = team.getWins();
        losses = team.getLosses();
        otLosses = team.getOtLosses();
        players = new ArrayList<>(List.of(team.getPlayers()));
        lines = new ArrayList<>(List.of(team.getLines()));
        goalies = new ArrayList<>(List.of(team.getGoalies()));
    }

    // Getter Methods

    public String getName() {
        return name;
    }

    public Skater[] getPlayers() {
        return players.toArray(new Skater[]{});
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
            throw new NullPointerException("Name cannot be null");
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name;
    }

    /**
     * @throws IllegalArgumentException If given stats are negative
     */
    public void setWins(int wins) {
        if (wins < 0) {
            throw new IllegalArgumentException("Wins value cannot be negative");
        }
        this.wins = wins;
    }

    /**
     * @throws IllegalArgumentException If given stats are negative
     */
    public void setLosses(int losses) {
        if (losses < 0) {
            throw new IllegalArgumentException("Losses value cannot be negative");
        }
        this.losses = losses;
    }

    /**
     * @throws IllegalArgumentException If given stats are negative
     */
    public void setOtLosses(int otLosses) {
        if (otLosses < 0) {
            throw new IllegalArgumentException("Overtime loss value cannot be negative");
        }
        this.otLosses = otLosses;
    }


    // Generates and returns a list containing every OffenseLine in the team
    public ArrayList<OffenseLine> getOffenseLines() {
        ArrayList<OffenseLine> result = new ArrayList<>();
        for (Line line: lines) {
            if (line instanceof OffenseLine offense) {
                result.add(offense);
            }
        }
        return result;
    }

    // Generates and returns a list containing every DefenseLine in the team
    public ArrayList<DefenseLine> getDefenseLines() {
        ArrayList<DefenseLine> result = new ArrayList<>();
        for (Line line: lines) {
            if (line instanceof DefenseLine de) {
                result.add(de);
            }
        }
        return result;
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
     * Adds given player to the team's ArrayList of players, ensures that list remains sorted by player number and that
     * no two players have the same number
     * @param player The player being added to the team
     * @return True if the player is added to the team, false if the team already has a player with that number
     */
    public int addPlayer(Skater player) {
        if (players.contains(player)) {
            return -1;
        }
        if (players.size() == 0) {
            players.add(player);
            return 0;
        }
        int low = 0;
        int high = players.size();
        while (true) {
            int i = (low + high) / 2;
            if (player.getPlayerNumber() < players.get(i).getPlayerNumber()) {
                if (i == 0) {
                    players.add(0, player);
                    return i;
                }
                if (player.getPlayerNumber() > players.get(i - 1).getPlayerNumber()) {
                    players.add(i, player);
                    return i;
                } else {
                    high = i;
                }
            } else {
                if (i == players.size() - 1) {
                    players.add(player);
                    return i + 1;
                }
                if (player.getPlayerNumber() < players.get(i + 1).getPlayerNumber()) {
                    players.add(i + 1, player);
                    return i + 1;
                } else {
                    low = i;
                }
            }
        }
    }

    /**
     * Similar to the addPlayer method, adds a goalie to the team, keeps the list sorted by player number, and ensures
     * that no two goalies have the same number
     * @param goalie The goalie being added to the team
     * @return True if the goalie is added, false if a goalie with the same player number already exists on the team
     */
    public int addGoalie(Goalie goalie) {
        if (goalies.contains(goalie)) {
            return -1;
        }
        if (goalies.size() == 0) {
            goalies.add(goalie);
            return 0;
        }
        int low = 0;
        int high = goalies.size();
        while (true) {
            int i = (low + high) / 2;
            if (goalie.getPlayerNumber() < goalies.get(i).getPlayerNumber()) {
                if (i == 0) {
                    goalies.add(0, goalie);
                    return i;
                }
                if (goalie.getPlayerNumber() > goalies.get(i - 1).getPlayerNumber()) {
                    goalies.add(i, goalie);
                    return i;
                } else {
                    high = i;
                }
            } else {
                if (i == goalies.size() - 1) {
                    goalies.add(goalie);
                    return i + 1;
                }
                if (goalie.getPlayerNumber() < goalies.get(i + i).getPlayerNumber()) {
                    goalies.add(i + 1, goalie);
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
    public int changePlayer(Skater oldPlayer, Skater newPlayer) {
        if (!newPlayer.equals(oldPlayer) && players.contains(newPlayer)) {
            return -1;
        }
        if (!players.remove(oldPlayer)) {
            return -2;
        }
        return addPlayer(newPlayer);
    }

    /**
     * Does functionality of changePlayer but for goalies
     * @param oldGoalie Goalie being changed
     * @param newGoalie New goalie being added
     * @return True if the change was made, false otherwise
     */
    public int changeGoalie(Goalie oldGoalie, Goalie newGoalie) {
        if (!newGoalie.equals(oldGoalie) && goalies.contains(newGoalie)) return -1;
        if (!goalies.remove(oldGoalie)) {
            return -2;
        }
        return addGoalie(newGoalie);
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
     * @param player Player removed from the list
     * @return Whether the removal was successful or not
     */
    public boolean removePlayer(Skater player) {
        return players.remove(player);
    }

    // Like removePLayer but for goalies
    public boolean removeGoalie(Goalie goalie) {
        return goalies.remove(goalie);
    }

    // Like removePlayer but for lines
    public boolean removeLine(Line line) {
        return lines.remove(line);
    }

    /**
     * Calculates team's overall face off percentage win rate by looking through the list of players for each center
     * and adding their stats to a running total.
     * @return The calculated percentage
     */
    public double getFaceOffPercent() {
        int totalFaceOffs = 0;
        int faceOffWins = 0;
        for (Skater player: players) {
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
        for (Skater player: players) {
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
        for (Skater player: players) {
            result.append(String.format("%s %d\n", player.getName(), player.getPlayerNumber()));
        }
        result.append("\nGoalies:\n");
        for (Goalie goalie: goalies) {
            result.append(String.format("%s %d\n", goalie.getName(), goalie.getPlayerNumber()));
        }
        return result.substring(0, result.length() - 1);
    }
    /**
     * @return A string containing every player listed on the team and some of their basic stats (points, number,
     * record, etc.)
     */
    public String generateRosterWithStats() {
        StringBuilder result = new StringBuilder(String.format("%s\nSkaters:\t|  #|Pos|  G|  A|Pts|+/-|\n", name));

        for (Skater player : players) {
            int index = player.getName().lastIndexOf(' ');
            if (index >= 0) {
                result.append(String.format("%.15s\t|%3d|", player.getName().substring(index),
                        player.getPlayerNumber()));
            } else {
                result.append(String.format("%.15s\t|%3d|", player.getName(), player.getPlayerNumber()));
            }
            switch (player.getPosition()) {
                case CENTER -> result.append("  C");
                case LEFT_WING -> result.append(" LW");
                case RIGHT_WING -> result.append(" RW");
                case LEFT_DEFENSE -> result.append(" LD");
                case RIGHT_DEFENSE -> result.append(" RD");
            }
            result.append(String.format("|%3d|%3d|%3d|%3d|\n", player.getGoals(), player.getAssists(),
                    player.getPoints(), player.getPlusMinus()));
        }
        result.append("\nGoalies:\t|  #|  W|  L|OT| GAA| Sv%|\n");
        for (Goalie goalie: goalies) {
            String lastName = goalie.getName().substring(goalie.getName().lastIndexOf(' '));
            result.append(String.format("%.15s\t|%3d|%3d|%3d|%3d|%4.2f|%.3f|\n", lastName,
                    goalie.getPlayerNumber(), goalie.getWins(), goalie.getLosses(), goalie.getOtLosses(),
                    goalie.getGAA(), goalie.getSavePercent()));
        }
        return result.substring(0, result.length() - 1);
    }

    /**
     * @return A 2D object array where each row corresponds to one skater, and each element corresponds to one of that
     * skater's stats to display in a JTable
     */
    public Object[][] generateSkaterRosterWithStats() {
        Object[][] result = new Object[players.size()][NUM_BASIC_STATS];
        for (int i = 0; i < players.size(); i++) {
            Skater currentPlayer = players.get(i);
            Object[] currentRow = result[i];
            currentRow[0] = currentPlayer.getName();
            currentRow[1] = currentPlayer.getPlayerNumber();
            switch (currentPlayer.getPosition()) {
                case CENTER -> currentRow[2] = "C";
                case LEFT_WING -> currentRow[2] = "LW";
                case RIGHT_WING -> currentRow[2] = "RW";
                case LEFT_DEFENSE -> currentRow[2] = "LD";
                case RIGHT_DEFENSE -> currentRow[2] = "RD";
            }
            currentRow[3] = currentPlayer.getGoals();
            currentRow[4] = currentPlayer.getAssists();
            currentRow[5] = currentPlayer.getPoints();
            currentRow[6] = currentPlayer.getPlusMinus();
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
            Goalie currentGoalie = goalies.get(i);
            Object[] currentRow = result[i];
            currentRow[0] = currentGoalie.getName();
            currentRow[1] = currentGoalie.getPlayerNumber();
            currentRow[2] = currentGoalie.getWins();
            currentRow[3] = currentGoalie.getLosses();
            currentRow[4] = currentGoalie.getOtLosses();
            currentRow[5] = String.format("%.3f", currentGoalie.getGAA());
            currentRow[6] = String.format("%.2f", currentGoalie.getSavePercent());
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
