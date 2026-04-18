package com.nikhitech.decisionlogger.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nikhitech.decisionlogger.exception.AppException;
import com.nikhitech.decisionlogger.model.Role;
import com.nikhitech.decisionlogger.model.User;
import com.nikhitech.decisionlogger.security.RoleAllowed;
import com.nikhitech.decisionlogger.service.DecisionService;
import com.nikhitech.decisionlogger.service.UserService;

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
	
	Logger logger = LoggerFactory.getLogger(AdminController.class);

	/*
	 * Dependencies injected by Spring container.
	 *
	 * Controller does NOT create objects. This follows Dependency Injection
	 * principle.
	 */
	private final UserService userService;
	private final DecisionService decisionService;

	/*
	 * Constructor Injection
	 *
	 * WHY Constructor Injection? -------------------------------------------------
	 * ✔ Ensures dependency is mandatory ✔ Object becomes immutable ✔ Easier unit
	 * testing
	 */
	public AdminController(UserService userService, DecisionService decisionService) {
		this.userService = userService;
		this.decisionService = decisionService;
	}

	/**
	 * Displays admin dashboard with users and decisions.
	 * 
	 * @param model Spring Model used to pass data to the view
	 * @return Admin dashboard view
	 */
	@GetMapping("/dashboard")
	public String dashboard(Model model) {

	  

	    // Add decisions to model
	    model.addAttribute("decisions", decisionService.getAllDecisions());

	    // Return view name
	    return "admin-dashboard";
	}

	/**
	 * Deactivates a user.
	 * 
	 * @param userId ID of the user to deactivate
	 * @return Redirects to admin dashboard
	 */
	@PostMapping("/deactivate")
	public String deactivate(@RequestParam Long userId) {

	    // Delegate to service layer
	    userService.deactivateUser(userId);

	    // Redirect to prevent duplicate submission
	    return "redirect:/admin/dashboard";
	}

	// =========================
	// User Creation - Admin Panel
	// =========================

	/**
	 * Displays the user creation form.
	 * 
	 * @param model Spring Model object used to pass data to the view
	 * @return Thymeleaf template name for creating a user
	 */
	@GetMapping("/get-user")
	public String displayUserList(Model model) {

		// Initialize an empty User object
		// This is required for form binding in Thymeleaf (th:object)
		model.addAttribute("user", new User());

		// Return the view name (create-user.html)
		return "user_list";
	}
	
	/**
	 * Displays the user creation form.
	 * 
	 * @param model Spring Model object used to pass data to the view
	 * @return Thymeleaf template name for creating a user
	 */
	@GetMapping("/create-user")
	public String showCreateUserForm(Model model) {

		// Initialize an empty User object
		// This is required for form binding in Thymeleaf (th:object)
		 if (!model.containsAttribute("user")) {
		        model.addAttribute("user", new User());
		    }

		// Return the view name (create-user.html)
		return "create-user";
	}

	/**
	 * Handles form submission for creating a new user.
	 * 
	 * @param user User object populated with form data
	 * @return Redirects to admin dashboard after successful creation
	 */
	@PostMapping("/create-user")
	public String createUser(@ModelAttribute User user,
	                         Model model,
	                         RedirectAttributes redirectAttributes) {

	    try {
	        userService.createUser(user);

	        redirectAttributes.addFlashAttribute("success", "User created successfully");
	        return "redirect:/admin/get-user";

	    } catch (AppException ex) {

	       // model.addAttribute("user", user);
	       // model.addAttribute("error", ex.getMessage());
	    	
	    	 redirectAttributes.addFlashAttribute("error", ex.getMessage());

	    	 return "redirect:/admin/get-user";
	    }
	}
}