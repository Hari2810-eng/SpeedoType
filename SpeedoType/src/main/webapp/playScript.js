const quoteInputElement = document.getElementById('quoteInput');
const quoteDisplayElement = document.getElementById('paragraph');
const timerElement = document.getElementById('timer');
const username = sessionStorage.getItem('username');
const roomCode = sessionStorage.getItem('roomCode');
const level = sessionStorage.getItem('level');
const message = document.getElementById('d');

const gameOverModal = document.getElementById("gameOverModal");
const winnerNameElement = document.getElementById("winnerName");
const leaderboardBody = document.getElementById("leaderboardBody");
const closeBtn = document.querySelector(".close");
const rematchButtonModal = document.getElementById("rematchButtonModal");
const exitButtonModal = document.getElementById("exitButtonModal");

console.log("Room Code from sessionStorage:", roomCode);
console.log("Level from sessionStorage:", level);
console.log('Username from sessionStorage:', username);

let textContent;
let check = true;
let now = 0;
let startTime;
let isGameStarted = false;
let ws;

const userProgressBars = new Map();
const progressBarContainer = document.getElementById('progress-bar-container');


window.onload = init;

function init() {
	if (!roomCode || roomCode === "" || !username) {
		console.error("Room Code is not set correctly. Cannot initiate WebSocket.");
		return;
	}
	ws = new WebSocket("ws://" + window.location.host + "/SpeedoType/speedoType/" + roomCode + "/" + level + "/" + username);
	console.log("Initiating WebSocket with room code:", roomCode, "and level:", level);

	ws.onopen = function(event) {
		console.log("WebSocket connection established on room " + roomCode);
	};
	ws.onerror = function(event) {
		console.error("WebSocket error observed:", event);
	};

	ws.onmessage = function(event) {
		console.log("Message received:", event.data);
		const message = event.data;
		handleWebSocketMessage(message);
	};

	ws.onclose = function(event) {
		console.log("WebSocket connection closed:", event);
		alert("Connection to the game server has been closed. Please refresh the page.");
	};
}


function handleWebSocketMessage(message) {
	if (message == "WAIT") {
		alert("The room is already in the game, please wait");
	}
	else if (message === "ROOM_FULL") {
		alert("The room is full. Please try another room.");
		history.back();
	}
	else if (message.startsWith("TEXT:")) {
		textContent = message.substring(5).trim();
		quoteDisplayElement.innerText = '';
		textContent.split('').forEach(character => {
			const characterSpan = document.createElement('span');
			characterSpan.innerText = character;
			quoteDisplayElement.appendChild(characterSpan);
		});
		quoteInputElement.value = null;
	}
	else if (message.startsWith("START")) {
		startGame();
	}
	else if (message.startsWith("RESULT")) {
		const resultParts = message.split(":");
		const winner = resultParts[1];
		const leaderboardData = resultParts.slice(2);
		const leaderboard = leaderboardData.map(entry => {
			const [username, wpm] = entry.split(",");
			return { username, wpm: parseInt(wpm, 10) };
		});
		console.log(message);
		endGame(winner, leaderboard);
	}
	else if (message.startsWith("REMATCH")) {
		showRematchOptions();
	}
	else if (message.startsWith("PROGRESS:")) {
		const parts = message.replace("PROGRESS:", "").split(":");
		console.log("Parts after split:", parts);


		const progress = parts[0];
		const sessionId = parts[1];
		const wpm = parts[2];

		console.log("Session ID:", sessionId);
		console.log("Progress:", progress);
		console.log("WPM:", wpm);

		updateProgressBar(sessionId, wpm);
	}
	else if (message.startsWith("LEADERBOARD:")) {
		const leaderBoardData = message.substring("LEADERBOARD:".length);
		displayLeaderBoard(leaderBoardData);
	}
}

function displayLeaderBoard(leaderBoardData) {
	const leaderboardTableBody = document.getElementById("leaderboard-table");

	const players = leaderBoardData.split(";");
	players.forEach(player => {
		if (!player.trim()) return;
		const [username, score] = player.split(":");

		let existingRow = Array.from(leaderboardTableBody.rows).find(row => row.cells[0].textContent === username);
		if (existingRow) {
			existingRow.cells[1].textContent = score;
		} else {
			const row = document.createElement("tr");

			const usernameCell = document.createElement("td");
			usernameCell.textContent = username;

			const scoreCell = document.createElement("td");
			scoreCell.textContent = score;

			row.appendChild(usernameCell);
			row.appendChild(scoreCell);

			leaderboardTableBody.appendChild(row);
		}
	});
}

function showGameOverModal(winner, leaderboard) {

	winnerNameElement.textContent = `Winner: ${winner}`;
	leaderboardBody.innerHTML = "";
	leaderboard.forEach(player => {
		const row = document.createElement("tr");
		const usernameCell = document.createElement("td");
		const wpmCell = document.createElement("td");

		usernameCell.textContent = player.username;
		wpmCell.textContent = player.wpm;

		row.appendChild(usernameCell);
		row.appendChild(wpmCell);
		leaderboardBody.appendChild(row);
	});

	gameOverModal.style.display = "flex";
}


