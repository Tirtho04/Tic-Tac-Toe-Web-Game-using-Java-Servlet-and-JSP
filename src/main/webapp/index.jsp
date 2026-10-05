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
    <meta name="description" content="A responsive, real-time Tic-Tac-Toe web game built with Java Servlets and JSP.">
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
            max-width: 430px;
            background-color: #1e293b;
            border: 1px solid #334155;
            border-radius: 20px;
            padding: 28px 22px;
            box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.5), 0 8px 10px -6px rgba(0, 0, 0, 0.4);
            text-align: center;
        }
        .title {
            font-size: 28px;
            font-weight: 800;
            letter-spacing: 1.5px;
            margin-bottom: 20px;
            background: linear-gradient(135deg, #38bdf8 0%, #a855f7 50%, #f87171 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }

        /* ── 1. Live Scoreboard ── */
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
            transition: transform 0.2s ease, box-shadow 0.2s ease, border-color 0.2s ease;
        }
        .score-card:hover {
            transform: translateY(-2px);
        }
        .score-card.x-card {
            border-color: rgba(56, 189, 248, 0.35);
            background: linear-gradient(180deg, rgba(56, 189, 248, 0.1) 0%, #0f172a 100%);
        }
        .score-card.x-card:hover {
            box-shadow: 0 4px 14px rgba(56, 189, 248, 0.25);
        }
        .score-card.o-card {
            border-color: rgba(248, 113, 113, 0.35);
            background: linear-gradient(180deg, rgba(248, 113, 113, 0.1) 0%, #0f172a 100%);
        }
        .score-card.o-card:hover {
            box-shadow: 0 4px 14px rgba(248, 113, 113, 0.25);
        }
        .score-card.draw-card {
            border-color: rgba(148, 163, 184, 0.3);
            background: linear-gradient(180deg, rgba(148, 163, 184, 0.08) 0%, #0f172a 100%);
        }
        .score-card.draw-card:hover {
            box-shadow: 0 4px 14px rgba(148, 163, 184, 0.2);
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
            font-size: 24px;
            font-weight: 800;
            color: #ffffff;
            font-variant-numeric: tabular-nums;
        }

        /* ── 2. Status Indicator ── */
        .status-box {
            min-height: 48px;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-bottom: 20px;
        }
        .turn-tag {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            background-color: #0f172a;
            border: 1px solid #334155;
            padding: 6px 18px;
            border-radius: 9999px;
            font-size: 14px;
            font-weight: 600;
            color: #cbd5e1;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
        }
        .badge {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            width: 24px;
            height: 24px;
            border-radius: 6px;
            font-weight: 800;
            font-size: 14px;
            transition: all 0.2s ease;
        }
        .badge-x {
            background-color: rgba(56, 189, 248, 0.2);
            color: #38bdf8;
            border: 1px solid #38bdf8;
            animation: pulseRingX 2s infinite ease-in-out;
        }
        .badge-o {
            background-color: rgba(248, 113, 113, 0.2);
            color: #f87171;
            border: 1px solid #f87171;
            animation: pulseRingO 2s infinite ease-in-out;
        }
        @keyframes pulseRingX {
            0%, 100% { box-shadow: 0 0 0 0 rgba(56, 189, 248, 0.4); }
            50% { box-shadow: 0 0 0 6px rgba(56, 189, 248, 0); }
        }
        @keyframes pulseRingO {
            0%, 100% { box-shadow: 0 0 0 0 rgba(248, 113, 113, 0.4); }
            50% { box-shadow: 0 0 0 6px rgba(248, 113, 113, 0); }
        }
        .banner {
            display: inline-block;
            padding: 10px 22px;
            border-radius: 9999px;
            font-size: 15px;
            font-weight: 700;
            letter-spacing: 0.3px;
            box-shadow: 0 6px 18px rgba(0, 0, 0, 0.35);
            animation: bannerSlideDown 0.35s cubic-bezier(0.34, 1.56, 0.64, 1);
        }
        @keyframes bannerSlideDown {
            from { transform: translateY(-12px); opacity: 0; }
            to { transform: translateY(0); opacity: 1; }
        }
        .banner.win-x {
            background-color: rgba(56, 189, 248, 0.18);
            color: #38bdf8;
            border: 1px solid #38bdf8;
        }
        .banner.win-o {
            background-color: rgba(248, 113, 113, 0.18);
            color: #f87171;
            border: 1px solid #f87171;
        }
        .banner.draw {
            background-color: rgba(251, 191, 36, 0.18);
            color: #fbbf24;
            border: 1px solid #fbbf24;
        }

        /* ── 3. 3×3 Game Board ── */
        .board-wrap {
            display: flex;
            justify-content: center;
            margin-bottom: 24px;
        }
        table.board {
            border-collapse: separate;
            border-spacing: 10px;
        }
        table.board td {
            width: 98px;
            height: 98px;
            background-color: #0f172a;
            border: 1px solid #334155;
            border-radius: 16px;
            padding: 0;
            text-align: center;
            vertical-align: middle;
            box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.3);
            transition: all 0.25s ease;
        }
        table.board td.cell-win {
            background-color: rgba(34, 197, 94, 0.22) !important;
            border-color: #22c55e !important;
            animation: glow-win 1.4s infinite alternate ease-in-out;
        }
        @keyframes glow-win {
            from {
                border-color: #22c55e;
                box-shadow: 0 0 14px rgba(34, 197, 94, 0.4), inset 0 0 8px rgba(34, 197, 94, 0.3);
                transform: scale(1.02);
            }
            to {
                border-color: #86efac;
                box-shadow: 0 0 24px rgba(34, 197, 94, 0.7), inset 0 0 14px rgba(34, 197, 94, 0.5);
                transform: scale(1.04);
            }
        }
        .mark-x, .mark-o {
            display: inline-block;
            font-size: 46px;
            font-weight: 900;
            user-select: none;
            animation: markPop 0.25s cubic-bezier(0.34, 1.56, 0.64, 1) forwards;
        }
        .mark-x {
            color: #38bdf8;
            text-shadow: 0 0 16px rgba(56, 189, 248, 0.6), 0 0 30px rgba(56, 189, 248, 0.2);
        }
        .mark-o {
            color: #f87171;
            text-shadow: 0 0 16px rgba(248, 113, 113, 0.6), 0 0 30px rgba(248, 113, 113, 0.2);
        }
        @keyframes markPop {
            0% { transform: scale(0.2) rotate(-8deg); opacity: 0; }
            70% { transform: scale(1.15) rotate(3deg); opacity: 1; }
            100% { transform: scale(1) rotate(0deg); opacity: 1; }
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
            border-radius: 16px;
            cursor: pointer;
            touch-action: manipulation;
            transition: background-color 0.2s ease, transform 0.15s ease, box-shadow 0.2s ease;
        }
        table.board button:hover {
            background-color: rgba(255, 255, 255, 0.06);
            box-shadow: inset 0 0 12px rgba(255, 255, 255, 0.04);
            transform: scale(0.96);
        }
        table.board button:focus-visible {
            outline: 2px solid #38bdf8;
            outline-offset: -2px;
        }
        table.board button:active {
            transform: scale(0.92);
        }
        .cell-disabled {
            width: 100%;
            height: 100%;
            display: flex;
            align-items: center;
            justify-content: center;
            cursor: default;
        }

        /* ── 4. Control Action Buttons ── */
        .actions {
            display: flex;
            gap: 12px;
            justify-content: center;
        }
        .btn {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            gap: 6px;
            padding: 11px 20px;
            font-size: 14px;
            font-weight: 700;
            border-radius: 12px;
            border: 1px solid transparent;
            cursor: pointer;
            touch-action: manipulation;
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
            transform: translateY(-2px);
        }
        .btn-primary:active {
            transform: translateY(1px);
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
            transform: translateY(-2px);
        }
        .btn-secondary:active {
            transform: translateY(1px);
        }
        .btn:focus-visible {
            outline: 2px solid #38bdf8;
            outline-offset: 2px;
        }
    </style>
</head>
<body>

<main class="container" role="main">
    <h1 class="title">TIC-TAC-TOE</h1>

    <!-- ── 1. Live Scoreboard ── -->
    <section class="scoreboard" aria-label="Game Scoreboard">
        <div class="score-card x-card" aria-label="Player X Score">
            <div class="score-label">Player X</div>
            <div class="score-val"><%= game.getXWins() %></div>
        </div>
        <div class="score-card draw-card" aria-label="Ties Count">
            <div class="score-label">Ties</div>
            <div class="score-val"><%= game.getDraws() %></div>
        </div>
        <div class="score-card o-card" aria-label="Player O Score">
            <div class="score-label">Player O</div>
            <div class="score-val"><%= game.getOWins() %></div>
        </div>
    </section>

    <!-- ── 2. Status / Turn Indicator ── -->
    <section class="status-box" aria-live="polite" aria-atomic="true">
        <% if (winner != ' ') { %>
            <div class="banner <%= winner == 'X' ? "win-x" : "win-o" %>" role="alert">
                🎉 Player <%= winner %> Wins This Round!
            </div>
        <% } else if (isDraw) { %>
            <div class="banner draw" role="alert">🤝 It's a Draw!</div>
        <% } else { %>
            <div class="turn-tag">
                <span class="badge <%= currentTurn == 'X' ? "badge-x" : "badge-o" %>"><%= currentTurn %></span>
                <span>Turn</span>
            </div>
        <% } %>
    </section>

    <!-- ── 3. 3×3 Grid Board ── -->
    <section class="board-wrap" aria-label="Tic Tac Toe Board">
        <table class="board" role="grid" aria-readonly="<%= gameOver %>">
            <% for (int row = 0; row < 3; row++) { %>
            <tr role="row">
                <% for (int col = 0; col < 3; col++) {
                       char mark = board[row][col];
                       boolean isWinCell = game.isWinningCell(row, col);
                       int humanRow = row + 1;
                       int humanCol = col + 1;
                %>
                <td role="gridcell" class="<%= isWinCell ? "cell-win" : "" %>">
                    <% if (mark == 'X') { %>
                        <span class="mark-x" aria-label="Row <%= humanRow %> Column <%= humanCol %>, X">X</span>
                    <% } else if (mark == 'O') { %>
                        <span class="mark-o" aria-label="Row <%= humanRow %> Column <%= humanCol %>, O">O</span>
                    <% } else if (!gameOver) { %>
                        <form method="post" action="play">
                            <input type="hidden" name="row" value="<%= row %>">
                            <input type="hidden" name="col" value="<%= col %>">
                            <button type="submit" aria-label="Row <%= humanRow %> Column <%= humanCol %>, Empty - Click to place <%= currentTurn %>">&nbsp;</button>
                        </form>
                    <% } else { %>
                        <div class="cell-disabled" aria-label="Row <%= humanRow %> Column <%= humanCol %>, Empty">&nbsp;</div>
                    <% } %>
                </td>
                <% } %>
            </tr>
            <% } %>
        </table>
    </section>

    <!-- ── 4. Control Action Buttons ── -->
    <nav class="actions" aria-label="Game Controls">
        <form method="post" action="play" style="display:inline;">
            <input type="hidden" name="action" value="reset">
            <button type="submit" class="btn btn-primary" aria-label="Start a new round">🔄 New Round</button>
        </form>
        <form method="post" action="play" style="display:inline;">
            <input type="hidden" name="action" value="resetScores">
            <button type="submit" class="btn btn-secondary" aria-label="Reset all match scores to zero">🗑️ Reset Score</button>
        </form>
    </nav>
</main>

<script>
    // Asynchronous move and action submission to prevent page reload blinking
    document.addEventListener('DOMContentLoaded', () => {
        const container = document.querySelector('.container');
        if (!container) return;

        container.addEventListener('submit', async (e) => {
            const form = e.target.closest('form');
            if (!form) return;

            e.preventDefault();

            try {
                const formData = new FormData(form);
                const response = await fetch(form.action || 'play', {
                    method: 'POST',
                    body: new URLSearchParams(formData)
                });

                if (response.ok) {
                    const htmlText = await response.text();
                    const parser = new DOMParser();
                    const doc = parser.parseFromString(htmlText, 'text/html');
                    const newContainer = doc.querySelector('.container');
                    if (newContainer) {
                        container.innerHTML = newContainer.innerHTML;
                    }
                } else {
                    form.submit();
                }
            } catch (err) {
                form.submit();
            }
        });
    });
</script>

</body>
</html>
