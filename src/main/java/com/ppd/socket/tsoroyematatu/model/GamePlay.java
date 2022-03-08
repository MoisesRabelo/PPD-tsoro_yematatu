package com.ppd.socket.tsoroyematatu.model;

import com.ppd.socket.tsoroyematatu.enums.PlayerType;

import lombok.Data;

/**
 * Player made play
 * @author moises.rabelo
 *
 */
@Data
public class GamePlay {
	private PlayerType type;
	private String gameID;
	
	/*
	 * 0 - top piece
	 * 1 - second line
	 * 2 - third line
	 */
	private Integer newFlag;
	//Position where the Player wants to place the piece
	private Integer newPosition;
	
	
	private Integer lastFlag;
	//Position the Player will take a piece
	private Integer lastPosition;
}
