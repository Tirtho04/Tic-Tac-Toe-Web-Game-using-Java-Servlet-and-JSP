package com.tictactoe;

import java.io.Serializable;

/**
 * Model component representing the core state and rules of a Tic-Tac-Toe game.
 * <p>
 * Manages the 3&times;3 board grid, validates move coordinates, tracks player turns ('X' and 'O'),
 * detects 8-line win conditions and draws, identifies winning cell coordinates for UI highlighting,
 * and maintains session-persistent match scores across multiple rounds.
 * </p>
 *
 * @author Tirtho04
 * @version 1.0
 */
public class GameLogic implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 3x3 character grid representing the game board (' ', 'X', or 'O'). */
    private char[][] board;

    /** The player whose turn it currently is: 'X' (starts first) or 'O'. */
    private char currentPlayer;

    /** Total number of valid moves made in the current round (0 to 9). */
    private int movecount;

    /** Current winner of the round: 'X', 'O', or ' ' (no winner yet / draw). */
    private char winner;

    /** Stores the [row, col] coordinates of the 3 winning cells, or null if no winner. */
    private int[][] winningCells;

    /** Cumulative number of rounds won by Player X in this session. */
    private int xWins;

    /** Cumulative number of rounds won by Player O in this session. */
    private int oWins;

    /** Cumulative number of tied rounds in this session. */
    private int draws;

    /** Flag preventing duplicate score increments within the same finished round. */
    private boolean scoreUpdated;

    /**
     * Constructs a new {@code GameLogic} instance initialized with an empty 3&times;3 board,
     * player 'X' starting first, zero move count, and match score counters set to 0.
     */
    public GameLogic() {
        board = new char[3][3];
        currentPlayer = 'X';
        movecount = 0;
        winner = ' ';
        winningCells = null;
        xWins = 0;
        oWins = 0;
        draws = 0;
        scoreUpdated = false;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                board[row][col] = ' ';
            }
        }
    }

    /**
     * Attempts to place the current player's mark at the specified grid cell.
     * <p>
     * If the move is valid (cell within bounds [0..2], cell empty, and game not over),
     * the cell is marked, move count is incremented, win/draw status is evaluated,
     * match scores are updated if the round concludes, and the turn is toggled.
     * </p>
     *
     * @param row row index (0 to 2)
     * @param col column index (0 to 2)
     * @return {@code true} if the move was valid and applied; {@code false} otherwise
     */
    public boolean makeMove(int row, int col) {
        if (isGameOver()) {
            return false;
        }
        if (row < 0 || row > 2 || col < 0 || col > 2) {
            return false;
        }
        if (board[row][col] != ' ') {
            return false;
        }

        board[row][col] = currentPlayer;
        movecount++;

        char win = checkWinner();
        if (win != ' ') {
            winner = win;
            if (!scoreUpdated) {
                if (win == 'X') {
                    xWins++;
                } else if (win == 'O') {
                    oWins++;
                }
                scoreUpdated = true;
            }
        } else if (movecount == 9) {
            if (!scoreUpdated) {
                draws++;
                scoreUpdated = true;
            }
        } else {
            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
        }

        return true;
    }

    /**
     * Evaluates the board across all 8 possible winning combinations (3 rows, 3 columns, 2 diagonals).
     * <p>
     * When a winning line is found, stores the 3 winning cell coordinates into {@code winningCells}
     * and returns the winning mark ('X' or 'O'). If no win exists, returns ' '.
     * </p>
     *
     * @return 'X' if Player X won, 'O' if Player O won, or ' ' if no winner
     */
    public char checkWinner() {
        // 1. Check Rows
        for (int r = 0; r < 3; r++) {
            if (board[r][0] != ' ' && board[r][0] == board[r][1] && board[r][1] == board[r][2]) {
                winningCells = new int[][]{{r, 0}, {r, 1}, {r, 2}};
                return board[r][0];
            }
        }

        // 2. Check Columns
        for (int c = 0; c < 3; c++) {
            if (board[0][c] != ' ' && board[0][c] == board[1][c] && board[1][c] == board[2][c]) {
                winningCells = new int[][]{{0, c}, {1, c}, {2, c}};
                return board[0][c];
            }
        }

        // 3. Check Main Diagonal (Top-Left to Bottom-Right)
        if (board[0][0] != ' ' && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            winningCells = new int[][]{{0, 0}, {1, 1}, {2, 2}};
            return board[0][0];
        }

        // 4. Check Anti-Diagonal (Top-Right to Bottom-Left)
        if (board[0][2] != ' ' && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            winningCells = new int[][]{{0, 2}, {1, 1}, {2, 0}};
            return board[0][2];
        }

        winningCells = null;
        return ' ';
    }

    /**
     * Resets the board for a new round while preserving the cumulative match scores.
     * Clears all 9 cells to ' ', resets turn to 'X', zeroes the move count,
     * clears winner and winning cells, and resets the round score update guard.
     */
    public void resetBoard() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                board[row][col] = ' ';
            }
        }
        currentPlayer = 'X';
        movecount = 0;
        winner = ' ';
        winningCells = null;
        scoreUpdated = false;
    }

    /**
     * Resets all cumulative match statistics (Player X wins, Player O wins, Ties) back to 0.
     */
    public void resetScores() {
        xWins = 0;
        oWins = 0;
        draws = 0;
        scoreUpdated = false;
    }

    /**
     * Checks if a given cell at (row, col) is part of the winning 3-in-a-row line.
     *
     * @param row the row index (0 to 2)
     * @param col the column index (0 to 2)
     * @return {@code true} if the cell is in the winning line; {@code false} otherwise
     */
    public boolean isWinningCell(int row, int col) {
        if (winningCells == null) {
            return false;
        }
        for (int[] cell : winningCells) {
            if (cell[0] == row && cell[1] == col) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the coordinates of the winning line cells.
     *
     * @return 2D array of [row, col] coordinates, or {@code null} if no winner
     */
    public int[][] getWinningCells() {
        return winningCells;
    }

    /**
     * Determines if the current round has concluded in a draw.
     *
     * @return {@code true} if 9 valid moves were made and no player won; {@code false} otherwise
     */
    public boolean isDraw() {
        return (movecount == 9 && winner == ' ');
    }

    /**
     * Determines if the current round is over (either won or drawn).
     *
     * @return {@code true} if a player has won or if all 9 cells are filled
     */
    public boolean isGameOver() {
        return (winner != ' ' || isDraw());
    }

    /**
     * Returns the 3&times;3 board grid state.
     *
     * @return 2D char array representing the board
     */
    public char[][] getBoard() {
        return board;
    }

    /**
     * Returns the mark of the player whose turn it currently is.
     *
     * @return 'X' or 'O'
     */
    public char getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Returns the total number of moves made in the current round.
     *
     * @return move count between 0 and 9
     */
    public int getMoveCount() {
        return movecount;
    }

    /**
     * Returns the winner of the current round.
     *
     * @return 'X', 'O', or ' ' if game is ongoing or tied
     */
    public char getWinner() {
        return winner;
    }

    /**
     * Returns the cumulative total of rounds won by Player X in this session.
     *
     * @return Player X win count
     */
    public int getXWins() {
        return xWins;
    }

    /**
     * Returns the cumulative total of rounds won by Player O in this session.
     *
     * @return Player O win count
     */
    public int getOWins() {
        return oWins;
    }

    /**
     * Returns the cumulative total of tied rounds in this session.
     *
     * @return tie count
     */
    public int getDraws() {
        return draws;
    }
}
