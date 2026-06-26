package com.tictactoe;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = {"/play", "/reset"})
public class GameServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // Create a new game if one does not yet exist in the sessions
        if (session.getAttribute("game") == null) {
            session.setAttribute("game", new GameLogic());
            session.setAttribute("scoreX", 0);
            session.setAttribute("scoreO", 0);
        }

        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        GameLogic game = (GameLogic) session.getAttribute("game");
        if (game == null) {
            game = new GameLogic();
            session.setAttribute("game", game);
        }

        // Initialise score counters if they are missing (first visit)
        if (session.getAttribute("scoreX") == null) {
            session.setAttribute("scoreX", 0);
        }
        if (session.getAttribute("scoreO") == null) {
            session.setAttribute("scoreO", 0);
        }
        String action = request.getParameter("action");
        if ("reset".equals(action)) {
            // New Game
            game.resetBoard();
            session.setAttribute("game", game);
            session.setAttribute("message", "");

        } else {
            char currentWinner = game.getwinner();
            boolean gameOver = (currentWinner != ' ') || game.isDraw();

            if (!gameOver) {
                String rowParam = request.getParameter("row");
                String colParam = request.getParameter("col");

                if (rowParam != null && colParam != null) {
                    try {
                        int row = Integer.parseInt(rowParam);
                        int col = Integer.parseInt(colParam);

                        boolean moveMade = game.makeMove(row, col);

                        if (moveMade) {
                            // Check whether this move ends the game
                            char winner = game.winner();
                            if (winner == 'X') {
                                int scoreX = (int) session.getAttribute("scoreX");
                                session.setAttribute("scoreX", scoreX + 1);
                                session.setAttribute("message", "Player X wins!");
                            } else if (winner == 'O') {
                                int scoreO = (int) session.getAttribute("scoreO");
                                session.setAttribute("scoreO", scoreO + 1);
                                session.setAttribute("message", "Player O wins!");
                            } else if (game.isDraw()) {
                                session.setAttribute("message", "It's a draw!");
                            } else {
                                session.setAttribute("message", "");
                            }

                            session.setAttribute("game", game);
                        }
                        // If moveMade is false the cell was already taken; ignore silently.

                    } catch (NumberFormatException e) {
                        // Malformed row/col — ignore and redirect back
                    }
                }
            }
        }

        response.sendRedirect(request.getContextPath() + "/index.jsp");
    }
}
