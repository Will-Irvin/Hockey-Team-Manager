import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

/**
 * SelectTeamGUI
 *
 * This GUI class serves as the introductory menu of the application. This is where the user will create or select
 * a team to manage in the main part of the application. The user can select an existing team, create a new team, delete
 * any existing teams, and view important information if it is their first time using the app.
 */
public class SelectTeamGUI extends JComponent implements Runnable {
    // Read in from the stored file, user selects team from this list
    private static final ArrayList<Team> teams = new ArrayList<>();
    private Team selectedTeam;  // Team that the user chooses to open or creates

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

    JComboBox<Team> teamSelection;
    JButton selectTeam;

    JLabel otherOptionsLabel;
    JButton createTeam;
    JButton deleteTeam;

    JButton newUsers;

    // Getter method
    public Team getSelectedTeam() {
        return selectedTeam;
    }

    /**
     * Creates a sample team that is pre-generated for a user who has no file. Allows the user to get accustomed to the
     * app and its features before creating their own team if they so choose.
     * @return The generated sample team containing data from the 2015 Chicago Blackhawks Regular Season.
     */
    public static Team createSample() {
        Team blackhawks = new Team("Sample Team: Chicago Blackhawks - 2015 Regular Season");
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
    public static boolean addTeam(Team team) {
        if (teams.contains(team)) {
            return false;
        }
        if (teams.size() == 0) {
            teams.add(team);
            return true;
        }

        int low = 0;
        int high = teams.size();
        while (true) {
            int i = (low + high) / 2;
            if (team.getName().compareTo(teams.get(i).getName()) <= 0) {
                if (i == 0) {
                    teams.add(0, team);
                    return true;
                } else if (team.getName().compareTo(teams.get(i - 1).getName()) >= 0) {
                    teams.add(i, team);
                    return true;
                } else {
                    high = i;
                }
            } else {
                if (i == teams.size() - 1) {
                    teams.add(team);
                    return true;
                } else if (team.getName().compareTo(teams.get(i + 1).getName()) <= 0) {
                    teams.add(i + 1, team);
                    return true;
                } else {
                    low = i;
                }
            }
        }
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
        JFrame frame = new JFrame("Welcome to Team Manager");
        Container content = frame.getContentPane();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        // Top of frame, selecting an already created team
        JPanel selectTeamPanel = new JPanel();
        // Combo Box to select your team
        teamSelection = new JComboBox<Team>();
        for (Team team: teams) {
            teamSelection.addItem(team);
        }
        selectTeamPanel.add(teamSelection);
        // Button to confirm selection
        selectTeam = new JButton("Open this team");
        selectTeamPanel.add(selectTeam);

        content.add(selectTeamPanel);

        // Middle of frame that contains other options with teams
        JPanel otherOptions = new JPanel();
        otherOptionsLabel = new JLabel("Additional Options: ");
        createTeam = new JButton("Create New Team");
        deleteTeam = new JButton("Delete Team");
        otherOptions.add(otherOptionsLabel);
        otherOptions.add(createTeam);
        otherOptions.add(deleteTeam);

        content.add(otherOptions);

        // Bottom of Frame, button where new users can find additional guidance
        JPanel bottom = new JPanel();
        newUsers = new JButton("Help and Guidance for New Users");
        bottom.add(newUsers);

        content.add(bottom);
        frame.pack();
        frame.setLocationRelativeTo(null);

        /*
          Sets the selected team instance var to the selected team from the combo box and closes the frame. If no
          selection is made (o is null/not a team) an error message will display and the frame will remain open.
         */
        selectTeam.addActionListener(e -> {
            Object o = teamSelection.getSelectedItem();
            if (o instanceof Team t) {
                selectedTeam = t;
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Please ensure that you have selected a team. " +
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
            int input = JOptionPane.showConfirmDialog(frame, "Would you like to provide a win/loss record for" +
                    " this team?", "Create Team", JOptionPane.YES_NO_CANCEL_OPTION);
            switch (input) {
                case JOptionPane.YES_OPTION -> { // Prompt for wins, losses, ot losses
                    while (true) {
                        try {
                            // Entering information for new team
                            String name = JOptionPane.showInputDialog(frame, "Enter Team Name:",
                                    "Create Team", JOptionPane.QUESTION_MESSAGE);
                            int wins = Integer.parseInt(JOptionPane.showInputDialog(frame,
                                    "Enter number of wins:", "Create Team", JOptionPane.QUESTION_MESSAGE));
                            int losses = Integer.parseInt(JOptionPane.showInputDialog(frame,
                                    "Enter number of losses:", "Create Team",
                                    JOptionPane.QUESTION_MESSAGE));
                            int otLosses = Integer.parseInt(JOptionPane.showInputDialog(frame,
                                    "Enter number of overtime losses or ties:", "Create Team",
                                    JOptionPane.QUESTION_MESSAGE));
                            Team newTeam = new Team(name, wins, losses, otLosses);

                            if (!addTeam(newTeam)) {
                                JOptionPane.showMessageDialog(frame, "Two teams cannot have the same name",
                                        "Create Team", JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            updateFile();

                            // Set selected team and close frame
                            selectedTeam = newTeam;
                            frame.dispose();
                            break;
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(frame, "Please enter a number when prompted",
                                    "Create Team", JOptionPane.ERROR_MESSAGE);
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(frame, "There was an issue writing to the file. " +
                                    "Please try again", "Create Team", JOptionPane.ERROR_MESSAGE);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Create Team",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
                case JOptionPane.NO_OPTION -> {  // Similar to yes, but only prompt for name
                    while (true) {
                        try {
                            String name = JOptionPane.showInputDialog(frame, "Enter team name:",
                                    "Create Team", JOptionPane.QUESTION_MESSAGE);

                            Team newTeam = new Team(name);

                            if (!addTeam(newTeam)) {
                                JOptionPane.showMessageDialog(frame, "Two teams cannot have the same name",
                                        "Create Team", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            updateFile();

                            selectedTeam = newTeam;
                            frame.dispose();

                            break;
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Create Team",
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
            frame.setVisible(false);

            // New Frame
            JFrame deleteFrame = new JFrame("Delete Team");
            deleteFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            Container deleteContent = deleteFrame.getContentPane();
            deleteContent.setLayout(new BorderLayout());

            deleteContent.add(teamSelection, BorderLayout.CENTER);
            JButton deleteButton = new JButton("Delete Selected Team");
            deleteContent.add(deleteButton, BorderLayout.SOUTH);

            deleteFrame.pack();
            deleteFrame.setLocationRelativeTo(frame);
            deleteFrame.setVisible(true);

            /*
              Asks the user to confirm their choice before actually deleting the team. After user has given
              confirmation, removes the selected team from the static variable, the combo box, and updates this change
              in the data file.
             */
            deleteButton.addActionListener(e1 -> {
                Object o = teamSelection.getSelectedItem();
                if (o instanceof Team t) {
                    int finalCheck = JOptionPane.showConfirmDialog(frame, "WARNING: Are you sure you want to " +
                                    "delete this team: " + t.getName() + "? This cannot be undone.",
                            "Delete Team", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (finalCheck == JOptionPane.YES_OPTION) {
                        teams.remove(t);
                        teamSelection.removeItemAt(teamSelection.getSelectedIndex());
                        try {
                            updateFile();
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(frame, ex.getMessage(), "File Error",
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
                    selectTeamPanel.removeAll();
                    selectTeamPanel.add(teamSelection);
                    selectTeamPanel.add(selectTeam);
                    frame.setVisible(true);
                }
            });
        });

        newUsers.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, newInfo, "New User Information", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    public static void main(String[] args) {
        SelectTeamGUI gui = new SelectTeamGUI();
        SwingUtilities.invokeLater(gui);
    }
}
