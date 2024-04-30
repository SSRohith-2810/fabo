package com.project.Fabo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.Fabo.entity.Client;
import com.project.Fabo.repository.ClientRepository;
import com.project.Fabo.service.ClientService;

import jakarta.mail.internet.MimeMessage;

@Controller
public class ClientController {
	
	private final ClientRepository clientRepository;
    private final ClientService clientService;


	public ClientController(ClientRepository clientRepository, ClientService clientService) {
		this.clientRepository = clientRepository;
		this.clientService = clientService;
	}
	
	 @Autowired
	 private JavaMailSender javaMailSender;

	@GetMapping("/faboAdd")
	public String faboPage(Model model) {
		Client client = new Client();
		model.addAttribute("client",client);
		return "AddClient";
	}
	
	@RequestMapping(value = {"/faboclients", "/faboclients"}, method = {RequestMethod.GET, RequestMethod.POST})
	public String searchAndFilterClients(@RequestParam(value = "search", required = false) String search,
	                                      @RequestParam(value = "state", required = false) String state,
	                                      @RequestParam(value = "city", required = false) String city,
	                                      Model model) {

	    model.addAttribute("selectedState", state);
	    model.addAttribute("selectedCity", city);

	    // Filter
	    List<String> states = clientRepository.findDistinctStates();
	    List<String> cities = clientRepository.findDistinctCities();
	    model.addAttribute("states", states);
	    model.addAttribute("cities", cities);

	    List<Client> clients;

	    if ((state == null || state.equalsIgnoreCase("All")) && (city == null || city.equalsIgnoreCase("All"))) {
	        // No specific state or city selected, return all active clients
	        clients = clientRepository.findByActiveStatusTrue();
	    } else if (search != null && !search.isEmpty()) {
	        // Search
	        clients = clientRepository.findBySearchTerm(search, state);
	    } else if (state != null && !state.isEmpty() && !state.equalsIgnoreCase("All") && city != null && !city.isEmpty() && !city.equalsIgnoreCase("All")) {
	        // Filter by both state and city
	        clients = clientRepository.findByStateAndCity(state, city);
	    } else if (state != null && !state.isEmpty() && !state.equalsIgnoreCase("All")) {
	        // Filter by state
	        clients = clientRepository.findByState(state);
	    } else if (city != null && !city.isEmpty() && !city.equalsIgnoreCase("All")) {
	        // Filter by city
	        clients = clientRepository.findByCity(city);
	    } else {
	        // Default case, return all active clients
	        clients = clientRepository.findByActiveStatusTrue();
	    }

	    model.addAttribute("clients", clients);
	    return "clientslist";
	}
	
