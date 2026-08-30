package com.tictactoe;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class GameServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if ("reset".equalsIgnoreCase(action)) {
            handleReset(request);
        }
        response.sendRedirect("index.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(true);

        GameLogic game = (GameLogic) session.getAttribute("game");
        if (game == null) {
            game = new GameLogic();
            session.setAttribute("game", game);
        }

        String action = request.getParameter("action");
        if ("reset".equalsIgnoreCase(action)) {
            game.resetBoard();
            session.setAttribute("game", game);
        } else {
            // Defensive parsing: "row"/"col" params may be missing, empty, or
            // malformed (e.g. a hand-crafted POST, or a stale/replayed form).
            // Never let a bad param blow up as an uncaught exception -- just
            // skip applying the move and fall through to the PRG redirect so
            // the board still renders with whatever state it already had.
            Integer row = parseCoordinate(request.getParameter("row"));
            Integer col = parseCoordinate(request.getParameter("col"));

            if (row != null && col != null) {
                game.makeMove(row, col);
                session.setAttribute("game", game);
            }
        }

        // Post-Redirect-Get: always redirect after a POST so a browser
        // refresh re-issues a GET against index.jsp instead of replaying
        // the move.
        response.sendRedirect("index.jsp");
    }

    private void handleReset(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            GameLogic game = (GameLogic) session.getAttribute("game");
            if (game != null) {
                game.resetBoard();
                session.setAttribute("game", game);
            }
        }
    }

    /**
     * Parses a request parameter expected to be a single board coordinate
     * (0-2). Returns null instead of throwing for any missing, blank, or
     * non-numeric input -- range checking (0-2) is left to GameLogic.makeMove,
     * which already validates bounds.
     */
    private Integer parseCoordinate(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(raw.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
