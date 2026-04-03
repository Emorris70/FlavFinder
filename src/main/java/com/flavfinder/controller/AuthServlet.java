package com.flavfinder.controller;

import com.flavfinder.APIdentity.AuthenticatedUser;
import com.flavfinder.persistence.CognitoAuthService;
import com.flavfinder.persistence.TokenVerifier;
import jakarta.servlet.RequestDispatcher;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.*;

import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.io.IOException;

/**
 * This authorization class manages end-user forwarding/redirection, handles
 * new user creation forms, and validates specific user credentials.
 *
 * SDK -> handles everything auth related
 * Nimbus -> handles JWT signature verification after login
 * ME -> handle session storage / redirects
 *
 * @author EmileM
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
        HttpSession session = req.getSession(false);

        if (session != null) {
            session.removeAttribute("error");
        }

        String url = "";

        if ("sign-up".equals(req.getParameter("action"))) {
            url = "/signup.jsp";
            req.setAttribute("page" ,"Sign up - FlavFinder");

        } else if ("login".equals(req.getParameter("action"))) {
            url = "/index.jsp";
            req.setAttribute("page", "Login - FlavFinder");

        } else if ("reset-pass".equals(req.getParameter("action"))) {
            url = "/passwordReset.jsp";
            req.setAttribute("page", "Reset Password - FlavFinder");

        }

        RequestDispatcher dispatcher = req.getRequestDispatcher(url);
        dispatcher.forward(req, resp);
    }

    /**
     * Handles all form submissions for authentication.
     * Manages user registration, confirmation, and login.
     *
     * @param req Client's Request.
     * @param resp Server's Response
     * @throws ServletException If a ServletException occurs.
     * @throws IOException If a Input/Output exception occurs.
     */
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        HttpSession session = req.getSession();
        session.removeAttribute("error");

        CognitoAuthService cognitoAuth = (CognitoAuthService) getServletContext().getAttribute("cognitoAuth");
        TokenVerifier tokenVerifier = (TokenVerifier) getServletContext().getAttribute("tokenVerifier");

        String action = req.getParameter("action");

        // get the submit button values
        if ("signUp".equals(action)) {
            // register the user
            String firstName = req.getParameter("first_name");
            String email = req.getParameter("email");
            String password = req.getParameter("password");

            try {
                cognitoAuth.register(firstName, email, password);

                session.setAttribute("pendingConfirmEmail", email);
                session.setAttribute("title", "confirm - FlavFinder");
                resp.sendRedirect("confirm.jsp");

            } catch (UsernameExistsException e) {
                session.setAttribute("error", "An account with this email already exists");
                resp.sendRedirect("signup.jsp");

            } catch (InvalidPasswordException e) {
                session.setAttribute("error", "Password does not meet requirements");
                resp.sendRedirect("signup.jsp");

            } catch (InvalidParameterException e) {
                session.setAttribute("error", "Please ensure all fields are filled out correctly");
                resp.sendRedirect("signup.jsp");

            } catch (TooManyRequestsException e) {
                session.setAttribute("error", "Too many attempts please try again later");
                resp.sendRedirect("signup.jsp");

            } catch (Exception e) {
                session.setAttribute("error", "Something went wrong please try again");
                resp.sendRedirect("signup.jsp");

            }

        } else if ("confirm".equals(action)) {
            String email = (String) session.getAttribute("pendingConfirmEmail");
            String code = req.getParameter("v-code");
            try {
                cognitoAuth.confirmSignUp(email, code);

                // clean up
                session.removeAttribute("pendingConfirmEmail");
                resp.sendRedirect("index.jsp");

            } catch (CodeMismatchException e) {
                session.setAttribute("error", "Invalid verification code");
                resp.sendRedirect("confirm.jsp");

            } catch (ExpiredCodeException e) {
                // TODO find a way to make this possible
                session.setAttribute("error", "Code has expired please request a new one");
                resp.sendRedirect("confirm.jsp");

            } catch (Exception e) {
                session.setAttribute("error", "Something went wrong please try again");
                resp.sendRedirect("confirm.jsp");

            }
        } else if ("login".equals(action)) {
            String email = req.getParameter("email");
            String password = req.getParameter("password");

            try {
                // Authenticate against Cognito
                AuthenticationResultType result = cognitoAuth.login(email, password);

                // Verify token and extract claims
                AuthenticatedUser user = tokenVerifier.verify(result.idToken());
                // user id
                session.setAttribute("user", user);

                // redirect to the HomeServlet route(/home)
                resp.sendRedirect(req.getContextPath() + "/home");

            } catch (NotAuthorizedException e) {
                session.setAttribute("error", "Incorrect email or password");
                resp.sendRedirect("index.jsp");

            } catch (UserNotConfirmedException e) {
                session.setAttribute("error", "Please confirm your email before logging in");
                resp.sendRedirect("index.jsp");

            } catch (UserNotFoundException e) {
                session.setAttribute("error", "No account found with that email");
                resp.sendRedirect("index.jsp");

            } catch (Exception e) {
                session.setAttribute("error", "Something went wrong please try again");
                resp.sendRedirect("index.jsp");

            }
        }

    }
}
