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
    boolean gameOver = game.isGameOver();
    char winner = game.getWinner();
    boolean isDraw = game.isDraw();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Tic-Tac-Toe</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            text-align: center;
            margin-top: 40px;
            background-color: #f8fafc;
            color: #1e293b;
        }
        h1 {
            margin-bottom: 8px;
            color: #0f172a;
        }
        .status-container {
            min-height: 48px;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-bottom: 12px;
        }
        .turn-indicator {
            font-size: 18px;
            font-weight: 500;
            color: #334155;
        }
        .turn-indicator span {
            font-weight: bold;
            color: #2563eb;
        }
        .banner {
            display: inline-block;
            padding: 10px 24px;
            border-radius: 8px;
            font-size: 18px;
            font-weight: bold;
            letter-spacing: 0.5px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
        }
        .banner.win {
            background-color: #dcfce7;
            color: #15803d;
            border: 1px solid #86efac;
        }
        .banner.draw {
            background-color: #fef9c3;
            color: #854d0e;
            border: 1px solid #fde047;
        }
        table.board {
            border-collapse: collapse;
            margin: 20px auto;
            background: #ffffff;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
            border-radius: 8px;
            overflow: hidden;
        }
        table.board td {
            width: 90px;
            height: 90px;
            border: 2px solid #cbd5e1;
            padding: 0;
            text-align: center;
            vertical-align: middle;
            font-size: 36px;
            font-weight: bold;
            transition: background-color 0.2s ease;
        }
        table.board td.cell-win {
            background-color: #dcfce7;
            animation: pulse-win 1.2s infinite alternate;
        }
        @keyframes pulse-win {
            from { background-color: #dcfce7; }
            to { background-color: #bbf7d0; }
        }
        .mark-x {
            color: #2563eb;
        }
        .mark-o {
            color: #dc2626;
        }
        table.board form {
            margin: 0;
            width: 100%;
            height: 100%;
        }
        table.board button {
            width: 100%;
            height: 100%;
            font-size: 36px;
            border: none;
            background: #f8fafc;
            cursor: pointer;
            transition: background-color 0.15s ease;
        }
        table.board button:hover {
            background: #e2e8f0;
        }
        .cell-disabled {
            background-color: #f1f5f9;
            width: 100%;
            height: 100%;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .controls {
            margin-top: 24px;
        }
        .btn-reset {
            background-color: #2563eb;
            color: #ffffff;
            border: none;
            padding: 10px 24px;
            font-size: 15px;
            font-weight: 600;
            border-radius: 8px;
            cursor: pointer;
            box-shadow: 0 2px 6px rgba(37, 99, 235, 0.25);
            transition: background-color 0.2s, transform 0.1s, box-shadow 0.2s;
        }
        .btn-reset:hover {
            background-color: #1d4ed8;
            box-shadow: 0 4px 12px rgba(37, 99, 235, 0.35);
        }
        .btn-reset:active {
            transform: scale(0.98);
        }
    </style>
</head>
<body>

    <h1>Tic-Tac-Toe</h1>

    <div class="status-container">
        <% if (winner != ' ') { %>
            <div class="banner win">🎉 Player <%= winner %> Wins!</div>
        <% } else if (isDraw) { %>
            <div class="banner draw">🤝 It's a Draw!</div>
        <% } else { %>
            <div class="turn-indicator">Turn: <span>Player <%= game.getCurrentPlayer() %></span></div>
        <% } %>
    </div>

    <table class="board">
        <% for (int row = 0; row < 3; row++) { %>
        <tr>
            <% for (int col = 0; col < 3; col++) {
                   char mark = board[row][col];
                   boolean isWinCell = game.isWinningCell(row, col);
            %>
            <td class="<%= isWinCell ? "cell-win" : "" %>">
                <% if (mark == 'X') { %>
                    <span class="mark-x">X</span>
                <% } else if (mark == 'O') { %>
                    <span class="mark-o">O</span>
                <% } else if (!gameOver) { %>
                    <form method="post" action="play">
                        <input type="hidden" name="row" value="<%= row %>">
                        <input type="hidden" name="col" value="<%= col %>">
                        <button type="submit" aria-label="Cell <%= row %>, <%= col %>">&nbsp;</button>
                    </form>
                <% } else { %>
                    <div class="cell-disabled">&nbsp;</div>
                <% } %>
            </td>
            <% } %>
        </tr>
        <% } %>
    </table>

    <div class="controls">
        <form method="post" action="play">
            <input type="hidden" name="action" value="reset">
            <button type="submit" class="btn-reset">🔄 New Game</button>
        </form>
    </div>

</body>
</html>
