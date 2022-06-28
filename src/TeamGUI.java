import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * TeamGUI
 *
 * This class contains the layout and design of the GUI Interface including the functionality for managing all teams
 * stored in the app and editing an individual team. Selecting a team contains a very small and simple frame with a few
 * different options while editing a team contains multiple different tabs for all the different ways to interact
 * with your team. It also contains the main method of the application.
 */
public class TeamGUI implements Runnable {
    // Read in from the stored file, user selects team from this list
    private static final ArrayList<Team> teams = new ArrayList<>();
    private Team team;  // Team that the user chooses to open or creates

    // Select GUI Strings/Components
    private static final String FILE_NAME = "TeamManagerData";  // Designated name for file storing data
    private static final String NEW_INFO = "Thank you for using Team Manager! Here are a few things to note as you get" +
            " started:\n" +
            "If this is your first time launching the application you will notice that the app comes preloaded with" +
            " a sample team. This is included as a way to ease yourself into the application.\n" +
            "Feel free to view or change it in any way you like, and use it to take some time to get used to how the " +
            "application works.\n" +
            "All data related to the teams that you create in this application is stored in a file called \"" + FILE_NAME
            + "\" which was automatically created when you first launched this application.\n" +
            "This file is not readable nor transferable between most devices. The file is automatically updated and " +
            "saved every time a change is made, and changes cannot be undone.\n" +
            "If the file is deleted or lost, when the application launches, a new file will be created, and there " +
            "will only be the sample team. There is no way to recover any data without the file.";

    // JComponents
    JFrame selectFrame;

    JComboBox<Team> teamSelection;
    JButton selectTeamButton;

    JLabel otherOptionsLabel;
    JButton createTeam;
    JButton deleteTeam;
    JButton restoreSample;

    JButton newUsers;

    // MainGUI Constants/Components
    // Reused String expressions
    private static final String NAME_STRING = "Enter name:";
    private static final String WINS_STRING = "Enter number of wins:";
    private static final String LOSSES_STRING = "Enter number of losses:";
    private static final String OT_STRING = "Enter number of overtime losses or ties:";
    private static final String EDIT_INSTRUCTIONS = "<html>Enter changes for the information that you would like to " +
            "change.<br>If a field is left blank, no changes will be made for the corresponding information.</html>";
    private static final String EDIT_LINE_INSTRUCTIONS = "<html>This tab will make changes to or delete any already " +
            "created lines.<br>Use the drop box at the top of the screen to select which line you would like to " +
            "change or delete.<br>" + EDIT_INSTRUCTIONS;
    private static final String RESET_STATS_WARNING = "<html>This button will reset all stats for your team and its" +
            " players to 0.<br>This action cannot be undone after the fact and their previous stats will be " +
            "lost.</html>";
    private static final String NUMBER_ERROR = "Please enter a number where prompted";
    private static final String FILE_ERROR = "There was an issue writing to the file. Please close the application " +
            "and try again.";
    private static final String EMPTY_INPUTS = "Please enter a value in at least one of the boxes";
    private static final String OFFENSE_LINE = "Offense Line";
    private static final String DEFENSE_LINE = "Defense Pair";
    private static final String PP_LINE = "Power Play Line";
    private static final String PK_LINE = "Penalty Kill Line";
    private static final String[] SKATER_STATS_COLUMNS = {"Skater Name", "Player #", "Position", "Goals", "Assists",
            "Points", "+/-"};
    private static final String[] GOALIE_STATS_COLUMNS = {"Goalie Name", "Player #", "Wins", "Losses", "OT L / Ties",
            "GAA", "SV%"};
    private static final String SELECT_LINE = "Please select a line to edit";

    // Numeric Constants
    private static final int ENTER_NAME_SIZE = 30;
    private static final int ENTER_STAT_SIZE = 5;
    private static final int NAME_COLUMN_WIDTH = 100;

    // JComponents

    JTabbedPane mainTabs;  // Tabs for each different menu of the program
    JLabel editInstructionsLabel;

    // Team Lists
    JLabel currentLineLabel;
    JComboBox<Line> lineOptions;
    JComboBox<Skater> skaterOptions;
    JComboBox<Center> centerOptions;
    JComboBox<Goalie> goalieOptions;

    // Manage Team

    JTabbedPane teamTabs;

    // Edit Team
    JLabel changeTeamNameLabel;
    JTextField changeTeamName;
    JLabel changeTeamWinsLabel;
    JTextField changeTeamWins;
    JLabel changeTeamLossesLabel;
    JTextField changeTeamLosses;
    JLabel changeTeamOTLabel;
    JTextField changeTeamOT;
    JButton updateTeamChanges;

    // View Roster
    JTextArea viewRoster;

    // View Roster with Basic Stats
    JTable viewRosterStatsSkaters;
    JTable viewRosterStatsGoalies;

    // View Stats
    JTextArea viewTeamStats;

    // Reset Stats
    JLabel resetStatsWarningLabel;
    JButton resetTeamStats;

    // Manage Lines

    JTabbedPane lineTabs;
    JComboBox<Skater> pickLeftWing;
    JComboBox<Skater> pickRightWing;
    JComboBox<Defenseman> pickLeftDe;
    JComboBox<Defenseman> pickRightDe;

    // Create Line
    JLabel enterLineNameLabel;
    JTextField lineName;
    JLabel lineTypeLabel;
    ButtonGroup lineTypeGroup;
    JRadioButton[] lineType;  // Offense, Defense, PP, PK
    JLabel selectCenterLabel;
    JLabel selectLWLabel;
    JLabel selectRWLabel;
    JLabel selectLDLabel;
    JLabel selectRDLabel;
    // For Special Teams
    JToggleButton enterStatsToggle;
    JLabel enterSuccessPercentageLabel;
    JTextField enterSuccessPercentage;
    JLabel enterNumOppsLabel;
    JTextField enterNumOpps;

    JButton createLine;

    // Edit/Delete Line
    JLabel editLineInstructions;
    JLabel changeLineNameLabel;
    JTextField changeLineName;
    JButton updateLineChanges;
    // For special teams
    JLabel changeSuccessPercentLabel;
    JTextField changeSuccessPercent;
    JLabel changeNumOppsLabel;
    JTextField changeNumOpps;

    JButton changeLinePlayers;
    JButton deleteLine;

    // View Lines
    JLabel viewLineLabel;
    JTextArea viewLine;

    // Create/Edit Skater/Goalie
    JTextArea playerName;
    JSlider playerNumber;

    // Manage Skaters

    JTabbedPane skaterTabs;

    // Create/Edit Skater
    JToggleButton chooseStickHand;
    JComboBox<Position> positionOptions;
    JToggleButton assignSkaterStats;
    JTextField enterGoals;
    JTextField enterAssists;
    JTextField enterPlusMinus;
    JTextField enterHits;
    JTextField enterPenaltyMinutes;
    JTextField enterShotsBlocked;
    JTextField enterFaceOffPercent;
    JTextField enterFaceOffTotal;

    JButton createPlayer;
    JButton editPlayer;

    // Delete Player
    JButton deletePlayer;

    // View Stats
    JTextArea viewSkaterStats;

    // Reset Stats
    JButton resetPlayerStats;

    // Manage Goalies

    JTabbedPane goalieTabs;

    // Create/Edit Goalie
    JToggleButton assignGoalieStats;
    JTextField enterSavePercentage;
    JTextField enterGoalieShotsAgainst;
    JTextField enterGoalieWins;
    JTextField enterGoalieLosses;
    JTextField enterGoalieOTLosses;
    JSlider enterShutouts;

    JButton createGoalie;
    JButton editGoalie;

    // Delete Goalie
    JButton deleteGoalie;

    // View Stats
    JTextArea viewGoalieStats;

    // Reset Stats
    JButton resetGoalieStats;

    // Enter Game Stats

    JTabbedPane enterStatsTabs;
    JToggleButton useOffenseDefenseLines;

    // Enter Live
    JButton goalLive;
    JButton shotBlockLive;
    JButton faceOffLive;
    JButton penaltyLive;
    JButton hitLive;
    JButton shotAgainstOnGoalLive;
    JButton scoredAgainstLive;
    JButton specialTeamsExpiredLive;
    JButton winLive;
    JButton lossLive;
    JButton tieLive;

    // Enter Post Game
    JTextField finalScoreTeam;
    JTextField finalScoreOpp;
    JSlider postGameShotsBlocked;
    JSlider postGameFaceOffs;
    JSlider postGameShotsAgainst;
    JSlider postGameHits;

