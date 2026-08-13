package com.tictactoe;

public class GameLogic implements java.io.Serializable {

    //Fields
    private char[][] board;
    private char currentPlayer;    // The player whose turn it currently is: 'X' or 'O'. X always starts
    private int movecount; //Total number of valid moves made in this game. Used for draw detection.
    private char winner;

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

    public boolean makeMove(int row, int col) {
        if (board[row][col] != ' ') {
            return false;
        }
        board[row][col] = currentPlayer;
        movecount++;
        currentPlayer = (currentPlayer == 'X') ? '0' : 'X';
        return true;
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

    //win& draw detection placeholder satubs(fully implemented week 5)
    public char winner() {
        // Week 5: implement all 8 winning combinations
        return winner;
    }

    public boolean isDraw() {
        return (movecount == 9 && winner == ' ');
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

    public char getwinner() {
        return winner;
    }

    // Quick test — remove before Week 5
    public static void main(String[] args) {
        GameLogic game = new GameLogic();

        game.makeMove(0, 0); // X places at top-left
        game.makeMove(1, 1); // O places at centre
        game.makeMove(0, 1); // X places at top-middle

        System.out.println("board[0][0] = " + game.getBoard()[0][0]); // X
        System.out.println("board[1][1] = " + game.getBoard()[1][1]); // O
        System.out.println("board[0][1] = " + game.getBoard()[0][1]); // X
        System.out.println("currentPlayer after 3 moves = " + game.getCurrentPlayer()); // O

        game.resetBoard();
        System.out.println("After reset -- board[0][0] = " + game.getBoard()[0][0]); // (space)
        System.out.println("After reset -- currentPlayer = " + game.getCurrentPlayer()); // X
    }
}
