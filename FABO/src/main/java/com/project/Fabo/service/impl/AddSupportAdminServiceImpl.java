 package com.project.Fabo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.Fabo.entity.AddSupportAdmin;
import com.project.Fabo.entity.ClientSupport;
import com.project.Fabo.entity.User;
import com.project.Fabo.repository.AddSupportAdminRepository;
import com.project.Fabo.repository.ClientSupportRepository;
import com.project.Fabo.service.AddSupportAdminService;
import com.project.Fabo.service.ClientUserService;
import com.project.Fabo.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class AddSupportAdminServiceImpl implements AddSupportAdminService{
	
	private AddSupportAdminRepository addSupportAdminRepository;
	
	private ClientSupportRepository clientSupportRepository;
	
	 private final JavaMailSender javaMailSender;
	 
	 private UserService userService;
	 
	 private ClientUserService clientUserService;


	public AddSupportAdminServiceImpl(AddSupportAdminRepository addSupportAdminRepository,
			ClientSupportRepository clientSupportRepository, JavaMailSender javaMailSender, UserService userService,
			ClientUserService clientUserService) {
		super();
		this.addSupportAdminRepository = addSupportAdminRepository;
		this.clientSupportRepository = clientSupportRepository;
		this.javaMailSender = javaMailSender;
		this.userService = userService;
		this.clientUserService = clientUserService;
	}

	@Value("${max.file.size}")
	 private long maxFileSize;

	@Override
	public AddSupportAdmin saveAddSupportAdmin(AddSupportAdmin addSupportAdmin) {
		 // Save the invoice to generate the auto-incremented numeric part
		AddSupportAdmin savedaddSupportAdmin = addSupportAdminRepository.save(addSupportAdmin);

        // Update the formatted invoice number based on the saved invoice ID
		savedaddSupportAdmin.setFormattedSupportNumber(savedaddSupportAdmin.getId());
		return addSupportAdminRepository.save(addSupportAdmin);
		
	}

	@Override
	public AddSupportAdmin getAddSupportAdminById(Long id) {
			return addSupportAdminRepository.findById(id).get();
	}
	
	public void saveCommentAndStatusById(Long id, String commentText, String clientVisible, String requestStatus) {
	    // Fetch the existing record by ID from the database
	    Optional<AddSupportAdmin> supportAdminOptional = addSupportAdminRepository.findById(id);

	    // Check if the record exists
	    if (supportAdminOptional.isPresent()) {
	        AddSupportAdmin supportAdmin = supportAdminOptional.get();

	        // Update fields based on clientVisible and save to the database
	        if (clientVisible.equals("Yes")) {
	            supportAdmin.setInternalComments(commentText);
	        } else {
	            supportAdmin.setCommentsToClient(commentText);
	            supportAdmin.setInternalComments(commentText);
	        }

	        supportAdmin.setStatus(requestStatus);

	        // Save the updated supportAdmin record back to the database
	        addSupportAdminRepository.save(supportAdmin);

	        // Now update the status in the clientSupport entity
	        Optional<ClientSupport> clientSupportOptional = clientSupportRepository.findById(id);
	        if (clientSupportOptional.isPresent()) {
	            ClientSupport clientSupport = clientSupportOptional.get();
	            clientSupport.setStatus(requestStatus);
	            clientSupport.setExternalComments(commentText);
	            clientSupportRepository.save(clientSupport); // Save the updated clientSupport entity
	        } else {
	            // Handle case when the clientSupport ID doesn't exist
	            // You might throw an exception or handle it according to your application logic
	            throw new IllegalArgumentException("Client Support ID not found: " + id);
	        }
	    } else {
	        // Handle case when the supportAdmin ID doesn't exist
	        // You might throw an exception or handle it according to your application logic
	        throw new IllegalArgumentException("Support Admin ID not found: " + id);
	    }
	}
	
	public void deleteAddSupportAdminById(Long id) {
	    Optional<AddSupportAdmin> supportAdminOptional = addSupportAdminRepository.findById(id);

	    if (supportAdminOptional.isPresent()) {
	        AddSupportAdmin supportAdmin = supportAdminOptional.get();
	        supportAdmin.setActiveStatus(false); // Marking as inactive

	        addSupportAdminRepository.save(supportAdmin);
	    } else {
	        throw new IllegalArgumentException("Add Support Admin ID not found: " + id);
	    }
	}
	
	 
	 public List<String> getUsernamesBasedOnStoreCode(String roleName, String storeCode) {
	        List<User> usersWithRole = userService.getUsersByName(roleName);
	        List<String> usernames = usersWithRole.stream().map(User::getUserName).collect(Collectors.toList());
	        return clientUserService.getUserNamesByStoreCode(usernames, storeCode);
	    }
	 
	 public List<String> getEmailsBasedOnUsernames(List<String> usernames) {
	        List<String> emails = new ArrayList<>();
	        for (String username : usernames) {
	            String email = clientUserService.getEmailByUserName(username);
	            if (email != null && !email.isEmpty()) {
	                emails.add(email);
	            }
	        }
	        return emails;
	    }
	 
	 public void sendEmailNotification(AddSupportAdmin addSupportAdmin, List<String> recipientEmails, List<MultipartFile> files, String commentText) {
		    try {
		        MimeMessage message = javaMailSender.createMimeMessage();
		        MimeMessageHelper helper = new MimeMessageHelper(message, true); // Enable multipart mode for attachments
		        
		        helper.setFrom("saddarirohit1616@gmail.com");
		        helper.setTo(recipientEmails.toArray(new String[0]));
		        helper.setSubject("FABO Support Notification");
		        
		        // Construct the email content
		        StringBuilder emailContent = new StringBuilder();
		        emailContent.append("A new support request has been created with the following details:\n\n");
		        emailContent.append("Date: ").append(addSupportAdmin.getDate()).append("\n");
		        emailContent.append("Store Code: ").append(addSupportAdmin.getStoreCode()).append("\n");
		        emailContent.append("Store Name: ").append(addSupportAdmin.getStoreName()).append("\n");
		        emailContent.append("Store Contact: ").append(addSupportAdmin.getStoreContact()).append("\n");
		        emailContent.append("Support Request Type: ").append(addSupportAdmin.getSupportRequestType()).append("\n");
		        emailContent.append("Issue Subject: ").append(addSupportAdmin.getIssueSubject()).append("\n");
		        emailContent.append("Status: ").append(addSupportAdmin.getStatus()).append("\n");
		        emailContent.append("\n");
		        
		        // Append information about the attached files (if any)
		        if (files != null && !files.isEmpty()) {
		            emailContent.append("Files Attached:\n");
		            for (MultipartFile file : files) {
		                emailContent.append("- ").append(file.getOriginalFilename()).append("\n");
		            }
		        }
		        
		        emailContent.append("Access the Application by clicking the URL below:\n");
		        emailContent.append("http://13.200.183.229:8086/showLoginPage");
		        
		        
		        // Add thank you message and application URL
		        emailContent.append("\nThank you,\nFABO Team\n");
		        
		        // Set the email content
		        helper.setText(emailContent.toString());
		        
		        // Add file attachments
		        if (files != null) {
		            for (MultipartFile file : files) {
		                helper.addAttachment(file.getOriginalFilename(), file);
		            }
		        }
		        
		        javaMailSender.send(message);
		        System.out.println("Email notification sent successfully to " + recipientEmails);
		    } catch (MessagingException e) {
		        System.out.println("Error creating or sending email message: " + e.getMessage());
		    } catch (MailException e) {
		        System.out.println("Error sending email notification: " + e.getMessage());
		    } finally {
		        // Close resources or perform any necessary cleanup here
		    }
		}
	 
	 public void sendEmailNotifications(List<String> recipientEmails, String notificationMessage, String commentText) {
		    MimeMessage message = javaMailSender.createMimeMessage();
		    MimeMessageHelper helper = new MimeMessageHelper(message);
		    try {
		        helper.setFrom("saddarirohit1616@gmail.com");
		        helper.setTo(recipientEmails.toArray(new String[0]));
		        helper.setSubject("Notification from FABO");

		        // Construct the email content
		        StringBuilder emailContent = new StringBuilder();
		        emailContent.append("Support Request has been Updated:\n\n");
		        emailContent.append(notificationMessage).append("\n\n");
		        emailContent.append("Comment: ").append(commentText).append("\n\n");
		        emailContent.append("Access the Application by clicking the URL below:\n");
		        emailContent.append("http://13.200.183.229:8086/showLoginPage\n\n");
		        emailContent.append("Thank you,\nFABO Team");

		        helper.setText(emailContent.toString());
		        
		        javaMailSender.send(message);
		        System.out.println("Email notification sent successfully.");
		    } catch (MessagingException | MailException e) {
		        System.out.println("Error sending email notification: " + e.getMessage());
		    }
		}


}
