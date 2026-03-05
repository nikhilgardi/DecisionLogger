package com.nikhitech.decisionlogger.controller;

import com.nikhitech.decisionlogger.model.Role;
import com.nikhitech.decisionlogger.security.RoleAllowed;
import com.nikhitech.decisionlogger.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/*
 ===============================================================
 ADMIN CONTROLLER
 ===============================================================

 LAYER
 ---------------------------------------------------------------
 Presentation Layer (Spring MVC Controller)

 RESPONSIBILITY
 ---------------------------------------------------------------
 Handles HTTP requests related to **ADMIN operations**.

 Admin capabilities include:
 • View all users
 • View all decisions
 • Deactivate users

 The controller does NOT contain business logic.
 It delegates logic to the **Service Layer**.

 MVC FLOW
 ---------------------------------------------------------------
 Browser Request
        ↓
 DispatcherServlet
        ↓
 AdminController
        ↓
 UserService / DecisionService
        ↓
 DAO Layer
        ↓
 Database


 SECURITY
 ---------------------------------------------------------------
 @RoleAllowed(Role.ADMIN)

 Custom RBAC annotation checked by **RBACInterceptor**.

 Only users with ADMIN role can access this controller.

 DESIGN PATTERNS
 ---------------------------------------------------------------

 1️⃣ MVC Pattern
 Controller handles HTTP requests.

 2️⃣ Service Layer Pattern
 Controller delegates logic to services.

 3️⃣ Dependency Injection
 Spring injects UserService and DecisionService.

 SOLID PRINCIPLES
 ---------------------------------------------------------------

 SRP (Single Responsibility)
 Controller only handles web requests.

 DIP (Dependency Inversion)
 Depends on abstraction (UserService) not implementation.

 OCP
 New admin features can be added without modifying existing code.
 */

@Controller
@RequestMapping("/admin")
@RoleAllowed(Role.ADMIN)
public class AdminController {

    /*
     * Dependencies injected by Spring container.
     *
     * Controller does NOT create objects.
     * This follows Dependency Injection principle.
     */
    private final UserService userService;
    private final DecisionService decisionService;

    /*
     * Constructor Injection
     *
     * WHY Constructor Injection?
     * -------------------------------------------------
     * ✔ Ensures dependency is mandatory
     * ✔ Object becomes immutable
     * ✔ Easier unit testing
     */
    public AdminController(UserService userService,
                           DecisionService decisionService) {
        this.userService = userService;
        this.decisionService = decisionService;
    }

    /*
     ===============================================================
     ADMIN DASHBOARD
     ===============================================================

     URL
     ---------------------------------------------------------------
     GET /admin/dashboard

     PURPOSE
     ---------------------------------------------------------------
     Displays admin dashboard with:
     • User list
     • Decision list

     FLOW
     ---------------------------------------------------------------
     Controller
        ↓
     Service Layer
        ↓
     DAO Layer
        ↓
     Database

     MODEL OBJECT
     ---------------------------------------------------------------
     Spring Model is used to send data to the Thymeleaf view.
     */

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        /*
         * Fetch all users created by Admin
         *
         * Delegated to service layer.
         */
    	model.addAttribute("users",
    	        userService.getUsersForAdmin());

        /*
         * Fetch all decisions
         */
        model.addAttribute("decisions",
                decisionService.getAllDecisions());

        /*
         * Return view name.
         *
         * ViewResolver converts this to:
         * /WEB-INF/views/admin-dashboard.html
         */
        return "admin-dashboard";
    }

    /*
     ===============================================================
     DEACTIVATE USER
     ===============================================================

     URL
     ---------------------------------------------------------------
     POST /admin/deactivate

     PURPOSE
     ---------------------------------------------------------------
     Allows admin to deactivate a user.

     BUSINESS RULE
     ---------------------------------------------------------------
     Deactivated users cannot login.

     SECURITY
     ---------------------------------------------------------------
     Only ADMIN role can access this endpoint.

     REQUEST PARAMETER
     ---------------------------------------------------------------
     userId → received from HTML form.

     HTML Example

     <form action="/admin/deactivate" method="post">
     */

    @PostMapping("/deactivate")
    public String deactivate(@RequestParam Long userId) {

        /*
         * Delegate business logic to service layer.
         */
        userService.deactivateUser(userId);

        /*
         * Redirect prevents duplicate form submission.
         */
        return "redirect:/admin/dashboard";
    }
}