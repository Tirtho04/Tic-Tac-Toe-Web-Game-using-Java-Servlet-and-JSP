package com.tictactoe;

public class GameLogic implements java.io.Serializable {

    // Board state fields
    private char[][] board;
    private char currentPlayer;    // The player whose turn it currently is: 'X' or 'O'. X always starts
    private int movecount;         // Total number of valid moves made in this game. Used for draw detection.
    private char winner;           // 'X', 'O', or ' ' (no winner yet)
    private int[][] winningCells;  // Stores 3 coordinates [row, col] of the winning line, or null

    // Match score tracker fields (persists across rounds in session)
    private int xWins;
    private int oWins;
    private int draws;
    private boolean scoreUpdated;  // Prevents multiple increments in the same round

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

        // Fill every cell with a space character (empty)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                board[row][col] = ' ';
            }
        }
    }

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

    public void resetScores() {
        xWins = 0;
        oWins = 0;
        draws = 0;
        scoreUpdated = false;
    }

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

    public int[][] getWinningCells() {
        return winningCells;
    }

    public boolean isDraw() {
        return (movecount == 9 && winner == ' ');
    }

    public boolean isGameOver() {
        return (winner != ' ' || isDraw());
    }

    public char[][] getBoard() {
        return board;
    }

    public char getCurrentPlayer() {
        return currentPlayer;
    }

    public int getMoveCount() {
        return movecount;
    }

    public char getWinner() {
        return winner;
    }

    public int getXWins() {
        return xWins;
    }

    public int getOWins() {
        return oWins;
    }

    public int getDraws() {
        return draws;
    }

    public static void main(String[] args) {
        System.out.println("=== Running Week 7 Tests ===");

        GameLogic game = new GameLogic();

        // Round 1: X wins
        game.makeMove(0, 0); // X
        game.makeMove(1, 0); // O
        game.makeMove(0, 1); // X
        game.makeMove(1, 1); // O
        game.makeMove(0, 2); // X
        System.out.println("Round 1 (X Win) -> X: " + game.getXWins() + " [Expected: 1], O: " + game.getOWins() + " [Expected: 0], Draws: " + game.getDraws() + " [Expected: 0]");

        // Round 2: New Round (preserve score)
        game.resetBoard();
        game.makeMove(0, 0); // X
        game.makeMove(0, 1); // O
        game.makeMove(2, 2); // X
        game.makeMove(1, 1); // O
        game.makeMove(2, 0); // X
        game.makeMove(2, 1); // O (Col 1 complete)
        System.out.println("Round 2 (O Win) -> X: " + game.getXWins() + " [Expected: 1], O: " + game.getOWins() + " [Expected: 1], Draws: " + game.getDraws() + " [Expected: 0]");

        // Round 3: Reset Scores
        game.resetScores();
        System.out.println("After Reset Scores -> X: " + game.getXWins() + " [Expected: 0], O: " + game.getOWins() + " [Expected: 0], Draws: " + game.getDraws() + " [Expected: 0]");
    }
}
