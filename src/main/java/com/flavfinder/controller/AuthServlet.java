package com.flavfinder.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * This authorization class manages end-user forwarding, handles
 * new user creation forms, and validates specific user credentials.
 *
 * @author EmileM
 */

@WebServlet(
        urlPatterns = {"/auth"}
)
public class AuthServlet extends HttpServlet {
//    private final Logger logger = LogManager.getLogger(this.getClass());
    /**
     * Forwards the end-user to either the signup or login page.
     *
     * @param req Client's Request.
     * @param resp Server's Response.
     * @throws ServletException If a ServletException occurs.
     * @throws IOException If a Input/Output exception occurs.
     */
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        String url = "";

        if ("sign-up".equals(req.getParameter("action"))) {
            url = "/signup.jsp";

        } else if ("login".equals(req.getParameter("action"))) {
            url = "/index.jsp";
        }

        RequestDispatcher dispatcher = req.getRequestDispatcher(url);
        dispatcher.forward(req, resp);
    }
}
