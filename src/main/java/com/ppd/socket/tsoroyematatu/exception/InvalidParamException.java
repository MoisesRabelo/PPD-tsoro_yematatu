package com.ppd.socket.tsoroyematatu.exception;

import lombok.Getter;

@Getter
public class InvalidParamException extends Exception{
	private String message;
	
	public InvalidParamException(String message) {
		this.message = message;
	}
}
