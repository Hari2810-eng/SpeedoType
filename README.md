# SpeedoType Game Web Application

## Overview
**SpeedoType** is a real-time multiplayer typing game where players compete by typing a given text as fast and accurately as possible. Players can join rooms, type out text passages, and challenge each other to achieve the highest Words Per Minute (WPM) score. The application uses WebSocket technology for real-time communication, ensuring that all players' progress is tracked and displayed dynamically.

## Features
- **Multiplayer Typing Races**: Players compete against each other by typing out a given passage of text.
- **Real-Time Updates**: Player progress is updated in real-time, showing current WPM and accuracy.
- **Dynamic Leaderboard**: Ranks players based on WPM and shows the leader at the end of each game round.
- **Rematch Feature**: After each game round, players can choose to rematch or exit the room.
- **WebSocket Communication**: Ensures real-time interaction between clients and the server.
- **Responsive UI**: Clean, minimalistic design with real-time progress tracking, countdown timer, and result display.

## Technologies Used
- **Frontend**: HTML, CSS (Poppins font for styling), JavaScript
- **Backend**: Java (Jakarta EE)
- **WebSocket**: For real-time communication between players and server
- **Game Logic**: JavaScript handles player inputs and progress tracking
- **Room Management**: Each room holds multiple players competing in the same game round

## Setup and Installation
1. **Clone the repository**:
   git clone https://github.com/Hari2810-eng/SpeedoType.git
2.**Java Setup**:
   Ensure you have Java installed (Jakarta EE support is required). Use your favorite IDE to import the project.
   java -version
3.**Server Setup**:
 - You can use any server that supports WebSockets (e.g., Apache Tomcat).
 - Deploy the project to your server.
4.**Frontend Setup**:
 - Ensure you have the necessary HTML, CSS, and JavaScript files properly configured and accessible through the web server.
 - Your web.xml file should configure the WebSocket server endpoint properly.
5.**Run the Application**:
 - Start the server and navigate to the URL where the frontend is hosted.
 - Players can join rooms, compete, and see their progress in real-time.

## Game Flow
**Joining a Room**: Players enter a room where the game will start after everyone is ready.
**Game Start**: Players are presented with a text passage to type. The timer starts, and progress is tracked in real-time.
**Real-Time Typing and Progress**: As players type, their progress is displayed dynamically, and their WPM is calculated.
**End of Round**: Once the timer hits 60 seconds or a player finishes, the leaderboard is displayed showing each player's WPM.
**Rematch/Exit**: Players can then choose to either rematch or exit. If players choose rematch, the game resets with new text. Players who exit are removed from the room.

## Key Classes and Files
**WebSocketServer.java**: Handles WebSocket communication between clients and the server.
**GameRepo.java**: Manages rooms, player sessions, and rematch logic.
**Room.java**: Handles game state, player progress, and leaderboard.
**playScript.js**: Manages client-side logic such as progress tracking, WPM calculation, and WebSocket message handling.

## Future Enhancements
**Customizable Text**: Allow players to choose the level of difficulty or customize the text passage.
**Matchmaking System**: Implement automatic matchmaking to pair players of similar skill levels.
**Player Profiles**: Add user accounts with profiles to track long-term stats and achievements.
