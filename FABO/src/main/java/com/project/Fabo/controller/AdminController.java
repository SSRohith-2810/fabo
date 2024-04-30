package com.project.Fabo.controller;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.Fabo.entity.Admin;
import com.project.Fabo.repository.AdminRepository;
import com.project.Fabo.service.AdminService;
import com.project.Fabo.service.RolesTableService;
import com.project.Fabo.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage; 

@Controller
public class AdminController {

	private final AdminRepository adminRepository;
    private final AdminService adminService;
    private RolesTableService rolesTableService;
    private UserService userService;
    


    public AdminController(AdminRepository adminRepository, AdminService adminService,
			RolesTableService rolesTableService, UserService userService, JavaMailSender javaMailSender) {
		super();
		this.adminRepository = adminRepository;
		this.adminService = adminService;
		this.rolesTableService = rolesTableService;
		this.userService = userService;
		this.javaMailSender = javaMailSender;
	}


	@Autowired
	private JavaMailSender javaMailSender;
	
	private static final Logger log = LoggerFactory.getLogger(ClientUserController.class);

	@PostMapping("/addAdmin")
	public String addAdmin(@RequestParam String email, Model model, @ModelAttribute("admin") Admin admin,
	        @RequestParam(required = false) List<Long> roleIds) {
	    String userName = admin.getUserName();
	    
	    String token = UUID.randomUUID().toString();
	    admin.setToken(token);
	   
	    if (userService.isUsernameDuplicate(userName)) {
	        // Username is a duplicate, show error
	        model.addAttribute("error", "Username already exists");
	        return "addadmin"; // Return to the form with an error message
	    } else {
	        /*if (roleIds != null && !roleIds.isEmpty()) {
	            adminService.addAdminAndRoles(admin, roleIds);
	        }*/
	        
	        
	        // Set the concatenatedRoleNames in the admin object
	        //if (roleIds != null && !roleIds.isEmpty()) {
	    	 adminService.addAdminAndRoles(admin, roleIds != null ? roleIds : Collections.emptyList());
	            String concatenatedRoleNames = rolesTableService.getConcatenatedRoleNamesByUserName(userName, roleIds != null ? roleIds : Collections.emptyList());
	            admin.setConcatenatedRoleNames(concatenatedRoleNames);
		        adminService.saveAdmin(admin);
	            
	       // }

	        model.addAttribute("email", email);
	        
	        // Send email notification
	        sendEmailNotification(email, admin, false);
	        
	        return "redirect:/faboAdmins";
	    }
	}
	
    @GetMapping("/faboAdmins")
	public String listAdmins(@RequestParam(value = "search", required = false) String search,
	                          @RequestParam(value = "accesses", required = false) String accesses,
	                          Model model) {
	    List<Admin> admins;

	    if ("All".equals(accesses)) {
	        // If "All" is selected, fetch all admins
	        admins = adminRepository.findAll();
	    } else if (accesses != null && !accesses.isEmpty()) {
	        admins = adminRepository.findByConcatenatedRoleNamesContaining(accesses);
	    } else if (search != null && !search.isEmpty()) {
	        admins = adminRepository.findBySearchTerm(search);
	    } else {
	        admins = adminRepository.findByActiveStatusTrue(); // Default to fetching all admins
	    }

	    model.addAttribute("admins", admins);
	    return "adminslist";
	}


	
	
	@GetMapping("admins/edit/{userName}")
	public String editAdmin(@PathVariable String userName, Model model) {
	    Admin admin = adminService.getAdminById(userName);
	    System.out.println("RoleIds in Model: " + admin.getRoleIds()); // Add this line
	    model.addAttribute("admin", adminService.getAdminById(userName));
	    return "adminedit";
	}
	
	
	
	@GetMapping("adminview/{userName}")
	public String viewAdminUser(@PathVariable String userName, Model model) {
		model.addAttribute("admin", adminService.getAdminById(userName));
		return "adminview";
	}

