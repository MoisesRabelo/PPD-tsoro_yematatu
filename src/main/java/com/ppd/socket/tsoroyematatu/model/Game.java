package com.ppd.socket.tsoroyematatu.model;

import com.ppd.socket.tsoroyematatu.enums.GameStatus;
import com.ppd.socket.tsoroyematatu.enums.PlayerType;

import lombok.Data;

@Data
public class Game {
	private String gameID;
	
	private Player player1;
	private Player player2;
	
	private Player playerTurn;
	private String turn;
	private Board board;
	private GameStatus status;
	
	private PlayerType winner;
}
