package com.aws.backend.model;

import lombok.Data;

import java.util.Objects;
import java.util.UUID;

@Data
public class TicTacToe {
	private String gameId;
	private String[][] board;
	private String player1;
	private String player2;
	private String winner;
	private String turn;
	private GameState gameState;

	public TicTacToe(String player1, String player2) {
		this.gameId = UUID.randomUUID().toString();
		this.player1 = player1;
		this.player2 = player2;
		this.turn = player1;
		this.board = new String[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				this.board[i][j] = " ";
			}
		}
		gameState = GameState.WAITING_FOR_PLAYER;
	}

	/**
	 * Makes a move in the specified position on the board.
	 *
	 * @param player the name of the player making the move
	 * @param move   the position of the move
	 */
	public void makeMove(String player, int move) {
		int row = move / 3;
		int col = move % 3;
		if (Objects.equals(board[row][col], " ")) {
			board[row][col] = Objects.equals(player, player1) ? "X" : "O";
			turn = player.equals(player1) ? player2 : player1;
			checkWinner();
			updateGameState();
		}
	}

	/**
	 * Check if there is a winner. If a winning combination is found,
	 * the winner is set to the corresponding player.
	 */
	private void checkWinner() {
		// Check rows
		for (int i = 0; i < 3; i++) {
			if (Objects.equals(board[i][0], board[i][1]) && Objects.equals(board[i][0], board[i][2])) {
				if (!Objects.equals(board[i][0], " ")) {
					setWinner(Objects.equals(board[i][0], player1) ? player1 : player2);
					return;
				}
			}
		}

		// Check columns
		for (int i = 0; i < 3; i++) {
			if (Objects.equals(board[0][i], board[1][i]) && Objects.equals(board[0][i], board[2][i])) {
				if (!Objects.equals(board[0][i], " ")) {
					setWinner(Objects.equals(board[0][i], player1) ? player1 : player2);
					return;
				}
			}
		}

		// Check diagonal (top-left to bottom-right)
		if (Objects.equals(board[0][0], board[1][1]) && Objects.equals(board[0][0], board[2][2])) {
			if (!Objects.equals(board[0][0], " ")) {
				setWinner(Objects.equals(board[0][0], player1) ? player1 : player2);
				return;
			}
		}

		// Check diagonal (top-right to bottom-left)
		if (Objects.equals(board[0][2], board[1][1]) && Objects.equals(board[0][2], board[2][0])) {
			if (!Objects.equals(board[0][2], " ")) {
				setWinner(Objects.equals(board[0][2], player1) ? player1 : player2);
				return;
			}
		}
	}



	private void updateGameState() {
		if (winner != null) {
			gameState = winner.equals(player1) ? GameState.PLAYER1_WON : GameState.PLAYER2_WON;
		} else if (isBoardFull()) {
			gameState = GameState.TIE;
		} else {
			gameState = turn.equals(player1) ? GameState.PLAYER1_TURN : GameState.PLAYER2_TURN;
		}
	}

	private boolean isBoardFull() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (Objects.equals(board[i][j], " ")) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean isGameOver() {
		return winner != null || isBoardFull();
	}
}
