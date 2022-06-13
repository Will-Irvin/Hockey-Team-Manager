import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class TeamGUI extends JComponent implements Runnable {
    private Team team;  // Team being manipulated by the GUI

    // JComponents

    JTabbedPane mainTabs;  // Tabs for each different menu of the program

    // Team Lists
    JComboBox<Line> lineOptions;
    JComboBox<Skater> skaterOptions;
    JComboBox<Center> centerOptions;
    JComboBox<Defenseman> defenseOptions;
    JComboBox<Goalie> goalieOptions;
    JComboBox<OffenseLine> offenseLineOptions;
    JComboBox<DefenseLine> defenseLineOptions;

    // Manage Team

    JTabbedPane teamTabs;

    // Edit Team
    JTextField changeTeamName;
    JTextField changeTeamWins;
    JTextField changeTeamLosses;
    JTextField changeTeamOT;

    // View Roster
    JTextArea viewRoster;

    // View Stats
    JTextArea viewTeamStats;

    // Reset Stats
    JLabel resetStatsWarning;
    JButton resetTeamStats;

    // Manage Lines

    JTabbedPane lineTabs;
    JComboBox<Skater> pickLeftWing;
    JComboBox<Skater> pickRightWing;
    JComboBox<Defenseman> pickLeftDe;
    JComboBox<Defenseman> pickRightDe;

    // Create Line
    JTextField lineName;
    JButton createLine;

    // Edit Line
    JTextField changeLineName;
    JButton changeLinePlayers;

    // Edit Special Teams
    JComboBox<Line> specialTeamsOptions;
    JButton changeSuccessPercent;

    // Delete Line
    JButton deleteLine;

    // View Lines
    JTextArea viewLines;

    // Create/Edit Skater/Goalie
    JTextArea playerName;
    JSlider playerNumber;

    // Manage Skaters

    JTabbedPane skaterTabs;

    // Create/Edit Skater
    JToggleButton stickHand;
    JComboBox<Position> positionOptions;
    JToggleButton assignSkaterStats;
    JTextField goals;
    JTextField assists;
    JTextField plusMinus;
    JTextField shotsBlocked;
    JTextField faceOffPercent;
    JTextField faceOffTotal;

    JButton createPlayer;
    JButton editPlayer;

    // Delete Player
    JButton deletePlayer;

    // View Stats
    JTextArea viewPlayerStats;

    // Reset Stats
    JButton resetPlayerStats;

    // Manage Goalies

    JTabbedPane goalieTabs;

    // Create/Edit Goalie
    JToggleButton assignGoalieStats;
    JTextField savePercentage;
    JTextField goalieShotsAgainst;
    JTextField goalieWins;
    JTextField goalieLosses;
    JTextField goalieOTLosses;
    JSlider shutouts;

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
    JButton goal;
    JButton shotBlock;
    JButton faceOff;
    JButton shotAgainstOnGoal;
    JButton scoredAgainst;
    JButton specialTeamsExpired;
    JButton gameOver;

    // Enter Post Game
    JTextField finalScore;
    JSlider shotsBlockedByDe;
    JSlider faceOffs;
    JSlider shotsAgainstInGame;

    @Override
    public void run() {

    }
}
