<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.tictactoe.GameLogic" %>
<%
    // Get-or-create the game for this session. This is the one place
    // (besides GameServlet) that touches the "game" session attribute,
    // since a fresh visit to index.jsp never goes through the servlet.
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
    <title>Tic-Tac-Toe</title>
    <style>
        body {
            font-family: sans-serif;
            text-align: center;
            margin-top: 40px;
        }
        table.board {
            border-collapse: collapse;
            margin: 20px auto;
        }
        table.board td {
            width: 80px;
            height: 80px;
            border: 2px solid #333;
            padding: 0;
            text-align: center;
            vertical-align: middle;
            font-size: 32px;
        }
        table.board form {
            margin: 0;
            width: 100%;
            height: 100%;
        }
        table.board button {
            width: 100%;
            height: 100%;
            font-size: 32px;
            border: none;
            background: #f5f5f5;
            cursor: pointer;
        }
        table.board button:hover {
            background: #e0e0e0;
        }
    </style>
</head>
<body>

    <h1>Tic-Tac-Toe</h1>
    <p>Turn: <strong><%= game.getCurrentPlayer() %></strong></p>

    <table class="board">
        <% for (int row = 0; row < 3; row++) { %>
        <tr>
            <% for (int col = 0; col < 3; col++) {
                   char mark = board[row][col];
            %>
            <td>
                <% if (mark != ' ') { %>
                    <%= mark %>
                <% } else { %>
                    <form method="post" action="play">
                        <input type="hidden" name="row" value="<%= row %>">
                        <input type="hidden" name="col" value="<%= col %>">
                        <button type="submit">&nbsp;</button>
                    </form>
                <% } %>
            </td>
            <% } %>
        </tr>
        <% } %>
    </table>

</body>
</html>
