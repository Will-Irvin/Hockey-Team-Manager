# Hockey Team Manager
This project is a way to organize and manage a hockey team and its players. It is intended for a coach, player, or spectator to monitor a team's players and 
stats and organize the team into lines.

After finishing my first college course in programming in Java, I wanted to start a project where I could apply what I had learned. I thought back on my own personal 
experiences to consider a possible application using Java. I thought of my years watching and playing hockey, and I realized that Java's object-oriented nature would
work well with storing data for different players on a team. 
## Build Status
All of the functionality that I have intended to implement for the project is completed. I am not currently aware of any bugs in the application. I am planning on 
having some personal friends test the application, and they may suggest some other features to implement.
## Features
The backend code dealing with the different team, line, and player classes was built from the ground up; and the GUI for the application was designed and implemented
with Swing. Using the GUI, a user can create, edit, and delete their teams, players, goalies, and lines. Once they have created all of the players on a team in the
program, they can use the final tab "Enter Stats From Game" in order to adjust the team's stats as a game is happening live or after a game using their notes or 
the scoresheet. The data from this program is saved and stored in a serialized file which is created on the user's computer when the program is first initialized. The
file is updated whenever an element is created or updated or whenever the user has finished entering stats after a game. The user can also generate a text file 
containing formatted data for each of the selected teams. This text file can be transferred between computers, but it only contains the data for the teams at the time
it was created; it is not updated with any new changes.
## How to Use
The main method for the program is located in the TeamGUI.java file. In order to use this program, you must have a JVM and all of the .java files in the src folder in
this repository on your device to run the code. The TeamGUI.java file must be compiled into bytecode and then run using your device's JVM.