	@PostMapping("admins/{userName}")
	public String updateAdmin(@PathVariable String userName, String email,
	        @RequestParam(required = false) List<Long> roleIds,
	        @ModelAttribute("admin") Admin admin, Model model) {
	    Admin existingAdmin = adminService.getAdminById(userName);
	    
	    roleIds = (roleIds == null || roleIds.isEmpty()) ? Collections.emptyList() : roleIds;

	    // Check if roleIds is not null before adding roles
	        adminService.addAdminAndRoles(admin, roleIds);

	    // Update admin details
	    existingAdmin.setAdminName(admin.getAdminName());
	    existingAdmin.setEmail(admin.getEmail());
	    existingAdmin.setUserName(admin.getUserName());
	    existingAdmin.setPhoneNumber(admin.getPhoneNumber());

	    // Update concatenated roles only if roleIds are provided
	    if (roleIds != null && !roleIds.isEmpty()) {
	        String concatenatedRoleNames = rolesTableService.getConcatenatedRoleNamesByUserName(userName, roleIds);
	        adminService.updateConcatenatedRolesByUserName(userName, concatenatedRoleNames);
	    } else {
	        // If roleIds are not provided, clear existing roles
	        existingAdmin.setConcatenatedRoleNames("");
	    }

	    model.addAttribute("userName", userName);

	    adminService.updateAdmin(existingAdmin);

	    // Send email notification for update
	    sendUpdateEmailNotification(existingAdmin.getEmail(), existingAdmin, true);

	    return "redirect:/faboAdmins";
	}



	
	@GetMapping("admin/{userName}")
	public String deleteAdmin(@PathVariable String userName) {
		adminService.removeAdminAndAssociationsByUserName(userName);
		adminService.deleteAdminByUserName(userName);
		return "redirect:/faboAdmins";
	}
	
	@PostMapping("/Clicks")
    public String handleButtonClick(Model model) {
        return "redirect:/faboAdmins"; 
    }
	
	
	@Async
    private void sendEmailNotification(String toEmail, Admin admin, boolean isUpdate) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false); // Set to false for plain text
            
            // Construct the password set URL with the user's ID
	        String setPasswordUrl = "http://13.200.183.229:8086/password/" + admin.getToken();
	        String applicationLink = "http://13.200.183.229:8086/showLoginPage";


            String subject = "Welocome to FABO Team";

            // Construct email content with user details

            String emailContent = "Dear " + admin.getDisplayName() + ",\n\n";
            emailContent += "A new account has been created for you to access the FABO Application with the following details:\n\n";
            emailContent += "User Name: " + admin.getUserName() + "\n";
            emailContent += "Display Name: " + admin.getDisplayName() + "\n";
            emailContent += "Phone Number: " + admin.getPhoneNumber() + "\n";
            emailContent += "Email: " + admin.getEmail() + "\n";
            emailContent += "Added Roles: " + admin.getConcatenatedRoleNames() + "\n";
            // Add other fields similarly
            
            String setPasswordLink = setPasswordUrl;
            emailContent += "\nPlease set your password using the link below to access the Application:\n";
            emailContent += setPasswordLink+ "\n\n";
            
            emailContent += "Access the Application by clicking the URL below:\n";
            emailContent += applicationLink + "\n\n";

            emailContent += "Thank you for joining our team!\n\n";
            emailContent += "Best regards,\nFABO Team";

	       

            log.info("Sending email to {} for user {} with subject: {}", toEmail, admin.getUserName(), subject);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(emailContent);

            javaMailSender.send(message); // Send the email
            log.info("Email sent successfully to {} for user {}", toEmail, admin.getUserName());
        } catch (MessagingException | MailException e) {
            // Handle email sending exception

            // Log the exception with additional details
            log.error("Error sending email notification to {} for user {}: {}", toEmail, admin.getUserName(), e.getMessage(), e);
        }

}
	
	@Async
    private void sendUpdateEmailNotification(String toEmail, Admin admin, boolean isUpdate) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false); // Set to false for plain text


            String subject = "Your information has been UPDATED";
            String greetingMessage = "Dear " + admin.getDisplayName() + ",\n\n";
            String applicationLink = "http://13.200.183.229:8086/showLoginPage";


            // Construct email content with user details

            String emailContent = greetingMessage;
            emailContent += "Your information has been updated successfully. Below are the details you provided:\n\n";
            emailContent += "User Name: " + admin.getUserName() + "\n";
            emailContent += "Display Name: " + admin.getDisplayName() + "\n";
            emailContent += "Phone Number: " + admin.getPhoneNumber() + "\n";
            emailContent += "Email: " + admin.getEmail() + "\n";
            emailContent += "Added Roles: " + admin.getConcatenatedRoleNames() + "\n";
            // Add other fields similarly
            
            emailContent += "Access the Application by clicking the URL below:\n";
            emailContent += applicationLink + "\n\n";



            emailContent += "Thank you for joining our team!\n\n";
            emailContent += "Best regards,\nFABO Team";

	       

            log.info("Sending email to {} for user {} with subject: {}", toEmail, admin.getUserName(), subject);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(emailContent);

            javaMailSender.send(message); // Send the email
            log.info("Email sent successfully to {} for user {}", toEmail, admin.getUserName());
        } catch (MessagingException | MailException e) {
            // Handle email sending exception

            // Log the exception with additional details
            log.error("Error sending email notification to {} for user {}: {}", toEmail, admin.getUserName(), e.getMessage(), e);
        }

}
}
