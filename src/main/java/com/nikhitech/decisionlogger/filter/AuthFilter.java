package com.nikhitech.decisionlogger.filter;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

/*
 * ==============================
 * AUTHENTICATION FILTER
 * ==============================
 *
 * Cross-cutting concern.
 *
 * OCP:
 * Security logic separated from controllers.
 *
 * This ensures:
 * - Unauthenticated users cannot access dashboard
 */

public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req,
                         ServletResponse res,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        HttpSession session = request.getSession(false);

        String uri = request.getRequestURI();

        // Allow login page
        if (uri.contains("/login")) {
            chain.doFilter(req, res);
            return;
        }

        // Block unauthenticated access
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("/login");
            return;
        }

        chain.doFilter(req, res);
    }
}