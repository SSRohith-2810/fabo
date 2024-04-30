package com.project.Fabo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.Fabo.entity.Invoice;
import com.project.Fabo.entity.User;
import com.project.Fabo.repository.InvoiceRepository;
import com.project.Fabo.service.ClientUserService;
import com.project.Fabo.service.InvoiceService;
import com.project.Fabo.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class InvoiceServiceImpl implements InvoiceService{
	
	@Autowired
	private InvoiceRepository invoiceRepository;
	
 private final JavaMailSender javaMailSender;
	 
	 private UserService userService;
	 
	 private ClientUserService clientUserService;

	
	
	 public InvoiceServiceImpl(InvoiceRepository invoiceRepository, JavaMailSender javaMailSender,
			UserService userService, ClientUserService clientUserService) {
		super();
		this.invoiceRepository = invoiceRepository;
		this.javaMailSender = javaMailSender;
		this.userService = userService;
		this.clientUserService = clientUserService;
	}

	@Override
	    public List<Invoice> getAllInvoices() {
	        return invoiceRepository.findAll();
	    }

	 public Invoice saveInvoice(Invoice invoice) {
	        return invoiceRepository.save(invoice);
	    }

		@Override
		public Invoice getInvoiceById(Long id) {
			return invoiceRepository.findById(id).get();
		}

		@Override
		public Invoice updateInvoice(Invoice invoice) {
			return invoiceRepository.save(invoice);
		}
		
		public void deleteInvoiceById(Long id) {
	        Optional<Invoice> invoiceOptional = invoiceRepository.findById(id);

	        if (invoiceOptional.isPresent()) {
	            Invoice invoice = invoiceOptional.get();
	            invoice.setActiveStatus(false); // Marking as inactive

	            invoiceRepository.save(invoice);
	        } else {
	            throw new IllegalArgumentException("Invoice ID not found: " + id);
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

		 public void sendEmailNotification(Invoice invoice, List<String> recipientEmails, List<MultipartFile> files) {
			    try {
			        MimeMessage message = javaMailSender.createMimeMessage();
			        MimeMessageHelper helper = new MimeMessageHelper(message, true); // Enable multipart mode for attachments

			        helper.setFrom("saddarirohit1616@gmail.com");
			        helper.setTo(recipientEmails.toArray(new String[0]));
			        helper.setSubject("FABO Invoice Notification");

			        // Load your HTML template
			        String template = "<html><head><style>" +
			                "body { font-family: Arial, sans-serif; margin: 0; padding: 0; }" +
			                ".email-container { max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ccc; border-radius: 10px; }" +
			                ".header { background-color: #f0f0f0; padding: 20px; text-align: center; }" +
			                ".header h1 { margin: 0; }" +
			                ".content { padding: 20px 0; }" +
			                ".invoice-details { border: 1px solid #f0f0f0; border-radius: 5px; padding: 20px; }" +
			                ".invoice-details ul { list-style-type: none; padding: 0; }" +
			                ".invoice-details li { margin-bottom: 10px; }" +
			                ".invoice-details li strong { font-weight: bold; }" +
			                ".invoice-actions { text-align: center; margin-top: 20px; }" +
			                ".footer { background-color: #f0f0f0; padding: 20px; text-align: center; }" +
			                ".company-logo { max-width: 150px; }" +
			                "</style></head><body>" +
			                "<div class='email-container'>" +
			                "<div class='header'>" +
			                "<img src='cid:fabo-logo' class='company-logo'><br>" +
			                "<h1>FABO Invoice Notification</h1>" +
			                "</div>" +
			                "<div class='content'>" +
			                "<div class='invoice-details'>" +
			                "<p>Dear User,</p>" +
			                "<p>A new invoice has been uploaded with the following details:</p>" +
			                "<ul>" +
			                "<li><strong>Invoice Date:</strong> " + invoice.getInvoiceDate() + "</li>" +
			                "<li><strong>Invoice Number:</strong> " + invoice.getInvoiceNumber() + "</li>" +
			                "<li><strong>Store Code:</strong> " + invoice.getStoreCode() + "</li>" +
			                "<li><strong>Store Name:</strong> " + invoice.getStoreName() + "</li>" +
			                "<li><strong>Invoice Type:</strong> " + invoice.getInvoiceType() + "</li>" +
			                "<li><strong>Invoice Amount:</strong> " + invoice.getInvoiceAmount() + "</li>" +
			                "<li><strong>Invoice Status:</strong> " + invoice.getInvoiceStatus() + "</li>" +
			                "</ul>" +
			                "<p>Access the Application by clicking the URL below:</p>" +
			                "<p><a href=\"http://13.200.183.229:8086/showLoginPage\">Click here to access the application</a></p>" +
			                "<div class='invoice-actions'>" +
			                "<p>Thank You,<br/>FABO Team</p>" +
			                "</div>" +
			                "</div>" +
			                "</div>" +
			                "<div class='footer'>" +
			                "<p>&copy; 2024 FABO. All rights reserved.</p>" +
			                "</div>" +
			                "</div></body></html>";

			        // Set the email content as HTML
			        helper.setText(template, true);

			        // Add the company logo as an inline attachment
			        FileSystemResource logoImage = new FileSystemResource("src/main/resources/static/images/fabo.logo.png");
			        helper.addInline("fabo-logo", logoImage);

			        // Add file attachments
			        if (files != null) {
			            for (MultipartFile file : files) {
			                helper.addAttachment(file.getOriginalFilename(), file);
			            }
			        }

			        javaMailSender.send(message);
			        System.out.println("Email notification sent successfully to " + recipientEmails);
			    } catch (MessagingException | MailException e) {
			        System.out.println("Error sending email notification: " + e.getMessage());
			    }
			}


		 
		 public void sendEmailNotification(List<String> recipientEmails, String notificationMessage, String commentText) {
			    try {
			        MimeMessage message = javaMailSender.createMimeMessage();
			        MimeMessageHelper helper = new MimeMessageHelper(message, true); // Enable multipart mode for attachments

			        helper.setFrom("saddarirohit1616@gmail.com");
			        helper.setTo(recipientEmails.toArray(new String[0]));
			        helper.setSubject("FABO Invoice Notification");

			        // Load your HTML template
			        String template = "<html><head><style>" +
			                "body { font-family: Arial, sans-serif; }" +
			                ".email-container { max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #f0f0f0; border-radius: 10px; background-color: #fff; }" +
			                ".header { text-align: center; }" +
			                ".header img { max-width: 150px; }" +
			                ".content { margin-top: 20px; }" +
			                ".notification { padding: 20px; border: 1px solid #f0f0f0; border-radius: 5px; background-color: #f9f9f9; }" +
			                ".notification p { margin: 0; }" +
			                ".comment { margin-top: 20px; }" +
			                ".comment p { margin: 0; }" +
			                ".footer { text-align: center; margin-top: 20px; }" +
			                "</style></head><body>" +
			                "<div class='email-container'>" +
			                "<div class='header'>" +
			                "<img src='cid:fabo-logo'><br>" +
			                "<h2>FABO Invoice Request Notification</h2>" +
			                "</div>" +
			                "<div class='content'>" +
			                "<div class='notification'>" +
			                "<p>Invoice Request has been updated:</p>" +
			                "<p>" + notificationMessage + "</p>" +
			                "</div>" +
			                "<div class='comment'>" +
			                "<p><strong>Comment:</strong> " + commentText + "</p>" +
			                "</div>" +
			                "</div>" +
			                "<div class='footer'>" +
			                "<p>&copy; 2024 FABO. All rights reserved.</p>" +
			                "</div>" +
			                "</div></body></html>";

			        // Set the email content as HTML
			        helper.setText(template, true);

			        // Add the company logo as an inline attachment
			        FileSystemResource logoImage = new FileSystemResource("src/main/resources/static/images/fabo.logo.png");
			        helper.addInline("fabo-logo", logoImage);

			        javaMailSender.send(message);
			        System.out.println("Email notification sent successfully.");
			    } catch (MessagingException | MailException e) {
			        System.out.println("Error sending email notification: " + e.getMessage());
			    }
			}

	   

}
