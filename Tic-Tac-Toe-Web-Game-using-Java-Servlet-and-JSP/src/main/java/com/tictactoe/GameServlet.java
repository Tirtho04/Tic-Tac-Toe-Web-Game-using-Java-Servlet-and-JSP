package com.tictactoe;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * GameServlet.java
 *
 * <p>Controller servlet for the Tic Tac Toe web game.</p>
 *
 * <p>URL mappings (defined in web.xml):</p>
 * <ul>
 * <li>{@code /play} — handles a player move (POST)</li>
 * <li>{@code /reset} — handles a new game reset (POST) — Week 6</li>
 * </ul>
 *
 * <p>Follows the Post-Redirect-Get (PRG) pattern: every POST ends with
 * response.sendRedirect("index.jsp") so a browser refresh never re-submits
 * the move.</p>
 *
 * <p><b>Week 3:</b> doGet() — redirects to index.jsp.<br>
 * <b>Week 4:</b> doPost() — reads row/col, calls makeMove(), PRG redirect.<br>
 * <b>Week 5:</b> checkWinner() and isDraw() calls added.<br>
 * <b>Week 6:</b> reset action branch added.</p>
 *
 * @author [Your Name]
 * @version 1.1
 */
public class GameServlet extends HttpServlet {

    // ----------------------------------------------------------------
    // doGet — Week 3
    // ----------------------------------------------------------------
    /**
     * Handles HTTP GET requests. Any direct browser navigation to /play is
     * redirected to the JSP view.
     */
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.sendRedirect("index.jsp");
    }

    // ----------------------------------------------------------------
    // doPost — Week 4
    // ----------------------------------------------------------------
    /**
     * Handles HTTP POST requests for player moves.
     *
     * Steps:
     * 1. Get the HttpSession.
     * 2. Read row and col parameters sent by the JSP form.
     * 3. Get GameLogic from session — create a new one if this is the
     *    first visit or the session was reset.
     * 4. Call makeMove(row, col) — GameLogic itself rejects out-of-range
     *    or already-occupied cells, so a bad/replayed request cannot
     *    corrupt the board.
     * 5. Save the updated GameLogic back to the session.
     * 6. Redirect to index.jsp (PRG pattern).
     */
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // Step 1: get the session (true = create if not exists)
        HttpSession session = request.getSession(true);

        // Step 3: get GameLogic from session, create new if absent
        GameLogic game = (GameLogic) session.getAttribute("game");
        if (game == null) {
            game = new GameLogic();
        }

        // Step 2 + 4: read row/col and make the move.
        // FIX (Week 4): the original code called Integer.parseInt()
        // directly on the request parameters with no validation. A
        // missing/non-numeric row or col (e.g. a hand-crafted or replayed
        // request) threw an uncaught NumberFormatException and returned a
        // raw HTTP 500 to the player instead of the game page. We now
        // parse defensively and simply ignore a malformed move — the
        // board is unaffected and the player just sees the current state.
        try {
            int row = Integer.parseInt(request.getParameter("row"));
            int col = Integer.parseInt(request.getParameter("col"));
            game.makeMove(row, col); // GameLogic bounds-checks row/col itself
        } catch (NumberFormatException e) {
            // row/col missing or not a number — no move applied.
        }

        // Step 5: save updated game back to session
        session.setAttribute("game", game);

        // Step 6: PRG — always redirect after POST
        // This prevents duplicate move on browser refresh
        response.sendRedirect("index.jsp");
    }
}
