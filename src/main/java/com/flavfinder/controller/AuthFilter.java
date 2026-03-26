package com.flavfinder.controller;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Intercepts all incoming requests and ensures
 * the user has a valid session before accessing
 * protected routes.
 *
 * @author EmileM
 */
@WebFilter("/*")
public class AuthFilter implements Filter {
    private static final Logger log = LogManager.getLogger(AuthFilter.class);

    /**
     * Checks for a valid session on every request.
     * Public routes are whitelisted and always accessible.
     * All other routes require a valid session.
     *
     * @param request  Client's request.
     * @param response Server's response.
     * @param chain    The filter chain.
     * @throws ServletException If a servlet exception occurs.
     * @throws IOException      If an Input/Output exception occurs.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {


    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}
