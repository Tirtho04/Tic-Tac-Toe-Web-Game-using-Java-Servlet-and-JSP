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

        response.sendRedirect("index.jsp");
    }

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
