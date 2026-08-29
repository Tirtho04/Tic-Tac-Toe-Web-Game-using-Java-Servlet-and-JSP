package com.tictactoe;

public class GameLogic implements java.io.Serializable {

    // Fields
    private char[][] board;
    private char currentPlayer;    // The player whose turn it currently is: 'X' or 'O'. X always starts
    private int movecount;         // Total number of valid moves made in this game. Used for draw detection.
    private char winner;           // 'X', 'O', or ' ' (no winner yet)

    public GameLogic() {
        board = new char[3][3];
        currentPlayer = 'X';
        movecount = 0;
        winner = ' ';
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
     * @return 'X' or 'O' if a player has won, or ' ' if no winner yet.
     */
    public char checkWinner() {
        // 1. Check Rows
        for (int r = 0; r < 3; r++) {
            if (board[r][0] != ' ' && board[r][0] == board[r][1] && board[r][1] == board[r][2]) {
                return board[r][0];
            }
        }

        // 2. Check Columns
        for (int c = 0; c < 3; c++) {
            if (board[0][c] != ' ' && board[0][c] == board[1][c] && board[1][c] == board[2][c]) {
                return board[0][c];
            }
        }

        // 3. Check Main Diagonal (Top-Left to Bottom-Right)
        if (board[0][0] != ' ' && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return board[0][0];
        }

        // 4. Check Anti-Diagonal (Top-Right to Bottom-Left)
        if (board[0][2] != ' ' && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return board[0][2];
        }

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
        System.out.println("=== Running Week 5 Win Detector Tests ===");

        // Test 1: Horizontal Row Win (Row 0)
        GameLogic g1 = new GameLogic();
        g1.makeMove(0, 0); // X
        g1.makeMove(1, 0); // O
        g1.makeMove(0, 1); // X
        g1.makeMove(1, 1); // O
        g1.makeMove(0, 2); // X (Row 0 complete)
        System.out.println("Test 1 (Row Win): Winner=" + g1.getWinner() + " [Expected: X], GameOver=" + g1.isGameOver());

        // Test 2: Vertical Column Win (Col 1)
        GameLogic g2 = new GameLogic();
        g2.makeMove(0, 0); // X
        g2.makeMove(0, 1); // O
        g2.makeMove(2, 2); // X
        g2.makeMove(1, 1); // O
        g2.makeMove(2, 0); // X
        g2.makeMove(2, 1); // O (Col 1 complete)
        System.out.println("Test 2 (Col Win): Winner=" + g2.getWinner() + " [Expected: O], GameOver=" + g2.isGameOver());

        // Test 3: Diagonal Win (Main Diagonal)
        GameLogic g3 = new GameLogic();
        g3.makeMove(0, 0); // X
        g3.makeMove(0, 1); // O
        g3.makeMove(1, 1); // X
        g3.makeMove(0, 2); // O
        g3.makeMove(2, 2); // X (Diagonal complete)
        System.out.println("Test 3 (Diag Win): Winner=" + g3.getWinner() + " [Expected: X], GameOver=" + g3.isGameOver());

        // Test 4: Anti-Diagonal Win
        GameLogic g4 = new GameLogic();
        g4.makeMove(0, 2); // X
        g4.makeMove(0, 0); // O
        g4.makeMove(1, 1); // X
        g4.makeMove(1, 0); // O
        g4.makeMove(2, 0); // X (Anti-Diagonal complete)
        System.out.println("Test 4 (Anti-Diag Win): Winner=" + g4.getWinner() + " [Expected: X], GameOver=" + g4.isGameOver());

        // Test 5: Draw Game (9 moves, no winner)
        // X O X
        // X X O
        // O X O
        GameLogic g5 = new GameLogic();
        g5.makeMove(0, 0); // X
        g5.makeMove(0, 1); // O
        g5.makeMove(0, 2); // X
        g5.makeMove(1, 2); // O
        g5.makeMove(1, 0); // X
        g5.makeMove(2, 0); // O
        g5.makeMove(1, 1); // X
        g5.makeMove(2, 2); // O
        g5.makeMove(2, 1); // X
        System.out.println("Test 5 (Draw): isDraw=" + g5.isDraw() + " [Expected: true], Winner='" + g5.getWinner() + "', GameOver=" + g5.isGameOver());
    }
}
