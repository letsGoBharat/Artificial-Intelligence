
// javac *.java && java Main init verbose < pipe | java Main > pipe
// SUBMISSION: 

import java.util.*;

public class Player {

    private final int MAX_DEPTH = 8;
    private final int OPPONENT = Constants.CELL_RED;
    private final int MY_PLAYER = Constants.CELL_WHITE;
    private final int KING = Constants.CELL_KING;
    private final int INIT_ALPHA = Integer.MIN_VALUE;
    private final int INIT_BETA = Integer.MAX_VALUE;
    private int currentPlayer;

    private HashMap<String, Integer> states = new HashMap<String, Integer>();


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

        // Get the current player
        if (gameState.getNextPlayer() == OPPONENT) {
            currentPlayer = MY_PLAYER;
        } else {
            currentPlayer = OPPONENT;
        }

        // Get the minmax
        int max_score = Integer.MIN_VALUE;
        int max_ind = 0;
        int currScore = 0;
        for (int depth = 4; depth < MAX_DEPTH && deadline.timeUntil() > 5e7; depth++) {
        	for (int i = 0; i < nextStates.size(); i++) {
                currScore = alphaBeta(nextStates.get(i), depth, INIT_ALPHA, INIT_BETA, currentPlayer, deadline);
                if (currScore > max_score) {
                    max_score = currScore;
                    max_ind = i;
                }
            }
        }
        
        return nextStates.elementAt(max_ind);
    }

    public int alphaBeta(GameState state, int depth, int alpha, int beta, int player, Deadline deadline) {
        Vector<GameState> nextStates2 = new Vector<GameState>();
        state.findPossibleMoves(nextStates2);
        int v = 0;
        GameState child;

        // Terminal state or end of game
        if ((depth <= 0) || (state.isEOG()) || deadline.timeUntil()<5e7) {
            v = gamma(player, state);

            // Player RED
        } else if (player == OPPONENT) {
            v = Integer.MIN_VALUE;
            for (int i = 0; i < nextStates2.size(); i++) {

                // Create the child
                child = nextStates2.get(i);
                if(states.containsKey(child.toMessage().substring(0, 32))) {
                    alpha = states.get(child.toMessage().substring(0, 32));
                } else {
                    v = Math.max(v, alphaBeta(child, (depth - 1), alpha, beta, MY_PLAYER, deadline));
                    alpha = Math.max(alpha, v);
                    states.put(child.toMessage().substring(0, 32), alpha);
                }
                
                // Beta prune
                if (beta <= alpha) {
                    break;
                }

            }

            // Player WHITE
        } else if (player == MY_PLAYER) {
            v = Integer.MAX_VALUE;
            for (int i = 0; i < nextStates2.size(); i++) {
                
                // Create the child
                child = nextStates2.get(i);
                if(states.containsKey(child.toMessage().substring(0, 32))) {
                    beta = states.get(child.toMessage().substring(0, 32));
                } else {
                    v = Math.min(v, alphaBeta(child, (depth - 1), alpha, beta, OPPONENT, deadline));
                    beta = Math.min(beta, v);
                    states.put(child.toMessage().substring(0, 32), beta);
                }
                
                // Alpha prune
                if (beta <= alpha) {
                    break;
                }
            }
        }

        return v;
    }

    public int gamma(int player, GameState state) {
        
        int[][] board = new int[GameState.NUMBER_OF_SQUARES][GameState.NUMBER_OF_SQUARES];
        int score = 0;

        if(state.isEOG()) {
            if((state.isRedWin() && player == Constants.CELL_RED) || (state.isWhiteWin() && player == Constants.CELL_WHITE)) {
                return Integer.MAX_VALUE;
            } else if((state.isWhiteWin() && player == Constants.CELL_RED) || (state.isRedWin() && player == Constants.CELL_WHITE)) {
                return Integer.MIN_VALUE;
            } 
        }
        
        // Build matrix
        for (int i = 0; i < GameState.NUMBER_OF_SQUARES; i++) {
            for (int j = 0; j < GameState.NUMBER_OF_SQUARES; j++) {
                board[i][j] = state.get(i, j);
            }
        }

        // Rows
        for (int i = 0; i < GameState.NUMBER_OF_SQUARES; i++) {
            for (int j = 0; j < GameState.NUMBER_OF_SQUARES; j++) {
                if (board[i][j] == MY_PLAYER) {
                    score += 100;
                } else if (board[i][j] == OPPONENT) {
                    score -= 50;
                }
            }
        }

        // Columns
        for (int i = 0; i < GameState.NUMBER_OF_SQUARES; i++) {
            for (int j = 0; j < GameState.NUMBER_OF_SQUARES; j++) {
                if (board[j][i] == MY_PLAYER) {
                    score += 100;
                } else if (board[j][i] == OPPONENT) {
                    score -= 50;
                }
            }
        }

        // Diagonals
        for (int i = 0; i < GameState.NUMBER_OF_SQUARES; i++) {
            if (board[i][i] == MY_PLAYER) {
                score += 100;
            } else if (board[i][i] == OPPONENT) {
                score -= 50;
            }
        }
        for (int i = 0; i < GameState.NUMBER_OF_SQUARES; i++) {
            if (board[i][GameState.NUMBER_OF_SQUARES - 1 - i] == MY_PLAYER) {
                score += 100;
            } else if (board[i][GameState.NUMBER_OF_SQUARES - 1 - i] == OPPONENT) {
                score -= 50;
            }
        }

        return score;

    }
}