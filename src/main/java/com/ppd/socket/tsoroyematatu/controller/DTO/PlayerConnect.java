package com.ppd.socket.tsoroyematatu.controller.DTO;

import com.ppd.socket.tsoroyematatu.model.Player;

import lombok.Data;

@Data
public class PlayerConnect {
	private Player player;
    private String gameID;
}
