package com.project.Fabo.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.Fabo.entity.Admin;
import com.project.Fabo.entity.Client;
import com.project.Fabo.entity.ClientUser;
import com.project.Fabo.entity.User;
import com.project.Fabo.repository.UserRepository;
import com.project.Fabo.service.ClientService;
import com.project.Fabo.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Controller
public class HomeController {
	
	// Assuming you're using SLF4J Logger
	private static final Logger log = LoggerFactory.getLogger(HomeController.class);

	
@Autowired
private ClientService clientService;

@Autowired
private UserService userService;

@Autowired
private UserRepository userRepository;

@Autowired
private JavaMailSender javaMailSender;

@Autowired
private JdbcTemplate jdbcTemplate;




public HomeController(JavaMailSender javaMailSender) {
	super();
	this.javaMailSender = javaMailSender;
}

@Autowired
private BCryptPasswordEncoder passwordEncoder;
	
	 // Default constructor
    public HomeController() {
        // Default constructor body (if needed)
    }
	
    @GetMapping("/superadminHome")    
    public String show(Model model) {
		Client client = new Client();
		model.addAttribute("client",client);
		ClientUser clientUser = new ClientUser();
		model.addAttribute("clientUser",clientUser);
		Admin admin = new Admin();
		model.addAttribute("admin",admin);
		 List<Client> clients = clientService.getAllClients();
	        model.addAttribute("clients", clients);
		return "superhome"; 
    }
    
    @GetMapping("/addclient")
    public String addClient(Model model) {
    	List<Client> clients = clientService.getAllClients();
        model.addAttribute("clients", clients);
    	Client client = new Client();
    	model.addAttribute("client", client);
    	return "addclient";
    }
    
    @GetMapping("/addadmin")
    public String addAdmin(Model model) {
    	Admin admin = new Admin();
    	model.addAttribute("admin", admin);
    	return "addadmin";
    }
    
    @GetMapping("/adduser")
    public String adduser(Model model) {
    	List<Client> clients = clientService.getAllClients();
        model.addAttribute("clients", clients);
    	ClientUser clientUser = new ClientUser();
    	model.addAttribute("clientUser", clientUser);
    	return "adduser";
    }
    
    @GetMapping("/emailLink")
    public String email() {
    	return "emailLink";
    }
   
    @PostMapping("/emailLinks")
    public String emailLink(@RequestParam("userName") String userName, Model model) {
        // Check for userName in client_user table
        List<Map<String, Object>> clientUserResult = jdbcTemplate.queryForList("SELECT * FROM client_user WHERE user_name = ?", userName);

        // Check for userName in admin table
        List<Map<String, Object>> adminResult = jdbcTemplate.queryForList("SELECT * FROM admin WHERE user_name = ?", userName);

        // If userName not found in either table, display error message
        if (clientUserResult.isEmpty() && adminResult.isEmpty()) {
            model.addAttribute("error", "Username not found"); // Add error message to model
            return "emailLink"; // Return to emailLink page to display the error message
        } else {
            String token = null;
            String email = null;

            // If userName is found in client_user table, get token and email
            if (!clientUserResult.isEmpty()) {
                token = (String) clientUserResult.get(0).get("token");
                email = (String) clientUserResult.get(0).get("email");
            }
            // If userName is found in admin table, get token and email
            if (!adminResult.isEmpty()) {
                token = (String) adminResult.get(0).get("token");
                email = (String) adminResult.get(0).get("email");
            }

            if (token == null || email == null) {
                model.addAttribute("error", "Token or email not found"); // Add error message to model
                return "emailLink"; // Return to emailLink page to display the error message
            }

            // Proceed with sending the email with token
            sendEmailNotification(email, token);
            return "login1"; // Assuming there's a corresponding HTML file
        }
    }
    
    @Async
    public void sendEmailNotification(String email, String token) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // Set isHtml to true

