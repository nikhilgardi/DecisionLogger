package com.nikhitech.decisionlogger.security;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.nikhitech.decisionlogger.model.Role;
import com.nikhitech.decisionlogger.model.User;
import com.nikhitech.decisionlogger.service.AuthService;

/*
 * ===============================================================
 * RBAC INTERCEPTOR
 * ===============================================================
 *
 * LAYER:
 * - Security Layer (Cross-Cutting Concern)
 *
 *
 * RESPONSIBILITY:
 * ---------------------------------------------------------------
 * 1. Check if user is authenticated.
 * 2. Restore session from remember-me cookie.
 * 3. Enforce role-based access control.
 *
 *
 * DESIGN PATTERNS:
 * ---------------------------------------------------------------
 * 1. Interceptor Pattern
 *    - Executes before controller method.
 *
 * 2. Annotation-Based Security Pattern
 *    - Reads @RoleAllowed at runtime.
 *
 *
 * WHY INTERCEPTOR?
 * ---------------------------------------------------------------
 * Without interceptor:
 * - Every controller method must check session manually.
 * - Code duplication.
 * - Violates DRY principle.
 *
 *
 * SOLID PRINCIPLES:
 * ---------------------------------------------------------------
 * SRP:
 * - Only handles security logic.
 *
 * OCP:
 * - Adding new role does not modify interceptor logic.
 *
 * DIP:
 * - Depends on AuthService abstraction.
 */
@Component
public class RBACInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public RBACInterceptor(AuthService authService) {
        this.authService = authService;
    }

    /*
     * ===============================================================
     * preHandle() → Executes BEFORE controller method.
     * ===============================================================
     */

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        /*
         * Ignore non-controller requests
         */
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HttpSession session = request.getSession(false);

        /*
         * ===============================================================
         * STEP 1: Remember-Me Restoration
         * ===============================================================
         *
         * If:
         * - Session expired
         * - But remember-me cookie exists
         *
         * Then:
         * - Restore session automatically
         */

        if (session == null || session.getAttribute("user") == null) {

            if (request.getCookies() != null) {

                for (Cookie cookie : request.getCookies()) {

                    if ("remember-me".equals(cookie.getName())) {

                        Long userId =
                                Long.parseLong(cookie.getValue());

                        authService.findById(userId)
                                .ifPresent(user ->
                                        request.getSession(true)
                                               .setAttribute("user", user));
                    }
                }
            }
        }

        /*
         * ===============================================================
         * STEP 2: Authorization Check
         * ===============================================================
         */

        HandlerMethod method = (HandlerMethod) handler;

        /*
         * Read @RoleAllowed annotation
         */
        RoleAllowed annotation =
                method.getMethodAnnotation(RoleAllowed.class);

        /*
         * If no annotation → public access allowed
         */
        if (annotation == null) {
            return true;
        }

        /*
         * Check authenticated user
         */
        session = request.getSession(false);

        if (session == null ||
            session.getAttribute("user") == null) {

            response.sendRedirect("/auth/login");
            return false;
        }

        User user = (User) session.getAttribute("user");

        /*
         * Check role match
         */
        for (Role allowed : annotation.value()) {

            if (user.getRole() == allowed) {
                return true;
            }
        }

        /*
         * Access denied
         */
        response.sendRedirect("/");
        return false;
    }
}