
// javac *.java && java Main init verbose < pipe | java Main > pipe
// SUBMISSION: 4621602

import java.util.*;

public class Player {

	private final int MAX_DEPTH = 6;
	private final int A = Constants.CELL_X;
	private final int B = Constants.CELL_O;
	private final int INIT_ALPHA = Integer.MIN_VALUE;
	private final int INIT_BETA = Integer.MAX_VALUE;
	private int currentPlayer;

	// static HashMap<String, Integer> states;

//	Vector<GameState> nextStates;

	/**
	 * Performs a move
	 *
	 * @param gameState the current state of the board
	 * @param deadline  time before which we must have returned
	 * @return the next state the board is in after our move
	 */
	public GameState play(final GameState gameState, final Deadline deadline) {
		Vector<GameState> nextStates = new Vector<GameState>();
		gameState.findPossibleMoves(nextStates);

		if (nextStates.size() == 0) {
			// Must play "pass" move if there are no other moves possible.
			return new GameState(gameState, new Move());
		}

		/**
		 * Here you should write your algorithms to get the best next move, i.e. the
		 * best next state. This skeleton returns a random move instead.
		 */
		// Random random = new Random();
		// return nextStates.elementAt(random.nextInt(nextStates.size()));

		// Check hashes
		// if(states.containsKey(gameState.toMessage()){
		// return states.get(gameState.toMessage());
		// }

		// Get the current player
//        int currentPlayer;
		if (gameState.getNextPlayer() == A) {
			currentPlayer = B;
		} else {
			currentPlayer = A;
		}

		// Get the minmax
		int max_score = Integer.MIN_VALUE;
		int max_ind = 0;
		int currScore = 0;
		for (int i = 0; i < nextStates.size(); i++) {
			currScore = alphaBeta(nextStates.get(i), MAX_DEPTH, INIT_ALPHA, INIT_BETA, currentPlayer);
			if (currScore > max_score) {
				max_score = currScore;
				max_ind = i;
			}
		}
		return nextStates.elementAt(max_ind);
	}

	public int alphaBeta(GameState state, int depth, int alpha, int beta, int player) {
		Vector<GameState> nextStates2 = new Vector<GameState>();
		state.findPossibleMoves(nextStates2);
		int v = 0;
		GameState child;

		// Terminal state or end of game
		if ((depth == 0) || (state.isEOG())) {
			v = gamma(player, state);

			// Player A (X)
		} else if (player == A) {
			v = Integer.MIN_VALUE;
			for (int i = 0; i < nextStates2.size(); i++) {

				// Create the child
				child = nextStates2.get(i);
				v = Math.max(v, alphaBeta(child, (depth - 1), alpha, beta, B));
				alpha = Math.max(alpha, v);

				// Beta prune
				if (beta <= alpha) {
					break;
				}

			}

			// Player B (O)
		} else if (player == B) {
			v = Integer.MAX_VALUE;
			for (int i = 0; i < nextStates2.size(); i++) {

				// Create the child
				child = nextStates2.get(i);
				v = Math.min(v, alphaBeta(child, (depth - 1), alpha, beta, A));
				beta = Math.min(beta, v);

				// Alpha prune
				if (beta <= alpha) {
					break;
				}
			}
		}

		return v;
	}

	public int gamma(int player, GameState state) {

		int[][] board = new int[GameState.BOARD_SIZE][GameState.BOARD_SIZE];
		int score = 0;

		if(state.isEOG()) {
			if(state.isXWin()) {
				return 100;
			} else if (state.isOWin()) {
				return -100;
			}
		}
		
		// Build matrix
		for (int i = 0; i < GameState.BOARD_SIZE; i++) {
			for (int j = 0; j < GameState.BOARD_SIZE; j++) {
				board[i][j] = state.at(i, j);
			}
		}

		// Rows
		for (int i = 0; i < GameState.BOARD_SIZE; i++) {
			for (int j = 0; j < GameState.BOARD_SIZE; j++) {
				if (board[i][j] == A) {
					score += 5;
				} else if (board[i][j] == B) {
					score -= 5;
				}
			}
		}

		// Columns
		for (int i = 0; i < GameState.BOARD_SIZE; i++) {
			for (int j = 0; j < GameState.BOARD_SIZE; j++) {
				if (board[j][i] == A) {
					score += 5;
				} else if (board[j][i] == B) {
					score -= 5;
				}
			}
		}

		// Diagonals
		for (int i = 0; i < GameState.BOARD_SIZE; i++) {
			if (board[i][i] == A) {
				score += 5;
			} else if (board[i][i] == B) {
				score -= 5;
			}
		}
		for (int i = 0; i < GameState.BOARD_SIZE; i++) {
			if (board[i][GameState.BOARD_SIZE - 1 - i] == A) {
				score += 5;
			} else if (board[i][GameState.BOARD_SIZE - 1 - i] == B) {
				score -= 5;
			}
		}

		return score;

	}
}
