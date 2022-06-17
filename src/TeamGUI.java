import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

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
    private static final String fileName = "TeamManagerData";  // Designated name for file storing data
    private static final String newInfo = "Thank you for using Team Manager! Here are a few things to note as you get" +
            " started:\n" +
            "If this is your first time launching the application you will notice that the app comes preloaded with" +
            " a sample team. This is included as a way to ease yourself into the application.\n" +
            "Feel free to view or change it in any way you like, and use it to take some time to get used to how the " +
            "application works.\n" +
            "All data related to the teams that you create in this application is stored in a file called \"" + fileName
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

    // MainGUI String Variables/Components
    // Reused String expressions
    private static final String nameString = "Enter name:";
    private static final String winsString = "Enter number of wins:";
    private static final String lossesString = "Enter number of losses:";
    private static final String otString = "Enter number of overtime losses or ties:";
    private static final String editInstructions = "<html>Enter changes for the information that you would like to " +
            "change.<br>If a field is left blank, no changes will be made for that information.</html>";
    private static final String numberError = "Please enter a number where prompted";
    private static final String fileError = "There was an issue writing to the file. Please close the application " +
            "and try again.";
    private static final String emptyInputs = "Please enter a value in at least one of the boxes";

    // JComponents

    JTabbedPane mainTabs;  // Tabs for each different menu of the program
    JLabel editInstructionsLabel;

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
    JTextArea viewRosterWithStats;

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
    JTextPane viewLines;

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
    JTextArea viewPlayerStats;

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
     * @return The generated sample team containing data from the 2015 Chicago Blackhawks Regular Season.
     */
    public static Team createSample() {
        Team blackhawks = new Team("Sample Team: Chicago Blackhawks - 2015 Regular Season", 47, 26,
                9);
        blackhawks.addPlayer(new Skater("Patrick Kane", 88, "Left", Position.RIGHT_WING,
                46, 60, 17, 37, 30));
        blackhawks.addPlayer(new Center("Artem Anisimov", 15, "Left", 20, 22,
                8, 53, 12, 44.16, 1148));
        blackhawks.addPlayer(new Skater("Kyle Baun", 39, "Right", Position.LEFT_WING,
                0, 0, 0, 3, 0));
        blackhawks.addPlayer(new Skater("Bryan Bickell", 29, "Left", Position.LEFT_WING,
                0, 2, -5, 58, 2));
        blackhawks.addGoalie(new Goalie("Corey Crawford", 50, .924, 1718,
                35, 18, 5, 7));
        blackhawks.addPlayer(new Defenseman("Trevor Daley", 6, "Left",
                Position.LEFT_DEFENSE, 0, 6, 1, 19, 8, 19));
        blackhawks.addPlayer(new Center("Phillip Danault", 24, "Left", 1, 4,
                -3, 33, 6, 44.2, 369));
        blackhawks.addPlayer(new Center("Marko Dano", 56, "Left", 1, 1,
                0, 15, 2, 25, 4));
        blackhawks.addGoalie(new Goalie("Scott Darling", 33, .915, 784, 12,
                8, 4, 1));
        blackhawks.addPlayer(new Skater("Andrew Desjardins", 11, "Left", Position.LEFT_WING,
                8, 5, -8, 30, 102));
        blackhawks.addPlayer(new Defenseman("Christian Ehrhoff", 55, "Left",
                Position.LEFT_DEFENSE, 0, 2, -1, 5, 2, 7));
        blackhawks.addPlayer(new Skater("Tomas Fleischmann", 12, "Left", Position.LEFT_WING,
                4, 1, -7, 10, 4));
        blackhawks.addPlayer(new Center("Ryan Garbutt", 28, "Left", 2, 4,
                -7, 94, 27, 0, 2));
        blackhawks.addPlayer(new Defenseman("Erik Gustafsson", 52, "Left",
                Position.LEFT_DEFENSE, 0, 14, 11, 29, 4, 37));
        blackhawks.addPlayer(new Skater("Ryan Hartman", 38, "Right", Position.RIGHT_WING,
                0, 1, -1, 5, 0));
        blackhawks.addPlayer(new Center("Vinnie Hinostroza", 48, "Right", 0, 0,
                -1, 13, 6, 40.6, 32));
        blackhawks.addPlayer(new Defenseman("Niklas Hjalmarsson", 4, "Left",
                Position.LEFT_DEFENSE, 2, 22, 13, 32, 32, 151));
        blackhawks.addPlayer(new Skater("Marian Hossa", 81, "Left", Position.RIGHT_WING,
                13, 20, 10, 37, 24));
        blackhawks.addPlayer(new Defenseman("Duncan Keith", 2, "Left",
                Position.LEFT_DEFENSE, 9, 34, 13, 16, 26, 116));
        blackhawks.addPlayer(new Skater("Tanner Kero", 67, "Left", Position.RIGHT_WING,
                1, 2, -2, 19, 2));
        blackhawks.addPlayer(new Center("Markus Kruger", 22, "Left", 0, 4,
                -5, 25, 24, 49.2, 674));
        blackhawks.addPlayer(new Skater("Andrew Ladd", 16, "Left", Position.LEFT_WING,
                8, 4, -3, 29, 6));
        blackhawks.addPlayer(new Skater("Brandon Mashinter", 53, "Left", Position.LEFT_WING,
                4, 1, -7, 62, 23));
        blackhawks.addPlayer(new Skater("Mark McNeill", 41, "Right", Position.RIGHT_WING,
                0, 0, 0, 3, 0));
        blackhawks.addPlayer(new Skater("Artemi Panarin", 72, "Right", Position.LEFT_WING,
                30, 47, 8, 46, 32));
        blackhawks.addPlayer(new Skater("Richard Panik", 14, "Left", Position.RIGHT_WING,
                6, 2, 4, 54, 6));
        blackhawks.addPlayer(new Center("Dennis Rasmussen", 70, "Right", 4, 5,
                9, 36, 4, 46.9, 207));
        blackhawks.addPlayer(new Defenseman("Michal Rozsival", 32, "Right",
                Position.RIGHT_DEFENSE, 1, 12, 3, 81, 33, 63));
        blackhawks.addPlayer(new Defenseman("David Rundblad", 5, "Right",
                Position.RIGHT_DEFENSE, 0, 2, -2, 3, 6, 3));
        blackhawks.addPlayer(new Defenseman("Rob Scuderi", 47, "Left",
                Position.LEFT_DEFENSE, 0, 0, -6, 7, 0, 10));
        blackhawks.addPlayer(new Defenseman("Brent Seabrook", 7, "Right",
                Position.RIGHT_DEFENSE, 14, 35, 6, 121, 32, 150));
        blackhawks.addPlayer(new Skater("Jiri Sekac", 34, "Left", Position.LEFT_WING,
                0, 1, -1, 7, 2));
        blackhawks.addPlayer(new Skater("Andrew Shaw", 65, "Right", Position.RIGHT_WING,
                14, 20, 11, 148, 69));
        blackhawks.addPlayer(new Defenseman("Viktor Svedberg", 43, "Left",
                Position.LEFT_DEFENSE, 2, 2, -5, 11, 4, 28));
        blackhawks.addPlayer(new Center("Teuvo Teravainen", 86, "Left", 13, 22,
                -2, 24, 20, 40.5, 209));
        blackhawks.addPlayer(new Skater("Viktor Tikhonov", 14, "Right",
                Position.RIGHT_WING, 0, 0, -4, 19, 6));
        blackhawks.addPlayer(new Center("Jonathan Toews", 19, "Left", 28, 30,
                16, 81, 62, 58.6, 1573));
        blackhawks.addPlayer(new Defenseman("Trevor van Riemsdyk", 57, "Right",
                Position.RIGHT_DEFENSE, 3, 11, -5, 44, 31, 155));
        blackhawks.addPlayer(new Skater("Dale Weise", 25, "Right", Position.RIGHT_WING,
                0, 1, 4, 19, 2));
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
        File f = new File(fileName);
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
        File f = new File(fileName);
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
                    return i;
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
     * @param index Index of the current team in the array, ensures that it is up-to-date in the combo box teamSelection
     *              If negative, no changes need to be made to the combo box
     */
    private void updateTeamComponents(Team oldTeam, Team newTeam, int index) {
        changeTeamWinsLabel.setText(winsString + " (Current: " + team.getWins() + ")");
        changeTeamLossesLabel.setText(lossesString + " (Current: " + team.getLosses() + ")");
        changeTeamOTLabel.setText(otString + " (Current: " + team.getOtLosses() + ")");
        changeTeamNameLabel.setText(nameString + " (Current: " + newTeam.getName() + ")");
        String roster = viewRoster.getText();
        viewRoster.setText(newTeam.getName() + roster.substring(roster.indexOf('\n')));
        roster = viewRosterWithStats.getText();
        viewRosterWithStats.setText(newTeam.getName() + roster.substring(roster.indexOf('\n')));
        if (index >= 0) {
            teamSelection.removeItem(oldTeam);
            teamSelection.insertItemAt(newTeam, index);
        }
    }

    private void displayTeamGUI() {
        // Setup Frame
        JFrame mainFrame = new JFrame(team.getName());
        mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        mainFrame.setSize(new Dimension(screenSize.width / 2, screenSize.height / 2));
        Container mainContent = mainFrame.getContentPane();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));

        mainTabs = new JTabbedPane();
        mainContent.add(mainTabs);

        editInstructionsLabel = new JLabel(editInstructions);

        // Team Tab

        teamTabs = new JTabbedPane();

        // Edit team
        Container editTeam = new Container();
        editTeam.setLayout(new BoxLayout(editTeam, BoxLayout.Y_AXIS));

        // Labels, text fields, and button
        changeTeamNameLabel = new JLabel(nameString + " (Current: " + team.getName() + ")");
        changeTeamName = new JTextField(30);
        changeTeamWinsLabel = new JLabel(winsString + " (Current: " + team.getWins() + ")");
        changeTeamWins = new JTextField(5);
        changeTeamLossesLabel = new JLabel(lossesString + " (Current: " + team.getLosses() + ")");
        changeTeamLosses = new JTextField(5);
        changeTeamOTLabel = new JLabel(otString + " (Current: " + team.getOtLosses() + ")");
        changeTeamOT = new JTextField(5);
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
                JOptionPane.showMessageDialog(mainFrame, emptyInputs,
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
                TeamGUI.updateFile();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(mainFrame, numberError, "Edit Team", JOptionPane.ERROR_MESSAGE);
                return;
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(mainFrame, fileError, "Edit Team", JOptionPane.ERROR_MESSAGE);
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
                    TeamGUI.updateFile();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(mainFrame, fileError, "Edit Team", JOptionPane.ERROR_MESSAGE);
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

        // Text Area that displays slightly more detailed roster
        viewRosterWithStats = new JTextArea(team.generateRosterWithStats());
        viewRosterWithStats.setEditable(false);
        JScrollPane viewRosterWithStatsScroll = new JScrollPane(viewRosterWithStats);
        teamTabs.add("View Basic Player Stats", viewRosterWithStatsScroll);

        mainTabs.add("Manage Teams", teamTabs);

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
            e.printStackTrace();
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
                JOptionPane.showMessageDialog(selectFrame, "Please ensure that you have selected a team. " +
                        "If there are no options to select from, please create a new team.", "Team Select",
                        JOptionPane.ERROR_MESSAGE);
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

                            if (addTeam(newTeam) == -1) {
                                JOptionPane.showMessageDialog(selectFrame, "Two teams cannot have the same name",
                                        "Create Team", JOptionPane.ERROR_MESSAGE);
                                return;
                            }

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

                            if (addTeam(newTeam) == -1) {
                                JOptionPane.showMessageDialog(selectFrame, "Two teams cannot have the same name",
                                        "Create Team", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            updateFile();

                            team = newTeam;
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
                } else {  // o is null - there was nothing to select
                    JOptionPane.showMessageDialog(deleteFrame, "There are no teams to delete.",
                            "Delete Team", JOptionPane.ERROR_MESSAGE);
                    deleteFrame.dispose();
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
        });

        newUsers.addActionListener(e -> JOptionPane.showMessageDialog(selectFrame, newInfo, "New User Information",
                JOptionPane.INFORMATION_MESSAGE));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new TeamGUI());
    }
}
