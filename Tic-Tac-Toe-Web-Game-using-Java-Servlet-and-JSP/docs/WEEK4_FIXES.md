# Week 4 — Anomalies Found & Fixed

Per the work plan, Week 4's goal was: *"Build clickable JSP board; implement
GameServlet.doPost() with PRG redirect"* (Git milestone `v0.2-board`:
*"Clickable JSP board; servlet receives moves; PRG redirect works"*).

The drop this was built from did not actually satisfy that milestone. Here's
what was wrong and what changed.

## 1. `index.jsp` did not exist (critical — the Week 4 deliverable itself)
`web.xml` declares `index.jsp` as the welcome file, and `GameServlet`
already redirected to it after every GET/POST — but no such file was
present anywhere in the project. There was no way to actually play the
game or see a board. **Fix:** added `src/main/webapp/index.jsp`, a
clickable 3×3 board that reads `GameLogic` from the session, renders each
empty cell as a small POST form to `/play` with hidden `row`/`col` fields
(PRG pattern), and shows the current turn. Winner banner, New Game button,
score tracker, and CSS colour-coding are intentionally left for Weeks 5–7
per the plan.

## 2. `GameLogic.makeMove()` used the digit `'0'` instead of the letter `'O'`
```java
currentPlayer = (currentPlayer == 'X') ? '0' : 'X';   // was '0' (zero)
```
This is a one-character bug, but it's a serious one: player two's mark was
never the letter `O`. It would have rendered as a zero on the board and,
worse, would have silently broken every win-detection comparison written in
Week 5 (`cell == 'O'` would never match). **Fix:** changed to `'O'`.

## 3. `GameServlet.doPost()` had no input validation
```java
int row = Integer.parseInt(request.getParameter("row"));
int col = Integer.parseInt(request.getParameter("col"));
```
Any missing or non-numeric `row`/`col` (a hand-crafted request, a stray
`/play` hit, a browser extension, etc.) threw an uncaught
`NumberFormatException` and returned a raw HTTP 500 instead of the game
page. **Fix:** wrapped the parse in a `try/catch` — a malformed request is
now simply ignored and the player is redirected back to the current board
state. `GameLogic.makeMove()` was also hardened to bounds-check `row`/`col`
itself (0–2), so even a well-formed but out-of-range request (e.g. `row=9`)
can no longer throw `ArrayIndexOutOfBoundsException`.

## 4. Project structure didn't match a buildable web app
The Java sources were nested inside
`src/main/java/com/tictactoe/GameLogic.java/src/com/tictactoe/...` — an
entire separate NetBeans **Java SE (`j2seproject`)** project (its own
`build/`, `nbproject/`, `build.xml`) sitting *inside* what looked like a
Maven-style `src/main/java` folder. A plain Java SE project has no servlet
API on its classpath, so `GameServlet.java` (which imports
`jakarta.servlet.*`) could not have compiled in that setup regardless of
any other fix. Stale, out-of-date `.class` files were also present under
that nested `build/` folder. **Fix:** flattened the layout to the standard
Maven web-app shape:

```
src/main/java/com/tictactoe/GameLogic.java
src/main/java/com/tictactoe/GameServlet.java
src/main/webapp/index.jsp
src/main/webapp/WEB-INF/web.xml
```

## 5. No build file at all
Despite the Maven-style directory layout, there was no `pom.xml` (or
equivalent) anywhere, so there was no way to actually compile the sources
or package a WAR. **Fix:** added `pom.xml` (packaging `war`, Jakarta
Servlet API 5.0 as `provided`, `maven-war-plugin`) so the project builds
with:

```bash
mvn clean package
```
producing `target/TicTacToeWeb.war`, deployable to Tomcat 10+ (matches the
`web-app_5_0` schema already used in `web.xml`).

## 6. `.gitignore` was missing
The repo had committed compiled `.class` files and a NetBeans `build/`
directory. **Fix:** added a `.gitignore` for `target/`, `build/`, `*.class`,
`*.war`, and IDE/OS cruft, per the plan's own commit convention (`init:`
for the skeleton, `.gitignore` explicitly called out).

## How to verify locally
1. `mvn clean package`
2. Deploy `target/TicTacToeWeb.war` to Tomcat 10+ (or point NetBeans's
   built-in server at this project).
3. Visit `http://localhost:8080/TicTacToeWeb/`.
4. Click any empty cell — it POSTs to `/play`, the servlet updates
   `GameLogic` in the session, redirects back to `index.jsp`, and the
   board (and "Current Turn" label) reflects the move. Refreshing the page
   does **not** replay the move (PRG pattern confirmed).
