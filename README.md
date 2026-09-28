# 🎮 Tic-Tac-Toe Web Game (Java Servlet & JSP)

![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Jakarta EE](https://img.shields.io/badge/Jakarta%20EE-10.0-00739C?style=for-the-badge&logo=eclipse-ide&logoColor=white)
![Apache Tomcat](https://img.shields.io/badge/Apache%20Tomcat-10.1-F8DC75?style=for-the-badge&logo=apache-tomcat&logoColor=black)
![JUnit 5](https://img.shields.io/badge/JUnit-5.10-25A162?style=for-the-badge&logo=junit5&logoColor=white)
![Maven](https://img.shields.io/badge/Apache%20Maven-3.9-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![Release](https://img.shields.io/badge/Release-v1.0--tested-brightgreen?style=for-the-badge)

A modern, responsive, two-player **Tic-Tac-Toe Web Application** built from the ground up using the **Model-View-Controller (MVC)** architectural pattern in Java. The application features session-based state management, Post-Redirect-Get (PRG) navigation flow, live match scoreboard tracking, automated JUnit 5 test suites, and an aesthetic dark-themed user interface.

---

## 📸 Game Interface Preview

```text
┌──────────────────────────────────────────────────────────┐
│                      TIC-TAC-TOE                         │
│                                                          │
│   ┌──────────────┐   ┌──────────────┐   ┌──────────────┐ │
│   │   PLAYER X   │   │     TIES     │   │   PLAYER O   │ │
│   │      3       │   │      1       │   │      2       │ │
│   └──────────────┘   └──────────────┘   └──────────────┘ │
│                                                          │
│                   [ (X) Player X Turn ]                  │
│                                                          │
│                 ┌─────────┬─────────┬─────────┐          │
│                 │    X    │    O    │         │          │
│                 ├─────────┼─────────┼─────────┤          │
│                 │         │    X    │         │          │
│                 ├─────────┼─────────┼─────────┤          │
│                 │    O    │         │    X    │          │
│                 └─────────┴─────────┴─────────┘          │
│                                                          │
│            [ 🔄 New Round ]     [ 🗑️ Reset Score ]        │
└──────────────────────────────────────────────────────────┘
```

---

## ✨ Features

* 🏛️ **Clean MVC Architecture**: Full decoupling between Model ([`GameLogic.java`](src/main/java/com/tictactoe/GameLogic.java)), Controller ([`GameServlet.java`](src/main/java/com/tictactoe/GameServlet.java)), and View ([`index.jsp`](src/main/webapp/index.jsp)).
* 🛡️ **Post-Redirect-Get (PRG) Pattern**: Eliminates duplicate form re-submissions and browser refresh warnings.
* 💾 **Session-Based Match Scoreboard**: Persists `Player X Wins`, `Player O Wins`, and `Ties` across rounds within the player's HTTP session.
* 🏆 **8-Line Win & Draw Detection**: Instant evaluation across 3 rows, 3 columns, and 2 diagonals.
* ✨ **Winning Line Highlight**: Winning 3-in-a-row cells glow with animated neon emerald effects.
* 🎨 **Deep Slate Dark Theme**: Deep dark canvas (`#0b0f19` / `#1e293b`), neon cyan `X`, and vibrant coral/red `O`.
* ⚡ **Fluid Micro-Interactions**: Elastic mark pop-in animations (`@keyframes markPop`), pulsing turn rings, and touch latency elimination (`touch-action: manipulation`).
* ♿ **Accessibility (a11y) First**: High-contrast `:focus-visible` keyboard rings, semantic HTML5 landmarks, dynamic `aria-label` tags, and `aria-live="polite"` announcements.
* 🧪 **Automated JUnit 5 Test Suite**: 8 automated test cases covering state transitions, boundaries, win combinations, and score tracking with 100% pass rates.

---

## 🏛️ MVC Architecture & Request Flow

```
1. User clicks grid cell
          │
          ▼
2. HTTP POST /play (row, col)
          │
          ▼
┌─────────────────────────┐
│       GameServlet       │ ──► Retrieves or creates GameLogic in HttpSession
│      (Controller)       │ ──► Validates inputs & calls game.makeMove(row, col)
└───────────┬─────────────┘
            │
            ▼
┌─────────────────────────┐
│        GameLogic        │ ──► Updates char[3][3] board & move count
│         (Model)         │ ──► Runs checkWinner() & isDraw()
└───────────┬─────────────┘ ──► Updates xWins, oWins, draws counters
            │
            ▼
3. response.sendRedirect("index.jsp")  (PRG Pattern)
            │
            ▼
4. HTTP GET index.jsp
            │
            ▼
┌─────────────────────────┐
│        index.jsp        │ ──► Reads game state from session
│         (View)          │ ──► Renders scoreboard, 3×3 grid, & status
└─────────────────────────┘
```

---

## 🚀 Getting Started

### Prerequisites
* **Java Development Kit (JDK):** Version 17 or higher (`java -version`)
* **Apache Maven:** Version 3.8 or higher (`mvn -version`)

---

### 📥 1. Clone the Repository
```bash
git clone https://github.com/Tirtho04/Tic-Tac-Toe-Web-Game-using-Java-Servlet-and-JSP.git
cd Tic-Tac-Toe-Web-Game-using-Java-Servlet-and-JSP
```

---

### 🧪 2. Run Automated Test Suite
Execute the 8 automated JUnit 5 test cases:
```bash
mvn test
```

Expected output:
```text
[INFO] Running Tic-Tac-Toe GameLogic Test Suite (Week 8 Testing)
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

---

### 🌐 3. Build & Run the Web Application
Launch the embedded Apache Tomcat 10 server with a single Maven command:
```bash
mvn package cargo:run
```

Once you see:
```text
[INFO] Tomcat 10.1.34 started on port [8080]
```

Open your web browser and navigate to:
👉 **`http://localhost:8080/TicTacToeWeb/`**

---

## 📂 Project Directory Structure

```text
Tic-Tac-Toe-Web-Game-using-Java-Servlet-and-JSP/
├── docs/
│   ├── DESIGN.md           # System architecture, UML diagrams & wireframes (Week 2)
│   ├── TEST_CASES.md       # Full 8 test case specifications & results (Week 8)
│   ├── REPORT.md           # Comprehensive technical project report (Week 9)
│   └── Week2_Design.html   # Visual design documentation
├── src/
│   ├── main/
│   │   ├── java/com/tictactoe/
│   │   │   ├── GameLogic.java     # Model: Board state, win/draw algorithms & scores
│   │   │   └── GameServlet.java   # Controller: Handles /play, routing & session PRG
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   └── web.xml        # Servlet mapping configuration
│   │       └── index.jsp          # View: Dark theme UI, live scoreboard & 3×3 board
│   └── test/
│       └── java/com/tictactoe/
│           └── GameLogicTest.java # JUnit 5 automated test suite (8 test cases)
├── pom.xml                 # Maven configuration (Tomcat Cargo & JUnit 5)
├── README.md               # Project documentation & quickstart guide
├── TEST_CASES.md           # Test suite reference
└── REPORT.md               # Project report reference
```

---

## 📊 10-Week Implementation Schedule Summary

| Week | Phase | Deliverables | Status |
| :---: | :---: | :--- | :---: |
| **Week 1** | **Setup** | Tools installation, NetBeans/Maven project setup, Git repository | ✅ Completed |
| **Week 2** | **Design** | Wireframe sketches, UML class diagrams, MVC documentation | ✅ Completed |
| **Week 3** | **Development** | `GameLogic.java` board matrix, `makeMove()`, `web.xml` | ✅ Completed |
| **Week 4** | **Development** | Interactive JSP board, `GameServlet.doPost()` with PRG redirect | ✅ Completed |
| **Week 5** | **Development** | 8-combo `checkWinner()`, `isDraw()`, game-over UI banners | ✅ Completed |
| **Week 6** | **Enhancement** | New Game reset button, winning cell highlights, color coding | ✅ Completed |
| **Week 7** | **Enhancement** | Session score tracker, live 3-tile scoreboard, Dark Theme UI | ✅ Completed |
| **Week 8** | **Testing** | `TEST_CASES.md`, JUnit 5 test suite (8 tests), Git tag `v1.0-tested` | ✅ Completed |
| **Week 9** | **Submission** | Javadoc on all classes, `REPORT.md`, finalised `README.md` | ✅ Completed |
| **Week 10** | **Submission** | Production WAR deployment, GitHub Release, submission demo | 🎯 Ready |

---

## 👤 Author
* **Developer:** [Tirtho04](https://github.com/Tirtho04)
* **Project:** Tic-Tac-Toe Web Game using Java Servlet & JSP
* **License:** MIT License
