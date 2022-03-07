//Classe feita para a criação e entrada em partidas

const url = 'http://localhost:8080';
let stompClient;
let gameID;
let playerType;
var playerTurn = new Object();
let turn;

function connectToSocket(gameID) {
	let socket = new SockJS(url + "/gameplay");
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame) {
		stompClient.subscribe("/topic/game-progress/" + gameID, function(response) {
			var data = response.body;
			playerTurn = data.playerTurn;
			turn = data.turn;
			displayResponse(JSON.parse(response.body));
		})
	})
}

function createGame() {
	let login = document.getElementById("login").value;
	if (login == null || login === '') {
		alert("Entre com um NickName");
	} else {
		$.ajax({
			url: url + "/game/start",
			type: 'POST',
			dataType: "json",
			contentType: "application/json",
			data: JSON.stringify({
				"login": login
			}),
			success: function(data) {
				gameID = data.gameID;
				playerType = 'PLAYER1';
				reset();
				connectToSocket(gameID);
				alert("Você criou um jogo. ID: " + data.gameID);
				gameOn = true;
			},
			error: function(error) {
				console.log(error);
			}
		})
	}
}

function connectToRandom() {
	let login = document.getElementById("login").value;
	if (login == null || login === '') {
		alert("Entre com um NickName");
	} else {
		$.ajax({
			url: url + "/game/connect/random",
			type: 'POST',
			dataType: "json",
			contentType: "application/json",
			data: JSON.stringify({
				"login": login
			}),
			success: function(data) {
				gameID = data.gameID;
				playerType = 'PLAYER2';
				reset();
				connectToSocket(gameID);
				alert("Você entrou na partida de: " + data.player1.login);
			},
			error: function(error) {
				console.log(error);
			}
		})
	}
}

function connectToID() {
	let login = document.getElementById("login").value;
	if (login == null || login === '') {
		alert("Entre com um NickName");
	} else {
		let gameID = document.getElementById("game_id").value;
		if (gameID == null || gameID === '') {
			alert("Por favor, coloque o ID da partida");
		}
		$.ajax({
			url: url + "/game/connect",
			type: 'POST',
			dataType: "json",
			contentType: "application/json",
			data: JSON.stringify({
				"player": {
					"login": login
				},
				"gameID": gameID
			}),
			success: function(data) {
				gameID = data.gameID;
				playerType = 'PLAYER2';
				reset();
				connectToSocket(gameID);
				alert("Você entrou na partida de: " + data.player1.login);
			},
			error: function(error) {
				console.log(error);
			}
		})
	}
}
