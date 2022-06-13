import java.io.Serializable;
import java.util.ArrayList;

/**
 * Team
 * Class used to store information and methods related to a hockey team including its players, lines, and stats
 */
public class Team implements Serializable {
    private String name;
    private ArrayList<Skater> players;
    private ArrayList<Line> lines;
    private ArrayList<Goalie> goalies;
    private int wins;
    private int losses;
    private int lossesOT;

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
        this.lossesOT = 0;
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
        this.lossesOT = lossesOT;
    }

    /**
     * Initializes team with given arguments assigned to their respective instance variables
     * @throws NullPointerException Thrown if given name or lists are null
     * @throws IllegalArgumentException Thrown if name is blank or given stats are negative
     */
    public Team(String name, ArrayList<Skater> players, ArrayList<Line> lines, ArrayList<Goalie> goalies, int wins,
                int losses, int lossesOT) throws NullPointerException, IllegalArgumentException {
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
        this.lossesOT = lossesOT;
    }

    public String getName() {
        return name;
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

    // Getter Methods

    public ArrayList<Skater> getPlayers() {
        return players;
    }

    public ArrayList<Line> getLines() {
        return lines;
    }

    public ArrayList<Goalie> getGoalies() {
        return goalies;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getLossesOT() {
        return lossesOT;
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

    // Generates and returns a list containing every special teams line in the team
    public ArrayList<Line> getSpecialTeams() {
        ArrayList<Line> result = new ArrayList<>();
        for (Line line: lines) {
            if (line instanceof PPLine || line instanceof PKLine) {
                result.add(line);
            }
        }
        return result;
    }

    /**
     * @throws IllegalArgumentException If given stats are negative
     */
    public void setRecord(int wins, int losses, int lossesOT) throws IllegalArgumentException {
        if (wins < 0 || losses < 0 || lossesOT < 0) {
            throw new IllegalArgumentException("Record cannot have negative values");
        }
        this.wins = wins;
        this.losses = losses;
        this.lossesOT = lossesOT;
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
        lossesOT++;
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
                    return i;
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
                    return i;
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
                    return i;
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
        if (!oldPlayer.equals(newPlayer) && players.contains(newPlayer)) return -1;
        if (!players.remove(oldPlayer)) {
            return -1;
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
            return -1;
        }
        return addGoalie(newGoalie);
    }

    // Edit a line on the list
    public boolean changeLine(Line oldLine, Line newLine) {
        if (!lines.remove(oldLine)) {
            return false;
        }
        addLine(newLine);
        return true;
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
    public double getFaceoffPercent() {
        int totalFaceoffs = 0;
        int faceoffWins = 0;
        for (Skater player: players) {
            if (player instanceof Center center) {
                totalFaceoffs += center.getFaceoffTotal();
                faceoffWins += center.getFaceoffWins();
            }
        }
        if (totalFaceoffs == 0) {
            return 0;
        }
        return ((double) faceoffWins / totalFaceoffs) * 100;
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
            if (line instanceof PKLine) {
                tryTotal += ((PKLine) line).getNumberAttempts();
                successTotal += ((PKLine) line).getNumberKilled();
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
            if (line instanceof PPLine) {
                tryTotal += ((PPLine) line).getNumberOpps();
                successTotal += ((PPLine) line).getNumberScored();
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
        int matches = wins + losses + lossesOT;
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
        lossesOT = 0;
        for (Skater player: players) {
            player.resetStats();
        }
        for (Goalie goalie: goalies) {
            goalie.resetStats();
        }
        for (Line line: lines) {
            if (line instanceof PKLine pk) {
                pk.setKillStatsWithPercentage(0, 0);
            }
            if (line instanceof PPLine pp) {
                pp.setStatsWithPercentage(0, 0);
            }
        }
    }

    /**
     * @return A string containing every player listed on the team
     */
    public String generateRoster() {
        String result = String.format("%s\nSkaters:\n", name);

        for (Skater player : players) {
            result += String.format("%s %d\n", player.getName(), player.getPlayerNumber());
        }
        result += "Goalies:\n";
        for (Goalie goalie: goalies) {
            result += String.format("%s %d\n", goalie.getName(), goalie.getPlayerNumber());
        }
        return result.substring(0, result.length() - 1);
    }

    /**
     * @return A formatted String containing the team's overall stats
     */
    public String displayTeamStats() {
        return String.format("%s\nRecord: %d-%d-%d\n" +
                "Face Off %%: %.2f\n" +
                "Power Play %%: %.2f\n" +
                "Penalty Kill %%: %.2f\n" +
                "Average Shots Against Per Game: %.2f", name, wins, losses, lossesOT, getFaceoffPercent(),
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
