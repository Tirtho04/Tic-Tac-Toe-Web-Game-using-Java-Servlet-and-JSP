# Project Report: Tic-Tac-Toe Web Game
**Course / Project:** Web Application Development (Java Servlet & JSP)  
**Author:** Tirtho04  
**Date:** September 2026  
**Version:** `v1.0-tested`  
**Repository:** [GitHub Repository](https://github.com/Tirtho04/Tic-Tac-Toe-Web-Game-using-Java-Servlet-and-JSP)

---

## 📑 Table of Contents
1. [Executive Summary](#1-executive-summary)
2. [Architecture & Design System](#2-architecture--design-system)
3. [Core Algorithms & State Management](#3-core-algorithms--state-management)
4. [User Interface & Modern Dark Theme](#4-user-interface--modern-dark-theme)
5. [Testing & Quality Assurance](#5-testing--quality-assurance)
6. [Challenges Encountered & Solutions](#6-challenges-encountered--solutions)
7. [Conclusion & Future Enhancements](#7-conclusion--future-enhancements)

---

## 1. Executive Summary

The **Tic-Tac-Toe Web Game** is an interactive, browser-based two-player game implemented in **Java (JDK 17+)**, **Jakarta Servlet 6.0**, **JavaServer Pages (JSP)**, and styled with modern CSS3 animations and semantic HTML5 landmarks.

The project demonstrates clean **Model-View-Controller (MVC)** architectural separation, state persistence via `HttpSession` without requiring an external database, the **Post-Redirect-Get (PRG)** design pattern to prevent duplicate form submissions, automated **JUnit 5** test suites with 100% pass rates, and an embedded Apache Tomcat 10 container managed via the **Cargo Maven Plugin**.

---

## 2. Architecture & Design System

### 2.1 Model-View-Controller (MVC) Pattern

```
                 HTTP GET (Initial load)
    User ───────────────────────────────────────┐
      │                                         ▼
      │  HTTP POST (Move / Action)        ┌───────────┐
      ├──────────────────────────────────►│ index.jsp │ (View)
      │                                   └─────▲─────┘
      ▼                                         │
┌────────────┐     Retrieves & Calls      ┌─────┴──────────┐
│ GameServlet│ ──────────────────────────►│ GameLogic.java │ (Model in HttpSession)
│(Controller)│                            └────────────────┘
└─────┬──────┘
      │
      └───────── PRG Redirect (302) ───────────┘
```

1. **Model (`GameLogic.java`):**
   * Encapsulates the 3×3 matrix (`char[][] board`), current player turn (`'X'` or `'O'`), total moves, round winner, winning line coordinates, and match score metrics (`xWins`, `oWins`, `draws`).
   * Implements `java.io.Serializable` for safe container serialization across HTTP sessions.

2. **Controller (`GameServlet.java`):**
   * Mapped to `/play`.
   * Intercepts HTTP requests, performs defensive parameter validation, invokes business logic methods (`makeMove()`, `resetBoard()`, `resetScores()`), updates session attributes, and redirects to the view.

3. **View (`index.jsp`):**
   * Dynamically renders the 3×3 grid from the session `GameLogic` bean.
   * Displays the 3-tile live scoreboard, turn status badges, win/draw notification banners, glowing winning lines, and action controls.

### 2.2 Post-Redirect-Get (PRG) Pattern
To avoid the classic web hazard where refreshing a browser after a POST prompt re-submits the form (potentially making duplicate moves or throwing errors), `GameServlet` always terminates POST processing with:
```java
response.sendRedirect("index.jsp");
```
This forces the client browser to issue a clean `GET index.jsp`, ensuring safe page refreshes and bookmarking.

---

## 3. Core Algorithms & State Management

### 3.1 8-Line Win Detection Algorithm
The `checkWinner()` method exhaustively checks all 8 possible winning lines on a 3×3 grid:
* **3 Rows:** `(0,0)-(0,1)-(0,2)`, `(1,0)-(1,1)-(1,2)`, `(2,0)-(2,1)-(2,2)`
* **3 Columns:** `(0,0)-(1,0)-(2,0)`, `(0,1)-(1,1)-(2,1)`, `(0,2)-(1,2)-(2,2)`
* **Main Diagonal:** `(0,0)-(1,1)-(2,2)`
* **Anti-Diagonal:** `(0,2)-(1,1)-(2,0)`

When a win is detected, the algorithm captures the exact coordinates in `winningCells` ($3 \times 2$ array) to allow the view to render an animated green neon glow on the winning cells.

### 3.2 Draw Detection
A draw occurs when `movecount == 9` and no winner was identified:
```java
public boolean isDraw() {
    return (movecount == 9 && winner == ' ');
}
```

### 3.3 Scoreboard Persistence Strategy
* **New Round (`resetBoard()`):** Clears grid cells and resets `currentPlayer = 'X'`, but **retains `xWins`, `oWins`, and `draws`**.
* **Reset Scores (`resetScores()`):** Explicitly zeroes out match score counters back to `0 - 0 - 0`.
* **Duplicate Guard (`scoreUpdated`):** Prevents double-incrementing scores if evaluated multiple times before reset.

---

## 4. User Interface & Modern Dark Theme

* **Color Palette:**
  * Background: Deep slate radial canvas (`#1e293b` to `#0b0f19`)
  * Container Card: `#1e293b` with `#334155` border and soft ambient box-shadow
  * Player X: Neon Cyan (`#38bdf8`) with text glow
  * Player O: Vibrant Coral Red (`#f87171`) with text glow
  * Winning Cells: Emerald Green pulsing glow (`#22c55e` / `#86efac`)
* **Micro-interactions:**
  * `@keyframes markPop`: Elastic snap animation when marks are placed on the board.
  * `@keyframes pulseRing`: Expanding neon ring on the active player's turn badge.
  * `@keyframes bannerSlideDown`: Smooth entrance for win/draw announcement banners.
  * `touch-action: manipulation`: Removes the default 300ms mobile tap delay.
* **Accessibility (a11y):**
  * Semantic landmarks (`<main>`, `<section>`, `<nav>`)
  * Dynamic `aria-label` descriptors on grid buttons
  * `aria-live="polite"` region for real-time turn and result announcements
  * High-visibility `:focus-visible` outline rings for keyboard accessibility (`Tab` + `Enter`).

---

## 5. Testing & Quality Assurance

Automated testing was implemented using **JUnit 5 (`org.junit.jupiter`)** and executed through the Maven Surefire plugin.

### Test Matrix Summary (8 Test Cases)

| Test ID | Test Name | Target Behavior | Result |
| :--- | :--- | :--- | :--- |
| **TC-01** | Valid Move & Turn Alternation | Marks cell, switches turn X ➔ O ➔ X, increments move count | ✅ **PASS** |
| **TC-02** | Invalid Move Rejection | Rejects occupied cells and out-of-bounds coordinates | ✅ **PASS** |
| **TC-03** | Row Win Detection | Detects 3-in-a-row on all rows and highlights coordinates | ✅ **PASS** |
| **TC-04** | Column Win Detection | Detects 3-in-a-column on all columns and highlights coordinates | ✅ **PASS** |
| **TC-05** | Diagonal Win Detection | Detects Main and Anti-diagonal win patterns | ✅ **PASS** |
| **TC-06** | Draw Detection | Flags game as drawn after 9 moves with no winner | ✅ **PASS** |
| **TC-07** | Board Reset vs Score | Clears board for new round while keeping match scores intact | ✅ **PASS** |
| **TC-08** | Score Tracker & Reset Scores | Accurately aggregates multi-round scores and resets to 0 | ✅ **PASS** |

**Execution Result:** `Tests run: 8, Failures: 0, Errors: 0, Skipped: 0` (100% Pass Rate).

---

## 6. Challenges Encountered & Solutions

1. **Browser Form Re-submission & State Duplication:**
   * *Problem:* Refreshing the page after placing a mark triggered browser re-POST alerts.
   * *Solution:* Implemented the Post-Redirect-Get (PRG) pattern in `GameServlet.java`.
2. **Defensive Parameter Handling:**
   * *Problem:* Malformed or empty HTTP POST parameters caused `NumberFormatException`.
   * *Solution:* Created `parseCoordinate()` helper method that safely validates and parses inputs without throwing unhandled exceptions.
3. **Standalone Tomcat Portability:**
   * *Problem:* Ensuring any reviewer can run the full web application without installing external application servers.
   * *Solution:* Configured `cargo-maven3-plugin` to automatically download, unpack, and launch an embedded Apache Tomcat 10.1 instance on port 8080.

---

## 7. Conclusion & Future Enhancements

The Tic-Tac-Toe Web Game successfully fulfills all architectural, functional, and user-experience objectives of the 10-Week schedule.

### Potential Future Enhancements:
* **AI Opponent (Single Player Mode):** Minimax algorithm for unbeatable or difficulty-adjustable AI.
* **WebSocket Multiplayer:** Bidirectional WebSocket integration for playing across separate remote browsers.
* **Persistent Database Integration:** Storing global player profiles and leaderboards using JPA/Hibernate.
