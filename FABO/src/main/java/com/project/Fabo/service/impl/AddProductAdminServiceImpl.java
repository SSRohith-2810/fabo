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

import com.project.Fabo.entity.AddProductAdmin;
import com.project.Fabo.entity.ClientProduct;
import com.project.Fabo.entity.User;
import com.project.Fabo.repository.AddProductAdminRepository;
import com.project.Fabo.repository.ClientProductRepository;
import com.project.Fabo.service.AddProductAdminService;
import com.project.Fabo.service.ClientUserService;
import com.project.Fabo.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class AddProductAdminServiceImpl implements AddProductAdminService{
	
	private AddProductAdminRepository addProductAdminRepository;
	
	private ClientProductRepository clientProductRepository;
	
 private final JavaMailSender javaMailSender;
	 
	 private UserService userService;
	 
	 private ClientUserService clientUserService;


	 public AddProductAdminServiceImpl(AddProductAdminRepository addProductAdminRepository,
			ClientProductRepository clientProductRepository, JavaMailSender javaMailSender, UserService userService,
			ClientUserService clientUserService) {
		super();
		this.addProductAdminRepository = addProductAdminRepository;
		this.clientProductRepository = clientProductRepository;
		this.javaMailSender = javaMailSender;
		this.userService = userService;
		this.clientUserService = clientUserService;
	}



	@Value("${max.file.size}")
	 private long maxFileSize;

	@Override
	public AddProductAdmin saveAddProductAdmin(AddProductAdmin addProductAdmin) {
		 // Save the invoice to generate the auto-incremented numeric part
		AddProductAdmin savedaddProductAdmin = addProductAdminRepository.save(addProductAdmin);

        // Update the formatted invoice number based on the saved invoice ID
		savedaddProductAdmin.setFormattedProductNumber(savedaddProductAdmin.getId());
		return addProductAdminRepository.save(addProductAdmin);
		
	}

	@Override
	public AddProductAdmin getAddProductAdminById(Long id) {
			return addProductAdminRepository.findById(id).get();
	}
	
	public void saveCommentAndStatusById(Long id, String commentText, String clientVisible, String requestStatus) {
	    // Fetch the existing record by ID from the database
	    Optional<AddProductAdmin> productAdminOptional = addProductAdminRepository.findById(id);

	    // Check if the record exists
	    if (productAdminOptional.isPresent()) {
	        AddProductAdmin productAdmin = productAdminOptional.get();

	        // Update fields based on clientVisible and save to the database
	        if (clientVisible.equals("Yes")) {
	            productAdmin.setInternalComments(commentText);
	        } else {
	            productAdmin.setCommentsToClient(commentText);
	            productAdmin.setInternalComments(commentText);
	        }

	        productAdmin.setStatus(requestStatus);

	        // Save the updated productAdmin record back to the database
	        addProductAdminRepository.save(productAdmin);

	        // Now update the status in the clientProduct entity
	        Optional<ClientProduct> clientProductOptional = clientProductRepository.findById(id);
	        if (clientProductOptional.isPresent()) {
	            ClientProduct clientProduct = clientProductOptional.get();
	            clientProduct.setStatus(requestStatus);
	            clientProduct.setExternalComments(commentText);
	            clientProductRepository.save(clientProduct); // Save the updated clientProduct entity
	        } else {
	            // Handle case when the clientSupport ID doesn't exist
	            // You might throw an exception or handle it according to your application logic
	            throw new IllegalArgumentException("Client Product ID not found: " + id);
	        }
	    } else {
	        // Handle case when the supportAdmin ID doesn't exist
	        // You might throw an exception or handle it according to your application logic
	        throw new IllegalArgumentException("Product Admin ID not found: " + id);
	    }
	}


	 public void deleteAddProductAdminById(Long id) {
	        Optional<AddProductAdmin> productAdminOptional = addProductAdminRepository.findById(id);

	        if (productAdminOptional.isPresent()) {
	            AddProductAdmin productAdmin = productAdminOptional.get();
	            productAdmin.setActiveStatus(false); // Marking as inactive

	            addProductAdminRepository.save(productAdmin);
	        } else {
	            throw new IllegalArgumentException("Product Admin ID not found: " + id);
	        }
	    }

	 public List<String> getUsernamesBasedOnStoreCode(String roleName, String storeCode) {
	        List<User> usersWithRole = userService.getUsersByName(roleName);
	        List<String> usernames = usersWithRole.stream().map(User::getUserName).collect(Collectors.toList());
	        System.out.println("Usernames: " + usernames); 
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

	 public void sendEmailNotifications(List<String> recipientEmails, String notificationMessage, String commentText) {
		    try {
		        MimeMessage message = javaMailSender.createMimeMessage();
		        MimeMessageHelper helper = new MimeMessageHelper(message);
		        
		        helper.setFrom("saddarirohit1616@gmail.com");
		        helper.setTo(recipientEmails.toArray(new String[0]));
		        helper.setSubject("FABO Product Request Notification");
		        
		        // Construct the email content
		        StringBuilder emailContent = new StringBuilder();
		        emailContent.append("Product Request has been updated:\n");
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

	 
	 public void sendEmailNotification(AddProductAdmin addProductAdmin, List<String> recipientEmails, List<MultipartFile> files) {
		    try {
		        MimeMessage message = javaMailSender.createMimeMessage();
		        MimeMessageHelper helper = new MimeMessageHelper(message, true); // Enable multipart mode for attachments
		        
		        helper.setFrom("saddarirohit1616@gmail.com");
		        helper.setTo(recipientEmails.toArray(new String[0]));
		        helper.setSubject("FABO Product Notification");
		        
		        // Construct the email content
		        StringBuilder emailContent = new StringBuilder();
		        emailContent.append("A new Product Request has been created with the following details:\n\n");
		        emailContent.append("Date: ").append(addProductAdmin.getDate()).append("\n");
		        emailContent.append("Store Code: ").append(addProductAdmin.getStoreCode()).append("\n");
		        emailContent.append("Store Name: ").append(addProductAdmin.getStoreName()).append("\n");
		        emailContent.append("Store Contact: ").append(addProductAdmin.getStoreContact()).append("\n");
		        emailContent.append("Product Request Type: ").append(addProductAdmin.getProductRequestType()).append("\n");
		        emailContent.append("Issue Subject: ").append(addProductAdmin.getIssueSubject()).append("\n");
		        emailContent.append("Status: ").append(addProductAdmin.getStatus()).append("\n");
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



}

