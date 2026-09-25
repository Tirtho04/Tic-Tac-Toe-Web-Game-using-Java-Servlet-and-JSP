# Test Cases Documentation
**Project:** Tic-Tac-Toe Web Game  
**Phase:** Week 8 — Testing & Verification  
**Version:** `v1.0-tested`  
**Test Suite:** `com.tictactoe.GameLogicTest` (JUnit 5)

---

## 📊 Test Execution Summary

| Metric | Value |
| :--- | :--- |
| **Total Test Cases** | 8 |
| **Passed** | 8 |
| **Failed** | 0 |
| **Execution Framework** | JUnit 5 (`org.junit.jupiter`) + Maven Surefire |
| **Coverage Area** | State transitions, win algorithm, draw detection, bounds checking, session score tracking |

---

## 📝 Detailed Test Specifications

### TC-01: Valid Move Execution & Turn Alternation
* **Objective:** Verify that placing a mark on an empty cell updates the board state, toggles player turn from `X` to `O` to `X`, and increments move count.
* **Preconditions:** Fresh game instance (`GameLogic`).
* **Test Steps:**
  1. Initialize game (`currentPlayer = 'X'`, `moveCount = 0`).
  2. Execute `makeMove(0, 0)`.
  3. Execute `makeMove(1, 1)`.
* **Expected Result:**
  * Step 2: returns `true`, `board[0][0] == 'X'`, `currentPlayer == 'O'`, `moveCount == 1`.
  * Step 3: returns `true`, `board[1][1] == 'O'`, `currentPlayer == 'X'`, `moveCount == 2`.
* **Status:** ✅ **PASS**

---

### TC-02: Invalid Move Rejection
* **Objective:** Ensure moves on already-occupied cells and out-of-bounds coordinates are rejected without modifying state.
* **Preconditions:** Game in progress with move at `(0, 0)`.
* **Test Steps:**
  1. Call `makeMove(0, 0)` again.
  2. Call `makeMove(-1, 0)`.
  3. Call `makeMove(0, 3)`.
  4. Call `makeMove(3, 3)`.
* **Expected Result:** All calls return `false`; `moveCount` remains 1; `currentPlayer` does not change.
* **Status:** ✅ **PASS**

---

### TC-03: Horizontal (Row) Win Detection & Winning Highlight
* **Objective:** Verify win detection across rows (Row 0, Row 1, Row 2) and verify that `winningCells` contains the correct 3 coordinates.
* **Preconditions:** New game.
* **Test Steps:**
  1. Play sequence: `(0,0)[X]`, `(1,0)[O]`, `(0,1)[X]`, `(1,1)[O]`, `(0,2)[X]`.
* **Expected Result:**
  * `winner == 'X'`.
  * `isGameOver() == true`.
  * `isWinningCell(0, 0)`, `isWinningCell(0, 1)`, `isWinningCell(0, 2)` return `true`.
  * `isWinningCell(1, 0)` returns `false`.
* **Status:** ✅ **PASS**

---

### TC-04: Vertical (Column) Win Detection & Winning Highlight
* **Objective:** Verify win detection across columns (Col 0, Col 1, Col 2) and coordinate tracking.
* **Preconditions:** New game.
* **Test Steps:**
  1. Play sequence: `(0,1)[X]`, `(0,0)[O]`, `(1,1)[X]`, `(1,0)[O]`, `(2,1)[X]`.
* **Expected Result:**
  * `winner == 'X'`.
  * `isGameOver() == true`.
  * `isWinningCell(0,1)`, `isWinningCell(1,1)`, `isWinningCell(2,1)` return `true`.
* **Status:** ✅ **PASS**

---

### TC-05: Diagonal Win Detection (Main & Anti-Diagonal)
* **Objective:** Verify win detection on main diagonal `[(0,0), (1,1), (2,2)]` and anti-diagonal `[(0,2), (1,1), (2,0)]`.
* **Preconditions:** New game.
* **Test Steps:**
  1. Main diagonal: `(0,0)[X]`, `(0,1)[O]`, `(1,1)[X]`, `(0,2)[O]`, `(2,2)[X]`.
  2. Anti diagonal: `(0,2)[X]`, `(0,0)[O]`, `(1,1)[X]`, `(0,1)[O]`, `(2,0)[X]`.
* **Expected Result:** In both sequences, `winner == 'X'`, `isGameOver() == true`, and respective diagonal coordinates return `isWinningCell == true`.
* **Status:** ✅ **PASS**

---

### TC-06: Draw Condition Detection (Cat's Game)
* **Objective:** Verify that when 9 moves are completed without any player aligning 3 in a row, the game declares a draw.
* **Preconditions:** New game.
* **Test Steps:**
  1. Play sequence filling grid: `(0,0)[X]`, `(0,1)[O]`, `(0,2)[X]`, `(1,2)[O]`, `(1,0)[X]`, `(2,0)[O]`, `(1,1)[X]`, `(2,2)[O]`, `(2,1)[X]`.
* **Expected Result:**
  * `moveCount == 9`.
  * `winner == ' '`.
  * `isDraw() == true`.
  * `isGameOver() == true`.
  * `draws == 1`.
* **Status:** ✅ **PASS**

---

### TC-07: Board Reset & Match Score Persistence
* **Objective:** Verify that `resetBoard()` (New Round) clears grid cells and resets turn to X while **preserving cumulative session scores**.
* **Preconditions:** Game where Round 1 was won by X (`xWins == 1`).
* **Test Steps:**
  1. Call `resetBoard()`.
* **Expected Result:**
  * All 9 cells contain `' '`.
  * `currentPlayer == 'X'`.
  * `moveCount == 0`.
  * `winner == ' '`.
  * `winningCells == null`.
  * `xWins == 1` (retained).
  * `oWins == 0`.
  * `draws == 0`.
* **Status:** ✅ **PASS**

---

### TC-08: Score Tracker & Match Score Reset
* **Objective:** Verify score increments across multiple rounds and verify that `resetScores()` clears match scores back to zero.
* **Preconditions:** New game.
* **Test Steps:**
  1. Complete Round 1 with X winning (`xWins = 1`).
  2. Call `resetBoard()`.
  3. Complete Round 2 with O winning (`oWins = 1`).
  4. Verify scores (`X: 1`, `O: 1`, `Draws: 0`).
  5. Call `resetScores()`.
* **Expected Result:**
  * After Step 4: `xWins == 1`, `oWins == 1`.
  * After Step 5: `xWins == 0`, `oWins == 0`, `draws == 0`.
* **Status:** ✅ **PASS**

---

## 🏃 Running Automated Tests

To execute the full automated test suite via Maven:

```bash
mvn test
```
