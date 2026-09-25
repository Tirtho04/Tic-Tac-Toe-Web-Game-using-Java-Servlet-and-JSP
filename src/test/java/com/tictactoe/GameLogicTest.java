package com.tictactoe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tic-Tac-Toe GameLogic Test Suite (Week 8 Testing)")
public class GameLogicTest {

    private GameLogic game;

    @BeforeEach
    public void setUp() {
        game = new GameLogic();
    }

    @Test
    @DisplayName("TC-01: Valid Move Execution & Turn Alternation (X -> O -> X)")
    public void testTC01_ValidMoveAndTurnAlternation() {
        // Initial state
        assertEquals('X', game.getCurrentPlayer(), "Initial player must be X");
        assertEquals(0, game.getMoveCount(), "Initial move count must be 0");
        assertFalse(game.isGameOver(), "Game should not be over at start");

        // Move 1 by X
        boolean move1 = game.makeMove(0, 0);
        assertTrue(move1, "Move (0,0) by X must succeed");
        assertEquals('X', game.getBoard()[0][0], "Cell (0,0) must contain 'X'");
        assertEquals('O', game.getCurrentPlayer(), "Turn must switch to O");
        assertEquals(1, game.getMoveCount(), "Move count must be 1");

        // Move 2 by O
        boolean move2 = game.makeMove(1, 1);
        assertTrue(move2, "Move (1,1) by O must succeed");
        assertEquals('O', game.getBoard()[1][1], "Cell (1,1) must contain 'O'");
        assertEquals('X', game.getCurrentPlayer(), "Turn must switch back to X");
        assertEquals(2, game.getMoveCount(), "Move count must be 2");
    }

    @Test
    @DisplayName("TC-02: Invalid Move Rejection (Occupied Cell & Out of Bounds)")
    public void testTC02_InvalidMoveRejection() {
        // Make a valid move
        assertTrue(game.makeMove(0, 0), "Initial move should be valid");

        // Attempt move on already occupied cell
        boolean duplicateMove = game.makeMove(0, 0);
        assertFalse(duplicateMove, "Move on occupied cell must be rejected");
        assertEquals(1, game.getMoveCount(), "Move count must not increment on invalid move");
        assertEquals('O', game.getCurrentPlayer(), "Current player should not change on failed move");

        // Attempt out of bounds moves
        assertFalse(game.makeMove(-1, 0), "Negative row must be rejected");
        assertFalse(game.makeMove(0, 3), "Column > 2 must be rejected");
        assertFalse(game.makeMove(3, 3), "Row and Col > 2 must be rejected");
        assertEquals(1, game.getMoveCount(), "Move count must remain 1");
    }

    @Test
    @DisplayName("TC-03: Row Win Detection (Rows 0, 1, 2) & Winning Cells Highlight")
    public void testTC03_RowWinDetection() {
        // Test Row 0 Win by X
        game.makeMove(0, 0); // X
        game.makeMove(1, 0); // O
        game.makeMove(0, 1); // X
        game.makeMove(1, 1); // O
        game.makeMove(0, 2); // X (Row 0 win)

        assertEquals('X', game.getWinner(), "Winner must be X for Row 0");
        assertTrue(game.isGameOver(), "Game must be over after win");
        assertTrue(game.isWinningCell(0, 0), "(0,0) must be a winning cell");
        assertTrue(game.isWinningCell(0, 1), "(0,1) must be a winning cell");
        assertTrue(game.isWinningCell(0, 2), "(0,2) must be a winning cell");
        assertFalse(game.isWinningCell(1, 0), "(1,0) must not be a winning cell");

        // Test Row 1 Win in a new game
        GameLogic gameRow1 = new GameLogic();
        gameRow1.makeMove(0, 0); // X
        gameRow1.makeMove(1, 0); // O
        gameRow1.makeMove(0, 1); // X
        gameRow1.makeMove(1, 1); // O
        gameRow1.makeMove(2, 2); // X
        gameRow1.makeMove(1, 2); // O (Row 1 win)
        assertEquals('O', gameRow1.getWinner(), "Winner must be O for Row 1");
    }

    @Test
    @DisplayName("TC-04: Column Win Detection (Cols 0, 1, 2) & Winning Cells Highlight")
    public void testTC04_ColumnWinDetection() {
        // Test Column 1 Win by X
        game.makeMove(0, 1); // X
        game.makeMove(0, 0); // O
        game.makeMove(1, 1); // X
        game.makeMove(1, 0); // O
        game.makeMove(2, 1); // X (Col 1 win)

        assertEquals('X', game.getWinner(), "Winner must be X for Column 1");
        assertTrue(game.isGameOver(), "Game must be over");
        assertTrue(game.isWinningCell(0, 1), "(0,1) must be a winning cell");
        assertTrue(game.isWinningCell(1, 1), "(1,1) must be a winning cell");
        assertTrue(game.isWinningCell(2, 1), "(2,1) must be a winning cell");
    }

