<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Play Multiplayer Mode</title>
    <link rel="stylesheet" href="PlayMulti.css">
    
</head>
<body>
	<!-- Modal Popup for Game Over -->
	<div id="gameOverModal" class="modal">
	  <div class="modal-content">
	    <span class="close">&times;</span>
	    <h2>Game Over!</h2>
	    <p id="winnerName">Winner: </p>
	    <h3>Leaderboard</h3>
	    <table id="modal-leaderboard">
	      <thead>
	        <tr>
	          <th>Username</th>
	          <th>WPM</th>
	        </tr>
	      </thead>
	      <tbody id="leaderboardBody">
	        <!-- Leaderboard will be dynamically populated here -->
	      </tbody>
	    </table>
	    <button id="rematchButtonModal">Rematch</button>
	    <button id="exitButtonModal">Exit</button>
	  </div>
	</div>
		

    <div class="leaderboard-container">
        <table>
            <tbody id="leaderboard-table">
                <!-- Leaderboard entries will be dynamically populated here -->
            	<thead>
            <tr>
                <th>Username</th>
                <th>Score</th>
            </tr>
        </thead>
            </tbody>
        </table>
    </div>
    <div class="play-wrapper">
        <h2>Play Multiplayer Mode</h2>
        <div class="input-field-container">
            <input type="text" class="input-field" id="quoteInput" placeholder="Type here..." disabled>
        </div>
        <div class="content-box">
            <div class="typing-text">
                <p id="paragraph">Waiting for game text...</p>
            </div>
            <div class="content">
                <ul class="result-details">
                    <li class="time">
                        <p>Time Left:</p>
                        <span><b id="timer">60</b></span>
                    </li>
                    <li class="wpm">
                        <p>WPM:</p>
                        <span id="wpmDisplay">0</span>
                    </li>
                </ul>
            </div>
        </div>
        <div class="buttons-container">
            <button id="start-btn" onclick="readyButtonClick()">Ready</button>
            <button id="rematchButton" style="display: none;">Rematch</button>
            <button id="exitButton" style="display: none;">Exit</button>
        </div>
    </div>

    
    <script src="playScript.js"></script>
    
    <script>
	    sessionStorage.setItem('username', '<%= session.getAttribute("username") != null ? session.getAttribute("username") : "" %>');
	    sessionStorage.setItem('roomCode', '<%= session.getAttribute("roomCode") != null ? session.getAttribute("roomCode") : "" %>');
	    sessionStorage.setItem('level', '<%= session.getAttribute("level") != null ? session.getAttribute("level") : "" %>');
	
	    console.log("Room Code from sessionStorage:", sessionStorage.getItem('roomCode'));
	    console.log("Level from sessionStorage:", sessionStorage.getItem('level'));
	    console.log("Username from sessionStorage:", sessionStorage.getItem('username'));
    </script>
</body>
</html>
