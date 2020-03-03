
// javac *.java && java Main init verbose < pipe | java Main > pipe
// Submission id  : 4629697

import java.util.*;

public class Player {

	private final int MAX_DEPTH = 3;
	private final int A = Constants.CELL_X;
	private final int B = Constants.CELL_O;
	private final int ALPHA = Integer.MIN_VALUE;
	private final int BETA = Integer.MAX_VALUE;
	public static int PLAYER;
	public static int CURRENT_PLAYER;

	private GameState bestState = new GameState();

	private static final int[][] WINNING_STATES = { { 0, 1, 2, 3 }, { 4, 5, 6, 7 }, { 8, 9, 10, 11 },
			{ 12, 13, 14, 15 }, { 16, 17, 18, 19 }, { 20, 21, 22, 23 }, { 24, 25, 26, 27 }, { 28, 29, 30, 31 },
			{ 32, 33, 34, 35 }, { 36, 37, 38, 39 }, { 40, 41, 42, 43 }, { 44, 45, 46, 47 }, { 48, 49, 50, 51 },
			{ 52, 53, 54, 55 }, { 56, 57, 58, 59 }, { 60, 61, 62, 63 }, { 0, 4, 8, 12 }, { 1, 5, 9, 13 },
			{ 2, 6, 10, 14 }, { 3, 7, 11, 15 }, { 16, 20, 24, 28 }, { 17, 21, 25, 29 }, { 18, 22, 26, 30 },
			{ 19, 23, 27, 31 }, { 32, 36, 40, 44 }, { 33, 37, 41, 45 }, { 34, 38, 42, 46 }, { 35, 39, 43, 47 },
			{ 48, 52, 56, 60 }, { 49, 53, 57, 61 }, { 50, 54, 58, 62 }, { 51, 55, 59, 63 }, { 3, 6, 9, 12 },
			{ 19, 22, 25, 28 }, { 35, 38, 41, 44 }, { 51, 54, 57, 60 }, { 0, 5, 10, 15 }, { 16, 21, 26, 31 },
			{ 32, 37, 42, 47 }, { 48, 53, 58, 63 }, { 0, 17, 34, 51 }, { 4, 21, 38, 55 }, { 8, 25, 42, 59 },
			{ 12, 29, 46, 63 }, { 3, 18, 33, 48 }, { 7, 22, 37, 52 }, { 11, 26, 41, 56 }, { 15, 30, 45, 60 },
			{ 0, 16, 32, 48 }, { 1, 17, 33, 49 }, { 2, 18, 34, 50 }, { 3, 19, 35, 51 }, { 4, 20, 36, 52 },
			{ 5, 21, 37, 53 }, { 6, 22, 38, 54 }, { 7, 23, 39, 55 }, { 8, 24, 40, 56 }, { 9, 25, 41, 57 },
			{ 10, 26, 42, 58 }, { 11, 27, 43, 59 }, { 12, 28, 44, 60 }, { 13, 29, 45, 61 }, { 14, 30, 46, 62 },
			{ 15, 31, 47, 63 }, { 0, 20, 40, 60 }, { 1, 21, 41, 61 }, { 2, 22, 42, 62 }, { 3, 23, 43, 63 },
			{ 12, 24, 36, 48 }, { 13, 25, 37, 49 }, { 14, 26, 38, 50 }, { 15, 27, 39, 51 }, { 51, 38, 25, 12 },
			{ 48, 37, 26, 15 }, { 3, 22, 41, 60 }, { 0, 21, 42, 63 }

	};

	private static int[][] HURESTIC = { { 0, -10, -100, -1000, -10000 }, { 10, 0, 0, 0, 0 }, {100, 0, 0, 0, 0 }, {1000, 0, 0, 0, 0 }, {10000, 0, 0, 0, 0 } };

	/**
	 * Performs a move
	 *
	 * @param gameState
	 *            the current state of the board
	 * @param deadline
	 *            time before which we must have returned
	 * @return the next state the board is in after our move
	 */
	public GameState play(final GameState gameState, final Deadline deadline) {
		Vector<GameState> nextStates = new Vector<GameState>();
		gameState.findPossibleMoves(nextStates);

		if (nextStates.size() == 0) {
			// Must play "pass" move if there are no other moves possible.
			return new GameState(gameState, new Move());
		}
		
		PLAYER = gameState.getNextPlayer();
    	CURRENT_PLAYER = PLAYER == 1 ? 2 : 1;

		/**
		 * Here you should write your algorithms to get the best next move, i.e.
		 * the best next state. This skeleton returns a random move instead.
		 */

		int currScore = alphaBeta(gameState, MAX_DEPTH, ALPHA, BETA, PLAYER);

		return bestState;
	}

	public int alphaBeta(GameState state, int depth, int alpha, int beta, int player) {
		int v = 0;
		int maxVal = 0;
		GameState maxMove = new GameState();

		// Terminal state or end of game
		if ((depth == 0) || (state.isEOG())) {
			if (state.isXWin()) {
				return Integer.MAX_VALUE;
			} else if (state.isOWin()) {
				return Integer.MIN_VALUE;
			} else {
				return gamma(state);
			}

			
		} else {
			Vector<GameState> localNextStates = new Vector<GameState>();
			state.findPossibleMoves(localNextStates);
			// Player A (X)
			if (player == PLAYER) {
				maxVal = Integer.MIN_VALUE;
				v = Integer.MIN_VALUE;
				for (int i = 0; i < localNextStates.size(); i++) {
					// Create the child
					v = Math.max(v, alphaBeta(localNextStates.get(i), (depth - 1), alpha, beta, CURRENT_PLAYER));
					alpha = Math.max(alpha, v);

					if (maxVal < v) {
						maxVal = v;
						maxMove = localNextStates.get(i);
					}

					// Beta prune
					if (beta <= alpha) {
						break;
					}
				}
				bestState = maxMove;

				// Player B (O)
			} else {
				maxVal = Integer.MAX_VALUE;
				v = Integer.MAX_VALUE;
				for (int i = 0; i < localNextStates.size(); i++) {

					// Create the child
					v = Math.min(v, alphaBeta(localNextStates.get(i), (depth - 1), alpha, beta, PLAYER));
					beta = Math.min(beta, v);

					if (maxVal > v) {
						maxVal = v;
						maxMove = localNextStates.get(i);
					}

					// Alpha prune
					if (beta <= alpha) {
						break;
					}
				}
				bestState = maxMove;
			}
		}
		return maxVal;
	}

	public int gamma(GameState state) {
		int xCount;
		int oCount;
		int score = 0;

		for (int i = 0; i < WINNING_STATES.length; i++) {
			xCount = 0;
			oCount = 0;
			for (int j = 0; j < WINNING_STATES[0].length; j++) {
				if (state.at(WINNING_STATES[i][j]) == A) {
					xCount++;
				} else if (state.at(WINNING_STATES[i][j]) == B) {
					oCount++;
				}
			}
			score += HURESTIC[xCount][oCount];
		}
		return score;

	}
}