    /**
     * Creates a sample team that is pre-generated for a user who has no file. Allows the user to get accustomed to the
     * app and its features before creating their own team if they so choose.
     * @return The generated sample team loosely based on data from the 2015 Chicago Blackhawks Regular Season.
     */
    public static Team createSample() {
        Team blackhawks = new Team("Sample Team: Chicago Blackhawks - 2014-15 Regular Season", 48, 28,
                6);
        blackhawks.addPlayer(new Skater("Patrick Kane", 88, "Left", Position.RIGHT_WING,
                27, 37, 10, 22, 10));
        blackhawks.addPlayer(new Skater("Kyle Baun", 39, "Right", Position.LEFT_WING,
                0, 0, -1, 12, 0));
        blackhawks.addPlayer(new Skater("Bryan Bickell", 29, "Left", Position.LEFT_WING,
                14, 14, 5, 205, 38));
        blackhawks.addGoalie(new Goalie("Corey Crawford", 50, .924, 1661,
                32, 20, 5, 2));
        blackhawks.addPlayer(new Center("Phillip Danault", 24, "Left", 0, 0,
                0, 3, 0, 30, 20));
        blackhawks.addGoalie(new Goalie("Scott Darling", 33, .936, 419, 9,
                4, 0, 1));
        blackhawks.addPlayer(new Center("Andrew Desjardins", 11, "Left", 0, 2,
                1, 32, 7, 63.2, 19));
        blackhawks.addPlayer(new Skater("Ryan Hartman", 38, "Right", Position.RIGHT_WING,
                0, 0, -1, 11, 2));
        blackhawks.addPlayer(new Defenseman("Niklas Hjalmarsson", 4, "Left",
                Position.LEFT_DEFENSE, 3, 16, 25, 39, 44, 127));
        blackhawks.addPlayer(new Skater("Marian Hossa", 81, "Left", Position.RIGHT_WING,
                22, 39, 17, 62, 32));
        blackhawks.addPlayer(new Defenseman("Duncan Keith", 2, "Left",
                Position.LEFT_DEFENSE, 10, 35, 12, 16, 20, 113));
        blackhawks.addPlayer(new Center("Markus Kruger", 22, "Left", 7, 10,
                -5, 29, 32, 53.3, 770));
        blackhawks.addPlayer(new Defenseman("Michal Rozsival", 32, "Right",
                Position.RIGHT_DEFENSE, 1, 12, 0, 103, 22, 87));
        blackhawks.addPlayer(new Defenseman("David Rundblad", 5, "Right",
                Position.RIGHT_DEFENSE, 3, 11, 17, 23, 12, 38));
        blackhawks.addPlayer(new Defenseman("Brent Seabrook", 7, "Right",
                Position.RIGHT_DEFENSE, 8, 23, -3, 135, 27, 141));
        blackhawks.addPlayer(new Center("Andrew Shaw", 65, "Right",
                15, 11, -8, 127, 67, 50.1, 712));
        blackhawks.addPlayer(new Skater("Teuvo Teravainen", 86, "Left", Position.LEFT_WING,
                4, 5, 4, 11, 2));
        blackhawks.addPlayer(new Center("Jonathan Toews", 19, "Left", 28, 38,
                30, 56, 36, 56.5, 1675));
        blackhawks.addPlayer(new Defenseman("Trevor van Riemsdyk", 57, "Right",
                Position.RIGHT_DEFENSE, 0, 1, 0, 4, 2, 15));
        blackhawks.addPlayer(new Skater("Brandon Saad", 20, "Left", Position.LEFT_WING,
                23, 29, 7, 53, 12));
        blackhawks.addPlayer(new Skater("Patrick Sharp", 10, "Right", Position.LEFT_WING,
                16, 27, -8, 74, 33));
        blackhawks.addPlayer(new Center("Brad Richards", 91, "Left", 12, 225,
                3, 74, 12, 48.4, 825));
        blackhawks.addPlayer(new Skater("Kris Versteeg", 23, "Right", Position.LEFT_WING,
                14, 20, 11, 31, 35));
        blackhawks.addPlayer(new Defenseman("Johnny Oduya", 27, "Left", Position.LEFT_DEFENSE,
                2, 8, 5, 69, 26, 123));
        blackhawks.addPlayer(new Skater("Ben Smith", 28, "Right", Position.RIGHT_WING,
                5, 4, -1, 49, 2));
        blackhawks.addPlayer(new Skater("Daniel Carcillo", 13, "Left", Position.LEFT_WING,
                4, 4, 3, 36, 54));
        blackhawks.addPlayer(new Center("Antoine Vermette", 80, "Left", 0, 3,
                -2, 9, 6, 50, 196));
        blackhawks.addPlayer(new Center("Joakim Nordstrom", 42, "Left", 0, 3,
                -5, 73, 4, 20, 10));
        blackhawks.addLine(new OffenseLine("Saad-Toews-Hossa", (Center) blackhawks.getPlayers()[7],
                blackhawks.getPlayers()[8], blackhawks.getPlayers()[22]));
        blackhawks.addLine(new OffenseLine("Kane-Richards-Versteeg", (Center) blackhawks.getPlayers()[25],
                blackhawks.getPlayers()[24], blackhawks.getPlayers()[10]));
        blackhawks.addLine(new OffenseLine("Nordstrom-Kruger-Smith", (Center) blackhawks.getPlayers()[9],
                blackhawks.getPlayers()[18], blackhawks.getPlayers()[13]));
        blackhawks.addLine(new OffenseLine("Hossa-Toews-Versteeg", (Center) blackhawks.getPlayers()[7],
                blackhawks.getPlayers()[22], blackhawks.getPlayers()[10]));
        blackhawks.addLine(new OffenseLine("Sharp-Richards-Kane", (Center) blackhawks.getPlayers()[25],
                blackhawks.getPlayers()[4], blackhawks.getPlayers()[24]));
        blackhawks.addLine(new OffenseLine("Sharp-Shaw-Bickell", (Center) blackhawks.getPlayers()[20],
                blackhawks.getPlayers()[4], blackhawks.getPlayers()[14]));
        blackhawks.addLine(new OffenseLine("Sharp-Toews-Hossa", (Center) blackhawks.getPlayers()[7],
                blackhawks.getPlayers()[4], blackhawks.getPlayers()[22]));
        blackhawks.addLine(new OffenseLine("Carcillo-Kruger-Smith", (Center) blackhawks.getPlayers()[9],
                blackhawks.getPlayers()[6], blackhawks.getPlayers()[13]));
        blackhawks.addLine(new OffenseLine("Teravainen-Shaw-Bickell", (Center) blackhawks.getPlayers()[20],
                blackhawks.getPlayers()[23], blackhawks.getPlayers()[14]));
        blackhawks.addLine(new OffenseLine("Carcillo-Shaw-Bickell", (Center) blackhawks.getPlayers()[20],
                blackhawks.getPlayers()[6], blackhawks.getPlayers()[14]));
        blackhawks.addLine(new DefenseLine("Hjalmarsson-Oduya", (Defenseman) blackhawks.getPlayers()[1],
                (Defenseman) blackhawks.getPlayers()[12]));
        blackhawks.addLine(new DefenseLine("Keith-Seabrook", (Defenseman) blackhawks.getPlayers()[0],
                (Defenseman) blackhawks.getPlayers()[3]));
        blackhawks.addLine(new DefenseLine("Keith-Rosival", (Defenseman) blackhawks.getPlayers()[0],
                (Defenseman) blackhawks.getPlayers()[15]));
        blackhawks.addLine(new DefenseLine("Hjalmarsson-Seabrook", (Defenseman) blackhawks.getPlayers()[1],
                (Defenseman) blackhawks.getPlayers()[3]));
        blackhawks.addLine(new DefenseLine("Keith-Hjalmarsson", (Defenseman) blackhawks.getPlayers()[0],
                (Defenseman) blackhawks.getPlayers()[1]));
        blackhawks.addLine(new DefenseLine("Keith-Rundblad", (Defenseman) blackhawks.getPlayers()[0],
                (Defenseman) blackhawks.getPlayers()[2]));
        blackhawks.addLine(new DefenseLine("Oduya-Seabrook", (Defenseman) blackhawks.getPlayers()[12],
                (Defenseman) blackhawks.getPlayers()[3]));
        blackhawks.addLine(new DefenseLine("Oduya-Rozsival", (Defenseman) blackhawks.getPlayers()[12],
                (Defenseman) blackhawks.getPlayers()[15]));
        blackhawks.addLine(new DefenseLine("Rozsival-Van Reimsdyk", (Defenseman) blackhawks.getPlayers()[15],
                (Defenseman) blackhawks.getPlayers()[19]));
        blackhawks.addLine(new PPLine("Power Play 1", (Center) blackhawks.getPlayers()[7],
                blackhawks.getPlayers()[25], blackhawks.getPlayers()[24],
                (Defenseman) blackhawks.getPlayers()[0], (Defenseman) blackhawks.getPlayers()[3],
                17.69, 130));
        blackhawks.addLine(new PPLine("Power Play 2", (Center) blackhawks.getPlayers()[9],
                blackhawks.getPlayers()[8], blackhawks.getPlayers()[22],
                (Defenseman) blackhawks.getPlayers()[1], (Defenseman) blackhawks.getPlayers()[15],
                17.69, 130));
        blackhawks.addLine(new PKLine("Penalty Kill 1", blackhawks.getPlayers()[7], blackhawks.getPlayers()[24],
                (Defenseman) blackhawks.getPlayers()[0], (Defenseman) blackhawks.getPlayers()[3],
                83.41, 106));
        blackhawks.addLine(new PKLine("Penalty Kill 2", blackhawks.getPlayers()[22],
                blackhawks.getPlayers()[14], (Defenseman) blackhawks.getPlayers()[1],
                (Defenseman) blackhawks.getPlayers()[12], 83.41, 105));
        return blackhawks;
    }

