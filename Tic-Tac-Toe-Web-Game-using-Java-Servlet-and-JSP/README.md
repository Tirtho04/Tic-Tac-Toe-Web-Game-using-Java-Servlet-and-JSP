# Tic-Tac-Toe-Web-Game-using-Java-Servlet-and-JSP

A web-based Tic-Tac-Toe game built with Java Servlet and JavaServer Pages (JSP). Two players play on a dynamic 3×3 board through a browser. The project demonstrates game logic, event handling, state management, and JSP-Servlet communication in Java web development.

## Status: Week 4 complete (`v0.2-board`)

Progress through the 10-week plan in `workplan/Work Plan Tic Tac Toe.pdf`:

- **Week 1 — Setup:** NetBeans/Maven web project skeleton, README, `.gitignore`.
- **Week 2 — Design:** wireframe, UML class diagram, MVC architecture — see `docs/DESIGN.md` and `docs/Week2_Design.html`.
- **Week 3 — Development:** `GameLogic.java` (board, `makeMove()`, `resetBoard()`), `GameServlet` registered in `web.xml`.
- **Week 4 — Development:** clickable `index.jsp` board, `GameServlet.doPost()` with PRG (Post-Redirect-Get) redirect. See `docs/WEEK4_FIXES.md` for the anomalies found in this batch and how they were fixed.

Not yet implemented (later weeks, by design): win/draw detection and banner (Week 5), New Game reset + winning-cell highlight + CSS colour-coding (Week 6), session score tracker + dark theme (Week 7), test suite (Week 8), Javadoc/report (Week 9), final WAR release (Week 10).

## Project structure

```
pom.xml
src/main/java/com/tictactoe/GameLogic.java     — model (board, move logic)
src/main/java/com/tictactoe/GameServlet.java   — controller (/play, /reset)
src/main/webapp/index.jsp                      — view (clickable board)
src/main/webapp/WEB-INF/web.xml                — servlet + welcome-file config
docs/DESIGN.md, docs/Week2_Design.html         — Week 2 design docs
docs/WEEK4_FIXES.md                            — Week 4 anomaly/fix log
workplan/Work Plan Tic Tac Toe.pdf             — 10-week work plan
```

## Running it

Requires JDK 17+ and Maven on your PATH.

### One command (recommended)

From the project root (e.g. the VS Code integrated terminal, `Terminal > New Terminal`):

```bash
mvn clean package cargo:run
```

This uses the [Cargo Maven plugin](https://codehaus-cargo.github.io/) to
download a real **Apache Tomcat 10.1.31** the first time (cached under
`~/.m2/../cargo/downloads` after that — later runs are much faster),
compile the sources, package the WAR, deploy it into that Tomcat, and
start the server — all in one step. This matches the container named in
the work plan (Apache Tomcat) exactly, rather than an embedded substitute.

Once you see Cargo log that the container has started, open:

```
http://localhost:8080/TicTacToeWeb/
```

Stop the server with `Ctrl+C` in the terminal.

> An earlier version of these instructions used the `jetty-maven-plugin`
> for a lighter-weight embedded server. That path hit a known
> `apache-jsp` / `JuliLog` `ServiceConfigurationError` in this Jetty
> version when serving JSPs, so the project now uses Cargo + real Tomcat
> instead, which doesn't have that issue.

### Alternative: build a WAR and deploy to Tomcat manually

```bash
mvn clean package
```

Deploy the resulting `target/TicTacToeWeb.war` to your own Tomcat 10+
install, then open the same URL above.

Click any empty cell to place the current player's mark. The board state
is kept in the HTTP session, so it persists across requests but resets
when the session ends (a dedicated "New Game" button arrives in Week 6).