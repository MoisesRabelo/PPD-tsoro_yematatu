package com.ppd.socket.tsoroyematatu.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PlayerType {
	DRAW(0), PLAYER1(1), PLAYER2(2);
	
	private Integer value;
	
	public static PlayerType findPlayerTypeByValue(Integer value) {
		for (PlayerType e : PlayerType.values()){
			if (e.value.equals(value))
				return e;
		}
		return null;
	}
}
