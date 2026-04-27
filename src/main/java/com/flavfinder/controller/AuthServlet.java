package com.flavfinder.controller;

import com.flavfinder.APIdentity.AuthenticatedUser;
import com.flavfinder.entity.SavedLocation;
import com.flavfinder.entity.User;
import com.flavfinder.persistence.CognitoAuthService;
import com.flavfinder.persistence.GenericDao;
import com.flavfinder.persistence.SavedLocationDao;
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
import java.util.List;

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
    private final Logger log = LogManager.getLogger(this.getClass());

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
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        if (session != null) {
            session.removeAttribute("error");
        }

        String url = "";

        if ("sign-up".equals(req.getParameter("action"))) {
            url = "/signup.jsp";
            req.setAttribute("page", "Sign up - FlavFinder");

        } else if ("login".equals(req.getParameter("action"))) {
            url = "/index.jsp";
            req.setAttribute("page", "Login - FlavFinder");

        } else if ("reset-pass".equals(req.getParameter("action"))) {
            url = "/passwordReset.jsp";
            req.setAttribute("page", "Reset Password - FlavFinder");

        } else if ("reset-pass-confirm".equals(req.getParameter("action"))) {
            url = "/resetPasswordConfirm.jsp";
            req.setAttribute("page", "Reset Password - FlavFinder");

        } else {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
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

        if ("signUp".equals(action)) {
            String firstName = req.getParameter("first_name");
            String email = req.getParameter("email");
            String password = req.getParameter("password");

            if (anyBlank(firstName, password)) {
                req.setAttribute("error", "Please fill out all fields");
                req.getRequestDispatcher("/signup.jsp").forward(req, resp);
                return;
            }

            if (!isValidEmail(email)) {
                req.setAttribute("error", "Please enter a valid email address");
                req.getRequestDispatcher("/signup.jsp").forward(req, resp);
                return;
            }

            try {
                String sub = cognitoAuth.register(firstName, email, password);

                session.setAttribute("pendingConfirmEmail", email);
                session.setAttribute("pendingConfirmSub", sub);
                session.setAttribute("title", "confirm - FlavFinder");
                resp.sendRedirect(req.getContextPath() + "/confirm.jsp");

            } catch (UsernameExistsException e) {
                session.setAttribute("error", "An account with this email already exists");
                resp.sendRedirect(req.getContextPath() + "/signup.jsp");

            } catch (InvalidPasswordException e) {
                session.setAttribute("error", "Password does not meet requirements");
                resp.sendRedirect(req.getContextPath() + "/signup.jsp");

            } catch (InvalidParameterException e) {
                session.setAttribute("error", "Please ensure all fields are filled out correctly");
                resp.sendRedirect(req.getContextPath() + "/signup.jsp");

            } catch (TooManyRequestsException e) {
                session.setAttribute("error", "Too many attempts please try again later");
                resp.sendRedirect(req.getContextPath() + "/signup.jsp");

            } catch (Exception e) {
                session.setAttribute("error", "Something went wrong please try again");
                resp.sendRedirect(req.getContextPath() + "/signup.jsp");

            }

        } else if ("confirm".equals(action)) {
            String email = (String) session.getAttribute("pendingConfirmEmail");
            String code = req.getParameter("v-code");

            if (email == null) {
                resp.sendRedirect(req.getContextPath() + "/signup.jsp");
                return;
            }

            if (anyBlank(code)) {
                req.setAttribute("error", "Please enter the verification code");
                req.getRequestDispatcher("/confirm.jsp").forward(req, resp);
                return;
            }

            try {
                cognitoAuth.confirmSignUp(email, code);

                String sub = (String) session.getAttribute("pendingConfirmSub");
                GenericDao<User> userDao = new GenericDao<>(User.class);
                userDao.insert(new User(sub));

                session.removeAttribute("pendingConfirmEmail");
                session.removeAttribute("pendingConfirmSub");
                resp.sendRedirect(req.getContextPath() + "/index.jsp");

            } catch (CodeMismatchException e) {
                session.setAttribute("error", "Invalid verification code");
                resp.sendRedirect(req.getContextPath() + "/confirm.jsp");

            } catch (ExpiredCodeException e) {
                // TODO find a way to make this possible
                session.setAttribute("error", "Code has expired please request a new one");
                resp.sendRedirect(req.getContextPath() + "/confirm.jsp");

            } catch (Exception e) {
                session.setAttribute("error", "Something went wrong please try again");
                resp.sendRedirect(req.getContextPath() + "/confirm.jsp");

            }

        } else if ("login".equals(action)) {
            String email = req.getParameter("email");
            String password = req.getParameter("password");

            if (!isValidEmail(email) || anyBlank(password)) {
                req.setAttribute("error", "Please fill out all fields");
                req.getRequestDispatcher("/index.jsp").forward(req, resp);
                return;
            }

            try {
                AuthenticationResultType result = cognitoAuth.login(email, password);
                AuthenticatedUser authUser = tokenVerifier.verify(result.idToken());

                GenericDao<User> userDao = new GenericDao<>(User.class);
                List<User> dbUsers = userDao.findBy("sub", authUser.getSub());
                if (dbUsers.isEmpty()) {
                    log.error("Login: no DB user found for sub: {}", authUser.getSub());
                    session.setAttribute("error", "Something went wrong please try again");
                    resp.sendRedirect(req.getContextPath() + "/index.jsp");
                    return;
                }
                User dbUser = dbUsers.get(0);

                // Cognito user for token/claims e.g., email, name, sub, etc.
                session.setAttribute("user", authUser);

                // DB user for internal id
                // When this attribute is called, simply target the associated id field in the DB
                // e.g., int userId = dbUser.getId();
                session.setAttribute("dbUser", dbUser);

                SavedLocationDao locationDao = new SavedLocationDao();
                SavedLocation savedLocation = locationDao.findByUserId(dbUser.getId());

                if (savedLocation != null) {
                    if (savedLocation.isDefault()) {
                        session.setAttribute("userLat", savedLocation.getLatitude());
                        session.setAttribute("userLon", savedLocation.getLongitude());
                        log.info("Login: restored geolocation: {}, {}", savedLocation.getLatitude(), savedLocation.getLongitude());
                    } else {
                        session.setAttribute("savedLocation", savedLocation);
                        log.info("Login: restored custom location: {}", savedLocation.getCityName());
                    }
                } else {
                    log.info("Login: no saved location found for userId={}", dbUser.getId());
                }

                resp.sendRedirect(req.getContextPath() + "/home");

            } catch (NotAuthorizedException e) {
                session.setAttribute("error", "Incorrect email or password");
                resp.sendRedirect(req.getContextPath() + "/index.jsp");

            } catch (UserNotConfirmedException e) {
                session.setAttribute("error", "Please confirm your email before logging in");
                resp.sendRedirect(req.getContextPath() + "/index.jsp");

            } catch (UserNotFoundException e) {
                session.setAttribute("error", "No account found with that email");
                resp.sendRedirect(req.getContextPath() + "/index.jsp");

            } catch (Exception e) {
                session.setAttribute("error", "Something went wrong please try again");
                resp.sendRedirect(req.getContextPath() + "/index.jsp");

            }

        } else if ("forgotPassword".equals(action)) {
            String email = req.getParameter("email");

            if (!isValidEmail(email)) {
                req.setAttribute("error", "Please enter a valid email address");
                req.getRequestDispatcher("/passwordReset.jsp").forward(req, resp);
                return;
            }

            try {
                cognitoAuth.forgotPassword(email);
                session.setAttribute("resetEmail", email);
                resp.sendRedirect(req.getContextPath() + "/auth?action=reset-pass-confirm");

            } catch (UserNotFoundException e) {
                session.setAttribute("error", "No account found with that email");
                resp.sendRedirect(req.getContextPath() + "/passwordReset.jsp");

            } catch (InvalidParameterException e) {
                session.setAttribute("error", "Account not confirmed. Please verify your email first");
                resp.sendRedirect(req.getContextPath() + "/passwordReset.jsp");

            } catch (TooManyRequestsException e) {
                session.setAttribute("error", "Too many attempts, please try again later");
                resp.sendRedirect(req.getContextPath() + "/passwordReset.jsp");

            } catch (Exception e) {
                session.setAttribute("error", "Something went wrong please try again");
                resp.sendRedirect(req.getContextPath() + "/passwordReset.jsp");

            }

        } else if ("confirmForgotPassword".equals(action)) {
            String email = (String) session.getAttribute("resetEmail");
            String code = req.getParameter("v-code");
            String newPassword = req.getParameter("password");

            if (email == null) {
                resp.sendRedirect(req.getContextPath() + "/auth?action=reset-pass");
                return;
            }

            if (anyBlank(code, newPassword)) {
                req.setAttribute("error", "Please fill out all fields");
                req.getRequestDispatcher("/resetPasswordConfirm.jsp").forward(req, resp);
                return;
            }

            try {
                cognitoAuth.confirmForgotPassword(email, code, newPassword);
                session.removeAttribute("resetEmail");
                session.setAttribute("successMsg", "Password reset successfully. Please log in.");
                resp.sendRedirect(req.getContextPath() + "/index.jsp");

            } catch (CodeMismatchException e) {
                session.setAttribute("error", "Invalid verification code");
                resp.sendRedirect(req.getContextPath() + "/resetPasswordConfirm.jsp");

            } catch (ExpiredCodeException e) {
                session.setAttribute("error", "Code has expired, please request a new one");
                resp.sendRedirect(req.getContextPath() + "/passwordReset.jsp");

            } catch (InvalidPasswordException e) {
                session.setAttribute("error", "Password does not meet requirements");
                resp.sendRedirect(req.getContextPath() + "/resetPasswordConfirm.jsp");

            } catch (TooManyRequestsException e) {
                session.setAttribute("error", "Too many attempts, please try again later");
                resp.sendRedirect(req.getContextPath() + "/resetPasswordConfirm.jsp");

            } catch (Exception e) {
                session.setAttribute("error", "Something went wrong please try again");
                resp.sendRedirect(req.getContextPath() + "/resetPasswordConfirm.jsp");

            }
        }
    }

    /**
     * Checks if any of the given values are blank.
     * @param values The values to check.
     * @return true if any value is blank or null, false otherwise.
     */
    private boolean anyBlank(String... values) {
        for (String v : values) {
            if (v == null || v.isBlank()) return true;
        }
        return false;
    }

    /**
     * Checks if the given email address is valid.
     * @param email The email address to validate.
     * @return true if the email address is valid, false otherwise.
     */
    private boolean isValidEmail(String email) {
        return !anyBlank(email) && email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    }
}
