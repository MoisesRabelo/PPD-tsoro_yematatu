package com.ppd.socket.tsoroyematatu.model;

import com.ppd.socket.tsoroyematatu.enums.PlayerType;

import lombok.Data;

/**
 * Jogada feita pelo Jogador
 * @author Moises
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
	//Posição onde o Player deseja colocar a peça
	private Integer newPosition;
	
	
	private Integer lastFlag;
	//Posição que o Player vai tirar uma peça
	private Integer lastPosition;
}
