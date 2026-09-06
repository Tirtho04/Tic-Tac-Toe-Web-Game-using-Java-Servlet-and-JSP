<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.tictactoe.GameLogic" %>
<%
    // Get-or-create the game for this session.
    GameLogic game = (GameLogic) session.getAttribute("game");
    if (game == null) {
        game = new GameLogic();
        session.setAttribute("game", game);
    }
    char[][] board = game.getBoard();
    boolean gameOver = game.isGameOver();
    char winner = game.getWinner();
    boolean isDraw = game.isDraw();
    char currentTurn = game.getCurrentPlayer();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tic-Tac-Toe Web Game</title>
    <style>
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }
        body {
            font-family: 'Segoe UI', system-ui, -apple-system, BlinkMacSystemFont, Roboto, sans-serif;
            min-height: 100vh;
            background: radial-gradient(circle at top, #1e293b 0%, #0b0f19 100%);
            color: #f8fafc;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 24px 16px;
        }
        .container {
            width: 100%;
            max-width: 420px;
            background-color: #1e293b;
            border: 1px solid #334155;
            border-radius: 20px;
            padding: 28px 20px;
            box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.5), 0 8px 10px -6px rgba(0, 0, 0, 0.4);
            text-align: center;
        }
        .title {
            font-size: 26px;
            font-weight: 800;
            letter-spacing: 1px;
            margin-bottom: 20px;
            background: linear-gradient(135deg, #38bdf8 0%, #a855f7 50%, #f87171 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }

        /* ── Live Scoreboard ── */
        .scoreboard {
            display: grid;
            grid-template-columns: 1fr 1fr 1fr;
            gap: 10px;
            margin-bottom: 20px;
        }
        .score-card {
            border-radius: 12px;
            padding: 10px 6px;
            text-align: center;
            background-color: #0f172a;
            border: 1px solid #334155;
            transition: transform 0.2s ease, box-shadow 0.2s ease;
        }
        .score-card:hover {
            transform: translateY(-2px);
        }
        .score-card.x-card {
            border-color: rgba(56, 189, 248, 0.35);
            background: linear-gradient(180deg, rgba(56, 189, 248, 0.1) 0%, #0f172a 100%);
        }
        .score-card.o-card {
            border-color: rgba(248, 113, 113, 0.35);
            background: linear-gradient(180deg, rgba(248, 113, 113, 0.1) 0%, #0f172a 100%);
        }
        .score-card.draw-card {
            border-color: rgba(148, 163, 184, 0.3);
            background: linear-gradient(180deg, rgba(148, 163, 184, 0.08) 0%, #0f172a 100%);
        }
        .score-label {
            font-size: 11px;
            font-weight: 700;
            letter-spacing: 0.5px;
            text-transform: uppercase;
            margin-bottom: 4px;
        }
        .x-card .score-label { color: #38bdf8; }
        .o-card .score-label { color: #f87171; }
        .draw-card .score-label { color: #94a3b8; }
        .score-val {
            font-size: 22px;
            font-weight: 800;
            color: #ffffff;
        }

        /* ── Status Indicator ── */
        .status-box {
            min-height: 46px;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-bottom: 18px;
        }
        .turn-tag {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            background-color: #0f172a;
            border: 1px solid #334155;
            padding: 6px 16px;
            border-radius: 9999px;
            font-size: 14px;
            font-weight: 600;
            color: #cbd5e1;
        }
        .badge {
            display: inline-block;
            width: 22px;
            height: 22px;
            line-height: 22px;
            border-radius: 6px;
            font-weight: 800;
            font-size: 13px;
        }
        .badge-x {
            background-color: rgba(56, 189, 248, 0.2);
            color: #38bdf8;
            border: 1px solid #38bdf8;
        }
        .badge-o {
            background-color: rgba(248, 113, 113, 0.2);
            color: #f87171;
            border: 1px solid #f87171;
        }
        .banner {
            display: inline-block;
            padding: 8px 20px;
            border-radius: 9999px;
            font-size: 15px;
            font-weight: 700;
            letter-spacing: 0.3px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.25);
        }
        .banner.win-x {
            background-color: rgba(56, 189, 248, 0.15);
            color: #38bdf8;
            border: 1px solid #38bdf8;
        }
        .banner.win-o {
            background-color: rgba(248, 113, 113, 0.15);
            color: #f87171;
            border: 1px solid #f87171;
        }
        .banner.draw {
            background-color: rgba(251, 191, 36, 0.15);
            color: #fbbf24;
            border: 1px solid #fbbf24;
        }

        /* ── 3×3 Game Board ── */
        .board-wrap {
            display: flex;
            justify-content: center;
            margin-bottom: 22px;
        }
        table.board {
            border-collapse: separate;
            border-spacing: 8px;
        }
        table.board td {
            width: 96px;
            height: 96px;
            background-color: #0f172a;
            border: 1px solid #334155;
            border-radius: 14px;
            padding: 0;
            text-align: center;
            vertical-align: middle;
            box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.3);
            transition: all 0.2s ease;
        }
        table.board td.cell-win {
            background-color: rgba(34, 197, 94, 0.2) !important;
            border-color: #22c55e !important;
            box-shadow: 0 0 16px rgba(34, 197, 94, 0.4), inset 0 0 8px rgba(34, 197, 94, 0.3);
            animation: glow-win 1.2s infinite alternate;
        }
        @keyframes glow-win {
            from { border-color: #22c55e; box-shadow: 0 0 12px rgba(34, 197, 94, 0.3); }
            to { border-color: #86efac; box-shadow: 0 0 22px rgba(34, 197, 94, 0.6); }
        }
        .mark-x {
            font-size: 42px;
            font-weight: 900;
            color: #38bdf8;
            text-shadow: 0 0 14px rgba(56, 189, 248, 0.6);
        }
        .mark-o {
            font-size: 42px;
            font-weight: 900;
            color: #f87171;
            text-shadow: 0 0 14px rgba(248, 113, 113, 0.6);
        }
        table.board form {
            margin: 0;
            width: 100%;
            height: 100%;
        }
        table.board button {
            width: 100%;
            height: 100%;
            background: transparent;
            border: none;
            border-radius: 14px;
            cursor: pointer;
            transition: background-color 0.15s ease, transform 0.1s ease;
        }
        table.board button:hover {
            background-color: rgba(255, 255, 255, 0.05);
            transform: scale(0.96);
        }
        .cell-disabled {
            width: 100%;
            height: 100%;
            display: flex;
            align-items: center;
            justify-content: center;
            cursor: default;
        }

        /* ── Controls / Action Buttons ── */
        .actions {
            display: flex;
            gap: 10px;
            justify-content: center;
        }
        .btn {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            gap: 6px;
            padding: 10px 18px;
            font-size: 14px;
            font-weight: 700;
            border-radius: 10px;
            border: 1px solid transparent;
            cursor: pointer;
            transition: all 0.2s ease;
        }
        .btn-primary {
            background-color: #2563eb;
            color: #ffffff;
            border-color: #3b82f6;
            box-shadow: 0 4px 12px rgba(37, 99, 235, 0.35);
        }
        .btn-primary:hover {
            background-color: #1d4ed8;
            box-shadow: 0 6px 16px rgba(37, 99, 235, 0.5);
            transform: translateY(-1px);
        }
        .btn-secondary {
            background-color: #0f172a;
            color: #94a3b8;
            border-color: #334155;
        }
        .btn-secondary:hover {
            background-color: #334155;
            color: #f87171;
            border-color: rgba(248, 113, 113, 0.4);
            transform: translateY(-1px);
        }
        .btn:active {
            transform: translateY(1px);
        }
    </style>
</head>
<body>

<div class="container">
    <h1 class="title">TIC-TAC-TOE</h1>

    <!-- ── 1. Live Scoreboard ── -->
    <div class="scoreboard">
        <div class="score-card x-card">
            <div class="score-label">Player X</div>
            <div class="score-val"><%= game.getXWins() %></div>
        </div>
        <div class="score-card draw-card">
            <div class="score-label">Ties</div>
            <div class="score-val"><%= game.getDraws() %></div>
        </div>
        <div class="score-card o-card">
            <div class="score-label">Player O</div>
            <div class="score-val"><%= game.getOWins() %></div>
        </div>
    </div>

    <!-- ── 2. Status / Turn Indicator ── -->
    <div class="status-box">
        <% if (winner != ' ') { %>
            <div class="banner <%= winner == 'X' ? "win-x" : "win-o" %>">
                🎉 Player <%= winner %> Wins This Round!
            </div>
        <% } else if (isDraw) { %>
            <div class="banner draw">🤝 It's a Draw!</div>
        <% } else { %>
            <div class="turn-tag">
                <span class="badge <%= currentTurn == 'X' ? "badge-x" : "badge-o" %>"><%= currentTurn %></span>
                <span>Turn</span>
            </div>
        <% } %>
    </div>

    <!-- ── 3. 3×3 Grid Board ── -->
    <div class="board-wrap">
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
    </div>

    <!-- ── 4. Control Action Buttons ── -->
    <div class="actions">
        <form method="post" action="play" style="display:inline;">
            <input type="hidden" name="action" value="reset">
            <button type="submit" class="btn btn-primary">🔄 New Round</button>
        </form>
        <form method="post" action="play" style="display:inline;">
            <input type="hidden" name="action" value="resetScores">
            <button type="submit" class="btn btn-secondary">🗑️ Reset Score</button>
        </form>
    </div>
</div>

</body>
</html>
