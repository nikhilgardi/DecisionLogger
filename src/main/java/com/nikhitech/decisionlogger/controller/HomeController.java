package com.nikhitech.decisionlogger.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

 @GetMapping("/")
 public String home(Model model) {
     model.addAttribute("message", "Spring MVC + Thymeleaf Working 🚀");
     return "home";
 }
}