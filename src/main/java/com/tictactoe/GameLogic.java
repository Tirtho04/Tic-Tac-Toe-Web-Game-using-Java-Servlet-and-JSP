package com.tictactoe;

public class GameLogic implements java.io.Serializable {

    // Fields
    private char[][] board;
    private char currentPlayer;    // The player whose turn it currently is: 'X' or 'O'. X always starts
    private int movecount;         // Total number of valid moves made in this game. Used for draw detection.
    private char winner;           // 'X', 'O', or ' ' (no winner yet)
    private int[][] winningCells;  // Stores 3 coordinates [row, col] of the winning line, or null

    public GameLogic() {
        board = new char[3][3];
        currentPlayer = 'X';
        movecount = 0;
        winner = ' ';
        winningCells = null;
        // Fill every cell with a space character (empty)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                board[row][col] = ' ';
            }
        }
    }

    /**
     * Executes a move on the board for the current player.
     * Checks for a win or draw condition immediately.
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
        } else if (movecount < 9) {
            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
        }

        return true;
    }

    /**
     * Checks all 8 possible winning lines (3 horizontal, 3 vertical, 2 diagonal).
     * Populates winningCells with the 3 coordinates if a win occurs.
     * @return 'X' or 'O' if a player has won, or ' ' if no winner yet.
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

    public char winner() {
        return winner;
    }

    public char getwinner() {
        return winner;
    }

    public static void main(String[] args) {
        System.out.println("=== Running Week 6 Tests ===");

        // Test 1: Horizontal Row Win (Row 0) + Winning Cell Check
        GameLogic g1 = new GameLogic();
        g1.makeMove(0, 0); // X
        g1.makeMove(1, 0); // O
        g1.makeMove(0, 1); // X
        g1.makeMove(1, 1); // O
        g1.makeMove(0, 2); // X (Row 0 complete)
        System.out.println("Test 1 (Row Win): Winner=" + g1.getWinner() + " [Expected: X]");
        System.out.println("Test 1 (isWinningCell(0,0)): " + g1.isWinningCell(0, 0) + " [Expected: true]");
        System.out.println("Test 1 (isWinningCell(1,0)): " + g1.isWinningCell(1, 0) + " [Expected: false]");

        // Test 2: Reset behavior
        g1.resetBoard();
        System.out.println("Test 2 (After Reset): Winner='" + g1.getWinner() + "', WinningCells=" + (g1.getWinningCells() == null) + " [Expected: true], CurrentPlayer=" + g1.getCurrentPlayer() + " [Expected: X]");
    }
}
