package com.nikhitech.decisionlogger.controller;

import com.nikhitech.decisionlogger.model.*;
import com.nikhitech.decisionlogger.security.RoleAllowed;
import com.nikhitech.decisionlogger.service.DecisionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/*
 * ===============================================================
 * DECISION CONTROLLER
 * ===============================================================
 *
 * RESPONSIBILITY:
 * - Handles user decision dashboard.
 *
 * SECURITY:
 * - Only USER role allowed.
 */

@Controller
@RequestMapping("/decision")
@RoleAllowed({Role.USER, Role.ADMIN})
public class DecisionController {

    private final DecisionService decisionService;

    public DecisionController(DecisionService decisionService) {
        this.decisionService = decisionService;
    }

    /*
     * ===============================================================
     * USER DASHBOARD
     * ===============================================================
     */
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session,
                            Model model) {

        User user =
            (User) session.getAttribute("user");

        model.addAttribute("decisions",
                decisionService.getDecisionsForUser(user));

        return "admin-dashboard";
    }
    
    
   /*
    * Displays the user decision list
    * @param model Spring Model Object use to pass data to the view 
    * @return Thymleaf template name for fetching list of decision take by the user
    */
    @GetMapping("/get-user-decision")
    public String getUserDecision(HttpSession session,
                            Model model) {

        User user =
            (User) session.getAttribute("user");

        model.addAttribute("decisions",
                decisionService.getDecisionsForUser(user));

        return "decision_list";
    }
    
    
    /*
     * Displays the decision form
     * @param model Spring Model Object use to pass object to the view
     * @return Thymleaf template name for submiting the decision of the user
     */
    @GetMapping("/user-decision-form")
    public String showDecisionForm(Model model)
    {
    	if(!model.containsAttribute("decision"))
    	{
    		model.addAttribute("decision",new Decision());
    	}
    	
    	return "user-decision-form";
    }
}