closeBtn.onclick = function() {
	gameOverModal.style.display = "none";
};


rematchButtonModal.onclick = function() {
	console.log("Rematch clicked");
	gameOverModal.style.display = "none";
	ws.send(`REMATCH`);
};

exitButtonModal.onclick = function() {
	console.log("Exit clicked");
	gameOverModal.style.display = "none";
};


function showRematchOptions() {
	document.getElementById("rematchButton").style.display = "inline";
	document.getElementById("rematchButton").addEventListener('click', function() {
		location.reload();
	});
	document.getElementById("exitButton").style.display = "inline";
	document.getElementById("exitButton").addEventListener('click', function() {
		history.back();
	});

}


function updateProgressBar(sessionId, wpm) {

	const wpmDisplay = document.getElementById("wpmDisplay");

	if (wpmDisplay) {
		wpmDisplay.textContent = `${wpm}`;
	} else {
		console.error("WPM display element not found.");
	}
}


function sendMessage() {
	const messageInput = `${n.value} finished check in ${now}seconds`;
	ws.send(messageInput);
}


quoteInputElement.addEventListener('input', () => {
	const arrayQuote = document.getElementById('paragraph').querySelectorAll('span');
	const inputText = quoteInputElement.value;
	const inputLength = inputText.length;
	const textContentTrimmed = textContent.trim();

	console.log("Span Length: ", arrayQuote.length);
	console.log("Inserting cursor before span at index: ", inputLength);


	let correctChars = 0;
	let isFullyCorrect = true;

	arrayQuote.forEach((span) => {
		span.style.textDecoration = 'none';
		span.style.color = 'initial';
	});

	if (inputLength < arrayQuote.length) {
		arrayQuote[inputLength].style.textDecoration = 'underline';
	}

	for (let i = 0; i < inputLength; i++) {
		console.log("Comparing text :", inputText[i], "with :", textContentTrimmed[i], " at :", i);
		if (inputText[i] === textContentTrimmed[i]) {
			arrayQuote[i].style.color = 'green';
			correctChars++;
		} else {
			arrayQuote[i].style.color = 'red';
			isFullyCorrect = false;
		}
	}

	if (inputLength !== textContentTrimmed.length) {
		isFullyCorrect = false;
	}

	const progressPercentage = textContent.length > 0 ? Math.round((correctChars / textContentTrimmed.length) * 100) : 0;
	const now = new Date().getTime();
	const elapsed = (now - startTime) / 1000;
	const wordsTyped = quoteInputElement.value.split(' ').length;
	const currentWPM = Math.round((wordsTyped / elapsed) * 60);

	const message = `PROGRESS:${progressPercentage}:${username}:${currentWPM}`;
	console.log("Sending WebSocket message:", message);
	ws.send(message);
});



function sTime() {
	let now = 0;
	function updateTimer() {
		console.log(now);
		now++;
		
		if (now >= 60) {
			const inputText = quoteInputElement.value;
			const textContentTrimmed = textContent.trim();
			let isFullyCorrect = true;

			for (let i = 0; i < inputText.length; i++) {
				if (inputText[i] !== textContentTrimmed[i]) {
					break;
				}
			}

			if (inputText.length !== textContentTrimmed.length) {
				isFullyCorrect = false;
			}
			if (isFullyCorrect) {
				const elapsed = 60;
				const wordsTyped = quoteInputElement.value.split(' ').length;
				const currentWPM = Math.round((wordsTyped / elapsed) * 60);

				//message.textContent = `RESULT:${username}:${currentWPM}`;
				console.log(`RESULT:${username}:${currentWPM}`);
				ws.send(`RESULT:${username}:${currentWPM}`);
			} else {
				//message.textContent = `RESULT: ${username} ran out of time`;
				console.log(`RESULT:${username}:0`);
				ws.send(`RESULT:${username}:0`);
			}

			quoteInputElement.readOnly = true;
			check = false;
			clearInterval(tm);
		}
		timerElement.textContent = now;
		if (!check) {
			clearInterval(tm);
		}
	}

	startTime = new Date().getTime();
	const tm = setInterval(updateTimer, 1000);
	document.getElementById('wpmDisplay').innerText = '0';
}

function endGame(winner, leaderboard) {
	timerElement.textContent = "Finished";
	quoteInputElement.readOnly = true;
	showGameOverModal(winner, leaderboard);
}

function updateWPM() {
	const input = quoteInputElement.value;
	const now1 = new Date().getTime();
	const totalSeconds = Math.floor((now1 - startTime) / 1000);
	const totalWords = input.split(' ').length;
	const wpm = Math.round((totalWords / totalSeconds) * 60);
	document.getElementById('wpmDisplay').textContent = `${wpm}`;
}

function startGame() {
	quoteInputElement.removeAttribute('disabled');
	sTime();
}

function startButtonClick() {
	ws.send("START")
}
function readyButtonClick() {
	ws.send("READY");
	const element = document.getElementById('quoteInput');
	element.focus();
	document.getElementById('start-btn').disabled = true;
}