            // Construct the password set URL with the user's ID
            String setPasswordUrl = "http://13.200.183.229:8086/password/" + token;

            String subject = "Set Your password here.";

            String emailContent = "Use the below link to set your new password.";

            String setPasswordLink = "<a href=\"" + setPasswordUrl + "\">Set your password</a>";
            emailContent += "To set your password, click " + setPasswordLink;

            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(emailContent, true); // Set the email content as HTML

            javaMailSender.send(message);
            System.out.println("Email sent successfully to " + email);
        } catch (MessagingException e) {
            System.out.println("Error sending email notification to " + email + " for user " + email + ": " + e.getMessage());
            // You may want to handle the error gracefully, such as sending a notification to an admin or retrying later
        }
    }
 
    
	    @GetMapping("/adminshome")
	    public String adminHome() {
	        // Logic for admin functionalities
	        return "adminshome"; // Assuming there's a corresponding HTML file
	    }

	    @GetMapping("/clientshome")
	    public String clientHome() {
	        // Logic for client functionalities
	        return "clientshome"; // Assuming there's a corresponding HTML file
	    }
	    
	    @GetMapping("/password/{token}")
	    public String generatePassword(Model model, @PathVariable String token) {
	    	User user = userService.getUserByToken(token);
	    	String userName = user.getUserName();
	    	model.addAttribute("user",user);
	    	model.addAttribute("userName",userName);
	    	return "set";
	    }
	    
	    
	    
	    @PostMapping("/savePassword/{userName}")
	    public String savePassword(Model model,
	    		@RequestParam("userName") String userName, 
	    		@RequestParam("newPassword") String newPassword, 
                @RequestParam("confirmPassword") String confirmPassword) {
	        User existingUser = userService.getUserByUserName(userName);
	        model.addAttribute("existingUser", existingUser);
	        
	        // Hash the password using BCrypt
	        String hashedPassword = passwordEncoder.encode(newPassword);
	        
	        existingUser.setPassword(hashedPassword);
	        
	        // Save the updated user with hashed password
	        userService.saveUser(existingUser);
	        
	        model.addAttribute("successMessage", "Password has been set successfully.");
	        
	        return "redirect:/showLoginPage?success=true";
	    }
	    
	    public User getCurrentUser() {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
	            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	            // Assuming your User entity has a method to retrieve user details by username
	            return userRepository.findByUserName(userDetails.getUsername());
	        } else {
	            throw new IllegalStateException("No authenticated user found");
	        }
	    }


	    @GetMapping("/password")
	    public String showPasswordForm(Model model) {
	        return "reset";
	    }

	    @PostMapping("/changePassword")
	    public String changePassword(Model model,
	                                 @RequestParam("oldPassword") String oldPassword,
	                                 @RequestParam("newPassword") String newPassword,
	                                 @RequestParam("confirmPassword") String confirmPassword) {
	        // Get the currently logged-in user
	        User currentUser = getCurrentUser();

	        // Retrieve the existing password associated with the user
	        String existingPassword = currentUser.getPassword();

	        // Check if the entered old password matches the existing password
	        if (!passwordEncoder.matches(oldPassword, existingPassword)) {
	            // If the passwords don't match, display an error message
	            model.addAttribute("error", "Old password is incorrect.");
	            return "reset"; // Return the view with the error message
	        }

	        // Proceed with changing the password if old password is correct
	        if (!newPassword.equals(confirmPassword)) {
	            // If the new password and confirm password don't match, display an error message
	            model.addAttribute("error", "New password and confirm password do not match.");
	            return "reset"; // Return the view with the error message
	        }

	        // Update the user's password with the new password
	        currentUser.setPassword(passwordEncoder.encode(newPassword));
	        userService.saveUser(currentUser);

	        // Redirect the user to a success page or home page
	        return "redirect:/superadminHome"; // Redirect to home page after successful password change
	    }
	    
	
}
