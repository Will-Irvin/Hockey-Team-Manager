import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.*;

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
            "If the file is deleted or lost, when the application launches, a new file will be created, and it " +
            "will only contain the sample team. There is no way to recover any data without the file.";

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
    // TODO
    private static final String EDIT_TEAM = "Edit Team";
    private static final String RESET_TEAM_STATS = "Reset Team Stats";
    private static final String CREATE_LINE = "Create Line";
    private static final String CENTER = "Center:";
    private static final String LEFT_WING = "Left Wing:";
    private static final String RIGHT_WING = "Right Wing:";
    private static final String LEFT_DE = "Left Defense:";
    private static final String RIGHT_DE = "Right Defense:";
    private static final String OFFENSE = "Offense ";
    private static final String UPDATE_NAME = "Update Name";
    private static final String CREATE_TEAM = "Create Team";
    private static final String NAME_STRING = "Enter Name:";
    private static final String ENTER_NUM_OPPS = "Enter Number of PP/PK Opportunities:";
    private static final String EDIT_LINE = "Edit Line";
    private static final String PLAYER_NUMBER_STRING = "Select Player Number:";
    private static final String CHANGE_NUMBER = "Change Number?";
    private static final String LEFT_HANDED = "Left-handed (Click to switch)";
    private static final String RIGHT_HANDED = "Right-handed (Click to switch)";
    private static final String STICK_HAND_STRING = "Current Stick Hand: ";
    private static final String CHANGE_POSITION = "Change Position? - Current: ";
    private static final String GOALS_STRING = "Enter Goals:";
    private static final String ASSISTS_STRING = "Enter Assists:";
    private static final String PM_STRING = "Enter +/-:";
    private static final String HITS_STRING = "Enter Hits:";
    private static final String PIM_STRING = "Enter Penalty Minutes:";
    private static final String SHOT_BLOCK_STRING = "Enter Shots Blocked:";
    private static final String FACE_OFF_STRING = "Enter Face Off % / Face Off Total:";
    private static final String WINS_STRING = "Enter wins:";
    private static final String LOSSES_STRING = "Enter losses:";
    private static final String OT_STRING = "Enter overtime losses or ties:";
    private static final String SHUTOUTS_STRING = "Select Shutouts:";
    private static final String SV_PERCENT_STRING = "Enter Save % / Total Shots Faced:";
    private static final String ENTER_PENALTIES = "Select Number of Penalties called on your Players: ";
    private static final String ENTER_POWER_PLAY = "Select Number of Power Play Opportunities: ";
    private static final String POST_SHOTS_BLOCKED = "Select Number of Shots Blocked by your Skaters: ";
    private static final String POST_SHOTS_AGAINST = "Select Number of Shots Against your Goalie: ";
    private static final String POST_HITS = "Select Number of Hits made by your Team: ";
    private static final String UPDATE = "Update Changes";
    private static final String RESET_CONFIRM = "Are you sure you want to reset ";
    private static final String TO_ZERO = "'s stats to 0?";
    private static final String CONFIRM_DELETE = "Are you sure you would like to delete ";
    private static final String EDIT_INSTRUCTIONS = "<html><center>Enter changes for the information that you would " +
            "like to change.<br>If a field is left blank, no changes will be made for the corresponding information." +
            "</html>";
    private static final String EDIT_LINE_INSTRUCTIONS = "<html><center>This tab will make changes to or delete any " +
            "already created lines.<br>Use the drop box at the top of the screen to select which line you would like " +
            "to change or delete.<br>" + EDIT_INSTRUCTIONS;
    private static final String EDIT_PLAYER_INSTRUCTIONS = "<html><center>This tab will make changes to or delete " +
            "any already created players.<br>Use the drop box at the top of the screen to select which player you " +
            "would like to change or delete.<br>" + EDIT_INSTRUCTIONS;
    private static final String RESET_STATS_WARNING = "<html><center>The button below will reset all stats for your " +
            "team, its players, and its special teams to 0.<br>This action cannot be undone after the fact and their " +
            "previous stats will be lost.</html>";
    private static final String NUMBER_ERROR = "Please enter a number where prompted.";
    private static final String FILE_ERROR = "There was an issue interacting with the file. Please ensure that your " +
            "file has not been moved or altered, close the application, and try again.";
    private static final String EMPTY_INPUTS = "Please enter a value in at least one of the boxes";
    private static final String BLANK_UPDATED = " (Any blank fields have already been updated)";
    private static final String PLAYER_DUPLICATE = "New player cannot share the same number as another player";
    private static final String SELECT_LINE = "Selected Line:";
    private static final String SELECT_GOALIE = "Selected Goalie:";
    private static final String OFFENSE_LINE = "Offense Line";
    private static final String DEFENSE_LINE = "Defense Pair";
    private static final String PP_LINE = "Power Play Line";
    private static final String PK_LINE = "Penalty Kill Line";
    private static final String USE_PLAYERS = "Select Players Manually";
    private static final String USE_LINES = "Select Player from Lines";
    private static final String CURRENT_SCORE = "Current Score: (Your Team - Opponent) ";
    private static final String[] SKATER_STATS_COLUMNS = {"Skater Name", "Player #", "Position", "Goals", "Assists",
            "Points", "+/-"};
    private static final String[] GOALIE_STATS_COLUMNS = {"Goalie Name", "Player #", "Wins", "Losses", "OT L / Ties",
            "GAA", "SV%"};
    private static final String SELECT = "Please select a player or line";

    // Numeric Constants
    private static final int ENTER_NAME_SIZE = 30;
    private static final int ENTER_STAT_SIZE = 5;
    private static final int NAME_COLUMN_WIDTH = 100;
    private static final int POST_GAME_MAX = 50;

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
    StatsTableModel skaterStats;
    StatsTableModel goalieStats;
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

    // Manage Skaters

    JTabbedPane skaterTabs;
    JLabel selectedSkaterLabel;

    // Create Skater
    JLabel enterSkaterNameLabel;
    JTextField enterSkaterName;
    JLabel enterSkaterNumberLabel;
    JSlider enterSkaterNumber;
    JToggleButton chooseStickHand;
    JLabel selectPosition;
    JComboBox<Position> positionOptions;
    JToggleButton assignSkaterStats;
    JLabel enterGoalsLabel;
    JTextField enterGoals;
    JLabel enterAssistsLabel;
    JTextField enterAssists;
    JLabel enterPMLabel;
    JTextField enterPlusMinus;
    JLabel enterHitsLabel;
    JTextField enterHits;
    JLabel enterPIMLabel;
    JTextField enterPenaltyMinutes;
    JLabel enterBlocksLabel;
    JTextField enterShotsBlocked;
    JLabel enterFaceOffLabel;
    JTextField enterFaceOffPercent;
    JTextField enterFaceOffTotal;

    JButton createPlayer;

    // Edit/Delete Skater
    JLabel editSkaterInstructions;
    JLabel changeSkaterNameLabel;
    JTextField changeSkaterName;
    JCheckBox changeSkaterNumberCheck;
    JSlider changeSkaterNumber;
    JCheckBox changePositionCheck;
    JComboBox<Position> changePosition;
    JLabel changeStickHandLabel;
    JCheckBox changeStickHand;
    JLabel changeGoalsLabel;
    JTextField changeGoals;
    JLabel changeAssistsLabel;
    JTextField changeAssists;
    JLabel changePMLabel;
    JTextField changePlusMinus;
    JLabel changeHitsLabel;
    JTextField changeHits;
    JLabel changePIMLabel;
    JTextField changePenaltyMinutes;
    JLabel changeBlocksLabel;
    JTextField changeShotsBlocked;
    JLabel changeFaceOffLabel;
    JTextField changeFaceOffPercent;
    JTextField changeFaceOffTotal;

    JButton editPlayer;
    JButton resetPlayerStats;
    JButton deletePlayer;

    // View Stats
    JTextArea viewSkaterStats;

    // Manage Goalies

    JTabbedPane goalieTabs;
    JLabel selectedGoalieLabel;

    // Create Goalie
    JLabel enterGoalieNameLabel;
    JTextField enterGoalieName;
    JLabel enterGoalieNumberLabel;
    JSlider selectGoalieNumber;
    JToggleButton assignGoalieStats;
    JLabel enterGoalieWinsLabel;
    JTextField enterGoalieWins;
    JLabel enterGoalieLossesLabel;
    JTextField enterGoalieLosses;
    JLabel enterGoalieOTLabel;
    JTextField enterGoalieOTLosses;
    JLabel enterShutoutsLabel;
    JSlider enterShutouts;
    JLabel enterSVPercentageLabel;
    JTextField enterSavePercentage;
    JTextField enterGoalieShotsAgainst;

    JButton createGoalie;

    // Edit/Delete Goalie
    JLabel editGoalieInstructions;
    JLabel changeGoalieNameLabel;
    JTextField changeGoalieName;
    JCheckBox changeGoalieNumberCheck;
    JSlider changeGoalieNumber;
    JLabel changeGoalieWinsLabel;
    JTextField changeGoalieWins;
    JLabel changeGoalieLossesLabel;
    JTextField changeGoalieLosses;
    JLabel changeGoalieOTLabel;
    JTextField changeGoalieOTLosses;
    JLabel changeShutoutsLabel;
    JTextField changeShutouts;
    JLabel changeSVPercentageLabel;
    JTextField changeSavePercentage;
    JTextField changeGoalieShotsAgainst;

    JButton editGoalie;
    JButton resetGoalieStats;
    JButton deleteGoalie;

    // View Stats
    JTextArea viewGoalieStats;

    // Enter Game Stats

    JTabbedPane enterStatsTabs;
    JComboBox<Line> nonDefenseLines;
    JLabel deLinesLabel;
    JComboBox<DefenseLine> defenseLines;
    JLabel ppLineLabel;
    JComboBox<PPLine> ppOptions;
    JLabel pkOptionsLabel;
    JComboBox<PKLine> pkOptions;
    JLabel goaliesForStatsLabel;
    JComboBox<Goalie> selectGoaliesForStats;

    // Enter Live
    JLabel offenseLinesLabel;
    JComboBox<OffenseLine> offenseLines;
    JLabel currentScore;
    JButton goalLive;
    JButton scoredAgainstLive;
    JButton shotAgainstOnGoalLive;
    JButton faceOffLive;
    ButtonGroup penaltyOptionsLive;
    JToggleButton powerPlayLive;
    JToggleButton penaltyLive;
    JLabel penaltyLengthLabel;
    JTextField penaltyLengthField;
    JButton penaltyOver;
    JButton shotBlockLive;
    JButton hitLive;
    JButton gameOver;

    // Enter Post Game
    JLabel finalScorePost;
    JTextField finalScoreTeam;
    JTextField finalScoreOpp;
    JLabel postGamePowerPlayLabel;
    JSlider postGamePowerPlay;
    JLabel postGamePenaltiesLabel;
    JSlider postGamePenalties;
    JLabel postGameShotsBlockedLabel;
    JSlider postGameShotsBlocked;
    JLabel postGameFaceOffLabel;
    JTextField postGameFaceOffWins;
    JTextField postGameFaceOffLosses;
    JLabel postGameShotsLabel;
    JSlider postGameShotsAgainst;
    JLabel postGameHitsLabel;
    JSlider postGameHits;
    JButton enterStats;

    // Enter Goal
    JToggleButton useLinesOrPlayers;
    JLabel scorerLabel;
    JComboBox<Position> scorerOptions;
    JLabel assistLabel1;
    JComboBox<Position> assistOptions1;
    JLabel assistLabel2;
    JComboBox<Position> assistOptions2;
    JLabel playerScoreLabel;
    JComboBox<Skater> scorerPlayerOptions;
    JLabel otherPlayersLabel;
    JComboBox<Skater> assistPlayerOptions1;
    JCheckBox assist1Check;
    JComboBox<Skater> assistPlayerOptions2;
    JCheckBox assist2Check;
    JComboBox<Skater> otherPlayerOptions1;
    JComboBox<Skater> otherPlayerOptions2;

    /**
     * Creates a sample team that is pre-generated for a user who has no file. Allows the user to get accustomed to the
     * app and its features before creating their own team if they so choose.
     * @return The generated sample team loosely based on data from the 2015 Chicago Blackhawks Regular Season.
     */
    public static Team createSample() {
        Team blackhawks = new Team("Sample Team: Chicago Blackhawks - 2014-15 Regular Season", 48, 28,
                6);
        blackhawks.addPlayer(new Skater("Patrick Kane", 88, "Left", Position.Right_Wing,
                27, 37, 10, 22, 10));
        blackhawks.addPlayer(new Skater("Kyle Baun", 39, "Right", Position.Left_Wing,
                0, 0, -1, 12, 0));
        blackhawks.addPlayer(new Skater("Bryan Bickell", 29, "Left", Position.Left_Wing,
                14, 14, 5, 205, 38));
        blackhawks.addPlayer(new Goalie("Corey Crawford", 50, .924, 1661,
                32, 20, 5, 2));
        blackhawks.addPlayer(new Center("Phillip Danault", 24, "Left", 0, 0,
                0, 3, 0, 30, 20));
        blackhawks.addPlayer(new Goalie("Scott Darling", 33, .936, 419, 9,
                4, 0, 1));
        blackhawks.addPlayer(new Center("Andrew Desjardins", 11, "Left", 0, 2,
                1, 32, 7, 63.2, 19));
        blackhawks.addPlayer(new Skater("Ryan Hartman", 38, "Right", Position.Right_Wing,
                0, 0, -1, 11, 2));
        blackhawks.addPlayer(new Defenseman("Niklas Hjalmarsson", 4, "Left",
                Position.Left_Defense, 3, 16, 25, 39, 44, 127));
        blackhawks.addPlayer(new Skater("Marian Hossa", 81, "Left", Position.Right_Wing,
                22, 39, 17, 62, 32));
        blackhawks.addPlayer(new Defenseman("Duncan Keith", 2, "Left",
                Position.Left_Defense, 10, 35, 12, 16, 20, 113));
        blackhawks.addPlayer(new Center("Markus Kruger", 22, "Left", 7, 10,
                -5, 29, 32, 53.3, 770));
        blackhawks.addPlayer(new Defenseman("Michal Rozsival", 32, "Right",
                Position.Right_Defense, 1, 12, 0, 103, 22, 87));
        blackhawks.addPlayer(new Defenseman("David Rundblad", 5, "Right",
                Position.Right_Defense, 3, 11, 17, 23, 12, 38));
        blackhawks.addPlayer(new Defenseman("Brent Seabrook", 7, "Right",
                Position.Right_Defense, 8, 23, -3, 135, 27, 141));
        blackhawks.addPlayer(new Center("Andrew Shaw", 65, "Right",
                15, 11, -8, 127, 67, 50.1, 712));
        blackhawks.addPlayer(new Skater("Teuvo Teravainen", 86, "Left", Position.Left_Wing,
                4, 5, 4, 11, 2));
        blackhawks.addPlayer(new Center("Jonathan Toews", 19, "Left", 28, 38,
                30, 56, 36, 56.5, 1675));
        blackhawks.addPlayer(new Defenseman("Trevor van Riemsdyk", 57, "Right",
                Position.Right_Defense, 0, 1, 0, 4, 2, 15));
        blackhawks.addPlayer(new Skater("Brandon Saad", 20, "Left", Position.Left_Wing,
                23, 29, 7, 53, 12));
        blackhawks.addPlayer(new Skater("Patrick Sharp", 10, "Right", Position.Left_Wing,
                16, 27, -8, 74, 33));
        blackhawks.addPlayer(new Center("Brad Richards", 91, "Left", 12, 25,
                3, 74, 12, 48.4, 825));
        blackhawks.addPlayer(new Skater("Kris Versteeg", 23, "Right", Position.Left_Wing,
                14, 20, 11, 31, 35));
        blackhawks.addPlayer(new Defenseman("Johnny Oduya", 27, "Left", Position.Left_Defense,
                2, 8, 5, 69, 26, 123));
        blackhawks.addPlayer(new Skater("Ben Smith", 28, "Right", Position.Right_Wing,
                5, 4, -1, 49, 2));
        blackhawks.addPlayer(new Skater("Daniel Carcillo", 13, "Left", Position.Left_Wing,
                4, 4, 3, 36, 54));
        blackhawks.addPlayer(new Center("Antoine Vermette", 80, "Left", 0, 3,
                -2, 9, 6, 50, 196));
        blackhawks.addPlayer(new Center("Joakim Nordstrom", 42, "Left", 0, 3,
                -5, 73, 4, 20, 10));
        blackhawks.addLine(new OffenseLine("Saad-Toews-Hossa", (Center) blackhawks.getSkaters()[7],
                blackhawks.getSkaters()[8], blackhawks.getSkaters()[22]));
        blackhawks.addLine(new OffenseLine("Kane-Richards-Versteeg", (Center) blackhawks.getSkaters()[25],
                blackhawks.getSkaters()[24], blackhawks.getSkaters()[10]));
        blackhawks.addLine(new OffenseLine("Nordstrom-Kruger-Smith", (Center) blackhawks.getSkaters()[9],
                blackhawks.getSkaters()[18], blackhawks.getSkaters()[13]));
        blackhawks.addLine(new OffenseLine("Hossa-Toews-Versteeg", (Center) blackhawks.getSkaters()[7],
                blackhawks.getSkaters()[22], blackhawks.getSkaters()[10]));
        blackhawks.addLine(new OffenseLine("Sharp-Richards-Kane", (Center) blackhawks.getSkaters()[25],
                blackhawks.getSkaters()[4], blackhawks.getSkaters()[24]));
        blackhawks.addLine(new OffenseLine("Sharp-Shaw-Bickell", (Center) blackhawks.getSkaters()[20],
                blackhawks.getSkaters()[4], blackhawks.getSkaters()[14]));
        blackhawks.addLine(new OffenseLine("Sharp-Toews-Hossa", (Center) blackhawks.getSkaters()[7],
                blackhawks.getSkaters()[4], blackhawks.getSkaters()[22]));
        blackhawks.addLine(new OffenseLine("Carcillo-Kruger-Smith", (Center) blackhawks.getSkaters()[9],
                blackhawks.getSkaters()[6], blackhawks.getSkaters()[13]));
        blackhawks.addLine(new OffenseLine("Teravainen-Shaw-Bickell", (Center) blackhawks.getSkaters()[20],
                blackhawks.getSkaters()[23], blackhawks.getSkaters()[14]));
        blackhawks.addLine(new OffenseLine("Carcillo-Shaw-Bickell", (Center) blackhawks.getSkaters()[20],
                blackhawks.getSkaters()[6], blackhawks.getSkaters()[14]));
        blackhawks.addLine(new DefenseLine("Hjalmarsson-Oduya", (Defenseman) blackhawks.getSkaters()[1],
                (Defenseman) blackhawks.getSkaters()[12]));
        blackhawks.addLine(new DefenseLine("Keith-Seabrook", (Defenseman) blackhawks.getSkaters()[0],
                (Defenseman) blackhawks.getSkaters()[3]));
        blackhawks.addLine(new DefenseLine("Keith-Rosival", (Defenseman) blackhawks.getSkaters()[0],
                (Defenseman) blackhawks.getSkaters()[15]));
        blackhawks.addLine(new DefenseLine("Hjalmarsson-Seabrook", (Defenseman) blackhawks.getSkaters()[1],
                (Defenseman) blackhawks.getSkaters()[3]));
        blackhawks.addLine(new DefenseLine("Keith-Hjalmarsson", (Defenseman) blackhawks.getSkaters()[0],
                (Defenseman) blackhawks.getSkaters()[1]));
        blackhawks.addLine(new DefenseLine("Keith-Rundblad", (Defenseman) blackhawks.getSkaters()[0],
                (Defenseman) blackhawks.getSkaters()[2]));
        blackhawks.addLine(new DefenseLine("Oduya-Seabrook", (Defenseman) blackhawks.getSkaters()[12],
                (Defenseman) blackhawks.getSkaters()[3]));
        blackhawks.addLine(new DefenseLine("Oduya-Rozsival", (Defenseman) blackhawks.getSkaters()[12],
                (Defenseman) blackhawks.getSkaters()[15]));
        blackhawks.addLine(new DefenseLine("Rozsival-Van Reimsdyk", (Defenseman) blackhawks.getSkaters()[15],
                (Defenseman) blackhawks.getSkaters()[19]));
        blackhawks.addLine(new PPLine("Power Play 1", (Center) blackhawks.getSkaters()[7],
                blackhawks.getSkaters()[25], blackhawks.getSkaters()[24],
                (Defenseman) blackhawks.getSkaters()[0], (Defenseman) blackhawks.getSkaters()[3],
                17.69, 130));
        blackhawks.addLine(new PPLine("Power Play 2", (Center) blackhawks.getSkaters()[9],
                blackhawks.getSkaters()[8], blackhawks.getSkaters()[22],
                (Defenseman) blackhawks.getSkaters()[1], (Defenseman) blackhawks.getSkaters()[15],
                17.69, 130));
        blackhawks.addLine(new PKLine("Penalty Kill 1", blackhawks.getSkaters()[7], blackhawks.getSkaters()[24],
                (Defenseman) blackhawks.getSkaters()[0], (Defenseman) blackhawks.getSkaters()[3],
                83.41, 106));
        blackhawks.addLine(new PKLine("Penalty Kill 2", blackhawks.getSkaters()[22],
                blackhawks.getSkaters()[14], (Defenseman) blackhawks.getSkaters()[1],
                (Defenseman) blackhawks.getSkaters()[12], 83.41, 105));
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
    private void createPanelForContainer(JComponent[] components, Container container) {
        JPanel panel = new JPanel();
        for (JComponent component : components) {
            panel.add(component);
        }
        container.add(panel);
    }

    /**
     * Adds given components to a newly created panel
     * @param components Components to be added to the panel
     * @return The created panel
     */
    private JPanel createPanel(JComponent[] components) {
        JPanel panel = new JPanel();
        for (JComponent component: components) {
            panel.add(component);
        }
        return panel;
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
     * Initializes all combo boxes containing elements of a team such as the boxes for selecting a line, a skater, a
     * goalie, etc.
     */
    private void setUpComboBoxes() {
        lineOptions = new JComboBox<>();
        lineOptions.addItem(null);
        nonDefenseLines = new JComboBox<>();
        nonDefenseLines.addItem(null);
        offenseLines = new JComboBox<>();
        defenseLines = new JComboBox<>();
        ppOptions = new JComboBox<>();
        pkOptions = new JComboBox<>();
        for (Line line: team.getLines()) {
            lineOptions.addItem(line);
            if (line instanceof DefenseLine d) {
                defenseLines.addItem(d);
            } else {
                if (line instanceof PPLine pp) {
                    ppOptions.addItem(pp);
                }
                if (line instanceof PKLine pk) {
                    pkOptions.addItem(pk);
                }
                if (line instanceof OffenseLine o) {
                    offenseLines.addItem(o);
                }
                nonDefenseLines.addItem(line);
            }
        }

        centerOptions = new JComboBox<>();
        centerOptions.addItem(null);
        pickLeftWing = new JComboBox<>();
        pickRightWing = new JComboBox<>();
        skaterOptions = new JComboBox<>();
        pickLeftWing.addItem(null);
        pickRightWing.addItem(null);
        skaterOptions.addItem(null);
        pickLeftDe = new JComboBox<>();
        pickRightDe = new JComboBox<>();
        pickLeftDe.addItem(null);
        pickRightDe.addItem(null);
        scorerPlayerOptions = new JComboBox<>();
        assistPlayerOptions1 = new JComboBox<>();
        assistPlayerOptions2 = new JComboBox<>();
        otherPlayerOptions1 = new JComboBox<>();
        otherPlayerOptions2 = new JComboBox<>();
        for (Skater player: team.getSkaters()) {
            pickLeftWing.addItem(player);
            pickRightWing.addItem(player);
            skaterOptions.addItem(player);
            scorerPlayerOptions.addItem(player);
            assistPlayerOptions1.addItem(player);
            assistPlayerOptions2.addItem(player);
            otherPlayerOptions1.addItem(player);
            otherPlayerOptions2.addItem(player);
            if (player instanceof Defenseman de) {
                pickLeftDe.addItem(de);
                pickRightDe.addItem(de);
            }
            if (player instanceof Center center) {
                centerOptions.addItem(center);
            }
        }

        goalieOptions = new JComboBox<>();
        selectGoaliesForStats = new JComboBox<>();
        goalieOptions.addItem(null);
        selectGoaliesForStats.addItem(null);
        for (Goalie goalie: team.getGoalies()) {
            goalieOptions.addItem(goalie);
            selectGoaliesForStats.addItem(goalie);
        }

        positionOptions = new JComboBox<>();
        positionOptions.addItem(Position.Center);
        positionOptions.addItem(Position.Left_Wing);
        positionOptions.addItem(Position.Right_Wing);
        positionOptions.addItem(Position.Left_Defense);
        positionOptions.addItem(Position.Right_Defense);
        changePosition = new JComboBox<>();
        changePosition.addItem(Position.Center);
        changePosition.addItem(Position.Left_Wing);
        changePosition.addItem(Position.Right_Wing);
        changePosition.addItem(Position.Left_Defense);
        changePosition.addItem(Position.Right_Defense);
        scorerOptions = new JComboBox<>();
        scorerOptions.addItem(Position.Center);
        scorerOptions.addItem(Position.Left_Wing);
        scorerOptions.addItem(Position.Right_Wing);
        scorerOptions.addItem(Position.Left_Defense);
        scorerOptions.addItem(Position.Right_Defense);
        assistOptions1 = new JComboBox<>();
        assistOptions1.addItem(null);
        assistOptions1.addItem(Position.Center);
        assistOptions1.addItem(Position.Left_Wing);
        assistOptions1.addItem(Position.Right_Wing);
        assistOptions1.addItem(Position.Left_Defense);
        assistOptions1.addItem(Position.Right_Defense);
        assistOptions2 = new JComboBox<>();
        assistOptions2.addItem(null);
        assistOptions2.addItem(Position.Center);
        assistOptions2.addItem(Position.Left_Wing);
        assistOptions2.addItem(Position.Right_Wing);
        assistOptions2.addItem(Position.Left_Defense);
        assistOptions2.addItem(Position.Right_Defense);
    }

    /**
     * Adds the given line to the appropriate combo boxes and removes the old line from the appropriate combo boxes
     * @param oldLine Line being removed from combo boxes
     * @param newLine Line being added to combo boxes
     * @param newIndex Index where the new line falls in the team line ArrayList
     */
    private void updateLineComboBoxes(Line oldLine, Line newLine, int newIndex) {
        if (oldLine != null) {
            lineOptions.removeItem(oldLine);
            if (oldLine instanceof DefenseLine) {
                defenseLines.removeItem(oldLine);
            } else if (oldLine instanceof PPLine) {
                nonDefenseLines.removeItem(oldLine);
                ppOptions.removeItem(oldLine);
            } else if (oldLine instanceof PKLine) {
                nonDefenseLines.removeItem(oldLine);
                pkOptions.removeItem(oldLine);
            } else if (oldLine instanceof OffenseLine) {
                nonDefenseLines.removeItem(oldLine);
            }
        }

        if (newLine != null) {
            lineOptions.insertItemAt(newLine, newIndex + 1);

            int low;
            int high;
            int i;
            if (newLine instanceof DefenseLine de) {
                if (defenseLines.getItemCount() == 0) defenseLines.addItem(de);
                else {
                    low = 0;
                    high = defenseLines.getItemCount();
                    while (true) {
                        i = (low + high) / 2;
                        if (de.getName().compareTo(defenseLines.getItemAt(i).getName()) < 0) {
                            if (i == 0 || de.getName().compareTo(defenseLines.getItemAt(i - 1).getName()) > 0) {
                                defenseLines.insertItemAt(de, i);
                                break;
                            }
                            high = i;
                        } else {
                            if (i == defenseLines.getItemCount() - 1) {
                                defenseLines.addItem(de);
                                break;
                            }
                            if (de.getName().compareTo(defenseLines.getItemAt(i + 1).getName()) < 0) {
                                defenseLines.insertItemAt(de, i + 1);
                                break;
                            }
                            low = i;
                        }
                    }
                }
            } else {
                if (nonDefenseLines.getItemCount() == 1) nonDefenseLines.addItem(newLine);
                else {
                    low = 1;
                    high = nonDefenseLines.getItemCount();
                    while (true) {
                        i = (high + low) / 2;
                        if (newLine.getName().compareTo(nonDefenseLines.getItemAt(i).getName()) < 0) {
                            if (i == 1 ||
                                    newLine.getName().compareTo(nonDefenseLines.getItemAt(i - 1).getName()) > 0) {
                                nonDefenseLines.insertItemAt(newLine, i);
                                break;
                            }
                            high = i;
                        } else {
                            if (i == nonDefenseLines.getItemCount() - 1) {
                                nonDefenseLines.addItem(newLine);
                                break;
                            }
                            if (newLine.getName().compareTo(nonDefenseLines.getItemAt(i + 1).getName()) < 0) {
                                nonDefenseLines.insertItemAt(newLine, i + 1);
                                break;
                            }
                            low = i;
                        }
                    }
                }

                if (newLine instanceof PPLine pp) {
                    if (ppOptions.getItemCount() == 0) ppOptions.addItem(pp);
                    else {
                        low = 0;
                        high = ppOptions.getItemCount();
                        while (true) {
                            i = (high + low) / 2;
                            if (pp.getName().compareTo(ppOptions.getItemAt(i).getName()) < 0) {
                                if (i == 0 || pp.getName().compareTo(ppOptions.getItemAt(i - 1).getName()) > 0) {
                                    ppOptions.insertItemAt(pp, i);
                                    break;
                                }
                                high = i;
                            } else {
                                if (i == ppOptions.getItemCount() - 1) {
                                    ppOptions.addItem(pp);
                                    break;
                                }
                                if (pp.getName().compareTo(ppOptions.getItemAt(i + 1).getName()) < 0) {
                                    ppOptions.insertItemAt(pp, i + 1);
                                    break;
                                }
                                low = i;
                            }
                        }
                    }
                } else if (newLine instanceof PKLine pk) {
                    if (pkOptions.getItemCount() == 0) pkOptions.addItem(pk);
                    else {
                        low = 0;
                        high = pkOptions.getItemCount();
                        while (true) {
                            i = (high + low) / 2;
                            if (pk.getName().compareTo(pkOptions.getItemAt(i).getName()) < 0) {
                                if (i == 0 || pk.getName().compareTo(pkOptions.getItemAt(i - 1).getName()) > 0) {
                                    pkOptions.insertItemAt(pk, i);
                                    break;
                                }
                                high = i;
                            } else {
                                if (i == pkOptions.getItemCount() - 1) {
                                    pkOptions.addItem(pk);
                                    break;
                                }
                                if (pk.getName().compareTo(pkOptions.getItemAt(i + 1).getName()) < 0) {
                                    pkOptions.insertItemAt(pk, i + 1);
                                    break;
                                }
                                low = i;
                            }
                        }
                    }
                } else if (newLine instanceof OffenseLine o) {
                    if (offenseLines.getItemCount() == 0) offenseLines.addItem(o);
                    else {
                        low = 0;
                        high = offenseLines.getItemCount();
                        while (true) {
                            i = (high + low) / 2;
                            if (o.getName().compareTo(offenseLines.getItemAt(i).getName()) < 0) {
                                if (i == 0 ||
                                        o.getName().compareTo(offenseLines.getItemAt(i - 1).getName()) > 0) {
                                    offenseLines.insertItemAt(o, i);
                                    break;
                                }
                                high = i;
                            } else {
                                if (i == offenseLines.getItemCount() - 1) {
                                    offenseLines.addItem(o);
                                    break;
                                }
                                if (o.getName().compareTo(offenseLines.getItemAt(i + 1).getName()) < 0) {
                                    offenseLines.insertItemAt(o, i + 1);
                                    break;
                                }
                                low = i;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Removes the old player from relevant combo boxes and adds the new player to the appropriate combo boxes in the
     * GUI. Also updates appropriate stat display areas (JTable and JTextAreas).
     * @param oldPlayer Player being removed
     * @param newPlayer Player being added
     * @param oldIndex Index where the old skater was in the combo boxes
     * @param index Index where the new skater was added to the team ArrayList
     */
    private void updatePlayerComponents(Player oldPlayer, Player newPlayer, int oldIndex, int index) {
        if (oldPlayer != null) {  // Remove old skater
            if (oldPlayer instanceof Skater oldSkater) {
                pickLeftWing.removeItem(oldSkater);
                pickRightWing.removeItem(oldSkater);
                skaterOptions.removeItem(oldSkater);
                skaterStats.removeRow(oldIndex);
                scorerPlayerOptions.removeItem(oldSkater);
                assistPlayerOptions1.removeItem(oldSkater);
                assistPlayerOptions2.removeItem(oldSkater);
                otherPlayerOptions1.removeItem(oldSkater);
                otherPlayerOptions2.removeItem(oldSkater);
                if (oldSkater instanceof Center) {
                    centerOptions.removeItem(oldSkater);
                }
                if (oldSkater instanceof Defenseman) {
                    pickLeftDe.removeItem(oldSkater);
                    pickRightDe.removeItem(oldSkater);
                }
                // Resets line combo boxes since lines containing this player have been removed
                lineOptions.removeAllItems();
                lineOptions.addItem(null);
                nonDefenseLines.removeAllItems();
                nonDefenseLines.addItem(null);
                offenseLines.removeAllItems();
                defenseLines.removeAllItems();
                ppOptions.removeAllItems();
                pkOptions.removeAllItems();
                for (Line line: team.getLines()) {
                    lineOptions.addItem(line);
                    if (line instanceof DefenseLine d) {
                        defenseLines.addItem(d);
                    } else {
                        if (line instanceof PPLine pp) {
                            ppOptions.addItem(pp);
                        }
                        if (line instanceof PKLine pk) {
                            pkOptions.addItem(pk);
                        }
                        if (line instanceof OffenseLine o) {
                            offenseLines.addItem(o);
                        }
                        nonDefenseLines.addItem(line);
                    }
                }
            } else if (oldPlayer instanceof Goalie oldGoalie) {
                goalieOptions.removeItem(oldGoalie);
                selectGoaliesForStats.removeItem(oldGoalie);
                goalieStats.removeRow(oldIndex);
            }
        }

        if (oldPlayer == null && oldIndex == index) {
            if (newPlayer instanceof Skater newSkater) {
                skaterStats.removeRow(oldIndex);
                skaterStats.insertRow(index, newSkater.getStatsArray());
                viewSkaterStats.setText(newSkater.statsDisplay());
            } else if (newPlayer instanceof Goalie newGoalie) {
                goalieStats.removeRow(oldIndex);
                goalieStats.insertRow(index, newGoalie.getStatsArray());
                viewGoalieStats.setText(newGoalie.statsDisplay());
            }
            return;
        }

        if (newPlayer != null) {
            if (newPlayer instanceof Skater newSkater) {
                pickLeftWing.insertItemAt(newSkater, index + 1);
                pickRightWing.insertItemAt(newSkater, index + 1);
                skaterOptions.insertItemAt(newSkater, index + 1);
                scorerPlayerOptions.insertItemAt(newSkater, index);
                assistPlayerOptions1.insertItemAt(newSkater, index);
                assistPlayerOptions2.insertItemAt(newSkater, index);
                otherPlayerOptions1.insertItemAt(newSkater, index);
                otherPlayerOptions2.insertItemAt(newSkater, index);
                skaterStats.insertRow(index, newSkater.getStatsArray());

                if (newSkater instanceof Center c) {  // Finds proper spot in center combo box if the player is a center
                    if (centerOptions.getItemCount() == 1) centerOptions.addItem(c);
                    else {
                        int low = 1;
                        int high = centerOptions.getItemCount();
                        while (true) {
                            int i = (low + high) / 2;
                            if (c.getPlayerNumber() < centerOptions.getItemAt(i).getPlayerNumber()) {
                                if (i == 1) {
                                    centerOptions.insertItemAt(c, 1);
                                    break;
                                }
                                if (c.getPlayerNumber() > centerOptions.getItemAt(i - 1).getPlayerNumber()) {
                                    centerOptions.insertItemAt(c, i);
                                    break;
                                }
                                high = i;
                            } else {
                                if (i == centerOptions.getItemCount() - 1) {
                                    centerOptions.addItem(c);
                                    break;
                                }
                                if (c.getPlayerNumber() < centerOptions.getItemAt(i + 1).getPlayerNumber()) {
                                    centerOptions.insertItemAt(c, i + 1);
                                    break;
                                }
                                low = i;
                            }
                        }
                    }
                }

                // Finds proper spot in defenseman combo boxes if player is de
                if (newSkater instanceof Defenseman de) {
                    for (int i = 1; i < pickLeftDe.getItemCount(); i++) {
                        if (i == pickLeftDe.getItemCount() - 1) {
                            pickLeftDe.addItem(de);
                            pickRightDe.addItem(de);
                            break;
                        }
                        if (pickLeftDe.getItemAt(i).getPlayerNumber() < de.getPlayerNumber() &&
                                de.getPlayerNumber() < pickLeftDe.getItemAt(i + 1).getPlayerNumber()) {
                            pickLeftDe.insertItemAt(de, i + 1);
                            pickRightDe.insertItemAt(de, i + 1);
                            break;
                        }
                    }
                }
            } else if (newPlayer instanceof Goalie newGoalie) {
                goalieOptions.insertItemAt(newGoalie, index + 1);
                selectGoaliesForStats.insertItemAt(newGoalie, index + 1);
                goalieStats.insertRow(index, newGoalie.getStatsArray());
            }
        }
    }

    /**
     * This method sets up and displays the GUI for editing an actual team after one has been selected/created from
     * the selectTeam frame. It is composed of a variety of tabs that encompass the different ways to modify or manage
     * a team.
     */
    private void displayTeamGUI() {
        setUpComboBoxes();
        // Setup Frame
        JFrame mainFrame = new JFrame(team.getName());
        mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        mainFrame.setSize(new Dimension(screenSize.width * 2 / 3, screenSize.height * 2 / 3));
        mainFrame.setLocationRelativeTo(null);
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
        updateTeamChanges = new JButton(UPDATE);
        resetStatsWarningLabel = new JLabel(RESET_STATS_WARNING);
        resetTeamStats = new JButton(RESET_TEAM_STATS);

        // Panels for each different instance variable (name, wins, losses, ot losses)
        createPanelForContainer(new JComponent[]{changeTeamNameLabel, changeTeamName}, editTeam);
        createPanelForContainer(new JComponent[]{changeTeamWinsLabel, changeTeamWins}, editTeam);
        createPanelForContainer(new JComponent[]{changeTeamLossesLabel, changeTeamLosses}, editTeam);
        createPanelForContainer(new JComponent[]{changeTeamOTLabel, changeTeamOT}, editTeam);

        // Instructions and Buttons
        createPanelForContainer(new JComponent[]{editInstructionsLabel}, editTeam);
        createPanelForContainer(new JComponent[]{updateTeamChanges}, editTeam);
        createPanelForContainer(new JComponent[]{resetStatsWarningLabel}, editTeam);
        createPanelForContainer(new JComponent[]{resetTeamStats}, editTeam);

        updateTeamChanges.addActionListener(e -> {
            // Get user inputs
            String name = changeTeamName.getText();
            String[] recordStrings = {changeTeamWins.getText(), changeTeamLosses.getText(), changeTeamOT.getText()};

            if (name.isBlank() && recordStrings[0].isBlank() && recordStrings[1].isBlank() &&
                    recordStrings[2].isBlank()) {
                JOptionPane.showMessageDialog(mainFrame, EMPTY_INPUTS,
                        EDIT_TEAM, JOptionPane.INFORMATION_MESSAGE);
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
                JOptionPane.showMessageDialog(mainFrame, NUMBER_ERROR, EDIT_TEAM, JOptionPane.ERROR_MESSAGE);
                return;
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(mainFrame, FILE_ERROR, EDIT_TEAM, JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), EDIT_TEAM, JOptionPane.ERROR_MESSAGE);
            }
            if (!name.isBlank()) {
                Team newTeam = new Team(team);
                newTeam.setName(name);
                changeTeamName.setText("");
                int index = TeamGUI.changeTeam(team, newTeam);
                updateTeamComponents(team, newTeam, index);
                if (index == -1) {
                    JOptionPane.showMessageDialog(mainFrame, "New name cannot be the same name as another team",
                            EDIT_TEAM, JOptionPane.ERROR_MESSAGE);
                    return;
                }
                team = newTeam;
                mainFrame.setTitle(team.getName());
                try {
                    updateFile();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(mainFrame, FILE_ERROR, EDIT_TEAM, JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), EDIT_TEAM,
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                updateTeamComponents(null, null, -1);
            }
        });

        // Resets stats for team and updates relevant components
        resetTeamStats.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(mainFrame, "Are you sure that you want to reset " +
                    "stats for the entire team?", RESET_TEAM_STATS, JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                team.resetTeamStats();
                try {
                    updateFile();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(mainFrame, FILE_ERROR, RESET_TEAM_STATS,
                            JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), RESET_TEAM_STATS,
                            JOptionPane.ERROR_MESSAGE);
                }
                updateEntireTeamComponents();
                mainFrame.repaint();
            }
        });

        teamTabs.add(EDIT_TEAM, editTeam);

        // Text Area that displays the teams roster
        viewRoster = new JTextArea(team.generateRoster());
        viewRoster.setEditable(false);
        JScrollPane viewRosterScroll = new JScrollPane(viewRoster);
        teamTabs.add("View Roster", viewRosterScroll);

        // Table that displays slightly more detailed roster with basic stats
        Container viewRosterWithStatsContent = new Container();
        viewRosterWithStatsContent.setLayout(new BoxLayout(viewRosterWithStatsContent, BoxLayout.Y_AXIS));
        JScrollPane viewRosterWithStatsScroll = new JScrollPane(viewRosterWithStatsContent);

        skaterStats = new StatsTableModel(team.generateSkaterRosterWithStats(),
                SKATER_STATS_COLUMNS);
        goalieStats = new StatsTableModel(team.generateGoalieRosterWithStats(),
                GOALIE_STATS_COLUMNS);
        viewRosterStatsSkaters = new JTable(skaterStats);
        viewRosterStatsSkaters.getColumnModel().getColumn(0).setPreferredWidth(NAME_COLUMN_WIDTH);
        viewRosterStatsGoalies = new JTable(goalieStats);
        viewRosterStatsGoalies.getColumnModel().getColumn(0).setPreferredWidth(NAME_COLUMN_WIDTH);

        createPanelForContainer(new JComponent[]{viewRosterStatsSkaters.getTableHeader()}, viewRosterWithStatsContent);
        createPanelForContainer(new JComponent[]{viewRosterStatsSkaters}, viewRosterWithStatsContent);
        createPanelForContainer(new JComponent[]{viewRosterStatsGoalies.getTableHeader()}, viewRosterWithStatsContent);
        createPanelForContainer(new JComponent[]{viewRosterStatsGoalies}, viewRosterWithStatsContent);

        teamTabs.add("View Basic Player Stats", viewRosterWithStatsScroll);

        // Text Area that displays stats for the overall team
        viewTeamStats = new JTextArea(team.displayTeamStats());
        viewTeamStats.setEditable(false);
        JScrollPane viewTeamStatsScroll = new JScrollPane(viewTeamStats);
        teamTabs.add("View Team Stats", viewTeamStatsScroll);



        mainTabs.add("Manage Team", teamTabs);

        // Manage Lines

        Container mainLineContainer = new Container();
        mainLineContainer.setLayout(new BoxLayout(mainLineContainer, BoxLayout.Y_AXIS));
        lineTabs = new JTabbedPane();
        currentLineLabel = new JLabel(SELECT_LINE);

        createPanelForContainer(new JComponent[]{currentLineLabel, lineOptions}, mainLineContainer);
        createPanelForContainer(new JComponent[]{lineTabs}, mainLineContainer);

        JScrollPane lineContainerScroll = new JScrollPane(mainLineContainer);

        // Create Line

        Container createLineContent = new Container();
        createLineContent.setLayout(new BoxLayout(createLineContent, BoxLayout.Y_AXIS));

        enterLineNameLabel = new JLabel(NAME_STRING);
        lineName = new JTextField(ENTER_NAME_SIZE);
        createPanelForContainer(new JComponent[]{enterLineNameLabel, lineName}, createLineContent);

        createLine = new JButton(CREATE_LINE);
        createPanelForContainer(new JComponent[]{createLine}, createLineContent);

        lineTypeLabel = new JLabel("Select Type of Line:");
        lineTypeGroup = new ButtonGroup();
        lineType = new JRadioButton[]{new JRadioButton(OFFENSE_LINE), new JRadioButton(DEFENSE_LINE),
                new JRadioButton(PP_LINE), new JRadioButton(PK_LINE)};
        for (JRadioButton button: lineType) {
            lineTypeGroup.add(button);
        }
        final boolean[] selectedLine = {false, false, false, false};
        createPanelForContainer(new JComponent[]{lineTypeLabel}, createLineContent);
        createPanelForContainer(lineType, createLineContent);

        // Center
        selectCenterLabel = new JLabel(CENTER);
        JPanel selectCenterPanel = createPanel(new JComponent[]{selectCenterLabel, centerOptions});

        // Left Wing
        selectLWLabel = new JLabel(LEFT_WING);
        JPanel selectLWPanel = createPanel(new JComponent[]{selectLWLabel, pickLeftWing});


        // Right Wing
        selectRWLabel = new JLabel(RIGHT_WING);
        JPanel selectRWPanel = createPanel(new JComponent[]{selectRWLabel, pickRightWing});

        // Left De
        selectLDLabel = new JLabel(LEFT_DE);
        JPanel selectLDPanel = createPanel(new JComponent[]{selectLDLabel, pickLeftDe});

        // Right De
        selectRDLabel = new JLabel(RIGHT_DE);
        JPanel selectRDPanel = createPanel(new JComponent[]{selectRDLabel, pickRightDe});

        // Special Teams Stats
        enterStatsToggle = new JToggleButton("Click to enter a starting Success %");
        JPanel statsTogglePanel = createPanel(new JComponent[]{enterStatsToggle});

        enterSuccessPercentageLabel = new JLabel("Enter Success Percentage:");
        enterSuccessPercentage = new JTextField(ENTER_STAT_SIZE);
        JPanel enterSuccessPercentagePanel = createPanel(new JComponent[]{enterSuccessPercentageLabel,
                enterSuccessPercentage});

        enterNumOppsLabel = new JLabel(ENTER_NUM_OPPS);
        enterNumOpps = new JTextField(ENTER_STAT_SIZE);
        JPanel enterNumOppsPanel = createPanel(new JComponent[]{enterNumOppsLabel, enterNumOpps});

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
                selectLWLabel.setText(OFFENSE + "1:");
                selectRWLabel.setText(OFFENSE + "2:");
                createLineContent.add(selectLWPanel);
                createLineContent.add(selectRWPanel);
                createLineContent.add(selectLDPanel);
                createLineContent.add(selectRDPanel);
                createLineContent.add(statsTogglePanel);
            } else if (selectedLine[3]) {
                selectedLine[3] = false;
                selectLWLabel.setText(LEFT_WING);
                selectRWLabel.setText(RIGHT_WING);
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
            Line newLine;
            if (lineType[0].isSelected()) {  // Offense Line
                try {
                    newLine = new OffenseLine(lineName.getText(), (Center) centerOptions.getSelectedItem(),
                            (Skater) pickLeftWing.getSelectedItem(), (Skater) pickRightWing.getSelectedItem());
                    
                } catch (IllegalArgumentException | NullPointerException ex) {
                    JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), CREATE_LINE,
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else if (lineType[1].isSelected()) {  // Defense Line
                try {
                    newLine = new DefenseLine(lineName.getText(), (Defenseman) pickLeftDe.getSelectedItem(),
                            (Defenseman) pickRightDe.getSelectedItem());
                } catch (IllegalArgumentException | NullPointerException ex) {
                    JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), CREATE_LINE,
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
                    JOptionPane.showMessageDialog(mainFrame, NUMBER_ERROR, CREATE_TEAM,
                            JOptionPane.ERROR_MESSAGE);
                    return;
                } catch (IllegalArgumentException | NullPointerException ex) {
                    JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), CREATE_TEAM,
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
                    JOptionPane.showMessageDialog(mainFrame, NUMBER_ERROR, CREATE_LINE,
                            JOptionPane.ERROR_MESSAGE);
                    return;
                } catch (IllegalArgumentException | NullPointerException ex) {
                    JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), CREATE_LINE,
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {  // No selection
                JOptionPane.showMessageDialog(mainFrame, "Please select a line type to assign players to " +
                        "your line", CREATE_LINE, JOptionPane.ERROR_MESSAGE);
                return;
            }
            int index = team.addLine(newLine);
            if (index == -1) {
                JOptionPane.showMessageDialog(mainFrame, "New Line cannot share the same name as another line",
                        CREATE_LINE, JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Reset Text Boxes
            lineName.setText("");
            enterSuccessPercentage.setText("");
            enterNumOpps.setText("");

            updateLineComboBoxes(null, newLine, index);

            try {
                updateFile();
                JOptionPane.showMessageDialog(mainFrame, "New line has successfully been created",
                        CREATE_LINE, JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(mainFrame, FILE_ERROR, CREATE_LINE, JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), CREATE_LINE, JOptionPane.ERROR_MESSAGE);
            }
        });

        lineTabs.add(CREATE_LINE, createLineContent);

        // Edit Line

        Container editLineContent = new Container();
        editLineContent.setLayout(new BoxLayout(editLineContent, BoxLayout.Y_AXIS));

        editLineInstructions = new JLabel(EDIT_LINE_INSTRUCTIONS);
        createPanelForContainer(new JComponent[]{editLineInstructions}, editLineContent);

        // Change Name
        changeLineNameLabel = new JLabel(NAME_STRING);
        changeLineName = new JTextField(ENTER_NAME_SIZE);
        createPanelForContainer(new JComponent[]{changeLineNameLabel, changeLineName}, editLineContent);

        updateLineChanges = new JButton(UPDATE_NAME);
        createPanelForContainer(new JComponent[]{updateLineChanges}, editLineContent);

        // Change Players
        changeLinePlayers = new JButton("Change Line Players");
        createPanelForContainer(new JComponent[]{changeLinePlayers}, editLineContent);

        // Delete Line
        deleteLine = new JButton("Delete Selected Line");
        createPanelForContainer(new JComponent[]{deleteLine}, editLineContent);

        // Change Special Teams Stats
        AtomicBoolean isSpecialTeams = new AtomicBoolean(false);
        changeSuccessPercentLabel = new JLabel("Enter New Success Percentage:");
        changeSuccessPercent = new JTextField(ENTER_STAT_SIZE);
        changeNumOppsLabel = new JLabel(ENTER_NUM_OPPS);
        changeNumOpps = new JTextField(ENTER_STAT_SIZE);
        JPanel changeSTSuccess = createPanel(new JComponent[]{changeSuccessPercentLabel,
                changeSuccessPercent, changeNumOppsLabel, changeNumOpps});

        // Ensures that proper components are displayed for editing a line depending on which line is selected
        lineOptions.addItemListener(e -> {
            if (lineOptions.getSelectedItem() instanceof SpecialTeamsLine && !isSpecialTeams.get()) {
                isSpecialTeams.set(true);
                updateLineChanges.setText(UPDATE);
                editLineContent.add(changeSTSuccess, editLineContent.getComponentCount() - 3);
            } else if (isSpecialTeams.get() && (lineOptions.getSelectedItem() instanceof OffenseLine ||
                    lineOptions.getSelectedItem() instanceof DefenseLine || lineOptions.getSelectedItem() == null) ) {
                updateLineChanges.setText(UPDATE_NAME);
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
                JOptionPane.showMessageDialog(mainFrame, SELECT, EDIT_LINE,
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
                    JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), EDIT_LINE,
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else if (editingLine instanceof DefenseLine defenseLine) {
                try {
                    newLine = new DefenseLine(defenseLine);
                    newLine.setName(name);
                } catch (IllegalArgumentException | NullPointerException ex) {
                    JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), EDIT_LINE,
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
                                        EDIT_LINE, JOptionPane.INFORMATION_MESSAGE);
                            } catch (IOException ex) {
                                JOptionPane.showMessageDialog(mainFrame, FILE_ERROR, EDIT_LINE,
                                        JOptionPane.ERROR_MESSAGE);
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), EDIT_LINE,
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
                    JOptionPane.showMessageDialog(mainFrame, NUMBER_ERROR, EDIT_LINE,
                            JOptionPane.ERROR_MESSAGE);
                    return;
                } catch (IllegalArgumentException | NullPointerException ex) {
                    JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), EDIT_LINE, JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            int index = team.changeLine(editingLine, newLine);
            if (index == -1) {
                JOptionPane.showMessageDialog(mainFrame, "New name cannot be the same as another line's name",
                        EDIT_LINE, JOptionPane.ERROR_MESSAGE);
                return;
            }
            updateLineComboBoxes(editingLine, newLine, index);

            try {
                updateFile();
                viewLine.setText(editingLine.lineRoster());
                changeLineName.setText("");
                lineOptions.setSelectedIndex(0);
                JOptionPane.showMessageDialog(mainFrame, "Line Updated Successfully", EDIT_LINE,
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(mainFrame, FILE_ERROR, EDIT_LINE, JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), EDIT_LINE, JOptionPane.ERROR_MESSAGE);
            }
        });

        changeLinePlayers.addActionListener(e -> {
            if (lineOptions.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(mainFrame, SELECT, EDIT_LINE, JOptionPane.ERROR_MESSAGE);
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
            createPanelForContainer(new JComponent[]{displayCurrentPlayers}, playersWindowContent);

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
                selectLWLabel.setText(OFFENSE + "1:");
                selectRWLabel.setText(OFFENSE + "2:");
            }

            JButton assignPlayers = new JButton("Assign These Players");
            createPanelForContainer(new JComponent[]{assignPlayers}, playersWindowContent);

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
                        if (pickLeftDe.getSelectedItem() != null) {
                            pkLine.setLeftDe((Defenseman) pickLeftDe.getSelectedItem());
                            change = true;
                        }
                        if (pickRightDe.getSelectedItem() != null) {
                            pkLine.setRightDe((Defenseman) pickRightDe.getSelectedItem());
                            change = true;
                        }
                    }
                } catch (IllegalArgumentException | NullPointerException ex) {
                    JOptionPane.showMessageDialog(playersWindow, ex.getMessage(), EDIT_LINE,
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (change) {
                    try {
                        updateFile();
                        viewLine.setText(editingLine.lineRoster());
                        JOptionPane.showMessageDialog(playersWindow, "Players successfully updated",
                                EDIT_LINE, JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(mainFrame, FILE_ERROR, EDIT_LINE,
                                JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), EDIT_LINE,
                                JOptionPane.ERROR_MESSAGE);
                    }
                    playersWindow.dispose();
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "Please select new players for the line or close" +
                            " the window.", EDIT_LINE, JOptionPane.ERROR_MESSAGE);
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

            int response = JOptionPane.showConfirmDialog(mainFrame, CONFIRM_DELETE +
                    removingLine.getName() + "?", "Delete Line", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                if (team.removeLine(removingLine)) {
                    updateLineComboBoxes(removingLine, null, -1);
                    try {
                        updateFile();
                        JOptionPane.showMessageDialog(mainFrame, "Line successfully deleted", "Delete Line",
                                JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(mainFrame, FILE_ERROR, "Delete Line",
                                JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "Delete Line",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "An unexpected error occurred - " +
                                    "deleteLine.addActionListener",
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

        createPanelForContainer(new JComponent[]{viewLineLabel}, viewLinesContent);
        createPanelForContainer(new JComponent[]{viewLine}, viewLinesContent);

        lineTabs.add("View Lines", viewLinesContent);

        mainTabs.add("Manage Lines", lineContainerScroll);

        // Manage Skaters

        Container manageSkaterContent = new Container();
        manageSkaterContent.setLayout(new BoxLayout(manageSkaterContent, BoxLayout.Y_AXIS));
        skaterTabs = new JTabbedPane();
        selectedSkaterLabel = new JLabel("Selected Skater:");
        createPanelForContainer(new JComponent[]{selectedSkaterLabel, skaterOptions}, manageSkaterContent);
        createPanelForContainer(new JComponent[]{skaterTabs}, manageSkaterContent);

        // Create Skater

        Container createSkater = new Container();
        createSkater.setLayout(new BoxLayout(createSkater, BoxLayout.Y_AXIS));
        JScrollPane createSkaterScroll = new JScrollPane(createSkater);

        enterSkaterNameLabel = new JLabel(NAME_STRING);
        enterSkaterName = new JTextField(ENTER_NAME_SIZE);
        enterSkaterNumber = new JSlider(1, 99);
        enterSkaterNumberLabel = new JLabel(PLAYER_NUMBER_STRING + "  " + enterSkaterNumber.getValue());
        createPanelForContainer(new JComponent[]{enterSkaterNameLabel, enterSkaterName, enterSkaterNumberLabel, enterSkaterNumber},
                createSkater);

        // Displays currently selected value in the JLabel
        enterSkaterNumber.addChangeListener(e ->
                enterSkaterNumberLabel.setText(PLAYER_NUMBER_STRING + "  " + enterSkaterNumber.getValue()));

        chooseStickHand = new JToggleButton(RIGHT_HANDED);
        selectPosition = new JLabel("Select Player Position:");
        createPanelForContainer(new JComponent[]{chooseStickHand, selectPosition, positionOptions}, createSkater);

        // Changes text for JToggleButton to reflect user's choice
        chooseStickHand.addActionListener(e -> {
            if (chooseStickHand.isSelected()) {
                chooseStickHand.setText(LEFT_HANDED);
            } else {
                chooseStickHand.setText(RIGHT_HANDED);
            }
        });

        assignSkaterStats = new JToggleButton("Initialize Skater Stats");
        createPanelForContainer(new JComponent[]{assignSkaterStats}, createSkater);

        createPlayer = new JButton("Create Player");
        createPanelForContainer(new JComponent[]{createPlayer}, createSkater);

        enterGoalsLabel = new JLabel(GOALS_STRING);
        enterGoals = new JTextField(ENTER_STAT_SIZE);
        enterAssistsLabel = new JLabel(ASSISTS_STRING);
        enterAssists = new JTextField(ENTER_STAT_SIZE);
        enterPMLabel = new JLabel(PM_STRING);
        enterPlusMinus = new JTextField(ENTER_STAT_SIZE);
        JPanel basicStatsPanel = createPanel(new JComponent[]{enterGoalsLabel, enterGoals,
                enterAssistsLabel, enterAssists, enterPMLabel, enterPlusMinus});

        enterHitsLabel = new JLabel(HITS_STRING);
        enterHits = new JTextField(ENTER_STAT_SIZE);
        enterPIMLabel = new JLabel(PIM_STRING);
        enterPenaltyMinutes = new JTextField(ENTER_STAT_SIZE);
        JPanel advancedStatsPanel = createPanel(new JComponent[]{enterHitsLabel, enterHits, enterPIMLabel,
                enterPenaltyMinutes});

        enterBlocksLabel = new JLabel(SHOT_BLOCK_STRING);
        enterShotsBlocked = new JTextField(ENTER_STAT_SIZE);
        JPanel defenseStatsPanel = createPanel(new JComponent[]{enterBlocksLabel, enterShotsBlocked});


        enterFaceOffLabel = new JLabel(FACE_OFF_STRING);
        enterFaceOffPercent = new JTextField(ENTER_STAT_SIZE);
        enterFaceOffTotal = new JTextField(ENTER_STAT_SIZE);
        JPanel centerStatsPanel =  createPanel(new JComponent[]{enterFaceOffLabel, enterFaceOffPercent,
                enterFaceOffTotal});

        assignSkaterStats.addActionListener(e -> {
            if (assignSkaterStats.isSelected()) {
                Position position = (Position) positionOptions.getSelectedItem();
                createSkater.add(basicStatsPanel, createSkater.getComponentCount() - 1);
                createSkater.add(advancedStatsPanel, createSkater.getComponentCount() - 1);
                if (position == Position.Center) {
                    createSkater.add(centerStatsPanel, createSkater.getComponentCount() - 1);
                } else if (position == Position.Left_Defense || position == Position.Right_Defense) {
                    createSkater.add(defenseStatsPanel, createSkater.getComponentCount() - 1);
                }
            } else {
                createSkater.remove(basicStatsPanel);
                createSkater.remove(advancedStatsPanel);
                createSkater.remove(centerStatsPanel);
                createSkater.remove(defenseStatsPanel);
            }
            mainFrame.repaint();
        });

        positionOptions.addItemListener(e -> {
            if (assignSkaterStats.isSelected() && e.getStateChange() == ItemEvent.SELECTED) {
                Position position = (Position) positionOptions.getSelectedItem();
                createSkater.remove(centerStatsPanel);
                createSkater.remove(defenseStatsPanel);
                if (position == Position.Center) {
                    createSkater.add(centerStatsPanel, createSkater.getComponentCount() - 1);
                } else if (position == Position.Left_Defense || position == Position.Right_Defense) {
                    createSkater.add(defenseStatsPanel, createSkater.getComponentCount() - 1);
                }
                mainFrame.repaint();
            }
        });

        createPlayer.addActionListener(e -> {
            String name = enterSkaterName.getText();
            String stickHand;
            Skater newSkater;
            if (chooseStickHand.isSelected()) {
                stickHand = "Left";
            } else {
                stickHand = "Right";
            }

            Position position = (Position) positionOptions.getSelectedItem();
            try {
                if (assignSkaterStats.isSelected()) {
                    int goals = Integer.parseInt(enterGoals.getText());
                    int assists = Integer.parseInt(enterAssists.getText());
                    int plusMinus = Integer.parseInt(enterPlusMinus.getText());
                    int hits = Integer.parseInt(enterHits.getText());
                    double penaltyMinutes = Double.parseDouble(enterPenaltyMinutes.getText());
                    if (position == Position.Center) {
                        double percent = Double.parseDouble(enterFaceOffPercent.getText());
                        int total = Integer.parseInt(enterFaceOffTotal.getText());
                        newSkater = new Center(name, enterSkaterNumber.getValue(), stickHand, goals, assists, plusMinus,
                                hits, penaltyMinutes, percent, total);
                    } else if (position == Position.Left_Defense || position == Position.Right_Defense) {
                        int shotsBlocked = Integer.parseInt(enterShotsBlocked.getText());
                        newSkater = new Defenseman(name, enterSkaterNumber.getValue(), stickHand, position, goals,
                                assists, plusMinus, hits, penaltyMinutes, shotsBlocked);
                    } else {
                        newSkater = new Skater(name, enterSkaterNumber.getValue(), stickHand, position, goals, assists,
                                plusMinus, hits, penaltyMinutes);
                    }
                } else {
                    if (position == Position.Center) {
                        newSkater = new Center(name, enterSkaterNumber.getValue(), stickHand);
                    } else if (position == Position.Left_Defense || position == Position.Right_Defense) {
                        newSkater = new Defenseman(name, enterSkaterNumber.getValue(), stickHand, position);
                    } else {
                        newSkater = new Skater(name, enterSkaterNumber.getValue(), stickHand, position);
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(mainFrame, NUMBER_ERROR, "Create Skater", JOptionPane.ERROR_MESSAGE);
                return;
            } catch (IllegalArgumentException | NullPointerException ex) {
                JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "Create Skater",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            int index = team.addPlayer(newSkater);
            if (index == -1) {
                JOptionPane.showMessageDialog(mainFrame, PLAYER_DUPLICATE, "Create Skater",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            updatePlayerComponents(null, newSkater, -1, index);

            try {
                updateFile();
                JOptionPane.showMessageDialog(mainFrame, "Skater successfully created", "Create Skater",
                        JOptionPane.INFORMATION_MESSAGE);

                // Reset Text Fields
                enterSkaterName.setText("");
                if (assignSkaterStats.isSelected()) {
                    enterGoals.setText("");
                    enterAssists.setText("");
                    enterPlusMinus.setText("");
                    enterHits.setText("");
                    enterPenaltyMinutes.setText("");
                    enterFaceOffPercent.setText("");
                    enterFaceOffTotal.setText("");
                    enterShotsBlocked.setText("");
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(selectFrame, FILE_ERROR, "Create Team", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(selectFrame, ex.getMessage(), "Create Team",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        skaterTabs.add("Create Skater", createSkaterScroll);

        // Edit/Delete Skater

        Container editSkaterContent = new Container();
        editSkaterContent.setLayout(new BoxLayout(editSkaterContent, BoxLayout.Y_AXIS));

        editSkaterInstructions = new JLabel(EDIT_PLAYER_INSTRUCTIONS);
        createPanelForContainer(new JComponent[]{editSkaterInstructions}, editSkaterContent);

        // Top Panel (name and number)

        changeSkaterNameLabel = new JLabel(NAME_STRING);
        changeSkaterName = new JTextField(ENTER_NAME_SIZE);
        changeSkaterNumberCheck = new JCheckBox(CHANGE_NUMBER);
        changeSkaterNumber = new JSlider(1, 99);

        JPanel nameAndNumberPanel = createPanel(new JComponent[]{changeSkaterNameLabel, changeSkaterName,
                changeSkaterNumberCheck});
        editSkaterContent.add(nameAndNumberPanel);

        // Adds/Removes proper components when user wants to change the player's number
        changeSkaterNumberCheck.addActionListener(e -> {
            if (changeSkaterNumberCheck.isSelected()) {
                changeSkaterNumberCheck.setText(CHANGE_NUMBER + " " + changeSkaterNumber.getValue());
                nameAndNumberPanel.add(changeSkaterNumber);
            } else {
                changeSkaterNumberCheck.setText(CHANGE_NUMBER);
                nameAndNumberPanel.remove(changeSkaterNumber);
            }
            mainFrame.repaint();
        });

        // Updates label to display currently selected value
        changeSkaterNumber.addChangeListener(e ->
                changeSkaterNumberCheck.setText(CHANGE_NUMBER + " " + changeSkaterNumber.getValue()));

        changePositionCheck = new JCheckBox(CHANGE_POSITION);
        JPanel secondEditSkaterPanel = createPanel(new JComponent[]{changePositionCheck});
        editSkaterContent.add(secondEditSkaterPanel);

        changeStickHandLabel = new JLabel(STICK_HAND_STRING);
        changeStickHand = new JCheckBox("Swap Stick Hand?");
        createPanelForContainer(new JComponent[]{changeStickHandLabel, changeStickHand}, editSkaterContent);

        // Adds position combo box if user wants to change player's position
        changePositionCheck.addActionListener(e -> {
            if (changePositionCheck.isSelected()) {
                secondEditSkaterPanel.add(changePosition, 1);
            } else {
                secondEditSkaterPanel.remove(changePosition);
            }
            mainFrame.repaint();
        });

        changeGoalsLabel = new JLabel(GOALS_STRING);
        changeGoals = new JTextField(ENTER_STAT_SIZE);
        changeAssistsLabel = new JLabel(ASSISTS_STRING);
        changeAssists = new JTextField(ENTER_STAT_SIZE);
        changePMLabel = new JLabel(PM_STRING);
        changePlusMinus = new JTextField(ENTER_STAT_SIZE);
        createPanelForContainer(new JComponent[]{changeGoalsLabel, changeGoals, changeAssistsLabel, changeAssists, changePMLabel,
                changePlusMinus}, editSkaterContent);

        changeHitsLabel = new JLabel(HITS_STRING);
        changeHits = new JTextField(ENTER_STAT_SIZE);
        changePIMLabel = new JLabel(PIM_STRING);
        changePenaltyMinutes = new JTextField(ENTER_STAT_SIZE);
        createPanelForContainer(new JComponent[]{changeHitsLabel, changeHits, changePIMLabel, changePenaltyMinutes},
                editSkaterContent);

        editPlayer = new JButton(UPDATE);
        createPanelForContainer(new JComponent[]{editPlayer}, editSkaterContent);

        resetPlayerStats = new JButton("Reset Skater Stats");
        createPanelForContainer(new JComponent[]{resetPlayerStats}, editSkaterContent);

        deletePlayer = new JButton("Delete Selected Skater");
        createPanelForContainer(new JComponent[]{deletePlayer}, editSkaterContent);

        changeFaceOffLabel = new JLabel(FACE_OFF_STRING);
        changeFaceOffPercent = new JTextField(ENTER_STAT_SIZE);
        changeFaceOffTotal = new JTextField(ENTER_STAT_SIZE);
        JPanel changeCenterPanel = createPanel(new JComponent[]{changeFaceOffLabel, changeFaceOffPercent,
                changeFaceOffTotal});

        changeBlocksLabel = new JLabel(SHOT_BLOCK_STRING);
        changeShotsBlocked = new JTextField(ENTER_STAT_SIZE);
        JPanel changeDefensePanel = createPanel(new JComponent[]{changeBlocksLabel, changeShotsBlocked});

        // Updates stick hand swap button and adds proper components for a center or defenseman
        skaterOptions.addItemListener(e -> {
            Skater newSkater = (Skater) skaterOptions.getSelectedItem();
            if (e.getStateChange() == ItemEvent.SELECTED && newSkater != null) {
                editSkaterContent.remove(changeCenterPanel);
                editSkaterContent.remove(changeDefensePanel);
                changeStickHandLabel.setText(STICK_HAND_STRING + newSkater.getStickHand());
                changePositionCheck.setText(CHANGE_POSITION + newSkater.getPosition());
                viewSkaterStats.setText(newSkater.statsDisplay());
                if (newSkater instanceof Center) {
                    editSkaterContent.add(changeCenterPanel, editSkaterContent.getComponentCount() - 3);
                } else if (newSkater instanceof Defenseman) {
                    editSkaterContent.add(changeDefensePanel, editSkaterContent.getComponentCount() - 3);
                }
                mainFrame.repaint();
            } else if (newSkater == null) {
                changeStickHandLabel.setText(STICK_HAND_STRING);
                changePositionCheck.setText(CHANGE_POSITION);
                viewSkaterStats.setText("");
            }
        });

        // Updates any changes being made to a skater in the edit skaters tab
        editPlayer.addActionListener(e -> {
            Skater editingSkater = (Skater) skaterOptions.getSelectedItem();
            if (editingSkater == null) {  // Did not choose a skater
                JOptionPane.showMessageDialog(mainFrame, SELECT, "Edit Skater", JOptionPane.ERROR_MESSAGE);
                return;
            }
            boolean change = false;  // Used to keep track of whether the user actually makes a change
            try {
                // Name
                String name = changeSkaterName.getText();
                if (!name.isBlank()) {
                    editingSkater.setName(name);
                    changeSkaterName.setText("");
                    change = true;
                }
                // Stick Hand
                if (changeStickHand.isSelected()) {
                    if (editingSkater.getStickHand().equals("Left")) {
                        editingSkater.setStickHand("Right");
                    } else {
                        editingSkater.setStickHand("Left");
                    }
                    changeStickHand.setSelected(false);
                    change = true;
                }
                // Stats
                String goalsText = changeGoals.getText();
                String assistsText = changeAssists.getText();
                String pmText = changePlusMinus.getText();
                String hitsText = changeHits.getText();
                String pimText = changePenaltyMinutes.getText();
                if (!goalsText.isBlank()) {
                    editingSkater.setGoals(Integer.parseInt(goalsText));
                    changeGoals.setText("");
                    change = true;
                }
                if (!assistsText.isBlank()) {
                    editingSkater.setAssists(Integer.parseInt(assistsText));
                    changeAssists.setText("");
                    change = true;
                }
                if (!pmText.isBlank()) {
                    editingSkater.setPlusMinus(Integer.parseInt(pmText));
                    changePlusMinus.setText("");
                    change = true;
                }
                if (!hitsText.isBlank()) {
                    editingSkater.setHits(Integer.parseInt(hitsText));
                    changeHits.setText("");
                    change = true;
                }
                if (!pimText.isBlank()) {
                    editingSkater.setPenaltyMinutes(Double.parseDouble(pimText));
                    changePenaltyMinutes.setText("");
                    change = true;
                }
                if (editingSkater instanceof Center c) {
                    String faceOffPercent = changeFaceOffPercent.getText();
                    if (!faceOffPercent.isBlank()) {
                        c.setFaceOffPercent(Double.parseDouble(faceOffPercent),
                                Integer.parseInt(changeFaceOffTotal.getText()));
                        changeFaceOffPercent.setText("");
                        changeFaceOffTotal.setText("");
                        change = true;
                    }
                }
                if (editingSkater instanceof Defenseman de) {
                    String shotsBlockedText = changeShotsBlocked.getText();
                    if (!shotsBlockedText.isBlank()) {
                        de.setShotsBlocked(Integer.parseInt(shotsBlockedText));
                        changeShotsBlocked.setText("");
                        change = true;
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(mainFrame, NUMBER_ERROR + BLANK_UPDATED, "Edit Skater",
                        JOptionPane.ERROR_MESSAGE);
                return;
            } catch (IllegalArgumentException | NullPointerException ex) {
                JOptionPane.showMessageDialog(mainFrame, ex.getMessage() + BLANK_UPDATED, "Edit Skater",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            Skater newSkater = null;  // Skater where changes to position or player number will be made

            /*
               If position needs to be changed, may need to create an entirely new skater
               This segment handles all the different possibilities for changing a player's position
             */
            if (changePositionCheck.isSelected()) {
                Position newPosition = (Position) changePosition.getSelectedItem();
                if (editingSkater instanceof Center) {
                    if (newPosition == Position.Left_Wing || newPosition == Position.Right_Wing) {
                        newSkater = new Skater(editingSkater);
                        newSkater.setPosition(newPosition);
                        if (changeSkaterNumberCheck.isSelected()) {
                            newSkater.setPlayerNumber(changeSkaterNumber.getValue());
                        }
                        change = true;
                    } else if (newPosition == Position.Right_Defense || newPosition == Position.Left_Defense) {
                        newSkater = new Defenseman(editingSkater);
                        newSkater.setPosition(newPosition);
                        if (changeSkaterNumberCheck.isSelected()) {
                            newSkater.setPlayerNumber(changeSkaterNumber.getValue());
                        }
                        change = true;
                    }
                } else if (editingSkater instanceof Defenseman) {
                    if (newPosition == Position.Center) {
                        newSkater = new Center(editingSkater);
                        if (changeSkaterNumberCheck.isSelected()) {
                            newSkater.setPlayerNumber(changeSkaterNumber.getValue());
                        }
                    } else if (newPosition == Position.Left_Wing || newPosition == Position.Right_Wing) {
                        newSkater = new Skater(editingSkater);
                        newSkater.setPosition(newPosition);
                        if (changeSkaterNumberCheck.isSelected()) {
                            newSkater.setPlayerNumber(changeSkaterNumber.getValue());
                        }
                    } else {
                        editingSkater.setPosition(newPosition);
                    }
                    change = true;
                } else {
                    if (newPosition == Position.Center) {
                        newSkater = new Center(editingSkater);
                        if (changeSkaterNumberCheck.isSelected()) {
                            newSkater.setPlayerNumber(changeSkaterNumber.getValue());
                        }
                    } else if (newPosition == Position.Left_Defense || newPosition == Position.Right_Defense) {
                        newSkater = new Defenseman(editingSkater);
                        newSkater.setPosition(newPosition);
                        if (changeSkaterNumberCheck.isSelected()) {
                            newSkater.setPlayerNumber(changeSkaterNumber.getValue());
                        }
                    } else {
                        editingSkater.setPosition(newPosition);
                    }
                    change = true;
                }
            } else if (changeSkaterNumberCheck.isSelected()) {
                if (editingSkater instanceof Center) {
                    newSkater = new Center(editingSkater);
                } else if (editingSkater instanceof  Defenseman) {
                    newSkater = new Defenseman(editingSkater);
                } else {
                    newSkater = new Skater(editingSkater);
                }
                newSkater.setPlayerNumber(changeSkaterNumber.getValue());
                changeSkaterNumberCheck.setSelected(false);
                changeSkaterNumberCheck.setText(CHANGE_NUMBER);
                change = true;
            }


            if (change) {  // Update GUI components, file, and the team lists if necessary
                int oldIndex = skaterOptions.getSelectedIndex() - 1;  // Index where the skater used to be

                if (newSkater != null) {
                    int index = team.changePlayer(editingSkater, newSkater);
                    if (index == -1) {
                        JOptionPane.showMessageDialog(mainFrame, PLAYER_DUPLICATE, "Edit Skater",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    updatePlayerComponents(editingSkater, newSkater, oldIndex, index);
                } else {
                    updatePlayerComponents(null, editingSkater, oldIndex, oldIndex);
                }

                try {
                    updateFile();
                    JOptionPane.showMessageDialog(mainFrame, "Skater updated successfully", "Edit Skater",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(mainFrame, FILE_ERROR, "Edit Skater", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "Edit Skater",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {  // Displays message telling the user to enter something
                JOptionPane.showMessageDialog(mainFrame, EMPTY_INPUTS, "Edit Skater", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Confirms user's selection and resets stats of the selected player
        resetPlayerStats.addActionListener(e -> {
            Skater selectedSkater = (Skater) skaterOptions.getSelectedItem();
            if (selectedSkater != null) {
                int confirm = JOptionPane.showConfirmDialog(mainFrame, RESET_CONFIRM +
                        selectedSkater.getName() + TO_ZERO, "Edit Skater", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    selectedSkater.resetStats();
                    updatePlayerComponents(null, selectedSkater, skaterOptions.getSelectedIndex() - 1,
                            skaterOptions.getSelectedIndex() - 1);
                    try {
                        updateFile();
                        JOptionPane.showMessageDialog(mainFrame, "Skater successfully updated", "Edit Skater",
                                JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(mainFrame, FILE_ERROR, "Delete Skater",
                                JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "Delete Skater",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(mainFrame, SELECT, "Edit Skater", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Deletes the selected player from the team and any GUI elements
        deletePlayer.addActionListener(e -> {
            Skater deletingSkater = (Skater) skaterOptions.getSelectedItem();
            if (deletingSkater == null) {
                JOptionPane.showMessageDialog(mainFrame, SELECT, "Delete Skater", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int selection = JOptionPane.showConfirmDialog(mainFrame, CONFIRM_DELETE + deletingSkater.getName()
                    + "?", "Delete Skater", JOptionPane.YES_NO_OPTION);
            if (selection == JOptionPane.YES_OPTION) {
                team.removePlayer(deletingSkater);
                updatePlayerComponents(deletingSkater, null, skaterOptions.getSelectedIndex() - 1,
                        -1);

                try {
                    updateFile();
                    JOptionPane.showMessageDialog(mainFrame, "Skater successfully deleted", "Delete Skater",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(mainFrame, FILE_ERROR, "Delete Skater",
                            JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "Delete Skater",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        skaterTabs.add("Edit or Delete Skaters", editSkaterContent);

        // View Skater Stats

        viewSkaterStats = new JTextArea();
        viewSkaterStats.setEditable(false);
        skaterTabs.add("View Skater Stats", viewSkaterStats);

        mainTabs.add("Manage Skaters", manageSkaterContent);

        // Manage Goalies

        Container manageGoalieContent = new Container();
        manageGoalieContent.setLayout(new BoxLayout(manageGoalieContent, BoxLayout.Y_AXIS));
        selectedGoalieLabel = new JLabel(SELECT_GOALIE);
        createPanelForContainer(new JComponent[]{selectedGoalieLabel, goalieOptions}, manageGoalieContent);
        goalieTabs = new JTabbedPane();
        createPanelForContainer(new JComponent[]{goalieTabs}, manageGoalieContent);

        // Create Goalie
        Container createGoalieContent = new Container();
        createGoalieContent.setLayout(new BoxLayout(createGoalieContent, BoxLayout.Y_AXIS));

        enterGoalieNameLabel = new JLabel(NAME_STRING);
        enterGoalieName = new JTextField(ENTER_NAME_SIZE);
        selectGoalieNumber = new JSlider(1, 99);
        enterGoalieNumberLabel = new JLabel(PLAYER_NUMBER_STRING + " " + selectGoalieNumber.getValue());
        createPanelForContainer(new JComponent[]{enterGoalieNameLabel, enterGoalieName, enterGoalieNumberLabel,
                selectGoalieNumber}, createGoalieContent);

        // Updates value displayed by select number value
        selectGoalieNumber.addChangeListener(e ->
                enterGoalieNumberLabel.setText(PLAYER_NUMBER_STRING + " " + selectGoalieNumber.getValue()));

        assignGoalieStats = new JToggleButton("Initialize Goalie Stats");
        createPanelForContainer(new JComponent[]{assignGoalieStats}, createGoalieContent);

        // Enter Record
        enterGoalieWinsLabel = new JLabel(WINS_STRING);
        enterGoalieWins = new JTextField(ENTER_STAT_SIZE);
        enterGoalieLossesLabel = new JLabel(LOSSES_STRING);
        enterGoalieLosses = new JTextField(ENTER_STAT_SIZE);
        enterGoalieOTLabel = new JLabel(OT_STRING);
        enterGoalieOTLosses = new JTextField(ENTER_STAT_SIZE);
        JPanel goalieRecordPanel = createPanel(new JComponent[]{enterGoalieWinsLabel, enterGoalieWins,
                enterGoalieLossesLabel, enterGoalieLosses, enterGoalieOTLabel, enterGoalieOTLosses});

        // Other Stats
        enterShutouts = new JSlider(0, 20);
        enterShutoutsLabel = new JLabel(SHUTOUTS_STRING + " " + enterShutouts.getValue());
        // Updates value displayed by enter shutouts label
        enterShutouts.addChangeListener(e ->
                enterShutoutsLabel.setText(SHUTOUTS_STRING + " " + enterShutouts.getValue()));
        enterSVPercentageLabel = new JLabel(SV_PERCENT_STRING);
        enterSavePercentage = new JTextField(ENTER_STAT_SIZE);
        enterGoalieShotsAgainst = new JTextField(ENTER_STAT_SIZE);
        JPanel otherGoalieStatsPanel = createPanel(new JComponent[]{enterShutoutsLabel, enterShutouts,
                enterSVPercentageLabel, enterSavePercentage, enterGoalieShotsAgainst});

        createGoalie = new JButton("Create Goalie");
        createPanelForContainer(new JComponent[]{createGoalie}, createGoalieContent);

        assignGoalieStats.addActionListener(e -> {
            if (assignGoalieStats.isSelected()) {
                createGoalieContent.add(goalieRecordPanel, createGoalieContent.getComponentCount() - 1);
                createGoalieContent.add(otherGoalieStatsPanel, createGoalieContent.getComponentCount() - 1);
            } else {
                createGoalieContent.remove(goalieRecordPanel);
                createGoalieContent.remove(otherGoalieStatsPanel);
            }
            mainFrame.repaint();
        });

        /*
           Parses stats from relevant text fields, creates new goalie, adds it to the team, and updates relevant GUI
           elements.
         */
        createGoalie.addActionListener(e -> {
            String name = enterGoalieName.getText();
            int goalieNum = selectGoalieNumber.getValue();
            Goalie newGoalie;
            try {
                if (assignGoalieStats.isSelected()) {
                    int wins = Integer.parseInt(enterGoalieWins.getText());
                    int losses = Integer.parseInt(enterGoalieLosses.getText());
                    int otLosses = Integer.parseInt(enterGoalieOTLosses.getText());
                    int shutouts = enterShutouts.getValue();
                    double savePercent = Double.parseDouble(enterSavePercentage.getText());
                    int shotsFaced = Integer.parseInt(enterGoalieShotsAgainst.getText());
                    newGoalie = new Goalie(name, goalieNum, savePercent, shotsFaced, wins, losses, otLosses, shutouts);
                } else {
                    newGoalie = new Goalie(name, goalieNum);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(mainFrame, NUMBER_ERROR, "Create Goalie", JOptionPane.ERROR_MESSAGE);
                return;
            } catch (NullPointerException | IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "Create Goalie",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int index = team.addPlayer(newGoalie);
            if (index == -1) {
                JOptionPane.showMessageDialog(mainFrame, PLAYER_DUPLICATE, "Create Goalie",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                updateFile();
                updatePlayerComponents(null, newGoalie, -1, index);
                enterGoalieName.setText("");
                enterGoalieWins.setText("");
                enterGoalieLosses.setText("");
                enterGoalieOTLosses.setText("");
                enterSavePercentage.setText("");
                enterGoalieShotsAgainst.setText("");
                JOptionPane.showMessageDialog(mainFrame, "Goalie successfully created", "Create Goalie",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(mainFrame, FILE_ERROR, "Create Goalie", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "Create Goalie",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        goalieTabs.add("Create Goalie", createGoalieContent);

        // Edit/Delete Goalie
        Container editGoalieContent = new Container();
        editGoalieContent.setLayout(new BoxLayout(editGoalieContent, BoxLayout.Y_AXIS));

        editGoalieInstructions = new JLabel(EDIT_PLAYER_INSTRUCTIONS);
        createPanelForContainer(new JComponent[]{editGoalieInstructions}, editGoalieContent);
        changeGoalieNameLabel = new JLabel(NAME_STRING);
        changeGoalieName = new JTextField(ENTER_NAME_SIZE);
        changeGoalieNumberCheck = new JCheckBox(CHANGE_NUMBER);
        changeGoalieNumber = new JSlider(1, 99);
        JPanel changeBasicStatsPanel = createPanel(new JComponent[]{changeGoalieNameLabel, changeGoalieName,
                changeGoalieNumberCheck});
        editGoalieContent.add(changeBasicStatsPanel);

        // Displays number slider when user wants to change the goalie's number
        changeGoalieNumberCheck.addActionListener(e -> {
            if (changeGoalieNumberCheck.isSelected()) {
                changeGoalieNumberCheck.setText(CHANGE_NUMBER + " " + changeGoalieNumber.getValue());
                changeBasicStatsPanel.add(changeGoalieNumber);
            } else {
                changeGoalieNumberCheck.setText(CHANGE_NUMBER);
                changeBasicStatsPanel.remove(changeGoalieNumber);
            }
            mainFrame.repaint();
        });

        // Updates value of check box text to reflect current value of the slider
        changeGoalieNumber.addChangeListener(e ->
                changeGoalieNumberCheck.setText(CHANGE_NUMBER + " " + changeGoalieNumber.getValue()));

        changeGoalieWinsLabel = new JLabel(WINS_STRING);
        changeGoalieWins = new JTextField(ENTER_STAT_SIZE);
        changeGoalieLossesLabel = new JLabel(LOSSES_STRING);
        changeGoalieLosses = new JTextField(ENTER_STAT_SIZE);
        changeGoalieOTLabel = new JLabel(OT_STRING);
        changeGoalieOTLosses = new JTextField(ENTER_STAT_SIZE);
        createPanelForContainer(new JComponent[]{changeGoalieWinsLabel, changeGoalieWins, changeGoalieLossesLabel,
                changeGoalieLosses, changeGoalieOTLabel, changeGoalieOTLosses}, editGoalieContent);

        changeShutoutsLabel = new JLabel("Enter Shutouts:");
        changeShutouts = new JTextField(ENTER_STAT_SIZE);
        changeSVPercentageLabel = new JLabel(SV_PERCENT_STRING);
        changeSavePercentage = new JTextField(ENTER_STAT_SIZE);
        changeGoalieShotsAgainst = new JTextField(ENTER_STAT_SIZE);
        createPanelForContainer(new JComponent[]{changeShutoutsLabel, changeShutouts, changeSVPercentageLabel,
                changeSavePercentage, changeGoalieShotsAgainst}, editGoalieContent);

        editGoalie = new JButton(UPDATE);
        createPanelForContainer(new JComponent[]{editGoalie}, editGoalieContent);
        resetGoalieStats = new JButton("Reset Goalie Stats");
        createPanelForContainer(new JComponent[]{resetGoalieStats}, editGoalieContent);
        deleteGoalie = new JButton("Delete Selected Goalie");
        createPanelForContainer(new JComponent[]{deleteGoalie}, editGoalieContent);

        editGoalie.addActionListener(e -> {
            Goalie editingGoalie = (Goalie) goalieOptions.getSelectedItem();
            if (editingGoalie == null) {
                JOptionPane.showMessageDialog(mainFrame, SELECT, "Edit Goalie", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String name = changeGoalieName.getText();
            String wins = changeGoalieWins.getText();
            String losses = changeGoalieLosses.getText();
            String otLosses = changeGoalieOTLosses.getText();
            String shutouts = changeShutouts.getText();
            String svPercent = changeSavePercentage.getText();
            String shotsFaced = changeGoalieShotsAgainst.getText();
            try {
                if (!name.isBlank()) {
                    editingGoalie.setName(name);
                    changeGoalieName.setText("");
                }
                if (!wins.isBlank()) {
                    editingGoalie.setWins(Integer.parseInt(wins));
                    changeGoalieWins.setText("");
                }
                if (!losses.isBlank()) {
                    editingGoalie.setLosses(Integer.parseInt(losses));
                    changeGoalieLosses.setText("");
                }
                if (!otLosses.isBlank()) {
                    editingGoalie.setOtLosses(Integer.parseInt(otLosses));
                    changeGoalieOTLosses.setText("");
                }
                if (!shutouts.isBlank()) {
                    editingGoalie.setShutouts(Integer.parseInt(shutouts));
                    changeShutouts.setText("");
                }
                if (!svPercent.isBlank()) {
                    editingGoalie.setSavePercentage(Double.parseDouble(svPercent), Integer.parseInt(shotsFaced));
                    changeSavePercentage.setText("");
                    changeGoalieShotsAgainst.setText("");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(mainFrame, NUMBER_ERROR, "Edit Goalie", JOptionPane.ERROR_MESSAGE);
                return;
            } catch (NullPointerException | IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "Edit Goalie",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            Goalie newGoalie = null;

            if (changeGoalieNumberCheck.isSelected()) {
                newGoalie = new Goalie(editingGoalie);
                newGoalie.setPlayerNumber(changeGoalieNumber.getValue());
            }

            int oldIndex = goalieOptions.getSelectedIndex() - 1;
            if (newGoalie != null) {
                int index = team.changePlayer(editingGoalie, newGoalie);
                if (index == -1) {
                    JOptionPane.showMessageDialog(mainFrame, PLAYER_DUPLICATE, "Edit Goalie",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                updatePlayerComponents(editingGoalie, newGoalie, oldIndex, index);
            } else {
                updatePlayerComponents(null, editingGoalie, oldIndex, oldIndex);
            }

            try {
                updateFile();
                JOptionPane.showMessageDialog(mainFrame, "Goalie Successfully Updated",
                        "Edit Goalie", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(mainFrame, FILE_ERROR, "Edit Goalie", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "Edit Goalie", JOptionPane.ERROR_MESSAGE);
            }
        });

        resetGoalieStats.addActionListener(e -> {
            Goalie editingGoalie = (Goalie) goalieOptions.getSelectedItem();
            if (editingGoalie == null) {
                JOptionPane.showMessageDialog(mainFrame, SELECT, "Edit Goalie", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(mainFrame, RESET_CONFIRM + editingGoalie.getName() +
                    TO_ZERO, "Edit Goalie", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                editingGoalie.resetStats();
                updatePlayerComponents(null, editingGoalie, goalieOptions.getSelectedIndex() - 1,
                        goalieOptions.getSelectedIndex() - 1);
                try {
                    updateFile();
                    JOptionPane.showMessageDialog(mainFrame, "Goalie Successfully Updated", "Edit Goalie",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(mainFrame, FILE_ERROR, "Edit Goalie", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "Edit Goalie",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        deleteGoalie.addActionListener(e -> {
            Goalie deletingGoalie = (Goalie) goalieOptions.getSelectedItem();
            if (deletingGoalie == null) {
                JOptionPane.showMessageDialog(mainFrame, SELECT, "Delete Goalie", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(mainFrame, CONFIRM_DELETE + deletingGoalie.getName()
                    + "?", "Delete Goalie", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                team.removePlayer(deletingGoalie);
                updatePlayerComponents(deletingGoalie, null, goalieOptions.getSelectedIndex() - 1,
                        -1);
                try {
                    updateFile();
                    JOptionPane.showMessageDialog(mainFrame, "Goalie Successfully Deleted",
                            "Delete Goalie", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(mainFrame, FILE_ERROR, "Delete Goalie",
                            JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "Delete Goalie",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        goalieTabs.add("Edit / Delete Goalie", editGoalieContent);

        // View Goalie Stats
        viewGoalieStats = new JTextArea();
        viewGoalieStats.setEditable(false);
        goalieTabs.add("View Goalie Stats", viewGoalieStats);

        // Updates view goalie stats text area with proper stats
        goalieOptions.addItemListener(e -> {
            Goalie selectedGoalie = (Goalie) goalieOptions.getSelectedItem();
            if (e.getStateChange() == ItemEvent.SELECTED && selectedGoalie != null) {
                viewGoalieStats.setText(selectedGoalie.statsDisplay());
            } else if (selectedGoalie == null) {
                viewGoalieStats.setText("");
            }
            mainFrame.repaint();
        });

        mainTabs.add("Manage Goalies", manageGoalieContent);

        // Enter Game Stats

        enterStatsTabs = new JTabbedPane();

        // Live
        Container liveStats = new Container();
        liveStats.setLayout(new BoxLayout(liveStats, BoxLayout.Y_AXIS));

        offenseLinesLabel = new JLabel("Select Current Offense Line:");
        JPanel offenseLinePanel = createPanel(new JComponent[]{offenseLinesLabel, offenseLines});
        liveStats.add(offenseLinePanel);

        deLinesLabel = new JLabel("Select Current Defense Pair:");
        JPanel defenseLinePanel = createPanel(new JComponent[]{deLinesLabel, defenseLines});
        liveStats.add(defenseLinePanel);

        goaliesForStatsLabel = new JLabel("Select Goalie in Net:");
        JPanel selectGoaliePanel = createPanel(new JComponent[]{goaliesForStatsLabel, selectGoaliesForStats});
        liveStats.add(selectGoaliePanel);

        AtomicInteger teamGoals = new AtomicInteger();
        AtomicInteger opponentGoals = new AtomicInteger();
        currentScore = new JLabel(CURRENT_SCORE + teamGoals.get() + "-" +
                opponentGoals.get());
        createPanelForContainer(new JComponent[]{currentScore}, liveStats);

        goalLive = new JButton("Team Scored");
        scoredAgainstLive = new JButton("Opponent Scored");
        createPanelForContainer(new JComponent[]{goalLive, scoredAgainstLive}, liveStats);

        scorerLabel = new JLabel("Position of Scorer:");
        JPanel scorerPanel = createPanel(new JComponent[]{scorerLabel, scorerOptions});

        assistLabel1 = new JLabel("Position of Assist 1 (if assisted):");
        JPanel assistPanel1 = createPanel(new JComponent[]{assistLabel1, assistOptions1});

        assistLabel2 = new JLabel("Position of Assist 2 (if assisted):");
        JPanel assistPanel2 = createPanel(new JComponent[]{assistLabel2, assistOptions2});

        playerScoreLabel = new JLabel("Select Scorer:");
        JPanel playerScorePanel = createPanel(new JComponent[]{playerScoreLabel, scorerPlayerOptions});

        otherPlayersLabel = new JLabel("Select Other Skaters on the Ice:");
        JPanel otherPlayersPanel = new JPanel();
        otherPlayersPanel.add(otherPlayersLabel);

        assist1Check = new JCheckBox("This Player Assisted");
        JPanel assistPlayerPanel1 = createPanel(new JComponent[]{assistPlayerOptions1, assist1Check});

        assist2Check = new JCheckBox("This Player Assisted");
        JPanel assistPlayerPanel2 = createPanel(new JComponent[]{assistPlayerOptions2, assist2Check});

        JPanel selectOtherPlayersPanel = createPanel(new JComponent[]{otherPlayerOptions1,
                otherPlayerOptions2});

        ppLineLabel = new JLabel("Select Power Play Line:");
        JPanel ppLinePanel = createPanel(new JComponent[]{ppLineLabel, ppOptions});

        pkOptionsLabel = new JLabel("Select Penalty Kill Line:");
        JPanel pkLinePanel = createPanel(new JComponent[]{pkOptionsLabel, pkOptions});

        useLinesOrPlayers = new JToggleButton("Select Players Manually");
        JPanel linesOrPlayersPanel = new JPanel();
        linesOrPlayersPanel.add(useLinesOrPlayers);

        penaltyOver = new JButton("Penalty Expired");
        JPanel penaltyOverPanel = new JPanel();
        penaltyOverPanel.add(penaltyOver);

        goalLive.addActionListener(e -> {
            mainFrame.setVisible(false);
            JWindow enterGoalLive = new JWindow(mainFrame);
            enterGoalLive.setLocationRelativeTo(mainFrame);
            Container liveGoalContent = enterGoalLive.getContentPane();
            liveGoalContent.setLayout(new BoxLayout(liveGoalContent, BoxLayout.Y_AXIS));

            liveGoalContent.add(linesOrPlayersPanel);
            useLinesOrPlayers.setText(USE_PLAYERS);
            useLinesOrPlayers.setSelected(false);

            JTextArea lineRosterArea = new JTextArea();
            lineRosterArea.setEditable(false);
            JPanel lineRosterPanel = new JPanel();
            lineRosterPanel.add(lineRosterArea);
            liveGoalContent.add(lineRosterPanel);

            liveGoalContent.add(scorerPanel);
            liveGoalContent.add(assistPanel1);
            liveGoalContent.add(assistPanel2);

            JButton enterGoalButton = new JButton("Enter Goal");
            JButton cancelButton = new JButton("Cancel");
            createPanelForContainer(new JComponent[]{enterGoalButton, cancelButton}, liveGoalContent);

            Line scoringLine;
            if (powerPlayLive.isSelected()) {
                scoringLine = (PPLine) ppOptions.getSelectedItem();
                if (scoringLine != null) {
                    lineRosterArea.setText(scoringLine.lineRoster());
                }
            } else if (penaltyLive.isSelected()) {
                scoringLine = (PKLine) pkOptions.getSelectedItem();
                if (scoringLine != null) {
                    lineRosterArea.setText(scoringLine.lineRoster());
                }
            } else {
                scoringLine = (OffenseLine) offenseLines.getSelectedItem();
                DefenseLine dLine = (DefenseLine) defenseLines.getSelectedItem();
                if (scoringLine != null && dLine != null) {
                    lineRosterArea.setText(scoringLine.lineRoster() + '\n' + dLine.lineRoster());
                }
            }

            useLinesOrPlayers.addActionListener(e1 -> {
                if (useLinesOrPlayers.isSelected()) {
                    useLinesOrPlayers.setText(USE_LINES);
                    liveGoalContent.remove(lineRosterPanel);
                    liveGoalContent.remove(scorerPanel);
                    liveGoalContent.remove(assistPanel1);
                    liveGoalContent.remove(assistPanel2);

                    liveGoalContent.add(selectOtherPlayersPanel, 1);
                    liveGoalContent.add(assistPlayerPanel2, 1);
                    liveGoalContent.add(assistPlayerPanel1, 1);
                    liveGoalContent.add(otherPlayersPanel, 1);
                    liveGoalContent.add(playerScorePanel, 1);
                } else {
                    useLinesOrPlayers.setText(USE_PLAYERS);
                    liveGoalContent.remove(selectOtherPlayersPanel);
                    liveGoalContent.remove(assistPlayerPanel2);
                    liveGoalContent.remove(assistPlayerPanel1);
                    liveGoalContent.remove(otherPlayersPanel);
                    liveGoalContent.remove(playerScorePanel);

                    liveGoalContent.add(assistPanel2, 1);
                    liveGoalContent.add(assistPanel1, 1);
                    liveGoalContent.add(scorerPanel, 1);
                    liveGoalContent.add(lineRosterPanel, 1);
                }
                enterGoalLive.pack();
            });

            ActionListener enterGoal = e1 -> {
                if (e1.getActionCommand().equals(enterGoalButton.getActionCommand())) {
                    if (useLinesOrPlayers.isSelected()) {
                        Skater scorer = (Skater) scorerPlayerOptions.getSelectedItem();
                        Skater assist1 = (Skater) assistPlayerOptions1.getSelectedItem();
                        Skater assist2 = (Skater) assistPlayerOptions2.getSelectedItem();
                        Skater other1 = (Skater) otherPlayerOptions1.getSelectedItem();
                        Skater other2 = (Skater) otherPlayerOptions2.getSelectedItem();
                        if (scorer == null || assist1 == null || assist2 == null || other1 == null || other2 == null) {
                            JOptionPane.showMessageDialog(enterGoalLive, SELECT, "Enter Goal Live",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        if (scorer.equals(assist1) || scorer.equals(assist2) || scorer.equals(other1) ||
                                scorer.equals(other2) || assist1.equals(assist2) || assist1.equals(other1)
                                || assist1.equals(other2) || assist2.equals(other1) ||
                                assist2.equals(other2) || other1.equals(other2)) {
                            JOptionPane.showMessageDialog(enterGoalLive, "Selected Players must " +
                                    "be different", "Enter Goals", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        scorer.score();
                        if (assist1Check.isSelected()) {
                            assist1.assist();
                        } else {
                            assist1.scoredOnIce();
                        }
                        if (assist2Check.isSelected()) {
                            assist2.assist();
                        } else {
                            assist2.scoredOnIce();
                        }
                        other1.scoredOnIce();
                        other2.scoredOnIce();
                    } else {
                        if (scoringLine != null) {
                            Position scorer = (Position) scorerOptions.getSelectedItem();
                            Position assist1 = (Position) assistOptions1.getSelectedItem();
                            Position assist2 = (Position) assistOptions2.getSelectedItem();
                            if (assist1 == null && assist2 != null) {
                                JOptionPane.showMessageDialog(enterGoalLive, "Please select Assist 1 " +
                                        "Instead of Assist 2", "Enter Goal Live", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            while (true) {
                                try {
                                    if (scoringLine instanceof OffenseLine oLine) {
                                        DefenseLine dLine = (DefenseLine) defenseLines.getSelectedItem();
                                        if (dLine == null) {
                                            JOptionPane.showMessageDialog(enterGoalLive, "There are currently" +
                                                    " no lines to select from. Please select players manually or " +
                                                    "create a line.", "Enter Goal Live",
                                                    JOptionPane.ERROR_MESSAGE);
                                            return;
                                        }
                                        if (assist2 != null) {
                                            oLine.score(scorer, assist1, assist2, dLine);
                                        } else if (assist1 != null) {
                                            oLine.score(scorer, assist1, dLine);
                                        } else {
                                            oLine.score(scorer, dLine);
                                        }
                                    } else {
                                        SpecialTeamsLine specialTeams = (SpecialTeamsLine) scoringLine;
                                        if (assist2 != null) {
                                            specialTeams.score(scorer, assist1, assist2);
                                        } else if (assist1 != null) {
                                            specialTeams.score(scorer, assist1);
                                        } else {
                                            specialTeams.score(scorer);
                                        }
                                        if (specialTeams instanceof PPLine) {
                                            specialTeams.success();
                                            penaltyOptionsLive.clearSelection();
                                            liveStats.remove(ppLinePanel);
                                            liveStats.remove(penaltyOverPanel);
                                            liveStats.add(defenseLinePanel, 0);
                                            liveStats.add(offenseLinePanel, 0);
                                            mainFrame.repaint();
                                        }
                                    }
                                    break;
                                } catch (IllegalArgumentException ex) {
                                    JOptionPane.showMessageDialog(enterGoalLive, ex.getMessage(), "Enter Goal Live",
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(enterGoalLive, "There are currently no lines to " +
                                    "select from. Please select players manually or create a line.",
                                    "Enter Goal Live", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                    teamGoals.getAndIncrement();
                    currentScore.setText(CURRENT_SCORE + teamGoals.get() + '-' + opponentGoals.get());
                }
                enterGoalLive.dispose();
            };

            enterGoalButton.addActionListener(enterGoal);
            cancelButton.addActionListener(enterGoal);

            enterGoalLive.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    mainFrame.setVisible(true);
                }
            });

            enterGoalLive.pack();
            enterGoalLive.setVisible(true);
        });

        scoredAgainstLive.addActionListener(e -> {
            if (penaltyLive.isSelected()) {
                PKLine failedLine = (PKLine) pkOptions.getSelectedItem();
                if (failedLine != null) {
                    failedLine.failure();
                    penaltyOptionsLive.clearSelection();
                    liveStats.remove(pkLinePanel);
                    liveStats.remove(penaltyOverPanel);
                    liveStats.add(defenseLinePanel, 0);
                    liveStats.add(offenseLinePanel, 0);
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "There are currently no lines to select from. " +
                            "Please create a penalty kill line.", "Enter Stats Live", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            Goalie goalieInNet = (Goalie) selectGoaliesForStats.getSelectedItem();
            if (goalieInNet != null) {
                goalieInNet.scoredOn();
            }
            opponentGoals.getAndIncrement();
            currentScore.setText(CURRENT_SCORE + teamGoals.get() + '-' + opponentGoals.get());
            mainFrame.repaint();
        });

        shotAgainstOnGoalLive = new JButton("Save by your Goalie");
        createPanelForContainer(new JComponent[]{shotAgainstOnGoalLive}, liveStats);
        shotAgainstOnGoalLive.addActionListener(e -> {
            Goalie goalieInNet = (Goalie) selectGoaliesForStats.getSelectedItem();
            if (goalieInNet != null) {
                goalieInNet.save();
            }
        });

        faceOffLive = new JButton("Face Off");
        createPanelForContainer(new JComponent[]{faceOffLive}, liveStats);
        faceOffLive.addActionListener(e -> {
            int input;
            do {
                input = JOptionPane.showConfirmDialog(mainFrame, "Did your center win this face off?",
                        "Enter Stats Live", JOptionPane.YES_NO_OPTION);
            } while (input != JOptionPane.YES_OPTION && input != JOptionPane.NO_OPTION);

            if (powerPlayLive.isSelected()) {
                PPLine line = (PPLine) ppOptions.getSelectedItem();
                if (line != null) {
                    if (input == JOptionPane.YES_OPTION) {
                        line.winFaceOff();
                    } else {
                        line.loseFaceOff();
                    }
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "You do not currently have a line to select. " +
                            "Please create a new line.", "Enter Stats Live", JOptionPane.ERROR_MESSAGE);
                }
            } else if (penaltyLive.isSelected()) {
                PKLine line = (PKLine) pkOptions.getSelectedItem();
                if (line != null) {
                    Skater[] skaters = line.getSkaters();
                    if (skaters[0] instanceof Center c1 && skaters[1] instanceof Center c2) {
                        int center;
                        do {
                            center = JOptionPane.showOptionDialog(mainFrame, "Please select the player taking" +
                                            " the face off.", "Enter Stats Live", JOptionPane.DEFAULT_OPTION,
                                    JOptionPane.QUESTION_MESSAGE, null, new Skater[]{skaters[0], skaters[1]},
                                    0);
                        } while (center != 0 && center != 1);
                        if (center == 0) {
                            if (input == JOptionPane.YES_OPTION) {
                                c1.winFaceOff();
                            } else {
                                c1.loseFaceOff();
                            }
                        } else {
                            if (input == JOptionPane.YES_OPTION) {
                                c2.winFaceOff();
                            } else {
                                c2.loseFaceOff();
                            }
                        }
                    } else if (skaters[0] instanceof Center c) {
                        c.winFaceOff();
                    } else if (skaters[1] instanceof Center c) {
                        c.winFaceOff();
                    } else {
                        JOptionPane.showMessageDialog(mainFrame, "This line does not have a center that can " +
                                "save their face off win rate.\n Please select another line or change the position of" +
                                        " one of these players to a center.", "Enter Stats Live",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "You do not currently have a line to select. " +
                            "Please create a new line.", "Enter Stats Live", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                OffenseLine line = (OffenseLine) offenseLines.getSelectedItem();
                if (line != null) {
                    if (input == JOptionPane.YES_OPTION) {
                        line.winFaceOff();
                    } else {
                        line.loseFaceOff();
                    }
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "You do not currently have a line to select. " +
                            "Please create a new line.", "Enter Stats Live", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        shotBlockLive = new JButton("Shot Block");
        hitLive = new JButton("Hit");
        createPanelForContainer(new JComponent[]{shotBlockLive, hitLive}, liveStats);
        shotBlockLive.addActionListener(e -> {
            Line currentLine;
            if (powerPlayLive.isSelected()) {
                currentLine = (Line) ppOptions.getSelectedItem();
            } else if (penaltyLive.isSelected()) {
                currentLine = (Line) pkOptions.getSelectedItem();
            } else {
                currentLine = (Line) defenseLines.getSelectedItem();
            }
            if (currentLine == null) {
                JOptionPane.showMessageDialog(mainFrame, "You do not currently have a line to select. Please" +
                        " create a new line.", "Enter Stats Live", JOptionPane.ERROR_MESSAGE);
                return;
            }
            mainFrame.setVisible(false);
            JWindow shotBlockWindow = new JWindow();
            shotBlockWindow.setLocationRelativeTo(mainFrame);
            Container shotBlockContent = shotBlockWindow.getContentPane();
            shotBlockContent.setLayout(new BoxLayout(shotBlockContent, BoxLayout.Y_AXIS));

            JLabel selectBlockerLabel = new JLabel("Select Defenseman That Blocked The Shot:");
            JComboBox<Defenseman> blockerOptions = new JComboBox<>();
            Skater[] skaters = currentLine.getSkaters();
            if (currentLine instanceof DefenseLine) {
                blockerOptions.addItem((Defenseman) skaters[0]);
                blockerOptions.addItem((Defenseman) skaters[1]);
            } else if (currentLine instanceof PPLine) {
                blockerOptions.addItem((Defenseman) skaters[3]);
                blockerOptions.addItem((Defenseman) skaters[4]);
            } else if (currentLine instanceof PKLine) {
                blockerOptions.addItem((Defenseman) skaters[2]);
                blockerOptions.addItem((Defenseman) skaters[3]);
            }
            JPanel selectBlockerPanel = createPanel(new JComponent[]{selectBlockerLabel, blockerOptions});
            shotBlockContent.add(selectBlockerPanel);

            JToggleButton selectOtherDefense = new JToggleButton("Select a Different Defenseman");
            createPanelForContainer(new JComponent[]{selectOtherDefense}, shotBlockContent);
            selectOtherDefense.addActionListener(e1 -> {
                if (selectOtherDefense.isSelected()) {
                    shotBlockContent.remove(selectBlockerPanel);
                    shotBlockContent.add(selectLDPanel, 0);
                    selectLDLabel.setText("Select Defenseman that blocked the shot");
                } else {
                    shotBlockContent.remove(selectLDPanel);
                    shotBlockContent.add(selectBlockerPanel, 0);
                }
                shotBlockWindow.pack();
                shotBlockWindow.repaint();
            });

            JButton enterShotBlock = new JButton("Enter Shot Block");
            JButton cancel = new JButton("Cancel");
            createPanelForContainer(new JComponent[]{enterShotBlock, cancel}, shotBlockContent);

            ActionListener enterShotBlockAction = e1 -> {
                if (e1.getActionCommand().equals(enterShotBlock.getActionCommand())) {
                    Defenseman blocker;
                    if (selectOtherDefense.isSelected()) {
                        blocker = (Defenseman) pickLeftDe.getSelectedItem();
                    } else {
                        blocker = (Defenseman) blockerOptions.getSelectedItem();
                    }
                    if (blocker == null) {
                        JOptionPane.showMessageDialog(mainFrame, SELECT, "Enter Stats Live",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    blocker.blockedShot();
                }
                shotBlockWindow.dispose();
            };
            enterShotBlock.addActionListener(enterShotBlockAction);
            cancel.addActionListener(enterShotBlockAction);

            shotBlockWindow.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    mainFrame.setVisible(true);
                    selectLDLabel.setText(LEFT_DE);
                }
            });

            shotBlockWindow.pack();
            shotBlockWindow.setVisible(true);
        });
        hitLive.addActionListener(e -> {
            Line currentLine;
            DefenseLine defenseLine = null;
            if (powerPlayLive.isSelected()) {
                currentLine = (Line) ppOptions.getSelectedItem();
            } else if (penaltyLive.isSelected()) {
                currentLine = (Line) pkOptions.getSelectedItem();
            } else {
                currentLine = (Line) offenseLines.getSelectedItem();
                defenseLine = (DefenseLine) defenseLines.getSelectedItem();
            }
            if (currentLine == null) {
                JOptionPane.showMessageDialog(mainFrame, SELECT, "Enter Stats Live", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Skater[] skaters = currentLine.getSkaters();

            mainFrame.setVisible(false);
            JWindow hitWindow = new JWindow();
            hitWindow.setLocationRelativeTo(mainFrame);
            Container hitContent = hitWindow.getContentPane();
            hitContent.setLayout(new BoxLayout(hitContent, BoxLayout.Y_AXIS));

            JLabel selectHitterLabel = new JLabel("Select Player Who Made The Hit:");
            JComboBox<Skater> hitterOptions = new JComboBox<>(skaters);
            if (defenseLine != null) {
                skaters = defenseLine.getSkaters();
                hitterOptions.addItem(skaters[0]);
                hitterOptions.addItem(skaters[1]);
            }
            JPanel selectHitterPanel = createPanel(new JComponent[]{selectHitterLabel, hitterOptions});
            hitContent.add(selectHitterPanel);

            JToggleButton useOtherSkater = new JToggleButton("Select A Different Skater");
            createPanelForContainer(new JComponent[]{useOtherSkater}, hitContent);
            useOtherSkater.addActionListener(e1 -> {
                if (useOtherSkater.isSelected()) {
                    hitContent.remove(selectHitterPanel);
                    hitContent.add(playerScorePanel, 0);
                    playerScoreLabel.setText("Select Player Who Made The Hit:");
                } else {
                    hitContent.remove(playerScorePanel);
                    hitContent.add(selectHitterPanel, 0);
                }
                hitWindow.pack();
                hitWindow.repaint();
            });

            JButton enterHit = new JButton("Enter Hit");
            JButton cancel = new JButton("Cancel");
            createPanelForContainer(new JComponent[]{enterHit, cancel}, hitContent);

            ActionListener enterHitAction = e1 -> {
                if (e1.getActionCommand().equals(enterHit.getActionCommand())) {
                    Skater hitter;
                    if (useOtherSkater.isSelected()) {
                        hitter = (Skater) scorerPlayerOptions.getSelectedItem();
                    } else {
                        hitter = (Skater) hitterOptions.getSelectedItem();
                    }
                    if (hitter == null) {
                        JOptionPane.showMessageDialog(mainFrame, SELECT, "Enter Stats Live",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    hitter.hit();
                }
                hitWindow.dispose();
            };

            enterHit.addActionListener(enterHitAction);
            cancel.addActionListener(enterHitAction);

            hitWindow.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    mainFrame.setVisible(true);
                    playerScoreLabel.setText("Select Scorer:");
                }
            });

            hitWindow.pack();
            hitWindow.setVisible(true);
        });

        penaltyOptionsLive = new ButtonGroup();
        powerPlayLive = new JToggleButton("Power Play");
        penaltyLive = new JToggleButton("Penalty Kill");
        penaltyOptionsLive.add(powerPlayLive);
        penaltyOptionsLive.add(penaltyLive);
        penaltyLengthLabel = new JLabel("Enter Penalty Length:");
        penaltyLengthField = new JTextField("2", ENTER_STAT_SIZE);
        createPanelForContainer(new JComponent[]{powerPlayLive, penaltyLive, penaltyLengthLabel, penaltyLengthField},
                liveStats);

        ActionListener penaltiesListener = e -> {
            if (ppOptions.getItemCount() == 0 || pkOptions.getItemCount() == 0) {
                JOptionPane.showMessageDialog(mainFrame, "You must have at least one power play line and one" +
                        "penalty kill line in order to use these features.", "Enter Stats Live",
                        JOptionPane.ERROR_MESSAGE);
                penaltyOptionsLive.clearSelection();
                return;
            }
            boolean penalty = e.getActionCommand().equals(penaltyLive.getActionCommand());
            if (penalty && scorerPlayerOptions.getItemCount() == 0) {
                JOptionPane.showMessageDialog(mainFrame, "You must have a skater who can serve the penalty",
                        "Enter Stats Live", JOptionPane.ERROR_MESSAGE);
                penaltyOptionsLive.clearSelection();
                return;
            }
            if (penalty) {  // Adds penalty minutes to player serving the penalty
                double penaltyLength;
                try {
                    penaltyLength = Double.parseDouble(penaltyLengthField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(mainFrame, NUMBER_ERROR, "Enter Stats Live",
                            JOptionPane.ERROR_MESSAGE);
                    penaltyOptionsLive.clearSelection();
                    return;
                }
                mainFrame.setVisible(false);
                JWindow selectPlayer = new JWindow();
                Container selectPlayerContent = selectPlayer.getContentPane();
                selectPlayerContent.setLayout(new BoxLayout(selectPlayerContent, BoxLayout.Y_AXIS));
                selectPlayer.setLocationRelativeTo(mainFrame);
                playerScoreLabel.setText("Select Player Serving Penalty:");
                selectPlayerContent.add(playerScorePanel);
                JButton confirmSelection = new JButton("Select this Player");
                selectPlayerContent.add(confirmSelection);
                selectPlayer.pack();
                selectPlayer.setVisible(true);
                confirmSelection.addActionListener(e1 -> {
                    Skater guiltyPlayer = (Skater) scorerPlayerOptions.getSelectedItem();
                    if (guiltyPlayer != null) {
                        guiltyPlayer.penalty(penaltyLength);
                    }
                    selectPlayer.dispose();
                    playerScoreLabel.setText("Select Scorer:");
                    mainFrame.setVisible(true);
                });
            }
            liveStats.remove(offenseLinePanel);
            liveStats.remove(defenseLinePanel);
            liveStats.remove(penaltyOverPanel);
            if (e.getActionCommand().equals(powerPlayLive.getActionCommand())) {
                liveStats.remove(pkLinePanel);
                liveStats.add(ppLinePanel, 0);
            } else if (penalty) {
                liveStats.remove(ppLinePanel);
                liveStats.add(pkLinePanel, 0);
            }
            liveStats.add(penaltyOverPanel, liveStats.getComponentCount() - 1);
            mainFrame.repaint();
        };

        powerPlayLive.addActionListener(penaltiesListener);
        penaltyLive.addActionListener(penaltiesListener);

        penaltyOver.addActionListener(e -> {
            if (powerPlayLive.isSelected()) {
                PPLine failedLine = (PPLine) ppOptions.getSelectedItem();
                if (failedLine != null) {
                    failedLine.failure();
                    liveStats.remove(ppLinePanel);
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "You must have at least one power play line. " +
                            "Please create a power play line.", "Enter Stats Live", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else if (penaltyLive.isSelected()) {
                PKLine successLine = (PKLine) pkOptions.getSelectedItem();
                if (successLine != null) {
                    successLine.success();
                    liveStats.remove(pkLinePanel);
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "You must have at least one penalty kill line. " +
                            "Please create a penalty kill line.", "Enter Stats Live", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            liveStats.add(defenseLinePanel, 0);
            liveStats.add(offenseLinePanel, 0);
            liveStats.remove(penaltyOverPanel);
            penaltyOptionsLive.clearSelection();
            mainFrame.repaint();
        });

        gameOver = new JButton("Game Over");
        createPanelForContainer(new JComponent[]{gameOver}, liveStats);
        gameOver.addActionListener(e -> {
            if (powerPlayLive.isSelected() || penaltyLive.isSelected()) {
                JOptionPane.showMessageDialog(mainFrame, "Please finish all power plays/penalty kills before" +
                        " finishing the game.", "Enter Stats Live", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Goalie mainGoalie = (Goalie) selectGoaliesForStats.getSelectedItem();
            if (mainGoalie == null) {
                JOptionPane.showMessageDialog(mainFrame, "Please select the goalie that should receive credit" +
                        " on their record for this game.", "Enter Stats Live", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (teamGoals.get() > opponentGoals.get()) {
                team.win();
                mainGoalie.win();
            } else if (teamGoals.get() == opponentGoals.get()) {
                team.tie();
                mainGoalie.loseOT();
            } else if (teamGoals.get() == opponentGoals.get() - 1) {
                int input;
                do {
                    input = JOptionPane.showConfirmDialog(mainFrame, "Was this an overtime loss?",
                            "Enter Stats Live", JOptionPane.YES_NO_OPTION);
                } while (input != JOptionPane.YES_OPTION && input != JOptionPane.NO_OPTION);
                if (input == JOptionPane.YES_OPTION) {
                    team.tie();
                    mainGoalie.loseOT();
                } else {
                    team.lose();
                    mainGoalie.lose();
                }
            } else {
                team.lose();
                mainGoalie.lose();
            }

            teamGoals.set(0);
            opponentGoals.set(0);
            currentScore.setText(CURRENT_SCORE + "0-0");
            updateEntireTeamComponents();
            try {
                updateFile();
                JOptionPane.showMessageDialog(mainFrame, "Stats successfully Updated", "Enter Stats Live",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(mainFrame, FILE_ERROR, "Enter Stats Live", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "Enter Stats Live",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        enterStatsTabs.add("Enter Live", liveStats);

        // Post Game
        Container postGameStats = new Container();
        postGameStats.setLayout(new BoxLayout(postGameStats, BoxLayout.Y_AXIS));
        finalScorePost = new JLabel("Enter Final Score: (Your Team - Opponent)");
        finalScoreTeam = new JTextField("0", ENTER_STAT_SIZE);
        finalScoreOpp = new JTextField("0", ENTER_STAT_SIZE);
        createPanelForContainer(new JComponent[]{finalScorePost, finalScoreTeam, new JLabel("-"), finalScoreOpp},
                postGameStats);

        postGameShotsAgainst = new JSlider(1, POST_GAME_MAX, 1);
        postGameShotsLabel = new JLabel(POST_SHOTS_AGAINST + postGameShotsAgainst.getValue());
        createPanelForContainer(new JComponent[]{postGameShotsLabel, postGameShotsAgainst}, postGameStats);
        postGameShotsAgainst.addChangeListener(e ->
                postGameShotsLabel.setText(POST_SHOTS_AGAINST + postGameShotsAgainst.getValue()));

        postGameFaceOffLabel = new JLabel("Enter Face Off Stats: (Wins - Losses)");
        postGameFaceOffWins = new JTextField("0", ENTER_STAT_SIZE);
        postGameFaceOffLosses = new JTextField("0", ENTER_STAT_SIZE);
        createPanelForContainer(new JComponent[]{postGameFaceOffLabel, postGameFaceOffWins, new JLabel("-"),
                postGameFaceOffLosses}, postGameStats);

        postGameShotsBlocked = new JSlider(0, POST_GAME_MAX, 0);
        postGameShotsBlockedLabel = new JLabel(POST_SHOTS_BLOCKED + postGameShotsBlocked.getValue());
        createPanelForContainer(new JComponent[]{postGameShotsBlockedLabel, postGameShotsBlocked}, postGameStats);
        postGameShotsBlocked.addChangeListener(e ->
                postGameShotsBlockedLabel.setText(POST_SHOTS_BLOCKED + postGameShotsBlocked.getValue()));

        postGameHits = new JSlider(0, POST_GAME_MAX, 0);
        postGameHitsLabel = new JLabel(POST_HITS + postGameHits.getValue());
        createPanelForContainer(new JComponent[]{postGameHitsLabel, postGameHits}, postGameStats);
        postGameHits.addChangeListener(e ->
                postGameHitsLabel.setText(POST_HITS + postGameHits.getValue()));

        postGamePowerPlay = new JSlider(0, 15, 0);
        postGamePowerPlayLabel = new JLabel(ENTER_POWER_PLAY + postGamePowerPlay.getValue());
        createPanelForContainer(new JComponent[]{postGamePowerPlayLabel, postGamePowerPlay}, postGameStats);
        postGamePowerPlay.addChangeListener(e ->
                postGamePowerPlayLabel.setText(ENTER_POWER_PLAY + postGamePowerPlay.getValue()));

        postGamePenalties = new JSlider(0, 15, 0);
        postGamePenaltiesLabel = new JLabel(ENTER_PENALTIES + postGamePenalties.getValue());
        createPanelForContainer(new JComponent[]{postGamePenaltiesLabel, postGamePenalties}, postGameStats);
        postGamePenalties.addChangeListener(e ->
                postGamePenaltiesLabel.setText(ENTER_PENALTIES + postGamePenalties.getValue()));

        enterStats = new JButton("Continue");
        createPanelForContainer(new JComponent[]{enterStats}, postGameStats);

        enterStats.addActionListener(new ActionListener() {
            private JWindow goalieWindow = null;
            // Ensures that no other tabs can be accessed while these windows are open
            private final ChangeListener haltTabs = e1 -> {
                mainTabs.setSelectedIndex(mainTabs.getTabCount() - 1);
                enterStatsTabs.setSelectedIndex(enterStatsTabs.getTabCount() - 1);
            };

            /**
             * Makes any necessary changes to the GUI and updates the file after all necessary stats have been entered
             */
            public void finishedEntering() {
                updateEntireTeamComponents();
                mainTabs.removeChangeListener(haltTabs);
                enterStatsTabs.removeChangeListener(haltTabs);
                goalieWindow = null;
                liveStats.add(selectGoaliePanel, 1);
                defenseLinePanel.remove(defenseLinePanel.getComponentCount() - 1);
                defenseLinePanel.add(defenseLines);
                pkLinePanel.remove(pkLinePanel.getComponentCount() - 1);
                pkLinePanel.add(pkOptions);
                liveStats.add(defenseLinePanel, 1);
                try {
                    updateFile();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(mainFrame, FILE_ERROR, "Enter Stats",
                            JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "Enter Stats",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                if (goalieWindow == null) {
                    if (team.getSkaters().length < 5 || team.getGoalies().length == 0 || pickLeftDe.getItemCount() < 2
                            || centerOptions.getItemCount() < 2) {
                        JOptionPane.showMessageDialog(mainFrame, "You must have at least 5 players and 1 " +
                                "goalie in order to enter stats after a game. At least one of your players must be a " +
                                "defenseman and at least one of your players must be a center.", "Enter Stats",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    int goalsScored;
                    int goalsAgainst;
                    int shotsAgainst = postGameShotsAgainst.getValue();
                    int faceOffWins;
                    int faceOffLosses;
                    int shotsBlocked = postGameShotsBlocked.getValue();
                    int hits = postGameHits.getValue();
                    AtomicInteger powerPlays = new AtomicInteger(postGamePowerPlay.getValue());
                    int penalties = postGamePenalties.getValue();

                    try {
                        goalsScored = Integer.parseInt(finalScoreTeam.getText());
                        goalsAgainst = Integer.parseInt(finalScoreOpp.getText());
                        faceOffWins = Integer.parseInt(postGameFaceOffWins.getText());
                        faceOffLosses = Integer.parseInt(postGameFaceOffLosses.getText());
                        if (goalsScored < 0 || goalsAgainst < 0 || faceOffWins < 0 || faceOffLosses < 0) {
                            throw new NumberFormatException();
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(mainFrame, NUMBER_ERROR, "Enter Stats",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (goalsAgainst > shotsAgainst) {
                        JOptionPane.showMessageDialog(mainFrame, "Opponent's score cannot be greater than " +
                                "the number of shots against your goalie.", "Enter Stats",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Window for each stat that needs further clarification from user
                    goalieWindow = new JWindow(mainFrame);
                    goalieWindow.setLocationRelativeTo(mainFrame);
                    JWindow scoreTeamGoals = new JWindow(mainFrame);
                    scoreTeamGoals.setLocationRelativeTo(mainFrame);
                    JWindow enterTeamFaceOffs = new JWindow(mainFrame);
                    enterTeamFaceOffs.setLocationRelativeTo(mainFrame);
                    JWindow enterTeamShotBlocks = new JWindow(mainFrame);
                    enterTeamShotBlocks.setLocationRelativeTo(mainFrame);
                    JWindow enterTeamHits = new JWindow(mainFrame);
                    enterTeamHits.setLocationRelativeTo(mainFrame);
                    JWindow enterTeamPPs = new JWindow(mainFrame);
                    enterTeamPPs.setLocationRelativeTo(mainFrame);
                    JWindow enterTeamPKs = new JWindow(mainFrame);
                    enterTeamPKs.setLocationRelativeTo(mainFrame);

                    mainTabs.addChangeListener(haltTabs);
                    enterStatsTabs.addChangeListener(haltTabs);

                    int multipleGoalies;
                    do {
                        multipleGoalies = JOptionPane.showConfirmDialog(mainFrame, "Did multiple goalies " +
                                "play in this game?", "Enter Stats", JOptionPane.YES_NO_OPTION);
                    } while (multipleGoalies != JOptionPane.YES_OPTION && multipleGoalies != JOptionPane.NO_OPTION);
                    int finalInput = multipleGoalies;

                    Container enterGoalieContent = goalieWindow.getContentPane();
                    enterGoalieContent.setLayout(new BoxLayout(enterGoalieContent, BoxLayout.Y_AXIS));

                    enterGoalieContent.add(selectGoaliePanel);

                    JSlider goalsAgainstSlider = new JSlider(0, goalsAgainst);
                    JSlider shotsAgainstSlider = new JSlider(1, shotsAgainst);
                    ArrayList<Goalie> selectedGoalies = new ArrayList<>();
                    if (finalInput == JOptionPane.YES_OPTION) {
                        JLabel goalsAgainstLabel = new JLabel("Select Number of Goals Scored on this Goalie: " +
                                goalsAgainstSlider.getValue());
                        goalsAgainstSlider.addChangeListener(e1 -> {
                            goalsAgainstLabel.setText("Select Number of Goals Scored on this Goalie: " +
                                    goalsAgainstSlider.getValue());
                            goalieWindow.pack();
                        });
                        JLabel shotsAgainstLabel = new JLabel(POST_SHOTS_AGAINST +
                                shotsAgainstSlider.getValue());
                        shotsAgainstSlider.addChangeListener(e1 -> {
                            shotsAgainstLabel.setText(POST_SHOTS_AGAINST + shotsAgainstSlider.getValue());
                            goalieWindow.pack();
                        });
                        createPanelForContainer(new JComponent[]{goalsAgainstLabel, goalsAgainstSlider},
                                enterGoalieContent);
                        createPanelForContainer(new JComponent[]{shotsAgainstLabel, shotsAgainstSlider},
                                enterGoalieContent);
                    }

                    JButton enterGoalie = new JButton("Enter Goalie");
                    createPanelForContainer(new JComponent[]{enterGoalie}, enterGoalieContent);

                    enterGoalie.addActionListener(e1 -> {
                        Goalie selectedGoalie;
                        if (finalInput == JOptionPane.YES_OPTION) {
                            selectedGoalie = (Goalie) selectGoaliesForStats.getSelectedItem();

                            int shotsAgainstOneGoalie = shotsAgainstSlider.getValue();
                            int goalsAgainstOneGoalie = goalsAgainstSlider.getValue();
                            if (selectedGoalie != null) {
                                selectedGoalie.enterSaves(goalsAgainstOneGoalie, shotsAgainstOneGoalie);
                                selectedGoalies.add(selectedGoalie);
                            }
                            goalsAgainstSlider.setMaximum(goalsAgainstSlider.getMaximum() - goalsAgainstOneGoalie);
                            shotsAgainstSlider.setMaximum(shotsAgainstSlider.getMaximum() - shotsAgainstOneGoalie);

                            if (goalsAgainstSlider.getMaximum() == 0 && shotsAgainstSlider.getMaximum() == 0) {
                                if (selectedGoalies.isEmpty()) {
                                    JOptionPane.showMessageDialog(goalieWindow, "At least one goalie must be " +
                                            "in net for the game. Please retry and use a goalie for at least one goal" +
                                            " or shot.", "Enter Goalie Stats", JOptionPane.ERROR_MESSAGE);
                                    goalieWindow.dispose();
                                    scoreTeamGoals.dispose();
                                    enterTeamFaceOffs.dispose();
                                    enterTeamShotBlocks.dispose();
                                    enterTeamHits.dispose();
                                    enterTeamPPs.dispose();
                                    enterTeamPKs.dispose();
                                    finishedEntering();
                                    return;
                                }
                                while (true) {
                                    int index = JOptionPane.showOptionDialog(goalieWindow, "Which goalie " +
                                                    "should receive credit on their record? (Goalie who was in " +
                                                    "net for the game deciding goal)", "Enter Goalie Stats",
                                            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                                            selectedGoalies.toArray(), selectedGoalies.get(0));
                                    if (index >= 0 && index < selectedGoalies.size()) {
                                        selectedGoalie = selectedGoalies.get(index);
                                        break;
                                    }
                                }
                            } else {
                                return;
                            }
                        } else {
                            selectedGoalie = (Goalie) selectGoaliesForStats.getSelectedItem();
                            if (selectedGoalie == null) {
                                JOptionPane.showMessageDialog(mainFrame, SELECT, "Enter Goalie Stats",
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            selectedGoalie.enterSaves(goalsAgainst, shotsAgainst);
                        }

                        if (goalsScored > goalsAgainst) {
                            team.win();
                            if (goalsAgainst == 0) {
                                selectedGoalie.shutoutWin();
                            } else {
                                selectedGoalie.win();
                            }
                        } else if (goalsScored == goalsAgainst) {
                            team.tie();
                            selectedGoalie.loseOT();
                        } else if (goalsScored == goalsAgainst - 1) {
                            int input;
                            while (true) {
                                input = JOptionPane.showConfirmDialog(mainFrame, "Was this a regulation loss?",
                                        "Enter Stats", JOptionPane.YES_NO_OPTION);
                                if (input == JOptionPane.YES_OPTION) {
                                    team.lose();
                                    selectedGoalie.lose();
                                    break;
                                } else if (input == JOptionPane.NO_OPTION) {
                                    team.tie();
                                    selectedGoalie.loseOT();
                                    break;
                                }
                            }
                        } else {
                            team.lose();
                            selectedGoalie.lose();
                        }

                        goalieWindow.dispose();
                        if (goalsScored > 0) {
                            scoreTeamGoals.setVisible(true);
                        } else if (faceOffWins > 0 || faceOffLosses > 0) {
                            enterTeamFaceOffs.setVisible(true);
                        } else if (shotsBlocked > 0) {
                            enterTeamShotBlocks.setVisible(true);
                        } else if (hits > 0) {
                            enterTeamHits.setVisible(true);
                        } else if (powerPlays.get() > 0 && ppOptions.getItemCount() > 0) {
                            enterTeamPPs.setVisible(true);
                        } else if (penalties > 0 && pkOptions.getItemCount() > 0) {
                            enterTeamPKs.setVisible(true);
                        } else {
                            finishedEntering();
                        }
                    });

                    goalieWindow.pack();
                    goalieWindow.setVisible(true);

                    AtomicInteger ppGoals = new AtomicInteger();
                    if (goalsScored > 0) {
                        AtomicInteger enteredGoalsScored = new AtomicInteger();
                        AtomicInteger enteredGoalsAgainst = new AtomicInteger();

                        Container scoreGoalsContent = scoreTeamGoals.getContentPane();
                        scoreGoalsContent.setLayout(new BoxLayout(scoreGoalsContent, BoxLayout.Y_AXIS));

                        scoreGoalsContent.add(linesOrPlayersPanel);
                        useLinesOrPlayers.setSelected(false);
                        useLinesOrPlayers.setText(USE_PLAYERS);

                        JLabel otherLineLabel = new JLabel("Line on Ice: ");
                        nonDefenseLines.setSelectedIndex(0);
                        JTextArea otherLineRoster = new JTextArea();
                        JTextArea defenseLineRoster = new JTextArea();
                        if (defenseLines.getItemCount() > 0) {
                            defenseLines.setSelectedIndex(0);
                            defenseLineRoster.setText(defenseLines.getItemAt(0).lineRoster());
                        }
                        otherLineRoster.setEditable(false);
                        defenseLineRoster.setEditable(false);

                        JPanel otherLinePanel = createPanel(new JComponent[]{otherLineLabel, nonDefenseLines,
                                otherLineRoster});
                        scoreGoalsContent.add(otherLinePanel);
                        defenseLinePanel.add(defenseLineRoster);

                        nonDefenseLines.addItemListener(e1 -> {
                            if (e1.getStateChange() == ItemEvent.SELECTED) {
                                Line selection = (Line) nonDefenseLines.getSelectedItem();
                                if (selection instanceof OffenseLine) {
                                    scoreGoalsContent.add(defenseLinePanel, 2);
                                } else {
                                    scoreGoalsContent.remove(defenseLinePanel);
                                }
                                if (selection != null) {
                                    otherLineRoster.setText(selection.lineRoster());
                                } else {
                                    otherLineRoster.setText("");
                                }
                            }
                            scoreTeamGoals.pack();
                            scoreTeamGoals.repaint();
                        });

                        defenseLines.addItemListener(e1 -> {
                            DefenseLine line = (DefenseLine) defenseLines.getSelectedItem();
                            if (line != null) {
                                defenseLineRoster.setText(line.lineRoster());
                            } else {
                                defenseLineRoster.setText("");
                            }
                            scoreTeamGoals.pack();
                        });

                        scoreGoalsContent.add(scorerPanel);
                        scoreGoalsContent.add(assistPanel1);
                        scoreGoalsContent.add(assistPanel2);


                        // Updates screen based on the toggle button selection
                        useLinesOrPlayers.addActionListener(e1 -> {
                            if (useLinesOrPlayers.isSelected()) {
                                useLinesOrPlayers.setText(USE_LINES);

                                scoreGoalsContent.remove(otherLinePanel);
                                scoreGoalsContent.remove(defenseLinePanel);
                                scoreGoalsContent.remove(scorerPanel);
                                scoreGoalsContent.remove(assistPanel1);
                                scoreGoalsContent.remove(assistPanel2);

                                scoreGoalsContent.add(selectOtherPlayersPanel, 1);
                                scoreGoalsContent.add(assistPlayerPanel2, 1);
                                scoreGoalsContent.add(assistPlayerPanel1, 1);
                                scoreGoalsContent.add(otherPlayersPanel, 1);
                                scoreGoalsContent.add(playerScorePanel, 1);
                            } else {
                                useLinesOrPlayers.setText(USE_PLAYERS);

                                scoreGoalsContent.remove(playerScorePanel);
                                scoreGoalsContent.remove(otherPlayersPanel);
                                scoreGoalsContent.remove(assistPlayerPanel1);
                                scoreGoalsContent.remove(assistPlayerPanel2);
                                scoreGoalsContent.remove(selectOtherPlayersPanel);

                                scoreGoalsContent.add(assistPanel2, 1);
                                scoreGoalsContent.add(assistPanel1, 1);
                                scoreGoalsContent.add(scorerPanel, 1);
                                scoreGoalsContent.add(otherLinePanel, 1);
                                nonDefenseLines.setSelectedIndex(0);
                            }
                            scoreTeamGoals.pack();
                            scoreTeamGoals.repaint();
                        });

                        JButton enterTeamGoal = new JButton("Enter Team Goal");
                        JButton enterOpponentGoal = new JButton("Enter Opponent Goal");
                        JPanel scoreButtonsPanel = createPanel(new JComponent[]{enterTeamGoal, enterOpponentGoal});
                        scoreGoalsContent.add(scoreButtonsPanel);

                        ActionListener enterGoalsListener = e1 -> {
                            if (useLinesOrPlayers.isSelected()) {
                                Skater scorer = (Skater) scorerPlayerOptions.getSelectedItem();
                                Skater assist1 = (Skater) assistPlayerOptions1.getSelectedItem();
                                Skater assist2 = (Skater) assistPlayerOptions2.getSelectedItem();
                                Skater onIce1 = (Skater) otherPlayerOptions1.getSelectedItem();
                                Skater onIce2 = (Skater) otherPlayerOptions2.getSelectedItem();

                                if (scorer == null || assist1 == null || assist2 == null || onIce1 == null ||
                                        onIce2 == null) {
                                    JOptionPane.showMessageDialog(scoreTeamGoals, SELECT, "Enter Goals",
                                            JOptionPane.ERROR_MESSAGE);
                                    return;
                                }

                                if (scorer.equals(assist1) || scorer.equals(assist2) || scorer.equals(onIce1) ||
                                        scorer.equals(onIce2) || assist1.equals(assist2) || assist1.equals(onIce1)
                                        || assist1.equals(onIce2) || assist2.equals(onIce1) ||
                                        assist2.equals(onIce2) || onIce1.equals(onIce2)) {
                                    JOptionPane.showMessageDialog(scoreTeamGoals, "Selected Players must " +
                                            "be different", "Enter Goals", JOptionPane.ERROR_MESSAGE);
                                    return;
                                }

                                if (e1.getActionCommand().equals(enterTeamGoal.getActionCommand())) {
                                    scorer.score();
                                    if (assist1Check.isSelected()) {
                                        assist1.assist();
                                    } else {
                                        assist1.scoredOnIce();
                                    }
                                    if (assist2Check.isSelected()) {
                                        assist2.assist();
                                    } else {
                                        assist2.scoredOnIce();
                                    }
                                    onIce1.scoredOnIce();
                                    onIce2.scoredOnIce();
                                } else {
                                    scorer.scoredAgainst();
                                    assist1.scoredAgainst();
                                    assist2.scoredAgainst();
                                    onIce1.scoredAgainst();
                                    onIce2.scoredAgainst();
                                }

                            } else {
                                Position scorer = (Position) scorerOptions.getSelectedItem();
                                Position assist1 = (Position) assistOptions1.getSelectedItem();
                                Position assist2 = (Position) assistOptions2.getSelectedItem();

                                if (assist1 == null && assist2 != null) {
                                    JOptionPane.showMessageDialog(scoreTeamGoals, "Please select Assist 1 "
                                            + "instead of Assist 2", "Enter Goals", JOptionPane.ERROR_MESSAGE);
                                    return;
                                }

                                Line selection = (Line) nonDefenseLines.getSelectedItem();
                                try {
                                    if (selection instanceof OffenseLine oLine) {
                                        DefenseLine dLine = (DefenseLine) defenseLines.getSelectedItem();
                                        if (dLine == null) {
                                            JOptionPane.showMessageDialog(scoreTeamGoals, SELECT, "Enter Goals",
                                                    JOptionPane.ERROR_MESSAGE);
                                            return;
                                        }
                                        if (e1.getActionCommand().equals(enterTeamGoal.getActionCommand())) {
                                            if (assist1 != null && assist2 != null) {
                                                oLine.score(scorer, assist1, assist2, dLine);
                                            } else if (assist1 != null) {
                                                oLine.score(scorer, assist1, dLine);
                                            } else {
                                                oLine.score(scorer, dLine);
                                            }
                                        } else {
                                            oLine.lineScoredOn();
                                            dLine.lineScoredOn();
                                        }
                                    } else if (selection instanceof SpecialTeamsLine specialTeamsLine) {
                                        if (e1.getActionCommand().equals(enterTeamGoal.getActionCommand())) {
                                            if (specialTeamsLine instanceof PPLine) {
                                                ppGoals.getAndIncrement();
                                            }
                                            if (assist1 != null && assist2 != null) {
                                                specialTeamsLine.score(scorer, assist1, assist2);
                                            } else if (assist1 != null) {
                                                specialTeamsLine.score(scorer, assist1);
                                            } else {
                                                specialTeamsLine.score(scorer);
                                            }
                                        } else {
                                            specialTeamsLine.lineScoredOn();
                                        }
                                    } else {
                                        JOptionPane.showMessageDialog(scoreTeamGoals, SELECT, "Enter Goals",
                                                JOptionPane.ERROR_MESSAGE);
                                        return;
                                    }
                                } catch (NullPointerException | IllegalArgumentException ex) {
                                    JOptionPane.showMessageDialog(scoreTeamGoals, ex.getMessage(),
                                            "Enter Goals", JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                            }
                            if (e1.getActionCommand().equals(enterTeamGoal.getActionCommand())) {
                                enteredGoalsScored.getAndIncrement();
                                JOptionPane.showMessageDialog(scoreTeamGoals, "Team Goal " +
                                        enteredGoalsScored.get() + " successfully entered", "Enter Goal",
                                        JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                enteredGoalsAgainst.getAndIncrement();
                                JOptionPane.showMessageDialog(scoreTeamGoals, "Opponent Goal " +
                                        enteredGoalsAgainst.get() + " successfully entered", "Enter Goal",
                                        JOptionPane.INFORMATION_MESSAGE);
                            }
                            if (enteredGoalsScored.get() == goalsScored) {
                                scoreButtonsPanel.remove(enterTeamGoal);
                                scoreTeamGoals.repaint();
                                if (enteredGoalsAgainst.get() == goalsAgainst) {
                                    scoreTeamGoals.dispose();
                                    if (ppGoals.get() >= powerPlays.get()) {
                                        enterTeamPPs.dispose();
                                        powerPlays.set(0);
                                    }
                                    if (faceOffWins > 0 || faceOffLosses > 0) {
                                        enterTeamFaceOffs.setVisible(true);
                                    } else if (shotsBlocked > 0) {
                                        enterTeamShotBlocks.setVisible(true);
                                    } else if (hits > 0) {
                                        enterTeamHits.setVisible(true);
                                    } else if (powerPlays.get() > 0 && ppOptions.getItemCount() > 0) {
                                        enterTeamPPs.setVisible(true);
                                    } else if (penalties > 0 && pkOptions.getItemCount() > 0) {
                                        enterTeamPKs.setVisible(true);
                                    } else {
                                        finishedEntering();
                                    }
                                }
                                return;
                            }
                            if (enteredGoalsAgainst.get() == goalsAgainst) {
                                scoreButtonsPanel.remove(enterOpponentGoal);
                                scoreTeamGoals.repaint();
                                if (enteredGoalsScored.get() == goalsScored) {
                                    scoreTeamGoals.dispose();
                                    if (ppGoals.get() >= powerPlays.get()) {
                                        enterTeamPPs.dispose();
                                        powerPlays.set(0);
                                    }
                                    if (faceOffWins > 0 || faceOffLosses > 0) {
                                        enterTeamFaceOffs.setVisible(true);
                                    } else if (shotsBlocked > 0) {
                                        enterTeamShotBlocks.setVisible(true);
                                    } else if (hits > 0) {
                                        enterTeamHits.setVisible(true);
                                    } else if (powerPlays.get() > 0 && ppOptions.getItemCount() > 0) {
                                        enterTeamPPs.setVisible(true);
                                    } else if (penalties > 0 && pkOptions.getItemCount() > 0) {
                                        enterTeamPKs.setVisible(true);
                                    } else {
                                        finishedEntering();
                                    }
                                }
                            }
                        };

                        enterTeamGoal.addActionListener(enterGoalsListener);
                        enterOpponentGoal.addActionListener(enterGoalsListener);

                        scoreTeamGoals.pack();
                    } else {
                        scoreTeamGoals.dispose();
                    }

                    // Enter Face Offs
                    if (faceOffWins > 0 || faceOffLosses > 0) {
                        Container faceOffContent = enterTeamFaceOffs.getContentPane();
                        faceOffContent.setLayout(new BoxLayout(faceOffContent, BoxLayout.Y_AXIS));
                        faceOffContent.add(selectCenterPanel);
                        JSlider winsSlider = new JSlider(0, faceOffWins);
                        JSlider lossesSlider = new JSlider(0, faceOffLosses);
                        JLabel winsLabel = new JLabel("Face Off Wins: " + winsSlider.getValue());
                        JLabel lossesLabel = new JLabel("Face Off Losses: " + lossesSlider.getValue());
                        winsSlider.addChangeListener(e1 -> {
                            winsLabel.setText("Face Off Wins: " + winsSlider.getValue());
                            enterTeamFaceOffs.pack();
                        });
                        lossesSlider.addChangeListener(e1 -> {
                            lossesLabel.setText("Face Off Losses: " + lossesSlider.getValue());
                            enterTeamFaceOffs.pack();
                        });
                        createPanelForContainer(new JComponent[]{winsLabel, winsSlider, lossesLabel, lossesSlider},
                                faceOffContent);

                        JButton enterFaceOffsButton = new JButton("Enter Face Offs");
                        createPanelForContainer(new JComponent[]{enterFaceOffsButton}, faceOffContent);
                        enterFaceOffsButton.addActionListener(e1 -> {
                            Center selectedCenter = (Center) centerOptions.getSelectedItem();
                            if (selectedCenter == null) {
                                JOptionPane.showMessageDialog(enterTeamFaceOffs, SELECT, "Enter Face Offs",
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            int wins = winsSlider.getValue();
                            int losses = lossesSlider.getValue();
                            selectedCenter.enterFaceOffsPostGame(wins, losses);

                            winsSlider.setMaximum(winsSlider.getMaximum() - wins);
                            lossesSlider.setMaximum(lossesSlider.getMaximum() - losses);

                            JOptionPane.showMessageDialog(enterTeamFaceOffs, "Face Offs Successfully Updated",
                                    "Enter Face Offs", JOptionPane.INFORMATION_MESSAGE);

                            if (winsSlider.getMaximum() == 0 && lossesSlider.getMaximum() == 0) {
                                enterTeamFaceOffs.dispose();
                                if (shotsBlocked > 0) {
                                    enterTeamShotBlocks.setVisible(true);
                                } else if (hits > 0) {
                                    enterTeamHits.setVisible(true);
                                } else if (powerPlays.get() > 0 && ppOptions.getItemCount() > 0) {
                                    enterTeamPPs.setVisible(true);
                                } else if (penalties > 0 && pkOptions.getItemCount() > 0) {
                                    enterTeamPKs.setVisible(true);
                                } else {
                                    finishedEntering();
                                }
                            }
                        });
                        enterTeamFaceOffs.pack();
                    } else {
                        enterTeamFaceOffs.dispose();
                    }

                    // Enter Shot Blocks
                    if (shotsBlocked > 0) {
                        Container enterShotsBlockedContent = enterTeamShotBlocks.getContentPane();
                        enterShotsBlockedContent.setLayout(new BoxLayout(enterShotsBlockedContent, BoxLayout.Y_AXIS));
                        enterShotsBlockedContent.add(selectLDPanel);
                        selectLDLabel.setText("Select Defenseman:");

                        JSlider shotsBlockedSlider = new JSlider(1, shotsBlocked);
                        JLabel shotsBlockedLabel = new JLabel(SHOT_BLOCK_STRING + " " +
                                shotsBlockedSlider.getValue());
                        shotsBlockedSlider.addChangeListener(e1 -> {
                            shotsBlockedLabel.setText(SHOT_BLOCK_STRING + " " + shotsBlockedSlider.getValue());
                            enterTeamShotBlocks.pack();
                        });
                        createPanelForContainer(new JComponent[]{shotsBlockedLabel, shotsBlockedSlider},
                                enterShotsBlockedContent);

                        JButton shotsBlockedButton = new JButton("Enter Shots Blocked");
                        createPanelForContainer(new JComponent[]{shotsBlockedButton}, enterShotsBlockedContent);

                        shotsBlockedButton.addActionListener(e1 -> {
                            Defenseman selectedDe = (Defenseman) pickLeftDe.getSelectedItem();
                            if (selectedDe == null) {
                                JOptionPane.showMessageDialog(enterTeamShotBlocks, SELECT, "Enter Shots Blocked",
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            int addShots = shotsBlockedSlider.getValue();
                            selectedDe.addShotsBlocked(addShots);
                            shotsBlockedSlider.setMaximum(shotsBlockedSlider.getMaximum() - addShots);

                            JOptionPane.showMessageDialog(enterTeamShotBlocks, "Shots Blocked Successfully " +
                                            "Updated", "Enter Shots Blocked", JOptionPane.INFORMATION_MESSAGE);
                            if (shotsBlockedSlider.getMaximum() == 0) {
                                enterTeamShotBlocks.dispose();
                                selectLDLabel.setText(LEFT_DE);
                                if (hits > 0) {
                                    enterTeamHits.setVisible(true);
                                } else if (powerPlays.get() > 0 && ppOptions.getItemCount() > 0) {
                                    enterTeamPPs.setVisible(true);
                                } else if (penalties > 0 && pkOptions.getItemCount() > 0) {
                                    enterTeamPKs.setVisible(true);
                                } else {
                                    finishedEntering();
                                }
                            } else {
                                enterTeamShotBlocks.pack();
                            }
                        });

                        enterTeamShotBlocks.pack();
                    } else {
                        enterTeamShotBlocks.dispose();
                    }

                    // Enter Hits
                    if (hits > 0) {
                        Container enterHitsContent = enterTeamHits.getContentPane();
                        enterHitsContent.setLayout(new BoxLayout(enterHitsContent, BoxLayout.Y_AXIS));
                        enterHitsContent.add(selectLWPanel);
                        selectLWLabel.setText("Select Skater:");

                        JSlider hitsSlider = new JSlider(1, hits);
                        JLabel hitsLabel = new JLabel(HITS_STRING + " " + hitsSlider.getValue());
                        hitsSlider.addChangeListener(e1 -> {
                            hitsLabel.setText(HITS_STRING + " " + hitsSlider.getValue());
                            enterTeamHits.pack();
                        });
                        createPanelForContainer(new JComponent[]{hitsLabel, hitsSlider}, enterHitsContent);

                        JButton hitsButton = new JButton("Enter Hits");
                        createPanelForContainer(new JComponent[]{hitsButton}, enterHitsContent);
                        hitsButton.addActionListener(e1 -> {
                            Skater selectedSkater = (Skater) pickLeftWing.getSelectedItem();
                            if (selectedSkater == null) {
                                JOptionPane.showMessageDialog(enterTeamHits, SELECT, "Enter Hits",
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            int addHits = hitsSlider.getValue();
                            selectedSkater.addHits(addHits);
                            hitsSlider.setMaximum(hitsSlider.getMaximum() - addHits);
                            JOptionPane.showMessageDialog(enterTeamHits, "Hits Successfully Updated",
                                    "Enter Hits", JOptionPane.INFORMATION_MESSAGE);

                            if (hitsSlider.getMaximum() == 0) {
                                enterTeamHits.dispose();
                                selectLWLabel.setText(LEFT_WING);
                                if (powerPlays.get() > 0 && ppOptions.getItemCount() > 0) {
                                    enterTeamPPs.setVisible(true);
                                } else if (penalties > 0 && pkOptions.getItemCount() > 0) {
                                    enterTeamPKs.setVisible(true);
                                } else {
                                    finishedEntering();
                                }
                            }
                        });

                        enterTeamHits.pack();
                    } else {
                        enterTeamHits.dispose();
                    }

                    // Enter Power Plays
                    if (powerPlays.get() > 0 && ppOptions.getItemCount() > 0) {
                        Container enterPPContent = enterTeamPPs.getContentPane();
                        enterPPContent.setLayout(new BoxLayout(enterPPContent, BoxLayout.Y_AXIS));


                        enterPPContent.add(ppLinePanel);

                        JSlider ppSlider = new JSlider(1, powerPlays.get() - ppGoals.get());
                        JLabel ppSliderLabel = new JLabel("Select Number of Expired Power Play Opportunities: " +
                                ppSlider.getValue());
                        createPanelForContainer(new JComponent[]{ppSliderLabel, ppSlider}, enterPPContent);
                        ppSlider.addChangeListener(e1 -> {
                            ppSliderLabel.setText("Select Number of Expired Power Play Opportunities: " +
                                    ppSlider.getValue());
                            enterTeamPPs.pack();
                        });

                        JButton enterPPs = new JButton("Enter Power Plays");
                        createPanelForContainer(new JComponent[]{enterPPs}, enterPPContent);

                        enterPPs.addActionListener(e1 -> {
                            PPLine selectedPPLine = (PPLine) ppOptions.getSelectedItem();

                            int failures = ppSlider.getValue();
                            if (selectedPPLine != null) {
                                selectedPPLine.addFailures(failures);
                            } else {
                                enterTeamPPs.dispose();
                                if (penalties > 0 && pkOptions.getItemCount() > 0) {
                                    enterTeamPKs.setVisible(true);
                                } else {
                                    finishedEntering();
                                }
                                return;
                            }
                            ppSlider.setMaximum(ppSlider.getMaximum() - failures);
                            JOptionPane.showMessageDialog(enterTeamPPs, "Power Plays Successfully Updated",
                                    "Enter Power Plays", JOptionPane.INFORMATION_MESSAGE);
                            if (ppSlider.getMaximum() == 0) {
                                enterTeamPPs.dispose();
                                if (penalties > 0 && pkOptions.getItemCount() > 0) {
                                    enterTeamPKs.setVisible(true);
                                } else {
                                    finishedEntering();
                                }
                            }
                        });

                        enterTeamPPs.pack();
                    } else {
                        enterTeamPPs.dispose();
                    }

                    // Enter Penalty Kills
                    if (penalties > 0 && pkOptions.getItemCount() > 0) {
                        enterTeamPKs.setFocusable(true);
                        AtomicInteger enteredPenalties = new AtomicInteger();
                        Container enterPKContent = enterTeamPKs.getContentPane();
                        enterPKContent.setLayout(new BoxLayout(enterPKContent, BoxLayout.Y_AXIS));

                        selectRWLabel.setText("Player Serving the Penalty:");
                        enterPKContent.add(selectRWPanel);

                        JLabel timeLabel = new JLabel("Enter Length of Penalty in Minutes:");
                        JTextField enterTimeLength = new JTextField(ENTER_STAT_SIZE);
                        enterTimeLength.setEditable(true);
                        createPanelForContainer(new JComponent[]{timeLabel, enterTimeLength}, enterPKContent);


                        JCheckBox checkSuccess = new JCheckBox("Penalty Killed Successfully");
                        pkLinePanel.add(checkSuccess);
                        enterPKContent.add(pkLinePanel);

                        JButton enterPenalty = new JButton("Enter Penalty");
                        createPanelForContainer(new JComponent[]{enterPenalty}, enterPKContent);

                        enterPenalty.addActionListener(e1 -> {
                            double penaltyLength;
                            Skater guiltyPlayer = (Skater) pickRightWing.getSelectedItem();
                            PKLine pkLine = (PKLine) pkOptions.getSelectedItem();
                            if (pkLine == null) {
                                enterTeamPKs.dispose();
                                finishedEntering();
                                return;
                            }
                            if (guiltyPlayer == null) {
                                JOptionPane.showMessageDialog(enterTeamPKs, SELECT, "Enter Penalty",
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            try {
                                penaltyLength = Double.parseDouble(enterTimeLength.getText());
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(enterTeamPKs, NUMBER_ERROR, "Enter Penalty",
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            if (penaltyLength <= 0) {
                                JOptionPane.showMessageDialog(enterTeamPKs, "Penalty Length must be positive",
                                        "Enter Penalty", JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            guiltyPlayer.penalty(penaltyLength);
                            if (checkSuccess.isSelected()) {
                                pkLine.success();
                            } else {
                                pkLine.failure();
                            }
                            enteredPenalties.getAndIncrement();
                            JOptionPane.showMessageDialog(enterTeamPKs, "Penalty " + enteredPenalties.get() +
                                    " " + "Entered Successfullly", "Enter Penalty",
                                    JOptionPane.INFORMATION_MESSAGE);
                            if (enteredPenalties.get() == penalties) {
                                enterTeamPKs.dispose();
                                finishedEntering();
                            }
                        });
                        enterTeamPKs.pack();
                    } else {
                        enterTeamPKs.dispose();
                    }
                }
            }
        });

        enterStatsTabs.add("Enter After Game", postGameStats);

        mainTabs.add("Enter Stats from Game", enterStatsTabs);

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
            JOptionPane.showMessageDialog(null, FILE_ERROR, "File Error", JOptionPane.ERROR_MESSAGE);
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
        teamSelection = new JComboBox<>();
        for (Team team: teams) {
            teamSelection.addItem(team);
        }
        // Button to confirm selection
        selectTeamButton = new JButton("Open this team");
        JPanel selectTeamPanel = createPanel(new JComponent[]{teamSelection, selectTeamButton});
        selectContent.add(selectTeamPanel);

        // Middle of frame that contains other options with teams
        otherOptionsLabel = new JLabel("Additional Options: ");
        createTeam = new JButton("Create New Team");
        deleteTeam = new JButton("Delete Team");
        restoreSample = new JButton("Restore Sample Team");

        createPanelForContainer(new JComponent[]{otherOptionsLabel, createTeam, deleteTeam, restoreSample}, selectContent);

        // Bottom of Frame, button where new users can find additional guidance
        newUsers = new JButton("Help and Guidance for New Users");
        createPanelForContainer(new JComponent[]{newUsers}, selectContent);

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
                            JOptionPane.showMessageDialog(selectFrame, NUMBER_ERROR, "Create Team",
                                    JOptionPane.ERROR_MESSAGE);
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(selectFrame, FILE_ERROR, "Create Team",
                                    JOptionPane.ERROR_MESSAGE);
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
                    selectTeamPanel.add(teamSelection, 0);
                    selectFrame.repaint();
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
 * It is a very simple implementation of the DefaultTableModel class. The main difference is that this table is view
 * only.
 */
class StatsTableModel extends DefaultTableModel {

    public StatsTableModel(Object[][] stats, String[] columnNames) {
        super(stats, columnNames);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