    /**
     * This method opens the file containing the stored teams if it exists. If it does not exist, it creates the file
     * and writes a sample team into the file. It will then read any teams that are stored into the file into the
     * teams instance variable.
     * @throws IOException If there is an issue opening the file or writing to the newly created file.
     * @throws ClassNotFoundException If there is an issue reading objects from the file
     * @throws Exception If there is an unexpected error with the file
     */
    public void openFile() throws IOException, ClassNotFoundException, Exception {
        File f = new File(FILE_NAME);
        if (!f.exists()) {
            if (!f.createNewFile()) throw new Exception("An unexpected error occurred - openFile 1");
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
            oos.writeInt(1);
            oos.writeObject(createSample());
            oos.flush();
            oos.close();
        }
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
        int size = ois.readInt();
        for (int i = 0; i < size; i++) {
            Object o = ois.readObject();
            if (o instanceof Team t) {
                teams.add(t);
            } else {
                throw new Exception("Unexpected error occurred while reading from the file - openFile 2");
            }
        }
        ois.close();
    }

    /**
     * Updates any changes made to the teams to the storage file
     * @throws IOException If there is an issue writing to the file
     * @throws Exception For unexpected errors with the file
     */
    public static void updateFile() throws IOException, Exception {
        File f = new File(FILE_NAME);
        if (!f.exists()) {
            if (!f.createNewFile()) throw new Exception("An unexpected error occurred - updateFile");
        }
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
        oos.writeInt(teams.size());
        for (Team team: teams) {
            oos.writeObject(team);
        }
        oos.flush();
        oos.close();
    }

    /**
     * Adds a team to the static list of teams while keeping the teams sorted in alphabetical/lexicographical order by
     * their team name
     * @param team Team being added
     */
    public static int addTeam(Team team) {
        if (teams.contains(team)) {
            return -1;
        }
        if (teams.size() == 0) {
            teams.add(team);
            return 0;
        }

        int low = 0;
        int high = teams.size();
        while (true) {
            int i = (low + high) / 2;
            if (team.getName().compareTo(teams.get(i).getName()) <= 0) {
                if (i == 0) {
                    teams.add(0, team);
                    return i;
                } else if (team.getName().compareTo(teams.get(i - 1).getName()) >= 0) {
                    teams.add(i, team);
                    return i;
                } else {
                    high = i;
                }
            } else {
                if (i == teams.size() - 1) {
                    teams.add(team);
                    return i + 1;
                } else if (team.getName().compareTo(teams.get(i + 1).getName()) <= 0) {
                    teams.add(i + 1, team);
                    return i + 1;
                } else {
                    low = i;
                }
            }
        }
    }

    /**
     * Replaces oldTeam (already in teams list) with newTeam. Returns -1 if newTeam shares the same name as
     * another team.
     * @param oldTeam The team being removed
     * @param newTeam The team being added
     * @return The index where the newTeam was placed or -1 if there was an issue
     */
    public static int changeTeam(Team oldTeam, Team newTeam) {
        if (!newTeam.equals(oldTeam) && teams.contains(newTeam)) {
            return -1;
        }
        teams.remove(oldTeam);
        return addTeam(newTeam);
    }

    /**
     * Creates a panel containing each of the given components, and adds it to the given container
     * @param components The JComponents being inserted into the new panel
     * @param container Container where the JPanel will go
     */
    private void createPanel(JComponent[] components, Container container) {
        JPanel panel = new JPanel();
        for (JComponent component : components) {
            panel.add(component);
        }
        container.add(panel);
    }

    /**
     * Creates a panel containing each of the given components, adds to given border layout container at the specified
     * location
     * @param components Components being added
     * @param container Container where the panel will be added
     * @param layoutString Border layout location specifier
     */
    private void createPanel(JComponent[] components, Container container, String layoutString) {
        JPanel panel = new JPanel();
        for (JComponent component: components) {
            panel.add(component);
        }
        container.add(panel, layoutString);
    }

    /**
     * Creates a panel containing each of the given components, and adds it to the given container at the specified
     * index
     * @param components The JComponents being inserted into the new panel
     * @param container Container where the JPanel will go
     * @param index Position where the panel will go in the container
     */
    private void createPanel(JComponent[] components, Container container, int index) {
        JPanel panel = new JPanel();
        for (JComponent component : components) {
            panel.add(component);
        }
        container.add(panel, index);
    }

    /**
     * This method can only be used to check responses that should be positive ints. If the string contains a response
     * attempts to enter the response into the appropriate index. If there is no response, leaves the index as -1.
     * @param stringInputs The inputs being checked.
     * @return An array of ints corresponding to the string inputs
     * @throws NumberFormatException If there is an issue parsing the string into ints
     */
    private static int[] checkForIntResponses(String[] stringInputs) throws NumberFormatException {
        int[] result = new int[stringInputs.length];
        for (int i = 0; i < stringInputs.length; i++) {
            if (!stringInputs[i].isBlank()) {
                result[i] = Integer.parseInt(stringInputs[i]);
            } else {
                result[i] = -1;
            }
        }
        return result;
    }

    /**
     * Updates JComponents such as labels and text areas with any changes to the team itself after they are made
     * For example, labels that display the teams record or name
     * @param index Index of the current team in the array, only given when team name has been changed
     *              If negative, no changes were made to the name
     */
    private void updateTeamComponents(Team oldTeam, Team newTeam, int index) {
        changeTeamWinsLabel.setText(WINS_STRING + " (Current: " + team.getWins() + ")");
        changeTeamLossesLabel.setText(LOSSES_STRING + " (Current: " + team.getLosses() + ")");
        changeTeamOTLabel.setText(OT_STRING + " (Current: " + team.getOtLosses() + ")");
        String statsString = viewTeamStats.getText();
        statsString = statsString.substring(statsString.indexOf('\n') + 1);
        statsString = statsString.substring(statsString.indexOf('\n') + 1);
        if (index >= 0) {
            changeTeamNameLabel.setText(NAME_STRING + " (Current: " + newTeam.getName() + ")");
            teamSelection.removeItem(oldTeam);
            teamSelection.insertItemAt(newTeam, index);
            viewTeamStats.setText(String.format("%s\nRecord: %d-%d-%d\n%s", newTeam.getName(), team.getWins(),
                    team.getLosses(), team.getOtLosses(), statsString));
            statsString = viewRoster.getText();
            viewRoster.setText(newTeam.getName() + statsString.substring(statsString.indexOf('\n')));
        } else {
            viewTeamStats.setText(String.format("%s\nRecord: %d-%d-%d\n%s", team.getName(), team.getWins(),
                    team.getLosses(), team.getOtLosses(), statsString));
        }
    }

    /**
     * Updates entirety of JComponents that display team info including roster stats. For use after reset team stats or
     * similar functions.
     */
    private void updateEntireTeamComponents() {
        changeTeamWinsLabel.setText(WINS_STRING + " (Current: " + team.getWins() + ")");
        changeTeamLossesLabel.setText(LOSSES_STRING + " (Current: " + team.getLosses() + ")");
        changeTeamOTLabel.setText(OT_STRING + " (Current: " + team.getOtLosses() + ")");
        viewTeamStats.setText(team.displayTeamStats());
        viewRosterStatsSkaters.setModel(new StatsTableModel(team.generateSkaterRosterWithStats(),
                SKATER_STATS_COLUMNS));
        viewRosterStatsGoalies.setModel(new StatsTableModel(team.generateGoalieRosterWithStats(),
                GOALIE_STATS_COLUMNS));
    }

