//Classe feita para a criação e entrada em partidas


const url = 'http://localhost:8080';
let stompClient;
let gameID;
let playerType;

function connectToSocket(gameID) {
	let socket = new SockJS(url + "/gameplay");
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame) {
		stompClient.subscribe("/topic/game-progress/" + gameID, function(response) {
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
				gameId = data.gameId;
				playerType = 'PLAYER1';
				reset();
				connectToSocket(gameId);
				alert("Você criou um jogo. ID: " + data.gameId);
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
				gameId = data.gameId;
				playerType = 'PLAYER2';
				reset();
				connectToSocket(gameId);
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
		let gameId = document.getElementById("game_id").value;
		if (gameId == null || gameId === '') {
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
				"gameId": gameId
			}),
			success: function(data) {
				gameId = data.gameId;
				playerType = 'PLAYER2';
				reset();
				connectToSocket(gameId);
				alert("Você entrou na partida de: " + data.player1.login);
			},
			error: function(error) {
				console.log(error);
			}
		})
	}
}
