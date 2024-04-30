package com.project.Fabo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @GetMapping("/access-denied")
    public String showAccessDenied() {

        return "access-denied";
    }  
    
    @GetMapping("/showLoginPage")
    public String show() {

        return "login1";
    }  
    
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // Get existing session, if any
        if (session != null) {
            session.invalidate(); // Invalidate the session
        }
        return "redirect:/login?logout"; // Redirect to login page with logout parameter
    }
}
