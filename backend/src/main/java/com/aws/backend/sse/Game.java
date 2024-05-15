package com.aws.backend.sse;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Game {
	static final AtomicLong gameIdGenerator = new AtomicLong(0);
	final String firstPlayer;
	final String secondPlayer;
	String currentPlayer;
	Move allowedCurrentMove = Move.X;
	Cell[][] board = new Cell[3][3];
	long gameId;
	Consumer<GameService.GameEvent> gameEventConsumer;

	public Game(String firstPlayer, String secondPlayer, Consumer<GameService.GameEvent> gameEventConsumer) {
		this.firstPlayer = firstPlayer;
		this.secondPlayer = secondPlayer;
		this.currentPlayer = new Random().nextBoolean() ? firstPlayer : secondPlayer;
		this.gameEventConsumer = gameEventConsumer;
		initGame();
	}

	private void initGame() {
		for (Cell[] row : board) {
			Arrays.fill(row, Cell.EMPTY);
		}
		currentPlayer = new Random().nextBoolean() ? firstPlayer : secondPlayer;
		gameId = gameIdGenerator.incrementAndGet();
		gameEventConsumer.accept(new GameService.GameEvent(GameService.GameStatus.GAME_STARTED, board, null, currentPlayer, gameId));
	}


	public void makeMove(int x, int y, String playerName) {
		validateMove(x, y, playerName);
		board[x][y] = allowedCurrentMove == Move.X ? Cell.X : Cell.O;
		processMoveOutcome(playerName);
	}


	private void validateMove(int x, int y, String playerName) {
		if (!playerName.equals(currentPlayer)) {
			throw new GameException("It's not your turn");
		}
		if (board[x][y] != Cell.EMPTY) {
			throw new GameException("Cell is already occupied");
		}
		if (x > 2 || y > 2) {
			throw new GameException("Invalid cell");
		}
	}

	private void processMoveOutcome(String playerName) {
		if (isWinningMove()) {
			gameEventConsumer.accept(new GameService.GameEvent(GameService.GameStatus.GAME_ENDED, board, playerName, currentPlayer, gameId));
			return;
		}

		if (isBoardFull()) {
			gameEventConsumer.accept(new GameService.GameEvent(GameService.GameStatus.GAME_ENDED, board, null, currentPlayer, gameId));
			return;
		}

		allowedCurrentMove = allowedCurrentMove == Move.X ? Move.O : Move.X;
		currentPlayer = currentPlayer.equals(firstPlayer) ? secondPlayer : firstPlayer;
		gameEventConsumer.accept(new GameService.GameEvent(GameService.GameStatus.GAME_UPDATED, board, null, currentPlayer, gameId));
	}

	public boolean isWinningMove() {
		return checkRows() || checkColumns() || checkDiagonals();
	}

	private boolean checkRows() {
		for (int i = 0; i < 3; i++) {
			if (board[i][0] != Cell.EMPTY && board[i][0] == board[i][1] && board[i][0] == board[i][2]) {
				return true;
			}
		}
		return false;
	}

	private boolean checkColumns() {
		for (int i = 0; i < 3; i++) {
			if (board[0][i] != Cell.EMPTY && board[0][i] == board[1][i] && board[0][i] == board[2][i]) {
				return true;
			}
		}
		return false;
	}

	private boolean checkDiagonals() {
		if (board[0][0] != Cell.EMPTY && board[0][0] == board[1][1] && board[0][0] == board[2][2]) {
			return true;
		}
		return board[0][2] != Cell.EMPTY && board[0][2] == board[1][1] && board[0][2] == board[2][0];
	}

	private boolean isBoardFull() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (board[i][j] == Cell.EMPTY) {
					return false;
				}
			}
		}
		return true;
	}

	enum Move {X, O}

	public enum Cell {EMPTY, X, O}

	static class GameException extends RuntimeException {
		public GameException(String message) {
			super(message);
		}
	}
}
