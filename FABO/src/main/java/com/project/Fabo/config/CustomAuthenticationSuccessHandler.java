package com.project.Fabo.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	 @Override
	    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
	        for (GrantedAuthority authority : authentication.getAuthorities()) {
	            String role = authority.getAuthority();
	            if (role.equals("ROLE_SUPERADMIN")) {
	                // Redirect superadmin to superadmin home page
	                getRedirectStrategy().sendRedirect(request, response, "/superadminHome");
	                return;
	            } else if (role.startsWith("ROLE_ADMIN")) {
	                // Redirect admin to admin home page
	                getRedirectStrategy().sendRedirect(request, response, "/adminshome");
	                return;
	            } else if (role.startsWith("ROLE_CLIENT")) {
	                // Redirect client to client home page
	                getRedirectStrategy().sendRedirect(request, response, "/clientshome");
	                return;
	            }
	        }
	        
	        // Default redirection if no matching role is found
	        super.onAuthenticationSuccess(request, response, authentication);
	    }
}

