package com.flavfinder.controller;

import com.flavfinder.persistence.CognitoAuthService;
import com.flavfinder.persistence.TokenVerifier;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.*;

import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UsernameExistsException;

import java.io.IOException;
// TODO rework the overall jdoc
/**
 * This authorization class manages end-user forwarding, handles
 * new user creation forms, and validates specific user credentials.
 *
 * @author EmileM
 */

/**
 * SDK       -> handles everything auth related
 * Nimbus    -> handles JWT signature verification after login
 * ME        -> handle session storage + redirects
 */

@WebServlet(
        urlPatterns = {"/auth"}
)
public class AuthServlet extends HttpServlet {
    private final Logger logger = LogManager.getLogger(this.getClass());
    /**
     * Forwards the end-user to the desired page.
     * This action is triggered through an anchor tag
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
        String title;

        if ("sign-up".equals(req.getParameter("action"))) {
            url = "/signup.jsp";
            title = "Sign up - FlavFinder";
            req.setAttribute("page" ,title);

        } else if ("login".equals(req.getParameter("action"))) {
            url = "/index.jsp";
            title = "Login - FlavFinder";
            req.setAttribute("page", title);
        } else if ("reset-pass".equals(req.getParameter("action"))) {
            url = "/passwordReset.jsp";
            title = "Reset Password - FlavFinder";
            req.setAttribute("page", title);

        }

        RequestDispatcher dispatcher = req.getRequestDispatcher(url);
        dispatcher.forward(req, resp);
    }

    /**
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        HttpSession session = req.getSession();
        String errorMsg = "";

        CognitoAuthService cognitoAuth = (CognitoAuthService) getServletContext().getAttribute("cognitoAuth");
        TokenVerifier tokenVerifier = (TokenVerifier) getServletContext().getAttribute("tokenVerifier");

        String action = req.getParameter("action");

        // get the submit button values
        if ("signUp-btn".equals(req.getParameter(action))) {
            // register the user
            String firstName = req.getParameter("first_name");
            String email = req.getParameter("email");
            String password = req.getParameter("password");

            try {
                cognitoAuth.register(firstName, email, password);
            } catch (UsernameExistsException e) {

            }

            // call session within this scope
            // and also session within the else if scope for login

            // redirect or confirm page also set the title
            // ensure the continue button redirects back to the index.jsp page(login)
            // this will have to be in an else if index.jsp - ensure to call verify method to verify user
            // store the returned values in HttpSession(sub(user_id), email, firstName)
        }

    }
}
