package com.nikhitech.decisionlogger.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DirectTestController {

    @GetMapping("/direct")
    @ResponseBody
    public String direct() {
        return "DIRECT WORKING";
    }
}