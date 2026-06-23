# Design Document
**Project:** Tic Tac Toe Web Game  
**Week 2:** System Design and Architecture

---

## 1. Wireframe Design

The game UI is a single page (`index.jsp`) with the following layout:

```
┌──────────────────────────────────────┐
│          TIC TAC TOE GAME            │
│       [ Current Turn: X ]            │
├──────────┬──────────┬────────────────┤
│    X     │    O     │                │
├──────────┼──────────┼────────────────┤
│          │    X     │                │
├──────────┼──────────┼────────────────┤
│    O     │          │                │
├──────────┴──────────┴────────────────┤
│     Player X: 3  |  Player O: 2      │
│           [ New Game ]               │
│       [ Winner Message Area ]        │
└──────────────────────────────────────┘
```

### Design Features

- **Responsive 3×3 game board** — each cell is a clickable form POST button
- **Turn indicator** — displays whose turn it is (X or O) at the top, updated dynamically
- **Scoreboard** — session-persisted win counts for both players
- **Winner / draw notification area** — message region shown on game end
- **New Game button** — triggers a board reset via POST

---

## 2. UML Class Diagram

### Classes

#### `GameLogic.java` — Model

| Type   | Member                                  |
|--------|-----------------------------------------|
| Field  | `- board : char[3][3]`                  |
| Field  | `- currentPlayer : char`                |
| Method | `+ makeMove(row: int, col: int) : boolean` |
| Method | `+ checkWinner() : char`                |
| Method | `+ isDraw() : boolean`                  |
| Method | `+ resetBoard() : void`                 |
| Method | `+ getBoard() : char[][]`               |
| Method | `+ getCurrentPlayer() : char`           |

#### `GameServlet.java` — Controller

| Type   | Member                               |
|--------|--------------------------------------|
| Field  | `- game : GameLogic`                 |
| Field  | `- session : HttpSession`            |
| Method | `+ doGet(req, res) : void`           |
| Method | `+ doPost(req, res) : void`          |

#### `index.jsp` — View

- Renders the 3×3 board from session state
- Shows current turn, winner/draw messages, and scores
- Submits moves as HTTP POST to `GameServlet`
- Provides the New Game (reset) action

### Relationships

```
GameServlet  ----uses---->  GameLogic
     |                       (creates instance, calls methods)
     |
     +----redirects--->  index.jsp
                            (PRG: response.sendRedirect)

index.jsp  ----POST form--->  GameServlet
                            (user submits move)
```

---

## 3. MVC Architecture (Without Database)

### Layer Responsibilities

| Layer      | File              | Responsibility                                                                                           |
|------------|-------------------|----------------------------------------------------------------------------------------------------------|
| Model      | `GameLogic.java`  | Maintains game state (board and current player). Validates and processes moves. Checks winner or draw. Provides data to the controller. |
| View       | `index.jsp`       | Displays the 3×3 board. Shows current turn, messages, and scores. Sends player moves to the controller. Provides New Game (reset) action. |
| Controller | `GameServlet.java`| Receives HTTP requests (GET/POST). Invokes `GameLogic` methods based on user actions. Stores game object in `HttpSession`. Uses PRG pattern (redirect) after POST. |

### MVC Request Flow

```
User
 │
 │  ① GET (initial page load)
 ▼
index.jsp (View)  ←──────────────────────────┐
 │                                            │
 │  ② POST (player clicks a cell)             │  ⑥ Response (updated UI)
 ▼                                            │
GameServlet (Controller)                      │
 │                                            │
 │  ③ Invoke makeMove(), checkWinner()        │
 ▼                                            │
GameLogic (Model)                             │
 │                                            │
 │  ④ Return result (winner / draw / next)    │
 ▼                                            │
GameServlet (Controller)                      │
 │                                            │
 │  ⑤ response.sendRedirect("index.jsp")     │
 └────────────────────────────────────────────┘
```

---

## 4. Key Technical Decisions

### PRG Pattern (Post-Redirect-Get)

After every `doPost()`, the servlet calls `response.sendRedirect("index.jsp")`. This prevents the browser from re-submitting the form on page reload. All state is stored in `HttpSession`, not as servlet instance variables.

### Session State Strategy

```java
// Read from session
GameLogic gl = (GameLogic) session.getAttribute("game");

// Create if absent (first visit or session expired)
if (gl == null) {
    gl = new GameLogic();
    session.setAttribute("game", gl);
}

// Persist after every move
session.setAttribute("game", gl);
```

### Win Detection — 8 Combinations

```
Rows  : [0][0],[0][1],[0][2]  |  [1][0],[1][1],[1][2]  |  [2][0],[2][1],[2][2]
Cols  : [0][0],[1][0],[2][0]  |  [0][1],[1][1],[2][1]  |  [0][2],[1][2],[2][2]
Diags : [0][0],[1][1],[2][2]  |  [0][2],[1][1],[2][0]
```

---

