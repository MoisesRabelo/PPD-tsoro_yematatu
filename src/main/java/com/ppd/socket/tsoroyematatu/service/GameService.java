package com.ppd.socket.tsoroyematatu.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ppd.socket.tsoroyematatu.enums.GameStatus;
import com.ppd.socket.tsoroyematatu.enums.PlayerType;
import com.ppd.socket.tsoroyematatu.enums.SpaceHole;
import com.ppd.socket.tsoroyematatu.exception.InvalidGameException;
import com.ppd.socket.tsoroyematatu.exception.InvalidParamException;
import com.ppd.socket.tsoroyematatu.exception.NotFoundException;
import com.ppd.socket.tsoroyematatu.model.Board;
import com.ppd.socket.tsoroyematatu.model.Game;
import com.ppd.socket.tsoroyematatu.model.GamePlay;
import com.ppd.socket.tsoroyematatu.model.Player;
import com.ppd.socket.tsoroyematatu.storage.GameStorage;

import lombok.AllArgsConstructor;


/**
 * 
 * @author moises.rabelo
 *
 */
@Service
@AllArgsConstructor
public class GameService {
	
	/**
	 * Create a new game
	 *
	 * @param player
	 * 			The first player
	 * @return the created game
	 */
	public Game createGame(Player player) {
		Game game = new Game();
		
		game.setBoard(new Board());		
		game.setGameID(UUID.randomUUID().toString());
		game.setPlayer1(player);
		game.setPlayerTurn(player);
		game.setTurn("PLAYER1");
		game.setStatus(GameStatus.NEW);
		GameStorage.getInstance().setGame(game);
		return game;
	}
	
	/**
	 * Joins an already created game, from the gameID received
	 *
	 * @param player
	 * 			the second Player
	 * @param gameID
	 * 			to know which game is entering
	 * @return the Game you joined
	 *
	 * @throws InvalidParamException
	 * 			When there is no match with the given ID
	 * @throws InvalidGameException
	 * 			When the match already has two players
	 */
	public Game connectToGame(Player player, String gameID) throws InvalidParamException, InvalidGameException {
		if(GameStorage.getInstance().getGames().containsKey(gameID)) {
			Game game = GameStorage.getInstance().getGames().get(gameID);
			if(game.getPlayer2() == null) {
				game.setPlayer2(player);
				game.setStatus(GameStatus.PROGRESS);
				GameStorage.getInstance().setGame(game);
				return game;
			}
			
			throw new InvalidGameException("Partida Cheia!!!");
		}
		
		throw new InvalidParamException("Não possui nenhuma partida com este ID!!!");
	}
	