    @Test
    @DisplayName("TC-05: Diagonal Win Detection (Main & Anti-Diagonal)")
    public void testTC05_DiagonalWinDetection() {
        // Main Diagonal Win (0,0), (1,1), (2,2)
        game.makeMove(0, 0); // X
        game.makeMove(0, 1); // O
        game.makeMove(1, 1); // X
        game.makeMove(0, 2); // O
        game.makeMove(2, 2); // X (Main diagonal win)

        assertEquals('X', game.getWinner(), "Winner must be X on main diagonal");
        assertTrue(game.isWinningCell(0, 0));
        assertTrue(game.isWinningCell(1, 1));
        assertTrue(game.isWinningCell(2, 2));

        // Anti-Diagonal Win (0,2), (1,1), (2,0)
        GameLogic gameAntiDiag = new GameLogic();
        gameAntiDiag.makeMove(0, 2); // X
        gameAntiDiag.makeMove(0, 0); // O
        gameAntiDiag.makeMove(1, 1); // X
        gameAntiDiag.makeMove(0, 1); // O
        gameAntiDiag.makeMove(2, 0); // X (Anti-diagonal win)
        assertEquals('X', gameAntiDiag.getWinner(), "Winner must be X on anti-diagonal");
        assertTrue(gameAntiDiag.isWinningCell(0, 2));
        assertTrue(gameAntiDiag.isWinningCell(1, 1));
        assertTrue(gameAntiDiag.isWinningCell(2, 0));
    }

    @Test
    @DisplayName("TC-06: Draw Condition Detection (9 moves filled, no winner)")
    public void testTC06_DrawConditionDetection() {
        // Board layout for draw:
        // X O X
        // X X O
        // O X O
        game.makeMove(0, 0); // X
        game.makeMove(0, 1); // O
        game.makeMove(0, 2); // X
        game.makeMove(1, 2); // O
        game.makeMove(1, 0); // X
        game.makeMove(2, 0); // O
        game.makeMove(1, 1); // X
        game.makeMove(2, 2); // O
        game.makeMove(2, 1); // X (Move 9)

        assertEquals(9, game.getMoveCount(), "Move count must be 9");
        assertEquals(' ', game.getWinner(), "Winner must be ' ' for draw");
        assertTrue(game.isDraw(), "isDraw() must return true");
        assertTrue(game.isGameOver(), "isGameOver() must return true on draw");
        assertEquals(1, game.getDraws(), "Draw count must be incremented to 1");
    }

    @Test
    @DisplayName("TC-07: Board Reset / New Round (Clears Grid & Keeps Score)")
    public void testTC07_BoardResetKeepsScore() {
        // Play round 1: X wins
        game.makeMove(0, 0); // X
        game.makeMove(1, 0); // O
        game.makeMove(0, 1); // X
        game.makeMove(1, 1); // O
        game.makeMove(0, 2); // X (X wins)

        assertEquals(1, game.getXWins(), "Player X score must be 1");

        // Perform board reset for new round
        game.resetBoard();

        // Verify board is cleared
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                assertEquals(' ', game.getBoard()[r][c], "All cells must be empty");
            }
        }
        assertEquals('X', game.getCurrentPlayer(), "Turn must reset to X");
        assertEquals(0, game.getMoveCount(), "Move count must reset to 0");
        assertEquals(' ', game.getWinner(), "Winner must reset to ' '");
        assertNull(game.getWinningCells(), "Winning cells must be null");
        assertFalse(game.isGameOver(), "Game must not be over");

        // CRUCIAL: Match scores must be preserved across rounds!
        assertEquals(1, game.getXWins(), "Player X wins must remain 1 after resetBoard()");
        assertEquals(0, game.getOWins(), "Player O wins must remain 0");
        assertEquals(0, game.getDraws(), "Draws must remain 0");
    }

    @Test
    @DisplayName("TC-08: Score Tracker & Reset Scores (xWins, oWins, draws, resetScores)")
    public void testTC08_ScoreTrackerAndResetScores() {
        // Round 1: X wins
        game.makeMove(0, 0); // X
        game.makeMove(1, 0); // O
        game.makeMove(0, 1); // X
        game.makeMove(1, 1); // O
        game.makeMove(0, 2); // X
        assertEquals(1, game.getXWins());
        assertEquals(0, game.getOWins());

        // Round 2: O wins
        game.resetBoard();
        game.makeMove(0, 0); // X
        game.makeMove(1, 0); // O
        game.makeMove(0, 1); // X
        game.makeMove(1, 1); // O
        game.makeMove(2, 2); // X
        game.makeMove(1, 2); // O (Row 1 win)
        assertEquals(1, game.getXWins(), "X wins should still be 1");
        assertEquals(1, game.getOWins(), "O wins should now be 1");

        // Reset all match scores
        game.resetScores();
        assertEquals(0, game.getXWins(), "X wins must reset to 0");
        assertEquals(0, game.getOWins(), "O wins must reset to 0");
        assertEquals(0, game.getDraws(), "Draws must reset to 0");
    }
}
