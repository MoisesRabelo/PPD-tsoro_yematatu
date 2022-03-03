package com.ppd.socket.tsoroyematatu.model;

import lombok.Data;

@Data
public class Player {
	private String login;
	
	private int quantPiece = 3;
	private int currentPiece = 3;
}
