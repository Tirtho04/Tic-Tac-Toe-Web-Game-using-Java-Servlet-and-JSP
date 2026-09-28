package com.tictactoe;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Controller component in the MVC architecture that handles player interactions and actions.
 * <p>
 * Mapped to the {@code /play} endpoint. Manages game sessions, processes move submissions,
 * handles "New Round" (board reset) and "Reset Scores" commands, and implements the
 * Post-Redirect-Get (PRG) pattern by redirecting to {@code index.jsp} after every request.
 * </p>
 *
 * @author Tirtho04
 * @version 1.0
 * @see GameLogic
 */
public class GameServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Handles HTTP GET requests for direct action triggers (e.g. reset actions via links).
     *
     * @param request  the {@link HttpServletRequest} containing query parameters
     * @param response the {@link HttpServletResponse} for issuing redirects
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs during redirection
     */
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if ("resetScores".equalsIgnoreCase(action)) {
            handleResetScores(request);
        } else if ("reset".equalsIgnoreCase(action)) {
            handleReset(request);
        }
        response.sendRedirect("index.jsp");
    }

    /**
     * Handles HTTP POST requests containing player moves or action commands.
     * <p>
     * Retrieves or initializes the {@link GameLogic} model in the {@link HttpSession}.
     * Executes {@code resetScores}, {@code reset}, or processes cell coordinate inputs
     * ({@code row} and {@code col}), then redirects back to {@code index.jsp} (PRG pattern).
     * </p>
     *
     * @param request  the {@link HttpServletRequest} containing move or action data
     * @param response the {@link HttpServletResponse} for issuing redirects
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs during redirection
     */
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
        if ("resetScores".equalsIgnoreCase(action)) {
            game.resetScores();
            session.setAttribute("game", game);
        } else if ("reset".equalsIgnoreCase(action)) {
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

    /**
     * Resets the board state for a new round in the user's active session.
     *
     * @param request the current HTTP request
     */
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
     * Resets cumulative match scores to zero in the user's active session.
     *
     * @param request the current HTTP request
     */
    private void handleResetScores(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            GameLogic game = (GameLogic) session.getAttribute("game");
            if (game != null) {
                game.resetScores();
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
