package com.nikhitech.decisionlogger.controller;

import com.nikhitech.decisionlogger.model.Role;
import com.nikhitech.decisionlogger.security.RoleAllowed;
import com.nikhitech.decisionlogger.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/*
 * ===============================================================
 * ADMIN CONTROLLER
 * ===============================================================
 *
 * LAYER:
 * - Presentation Layer
 *
 * SECURITY:
 * - Restricted using @RoleAllowed
 */

@Controller
@RequestMapping("/admin")
@RoleAllowed(Role.ADMIN)
public class AdminController {

    private final UserService userService;
    private final DecisionService decisionService;

    public AdminController(UserService userService,
                           DecisionService decisionService) {
        this.userService = userService;
        this.decisionService = decisionService;
    }

    /*
     * ===============================================================
     * ADMIN DASHBOARD
     * ===============================================================
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        model.addAttribute("users",
                userService.getAllUsers());

        model.addAttribute("decisions",
                decisionService.getAllDecisions());

        return "admin-dashboard";
    }

    /*
     * ===============================================================
     * DEACTIVATE USER
     * ===============================================================
     */
    @PostMapping("/deactivate")
    public String deactivate(@RequestParam Long userId) {

        userService.deactivateUser(userId);

        return "redirect:/admin/dashboard";
    }
}