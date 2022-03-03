package com.ppd.socket.tsoroyematatu.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SpaceHole {
	EMPTY(0), PLAYER1(1), PLAYER2(2);
	
	private Integer value;
	
	public static SpaceHole findSpaceHoleByValue(Integer value) {
		for (SpaceHole e : SpaceHole.values()){
			if (e.value.equals(value))
				return e;
		}
		return null;
	}
}
