//Classe criada para a manipulação dos dados para o jogo e jogadas

var gameOn = false;
var cima = "#";
var fila1 = ["#", "#", "#"];
var fila2 = ["#", "#", "#"];

let count = 0;

let lastFlag;
let lastPosition;

function play(turn, id) {
	if (gameOn) {

		var spotTaken = $("#" + id).text();
		if (playerType === turn) {
			if (spotTaken === "#") {
				if (playerTurn.currentPiece > 0) {
					console.log(id);
					makeAMove(playerType, id.split("_")[0], id.split("_")[1], null, null);
				}
			} else if (playerTurn.currentPiece < 1) {
				if (count == 1) {
					makeAMove(playerType, id.split("_")[0], id.split("_")[1], lastFlag, lastPosition);
					count = 0;
				} else if (turn === playerType) {
					lastFlag = id.split("_")[0];
					lastPosition = id.split("_")[1];
					count += 1;
				}
			}
		}
	}
}


function makeAMove(tipo, newFlag, newPosition, lastFlag, lastPosition) {
	$.ajax({
		url: url + "/game/gameplay",
		type: 'POST',
		dataType: "json",
		contentType: "application/json",
		data: JSON.stringify({
			"type": tipo,
			"gameID": gameID,
			"newFlag": newFlag,
			"newPosition": newPosition,
			"lastFlag": lastFlag,
			"lastPosition": lastPosition,
		}),
		success: function(data) {
			gameOn = false;
			playerTurn = data.playerTurn;
			turn = data.turn;
			displayResponse(data);
		},
		error: function(error) {
			console.log(error);
		}
	});

	$("#" + "turno").text(playerTurn.login);
}


function displayResponse(data) {
	let board = data.board;
	let _cima = board.cima;
	let _fila1 = board.fila1;
	let _fila2 = board.fila2;

	if (_cima === "PLAYER1") {
		cima = "1";
	} else if (_cima === "PLAYER2") {
		cima = "2";
	}
	$("#" + "0_0").text(cima);

	for (let i = 0; i < _fila1.length; i++) {
		if (_fila1[i] === "PLAYER1") {
			fila1[i] = "1";
		} else if (_fila1[i] === "PLAYER2") {
			fila1[i] = "2";
		}

		if (_fila2[i] === "PLAYER1") {
			fila2[i] = "1";
		} else if (_fila2[i] === "PLAYER2") {
			fila2[i] = "2";
		}

		$("#" + "1_" + i).text(fila1[i]);
		$("#" + "2_" + i).text(fila2[i]);
	}

	if (data.winner == "DRAW") {
		alert("EMPATE!!");
	} else if (data.winner != null) {
		alert("O vencedor foi " + data.winner);
	}
	gameOn = true;
}

function reset() {
	cima = "#";
	fila1 = ["#", "#", "#"];
	fila2 = ["#", "#", "#"];
	count = 0;
	$(".piece").text("#");
}

$(".piece").click(function() {
	var slot = $(this).attr('id');
	play(turn, slot);
});

$("#reset").click(function() {
	reset();
});