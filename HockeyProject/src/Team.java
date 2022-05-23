import java.util.ArrayList;

public class Team {
    private String name;
    private ArrayList<Skater> players;
    private ArrayList<Line> lines;
    private ArrayList<Goalie> goalies;
    private int wins;
    private int losses;
    private int lossesOT;

    public Team(String name) {
        if (name == null) {
            throw new NullPointerException("Name cannot be null");
        }
        if (name.isBlank()) {
            throw new NullPointerException("Name cannot be empty");
        }
        this.name = name;
        this.players = new ArrayList<>();
        this.lines = new ArrayList<>();
        this.goalies = new ArrayList<>();
        this.wins = 0;
        this.losses = 0;
        this.lossesOT = 0;
    }

    public Team(String name, int wins, int losses, int lossesOT) {
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

    public Team(String name, ArrayList<Skater> players, ArrayList<Line> lines, ArrayList<Goalie> goalies, int wins,
                int losses, int lossesOT) {
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

    public void setName(String name) throws NullPointerException, IllegalArgumentException {
        if (name == null) {
            throw new NullPointerException("Name cannot be null");
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name;
    }

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

    public void setRecord(int wins, int losses, int lossesOT) {
        if (wins < 0 || losses < 0 || lossesOT < 0) {
            throw new IllegalArgumentException("Record cannot have negative values");
        }
        this.wins = wins;
        this.losses = losses;
        this.lossesOT = lossesOT;
    }

    public void win() {
        wins++;
    }

    public void lose() {
        losses++;
    }

    public void tie() {
        lossesOT++;
    }

    public boolean addPlayer(Skater player) {
        if (players.contains(player)) {
            return false;
        }
        if (players.size() == 0) {
            players.add(player);
            return true;
        }
        int low = 0;
        int high = players.size();
        while (true) {
            int i = (low + high) / 2;
            if (player.getPlayerNumber() < players.get(i).getPlayerNumber()) {
                if (i == 0) {
                    players.add(0, player);
                    return true;
                }
                if (player.getPlayerNumber() > players.get(i - 1).getPlayerNumber()) {
                    players.add(i, player);
                    return true;
                } else {
                    high = i;
                }
            } else {
                if (i == players.size() - 1) {
                    players.add(player);
                    return true;
                }
                if (player.getPlayerNumber() < players.get(i + i).getPlayerNumber()) {
                    players.add(i + 1, player);
                    return true;
                } else {
                    low = i;
                }
            }
        }
    }

    public boolean addGoalie(Goalie goalie) {
        if (goalies.contains(goalie)) {
            return false;
        }
        if (goalies.size() == 0) {
            goalies.add(goalie);
            return true;
        }
        int low = 0;
        int high = goalies.size();
        while (true) {
            int i = (low + high) / 2;
            if (goalie.getPlayerNumber() < goalies.get(i).getPlayerNumber()) {
                if (i == 0) {
                    goalies.add(0, goalie);
                    return true;
                }
                if (goalie.getPlayerNumber() > goalies.get(i - 1).getPlayerNumber()) {
                    goalies.add(i, goalie);
                    return true;
                } else {
                    high = i;
                }
            } else {
                if (i == goalies.size() - 1) {
                    goalies.add(goalie);
                    return true;
                }
                if (goalie.getPlayerNumber() < goalies.get(i + i).getPlayerNumber()) {
                    goalies.add(i + 1, goalie);
                    return true;
                } else {
                    low = i;
                }
            }
        }
    }

    public void addLine(Line line) {
        lines.add(line);
    }

    public boolean changePlayer(Skater oldPlayer, Skater newPlayer) {
        if (!oldPlayer.equals(newPlayer) && players.contains(newPlayer)) return false;
        if (!players.remove(oldPlayer)) {
            return false;
        }
        return addPlayer(newPlayer);
    }

    public boolean changeGoalie(Goalie oldGoalie, Goalie newGoalie) {
        if (!newGoalie.equals(oldGoalie) && goalies.contains(newGoalie)) return false;
        if (!goalies.remove(oldGoalie)) {
            return false;
        }
        return addGoalie(newGoalie);
    }

    public boolean changeLine(Line oldLine, Line newLine) {
        if (!lines.remove(oldLine)) {
            return false;
        }
        lines.add(newLine);
        return true;
    }

    public boolean removePlayer(Skater player) {
        return players.remove(player);
    }

    public boolean removeGoalie(Goalie goalie) {
        return goalies.remove(goalie);
    }

    public boolean removeLine(Line line) {
        return lines.remove(line);
    }

    public double getPKSuccess() {
        int lineTotal = 0;
        double rateTotal = 0;
        for (Line line: lines) {
            if (line instanceof PKLine) {
                lineTotal++;
                rateTotal += ((PKLine) line).getKillPercent();
            }
        }
        if (lineTotal == 0) {
            return -1;
        }
        return rateTotal / lineTotal;
    }

    public double getPPSuccess() {
        int lineTotal = 0;
        double rateTotal = 0;
        for (Line line: lines) {
            if (line instanceof PPLine) {
                lineTotal++;
                rateTotal += ((PPLine) line).getSuccessRate();
            }
        }
        if (lineTotal == 0) {
            return -1;
        }
        return rateTotal / lineTotal;
    }

    public String toString() {
        String result = String.format("%s\n", name);
        for (Skater player : players) {
            result += String.format("%d %s\n", player.getPlayerNumber(), player.getName());
        }
        return result.substring(0, result.length() - 1);
    }
}