	/**
	 * Join an already created game
	 *
	 * @param player
	 * 			any user wanting to join any match
	 * @return the game the player joined or created
	 * @throws NotFoundException
	 * 			When there is no free match
	 */
	public Game connectToGameRandom(Player player) throws NotFoundException {
		Game game = GameStorage.getInstance().getGames().values().stream()
				.filter(g -> g.getStatus().equals(GameStatus.NEW))
				.findAny().orElseThrow(() -> new NotFoundException("Não existe partida aguardando jogador"));
		
		game.setPlayer2(player);
		game.setStatus(GameStatus.PROGRESS);
		GameStorage.getInstance().setGame(game);
		return game;
		
	}
	
	
	/**
	 * Make the move
	 * 
	 * @param play
	 * 			Play made by player
	 * @return the game in which the move is taking place
	 * @throws InvalidParamException
	 * 			When there is no match for the requested move
	 * @throws InvalidGameException
	 * 			When the match has already ended
	 */
	public Game movePiece(GamePlay play) throws InvalidParamException, InvalidGameException {
		if(GameStorage.getInstance().getGames().containsKey(play.getGameID())) {
			Game game = GameStorage.getInstance().getGames().get(play.getGameID());
			
			if (game.getStatus().equals(GameStatus.FINISHED)) {
					throw new InvalidGameException("A partida já acabou");
			}
			
			Board board = game.getBoard();
			Player currentPlayer = play.getType().equals(PlayerType.PLAYER1) ? game.getPlayer1() : game.getPlayer2();
			play(play, board, currentPlayer);
			
			PlayerType winner = checkWinner(board);
			if(winner != null) {
				game.setWinner(winner);
			}
			game.setPlayerTurn(play.getType().equals(PlayerType.PLAYER1) ? game.getPlayer2() : game.getPlayer1());
			game.setTurn(play.getType().equals(PlayerType.PLAYER1) ? "PLAYER2" : "PLAYER1");
			GameStorage.getInstance().setGame(game);
			return game;
		}
		
		throw new InvalidParamException("Não possui nenhuma partida com o ID dessa jogada!!!");
	}
	
	
	private void play(GamePlay play, Board board, Player currentPlayer) throws InvalidGameException {
						
		switch (play.getNewFlag()) {
			case 0:
				if(board.getCima().equals(SpaceHole.EMPTY)) {
					if(currentPlayer.getCurrentPiece() == 0) {
						if(checkLastPosition(play, board)) {
							board.setCima(SpaceHole.findSpaceHoleByValue(play.getType().getValue()));
						}
					} else {
						board.setCima(SpaceHole.findSpaceHoleByValue(play.getType().getValue()));
						currentPlayer.setCurrentPiece(currentPlayer.getCurrentPiece() - 1);
					}
				}
				break;
			case 1:
				SpaceHole[] fila1 = board.getFila1();
				if(fila1[play.getNewPosition()].equals(SpaceHole.EMPTY)) {
					if(currentPlayer.getCurrentPiece() == 0) {
						if(checkLastPosition(play, board)) {
							fila1[play.getNewPosition()] = SpaceHole.findSpaceHoleByValue(play.getType().getValue());
							board.setFila1(fila1);
						}
					} else {
						fila1[play.getNewPosition()] = SpaceHole.findSpaceHoleByValue(play.getType().getValue());
						board.setFila1(fila1);
						currentPlayer.setCurrentPiece(currentPlayer.getCurrentPiece() - 1);
					}
				}
				break;
			case 2:
				SpaceHole[] fila2 = board.getFila2();
				if(fila2[play.getNewPosition()].equals(SpaceHole.EMPTY)) {
					if(currentPlayer.getCurrentPiece() == 0) {
						if(checkLastPosition(play, board)) {
							fila2[play.getNewPosition()] = SpaceHole.findSpaceHoleByValue(play.getType().getValue());
							board.setFila2(fila2);
						}
					} else {
						fila2[play.getNewPosition()] = SpaceHole.findSpaceHoleByValue(play.getType().getValue());
						board.setFila2(fila2);
						currentPlayer.setCurrentPiece(currentPlayer.getCurrentPiece() - 1);
					}
				}
				break;
			default:
				throw new InvalidGameException("Não há esse espaço de peça em jogo");
		}
		
	}


	private Boolean checkLastPosition(GamePlay play, Board board) {
		if(play.getLastFlag() == 0) {
			board.setCima(SpaceHole.EMPTY);
			return true;
		}
		
		//Quer dizer que estão na mesma linha
		if(play.getLastFlag().equals(play.getNewFlag())) {
			clearSpace(play, board);
			return true;
		} else {
			//Quer dizer que estão na mesma coluna
			if(play.getLastPosition().equals(play.getNewPosition())) {
				clearSpace(play, board);
				return true;
			}
		}
		
		return false;
	}
	
	
	private void clearSpace(GamePlay play, Board board) {
		if(play.getLastFlag().equals(1)) {
			SpaceHole[] fila1 = board.getFila1();
			fila1[play.getNewPosition()] = SpaceHole.EMPTY;
			board.setFila2(fila1);
		} else {
			SpaceHole[] fila2 = board.getFila2();
			fila2[play.getNewPosition()] = SpaceHole.EMPTY;
			board.setFila2(fila2);
		}
	}
	

	private PlayerType checkWinner(Board board) {
		SpaceHole cima = board.getCima();
		SpaceHole[] fila1 = board.getFila1();
		SpaceHole[] fila2 = board.getFila2();
		
		if(cima.equals(SpaceHole.EMPTY)) {
			
			if(!fila1[0].equals(SpaceHole.EMPTY) && fila1[0].equals(fila1[1]) && fila1[1].equals(fila1[2])) {
				
				return PlayerType.findPlayerTypeByValue(fila1[0].getValue());
			} else if(!fila2[0].equals(SpaceHole.EMPTY) 
					&& fila2[0].equals(fila2[1]) 
					&& fila2[1].equals(fila2[2])) {
				return PlayerType.findPlayerTypeByValue(fila2[0].getValue());
			}
		} else {
			for(int i = 0; i < 3; i++) {
				if(cima.equals(fila1[i]) && cima.equals(fila2[i])) {
					return PlayerType.findPlayerTypeByValue(cima.getValue());
				}
			}
		}
		return null;
	}
}