    /**
     * @return An array of JRadioButtons that each correspond to a different subclass of lines for when the user is
     * creating a line
     */
    private JRadioButton[] initializeLineType() {
        return new JRadioButton[]{new JRadioButton(OFFENSE_LINE), new JRadioButton(DEFENSE_LINE),
                new JRadioButton(PP_LINE), new JRadioButton(PK_LINE)};
    }

    /**
     * This method sets up and displays the GUI for editing an actual team after one has been selected/created from
     * the selectTeam frame. It is composed of a variety of tabs that encompass the different ways to modify or manage
     * a team.
     */
    private void displayTeamGUI() {
        // Setup Frame
        JFrame mainFrame = new JFrame(team.getName());
        mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        mainFrame.setSize(new Dimension(screenSize.width * 2 / 3, screenSize.height * 2 / 3));
        Container mainContent = mainFrame.getContentPane();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));

        mainTabs = new JTabbedPane();
        mainContent.add(mainTabs);

        editInstructionsLabel = new JLabel(EDIT_INSTRUCTIONS);

        // Team Tab

        teamTabs = new JTabbedPane();

        // Edit team
        Container editTeam = new Container();
        editTeam.setLayout(new BoxLayout(editTeam, BoxLayout.Y_AXIS));

        // Labels, text fields, and button
        changeTeamNameLabel = new JLabel(NAME_STRING + " (Current: " + team.getName() + ")");
        changeTeamName = new JTextField(ENTER_NAME_SIZE);
        changeTeamWinsLabel = new JLabel(WINS_STRING + " (Current: " + team.getWins() + ")");
        changeTeamWins = new JTextField(ENTER_STAT_SIZE);
        changeTeamLossesLabel = new JLabel(LOSSES_STRING + " (Current: " + team.getLosses() + ")");
        changeTeamLosses = new JTextField(ENTER_STAT_SIZE);
        changeTeamOTLabel = new JLabel(OT_STRING + " (Current: " + team.getOtLosses() + ")");
        changeTeamOT = new JTextField(ENTER_STAT_SIZE);
        updateTeamChanges = new JButton("Update Changes");

        // Panels for each different instance variable (name, wins, losses, ot losses)
        createPanel(new JComponent[]{changeTeamNameLabel, changeTeamName}, editTeam);
        createPanel(new JComponent[]{changeTeamWinsLabel, changeTeamWins}, editTeam);
        createPanel(new JComponent[]{changeTeamLossesLabel, changeTeamLosses}, editTeam);
        createPanel(new JComponent[]{changeTeamOTLabel, changeTeamOT}, editTeam);

        // Instructions and Button
        createPanel(new JComponent[]{editInstructionsLabel}, editTeam);
        createPanel(new JComponent[]{updateTeamChanges}, editTeam);

        updateTeamChanges.addActionListener(e -> {
            // Get user inputs
            String name = changeTeamName.getText();
            String[] recordStrings = {changeTeamWins.getText(), changeTeamLosses.getText(), changeTeamOT.getText()};

            if (name.isBlank() && recordStrings[0].isBlank() && recordStrings[1].isBlank() &&
                    recordStrings[2].isBlank()) {
                JOptionPane.showMessageDialog(mainFrame, EMPTY_INPUTS,
                        "Edit Team", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            // Adjust record if necessary
            try {
                int[] record = checkForIntResponses(recordStrings);
                // Update Record stats
                if (record[0] != -1) {
                    team.setWins(record[0]);
                }
                if (record[1] != -1) {
                    team.setLosses(record[1]);
                }
                if (record[2] != -1) {
                    team.setOtLosses(record[2]);
                }
                // Clear text fields
                changeTeamWins.setText("");
                changeTeamLosses.setText("");
                changeTeamOT.setText("");
                updateFile();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(mainFrame, NUMBER_ERROR, "Edit Team", JOptionPane.ERROR_MESSAGE);
                return;
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(mainFrame, FILE_ERROR, "Edit Team", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "Edit Team", JOptionPane.ERROR_MESSAGE);
            }
            if (!name.isBlank()) {
                Team newTeam = new Team(team);
                newTeam.setName(name);
                changeTeamName.setText("");
                int index = TeamGUI.changeTeam(team, newTeam);
                updateTeamComponents(team, newTeam, index);
                if (index == -1) {
                    JOptionPane.showMessageDialog(mainFrame, "New name cannot be the same name as another team",
                            "Edit Team", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                team = newTeam;
                mainFrame.setTitle(team.getName());
                try {
                    updateFile();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(mainFrame, FILE_ERROR, "Edit Team", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "Edit Team",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                updateTeamComponents(null, null, -1);
            }
        });

        teamTabs.add("Edit Team", editTeam);

        // Text Area that displays the teams roster
        viewRoster = new JTextArea(team.generateRoster());
        viewRoster.setEditable(false);
        JScrollPane viewRosterScroll = new JScrollPane(viewRoster);
        teamTabs.add("View Roster", viewRosterScroll);

        // Table that displays slightly more detailed roster with basic stats
        Container viewRosterWithStatsContent = new Container();
        viewRosterWithStatsContent.setLayout(new BoxLayout(viewRosterWithStatsContent, BoxLayout.Y_AXIS));
        JScrollPane viewRosterWithStatsScroll = new JScrollPane(viewRosterWithStatsContent);

        viewRosterStatsSkaters = new JTable(new StatsTableModel(team.generateSkaterRosterWithStats(),
                SKATER_STATS_COLUMNS));
        viewRosterStatsSkaters.getColumnModel().getColumn(0).setPreferredWidth(NAME_COLUMN_WIDTH);
        viewRosterStatsGoalies = new JTable(new StatsTableModel(team.generateGoalieRosterWithStats(),
                GOALIE_STATS_COLUMNS));
        viewRosterStatsGoalies.getColumnModel().getColumn(0).setPreferredWidth(NAME_COLUMN_WIDTH);

        createPanel(new JComponent[]{viewRosterStatsSkaters.getTableHeader()}, viewRosterWithStatsContent);
        createPanel(new JComponent[]{viewRosterStatsSkaters}, viewRosterWithStatsContent);
        createPanel(new JComponent[]{viewRosterStatsGoalies.getTableHeader()}, viewRosterWithStatsContent);
        createPanel(new JComponent[]{viewRosterStatsGoalies}, viewRosterWithStatsContent);

        teamTabs.add("View Basic Player Stats", viewRosterWithStatsScroll);

        // Text Area that displays stats for the overall team
        viewTeamStats = new JTextArea(team.displayTeamStats());
        viewTeamStats.setEditable(false);
        JScrollPane viewTeamStatsScroll = new JScrollPane(viewTeamStats);
        teamTabs.add("View Team Stats", viewTeamStatsScroll);


        // Reset Team Stats
        resetStatsWarningLabel = new JLabel(RESET_STATS_WARNING);
        resetTeamStats = new JButton("Reset Team Stats");
        Container resetTeamContent = new Container();
        resetTeamContent.setLayout(new BoxLayout(resetTeamContent, BoxLayout.X_AXIS));
        createPanel(new JComponent[]{resetStatsWarningLabel}, resetTeamContent);
        createPanel(new JComponent[]{resetTeamStats}, resetTeamContent);

        // Resets stats for teams and updates relevant components
        resetTeamStats.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(mainFrame, "Are you sure that you want to reset " +
                    "stats for the entire team?", "Reset Team Stats", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                team.resetTeamStats();
                try {
                    updateFile();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(mainFrame, FILE_ERROR, "Reset Team Stats", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "Reset Team Stats",
                            JOptionPane.ERROR_MESSAGE);
                }
                updateEntireTeamComponents();
                mainFrame.repaint();
            }
        });

        teamTabs.add("Reset Team Stats", resetTeamContent);

        mainTabs.add("Manage Team", teamTabs);

        // Line Tabs

        Container mainLineContainer = new Container();
        mainLineContainer.setLayout(new BoxLayout(mainLineContainer, BoxLayout.Y_AXIS));
        lineTabs = new JTabbedPane();
        currentLineLabel = new JLabel("Selected Line:");
        lineOptions = new JComboBox<Line>();
        lineOptions.addItem(null);
        for (Line line: team.getLines()) {
            lineOptions.addItem(line);
        }

        createPanel(new JComponent[]{currentLineLabel, lineOptions}, mainLineContainer);
        createPanel(new JComponent[]{lineTabs}, mainLineContainer);

        JScrollPane lineContainerScroll = new JScrollPane(mainLineContainer);

        // Create Line

        Container createLineContent = new Container();
        createLineContent.setLayout(new BoxLayout(createLineContent, BoxLayout.Y_AXIS));

        enterLineNameLabel = new JLabel("Enter Line Name:");
        lineName = new JTextField(ENTER_NAME_SIZE);
        createPanel(new JComponent[]{enterLineNameLabel, lineName}, createLineContent);

        createLine = new JButton("Create Line");
        createPanel(new JComponent[]{createLine}, createLineContent);

        lineTypeLabel = new JLabel("Select Type of Line:");
        lineTypeGroup = new ButtonGroup();
        lineType = initializeLineType();
        for (JRadioButton button: lineType) {
            lineTypeGroup.add(button);
        }
        final boolean[] selectedLine = {false, false, false, false};
        createPanel(new JComponent[]{lineTypeLabel}, createLineContent);
        createPanel(lineType, createLineContent);

        // Set up Combo Boxes
        centerOptions = new JComboBox<>();
        centerOptions.addItem(null);
        for (Skater player: team.getPlayers()) {
            if (player instanceof Center center) {
                centerOptions.addItem(center);
            }
        }

        pickLeftWing = new JComboBox<>();
        pickRightWing = new JComboBox<>();
        pickLeftWing.addItem(null);
        pickRightWing.addItem(null);
        for (Skater player: team.getPlayers()) {
            pickLeftWing.addItem(player);
            pickRightWing.addItem(player);
        }

        pickLeftDe = new JComboBox<>();
        pickRightDe = new JComboBox<>();
        pickLeftDe.addItem(null);
        pickRightDe.addItem(null);
        for (Skater player: team.getPlayers()) {
            if (player instanceof Defenseman de) {
                pickLeftDe.addItem(de);
                pickRightDe.addItem(de);
            }
        }

        // Center
        JPanel selectCenterPanel = new JPanel();
        selectCenterLabel = new JLabel("Center:");
        selectCenterPanel.add(selectCenterLabel);
        selectCenterPanel.add(centerOptions);

        // Left Wing
        JPanel selectLWPanel = new JPanel();
        selectLWLabel = new JLabel("Left Wing:");
        selectLWPanel.add(selectLWLabel);
        selectLWPanel.add(pickLeftWing);

        // Right Wing
        JPanel selectRWPanel = new JPanel();
        selectRWLabel = new JLabel("Right Wing:");
        selectRWPanel.add(selectRWLabel);
        selectRWPanel.add(pickRightWing);

        // Left De
        JPanel selectLDPanel = new JPanel();
        selectLDLabel = new JLabel("Left Defense:");
        selectLDPanel.add(selectLDLabel);
        selectLDPanel.add(pickLeftDe);

        // Right De
        JPanel selectRDPanel = new JPanel();
        selectRDLabel = new JLabel("Right Defense:");
        selectRDPanel.add(selectRDLabel);
        selectRDPanel.add(pickRightDe);

        // Special Teams Stats
        JPanel statsTogglePanel = new JPanel();
        enterStatsToggle = new JToggleButton("Click to enter a starting Success %");
        statsTogglePanel.add(enterStatsToggle);

        JPanel enterSuccessPercentagePanel = new JPanel();
        enterSuccessPercentageLabel = new JLabel("Enter Success Percentage:");
        enterSuccessPercentage = new JTextField(ENTER_STAT_SIZE);
        enterSuccessPercentagePanel.add(enterSuccessPercentageLabel);
        enterSuccessPercentagePanel.add(enterSuccessPercentage);

        JPanel enterNumOppsPanel = new JPanel();
        enterNumOppsLabel = new JLabel("Enter Number of PP/PK Opportunities:");
        enterNumOpps = new JTextField(ENTER_STAT_SIZE);
        enterNumOppsPanel.add(enterNumOppsLabel);
        enterNumOppsPanel.add(enterNumOpps);

        // Displays text fields to enter success percentage and number of opportunities for the newly created line
        enterStatsToggle.addActionListener(e -> {
            if (enterStatsToggle.isSelected()) {
                createLineContent.add(enterSuccessPercentagePanel);
                createLineContent.add(enterNumOppsPanel);
            } else {
                createLineContent.remove(enterSuccessPercentagePanel);
                createLineContent.remove(enterNumOppsPanel);
            }
            mainFrame.repaint();
        });

        // Action Listener for the Radio Buttons (lineType)
        ActionListener lineTypeListener = e -> {
            if (e.getActionCommand().equals(OFFENSE_LINE)) {  // Set up Offense
                selectedLine[0] = true;
                createLineContent.add(selectCenterPanel);
                createLineContent.add(selectLWPanel);
                createLineContent.add(selectRWPanel);
            } else if (selectedLine[0]) {
                selectedLine[0] = false;
                createLineContent.remove(selectCenterPanel);
                createLineContent.remove(selectLWPanel);
                createLineContent.remove(selectRWPanel);
            }

            if (e.getActionCommand().equals(DEFENSE_LINE)) {  // Set up Defense
                selectedLine[1] = true;
                createLineContent.add(selectLDPanel);
                createLineContent.add(selectRDPanel);
            } else if (selectedLine[1]) {
                selectedLine[1] = false;
                createLineContent.remove(selectLDPanel);
                createLineContent.remove(selectRDPanel);
            }

            if (e.getActionCommand().equals(PP_LINE)) {  // Set up PP
                selectedLine[2] = true;
                createLineContent.add(selectCenterPanel);
                createLineContent.add(selectLWPanel);
                createLineContent.add(selectRWPanel);
                createLineContent.add(selectLDPanel);
                createLineContent.add(selectRDPanel);
                createLineContent.add(statsTogglePanel);
            } else if (selectedLine[2]) {
                selectedLine[2] = false;
                if (!selectedLine[0]) {
                    createLineContent.remove(selectCenterPanel);
                    createLineContent.remove(selectLWPanel);
                    createLineContent.remove(selectRWPanel);
                }
                if (!selectedLine[1]) {
                    createLineContent.remove(selectLDPanel);
                    createLineContent.remove(selectRDPanel);
                }
                createLineContent.remove(statsTogglePanel);
                if (enterStatsToggle.isSelected()) {
                    createLineContent.remove(enterSuccessPercentagePanel);
                    createLineContent.remove(enterNumOppsPanel);
                    enterStatsToggle.setSelected(false);
                }
            }

            if (e.getActionCommand().equals(PK_LINE)) {  // Set up PK
                selectedLine[3] = true;
                selectLWLabel.setText("Offense 1:");
                selectRWLabel.setText("Offense 2:");
                createLineContent.add(selectLWPanel);
                createLineContent.add(selectRWPanel);
                createLineContent.add(selectLDPanel);
                createLineContent.add(selectRDPanel);
                createLineContent.add(statsTogglePanel);
            } else if (selectedLine[3]) {
                selectedLine[3] = false;
                selectLWLabel.setText("Left Wing:");
                selectRWLabel.setText("Right Wing:");
                if (!selectedLine[2]) {
                    if (!selectedLine[0]) {
                        createLineContent.remove(selectLWPanel);
                        createLineContent.remove(selectRWPanel);
                    }
                    if (!selectedLine[1]) {
                        createLineContent.remove(selectLDPanel);
                        createLineContent.remove(selectRDPanel);
                    }
                    createLineContent.remove(statsTogglePanel);
                }
                if (enterStatsToggle.isSelected()) {
                    createLineContent.remove(enterSuccessPercentagePanel);
                    createLineContent.remove(enterNumOppsPanel);
                    enterStatsToggle.setSelected(false);
                }
            }
            mainFrame.repaint();
        };

        // Adding action listener to radio buttons for the different line types
        for (JRadioButton type : lineType) {
            type.addActionListener(lineTypeListener);
        }

        createLine.addActionListener(e -> {
            Line newLine = null;
            if (lineType[0].isSelected()) {  // Offense Line
                try {
                    newLine = new OffenseLine(lineName.getText(), (Center) centerOptions.getSelectedItem(),
                            (Skater) pickLeftWing.getSelectedItem(), (Skater) pickRightWing.getSelectedItem());
                    
                } catch (IllegalArgumentException | NullPointerException ex) {
                    JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "Create Line", 
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else if (lineType[1].isSelected()) {  // Defense Line
                try {
                    newLine = new DefenseLine(lineName.getText(), (Defenseman) pickLeftDe.getSelectedItem(),
                            (Defenseman) pickRightDe.getSelectedItem());
                } catch (IllegalArgumentException | NullPointerException ex) {
                    JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "Create Line", 
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else if (lineType[2].isSelected()) {  // PP Line
                try {
                    if (enterStatsToggle.isSelected()) {
                        double ppPercentage = Double.parseDouble(enterSuccessPercentage.getText());
                        int numOpps = Integer.parseInt(enterNumOpps.getText());
                        newLine = new PPLine(lineName.getText(), (Center) centerOptions.getSelectedItem(), 
                                (Skater) pickLeftWing.getSelectedItem(), (Skater) pickRightWing.getSelectedItem(),
                                (Defenseman) pickLeftDe.getSelectedItem(), (Defenseman) pickRightDe.getSelectedItem(),
                                ppPercentage, numOpps);
                    } else {
                        newLine = new PPLine(lineName.getText(), (Center) centerOptions.getSelectedItem(),
                                (Skater) pickLeftWing.getSelectedItem(), (Skater) pickRightWing.getSelectedItem(),
                                (Defenseman) pickLeftDe.getSelectedItem(), (Defenseman) pickRightDe.getSelectedItem());
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(mainFrame, NUMBER_ERROR, "Create Team", 
                            JOptionPane.ERROR_MESSAGE);
                    return;
                } catch (IllegalArgumentException | NullPointerException ex) {
                    JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "Create Team", 
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else if (lineType[3].isSelected()) {  // PK Line
                try {
                    if (enterStatsToggle.isSelected()) {
                        double pkPercentage = Double.parseDouble(enterSuccessPercentage.getText());
                        int numOpps = Integer.parseInt(enterNumOpps.getText());
                        newLine = new PKLine(lineName.getText(), (Skater) pickLeftWing.getSelectedItem(), 
                                (Skater) pickRightWing.getSelectedItem(), (Defenseman) pickLeftDe.getSelectedItem(),
                                (Defenseman) pickRightDe.getSelectedItem(), pkPercentage, numOpps);
                    } else {
                        newLine = new PKLine(lineName.getText(), (Skater) pickLeftWing.getSelectedItem(),
                                (Skater) pickRightWing.getSelectedItem(), (Defenseman) pickLeftDe.getSelectedItem(),
                                (Defenseman) pickRightDe.getSelectedItem());
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(mainFrame, NUMBER_ERROR, "Create Team",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                } catch (IllegalArgumentException | NullPointerException ex) {
                    JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "Create Team",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {  // No selection
                JOptionPane.showMessageDialog(mainFrame, "Please select a line type to assign players to " +
                        "your line", "Create Line", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int index = team.addLine(newLine);
            if (index == -1) {
                JOptionPane.showMessageDialog(mainFrame, "New Line cannot share the same name as another line",
                        "Create Line", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Reset Text Boxes
            lineName.setText("");
            enterSuccessPercentage.setText("");
            enterNumOpps.setText("");

            lineOptions.insertItemAt(newLine, index);
            try {
                updateFile();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(mainFrame, FILE_ERROR, "Create Line", JOptionPane.ERROR_MESSAGE);
                return;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "Create Line",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(mainFrame, "New line has successfully been created",
                    "Create Line", JOptionPane.INFORMATION_MESSAGE);
        });

        lineTabs.add("Create Line", createLineContent);

        // Edit Line

        Container editLineContent = new Container();
        editLineContent.setLayout(new BoxLayout(editLineContent, BoxLayout.Y_AXIS));

        editLineInstructions = new JLabel(EDIT_LINE_INSTRUCTIONS);
        createPanel(new JComponent[]{editLineInstructions}, editLineContent);

        // Change Name
        changeLineNameLabel = new JLabel("Enter New Line Name:");
        changeLineName = new JTextField(ENTER_NAME_SIZE);
        createPanel(new JComponent[]{changeLineNameLabel, changeLineName}, editLineContent);

        updateLineChanges = new JButton("Update Name");
        createPanel(new JComponent[]{updateLineChanges}, editLineContent);

        // Change Players
        changeLinePlayers = new JButton("Change Line Players");
        createPanel(new JComponent[]{changeLinePlayers}, editLineContent);

        // Delete Line
        deleteLine = new JButton("Delete Selected Line");
        createPanel(new JComponent[]{deleteLine}, editLineContent);

        // Change Special Teams Stats
        AtomicBoolean isSpecialTeams = new AtomicBoolean(false);
        JPanel changeSTSuccess = new JPanel();
        changeSuccessPercentLabel = new JLabel("Enter New Success Percentage:");
        changeSuccessPercent = new JTextField(ENTER_STAT_SIZE);
        changeNumOppsLabel = new JLabel("Enter Number of Opportunities:");
        changeNumOpps = new JTextField(ENTER_STAT_SIZE);
        changeSTSuccess.add(changeSuccessPercentLabel);
        changeSTSuccess.add(changeSuccessPercent);
        changeSTSuccess.add(changeNumOppsLabel);
        changeSTSuccess.add(changeNumOpps);

        // Ensures that proper components are displayed for editing a line depending on which line is selected
        lineOptions.addItemListener(e -> {
            if (lineOptions.getSelectedItem() instanceof SpecialTeamsLine && !isSpecialTeams.get()) {
                isSpecialTeams.set(true);
                updateLineChanges.setText("Update Changes");
                editLineContent.add(changeSTSuccess, editLineContent.getComponentCount() - 3);
            } else if (isSpecialTeams.get() && (lineOptions.getSelectedItem() instanceof OffenseLine ||
                    lineOptions.getSelectedItem() instanceof DefenseLine || lineOptions.getSelectedItem() == null) ) {
                updateLineChanges.setText("Update Name");
                editLineContent.remove(changeSTSuccess);
                isSpecialTeams.set(false);
            }
            if (lineOptions.getSelectedItem() == null) {
                viewLine.setText("");
            } else {
                Line line = (Line) lineOptions.getSelectedItem();
                viewLine.setText(line.lineRoster());
            }
        });

        updateLineChanges.addActionListener(e -> {
            if (lineOptions.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(mainFrame, SELECT_LINE, "Edit Line",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            String name = changeLineName.getText();
            Line editingLine = (Line) lineOptions.getSelectedItem();
            Line newLine = null;
            if (editingLine instanceof OffenseLine oLine) {
                try {
                    newLine = new OffenseLine(oLine);
                    newLine.setName(name);
                } catch (IllegalArgumentException | NullPointerException ex) {
                    JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "Edit Line",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else if (editingLine instanceof DefenseLine defenseLine) {
                try {
                    newLine = new DefenseLine(defenseLine);
                    newLine.setName(name);
                } catch (IllegalArgumentException | NullPointerException ex) {
                    JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "Edit Line",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else if (editingLine instanceof SpecialTeamsLine specialLine) {
                try {
                    if (changeSuccessPercent.getText().isBlank() && changeNumOpps.getText().isBlank()) {
                        if (specialLine instanceof PPLine pp) {
                            newLine = new PPLine(pp);
                            newLine.setName(name);
                        } else if (specialLine instanceof PKLine pk) {
                            newLine = new PKLine(pk);
                            newLine.setName(name);
                        }
                    } else {
                        double successPercent = Double.parseDouble(changeSuccessPercent.getText());
                        int numAttempts = Integer.parseInt(changeNumOpps.getText());
                        specialLine.setSuccessStats(successPercent, numAttempts);
                        changeSuccessPercent.setText("");
                        changeNumOpps.setText("");
                        if (name.isBlank()) {
                            try {
                                updateFile();
                                viewLine.setText(editingLine.lineRoster());
                                JOptionPane.showMessageDialog(mainFrame, "Line Updated Successfully",
                                        "Edit Line", JOptionPane.INFORMATION_MESSAGE);
                            } catch (IOException ex) {
                                JOptionPane.showMessageDialog(mainFrame, FILE_ERROR, "Edit Line",
                                        JOptionPane.ERROR_MESSAGE);
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "Edit Line",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                            return;
                        } else {
                            if (specialLine instanceof PPLine pp) {
                                newLine = new PPLine(pp);
                                newLine.setName(name);
                            } else if (specialLine instanceof PKLine pk) {
                                newLine = new PKLine(pk);
                                newLine.setName(name);
                            }
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(mainFrame, NUMBER_ERROR, "Edit Line",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                } catch (IllegalArgumentException | NullPointerException ex) {
                    JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "Edit Line",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            int index = team.changeLine(editingLine, newLine);
            if (index == -1) {
                JOptionPane.showMessageDialog(mainFrame, "New name cannot be the same as another line's name",
                        "Edit Line", JOptionPane.ERROR_MESSAGE);
                return;
            }
            lineOptions.removeItem(editingLine);
            lineOptions.insertItemAt(newLine, index + 1);
            try {
                updateFile();
                viewLine.setText(editingLine.lineRoster());
                changeLineName.setText("");
                lineOptions.setSelectedIndex(0);
                JOptionPane.showMessageDialog(mainFrame, "Line Updated Successfully", "Edit Line",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(mainFrame, FILE_ERROR, "Edit Line", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "Edit Line", JOptionPane.ERROR_MESSAGE);
            }
        });

        changeLinePlayers.addActionListener(e -> {
            if (lineOptions.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(mainFrame, SELECT_LINE, "Edit Line", JOptionPane.ERROR_MESSAGE);
                return;
            }
            mainFrame.setVisible(false);

            Line editingLine = (Line) lineOptions.getSelectedItem();
            JFrame playersWindow = new JFrame("Change Players:");
            playersWindow.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            Container playersWindowContent = playersWindow.getContentPane();
            playersWindowContent.setLayout(new BoxLayout(playersWindowContent, BoxLayout.Y_AXIS));
            playersWindow.setLocationRelativeTo(mainFrame);

            // Clears the selection in create line tab and removes any selected components
            lineTypeGroup.clearSelection();
            for (int i = 0; i < selectedLine.length; i++) {
                if (selectedLine[i]) {
                    selectedLine[i] = false;
                    switch (i) {
                        case 0 -> { // Offense Line
                            createLineContent.remove(selectCenterPanel);
                            createLineContent.remove(selectLWPanel);
                            createLineContent.remove(selectRWPanel);
                        }
                        case 1 -> { // De Line
                            createLineContent.remove(selectLDPanel);
                            createLineContent.remove(selectRDPanel);
                        }
                        case 2 -> { // PP Line
                            createLineContent.remove(selectCenterPanel);
                            createLineContent.remove(selectLWPanel);
                            createLineContent.remove(selectRWPanel);
                            createLineContent.remove(selectLDPanel);
                            createLineContent.remove(selectRDPanel);
                        }
                        case 3 -> { // PK Line
                            createLineContent.remove(selectLWPanel);
                            createLineContent.remove(selectRWPanel);
                            createLineContent.remove(selectLDPanel);
                            createLineContent.remove(selectRDPanel);
                        }
                    }
                    break;
                }
            }

            JTextArea displayCurrentPlayers = new JTextArea(editingLine.lineRoster());
            displayCurrentPlayers.setEditable(false);
            createPanel(new JComponent[]{displayCurrentPlayers}, playersWindowContent);

            if (editingLine instanceof OffenseLine) {
                playersWindowContent.add(selectCenterPanel);
                playersWindowContent.add(selectLWPanel);
                playersWindowContent.add(selectRWPanel);
            } else if (editingLine instanceof DefenseLine) {
                playersWindowContent.add(selectLDPanel);
                playersWindowContent.add(selectRDPanel);
            } else if (editingLine instanceof PPLine) {
                playersWindowContent.add(selectCenterPanel);
                playersWindowContent.add(selectLWPanel);
                playersWindowContent.add(selectRWPanel);
                playersWindowContent.add(selectLDPanel);
                playersWindowContent.add(selectRDPanel);
            } else if (editingLine instanceof PKLine) {
                playersWindowContent.add(selectLWPanel);
                playersWindowContent.add(selectRWPanel);
                playersWindowContent.add(selectLDPanel);
                playersWindowContent.add(selectRDPanel);
                selectLWLabel.setText("Offense 1:");
                selectRWLabel.setText("Offense 2:");
            }

            JButton assignPlayers = new JButton("Assign These Players");
            createPanel(new JComponent[]{assignPlayers}, playersWindowContent);

            playersWindow.pack();
            playersWindow.setVisible(true);

            assignPlayers.addActionListener(e1 -> {
                boolean change = false;
                try {
                    if (editingLine instanceof OffenseLine oLine) {
                        if (centerOptions.getSelectedItem() != null) {
                            oLine.setCenter((Center) centerOptions.getSelectedItem());
                            change = true;
                        }
                        if (pickLeftWing.getSelectedItem() != null) {
                            oLine.setLeftWing((Skater) pickLeftWing.getSelectedItem());
                            change = true;
                        }
                        if (pickRightWing.getSelectedItem() != null) {
                            oLine.setRightWing((Skater) pickRightWing.getSelectedItem());
                            change = true;
                        }
                    } else if (editingLine instanceof DefenseLine dLine) {
                        if (pickLeftDe.getSelectedItem() != null) {
                            dLine.setLeftDe((Defenseman) pickLeftDe.getSelectedItem());
                            change = true;
                        }
                        if (pickRightDe.getSelectedItem() != null) {
                            dLine.setRightDe((Defenseman) pickRightDe.getSelectedItem());
                            change = true;
                        }
                    } else if (editingLine instanceof PPLine ppLine) {
                        if (centerOptions.getSelectedItem() != null) {
                            ppLine.setCenter((Center) centerOptions.getSelectedItem());
                            change = true;
                        }
                        if (pickLeftWing.getSelectedItem() != null) {
                            ppLine.setLeftWing((Skater) pickLeftWing.getSelectedItem());
                            change = true;
                        }
                        if (pickRightWing.getSelectedItem() != null) {
                            ppLine.setRightWing((Skater) pickRightWing.getSelectedItem());
                            change = true;
                        }
                        if (pickLeftDe.getSelectedItem() != null) {
                            ppLine.setLeftDe((Defenseman) pickLeftDe.getSelectedItem());
                            change = true;
                        }
                        if (pickRightDe.getSelectedItem() != null) {
                            ppLine.setRightDe((Defenseman) pickRightDe.getSelectedItem());
                            change = true;
                        }
                    } else if (editingLine instanceof PKLine pkLine) {
                        if (pickLeftWing.getSelectedItem() != null) {
                            pkLine.setOffense1((Skater) pickLeftWing.getSelectedItem());
                            change = true;
                        }
                        if (pickRightWing.getSelectedItem() != null) {
                            pkLine.setOffense2((Skater) pickRightWing.getSelectedItem());
                            change = true;
                        }
                    }
                } catch (IllegalArgumentException | NullPointerException ex) {
                    JOptionPane.showMessageDialog(playersWindow, ex.getMessage(), "Edit Line",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (change) {
                    try {
                        updateFile();
                        viewLine.setText(editingLine.lineRoster());
                        JOptionPane.showMessageDialog(playersWindow, "Players successfully updated",
                                "Edit Line", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(mainFrame, FILE_ERROR, "Edit Line",
                                JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "Edit Line",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    playersWindow.dispose();
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "Please select new players for the line or close" +
                            " the window.", "Edit Line", JOptionPane.ERROR_MESSAGE);
                }
            });

            // Redisplay main JFrame when the window is closed
            playersWindow.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    mainFrame.setVisible(true);
                }
            });
        });

        // Asks user to confirm choice, then deletes selected line from team and ComboBox
        deleteLine.addActionListener(e -> {
            Line removingLine = (Line) lineOptions.getSelectedItem();
            if (removingLine == null) {
                JOptionPane.showMessageDialog(mainFrame, "Please select a line to delete", "Delete Line",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int response = JOptionPane.showConfirmDialog(mainFrame, "Are you sure you would like to delete " +
                    removingLine.getName() + "?", "Delete Line", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                if (team.removeLine(removingLine)) {
                    lineOptions.removeItem(removingLine);
                    try {
                        updateFile();
                        JOptionPane.showMessageDialog(mainFrame, "Line successfully deleted", "Delete Line",
                                JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(mainFrame, FILE_ERROR, "Edit Line",
                                JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "Edit Line",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "There was an issue deleting the selected line.",
                            "Delete Line", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        lineTabs.add("Edit or Delete Lines", editLineContent);

        // View Lines

        Container viewLinesContent = new Container();
        viewLinesContent.setLayout(new BoxLayout(viewLinesContent, BoxLayout.Y_AXIS));
        viewLineLabel = new JLabel("Select a line above to view its assigned players and relevant stats here");
        viewLine = new JTextArea();
        viewLine.setEditable(false);

        createPanel(new JComponent[]{viewLineLabel}, viewLinesContent);
        createPanel(new JComponent[]{viewLine}, viewLinesContent);

        lineTabs.add("View Lines", viewLinesContent);

        mainTabs.add("Manage Lines", lineContainerScroll);

        // Sets team to null and re displays SelectTeamGUI when window is closed
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                team = null;
                selectFrame.setVisible(true);
            }
        });
        mainFrame.setVisible(true);
    }

    @Override
    public void run() {
        // Sets up the teams array with its data, displays an error message if there are issues with reading the files
        try {
            openFile();
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "There was an issue reading from the file. " +
                    "Please try again.", "File Error", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "File Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Setting up frame
        selectFrame = new JFrame("Welcome to Team Manager");
        Container selectContent = selectFrame.getContentPane();
        selectContent.setLayout(new BoxLayout(selectContent, BoxLayout.Y_AXIS));
        selectFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        selectFrame.setVisible(true);

        // Top of frame, selecting an already created team
        // Combo Box to select your team
        teamSelection = new JComboBox<Team>();
        for (Team team: teams) {
            teamSelection.addItem(team);
        }
        // Button to confirm selection
        selectTeamButton = new JButton("Open this team");
        JComponent[] selectTeamComponents = {teamSelection, selectTeamButton};
        createPanel(selectTeamComponents, selectContent);

        // Middle of frame that contains other options with teams
        otherOptionsLabel = new JLabel("Additional Options: ");
        createTeam = new JButton("Create New Team");
        deleteTeam = new JButton("Delete Team");
        restoreSample = new JButton("Restore Sample Team");

        createPanel(new JComponent[]{otherOptionsLabel, createTeam, deleteTeam, restoreSample}, selectContent);

        // Bottom of Frame, button where new users can find additional guidance
        newUsers = new JButton("Help and Guidance for New Users");
        createPanel(new JComponent[]{newUsers}, selectContent);

        selectFrame.pack();
        selectFrame.setLocationRelativeTo(null);

        /*
          Sets the selected team instance var to the selected team from the combo box and closes the frame. If no
          selection is made (o is null/not a team) an error message will display and the frame will remain open.
         */
        selectTeamButton.addActionListener(e -> {
            Object o = teamSelection.getSelectedItem();
            if (o instanceof Team t) {
                team = t;
                selectFrame.setVisible(false);
                displayTeamGUI();
            } else {
                JOptionPane.showMessageDialog(selectFrame, "Please ensure that you have selected a team in " +
                                "the box. If there are no options to select from, please create a new team.",
                        "Team Select", JOptionPane.ERROR_MESSAGE);
            }
        });

        /*
          Prompts the user through creating a new team. They can either provide only a name and have the record set to
          0, or they can input the record themselves. After the team is created, that will become the chosen team and
          the GUI for that team will open, and the data file will be updated with the new team.
         */
        createTeam.addActionListener(e -> {
            int input = JOptionPane.showConfirmDialog(selectFrame, "Would you like to provide a win/loss record for" +
                    " this team?", "Create Team", JOptionPane.YES_NO_CANCEL_OPTION);
            switch (input) {
                case JOptionPane.YES_OPTION -> { // Prompt for wins, losses, ot losses
                    while (true) {
                        try {
                            // Entering information for new team
                            String name = JOptionPane.showInputDialog(selectFrame, "Enter Team Name:",
                                    "Create Team", JOptionPane.QUESTION_MESSAGE);
                            int wins = Integer.parseInt(JOptionPane.showInputDialog(selectFrame,
                                    "Enter number of wins:", "Create Team", JOptionPane.QUESTION_MESSAGE));
                            int losses = Integer.parseInt(JOptionPane.showInputDialog(selectFrame,
                                    "Enter number of losses:", "Create Team",
                                    JOptionPane.QUESTION_MESSAGE));
                            int otLosses = Integer.parseInt(JOptionPane.showInputDialog(selectFrame,
                                    "Enter number of overtime losses or ties:", "Create Team",
                                    JOptionPane.QUESTION_MESSAGE));
                            Team newTeam = new Team(name, wins, losses, otLosses);

                            int index = addTeam(newTeam);
                            if (index == -1) {
                                JOptionPane.showMessageDialog(selectFrame, "Two teams cannot have the same name",
                                        "Create Team", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            teamSelection.insertItemAt(newTeam, index);
                            updateFile();

                            // Set selected team and hide frame
                            team = newTeam;
                            selectFrame.setVisible(false);
                            displayTeamGUI();
                            break;
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(selectFrame, "Please enter a number when prompted",
                                    "Create Team", JOptionPane.ERROR_MESSAGE);
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(selectFrame, "There was an issue writing to the file. " +
                                    "Please try again", "Create Team", JOptionPane.ERROR_MESSAGE);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(selectFrame, ex.getMessage(), "Create Team",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
                case JOptionPane.NO_OPTION -> {  // Similar to yes, but only prompt for name
                    while (true) {
                        try {
                            String name = JOptionPane.showInputDialog(selectFrame, "Enter team name:",
                                    "Create Team", JOptionPane.QUESTION_MESSAGE);

                            Team newTeam = new Team(name);

                            int index = addTeam(newTeam);
                            if (index == -1) {
                                JOptionPane.showMessageDialog(selectFrame, "Two teams cannot have the same name",
                                        "Create Team", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            updateFile();

                            team = newTeam;
                            teamSelection.insertItemAt(team, index);
                            selectFrame.setVisible(false);
                            displayTeamGUI();
                            break;
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(selectFrame, ex.getMessage(), "Create Team",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });

        /*
          Opens a new JFrame that allows the user to delete one of the created teams from the list. The frame will only
          contain a combo box listing the teams and a button to confirm the selection.
         */
        deleteTeam.addActionListener(e -> {
            if (teams.isEmpty()) {
                JOptionPane.showMessageDialog(selectFrame, "There are no teams to delete.",
                        "Delete Team", JOptionPane.ERROR_MESSAGE);
                return;
            }
            selectFrame.setVisible(false);

            // New Frame
            JFrame deleteFrame = new JFrame("Delete Team");
            deleteFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            Container deleteContent = deleteFrame.getContentPane();
            deleteContent.setLayout(new BorderLayout());

            deleteContent.add(teamSelection, BorderLayout.CENTER);
            JButton deleteButton = new JButton("Delete Selected Team");
            deleteContent.add(deleteButton, BorderLayout.SOUTH);

            deleteFrame.pack();
            deleteFrame.setLocationRelativeTo(selectFrame);
            deleteFrame.setVisible(true);

            /*
              Asks the user to confirm their choice before actually deleting the team. After user has given
              confirmation, removes the selected team from the static variable, the combo box, and updates this change
              in the data file.
             */
            deleteButton.addActionListener(e1 -> {
                Object o = teamSelection.getSelectedItem();
                if (o instanceof Team t) {
                    int finalCheck = JOptionPane.showConfirmDialog(selectFrame, "WARNING: Are you sure you want to " +
                                    "delete this team (" + t.getName() + ")? This cannot be undone.",
                            "Delete Team", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (finalCheck == JOptionPane.YES_OPTION) {
                        teams.remove(t);
                        teamSelection.removeItemAt(teamSelection.getSelectedIndex());
                        try {
                            updateFile();
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(selectFrame, ex.getMessage(), "File Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                        deleteFrame.dispose();
                    }
                }
            });

            // Sets the original frame back to visible and ensures that every component is back in its original place
            deleteFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    selectContent.remove(0);
                    createPanel(selectTeamComponents, selectContent, 0);
                    selectFrame.setVisible(true);
                }
            });
        });

        // Adds sample team back into the list if the user deleted it but wants it back
        restoreSample.addActionListener(e -> {
            Team sample = createSample();
            int index = addTeam(sample);
            if (index == -1) {
                JOptionPane.showMessageDialog(selectFrame, "The sample team is still in your list",
                        "Restore Sample", JOptionPane.ERROR_MESSAGE);
                return;
            }
            teamSelection.insertItemAt(sample, index);
            try {
                updateFile();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(selectFrame, FILE_ERROR, "Restore Sample", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(selectFrame, ex.getMessage(), "Restore Sample",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        newUsers.addActionListener(e -> JOptionPane.showMessageDialog(selectFrame, NEW_INFO, "New User Information",
                JOptionPane.INFORMATION_MESSAGE));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new TeamGUI());
    }
}

/**
 * StatsTableModel
 *
 * This class serves as the model for the JTables used to display basic player stats for the entire team. It displays
 * data from a 2D Object array containing Strings, ints, and doubles corresponding to a player's stats.
 * It is a very simple implementation of the AbstractTableModel because the table is for viewing only.
 */
class StatsTableModel extends AbstractTableModel {
    private final String[] columnNames;
    private final Object[][] stats;

    public StatsTableModel(Object[][] stats, String[] columnNames) {
        this.stats = stats;
        this.columnNames = columnNames;
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return stats.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return stats[rowIndex][columnIndex];
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}
