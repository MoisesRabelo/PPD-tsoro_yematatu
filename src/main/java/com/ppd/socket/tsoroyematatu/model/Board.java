package com.ppd.socket.tsoroyematatu.model;

import com.ppd.socket.tsoroyematatu.enums.SpaceHole;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Board {
	SpaceHole cima;
	SpaceHole[] fila1;
	SpaceHole[] fila2;
	
	public Board() {
		clearBoard();
	}
	
	public void clearBoard() {
		cima = SpaceHole.EMPTY;
		fila1 = new SpaceHole[3];
		fila2 = new SpaceHole[3];
		for(int i = 0; i < fila1.length; i++) {
			fila1[i] = SpaceHole.EMPTY;
			fila2[i] = SpaceHole.EMPTY; 
		}
	}
}
