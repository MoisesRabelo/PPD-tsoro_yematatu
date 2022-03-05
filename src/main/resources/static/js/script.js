//Classe criada para a manipulação dos dados para o jogo e jogadas

var gameOn = false;
var cima = "#";
var fila1 = ["#", "#", "#"];
var fila2 = ["#", "#", "#"];
var count = 0;

var turno = "";

var startTurn = prompt("Escolha sua peça", "X ou O").toUpperCase;

function playerTurn(turno, id) {
	if(gameOn) {
		var spotTaken = $("#" + id).text();
		if (spotTaken === "#") {
			makeAMove(playertype, id.split("_")[0], id.split("_")[1]);
		}
	}
}