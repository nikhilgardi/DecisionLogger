package com.nikhitech.decisionlogger.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GlobalErrorController {

    @RequestMapping("/error")
    public String handleError(javax.servlet.http.HttpServletRequest request,
                              Model model) {

        Object statusObj =
                request.getAttribute("javax.servlet.error.status_code");

        int statusCode = statusObj != null ? (int) statusObj : 500;

        model.addAttribute("errorCode", statusCode);

        if (statusCode == 404) {
            model.addAttribute("message", "Page not found");
        } else {
            model.addAttribute("message", "Something went wrong");
        }

        return "error";
    }
}
