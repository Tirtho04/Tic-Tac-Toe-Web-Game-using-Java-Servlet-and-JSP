<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.tictactoe.GameLogic" %>
<%--
    index.jsp
    View for the Tic Tac Toe web game (MVC "View" per docs/DESIGN.md).

    Week 4: this file did not exist yet in the previous drop, even though
    web.xml declares it as the welcome-file and GameServlet already
    redirects to it after every GET/POST. Without it the app had no view
    to redirect to. Added here: a clickable 3x3 board wired to the
    GameServlet /play endpoint using the PRG pattern.

    Winner/draw banner, New Game button, score tracker, highlighting and
    CSS colour-coding are out of scope for Week 4 (see Weeks 5-7 in the
    work plan) and are left as later enhancements.
--%>
<%
    // Read GameLogic from session, or create a fresh game on first visit.
    GameLogic game = (GameLogic) session.getAttribute("game");
    if (game == null) {
        game = new GameLogic();
        session.setAttribute("game", game);
    }
    char[][] board = game.getBoard();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Tic Tac Toe</title>
    <style>
        body {
            font-family: Arial, Helvetica, sans-serif;
            text-align: center;
            margin-top: 40px;
        }
        h1 {
            margin-bottom: 0.25em;
        }
        .turn {
            font-size: 1.1em;
            margin-bottom: 1em;
        }
        table.board {
            border-collapse: collapse;
            margin: 0 auto;
        }
        table.board td {
            width: 80px;
            height: 80px;
            border: 2px solid #333;
            padding: 0;
            text-align: center;
            vertical-align: middle;
        }
        table.board form {
            margin: 0;
        }
        table.board button {
            width: 100%;
            height: 100%;
            font-size: 2em;
            border: none;
            background: #fafafa;
            cursor: pointer;
        }
        table.board button:hover {
            background: #eee;
        }
        .mark {
            font-size: 2em;
            font-weight: bold;
        }
    </style>
</head>
<body>

    <h1>Tic Tac Toe</h1>
    <p class="turn">Current Turn: <strong><%= game.getCurrentPlayer() %></strong></p>

    <table class="board">
        <% for (int row = 0; row < 3; row++) { %>
        <tr>
            <% for (int col = 0; col < 3; col++) {
                   char cell = board[row][col];
            %>
            <td>
                <% if (cell == ' ') { %>
                    <form method="post" action="play">
                        <input type="hidden" name="row" value="<%= row %>">
                        <input type="hidden" name="col" value="<%= col %>">
                        <button type="submit" aria-label="Row <%= row %>, Column <%= col %>">&nbsp;</button>
                    </form>
                <% } else { %>
                    <span class="mark"><%= cell %></span>
                <% } %>
            </td>
            <% } %>
        </tr>
        <% } %>
    </table>

    <!-- New Game / winner banner / scoreboard: added in later weeks -->

</body>
</html>
