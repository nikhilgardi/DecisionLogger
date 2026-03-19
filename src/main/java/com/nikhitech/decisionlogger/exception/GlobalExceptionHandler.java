package com.nikhitech.decisionlogger.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    /*
     ===============================================================
     CUSTOM APP EXCEPTION
     ===============================================================
    */
    @ExceptionHandler(AppException.class)
    public String handleAppException(AppException ex,
                                     Model model,
                                     HttpServletResponse response) {

        response.setStatus(ex.getStatus().getCode());

        model.addAttribute("errorCode", ex.getStatus().getCode());
        model.addAttribute("message", ex.getMessage());

        return "error";
    }

    /*
     ===============================================================
     404 - NO CONTROLLER FOUND
     ===============================================================
    */
    @ExceptionHandler(org.springframework.web.servlet.NoHandlerFoundException.class)
    public String handle404(Exception ex,
                            Model model,
                            HttpServletResponse response) {

        response.setStatus(404);

        model.addAttribute("errorCode", 404);
        model.addAttribute("message", "Page not found");

        return "error";
    }

    /*
     ===============================================================
     VIEW / THYMELEAF ERROR
     ===============================================================
    */
    @ExceptionHandler(org.thymeleaf.exceptions.TemplateInputException.class)
    public String handleTemplate(Exception ex,
                                 Model model,
                                 HttpServletResponse response) {

        response.setStatus(500);

        model.addAttribute("errorCode", 500);
        model.addAttribute("message", "Page not available");

        return "error";
    }

    /*
     ===============================================================
     FALLBACK
     ===============================================================
    */
    @ExceptionHandler(Exception.class)
    public String handleGeneric(Exception ex,
                                Model model,
                                HttpServletResponse response) {

        response.setStatus(500);

        model.addAttribute("errorCode", 500);
        model.addAttribute("message", "Something went wrong");

        ex.printStackTrace();

        return "error";
    }
}