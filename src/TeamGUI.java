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
    private Team team;  // Team that the user chooses to open

    // Select GUI Strings/Components
    private static final String INIT = "Initialize";
    // Designated name for file storing data
    private static final String FILE_NAME = System.getProperty("user.dir") + File.separator +
            "HockeyTeamManagerData";
    private static final String NEW_INFO = "Thank you for using Team Manager! Here are a few things to note as you " +
            "get started:\nIf this is your first time launching the application you will notice that the app comes " +
            "preloaded with a sample team. This is included as a way to ease yourself into the application.\n" +
            "Feel free to view or change it in any way you like, and use it to take some time to get used to how the" +
            " application works.\n" +
            "All data related to the teams that you create in this application is stored in a file called \"" +
            FILE_NAME + "\" which was automatically created when you first launched this application.\n" +
            "This file is not readable nor transferable between most devices. The file is automatically updated and " +
            "saved every time a change is made.\n" +
            "If the file is deleted or lost, when the application launches, a new file will be created, and it " +
            "will only contain the sample team.\nIf you would like to transfer data between devices, save older " +
            "versions of your teams, or have a more reliable back up file;\nyou can save any of your teams by " +
            "creating a formatted text file using the button in the welcome menu.\nThis file will have the " +
            "information for the team at the time it was created, and it will not be updated with any new changes.\n" +
            "You can then import your teams from this text file using the adjacent button in the welcome menu.";
    private static final String NEW_INFO_2 = """
            This application also allows you to organize your skaters into lines.
            When creating skaters you must assign them to one of the five positions.
            It should be noted that only skaters who are assigned the center position can be assigned as the center of
            a line, and only skaters who are assigned a defensive position (Left Defense or Right Defense)
            can be assigned as a defenseman on a line. Any skater can be assigned as a winger. You can also change the
            position of your skaters later if needed. Additionally, only centers have the ability to save a face off
            record and only defenseman have the ability to save a shot block stat.""";
    private static final String CREATE_FILE_INSTRUCTIONS = "<html><center>Click on the Team(s) You Wish to Save Above" +
            " and Enter New File Name Below<br>Hold down the Ctrl key to select multiple teams<center></html>";
    private static final String NEW_FILE_ERR = "There was an issue creating the given file name. It may be invalid " +
            "or already exist on your computer. Please delete the old file or try a different name.";
    private static final String DUPLICATES_INSTRUCTIONS = "<html><center>Select the Teams from your program above " +
            "that you would like to replace with the information from the text file.<br>Hold down Ctrl to select" +
            " multiple teams.</center></html>";
    private static final String FILE_DUPLICATE_PLAYER = "Two or more players share the same number";
    private static final String FILE_DUPLICATE_LINE = "Two or more lines share the same name";
    private static final String WRONG_POSITION = "Players are not assigned to the correct position";
    private static final String POSITION_STRING = "Position String is not valid";
    private static final String ELEMENT_NUM = "Unexpected number of elements";
    private static final String CREATE_TEAM = "Create Team";
    private static final String DELETE_TEAM = "Delete Team";
    private static final String RESTORE_SAMPLE = "Restore Sample Team";
    private static final String CREATE_FILE = "Save Teams To Text File";
    private static final String IMPORT_FILE = "Import Teams From Text File";
    private static final String NEW_USER_INFO = "New User Information";


    // JComponents
    JFrame selectFrame;

    JComboBox<Team> teamSelection;
    JButton selectTeamButton;

    JLabel otherOptionsLabel;
    JButton createTeam;
    JButton deleteTeam;
    JButton restoreSample;

    JButton createTextFile;
    JButton importTextFile;

    JButton newUsers;
    JButton closeApp;

    // MainGUI Constants/Components

    // Reused String expressions
    // GUI Sections
    private static final String EDIT_TEAM = "Edit Team";
    private static final String RESET_TEAM_STATS = "Reset Team Stats";
    private static final String CREATE_LINE = "Create Line";
    private static final String EDIT_LINE = "Edit Line";
    private static final String DELETE_LINE = "Delete Line";
    private static final String CREATE_SKATER = "Create Skater";
    private static final String EDIT_SKATER = "Edit Skater";
    private static final String DELETE_SKATER = "Delete Skater";
    private static final String CREATE_GOALIE = "Create Goalie";
    private static final String EDIT_GOALIE = "Edit Goalie";
    private static final String DELETE_GOALIE = "Delete Goalie";
    private static final String ENTER_STATS_LIVE = "Enter Stats Live";
    private static final String ENTER_STATS_AFTER = "Enter Stats After Game";

    // JOptionPane Confirmations
    private static final String UPDATE = "Update Changes";
    private static final String RESET_CONFIRM = "Are you sure you want to reset ";
    private static final String TO_ZERO = "'s stats to 0?";
    private static final String CONFIRM_DELETE = "Are you sure you would like to delete ";
    private static final String CREATE_SUCCESS = "Successfully Created ";
    private static final String UPDATE_SUCCESS = "Successfully Updated Changes";
    private static final String REG_LOSS = "Was this a regulation loss?";
    private static final String EDIT_SKATER_CONFIRM = "Warning: Changing your skater's position and/or number will " +
            "delete any lines that the skater is assigned to.\nAre you sure you would like to proceed?";

    // JLabel/JButton Display Strings
    private static final String CENTER = "Center:";
    private static final String LEFT_WING = "Left Wing:";
    private static final String RIGHT_WING = "Right Wing:";
    private static final String LEFT_DE = "Left Defense:";
    private static final String RIGHT_DE = "Right Defense:";
    private static final String OFFENSE = "Offense ";
    private static final String OFFENSE_LINE = "Offense Line";
    private static final String DEFENSE_LINE = "Defense Pair";
    private static final String PP_LINE = "Power Play Line";
    private static final String PK_LINE = "Penalty Kill Line";
    private static final String SELECT_LINE = "Selected Line:";
    private static final String SELECT_GOALIE = "Selected Goalie:";
    private static final String SELECT_CURRENT = "Select Current ";
    private static final String USE_PLAYERS = "Select Players Manually";
    private static final String USE_LINES = "Select Player from Lines";
    private static final String CURRENT_SCORE = "Current Score: (Your Team - Opponent) ";
    private static final String UPDATE_NAME = "Update Name";
    private static final String NAME_STRING = "Enter Name:";
    private static final String ENTER_NUM_OPPS = "Enter Number of PP/PK Opportunities:";
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
    private static final String TEAM_GOAL = "Team Scored";
    private static final String OPPONENT_GOAL = "Opponent Scored";
    private static final String SELECT_SCORER = "Select Scorer";
    private static final String SELECT_DE = "Select Defenseman";
    private static final String SELECT_OTHER_DE = "Select Other Defenseman";
    private static final String THAT_SHOT_BLOCK = "That Blocked The Shot:";
    private static final String SELECT_SKATER_WHO_HIT = "Select Skater who Made the Hit";
    private static final String SELECT_OTHER_SKATER = "Select Other Skater";
    private static final String ASSIST_CHECK = "This Player Assisted";
    private static final String CANCEL = "Cancel";
    private static final String FACE_OFF = "Face Off";
    private static final String FACE_OFF_WIN = "Face Off Wins: ";
    private static final String FACE_OFF_LOSS = "Face Off Losses: " ;
    private static final String SHOT_BLOCK = "Shot Block";
    private static final String HIT = "Hit";
    private static final String ENTER_PENALTIES = "Select Number of Penalties called on your Players: ";
    private static final String ENTER_POWER_PLAY = "Select Number of Power Play Opportunities: ";
    private static final String POST_SHOTS_BLOCKED = "Select Number of Shots Blocked by your Defenseman: ";
    private static final String POST_SHOTS_AGAINST = "Select Number of Shots Against your Goalie: ";
    private static final String POST_HITS = "Select Number of Hits made by your Team: ";
    private static final String GOAL_AGAINST_GOALIE = "Select Number of Goals Scored on this Goalie: ";
    private static final String SELECT_EXPIRED_PP = "Select Number of Expired Power Play Opportunities: ";
    private static final String SELECT_SERVING_PLAYER = "Select Player Serving Penalty:";
    private static final String PENALTY_LENGTH = "Enter Duration of Penalty in Minutes:";

    // HTML Strings
    private static final String EDIT_INSTRUCTIONS = "<html><center>Enter changes for the information that you would " +
            "like to change.<br>If a field is left blank, no changes will be made for the corresponding information." +
            "</html>";
    private static final String EDIT_LINE_INSTRUCTIONS = "<html><center>This tab will make changes to or delete any " +
            "already created lines.<br>Use the drop box at the top of the screen to select which line you would like " +
            "to change or delete.<hr>" + EDIT_INSTRUCTIONS;
    private static final String EDIT_PLAYER_INSTRUCTIONS = "<html><center>This tab will make changes to or delete " +
            "any already created players.<br>Use the drop box at the top of the screen to select which player you " +
            "would like to change or delete.<hr>" + EDIT_INSTRUCTIONS;
    private static final String RESET_STATS_WARNING = "<html><center><hr>The button below will reset all stats for " +
            "your team, its players, and its special teams to 0.<br>This action cannot be undone after the fact and " +
            "their previous stats will be lost unless you have a backup file.</html>";
    private static final String ENTER_LIVE_INSTRUCTIONS = "<html><center>This tab is best for watching the game live " +
            "and entering stats as the game progresses.<br>You must have created at least one of each type of line to" +
            " be able to completely use this tab.<hr>Any stats you update may display in some elements of the " +
            "application; however,<br> the application will not be completely updated and your changes will not be " +
            "saved to your file until you click \"Game Over\".<hr>If you make a mistake and need to restart, you can " +
            "close and restart the application without clicking \"Game Over\".<br> This will undo any stats that you " +
            "have already entered.<hr></center></html>";
    private static final String CHANGE_LINE_NOTE = "<html><center>Note: You cannot swap two players at once on the " +
            "same line (e.g. switch the left wing with the right wing).<br>Instead, you must use a placeholder player" +
            " and make the swap one at a time.</center></html>";

    // Error Strings
    private static final String NUMBER_ERROR = "Please enter a valid number where prompted.";
    private static final String FILE_NUM_ERROR = "A number was not located where is was expected.";
    private static final String BIN_FILE_ERROR = "There was an issue interacting with the file. Please ensure that " +
            "your file has not been moved or altered, close the application, and try again.";
    private static final String TEXT_FILE_ERROR = "There was an issue interacting with the file. Please try again.";
    private static final String EMPTY_INPUTS = "Please enter a value in at least one of the boxes";
    private static final String BLANK_UPDATED = " (Any blank fields have already been updated in the application but " +
            "not saved to the file)";
    private static final String PLAYER_NUMBER_DUPLICATE = "New player cannot share the same number as another player";
    private static final String PLAYER_DUPLICATE = "Selected players must be different";
    private static final String USE_ASSIST_1 = "Please select Assist 1 instead of Assist 2";
    private static final String UNIQUE_NAME = " cannot share the same name";
    private static final String SELECT_GOALIE_RECORD = "Please select the goalie that should have this game recorded" +
            " on their record. This is the goalie who was in net for the game deciding goal.";
    private static final String SELECT = "Please select a player or line";
    private static final String NO_OPTIONS = "There are currently no options to choose from. Please create a ";
    private static final String UNEXPECTED_ERROR = "An unexpected error occurred - ";
    private static final String ENTER_AFTER_REQS = "You must have at least 5 players and 1 goalie in order to enter " +
            "stats after a game.\nAt least one of your players must be a defenseman, and at least one of your players" +
            " must be a center.";
    private static final String SPECIAL_TEAM_REQ = "You do not have the necessary special teams lines to record your" +
            " penalties and power plays. Please create at least one Power Play Line and one Penalty Kill Line.";
    private static final String ACTIVE_SP_TEAMS = "You currently have a power play or penalty kill active in the " +
            "enter live tab. Please finish entering that before doing anything in this tab.";
    private static final String POS_PENALTY_LENGTH = "Penalty Duration Must Be Positive";

    // JTable Columns
    private static final String[] SKATER_STATS_COLUMNS = {"Skater Name", "Player #", "Position", "Goals", "Assists",
            "Points", "+/-"};
    private static final String[] GOALIE_STATS_COLUMNS = {"Goalie Name", "Player #", "Wins", "Losses", "OT L / Ties",
            "GAA", "SV%"};

    // Numeric Constants
    private static final int ENTER_NAME_SIZE = 30;
    private static final int ENTER_STAT_SIZE = 5;
    private static final int NAME_COLUMN_WIDTH = 100;
    private static final int POST_GAME_MAX = 50;
    private static final int PENALTY_MAX = 15;

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
        if (!f.exists()) {  // No file found, new data created
            if (!f.createNewFile()) throw new Exception(UNEXPECTED_ERROR + "openFile 1");
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
                throw new Exception(UNEXPECTED_ERROR + "openFile 2");
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
     * This method reads in information about a team from a text file and adds the read in teams to the static list,
     * and GUI components.
     * @param f The file being read by the method
     * @return An ArrayList of any teams whose name already exists in the program.
     * @throws IOException If there is an issue reading from the file or an issue with the format of the file.
     */
    public ArrayList<Team> importFile(File f) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(f));
        int lineNum = 1;  // Keep track of current line number, used in error reporting
        ArrayList<Team> duplicates = new ArrayList<>();
        String regEx = "\\|";  // String used to split by '|' char
        try {
            int numTeams = Integer.parseInt(br.readLine());  // Number of teams in file
            lineNum++;
            int index;
            for (int i = 0; i < numTeams; i++) {
                Team team;
                String[] teamInfo = br.readLine().split(regEx);
                team = new Team(teamInfo[0], Integer.parseInt(teamInfo[1]), Integer.parseInt(teamInfo[2]),
                        Integer.parseInt(teamInfo[3]));
                lineNum++;
                int loopTotal = Integer.parseInt(br.readLine());  // Number of skaters
                for (int k = 0; k < loopTotal; k++) {  // Read in skaters
                    String[] skaterInfo = br.readLine().split(regEx);
                    Skater skater;
                    switch (skaterInfo[3]) {
                        case "C" -> skater = new Center(skaterInfo[0], Integer.parseInt(skaterInfo[1]), skaterInfo[2],
                                    Integer.parseInt(skaterInfo[4]), Integer.parseInt(skaterInfo[5]),
                                    Integer.parseInt(skaterInfo[6]), Integer.parseInt(skaterInfo[7]),
                                    Double.parseDouble(skaterInfo[8]), Double.parseDouble(skaterInfo[9]),
                                    Integer.parseInt(skaterInfo[10]));
                        case "LW" -> skater = new Skater(skaterInfo[0], Integer.parseInt(skaterInfo[1]), skaterInfo[2],
                                Position.Left_Wing, Integer.parseInt(skaterInfo[4]), Integer.parseInt(skaterInfo[5]),
                                Integer.parseInt(skaterInfo[6]), Integer.parseInt(skaterInfo[7]),
                                Double.parseDouble(skaterInfo[8]));
                        case "RW" -> skater = new Skater(skaterInfo[0], Integer.parseInt(skaterInfo[1]), skaterInfo[2],
                                Position.Right_Wing, Integer.parseInt(skaterInfo[4]), Integer.parseInt(skaterInfo[5]),
                                Integer.parseInt(skaterInfo[6]), Integer.parseInt(skaterInfo[7]),
                                Double.parseDouble(skaterInfo[8]));
                        case "LD" -> skater = new Defenseman(skaterInfo[0], Integer.parseInt(skaterInfo[1]),
                                skaterInfo[2], Position.Left_Defense, Integer.parseInt(skaterInfo[4]),
                                Integer.parseInt(skaterInfo[5]), Integer.parseInt(skaterInfo[6]),
                                Integer.parseInt(skaterInfo[7]), Double.parseDouble(skaterInfo[8]),
                                Integer.parseInt(skaterInfo[9]));
                        case "RD" -> skater = new Defenseman(skaterInfo[0], Integer.parseInt(skaterInfo[1]),
                                skaterInfo[2], Position.Right_Defense, Integer.parseInt(skaterInfo[4]),
                                Integer.parseInt(skaterInfo[5]), Integer.parseInt(skaterInfo[6]),
                                Integer.parseInt(skaterInfo[7]), Double.parseDouble(skaterInfo[8]),
                                Integer.parseInt(skaterInfo[9]));
                        default -> throw new FormatException(POSITION_STRING, lineNum);
                    }
                    index = team.addPlayer(skater);
                    if (index < 0) {
                        throw new FormatException(FILE_DUPLICATE_PLAYER, lineNum);
                    }
                    lineNum++;
                }
                loopTotal = Integer.parseInt(br.readLine());  // Number of goalies
                for (int k = 0; k < loopTotal; k++) {  // Read in goalies
                    String[] goalieInfo = br.readLine().split(regEx);
                    index = team.addPlayer(new Goalie(goalieInfo[0], Integer.parseInt(goalieInfo[1]),
                            Double.parseDouble(goalieInfo[6]), Integer.parseInt(goalieInfo[7]),
                            Integer.parseInt(goalieInfo[2]), Integer.parseInt(goalieInfo[3]),
                            Integer.parseInt(goalieInfo[4]), Integer.parseInt(goalieInfo[5])));
                    if (index < 0) {
                        throw new FormatException(FILE_DUPLICATE_PLAYER, lineNum);
                    }
                    lineNum++;
                }
                loopTotal = Integer.parseInt(br.readLine());  // Number of lines
                for (int k = 0; k < loopTotal; k++) {  // Read in lines
                    String[] lineInfo = br.readLine().split(regEx);
                    Line line;
                    switch (lineInfo.length) {
                        // Offense Line
                        case 4 -> {
                            Skater center = team.getSkater(Integer.parseInt(lineInfo[1]));
                            if (center instanceof Center c) {
                                line = new OffenseLine(lineInfo[0],
                                        c, team.getSkater(Integer.parseInt(lineInfo[2])),
                                        team.getSkater(Integer.parseInt(lineInfo[3])));
                            } else {
                                throw new FormatException(WRONG_POSITION, lineNum);
                            }
                        }
                        // Defense Line
                        case 3 -> {
                            Skater left = team.getSkater(Integer.parseInt(lineInfo[1]));
                            Skater right = team.getSkater(Integer.parseInt(lineInfo[2]));
                            if (left instanceof Defenseman leftDe && right instanceof Defenseman rightDe) {
                                line = new DefenseLine(lineInfo[0], leftDe, rightDe);
                            } else {
                                throw new FormatException(WRONG_POSITION, lineNum);
                            }
                        }
                        // PP Line
                        case 8 -> {
                            Skater center = team.getSkater(Integer.parseInt(lineInfo[1]));
                            Skater left = team.getSkater(Integer.parseInt(lineInfo[4]));
                            Skater right = team.getSkater(Integer.parseInt(lineInfo[5]));
                            if (center instanceof Center c && left instanceof Defenseman leftDe &&
                                    right instanceof Defenseman rightDe) {
                                line = new PPLine(lineInfo[0], c, team.getSkater(Integer.parseInt(lineInfo[2])),
                                        team.getSkater(Integer.parseInt(lineInfo[3])), leftDe, rightDe,
                                        Double.parseDouble(lineInfo[6]), Integer.parseInt(lineInfo[7]));
                            } else {
                                throw new FormatException(WRONG_POSITION, lineNum);
                            }
                        }
                        // PK Line
                        case 7 -> {
                            Skater left = team.getSkater(Integer.parseInt(lineInfo[3]));
                            Skater right = team.getSkater(Integer.parseInt(lineInfo[4]));
                            if (left instanceof Defenseman leftDe && right instanceof Defenseman rightDe) {
                                line = new PKLine(lineInfo[0], team.getSkater(Integer.parseInt(lineInfo[1])),
                                        team.getSkater(Integer.parseInt(lineInfo[2])), leftDe, rightDe,
                                        Double.parseDouble(lineInfo[5]), Integer.parseInt(lineInfo[6]));
                            } else {
                                throw new FormatException(WRONG_POSITION, lineNum);
                            }
                        }
                        default -> throw new FormatException(ELEMENT_NUM, lineNum);
                    }
                    index = team.addLine(line);
                    if (index < 0) {
                        throw new FormatException(FILE_DUPLICATE_LINE, lineNum);
                    }
                    lineNum++;
                }

                index = addTeam(team);
                if (index >= 0) {
                    teamSelection.insertItemAt(team, index);
                } else {
                    duplicates.add(team);
                }
            }
            return duplicates;
        } catch (IndexOutOfBoundsException ex) {
            throw new FormatException(ELEMENT_NUM, lineNum);
        } catch (NumberFormatException ex) {
            throw new FormatException(FILE_NUM_ERROR, lineNum);
        } catch (IllegalArgumentException | NullPointerException ex) {
            throw new FormatException(ex.getMessage(), lineNum);
        }
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
     * goalie, etc. as well as the position option combo boxes
     */
    private void setUpComboBoxes() {
        // Lines
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

        // Skaters
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

        // Goalies
        goalieOptions = new JComboBox<>();
        selectGoaliesForStats = new JComboBox<>();
        goalieOptions.addItem(null);
        selectGoaliesForStats.addItem(null);
        for (Goalie goalie: team.getGoalies()) {
            goalieOptions.addItem(goalie);
            selectGoaliesForStats.addItem(goalie);
        }

        // Positions
        Position[] positions = new Position[]{Position.Center, Position.Left_Wing, Position.Right_Wing,
                Position.Left_Defense, Position.Right_Defense};
        positionOptions = new JComboBox<>(positions);
        changePosition = new JComboBox<>(positions);
        scorerOptions = new JComboBox<>(positions);
        assistOptions1 = new JComboBox<>(positions);
        assistOptions1.insertItemAt(null, 0);
        assistOptions2 = new JComboBox<>(positions);
        assistOptions2.insertItemAt(null, 0);
    }

    /**
     * Adds the given line to the appropriate combo boxes and removes the old line from the appropriate combo boxes
     * @param oldLine Line being removed from combo boxes
     * @param newLine Line being added to combo boxes
     * @param newIndex Index where the new line falls in the team line ArrayList
     */
    private void updateLineComboBoxes(Line oldLine, Line newLine, int newIndex) {
        if (oldLine != null) {  // Delete old line from combo boxes
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

        if (newLine != null) {  // Insert new line in necessary combo boxes
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
        if (oldPlayer != null) {  // Remove old player
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

        if (oldPlayer == null && oldIndex == index) {  // Player is unchanged, but stats are updated
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

        if (newPlayer != null) {  // Add new player to proper locations
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
                } else if (newSkater instanceof Defenseman de) {
                    if (pickLeftDe.getItemCount() == 1) {
                        pickLeftDe.addItem(de);
                        pickRightDe.addItem(de);
                    } else {
                        int low = 1;
                        int high = pickLeftDe.getItemCount();
                        while (true) {
                            int i = (low + high) / 2;
                            if (de.getPlayerNumber() < pickLeftDe.getItemAt(i).getPlayerNumber()) {
                                if (i == 1 ||
                                        de.getPlayerNumber() > pickLeftDe.getItemAt(i - 1).getPlayerNumber()) {
                                    pickLeftDe.insertItemAt(de, i);
                                    pickRightDe.insertItemAt(de, i);
                                    break;
                                }
                                high = i;
                            } else {
                                if (i == pickLeftDe.getItemCount() - 1) {
                                    pickLeftDe.addItem(de);
                                    pickRightDe.addItem(de);
                                    break;
                                } else if (de.getPlayerNumber() < pickLeftDe.getItemAt(i + 1).getPlayerNumber()) {
                                    pickLeftDe.insertItemAt(de, i + 1);
                                    pickRightDe.insertItemAt(de, i + 1);
                                    break;
                                }
                                low = i;
                            }
                        }
                    }
                }
            } else if (newPlayer instanceof Goalie newGoalie) {
                goalieOptions.insertItemAt(newGoalie, index + 1);
                selectGoaliesForStats.insertItemAt(newGoalie, index + 1);
                goalieStats.insertRow(index, newGoalie.getStatsArray());
            }
        }
        viewRoster.setText(team.generateRoster());
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
                JOptionPane.showMessageDialog(mainFrame, EMPTY_INPUTS, EDIT_TEAM, JOptionPane.INFORMATION_MESSAGE);
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
                JOptionPane.showMessageDialog(mainFrame, BIN_FILE_ERROR, EDIT_TEAM, JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), EDIT_TEAM, JOptionPane.ERROR_MESSAGE);
            }
            if (!name.isBlank()) {  // Create new team with the same information but new name
                Team newTeam = new Team(team);
                newTeam.setName(name);
                changeTeamName.setText("");
                int index = TeamGUI.changeTeam(team, newTeam);
                updateTeamComponents(team, newTeam, index);
                if (index == -1) {
                    JOptionPane.showMessageDialog(mainFrame, "Two teams" + UNIQUE_NAME, EDIT_TEAM,
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                team = newTeam;
                mainFrame.setTitle(team.getName());
                try {
                    updateFile();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(mainFrame, BIN_FILE_ERROR, EDIT_TEAM, JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), EDIT_TEAM, JOptionPane.ERROR_MESSAGE);
                }
            } else {
                updateTeamComponents(null, null, -1);
            }
        });

        // Resets stats for team and updates relevant components
        resetTeamStats.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(mainFrame, RESET_CONFIRM + "the entire team" + TO_ZERO,
                    RESET_TEAM_STATS, JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {  // Ensure user truly wants to reset stats
                team.resetTeamStats();
                try {
                    updateFile();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(mainFrame, BIN_FILE_ERROR, RESET_TEAM_STATS,
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

        // Text Area that displays the team's roster
        viewRoster = new JTextArea(team.generateRoster());
        viewRoster.setEditable(false);
        JScrollPane viewRosterScroll = new JScrollPane(viewRoster);
        teamTabs.add("View Roster", viewRosterScroll);

        // Table that displays slightly more detailed roster with basic stats
        Container viewRosterWithStatsContent = new Container();
        viewRosterWithStatsContent.setLayout(new BoxLayout(viewRosterWithStatsContent, BoxLayout.Y_AXIS));
        JScrollPane viewRosterWithStatsScroll = new JScrollPane(viewRosterWithStatsContent);

        skaterStats = new StatsTableModel(team.generateSkaterRosterWithStats(), SKATER_STATS_COLUMNS);
        goalieStats = new StatsTableModel(team.generateGoalieRosterWithStats(), GOALIE_STATS_COLUMNS);
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
        enterSkaterNumber = new JSlider(1, Player.PLAYER_NUM_MAX);
        enterSkaterNumberLabel = new JLabel(PLAYER_NUMBER_STRING + "  " + enterSkaterNumber.getValue());
        createPanelForContainer(new JComponent[]{enterSkaterNameLabel, enterSkaterName, enterSkaterNumberLabel,
                        enterSkaterNumber}, createSkater);

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

        createPlayer = new JButton(CREATE_SKATER);
        createPanelForContainer(new JComponent[]{createPlayer}, createSkater);

        // Labels and fields for initializing player stats

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

        /*
          Adds stat components to the container when selected for the user to initialize their skater's stats.
          Removes stat components when unselected.
         */
        assignSkaterStats.addActionListener(e -> {
            if (assignSkaterStats.isSelected()) {
                Position position = (Position) positionOptions.getSelectedItem();
                createSkater.add(basicStatsPanel, createSkater.getComponentCount() - 1);
                createSkater.add(advancedStatsPanel, createSkater.getComponentCount() - 1);
                if (position == Position.Center) {  // Adds panel to initialize center stats
                    createSkater.add(centerStatsPanel, createSkater.getComponentCount() - 1);
                } else if (position == Position.Left_Defense || position == Position.Right_Defense) {  // Panel for de
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

        // Adds/Removes relevant panels depending on what position is selected for the new player
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

        // Takes in input from the user in the various fields and creates new player from this information
        createPlayer.addActionListener(e -> {
            String name = enterSkaterName.getText();
            String stickHand;
            Skater newSkater;
            if (chooseStickHand.isSelected()) {
                stickHand = Skater.STICK_LEFT;
            } else {
                stickHand = Skater.STICK_RIGHT;
            }
            Position position = (Position) positionOptions.getSelectedItem();
            try {
                if (assignSkaterStats.isSelected()) {  // Initializes stats of the new skater
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
                } else {  // Uses more limited constructor, other stats initialized to 0
                    if (position == Position.Center) {
                        newSkater = new Center(name, enterSkaterNumber.getValue(), stickHand);
                    } else if (position == Position.Left_Defense || position == Position.Right_Defense) {
                        newSkater = new Defenseman(name, enterSkaterNumber.getValue(), stickHand, position);
                    } else {
                        newSkater = new Skater(name, enterSkaterNumber.getValue(), stickHand, position);
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(mainFrame, NUMBER_ERROR, CREATE_SKATER, JOptionPane.ERROR_MESSAGE);
                return;
            } catch (IllegalArgumentException | NullPointerException ex) {
                JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), CREATE_SKATER, JOptionPane.ERROR_MESSAGE);
                return;
            }
            int index = team.addPlayer(newSkater);
            if (index == -1) {  // Player is a duplicate
                JOptionPane.showMessageDialog(mainFrame, PLAYER_NUMBER_DUPLICATE, CREATE_SKATER,
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            updatePlayerComponents(null, newSkater, -1, index);

            try {
                updateFile();
                JOptionPane.showMessageDialog(mainFrame, CREATE_SUCCESS + "Skater", CREATE_SKATER,
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
                JOptionPane.showMessageDialog(selectFrame, BIN_FILE_ERROR, CREATE_SKATER, JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(selectFrame, ex.getMessage(), CREATE_SKATER, JOptionPane.ERROR_MESSAGE);
            }
        });

        skaterTabs.add(CREATE_SKATER, createSkaterScroll);

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

        // Components for various stats related to the skater

        changeGoalsLabel = new JLabel(GOALS_STRING);
        changeGoals = new JTextField(ENTER_STAT_SIZE);
        changeAssistsLabel = new JLabel(ASSISTS_STRING);
        changeAssists = new JTextField(ENTER_STAT_SIZE);
        changePMLabel = new JLabel(PM_STRING);
        changePlusMinus = new JTextField(ENTER_STAT_SIZE);
        createPanelForContainer(new JComponent[]{changeGoalsLabel, changeGoals, changeAssistsLabel, changeAssists,
                changePMLabel, changePlusMinus}, editSkaterContent);

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

        /*
         Updates stick hand swap button and adds proper components for a center or defenseman to the edit player tab
         Updates View Skater Text Area to display the currently selected skater's stats
         */
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

        /*
           Updates any changes being made to a skater in the edit skaters tab
           If a field is blank, it is not updated
         */
        editPlayer.addActionListener(e -> {
            Skater editingSkater = (Skater) skaterOptions.getSelectedItem();
            if (editingSkater == null) {  // Did not choose a skater
                JOptionPane.showMessageDialog(mainFrame, SELECT, EDIT_SKATER, JOptionPane.ERROR_MESSAGE);
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
                    if (editingSkater.getStickHand().equals(Skater.STICK_LEFT)) {
                        editingSkater.setStickHand(Skater.STICK_RIGHT);
                    } else {
                        editingSkater.setStickHand(Skater.STICK_LEFT);
                    }
                    changeStickHand.setSelected(false);
                    changeStickHandLabel.setText(STICK_HAND_STRING + editingSkater.getStickHand());
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
                JOptionPane.showMessageDialog(mainFrame, NUMBER_ERROR + BLANK_UPDATED, EDIT_SKATER,
                        JOptionPane.ERROR_MESSAGE);
                return;
            } catch (IllegalArgumentException | NullPointerException ex) {
                JOptionPane.showMessageDialog(mainFrame, ex.getMessage() + BLANK_UPDATED, EDIT_SKATER,
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
            } else if (changeSkaterNumberCheck.isSelected()) {  // Changes player number if needed
                if (editingSkater instanceof Center) {
                    newSkater = new Center(editingSkater);
                } else if (editingSkater instanceof  Defenseman) {
                    newSkater = new Defenseman(editingSkater);
                } else {
                    newSkater = new Skater(editingSkater);
                }
                newSkater.setPlayerNumber(changeSkaterNumber.getValue());
                change = true;
            }

            if (change) {  // Update GUI components, file, and the team lists if necessary
                int oldIndex = skaterOptions.getSelectedIndex() - 1;  // Index where the skater used to be

                if (newSkater != null) {
                    int input;
                    do {  // Confirmation message, making these changes will delete lines containing this player
                        input = JOptionPane.showConfirmDialog(mainFrame, EDIT_SKATER_CONFIRM, EDIT_SKATER,
                                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    } while (input != JOptionPane.YES_OPTION && input != JOptionPane.NO_OPTION);
                    if (input == JOptionPane.NO_OPTION) {
                        return;
                    }
                    int index = team.changePlayer(editingSkater, newSkater);
                    if (index == -1) {  // Player is a duplicate
                        JOptionPane.showMessageDialog(mainFrame, PLAYER_NUMBER_DUPLICATE, EDIT_SKATER,
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    updatePlayerComponents(editingSkater, newSkater, oldIndex, index);
                    changeSkaterNumberCheck.setSelected(false);
                    changeSkaterNumberCheck.setText(CHANGE_NUMBER);
                    nameAndNumberPanel.remove(changeSkaterNumber);
                } else {
                    updatePlayerComponents(null, editingSkater, oldIndex, oldIndex);
                    changePositionCheck.setText(CHANGE_POSITION + editingSkater.getPosition());
                    mainFrame.repaint();
                }
                try {
                    updateFile();
                    JOptionPane.showMessageDialog(mainFrame, UPDATE_SUCCESS, EDIT_SKATER,
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(mainFrame, BIN_FILE_ERROR, EDIT_SKATER, JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), EDIT_SKATER, JOptionPane.ERROR_MESSAGE);
                }
            } else {  // Displays message telling the user to enter something
                JOptionPane.showMessageDialog(mainFrame, EMPTY_INPUTS, EDIT_SKATER, JOptionPane.ERROR_MESSAGE);
            }
        });

        // Confirms user's selection and resets stats of the selected player, updates relevant components
        resetPlayerStats.addActionListener(e -> {
            Skater selectedSkater = (Skater) skaterOptions.getSelectedItem();
            if (selectedSkater != null) {
                int confirm = JOptionPane.showConfirmDialog(mainFrame, RESET_CONFIRM +
                        selectedSkater.getName() + TO_ZERO, EDIT_SKATER, JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    selectedSkater.resetStats();
                    updatePlayerComponents(null, selectedSkater, skaterOptions.getSelectedIndex() - 1,
                            skaterOptions.getSelectedIndex() - 1);
                    try {
                        updateFile();
                        JOptionPane.showMessageDialog(mainFrame, UPDATE_SUCCESS, EDIT_SKATER,
                                JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(mainFrame, BIN_FILE_ERROR, EDIT_SKATER,
                                JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), EDIT_SKATER,
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(mainFrame, SELECT, EDIT_SKATER, JOptionPane.ERROR_MESSAGE);
            }
        });

        // Deletes the selected player from the team and any GUI elements
        deletePlayer.addActionListener(e -> {
            Skater deletingSkater = (Skater) skaterOptions.getSelectedItem();
            if (deletingSkater == null) {
                JOptionPane.showMessageDialog(mainFrame, SELECT, DELETE_SKATER, JOptionPane.ERROR_MESSAGE);
                return;
            }
            int selection = JOptionPane.showConfirmDialog(mainFrame, CONFIRM_DELETE + deletingSkater.getName()
                    + "?", DELETE_SKATER, JOptionPane.YES_NO_OPTION);
            if (selection == JOptionPane.YES_OPTION) {
                team.removePlayer(deletingSkater);
                updatePlayerComponents(deletingSkater, null, skaterOptions.getSelectedIndex() - 1,
                        -1);

                try {
                    updateFile();
                    JOptionPane.showMessageDialog(mainFrame, UPDATE_SUCCESS, DELETE_SKATER,
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(mainFrame, BIN_FILE_ERROR, DELETE_SKATER, JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), DELETE_SKATER, JOptionPane.ERROR_MESSAGE);
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

        createGoalie = new JButton(CREATE_GOALIE);
        createPanelForContainer(new JComponent[]{createGoalie}, createGoalieContent);

        // Adds relevant stat panels to the container when selected, removes them when deselected
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
                if (assignGoalieStats.isSelected()) {  // Initialize stats with user values
                    int[] record = checkForIntResponses(new String[]{enterGoalieWins.getText(),
                            enterGoalieLosses.getText(), enterGoalieOTLosses.getText()});
                    int shutouts = enterShutouts.getValue();
                    double savePercent = Double.parseDouble(enterSavePercentage.getText());
                    int shotsFaced = Integer.parseInt(enterGoalieShotsAgainst.getText());
                    newGoalie = new Goalie(name, goalieNum, savePercent, shotsFaced, record[0], record[1], record[2],
                            shutouts);
                } else {  // Initialize stats to 0
                    newGoalie = new Goalie(name, goalieNum);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(mainFrame, NUMBER_ERROR, CREATE_GOALIE, JOptionPane.ERROR_MESSAGE);
                return;
            } catch (NullPointerException | IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), CREATE_GOALIE, JOptionPane.ERROR_MESSAGE);
                return;
            }

            int index = team.addPlayer(newGoalie);
            if (index == -1) {  // Player is a duplicate
                JOptionPane.showMessageDialog(mainFrame, PLAYER_NUMBER_DUPLICATE, CREATE_GOALIE,
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                updateFile();
                updatePlayerComponents(null, newGoalie, -1, index);
                // Reset text fields
                enterGoalieName.setText("");
                enterGoalieWins.setText("");
                enterGoalieLosses.setText("");
                enterGoalieOTLosses.setText("");
                enterSavePercentage.setText("");
                enterGoalieShotsAgainst.setText("");
                JOptionPane.showMessageDialog(mainFrame, CREATE_SUCCESS + "Goalie", CREATE_GOALIE,
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(mainFrame, BIN_FILE_ERROR, CREATE_GOALIE, JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), CREATE_GOALIE, JOptionPane.ERROR_MESSAGE);
            }
        });

        goalieTabs.add(CREATE_GOALIE, createGoalieContent);

        // Edit/Delete Goalie
        Container editGoalieContent = new Container();
        editGoalieContent.setLayout(new BoxLayout(editGoalieContent, BoxLayout.Y_AXIS));

        editGoalieInstructions = new JLabel(EDIT_PLAYER_INSTRUCTIONS);
        createPanelForContainer(new JComponent[]{editGoalieInstructions}, editGoalieContent);
        changeGoalieNameLabel = new JLabel(NAME_STRING);
        changeGoalieName = new JTextField(ENTER_NAME_SIZE);
        changeGoalieNumberCheck = new JCheckBox(CHANGE_NUMBER);
        changeGoalieNumber = new JSlider(1, Player.PLAYER_NUM_MAX);
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

        // Components for other stats

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

        /*
          Parses any changes to stats entered by the user, makes those changes, and updates the relevant components.
          Any blank fields will be ignored and unchanged.
         */
        editGoalie.addActionListener(e -> {
            Goalie editingGoalie = (Goalie) goalieOptions.getSelectedItem();
            if (editingGoalie == null) {  // No selection made
                JOptionPane.showMessageDialog(mainFrame, SELECT, EDIT_GOALIE, JOptionPane.ERROR_MESSAGE);
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
                // Attempt to update all of the stats that are entered by the user
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
                JOptionPane.showMessageDialog(mainFrame, NUMBER_ERROR + BLANK_UPDATED, EDIT_GOALIE,
                        JOptionPane.ERROR_MESSAGE);
                return;
            } catch (NullPointerException | IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(mainFrame, ex.getMessage() + BLANK_UPDATED, EDIT_GOALIE,
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            Goalie newGoalie = null;

            if (changeGoalieNumberCheck.isSelected()) {  // Create new goalie with same info, but different number
                newGoalie = new Goalie(editingGoalie);
                newGoalie.setPlayerNumber(changeGoalieNumber.getValue());
            }

            int oldIndex = goalieOptions.getSelectedIndex() - 1;
            if (newGoalie != null) {
                int index = team.changePlayer(editingGoalie, newGoalie);
                if (index == -1) {  // Player is duplicate
                    JOptionPane.showMessageDialog(mainFrame, PLAYER_NUMBER_DUPLICATE + BLANK_UPDATED,
                            EDIT_GOALIE, JOptionPane.ERROR_MESSAGE);
                    return;
                }
                updatePlayerComponents(editingGoalie, newGoalie, oldIndex, index);
            } else {
                updatePlayerComponents(null, editingGoalie, oldIndex, oldIndex);
                mainFrame.repaint();
            }

            try {
                updateFile();
                JOptionPane.showMessageDialog(mainFrame, UPDATE_SUCCESS, EDIT_GOALIE, JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(mainFrame, BIN_FILE_ERROR, EDIT_GOALIE, JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), EDIT_GOALIE, JOptionPane.ERROR_MESSAGE);
            }
        });

        // Reset selected goalie's stats to 0 and update the relevant components
        resetGoalieStats.addActionListener(e -> {
            Goalie editingGoalie = (Goalie) goalieOptions.getSelectedItem();
            if (editingGoalie == null) {
                JOptionPane.showMessageDialog(mainFrame, SELECT, EDIT_GOALIE, JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(mainFrame, RESET_CONFIRM + editingGoalie.getName() +
                    TO_ZERO, EDIT_GOALIE, JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                editingGoalie.resetStats();
                updatePlayerComponents(null, editingGoalie, goalieOptions.getSelectedIndex() - 1,
                        goalieOptions.getSelectedIndex() - 1);
                try {
                    updateFile();
                    JOptionPane.showMessageDialog(mainFrame, UPDATE_SUCCESS, EDIT_GOALIE,
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(mainFrame, BIN_FILE_ERROR, EDIT_GOALIE, JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), EDIT_GOALIE, JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Confirm user selection, remove the selected goalie from the team's lists, and update any relevant components
        deleteGoalie.addActionListener(e -> {
            Goalie deletingGoalie = (Goalie) goalieOptions.getSelectedItem();
            if (deletingGoalie == null) {
                JOptionPane.showMessageDialog(mainFrame, SELECT, DELETE_GOALIE, JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(mainFrame, CONFIRM_DELETE + deletingGoalie.getName()
                    + "?", DELETE_GOALIE, JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                team.removePlayer(deletingGoalie);
                updatePlayerComponents(deletingGoalie, null, goalieOptions.getSelectedIndex() - 1,
                        -1);
                try {
                    updateFile();
                    JOptionPane.showMessageDialog(mainFrame, UPDATE_SUCCESS, DELETE_GOALIE,
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(mainFrame, BIN_FILE_ERROR, DELETE_GOALIE, JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), DELETE_GOALIE, JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        goalieTabs.add("Edit or Delete Goalie", editGoalieContent);

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

        // Radio buttons for selecting the different line types
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

        // Panels to select the players at different positions

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

        /*
         Displays text fields to enter success percentage and number of opportunities for the newly created line
         Only appears/applies to special teams lines
         */
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

        /*
         Action Listener for the Radio Buttons (lineType)
         Adds relevant components to container depending on selected line
         */
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

        /*
          Reads in information provided by the user, creates new line from that information, and updates relavant
          components with that new line
         */
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
                    JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), CREATE_LINE, JOptionPane.ERROR_MESSAGE);
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
            if (index == -1) {  // Line is a duplicate
                JOptionPane.showMessageDialog(mainFrame, "Two lines" + UNIQUE_NAME, CREATE_LINE,
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Reset Text Boxes
            lineName.setText("");
            enterSuccessPercentage.setText("");
            enterNumOpps.setText("");

            updateLineComboBoxes(null, newLine, index);

            try {
                updateFile();
                JOptionPane.showMessageDialog(mainFrame, CREATE_SUCCESS + "New Line",
                        CREATE_LINE, JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(mainFrame, BIN_FILE_ERROR, CREATE_LINE, JOptionPane.ERROR_MESSAGE);
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

        // Updates changes input by the user, updates the team and relevant components with the new changes
        updateLineChanges.addActionListener(e -> {
            if (lineOptions.getSelectedItem() == null) {  // No line selected
                JOptionPane.showMessageDialog(mainFrame, SELECT, EDIT_LINE,
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            String name = changeLineName.getText();
            Line editingLine = (Line) lineOptions.getSelectedItem();
            Line newLine = null;
            if (editingLine instanceof OffenseLine oLine) {  // Offense line, only can change name
                try {
                    newLine = new OffenseLine(oLine);
                    newLine.setName(name);
                } catch (IllegalArgumentException | NullPointerException ex) {
                    JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), EDIT_LINE, JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else if (editingLine instanceof DefenseLine defenseLine) {  // De line, only can change name
                try {
                    newLine = new DefenseLine(defenseLine);
                    newLine.setName(name);
                } catch (IllegalArgumentException | NullPointerException ex) {
                    JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), EDIT_LINE, JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else if (editingLine instanceof SpecialTeamsLine specialLine) {  // Special Teams
                try {
                    // Stats not updated by user
                    if (changeSuccessPercent.getText().isBlank() && changeNumOpps.getText().isBlank()) {
                        if (specialLine instanceof PPLine pp) {
                            newLine = new PPLine(pp);
                            newLine.setName(name);
                        } else if (specialLine instanceof PKLine pk) {
                            newLine = new PKLine(pk);
                            newLine.setName(name);
                        }
                    } else {  // Stats and/or name being updated
                        double successPercent = Double.parseDouble(changeSuccessPercent.getText());
                        int numAttempts = Integer.parseInt(changeNumOpps.getText());
                        specialLine.setSuccessStats(successPercent, numAttempts);
                        changeSuccessPercent.setText("");
                        changeNumOpps.setText("");
                        if (name.isBlank()) {  // No name change, do not need to replace line on team
                            try {
                                updateFile();
                                viewLine.setText(editingLine.lineRoster());
                                JOptionPane.showMessageDialog(mainFrame, UPDATE_SUCCESS, EDIT_LINE,
                                        JOptionPane.INFORMATION_MESSAGE);
                            } catch (IOException ex) {
                                JOptionPane.showMessageDialog(mainFrame, BIN_FILE_ERROR, EDIT_LINE,
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
                    JOptionPane.showMessageDialog(mainFrame, NUMBER_ERROR, EDIT_LINE, JOptionPane.ERROR_MESSAGE);
                    return;
                } catch (IllegalArgumentException | NullPointerException ex) {
                    JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), EDIT_LINE, JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            int index = team.changeLine(editingLine, newLine);
            if (index == -1) {  // New name is a duplicate
                JOptionPane.showMessageDialog(mainFrame, "Two lines" + UNIQUE_NAME, EDIT_LINE,
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            updateLineComboBoxes(editingLine, newLine, index);

            try {
                updateFile();
                viewLine.setText(editingLine.lineRoster());
                changeLineName.setText("");
                lineOptions.setSelectedIndex(0);
                JOptionPane.showMessageDialog(mainFrame, UPDATE_SUCCESS, EDIT_LINE, JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(mainFrame, BIN_FILE_ERROR, EDIT_LINE, JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), EDIT_LINE, JOptionPane.ERROR_MESSAGE);
            }
        });

        /*
          Displays a new JFrame where the user can select new players to assign to the line
          Main frame will be hidden while this frame is active and will reappear once the user is finished, or the new
          frame is closed
         */
        changeLinePlayers.addActionListener(e -> {
            if (lineOptions.getSelectedItem() == null) {  // No line selected
                JOptionPane.showMessageDialog(mainFrame, SELECT, EDIT_LINE, JOptionPane.ERROR_MESSAGE);
                return;
            }
            mainFrame.setVisible(false);

            Line editingLine = (Line) lineOptions.getSelectedItem();
            // Set up new frame
            JFrame playersFrame = new JFrame("Change Players");
            playersFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            Container playersFrameContent = playersFrame.getContentPane();
            playersFrameContent.setLayout(new BoxLayout(playersFrameContent, BoxLayout.Y_AXIS));
            playersFrame.setLocationRelativeTo(mainFrame);

            /*
              Clears the selection in create line tab and removes any selected components
              These components are reused in this frame, and glitchy behavior occurs if they are not removed
             */
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
            createPanelForContainer(new JComponent[]{displayCurrentPlayers}, playersFrameContent);

            // Add proper select player panels to the frame
            if (editingLine instanceof OffenseLine) {
                playersFrameContent.add(selectCenterPanel);
                playersFrameContent.add(selectLWPanel);
                playersFrameContent.add(selectRWPanel);
            } else if (editingLine instanceof DefenseLine) {
                playersFrameContent.add(selectLDPanel);
                playersFrameContent.add(selectRDPanel);
            } else if (editingLine instanceof PPLine) {
                playersFrameContent.add(selectCenterPanel);
                playersFrameContent.add(selectLWPanel);
                playersFrameContent.add(selectRWPanel);
                playersFrameContent.add(selectLDPanel);
                playersFrameContent.add(selectRDPanel);
            } else if (editingLine instanceof PKLine) {
                playersFrameContent.add(selectLWPanel);
                playersFrameContent.add(selectRWPanel);
                playersFrameContent.add(selectLDPanel);
                playersFrameContent.add(selectRDPanel);
                selectLWLabel.setText(OFFENSE + "1:");
                selectRWLabel.setText(OFFENSE + "2:");
            }

            JButton assignPlayers = new JButton("Assign These Players");
            createPanelForContainer(new JComponent[]{assignPlayers}, playersFrameContent);

            JLabel changePlayersNote = new JLabel(CHANGE_LINE_NOTE);
            createPanelForContainer(new JComponent[]{changePlayersNote}, playersFrameContent);
            playersFrame.pack();
            playersFrame.setVisible(true);

            /*
              If a player is selected, replace that player in the line.
              If a box selection is left blank, no change is made.
             */
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
                    JOptionPane.showMessageDialog(playersFrame, ex.getMessage(), EDIT_LINE,
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (change) {
                    try {
                        updateFile();
                        viewLine.setText(editingLine.lineRoster());
                        JOptionPane.showMessageDialog(playersFrame, UPDATE_SUCCESS, EDIT_LINE,
                                JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(mainFrame, BIN_FILE_ERROR, EDIT_LINE, JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), EDIT_LINE, JOptionPane.ERROR_MESSAGE);
                    }
                    playersFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "Please select new players for the line or close" +
                            " the window.", EDIT_LINE, JOptionPane.ERROR_MESSAGE);
                }
            });

            // Redisplay main JFrame when the window is closed
            playersFrame.addWindowListener(new WindowAdapter() {
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
                JOptionPane.showMessageDialog(mainFrame, SELECT, DELETE_LINE, JOptionPane.ERROR_MESSAGE);
                return;
            }

            int response = JOptionPane.showConfirmDialog(mainFrame, CONFIRM_DELETE +
                    removingLine.getName() + "?", DELETE_LINE, JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                if (team.removeLine(removingLine)) {
                    updateLineComboBoxes(removingLine, null, -1);
                    try {
                        updateFile();
                        JOptionPane.showMessageDialog(mainFrame, UPDATE_SUCCESS, DELETE_LINE,
                                JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(mainFrame, BIN_FILE_ERROR, DELETE_LINE,
                                JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), DELETE_LINE,
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(mainFrame, UNEXPECTED_ERROR + "deleteLine.addActionListener",
                            DELETE_LINE, JOptionPane.ERROR_MESSAGE);
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

        // Enter Game Stats

        enterStatsTabs = new JTabbedPane();

        // Components used in both tabs for scoring a goal
        scorerLabel = new JLabel("Position of Scorer:");
        JPanel scorerPanel = createPanel(new JComponent[]{scorerLabel, scorerOptions});

        assistLabel1 = new JLabel("Position of Assist 1 (if assisted):");
        JPanel assistPanel1 = createPanel(new JComponent[]{assistLabel1, assistOptions1});

        assistLabel2 = new JLabel("Position of Assist 2 (if assisted):");
        JPanel assistPanel2 = createPanel(new JComponent[]{assistLabel2, assistOptions2});

        playerScoreLabel = new JLabel(SELECT_SCORER);
        JPanel playerScorePanel = createPanel(new JComponent[]{playerScoreLabel, scorerPlayerOptions});

        otherPlayersLabel = new JLabel("Select Other Skaters on the Ice:");
        JPanel otherPlayersPanel = new JPanel();
        otherPlayersPanel.add(otherPlayersLabel);

        assist1Check = new JCheckBox(ASSIST_CHECK);
        JPanel assistPlayerPanel1 = createPanel(new JComponent[]{assistPlayerOptions1, assist1Check});

        assist2Check = new JCheckBox(ASSIST_CHECK);
        JPanel assistPlayerPanel2 = createPanel(new JComponent[]{assistPlayerOptions2, assist2Check});

        JPanel selectOtherPlayersPanel = createPanel(new JComponent[]{otherPlayerOptions1,
                otherPlayerOptions2});

        useLinesOrPlayers = new JToggleButton(USE_PLAYERS);
        JPanel linesOrPlayersPanel = new JPanel();
        linesOrPlayersPanel.add(useLinesOrPlayers);

        // Enter Live

        Container liveStats = new Container();
        liveStats.setLayout(new BoxLayout(liveStats, BoxLayout.Y_AXIS));

        JLabel enterLiveInstructions = new JLabel(ENTER_LIVE_INSTRUCTIONS);
        createPanelForContainer(new JComponent[]{enterLiveInstructions}, liveStats);

        // Panels to select current lines/goalies

        offenseLinesLabel = new JLabel(SELECT_CURRENT + OFFENSE_LINE + ':');
        JPanel offenseLinePanel = createPanel(new JComponent[]{offenseLinesLabel, offenseLines});
        liveStats.add(offenseLinePanel);

        deLinesLabel = new JLabel(SELECT_CURRENT + DEFENSE_LINE + ":");
        JPanel defenseLinePanel = createPanel(new JComponent[]{deLinesLabel, defenseLines});
        liveStats.add(defenseLinePanel);

        ppLineLabel = new JLabel(SELECT_CURRENT + PP_LINE + ':');
        JPanel ppLinePanel = createPanel(new JComponent[]{ppLineLabel, ppOptions});

        pkOptionsLabel = new JLabel(SELECT_CURRENT + PK_LINE + ':');
        JPanel pkLinePanel = createPanel(new JComponent[]{pkOptionsLabel, pkOptions});

        goaliesForStatsLabel = new JLabel(SELECT_CURRENT + "Goalie In Net:");
        JPanel selectGoaliePanel = createPanel(new JComponent[]{goaliesForStatsLabel, selectGoaliesForStats});
        liveStats.add(selectGoaliePanel);

        // Track current score of the game based on user input
        AtomicInteger teamGoals = new AtomicInteger();
        AtomicInteger opponentGoals = new AtomicInteger();
        currentScore = new JLabel(CURRENT_SCORE + teamGoals.get() + "-" +
                opponentGoals.get());
        createPanelForContainer(new JComponent[]{currentScore}, liveStats);

        goalLive = new JButton(TEAM_GOAL);
        scoredAgainstLive = new JButton(OPPONENT_GOAL);
        createPanelForContainer(new JComponent[]{goalLive, scoredAgainstLive}, liveStats);

        penaltyOver = new JButton("Penalty Expired");
        JPanel penaltyOverPanel = new JPanel();
        penaltyOverPanel.add(penaltyOver);

        /*
          Prompt the user in a new window to select the positions of the current line that were involved with the goal
          or manually select the players who scored the goal.
          The user is able to leave the window if needed, and the enter stats live tab will be updated with the new
          score.
         */
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
            JButton cancelButton = new JButton(CANCEL);
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

            // Updates the screen to display player select or position select depending on toggle selection
            ActionListener switchLinesAndPlayersLive = e1 -> {
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
            };
            useLinesOrPlayers.addActionListener(switchLinesAndPlayersLive);

            /*
              Updates selected player stats, updates team goals, displays new team score in the enter live tab
              If power play is active, PPLine will succeed, and the enter live tab will be reset
             */
            ActionListener enterGoal = e1 -> {
                if (e1.getActionCommand().equals(enterGoalButton.getActionCommand())) {
                    if (useLinesOrPlayers.isSelected()) {
                        Skater scorer = (Skater) scorerPlayerOptions.getSelectedItem();
                        Skater assist1 = (Skater) assistPlayerOptions1.getSelectedItem();
                        Skater assist2 = (Skater) assistPlayerOptions2.getSelectedItem();
                        Skater other1 = (Skater) otherPlayerOptions1.getSelectedItem();
                        Skater other2 = (Skater) otherPlayerOptions2.getSelectedItem();
                        if (scorer == null || assist1 == null || assist2 == null || other1 == null || other2 == null) {
                            JOptionPane.showMessageDialog(enterGoalLive, SELECT, ENTER_STATS_LIVE,
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        if (scorer.equals(assist1) || scorer.equals(assist2) || scorer.equals(other1) ||
                                scorer.equals(other2) || assist1.equals(assist2) || assist1.equals(other1)
                                || assist1.equals(other2) || assist2.equals(other1) ||
                                assist2.equals(other2) || other1.equals(other2)) {  // Check duplicate players
                            JOptionPane.showMessageDialog(enterGoalLive, PLAYER_DUPLICATE, ENTER_STATS_LIVE,
                                    JOptionPane.ERROR_MESSAGE);
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
                                JOptionPane.showMessageDialog(enterGoalLive, USE_ASSIST_1, ENTER_STATS_LIVE,
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            try {
                                if (scoringLine instanceof OffenseLine oLine) {
                                    DefenseLine dLine = (DefenseLine) defenseLines.getSelectedItem();
                                    if (dLine == null) {
                                        JOptionPane.showMessageDialog(enterGoalLive, NO_OPTIONS + "line " +
                                                        "or select players manually.", ENTER_STATS_LIVE,
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
                                        liveStats.add(defenseLinePanel, 1);
                                        liveStats.add(offenseLinePanel, 1);
                                        mainFrame.repaint();
                                    }
                                }
                            } catch (IllegalArgumentException ex) {
                                JOptionPane.showMessageDialog(enterGoalLive, ex.getMessage(), ENTER_STATS_LIVE,
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        } else {
                            JOptionPane.showMessageDialog(enterGoalLive, NO_OPTIONS + "line or select " +
                                            "players manually.", ENTER_STATS_LIVE, JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                    teamGoals.getAndIncrement();
                    currentScore.setText(CURRENT_SCORE + teamGoals.get() + '-' + opponentGoals.get());
                }
                enterGoalLive.dispose();  // Close window once goal is entered
            };

            enterGoalButton.addActionListener(enterGoal);
            cancelButton.addActionListener(enterGoal);

            enterGoalLive.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    mainFrame.setVisible(true);
                    useLinesOrPlayers.removeActionListener(switchLinesAndPlayersLive);
                }
            });

            enterGoalLive.pack();
            enterGoalLive.setVisible(true);
        });

        /*
          Display new window where similar to team goal window, user will select players who were on the ice for
          the goal to adjust their plus minus stats using the line or selecting the players manually
          If a penalty kill is active, the current line will fail the penalty kill and the enter live tab will be reset
         */
        scoredAgainstLive.addActionListener(e -> {
            Goalie goalieInNet = (Goalie) selectGoaliesForStats.getSelectedItem();
            int input;
            if (goalieInNet == null) {
                do {
                    input = JOptionPane.showConfirmDialog(mainFrame, "You do not currently have a goalie" +
                            " in net. Was this an empty net goal?", ENTER_STATS_LIVE, JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE);
                } while (input != JOptionPane.YES_OPTION && input != JOptionPane.NO_OPTION);
                if (input == JOptionPane.NO_OPTION) {
                    return;
                }
            }
            if (penaltyLive.isSelected()) {
                PKLine failedLine = (PKLine) pkOptions.getSelectedItem();
                if (failedLine != null) {
                    failedLine.lineScoredOn();
                    penaltyOptionsLive.clearSelection();
                    liveStats.remove(pkLinePanel);
                    liveStats.remove(penaltyOverPanel);
                    liveStats.add(defenseLinePanel, 1);
                    liveStats.add(offenseLinePanel, 1);
                } else {
                    JOptionPane.showMessageDialog(mainFrame, NO_OPTIONS + "penalty kill line.",
                            ENTER_STATS_LIVE, JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else if (powerPlayLive.isSelected()) {
                PPLine failedLine = (PPLine) ppOptions.getSelectedItem();
                if (failedLine != null) {
                    failedLine.lineScoredOn();
                } else {
                    JOptionPane.showMessageDialog(mainFrame, NO_OPTIONS + "power play line.",
                            ENTER_STATS_LIVE, JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                do {  // Ask user if they want to enter players manually or not
                    input = JOptionPane.showConfirmDialog(mainFrame, "Can your currently selected lines be " +
                                    "used for adjusting +/- stats?", ENTER_STATS_LIVE, JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
                } while (input != JOptionPane.YES_OPTION && input != JOptionPane.NO_OPTION);
                if (input == JOptionPane.YES_OPTION) {  // Use lines
                    OffenseLine oLine = (OffenseLine) offenseLines.getSelectedItem();
                    DefenseLine dLine = (DefenseLine) defenseLines.getSelectedItem();
                    if (oLine != null && dLine != null) {
                        oLine.lineScoredOn();
                        dLine.lineScoredOn();
                    } else {
                        JOptionPane.showMessageDialog(mainFrame, NO_OPTIONS +
                                        "offense line and a defense line.",
                                ENTER_STATS_LIVE, JOptionPane.ERROR_MESSAGE);
                    }
                } else {  // Select player manually
                    if (team.getSkaters().length < 5) {
                        JOptionPane.showMessageDialog(mainFrame,
                                NO_OPTIONS.substring(0, NO_OPTIONS.length() - 1) + "t least 5 skaters.",
                                ENTER_STATS_LIVE, JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    mainFrame.setVisible(false);
                    // Set up window
                    JWindow scoredOnWindow = new JWindow(mainFrame);
                    scoredOnWindow.setLocationRelativeTo(mainFrame);
                    Container scoredOnContent = scoredOnWindow.getContentPane();
                    scoredOnContent.setLayout(new BoxLayout(scoredOnContent, BoxLayout.Y_AXIS));
                    JLabel scoredOnPlayersLabel = new JLabel("Select Players on Ice when Goal was Scored");
                    createPanelForContainer(new JComponent[]{scoredOnPlayersLabel}, scoredOnContent);
                    playerScoreLabel.setText("");
                    scoredOnContent.add(playerScorePanel);
                    assistPlayerPanel1.remove(assist1Check);
                    scoredOnContent.add(assistPlayerPanel1);
                    assistPlayerPanel2.remove(assist2Check);
                    scoredOnContent.add(assistPlayerPanel2);
                    scoredOnContent.add(selectOtherPlayersPanel);
                    JButton enterPlayers = new JButton("Select Players");
                    createPanelForContainer(new JComponent[]{enterPlayers}, scoredOnContent);
                    // Check selected players, and enter goal scored
                    enterPlayers.addActionListener(e1 -> {
                        Skater skater1 = (Skater) scorerPlayerOptions.getSelectedItem();
                        Skater skater2 = (Skater) assistPlayerOptions1.getSelectedItem();
                        Skater skater3 = (Skater) assistPlayerOptions2.getSelectedItem();
                        Skater skater4 = (Skater) otherPlayerOptions1.getSelectedItem();
                        Skater skater5 = (Skater) otherPlayerOptions2.getSelectedItem();
                        if (skater1 == null || skater2 == null || skater3 == null || skater4 == null || skater5 == null)
                        {
                            JOptionPane.showMessageDialog(mainFrame, UNEXPECTED_ERROR +
                                    "enterPlayers.addActionListener", ENTER_STATS_LIVE, JOptionPane.ERROR_MESSAGE);
                            scoredOnWindow.dispose();
                            return;
                        }
                        if (skater1.equals(skater2) || skater1.equals(skater3) || skater1.equals(skater4) ||
                                skater1.equals(skater5) || skater2.equals(skater3) || skater2.equals(skater4) ||
                                skater2.equals(skater5) || skater3.equals(skater4) || skater3.equals(skater5) ||
                                skater4.equals(skater5)) {
                            JOptionPane.showMessageDialog(mainFrame, PLAYER_DUPLICATE, ENTER_STATS_LIVE,
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        skater1.scoredAgainst();
                        skater2.scoredAgainst();
                        skater3.scoredAgainst();
                        skater4.scoredAgainst();
                        skater5.scoredAgainst();
                        scoredOnWindow.dispose();
                    });

                    scoredOnWindow.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            playerScoreLabel.setText(SELECT_SCORER);
                            assistPlayerPanel1.add(assist1Check);
                            assistPlayerPanel2.add(assist2Check);
                            mainFrame.setVisible(true);
                        }
                    });

                    scoredOnWindow.pack();
                    scoredOnWindow.setVisible(true);
                }
            }
            if (goalieInNet != null) {
                goalieInNet.scoredOn();
            }
            opponentGoals.getAndIncrement();
            currentScore.setText(CURRENT_SCORE + teamGoals.get() + '-' + opponentGoals.get());
            mainFrame.repaint();
        });

        // Enter a save for the goalie
        shotAgainstOnGoalLive = new JButton("Save by your Goalie");
        createPanelForContainer(new JComponent[]{shotAgainstOnGoalLive}, liveStats);
        shotAgainstOnGoalLive.addActionListener(e -> {
            Goalie goalieInNet = (Goalie) selectGoaliesForStats.getSelectedItem();
            if (goalieInNet != null) {
                goalieInNet.save();
            } else {
                JOptionPane.showMessageDialog(mainFrame, SELECT, ENTER_STATS_LIVE, JOptionPane.ERROR_MESSAGE);
            }
        });

        // Enter a Face Off that occured
        faceOffLive = new JButton(FACE_OFF);
        createPanelForContainer(new JComponent[]{faceOffLive}, liveStats);
        /*
          Ask the user if their center won the face off or not, use the currently selected line to select which center
          should win the face off
         */
        faceOffLive.addActionListener(e -> {
            int input;
            do {
                input = JOptionPane.showConfirmDialog(mainFrame, "Did your center win this face off?",
                        ENTER_STATS_LIVE, JOptionPane.YES_NO_OPTION);
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
                    JOptionPane.showMessageDialog(mainFrame, NO_OPTIONS + "new line.", ENTER_STATS_LIVE,
                            JOptionPane.ERROR_MESSAGE);
                }
            } else if (penaltyLive.isSelected()) {
                PKLine line = (PKLine) pkOptions.getSelectedItem();
                if (line != null) {
                    Skater[] skaters = line.getSkaters();
                    // User needs to choose from the two centers on the line
                    if (skaters[0] instanceof Center c1 && skaters[1] instanceof Center c2) {
                        int center;
                        do {
                            center = JOptionPane.showOptionDialog(mainFrame, "Please select the player who " +
                                            "took the face off.", ENTER_STATS_LIVE, JOptionPane.DEFAULT_OPTION,
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
                    } else if (skaters[0] instanceof Center c) {  // Only one center on the line
                        c.winFaceOff();
                    } else if (skaters[1] instanceof Center c) {  // Only one center on the line
                        c.winFaceOff();
                    } else {  // No centers on the line
                        JOptionPane.showMessageDialog(mainFrame, "This line does not have a center that can " +
                                        "save their face off win rate.\nPlease select another line or change the " +
                                        "position of one of these players to a center.",
                                ENTER_STATS_LIVE, JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(mainFrame, NO_OPTIONS + "new line", ENTER_STATS_LIVE,
                            JOptionPane.ERROR_MESSAGE);
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
                    JOptionPane.showMessageDialog(mainFrame, NO_OPTIONS + "new line.", ENTER_STATS_LIVE,
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Select skater who blocked a shot / made a body check
        shotBlockLive = new JButton(SHOT_BLOCK);
        hitLive = new JButton(HIT);
        createPanelForContainer(new JComponent[]{shotBlockLive, hitLive}, liveStats);
        /*
          User selects a defenseman who blocked a shot in a new window
          Window is initialized with just the defenseman of the currently selected line, but user can choose to select
          from any defenseman
         */
        shotBlockLive.addActionListener(e -> {
            // Clears the selection in create line tab and removes any selected components
            // Select LD Panel may be used so this ensures that create line tab does not appear to miss components
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

            Line currentLine;
            if (powerPlayLive.isSelected()) {
                currentLine = (Line) ppOptions.getSelectedItem();
            } else if (penaltyLive.isSelected()) {
                currentLine = (Line) pkOptions.getSelectedItem();
            } else {
                currentLine = (Line) defenseLines.getSelectedItem();
            }
            if (currentLine == null) {
                JOptionPane.showMessageDialog(mainFrame, NO_OPTIONS + "new line.", ENTER_STATS_LIVE,
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            mainFrame.setVisible(false);
            JWindow shotBlockWindow = new JWindow();
            shotBlockWindow.setLocationRelativeTo(mainFrame);
            Container shotBlockContent = shotBlockWindow.getContentPane();
            shotBlockContent.setLayout(new BoxLayout(shotBlockContent, BoxLayout.Y_AXIS));

            JLabel selectBlockerLabel = new JLabel(SELECT_DE + ' ' + THAT_SHOT_BLOCK);
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

            JToggleButton selectOtherDefense = new JToggleButton(SELECT_OTHER_DE);
            createPanelForContainer(new JComponent[]{selectOtherDefense}, shotBlockContent);
            // Switch between selecting from just the current line or the entire team
            selectOtherDefense.addActionListener(e1 -> {
                if (selectOtherDefense.isSelected()) {
                    selectOtherDefense.setText("Select Defenseman from Current Line");
                    shotBlockContent.remove(selectBlockerPanel);
                    shotBlockContent.add(selectLDPanel, 0);
                    selectLDLabel.setText(SELECT_DE + ' ' + THAT_SHOT_BLOCK);
                } else {
                    selectOtherDefense.setText(SELECT_OTHER_DE);
                    shotBlockContent.remove(selectLDPanel);
                    shotBlockContent.add(selectBlockerPanel, 0);
                }
                shotBlockWindow.pack();
                shotBlockWindow.repaint();
            });

            JButton enterShotBlock = new JButton("Enter Shot Block");
            JButton cancel = new JButton(CANCEL);
            createPanelForContainer(new JComponent[]{enterShotBlock, cancel}, shotBlockContent);

            // Enter the shot block for the selected defenseman
            ActionListener enterShotBlockAction = e1 -> {
                if (e1.getActionCommand().equals(enterShotBlock.getActionCommand())) {
                    Defenseman blocker;
                    if (selectOtherDefense.isSelected()) {
                        blocker = (Defenseman) pickLeftDe.getSelectedItem();
                    } else {
                        blocker = (Defenseman) blockerOptions.getSelectedItem();
                    }
                    if (blocker == null) {
                        JOptionPane.showMessageDialog(mainFrame, SELECT, ENTER_STATS_LIVE, JOptionPane.ERROR_MESSAGE);
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
        /*
          New window opens up for user to select which skater made a body check
          Window is initialized with skaters from currently selected line(s), but can be switched by the user to select
          any skater on the team
         */
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
                JOptionPane.showMessageDialog(mainFrame, NO_OPTIONS + "new line.", ENTER_STATS_LIVE,
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            Skater[] skaters = currentLine.getSkaters();

            mainFrame.setVisible(false);
            JWindow hitWindow = new JWindow();
            hitWindow.setLocationRelativeTo(mainFrame);
            Container hitContent = hitWindow.getContentPane();
            hitContent.setLayout(new BoxLayout(hitContent, BoxLayout.Y_AXIS));

            JLabel selectHitterLabel = new JLabel(SELECT_SKATER_WHO_HIT);
            JComboBox<Skater> hitterOptions = new JComboBox<>(skaters);
            if (defenseLine != null) {
                skaters = defenseLine.getSkaters();
                hitterOptions.addItem(skaters[0]);
                hitterOptions.addItem(skaters[1]);
            }
            JPanel selectHitterPanel = createPanel(new JComponent[]{selectHitterLabel, hitterOptions});
            hitContent.add(selectHitterPanel);

            JToggleButton useOtherSkater = new JToggleButton(SELECT_OTHER_SKATER);
            createPanelForContainer(new JComponent[]{useOtherSkater}, hitContent);
            // Update selection combo box to select from any skater
            useOtherSkater.addActionListener(e1 -> {
                if (useOtherSkater.isSelected()) {
                    useOtherSkater.setText("Select Skater on Current Line");
                    hitContent.remove(selectHitterPanel);
                    hitContent.add(playerScorePanel, 0);
                    playerScoreLabel.setText(SELECT_SKATER_WHO_HIT);
                } else {
                    useOtherSkater.setText(SELECT_OTHER_SKATER);
                    hitContent.remove(playerScorePanel);
                    hitContent.add(selectHitterPanel, 0);
                }
                hitWindow.pack();
                hitWindow.repaint();
            });

            JButton enterHit = new JButton("Enter " + HIT);
            JButton cancel = new JButton(CANCEL);
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
                        JOptionPane.showMessageDialog(mainFrame, SELECT, ENTER_STATS_LIVE, JOptionPane.ERROR_MESSAGE);
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
                    playerScoreLabel.setText(SELECT_SCORER);
                }
            });

            hitWindow.pack();
            hitWindow.setVisible(true);
        });

        // Buttons to activate a special teams situation
        penaltyOptionsLive = new ButtonGroup();
        powerPlayLive = new JToggleButton("Power Play");
        penaltyLive = new JToggleButton("Penalty Kill");
        penaltyOptionsLive.add(powerPlayLive);
        penaltyOptionsLive.add(penaltyLive);
        penaltyLengthLabel = new JLabel(PENALTY_LENGTH);
        penaltyLengthField = new JTextField("2", ENTER_STAT_SIZE);
        createPanelForContainer(new JComponent[]{powerPlayLive, penaltyLive, penaltyLengthLabel, penaltyLengthField},
                liveStats);

        /*
          Activates special teams situations; removes and adds proper components for selecting lines to the tab as well
          as the "Penalty Expired" button. Prompts user to select guilty player if a penalty is selected
         */
        ActionListener penaltiesListener = e -> {
            if (ppOptions.getItemCount() == 0 || pkOptions.getItemCount() == 0) {
                JOptionPane.showMessageDialog(mainFrame, NO_OPTIONS +"power play line and a penalty kill line.",
                        ENTER_STATS_LIVE, JOptionPane.ERROR_MESSAGE);
                penaltyOptionsLive.clearSelection();
                return;
            }
            boolean penalty = e.getActionCommand().equals(penaltyLive.getActionCommand());
            if (penalty && scorerPlayerOptions.getItemCount() == 0) {
                JOptionPane.showMessageDialog(mainFrame, NO_OPTIONS + "new skater.", ENTER_STATS_LIVE,
                        JOptionPane.ERROR_MESSAGE);
                penaltyOptionsLive.clearSelection();
                return;
            }
            if (penalty) {  // Adds penalty minutes to player serving the penalty
                double penaltyLength;
                try {
                    penaltyLength = Double.parseDouble(penaltyLengthField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(mainFrame, NUMBER_ERROR, ENTER_STATS_LIVE, JOptionPane.ERROR_MESSAGE);
                    penaltyOptionsLive.clearSelection();
                    return;
                }
                if (penaltyLength < 0) {
                    JOptionPane.showMessageDialog(mainFrame, POS_PENALTY_LENGTH, ENTER_STATS_LIVE,
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                mainFrame.setVisible(false);
                // Window to select the player serving the penalty
                JWindow selectPlayer = new JWindow();
                Container selectPlayerContent = selectPlayer.getContentPane();
                selectPlayerContent.setLayout(new BoxLayout(selectPlayerContent, BoxLayout.Y_AXIS));
                selectPlayer.setLocationRelativeTo(mainFrame);
                playerScoreLabel.setText(SELECT_SERVING_PLAYER);
                selectPlayerContent.add(playerScorePanel);
                JButton confirmSelection = new JButton("Select This Player");
                selectPlayerContent.add(confirmSelection);
                selectPlayer.pack();
                selectPlayer.setVisible(true);
                confirmSelection.addActionListener(e1 -> {
                    Skater guiltyPlayer = (Skater) scorerPlayerOptions.getSelectedItem();
                    if (guiltyPlayer != null) {
                        guiltyPlayer.penalty(penaltyLength);
                    }
                    selectPlayer.dispose();
                    playerScoreLabel.setText(SELECT_SCORER);
                    mainFrame.setVisible(true);
                });
            }
            liveStats.remove(offenseLinePanel);
            liveStats.remove(defenseLinePanel);
            liveStats.remove(penaltyOverPanel);
            if (e.getActionCommand().equals(powerPlayLive.getActionCommand())) {
                liveStats.remove(pkLinePanel);
                liveStats.add(ppLinePanel, 1);
            } else if (penalty) {
                liveStats.remove(ppLinePanel);
                liveStats.add(pkLinePanel, 1);
            }
            liveStats.add(penaltyOverPanel, liveStats.getComponentCount() - 1);
            mainFrame.repaint();
        };

        powerPlayLive.addActionListener(penaltiesListener);
        penaltyLive.addActionListener(penaltiesListener);

        /*
          Updates stats properly for the selected special teams lines and resets the displayed components for selecting
          lines as well as removes the "Penalty Over" button
         */
        penaltyOver.addActionListener(e -> {
            if (powerPlayLive.isSelected()) {
                PPLine failedLine = (PPLine) ppOptions.getSelectedItem();
                if (failedLine != null) {
                    failedLine.failure();
                    liveStats.remove(ppLinePanel);
                } else {
                    JOptionPane.showMessageDialog(mainFrame, NO_OPTIONS + "power play line.", ENTER_STATS_LIVE,
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else if (penaltyLive.isSelected()) {
                PKLine successLine = (PKLine) pkOptions.getSelectedItem();
                if (successLine != null) {
                    successLine.success();
                    liveStats.remove(pkLinePanel);
                } else {
                    JOptionPane.showMessageDialog(mainFrame, NO_OPTIONS +"penalty kill line.", ENTER_STATS_LIVE,
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            liveStats.add(defenseLinePanel, 1);
            liveStats.add(offenseLinePanel, 1);
            liveStats.remove(penaltyOverPanel);
            penaltyOptionsLive.clearSelection();
            mainFrame.repaint();
        });

        // Resets team score, updates team record, saves and displays all changes to the GUI
        gameOver = new JButton("Game Over");
        createPanelForContainer(new JComponent[]{gameOver}, liveStats);
        gameOver.addActionListener(e -> {
            if (powerPlayLive.isSelected() || penaltyLive.isSelected()) {
                JOptionPane.showMessageDialog(mainFrame, "Please finish all power plays/penalty kills before" +
                        " finishing the game.", ENTER_STATS_LIVE, JOptionPane.ERROR_MESSAGE);
                return;
            }
            Goalie mainGoalie = (Goalie) selectGoaliesForStats.getSelectedItem();
            if (mainGoalie == null) {
                JOptionPane.showMessageDialog(mainFrame, SELECT_GOALIE_RECORD, ENTER_STATS_LIVE,
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (teamGoals.get() > opponentGoals.get()) {  // Win
                team.win();
                mainGoalie.win();
            } else if (teamGoals.get() == opponentGoals.get()) {  // Tie
                team.tie();
                mainGoalie.loseOT();
            } else if (teamGoals.get() == opponentGoals.get() - 1) {  // Possibly OT
                int input;
                do {
                    input = JOptionPane.showConfirmDialog(mainFrame, REG_LOSS, ENTER_STATS_LIVE,
                            JOptionPane.YES_NO_OPTION);
                } while (input != JOptionPane.YES_OPTION && input != JOptionPane.NO_OPTION);
                if (input == JOptionPane.NO_OPTION) {
                    team.tie();
                    mainGoalie.loseOT();
                } else {
                    team.lose();
                    mainGoalie.lose();
                }
            } else {  // Loss
                team.lose();
                mainGoalie.lose();
            }

            // Reset Goals and Update Elements
            teamGoals.set(0);
            opponentGoals.set(0);
            currentScore.setText(CURRENT_SCORE + "0-0");
            updateEntireTeamComponents();
            try {
                updateFile();
                JOptionPane.showMessageDialog(mainFrame, UPDATE_SUCCESS, ENTER_STATS_LIVE,
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(mainFrame, BIN_FILE_ERROR, ENTER_STATS_LIVE, JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), ENTER_STATS_LIVE, JOptionPane.ERROR_MESSAGE);
            }
        });

        JScrollPane enterLiveScroll = new JScrollPane(liveStats);
        enterStatsTabs.add("Enter Live", enterLiveScroll);

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

        postGameFaceOffLabel = new JLabel( "Enter Face Off Stats: (Wins - Losses)");
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

        postGamePowerPlay = new JSlider(0, PENALTY_MAX, 0);
        postGamePowerPlayLabel = new JLabel(ENTER_POWER_PLAY + postGamePowerPlay.getValue());
        createPanelForContainer(new JComponent[]{postGamePowerPlayLabel, postGamePowerPlay}, postGameStats);
        postGamePowerPlay.addChangeListener(e ->
                postGamePowerPlayLabel.setText(ENTER_POWER_PLAY + postGamePowerPlay.getValue()));

        postGamePenalties = new JSlider(0, PENALTY_MAX, 0);
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
                liveStats.add(selectGoaliePanel, 2);
                defenseLinePanel.remove(defenseLinePanel.getComponentCount() - 1);
                defenseLinePanel.add(defenseLines);
                pkLinePanel.remove(pkLinePanel.getComponentCount() - 1);
                pkLinePanel.add(pkOptions);
                liveStats.add(defenseLinePanel, 2);
                useLinesOrPlayers.removeAll();
                try {
                    updateFile();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(mainFrame, BIN_FILE_ERROR, ENTER_STATS_AFTER,
                            JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), ENTER_STATS_AFTER,
                            JOptionPane.ERROR_MESSAGE);
                }
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                if (goalieWindow == null) {  // Checks that window is not already open
                    if (team.getSkaters().length < 5 || team.getGoalies().length == 0 || pickLeftDe.getItemCount() < 2
                            || centerOptions.getItemCount() < 2) {
                        JOptionPane.showMessageDialog(mainFrame, ENTER_AFTER_REQS, ENTER_STATS_AFTER,
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (powerPlayLive.isSelected() || penaltyLive.isSelected()) {
                        JOptionPane.showMessageDialog(mainFrame, ACTIVE_SP_TEAMS, ENTER_STATS_AFTER,
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    /*
                      Stores the inputs from the JTextFields (score and face off totals)
                      Index 0 - team goals scored
                      Index 1 - opponent goals scored
                      Index 2 - team face off wins
                      Index 3 - team face off losses
                     */
                    int[] enterAfterNumInputs;

                    int shotsAgainst = postGameShotsAgainst.getValue();
                    int shotsBlocked = postGameShotsBlocked.getValue();
                    int hits = postGameHits.getValue();
                    AtomicInteger powerPlays = new AtomicInteger(postGamePowerPlay.getValue());
                    int penalties = postGamePenalties.getValue();
                    if ((powerPlays.get() > 0 && ppOptions.getItemCount() == 0) ||
                            (penalties > 0 && pkOptions.getItemCount() == 0)) {
                        JOptionPane.showMessageDialog(mainFrame, SPECIAL_TEAM_REQ, ENTER_STATS_AFTER,
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    try {
                        enterAfterNumInputs = checkForIntResponses(new String[]{finalScoreTeam.getText(),
                                finalScoreOpp.getText(), postGameFaceOffWins.getText(),
                                postGameFaceOffLosses.getText()});
                        if (enterAfterNumInputs[0] < 0 || enterAfterNumInputs[1] < 0 || enterAfterNumInputs[2] < 0
                                || enterAfterNumInputs[3] < 0) {
                            throw new NumberFormatException();
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(mainFrame, NUMBER_ERROR, ENTER_STATS_AFTER,
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (enterAfterNumInputs[1] > shotsAgainst) {
                        JOptionPane.showMessageDialog(mainFrame, "Opponent's score cannot be greater than " +
                                "the number of shots against your goalie.", ENTER_STATS_AFTER,
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    // Clears the selection in create line tab and removes any selected components
                    // Some components may be used in this process which is why the other tab must be reset
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

                    // Check if multiple goalies played in the game
                    int multipleGoalies;
                    do {
                        multipleGoalies = JOptionPane.showConfirmDialog(mainFrame, "Did multiple goalies " +
                                "play in this game?", ENTER_STATS_AFTER, JOptionPane.YES_NO_OPTION);
                    } while (multipleGoalies != JOptionPane.YES_OPTION && multipleGoalies != JOptionPane.NO_OPTION);
                    int finalInput = multipleGoalies;

                    Container enterGoalieContent = goalieWindow.getContentPane();
                    enterGoalieContent.setLayout(new BoxLayout(enterGoalieContent, BoxLayout.Y_AXIS));

                    enterGoalieContent.add(selectGoaliePanel);

                    // Components for if multiples goalies need to play
                    JSlider goalsAgainstSlider = new JSlider(0, enterAfterNumInputs[1]);
                    JSlider shotsAgainstSlider = new JSlider(1, shotsAgainst);
                    ArrayList<Goalie> selectedGoalies = new ArrayList<>();
                    if (finalInput == JOptionPane.YES_OPTION) {
                        JLabel goalsAgainstLabel = new JLabel(GOAL_AGAINST_GOALIE + goalsAgainstSlider.getValue());
                        goalsAgainstSlider.addChangeListener(e1 -> {
                            goalsAgainstLabel.setText(GOAL_AGAINST_GOALIE + goalsAgainstSlider.getValue());
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

                    /*
                      For a single goalie: updates stats and records for the single goalie and progresses to next window
                      For multiple goalies: updates stats for currently selected goalie, if stats are finished, has user
                      select which goalie should have this game on their record, and updates the goalie's record, and
                      progresses to the next window
                     */
                    enterGoalie.addActionListener(e1 -> {
                        Goalie selectedGoalie;
                        if (finalInput == JOptionPane.YES_OPTION) {
                            selectedGoalie = (Goalie) selectGoaliesForStats.getSelectedItem();

                            int shotsAgainstOneGoalie = shotsAgainstSlider.getValue();
                            int goalsAgainstOneGoalie = goalsAgainstSlider.getValue();
                            if (selectedGoalie != null) {
                                selectedGoalie.enterSaves(goalsAgainstOneGoalie, shotsAgainstOneGoalie);
                                if (!selectedGoalies.contains(selectedGoalie)) {
                                    selectedGoalies.add(selectedGoalie);
                                }
                            } else {
                                JOptionPane.showMessageDialog(mainFrame, UNEXPECTED_ERROR +
                                                "enterGoalie.addActionListener", ENTER_STATS_AFTER,
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            goalsAgainstSlider.setMaximum(goalsAgainstSlider.getMaximum() - goalsAgainstOneGoalie);
                            shotsAgainstSlider.setMaximum(shotsAgainstSlider.getMaximum() - shotsAgainstOneGoalie);

                            if (goalsAgainstSlider.getMaximum() == 0 && shotsAgainstSlider.getMaximum() == 0) {
                                if (selectedGoalies.isEmpty()) {
                                    JOptionPane.showMessageDialog(goalieWindow, "At least one goalie must be " +
                                            "in net for the game. Please retry and use a goalie for at least one goal" +
                                            " or shot.", ENTER_STATS_AFTER, JOptionPane.ERROR_MESSAGE);
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
                                    int index = JOptionPane.showOptionDialog(goalieWindow, SELECT_GOALIE_RECORD,
                                            ENTER_STATS_AFTER, JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                                            null, selectedGoalies.toArray(), selectedGoalies.get(0));
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
                                JOptionPane.showMessageDialog(mainFrame, SELECT, ENTER_STATS_AFTER,
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            selectedGoalie.enterSaves(enterAfterNumInputs[1], shotsAgainst);
                        }

                        if (enterAfterNumInputs[0] > enterAfterNumInputs[1]) {
                            team.win();
                            if (enterAfterNumInputs[1] == 0) {
                                selectedGoalie.shutoutWin();
                            } else {
                                selectedGoalie.win();
                            }
                        } else if (enterAfterNumInputs[0] == enterAfterNumInputs[1]) {
                            team.tie();
                            selectedGoalie.loseOT();
                        } else if (enterAfterNumInputs[0] == enterAfterNumInputs[1] - 1) {
                            int input;
                            while (true) {
                                input = JOptionPane.showConfirmDialog(mainFrame, REG_LOSS, ENTER_STATS_AFTER,
                                        JOptionPane.YES_NO_OPTION);
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
                        if (enterAfterNumInputs[0] > 0) {
                            scoreTeamGoals.setVisible(true);
                        } else if (enterAfterNumInputs[2] > 0 || enterAfterNumInputs[3] > 0) {
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
                    if (enterAfterNumInputs[0] > 0) {  // Enter Goals
                        AtomicInteger enteredGoalsScored = new AtomicInteger();
                        AtomicInteger enteredGoalsAgainst = new AtomicInteger();

                        Container scoreGoalsContent = scoreTeamGoals.getContentPane();
                        scoreGoalsContent.setLayout(new BoxLayout(scoreGoalsContent, BoxLayout.Y_AXIS));

                        scoreGoalsContent.add(linesOrPlayersPanel);
                        useLinesOrPlayers.setSelected(false);
                        useLinesOrPlayers.setText(USE_PLAYERS);

                        JLabel otherLineLabel = new JLabel("Line on Ice:");
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

                        /*
                          Updates currently displayed components. Updates line roster for selected line, and adds
                          defense line roster when necessary
                         */
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
                        ActionListener switchLinesAndPlayersAfter = e1 -> {
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
                        };
                        useLinesOrPlayers.addActionListener(switchLinesAndPlayersAfter);

                        JButton enterTeamGoal = new JButton(TEAM_GOAL);
                        JButton enterOpponentGoal = new JButton(OPPONENT_GOAL);
                        JPanel scoreButtonsPanel = createPanel(new JComponent[]{enterTeamGoal, enterOpponentGoal});
                        scoreGoalsContent.add(scoreButtonsPanel);

                        /*
                          Ensures that players/lines have properly been selected, enters the stats for the relevant
                          selections, updates the totals, and if the entered amount has reached the number of goals,
                          progresses to the next window
                         */
                        ActionListener enterGoalsListener = e1 -> {
                            if (useLinesOrPlayers.isSelected()) {  // Using players
                                Skater scorer = (Skater) scorerPlayerOptions.getSelectedItem();
                                Skater assist1 = (Skater) assistPlayerOptions1.getSelectedItem();
                                Skater assist2 = (Skater) assistPlayerOptions2.getSelectedItem();
                                Skater onIce1 = (Skater) otherPlayerOptions1.getSelectedItem();
                                Skater onIce2 = (Skater) otherPlayerOptions2.getSelectedItem();

                                if (scorer == null || assist1 == null || assist2 == null || onIce1 == null ||
                                        onIce2 == null) {  // Player has not been selected
                                    JOptionPane.showMessageDialog(scoreTeamGoals, UNEXPECTED_ERROR +
                                                    "enterGoalsListener", ENTER_STATS_AFTER, JOptionPane.ERROR_MESSAGE);
                                    return;
                                }

                                if (scorer.equals(assist1) || scorer.equals(assist2) || scorer.equals(onIce1) ||
                                        scorer.equals(onIce2) || assist1.equals(assist2) || assist1.equals(onIce1)
                                        || assist1.equals(onIce2) || assist2.equals(onIce1) ||
                                        assist2.equals(onIce2) || onIce1.equals(onIce2)) {  // Duplicate players
                                    JOptionPane.showMessageDialog(scoreTeamGoals, PLAYER_DUPLICATE, ENTER_STATS_AFTER,
                                            JOptionPane.ERROR_MESSAGE);
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

                            } else {  // Using lines
                                Position scorer = (Position) scorerOptions.getSelectedItem();
                                Position assist1 = (Position) assistOptions1.getSelectedItem();
                                Position assist2 = (Position) assistOptions2.getSelectedItem();

                                if (assist1 == null && assist2 != null) {  // Wrong assist selected
                                    JOptionPane.showMessageDialog(scoreTeamGoals, USE_ASSIST_1, ENTER_STATS_AFTER,
                                            JOptionPane.ERROR_MESSAGE);
                                    return;
                                }

                                Line selection = (Line) nonDefenseLines.getSelectedItem();
                                try {
                                    if (selection instanceof OffenseLine oLine) {
                                        DefenseLine dLine = (DefenseLine) defenseLines.getSelectedItem();
                                        if (dLine == null) {
                                            JOptionPane.showMessageDialog(scoreTeamGoals, UNEXPECTED_ERROR +
                                                            "enterGoalsListener", ENTER_STATS_AFTER,
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
                                        JOptionPane.showMessageDialog(scoreTeamGoals,
                                                NO_OPTIONS.substring(0, NO_OPTIONS.indexOf('.') + 1) +
                                                        "Please select players manually instead.", ENTER_STATS_AFTER,
                                                JOptionPane.ERROR_MESSAGE);
                                        return;
                                    }
                                } catch (NullPointerException | IllegalArgumentException ex) {
                                    JOptionPane.showMessageDialog(scoreTeamGoals, ex.getMessage(), ENTER_STATS_AFTER,
                                            JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                            }
                            if (e1.getActionCommand().equals(enterTeamGoal.getActionCommand())) {
                                JOptionPane.showMessageDialog(scoreTeamGoals, "Team Goal " +
                                        enteredGoalsScored.incrementAndGet() + " successfully entered",
                                        ENTER_STATS_AFTER, JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(scoreTeamGoals, "Opponent Goal " +
                                        enteredGoalsAgainst.incrementAndGet() + " successfully entered",
                                        ENTER_STATS_AFTER, JOptionPane.INFORMATION_MESSAGE);
                            }
                            if (enteredGoalsScored.get() == enterAfterNumInputs[0]) {  // Done entering team goals
                                scoreButtonsPanel.remove(enterTeamGoal);
                                scoreTeamGoals.repaint();
                                if (enteredGoalsAgainst.get() == enterAfterNumInputs[1]) {  // Done entering opp goals
                                    scoreTeamGoals.dispose();
                                    useLinesOrPlayers.removeActionListener(switchLinesAndPlayersAfter);
                                    if (ppGoals.get() >= powerPlays.get()) {
                                        enterTeamPPs.dispose();
                                        powerPlays.set(0);
                                    }
                                    if (enterAfterNumInputs[2] > 0 || enterAfterNumInputs[3] > 0) {
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
                            if (enteredGoalsAgainst.get() == enterAfterNumInputs[1]) {  // Done entering opp goals
                                scoreButtonsPanel.remove(enterOpponentGoal);
                                scoreTeamGoals.repaint();
                                useLinesOrPlayers.removeActionListener(switchLinesAndPlayersAfter);
                                if (enteredGoalsScored.get() == enterAfterNumInputs[0]) {  // Done entering team goals
                                    scoreTeamGoals.dispose();
                                    if (ppGoals.get() >= powerPlays.get()) {
                                        enterTeamPPs.dispose();
                                        powerPlays.set(0);
                                    }
                                    if (enterAfterNumInputs[2] > 0 || enterAfterNumInputs[3] > 0) {
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
                    if (enterAfterNumInputs[2] > 0 || enterAfterNumInputs[3] > 0) {
                        Container faceOffContent = enterTeamFaceOffs.getContentPane();
                        faceOffContent.setLayout(new BoxLayout(faceOffContent, BoxLayout.Y_AXIS));
                        faceOffContent.add(selectCenterPanel);
                        JSlider winsSlider = new JSlider(0, enterAfterNumInputs[2]);
                        JSlider lossesSlider = new JSlider(0, enterAfterNumInputs[3]);
                        JLabel winsLabel = new JLabel(FACE_OFF_WIN + winsSlider.getValue());
                        JLabel lossesLabel = new JLabel(FACE_OFF_LOSS + lossesSlider.getValue());
                        winsSlider.addChangeListener(e1 -> {
                            winsLabel.setText(FACE_OFF_WIN + winsSlider.getValue());
                            enterTeamFaceOffs.pack();
                        });
                        lossesSlider.addChangeListener(e1 -> {
                            lossesLabel.setText(FACE_OFF_LOSS + lossesSlider.getValue());
                            enterTeamFaceOffs.pack();
                        });
                        createPanelForContainer(new JComponent[]{winsLabel, winsSlider, lossesLabel, lossesSlider},
                                faceOffContent);

                        JButton enterFaceOffsButton = new JButton("Enter " + FACE_OFF + 's');
                        createPanelForContainer(new JComponent[]{enterFaceOffsButton}, faceOffContent);
                        /*
                          Updates stats for the current center, if the entered totals are finished, progresses to the
                          next window
                         */
                        enterFaceOffsButton.addActionListener(e1 -> {
                            Center selectedCenter = (Center) centerOptions.getSelectedItem();
                            if (selectedCenter == null) {
                                JOptionPane.showMessageDialog(enterTeamFaceOffs, SELECT, ENTER_STATS_AFTER,
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            int wins = winsSlider.getValue();
                            int losses = lossesSlider.getValue();
                            selectedCenter.enterFaceOffsPostGame(wins, losses);

                            winsSlider.setMaximum(winsSlider.getMaximum() - wins);
                            lossesSlider.setMaximum(lossesSlider.getMaximum() - losses);

                            JOptionPane.showMessageDialog(enterTeamFaceOffs, FACE_OFF +"s Successfully Updated",
                                    ENTER_STATS_AFTER, JOptionPane.INFORMATION_MESSAGE);

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
                        selectLDLabel.setText(SELECT_DE);

                        JSlider shotsBlockedSlider = new JSlider(1, shotsBlocked);
                        JLabel shotsBlockedLabel = new JLabel(SHOT_BLOCK_STRING + " " +
                                shotsBlockedSlider.getValue());
                        shotsBlockedSlider.addChangeListener(e1 -> {
                            shotsBlockedLabel.setText(SHOT_BLOCK_STRING + " " + shotsBlockedSlider.getValue());
                            enterTeamShotBlocks.pack();
                        });
                        createPanelForContainer(new JComponent[]{shotsBlockedLabel, shotsBlockedSlider},
                                enterShotsBlockedContent);

                        JButton shotsBlockedButton = new JButton("Enter " + SHOT_BLOCK + 's');
                        createPanelForContainer(new JComponent[]{shotsBlockedButton}, enterShotsBlockedContent);

                        /*
                          Updates shot blocks for the currently selected defenseman, once the shot block total has been
                          reached, progresses to the next window
                         */
                        shotsBlockedButton.addActionListener(e1 -> {
                            Defenseman selectedDe = (Defenseman) pickLeftDe.getSelectedItem();
                            if (selectedDe == null) {
                                JOptionPane.showMessageDialog(enterTeamShotBlocks, SELECT, ENTER_STATS_AFTER,
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            int addShots = shotsBlockedSlider.getValue();
                            selectedDe.addShotsBlocked(addShots);
                            shotsBlockedSlider.setMaximum(shotsBlockedSlider.getMaximum() - addShots);

                            JOptionPane.showMessageDialog(enterTeamShotBlocks, "Shots Blocked Successfully " +
                                            "Updated", ENTER_STATS_AFTER, JOptionPane.INFORMATION_MESSAGE);
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

                        JButton hitsButton = new JButton("Enter " + HIT + 's');
                        createPanelForContainer(new JComponent[]{hitsButton}, enterHitsContent);
                        /*
                          Enters the hits for the selected skaters, once the total has been reached, progresses to the
                          next window
                         */
                        hitsButton.addActionListener(e1 -> {
                            Skater selectedSkater = (Skater) pickLeftWing.getSelectedItem();
                            if (selectedSkater == null) {
                                JOptionPane.showMessageDialog(enterTeamHits, SELECT, ENTER_STATS_AFTER,
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            int addHits = hitsSlider.getValue();
                            selectedSkater.addHits(addHits);
                            hitsSlider.setMaximum(hitsSlider.getMaximum() - addHits);
                            JOptionPane.showMessageDialog(enterTeamHits, HIT + "s Successfully Updated",
                                    ENTER_STATS_AFTER, JOptionPane.INFORMATION_MESSAGE);

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

                        // Slider is set to entered value subtracted with number of power play goals scored
                        JSlider ppSlider = new JSlider(1, powerPlays.get() - ppGoals.get());
                        JLabel ppSliderLabel = new JLabel(SELECT_EXPIRED_PP + ppSlider.getValue());
                        createPanelForContainer(new JComponent[]{ppSliderLabel, ppSlider}, enterPPContent);
                        ppSlider.addChangeListener(e1 -> {
                            ppSliderLabel.setText(SELECT_EXPIRED_PP + ppSlider.getValue());
                            enterTeamPPs.pack();
                        });

                        JButton enterPPs = new JButton("Enter Power Plays");
                        createPanelForContainer(new JComponent[]{enterPPs}, enterPPContent);

                        /*
                          Failure is updated for selected line, total is updated, once the needed amount has been
                          entered, progresses to the next window
                         */
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
                                    ENTER_STATS_AFTER, JOptionPane.INFORMATION_MESSAGE);
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
                        AtomicInteger enteredPenalties = new AtomicInteger();
                        Container enterPKContent = enterTeamPKs.getContentPane();
                        enterPKContent.setLayout(new BoxLayout(enterPKContent, BoxLayout.Y_AXIS));

                        selectRWLabel.setText(SELECT_SERVING_PLAYER);
                        enterPKContent.add(selectRWPanel);

                        JLabel timeLabel = new JLabel(PENALTY_LENGTH);
                        JTextField enterTimeLength = new JTextField(ENTER_STAT_SIZE);
                        enterTimeLength.setEditable(true);
                        createPanelForContainer(new JComponent[]{timeLabel, enterTimeLength}, enterPKContent);

                        JCheckBox checkSuccess = new JCheckBox("Penalty Killed Successfully");
                        pkLinePanel.add(checkSuccess);
                        enterPKContent.add(pkLinePanel);

                        JButton enterPenalty = new JButton("Enter Penalty");
                        createPanelForContainer(new JComponent[]{enterPenalty}, enterPKContent);

                        /*
                          Enters penalty minutes for the guilty players, enters success/failure for pk lines, and once
                          the necessary penalties have been entered, closes the window
                         */
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
                                JOptionPane.showMessageDialog(enterTeamPKs, SELECT, ENTER_STATS_AFTER,
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            try {
                                penaltyLength = Double.parseDouble(enterTimeLength.getText());
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(enterTeamPKs, NUMBER_ERROR, ENTER_STATS_AFTER,
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            if (penaltyLength <= 0) {
                                JOptionPane.showMessageDialog(enterTeamPKs, POS_PENALTY_LENGTH, ENTER_STATS_AFTER,
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            guiltyPlayer.penalty(penaltyLength);
                            if (checkSuccess.isSelected()) {
                                pkLine.success();
                            }
                            JOptionPane.showMessageDialog(enterTeamPKs, "Penalty " +
                                    enteredPenalties.incrementAndGet() + " Entered Successfullly", ENTER_STATS_AFTER,
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
            JOptionPane.showMessageDialog(null, BIN_FILE_ERROR, INIT, JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(null, e.getStackTrace(), INIT, JOptionPane.ERROR_MESSAGE);
            return;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), INIT, JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Setting up frame
        selectFrame = new JFrame("Welcome to Hockey Team Manager");
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
        createPanelForContainer(new JComponent[]{otherOptionsLabel}, selectContent);
        createTeam = new JButton(CREATE_TEAM);
        deleteTeam = new JButton(DELETE_TEAM);
        restoreSample = new JButton(RESTORE_SAMPLE);
        createPanelForContainer(new JComponent[]{createTeam, deleteTeam, restoreSample}, selectContent);

        createTextFile = new JButton(CREATE_FILE);
        importTextFile = new JButton(IMPORT_FILE);
        createPanelForContainer(new JComponent[]{createTextFile, importTextFile}, selectContent);
        createTextFile.addActionListener(e -> {
            if (teams.size() == 0) {
                JOptionPane.showMessageDialog(selectFrame, NO_OPTIONS + "team to save to a file.", IMPORT_FILE,
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            selectFrame.setVisible(false);

            JFrame createFileFrame = new JFrame(CREATE_FILE);
            createFileFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            createFileFrame.setLocationRelativeTo(selectFrame);
            Container createFileContent = createFileFrame.getContentPane();
            createFileContent.setLayout(new BoxLayout(createFileContent, BoxLayout.Y_AXIS));

            JList<Team> teamList = new JList<>(teams.toArray(new Team[]{}));
            teamList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            createPanelForContainer(new JComponent[]{teamList}, createFileContent);
            JLabel createFileLabel = new JLabel(CREATE_FILE_INSTRUCTIONS);
            createPanelForContainer(new JComponent[]{createFileLabel}, createFileContent);
            JTextField fileName = new JTextField(ENTER_NAME_SIZE);
            createPanelForContainer(new JComponent[]{fileName}, createFileContent);

            JButton createFile = new JButton("Create File");
            ActionListener createFileAction = e1 -> {
                Team[] savedTeams = teamList.getSelectedValuesList().toArray(new Team[]{});
                if (savedTeams.length == 0) {
                    JOptionPane.showMessageDialog(createFileFrame, "Please select a team to save to the" +
                            " file", CREATE_FILE, JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    File file = new File(fileName.getText());
                    if (!file.createNewFile()) {
                        JOptionPane.showMessageDialog(createFileFrame, NEW_FILE_ERR, CREATE_FILE,
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    PrintWriter pw = new PrintWriter(new FileOutputStream(file));
                    pw.println(savedTeams.length);
                    pw.flush();
                    for (Team team: savedTeams) {
                        pw.println(team.writeToFile());
                        pw.flush();
                    }
                    JOptionPane.showMessageDialog(createFileFrame, CREATE_SUCCESS + "file and saved team" +
                            " data.", CREATE_FILE, JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(createFileFrame, TEXT_FILE_ERROR, CREATE_FILE,
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                createFileFrame.dispose();
            };
            createFile.addActionListener(createFileAction);
            createPanelForContainer(new JComponent[]{createFile}, createFileContent);

            createFileFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    selectFrame.setVisible(true);
                }

                @Override
                public void windowClosed(WindowEvent e) {
                    selectFrame.setVisible(true);
                }
            });

            createFileFrame.pack();
            createFileFrame.setVisible(true);
        });
        importTextFile.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Select Formatted File Storing TeamManager Data");
            int input = fc.showOpenDialog(selectFrame);
            if (input == JFileChooser.APPROVE_OPTION) {
                File f = fc.getSelectedFile();
                ArrayList<Team> duplicateTeams;
                try {
                    duplicateTeams = importFile(f);
                } catch (FormatException ex) {
                    JOptionPane.showMessageDialog(selectFrame, ex.getMessage(), IMPORT_FILE, JOptionPane.ERROR_MESSAGE);
                    return;
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(selectFrame, TEXT_FILE_ERROR, IMPORT_FILE, JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!duplicateTeams.isEmpty()) {
                    selectFrame.setVisible(false);
                    JWindow duplicatesWindow = new JWindow();
                    duplicatesWindow.setLocationRelativeTo(selectFrame);
                    Container duplicateContent = duplicatesWindow.getContentPane();
                    duplicateContent.setLayout(new BoxLayout(duplicateContent, BoxLayout.Y_AXIS));
                    JList<Team> duplicateList = new JList<>(duplicateTeams.toArray(new Team[]{}));
                    duplicateList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                    createPanelForContainer(new JComponent[]{duplicateList}, duplicateContent);
                    JLabel duplicatesInstructions = new JLabel(DUPLICATES_INSTRUCTIONS);
                    createPanelForContainer(new JComponent[]{duplicatesInstructions}, duplicateContent);
                    JButton confirm = new JButton("Select These Teams");
                    createPanelForContainer(new JComponent[]{confirm}, duplicateContent);
                    confirm.addActionListener(e1 -> {
                        Team[] selections = duplicateList.getSelectedValuesList().toArray(new Team[]{});
                        if (selections.length > 0) {
                            for (Team newTeam: selections) {
                                int index = changeTeam(newTeam, newTeam);
                                teamSelection.removeItemAt(index);
                                teamSelection.insertItemAt(newTeam, index);
                            }
                        }
                        duplicatesWindow.dispose();
                    });

                    duplicatesWindow.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            selectFrame.setVisible(true);
                            try {
                                updateFile();
                            } catch (IOException ex) {
                                JOptionPane.showMessageDialog(selectFrame, BIN_FILE_ERROR, IMPORT_FILE,
                                        JOptionPane.ERROR_MESSAGE);
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(selectFrame, ex.getMessage(), IMPORT_FILE,
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    });

                    duplicatesWindow.pack();
                    duplicatesWindow.setVisible(true);
                } else {
                    try {
                        updateFile();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(selectFrame, BIN_FILE_ERROR, IMPORT_FILE,
                                JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(selectFrame, ex.getMessage(), IMPORT_FILE,
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Bottom of Frame, button where new users can find additional guidance
        newUsers = new JButton("Help and Guidance for New Users");
        closeApp = new JButton("Close Application");
        createPanelForContainer(new JComponent[]{newUsers, closeApp}, selectContent);

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
            int input = JOptionPane.showConfirmDialog(selectFrame, "Would you like to provide a win/loss " +
                    "record for this team?", CREATE_TEAM, JOptionPane.YES_NO_CANCEL_OPTION);
            switch (input) {
                case JOptionPane.YES_OPTION -> { // Prompt for wins, losses, ot losses
                    try {
                        // Entering information for new team
                        String name = JOptionPane.showInputDialog(selectFrame, NAME_STRING, CREATE_TEAM,
                                JOptionPane.QUESTION_MESSAGE);
                        if (name == null || name.isBlank()) {
                            return;
                        }
                        int wins = Integer.parseInt(JOptionPane.showInputDialog(selectFrame, WINS_STRING,
                                CREATE_TEAM, JOptionPane.QUESTION_MESSAGE));
                        int losses = Integer.parseInt(JOptionPane.showInputDialog(selectFrame, LOSSES_STRING,
                                CREATE_TEAM, JOptionPane.QUESTION_MESSAGE));
                        int otLosses = Integer.parseInt(JOptionPane.showInputDialog(selectFrame, OT_STRING,
                                CREATE_TEAM, JOptionPane.QUESTION_MESSAGE));
                        Team newTeam = new Team(name, wins, losses, otLosses);

                        int index = addTeam(newTeam);
                        if (index == -1) {
                            JOptionPane.showMessageDialog(selectFrame, "Two teams" + UNIQUE_NAME,
                                    CREATE_TEAM, JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        teamSelection.insertItemAt(newTeam, index);
                        updateFile();

                        // Set selected team and hide frame
                        team = newTeam;
                        selectFrame.setVisible(false);
                        displayTeamGUI();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(selectFrame, NUMBER_ERROR, CREATE_TEAM,
                                JOptionPane.ERROR_MESSAGE);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(selectFrame, BIN_FILE_ERROR, CREATE_TEAM,
                                JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(selectFrame, ex.getMessage(), CREATE_TEAM,
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
                case JOptionPane.NO_OPTION -> {  // Similar to yes, but only prompt for name
                    try {
                        String name = JOptionPane.showInputDialog(selectFrame, NAME_STRING, CREATE_TEAM,
                                JOptionPane.QUESTION_MESSAGE);
                        if (name == null) {
                            return;
                        }
                        Team newTeam = new Team(name);

                        int index = addTeam(newTeam);
                        if (index == -1) {
                            JOptionPane.showMessageDialog(selectFrame, "Two teams " + UNIQUE_NAME,
                                    CREATE_TEAM, JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        updateFile();

                        team = newTeam;
                        teamSelection.insertItemAt(team, index);
                        selectFrame.setVisible(false);
                        displayTeamGUI();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(selectFrame, BIN_FILE_ERROR, CREATE_TEAM,
                                JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(selectFrame, ex.getMessage(), CREATE_TEAM,
                                JOptionPane.ERROR_MESSAGE);
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
                JOptionPane.showMessageDialog(selectFrame, "There are no teams to delete.", DELETE_TEAM,
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            selectFrame.setVisible(false);

            // New Frame
            JFrame deleteFrame = new JFrame(DELETE_TEAM);
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
                    int finalCheck = JOptionPane.showConfirmDialog(selectFrame, CONFIRM_DELETE + t.getName() +
                                    "? This cannot be undone.", DELETE_TEAM, JOptionPane.YES_NO_OPTION);
                    if (finalCheck == JOptionPane.YES_OPTION) {
                        teams.remove(t);
                        teamSelection.removeItemAt(teamSelection.getSelectedIndex());
                        try {
                            updateFile();
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(selectFrame, BIN_FILE_ERROR, DELETE_TEAM,
                                    JOptionPane.ERROR_MESSAGE);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(selectFrame, ex.getMessage(), DELETE_TEAM,
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
                        RESTORE_SAMPLE, JOptionPane.ERROR_MESSAGE);
                return;
            }
            teamSelection.insertItemAt(sample, index);
            try {
                updateFile();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(selectFrame, BIN_FILE_ERROR, RESTORE_SAMPLE, JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(selectFrame, ex.getMessage(), RESTORE_SAMPLE, JOptionPane.ERROR_MESSAGE);
            }
        });

        newUsers.addActionListener(e -> {
            JOptionPane.showMessageDialog(selectFrame, NEW_INFO, NEW_USER_INFO, JOptionPane.INFORMATION_MESSAGE);
            JOptionPane.showMessageDialog(selectFrame, NEW_INFO_2, NEW_USER_INFO, JOptionPane.INFORMATION_MESSAGE);
        });

        closeApp.addActionListener(e -> selectFrame.dispose());
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