	//Duplicate Email
	@PostMapping("/clients")
	public String save(@ModelAttribute("client") Client client, Model model) {
	    boolean isUpdate = false;
	    try {
	        clientService.saveClient(client);
	        sendEmailNotification(client.getEmail(), client, isUpdate);
	        return "redirect:/faboclients";
	    } catch (DataIntegrityViolationException e) {
	        String errorMessage = "Store code '" + client.getStoreCode() + "' already exists.";
	        model.addAttribute("error", errorMessage);
	        // Add other necessary attributes to the model if needed
	        return "addclient"; // Or whatever your view name is
	    }
	}

	
	private void sendEmailNotification(String toEmail, Client client, boolean isUpdate) {
	    try {
	        MimeMessage message = javaMailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message, true);

	        String subject;
	        String emailContent;

	        if (isUpdate) {
	            subject = "Your info has been UPDATED";
	            emailContent = "Your information has been updated successfully. Below are the details you provided:\n\n";
	        } else {
	            subject = "Welcome to FABO Team";
	            emailContent = "A new partner record has been added to the FABO Application with the following details:\n\n";
	        }

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
	                "<h2>WELCOME "+ client.getOwnerName() +"</h2>" +
	                "</div>" +
	                "<div class='content'>" +
	                "<div class='notification'>" +
	                "<p><b>" + emailContent + "</b></p><br>" +
	                "<p>Store Name: " + client.getStoreName() + "</p>" +
	                "<p>Store Code: " + client.getStoreCode() + "</p>" +
	                "<p>Owner Name: " + client.getOwnerName() + "</p>" +
	                "<p>Email: " + client.getEmail() + "</p>" +
	                "<p>Owner Contact: " + client.getOwnerContact() + "</p>" +
	                "<p>Store Contact: " + client.getStoreContact() + "</p>" +
	                "<p>GST Number: " + client.getGstNo() + "</p>" +
	                "<p>State: " + client.getState() + "</p>" +
	                "<p>City: " + client.getCity() + "</p>" +
	                "<p>Full Address: " + client.getFullAddress() + "</p>" +
	                "<p>GMB Profile Link: " + client.getGmbProfileLink() + "</p>" +
	                "<p>&nbsp;</p>" + // Add space for better readability
	                "</div>" +
	                "<div class='footer'>" +
	                "<p>Thank you,</p>" +
	                "<p>Team FABO</p>" +
	                "<p>&copy; 2024 FABO. All rights reserved.</p>" +
	                "</div>" +
	                "</div></div></body></html>";


	        // Set the email content as HTML
	        helper.setText(template, true);

	        // Add the company logo as an inline attachment
	        FileSystemResource logoImage = new FileSystemResource("src/main/resources/static/images/fabo.logo.png");
	        helper.addInline("fabo-logo", logoImage);

	        System.out.println("isUpdate value: " + isUpdate);
	        System.out.println("Subject: " + subject);

	        helper.setTo(toEmail);
	        helper.setSubject(subject);
	        // Remove setting text directly, as it's already set in the template

	        javaMailSender.send(message); // Send the email
	    } catch (Exception e) {
	        e.printStackTrace(); // Handle email sending exception
	    }
	}

	
	@GetMapping("clients/edit/{Id}")
	public String editClient(@PathVariable Long Id, Model model) {
		model.addAttribute("client", clientService.getClientById(Id));
		return "clientsedit";
	}
	
	@RequestMapping("clientview/{Id}")
	public String viewClient(@PathVariable Long Id, Model model) {
		model.addAttribute("client", clientService.getClientById(Id));
		return "clientsview";
	}

	@PostMapping("clients/{Id}")
	public String updateClient(@PathVariable Long Id,
	                            @ModelAttribute("client") Client updatedClient, 
	                            Model model) {
	    // Retrieve the existing client from the database
	    Client existingClient = clientService.getClientById(Id);

	    if (existingClient != null) {
	        // Update the properties of the existing client
	        existingClient.setStoreCode(updatedClient.getStoreCode());
	        existingClient.setStoreName(updatedClient.getStoreName());
	        existingClient.setOwnerName(updatedClient.getOwnerName());
	        existingClient.setEmail(updatedClient.getEmail());
	        existingClient.setOwnerContact(updatedClient.getOwnerContact());
	        existingClient.setStoreContact(updatedClient.getStoreContact());
	        existingClient.setGstNo(updatedClient.getGstNo());
	        existingClient.setState(updatedClient.getState());
	        existingClient.setCity(updatedClient.getCity());
	        existingClient.setFullAddress(updatedClient.getFullAddress());

	        // Save the updated client
	        clientService.updateClient(existingClient);

	        // Perform other actions as needed
	        sendEmailNotification(existingClient.getEmail(), existingClient, true);
	    }

	    return "redirect:/faboclients";
	}

	
	@GetMapping("/client/{Id}")
	public String deleteClient(@PathVariable Long Id) {
		clientService.deleteClientById(Id);
		return "redirect:/faboclients";
	}
	
	@PostMapping("/handleButtonClick")
    public String handleButtonClick(Model model) {
        return "redirect:/faboclients"; 
    }
	
	 
	 /*@PostMapping("/faboclients")
	 public String searchClients(@RequestParam(value = "search", required = false) String search, Model model) {
	     List<Client> clientss;

	     if (search != null && !search.isEmpty()) {

	         clientss = clientRepository.findBySearchTerm(search);
     } else {

	         clientss = clientRepository.findAll();
	 }

     model.addAttribute("clients", clientss);

     return "faboclients";
	 }*/
	 

}
