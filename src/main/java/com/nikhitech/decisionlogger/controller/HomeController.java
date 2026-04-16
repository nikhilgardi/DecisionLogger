package com.nikhitech.decisionlogger.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.nikhitech.decisionlogger.model.User;

/*
 * ===============================================================
 * HOME CONTROLLER
 * ===============================================================
 *
 * PURPOSE:
 * - Central routing logic.
 *
 * WHY?
 * - Keeps role-based dashboard routing centralized.
 */

@Controller
public class HomeController {

    /*
     * Root endpoint "/"
     * Decides dashboard based on role.
     */
    @GetMapping("/")
    public String home(HttpSession session, Model model) {

        User user =
                (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/auth/login";
        }
        
        model.addAttribute("user", user);

        if (user.getRole().name().equals("ADMIN")) {
            return "redirect:/admin/dashboard";
        }

        return "redirect:/decision/dashboard";
    }
}