package com.nikhitech.decisionlogger.controller;

import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nikhitech.decisionlogger.model.LoginForm;
import com.nikhitech.decisionlogger.model.User;
import com.nikhitech.decisionlogger.service.AuthService;

/*
 * ===============================================================
 * AUTH CONTROLLER
 * ===============================================================
 *
 * LAYER:
 * - Presentation Layer (Web Layer)
 *
 * RESPONSIBILITY:
 * ---------------------------------------------------------------
 * - Render login page
 * - Handle login submission
 * - Create session
 * - Handle logout
 *
 *
 * DESIGN PATTERNS:
 * ---------------------------------------------------------------
 * 1. MVC Controller Pattern
 *    - Receives HTTP request
 *    - Returns view name
 *
 * 2. Front Controller Pattern
 *    - Managed by DispatcherServlet
 *
 *
 * SOLID PRINCIPLES:
 * ---------------------------------------------------------------
 * SRP:
 * - Only handles HTTP-related logic.
 *
 * DIP:
 * - Depends on AuthService abstraction.
 *
 * DOES NOT:
 * - Perform SQL
 * - Implement business rules
 */

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /*
     * ===============================================================
     * DISPLAY LOGIN PAGE
     * ===============================================================
     *
     * WHY Model?
     * - To bind LoginForm object to Thymeleaf.
     */
    @GetMapping("/login")
    public String loginPage(Model model) {

        model.addAttribute("loginForm", new LoginForm());

        return "login"; // resolved to /WEB-INF/views/login.html
    }

    /*
     * ===============================================================
     * PROCESS LOGIN
     * ===============================================================
     *
     * WHY @ModelAttribute?
     * - Automatically binds form fields to LoginForm object.
     */
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm form,
    					BindingResult result,
                        HttpSession session,
                        HttpServletResponse response,
                        Model model) {
    	
    	
    	/*
         * Validation errors
         */

        if(result.hasErrors()) {
            return "login";
        }

    	Optional<User> userOpt =
    	        authService.login(form.getEmail(), form.getPassword());

    	if (!userOpt.isPresent()) {

            model.addAttribute("error", "Invalid credentials");
            return "login";
        }

        User user = userOpt.get();

        /*
         * Session-based authentication.
         * Session ID stored in browser cookie.
         */
        session.setAttribute("user", user);

        /*
         * Remember-Me handled via cookie.
         */
        if (form.isRememberMe()) {

            Cookie cookie =
                    new Cookie("remember-me",
                            user.getId().toString());

            cookie.setMaxAge(7 * 24 * 60 * 60);
            cookie.setHttpOnly(true);

            response.addCookie(cookie);
        }

        return "redirect:/";
    }

    /*
     * ===============================================================
     * LOGOUT
     * ===============================================================
     */
    @GetMapping("/logout")
    public String logout(HttpSession session,
                         HttpServletResponse response) {

        session.invalidate();

        Cookie cookie = new Cookie("remember-me", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "redirect:/auth/login";
    }
}