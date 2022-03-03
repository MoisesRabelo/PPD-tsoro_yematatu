package com.ppd.socket.tsoroyematatu.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ppd.socket.tsoroyematatu.controller.DTO.PlayerConnect;
import com.ppd.socket.tsoroyematatu.exception.InvalidGameException;
import com.ppd.socket.tsoroyematatu.exception.InvalidParamException;
import com.ppd.socket.tsoroyematatu.exception.NotFoundException;
import com.ppd.socket.tsoroyematatu.model.Game;
import com.ppd.socket.tsoroyematatu.model.GamePlay;
import com.ppd.socket.tsoroyematatu.model.Player;
import com.ppd.socket.tsoroyematatu.service.GameService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/game")
public class GameController {
	private final GameService gameService;
	private final SimpMessagingTemplate simpMessagingTemplate;

	@PostMapping("/start")
	public ResponseEntity<Game> start(@RequestBody Player player) {
		log.info("Pedido de criação de partida pelo: {}", player);
		return ResponseEntity.ok(gameService.createGame(player));
	}

	@PostMapping("/connect")
	public ResponseEntity<Game> connect(@RequestBody PlayerConnect request)
			throws InvalidParamException, InvalidGameException {
		log.info("Conectando na partida: {}", request);
		return ResponseEntity.ok(gameService.connectToGame(request.getPlayer(), request.getGameID()));
	}

	@PostMapping("/connect/random")
	public ResponseEntity<Game> connectRandom(@RequestBody Player player) throws NotFoundException {
		log.info("Conectando a uma partida Random: {}", player);
		return ResponseEntity.ok(gameService.connectToGameRandom(player));
	}

	@PostMapping("/gameplay")
	public ResponseEntity<Game> gamePlay(@RequestBody GamePlay request)
			throws InvalidGameException, InvalidParamException {
		log.info("Jogada: {}", request);
		Game game = gameService.movePiece(request);
		simpMessagingTemplate.convertAndSend("/topic/game-progress/" + game.getGameID(), game);
		return ResponseEntity.ok(game);
	}
}
