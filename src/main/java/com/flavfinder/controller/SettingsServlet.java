package com.flavfinder.controller;

import com.flavfinder.APIdentity.AuthenticatedUser;
import com.flavfinder.entity.User;
import com.flavfinder.persistence.CognitoAuthService;
import com.flavfinder.persistence.GenericDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Handles the user settings page and account management actions.
 *
 * <p>Supported actions (POST):
 * <ul>
 *   <li>{@code deleteAccount} — permanently removes the user from both
 *       the Cognito User Pool and the application database, then
 *       invalidates the session and redirects to the login page.</li>
 * </ul>
 *
 * @author EmileM
 */
@WebServlet(urlPatterns = "/settings")
public class SettingsServlet extends HttpServlet {

    private static final Logger log = LogManager.getLogger(SettingsServlet.class);

    private CognitoAuthService cognitoAuth;

    /**
     * Initialises servlet-scoped dependencies from the application context.
     *
     * @throws ServletException If a servlet-level error occurs during initialisation.
     */
    @Override
    public void init() throws ServletException {
        cognitoAuth = (CognitoAuthService) getServletContext().getAttribute("cognitoAuth");
    }

    /**
     * Forwards the authenticated user to the settings page.
     *
     * @param req  Incoming request.
     * @param resp Outgoing response; forwards to settings.jsp.
     * @throws ServletException If a servlet-level error occurs.
     * @throws IOException      If writing the response fails.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        session.setAttribute("page", "Settings - FlavFinder");
        req.getRequestDispatcher("/WEB-INF/jsp/settings.jsp").forward(req, resp);
    }

    /**
     * Handles account management form submissions.
     *
     * <p>For {@code deleteAccount}: deletes the user from Cognito, then from the
     * database (cascade removes all saved locations and restaurants), invalidates
     * the session, and redirects to the login page.
     *
     * @param req  Incoming request; expects an {@code action} parameter.
     * @param resp Outgoing response.
     * @throws ServletException If a servlet-level error occurs.
     * @throws IOException      If writing the response fails.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        if ("deleteAccount".equals(req.getParameter("action"))) {
            AuthenticatedUser authUser = (AuthenticatedUser) session.getAttribute("user");
            User dbUser = (User) session.getAttribute("dbUser");

            try {
                cognitoAuth.deleteUser(authUser.getAccessToken());
                log.info("SettingsServlet — Cognito user deleted for sub={}", authUser.getSub());
            } catch (Exception e) {
                log.error("SettingsServlet — failed to delete Cognito user for sub={}", authUser.getSub(), e);
                session.setAttribute("settingsError", "Something went wrong. Please try again.");
                resp.sendRedirect(req.getContextPath() + "/settings");
                return;
            }

            try {
                GenericDao<User> userDao = new GenericDao<>(User.class);
                userDao.delete(dbUser);
                log.info("SettingsServlet — DB user deleted for id={}", dbUser.getId());
            } catch (Exception e) {
                log.error("SettingsServlet — failed to delete DB user id={}", dbUser.getId(), e);
            }

            session.invalidate();
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
        }
    }
}
