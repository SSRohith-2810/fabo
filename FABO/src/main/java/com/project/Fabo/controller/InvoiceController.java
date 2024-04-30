package com.project.Fabo.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.project.Fabo.entity.Admin;
import com.project.Fabo.entity.Client;
import com.project.Fabo.entity.ClientUser;
import com.project.Fabo.entity.Invoice;
import com.project.Fabo.entity.InvoiceComments;
import com.project.Fabo.entity.InvoiceFile;
import com.project.Fabo.repository.ClientRepository;
import com.project.Fabo.repository.InvoiceFileRepository;
import com.project.Fabo.repository.InvoiceRepository;
import com.project.Fabo.service.AdminService;
import com.project.Fabo.service.ClientService;
import com.project.Fabo.service.ClientUserService;
import com.project.Fabo.service.InvoiceCommentsService;
import com.project.Fabo.service.InvoiceService;
import com.project.Fabo.service.UserService;

@Controller
public class InvoiceController {
	
	 private static final Logger log = LoggerFactory.getLogger(InvoiceController.class);

	private InvoiceService invoiceService;
	private InvoiceRepository invoiceRepository;
	private ClientRepository clientRepository;
	private ClientService clientService;
	private InvoiceCommentsService invoiceCommentsService;
	private UserService userService;
	private AdminService adminService;

		

		public InvoiceController(InvoiceService invoiceService, InvoiceRepository invoiceRepository,
			ClientRepository clientRepository, ClientService clientService,
			InvoiceCommentsService invoiceCommentsService, UserService userService,
			AdminService adminService, Map<String, Long> storeCodeCounters, List<String> base64ImageList) {
		super();
		this.invoiceService = invoiceService;
		this.invoiceRepository = invoiceRepository;
		this.clientRepository = clientRepository;
		this.clientService = clientService;
		this.invoiceCommentsService = invoiceCommentsService;
		this.userService = userService;
		this.adminService = adminService;
		this.storeCodeCounters = storeCodeCounters;
		this.base64ImageList = base64ImageList;
	}

		//calling addinvoice form
	    @GetMapping("/addinvoice")
	    public String showaddInvoiceForm(Model model) {
	    	List<String> activeStoreCodes = invoiceRepository.findActiveStoreCodes();
	    	List<Client> clients;
			clients = clientRepository.findAll();
	        model.addAttribute("invoice", new Invoice());
	        model.addAttribute("storeCodes", activeStoreCodes);
	        model.addAttribute("clients", clients);
	        return "addinvoiceadmin"; 
	    }
	   
	    /*@PostMapping("/createInvoice")
	    public String saveInvoice(@ModelAttribute("invoice") Invoice invoice,
	                              @RequestParam("storeCode") String storeCode,
	                              @RequestParam("invoiceComments") String invoiceComments,
	                              @RequestParam("invoiceType") String invoiceType,
	                              @RequestParam(value = "files", required = false) List<MultipartFile> files, Principal principal) {
	         log.info("Number of files received: " + (files != null ? files.size() : 0));
	         String username = principal.getName();
	        invoice.setInvoiceStatus("Pending");
	        invoice.setUploadedBy(username);

	        // Handle file uploads
	        List<InvoiceFile> invoiceFiles = new ArrayList<>();

	        if (files != null && !files.isEmpty()) {
	            for (MultipartFile file : files) {
	                try {
	                    InvoiceFile invoiceFile = new InvoiceFile();
	                    invoiceFile.setFileData(file.getBytes());
	                    invoiceFile.setInvoice(invoice);
	                    invoiceFiles.add(invoiceFile);
	                } catch (IOException e) {
	                    // Handle the IOException as needed
	                    e.printStackTrace(); // Log the exception or throw a custom exception
	                }
	            }
	        }

	        // Associate files with the invoice
	        invoice.setInvoiceFiles(invoiceFiles);

	        // Save the invoice with associated files
	        Invoice savedInvoice = invoiceService.saveInvoice(invoice);

	        // Update the formatted invoice number based on the saved invoice ID and store code
	        savedInvoice.setFormattedInvoiceNumber(storeCode, savedInvoice.getId());

	        // Save the invoice again to update the invoice number
	        invoiceService.saveInvoice(savedInvoice);

	        List<String> usernamesBasedOnStorecode = invoiceService.getUsernamesBasedOnStoreCode("ROLE_CLIENT_ACCOUNTS", storeCode);
	        System.out.println("Usernames based on storecode: " + usernamesBasedOnStorecode);

	        // Get emails based on usernames
	        List<String> emailsBasedOnUsernames = invoiceService.getEmailsBasedOnUsernames(usernamesBasedOnStorecode);
	        System.out.println("Emails based on usernames: " + emailsBasedOnUsernames);

	        // Call this in your controller method
	        invoiceService.sendEmailNotification(invoice, emailsBasedOnUsernames, files );  
	        // Handle requestStatus as needed

	        return "redirect:/viewinvoice";
	    }*/
	    
	 // Define a map to store the last used counter for each store code
	    Map<String, Long> storeCodeCounters = new HashMap<>();

	    @PostMapping("/createInvoice")
	    public String saveInvoice(@ModelAttribute("invoice") Invoice invoice,
	                              @RequestParam("storeCode") String storeCode,
	                              @RequestParam("invoiceDate") String invoiceDate,
	                              @RequestParam("invoiceComments") String invoiceComments,
	                              @RequestParam("invoiceType") String invoiceType,
	                              @RequestParam(value = "files", required = false) List<MultipartFile> files, Principal principal) {
	         log.info("Number of files received: " + (files != null ? files.size() : 0));
	        String username = principal.getName();
	        invoice.setInvoiceStatus("Pending");
	        
	        String displayName = getDisplayNameByUsername(username);
	        invoice.setUploadedBy(displayName);
	        System.out.println(displayName);

	        // Handle file uploads
	        List<InvoiceFile> invoiceFiles = new ArrayList<>();

	        if (files != null && !files.isEmpty()) {
	            for (MultipartFile file : files) {
	                try {
	                    InvoiceFile invoiceFile = new InvoiceFile();
	                    invoiceFile.setFileData(file.getBytes());
	                    invoiceFile.setInvoice(invoice);
	                    invoiceFiles.add(invoiceFile);
	                } catch (IOException e) {
	                    // Handle the IOException as needed
	                    e.printStackTrace(); // Log the exception or throw a custom exception
	                }
	            }
	        }

	        // Associate files with the invoice
	        invoice.setInvoiceFiles(invoiceFiles);

	        // Save the invoice with associated files
	        Invoice savedInvoice = invoiceService.saveInvoice(invoice);

	        // Get the last used counter for the specific store code
	        Long counter = storeCodeCounters.getOrDefault(storeCode, 0L);
	        
	        // Increment the counter for the store code
	        counter++;
	        
	        // Update the store code counter map
	        storeCodeCounters.put(storeCode, counter);

	        // Update the formatted invoice number based on the store code and counter
	        savedInvoice.setFormattedInvoiceNumber(storeCode, savedInvoice.getId());

	        // Save the invoice again to update the invoice number
	        invoiceService.saveInvoice(savedInvoice);

	        List<String> usernamesBasedOnStorecode = invoiceService.getUsernamesBasedOnStoreCode("ROLE_CLIENT_ACCOUNTS", storeCode);
	        System.out.println("Usernames based on storecode: " + usernamesBasedOnStorecode);

	        // Get emails based on usernames
	        List<String> emailsBasedOnUsernames = invoiceService.getEmailsBasedOnUsernames(usernamesBasedOnStorecode);
	        System.out.println("Emails based on usernames: " + emailsBasedOnUsernames);

	        // Call this in your controller method
	        invoiceService.sendEmailNotification(invoice, emailsBasedOnUsernames, files );  
	        // Handle requestStatus as needed

	        return "redirect:/viewinvoice";
	    }
	    
	    @PostMapping("/uploadImages/{id}")
	    public String saveImages(@PathVariable Long id,
	                              @RequestParam(value = "files", required = false) List<MultipartFile> files, Model model) {
	    	 Invoice existingInvoice = invoiceService.getInvoiceById(id);
	    	 model.addAttribute("invoice",existingInvoice);

	        // Handle file uploads
	        List<InvoiceFile> invoiceFiles = new ArrayList<>();

	        if (files != null && !files.isEmpty()) {
	            for (MultipartFile file : files) {
	                try {
	                    InvoiceFile invoiceFile = new InvoiceFile();
	                    invoiceFile.setFileData(file.getBytes());
	                    invoiceFile.setInvoice(existingInvoice);
	                    invoiceFiles.add(invoiceFile);
	                } catch (IOException e) {
	                    // Handle the IOException as needed
	                    e.printStackTrace(); // Log the exception or throw a custom exception
	                }
	            }
	        }

	        // Associate files with the invoice
	        existingInvoice.setInvoiceFiles(invoiceFiles);

	        // Save the invoice with associated files
	        Invoice savedInvoice = invoiceService.saveInvoice(existingInvoice);

	        return "redirect:/invoiceDetail/{id}";
	    }
	    
	    
	    private String getDisplayNameByUsername(String username) {
	        Optional<Admin> admin = adminService.findByUserName(username);

	        if (admin.isPresent()) {
	            return admin.get().getDisplayName();
	       } else {
	            // Handle the case when no matching user is found
	            return "Unknown User";
	        }
	    }




	    @RequestMapping(value = {"/viewinvoice", "/viewinvoice"}, method = {RequestMethod.GET, RequestMethod.POST})
	    public String listAndSearchInvoices(
	            @RequestParam(value = "search", required = false) String search,
	            @RequestParam(value = "invoiceType", required = false) String invoiceType,
	            Model model) {

	        // Retrieve all distinct invoice types for the filter dropdown
	        List<String> distinctInvoiceTypes = invoiceRepository.findDistinctInvoiceType();
	        model.addAttribute("distinctInvoiceTypes", distinctInvoiceTypes);

	        // Retrieve all distinct store codes for pre-populating the dropdown
	        List<String> storeCodes = invoiceRepository.findDistinctActiveStoreCodes();
	        model.addAttribute("storeCodes", storeCodes);
	        
	        List<String> invoiceNumbers = invoiceRepository.findDistinctInvoiceNumbers();
	        model.addAttribute("invoiceNumbers", invoiceNumbers);

 
	        // Retrieve all invoices from the service as a List
	        List<Invoice> invoices;

	        if (search != null && !search.isEmpty()) {
	            // If search parameter is provided, filter by search term
	            invoices = invoiceRepository.findBySearchTerm(search);
	        } else if (invoiceType != null && !invoiceType.isEmpty()) {
	            // If invoiceType parameter is provided, filter by invoice type
	            invoices = invoiceRepository.findByInvoiceType(invoiceType);
	        } else {
	            // Otherwise, retrieve all invoices
	            invoices = invoiceRepository.findByActiveStatusTrue();
	        }

	        // Add the list of invoices, search parameter, selected invoiceType, and store codes to the model
	        model.addAttribute("invoices", invoices);
	        model.addAttribute("search", search);
	        model.addAttribute("selectedInvoiceType", invoiceType);

	        return "invoiceslistadmin";
	    }
	    
	  
	    //View
	    @GetMapping("/viewinvoicedetails/{id}")
	    public String viewInvoiceDetails(@PathVariable Long id, Model model) {
	    	model.addAttribute("invoice", invoiceService.getInvoiceById(id));
	    	return "viewadmininvoicedts";
	    }
	    
	    @GetMapping("/clientstoreview/{id}")
	    public String viewClientInfo(@PathVariable Long id, Model model) {
	        Invoice invoice = invoiceService.getInvoiceById(id);

	        if (invoice != null) {
	            // Fetch associated Client information based on the storeCode in the Invoice
	            Client client = clientService.getClientByStoreCode(invoice.getStoreCode());

	            // Add the Invoice and Client information to the model
	            model.addAttribute("invoice", invoice);
	            model.addAttribute("client", client);
	        }

	        return "accountstrorecontactinfoadmin";
	    }
	    
	    @GetMapping("/invoiceComment/{id}")
	    public String invoiceComment(@PathVariable Long id,Model model) {
	    	model.addAttribute("invoice", invoiceService.getInvoiceById(id));
	    	return "invoicecommentbox";
	    }
	    
	    @PostMapping("/invoiceComments/{id}")
	  		public String saveInvoiceComments(@PathVariable Long id,
	  		                           @ModelAttribute("invoiceComments") InvoiceComments invoiceComments,
	  		                           @RequestParam("commentText") String commentText,
	  		                           @RequestParam(value = "requestStatus", defaultValue = "false") boolean requestStatus,
	  		                           Model model,Principal principal) {

	  		    // Retrieve the AddSupportAdmin entity
	  		    Invoice existingInvoice = invoiceService.getInvoiceById(id);

	  	    	invoiceService.saveInvoice(existingInvoice);
	  		    
	  	    	  String notificationMessage = "Admin has made some comments for Invoice ID:";
	  			    String invoiceId = existingInvoice.getInvoiceNumber();
	  			    String storeCode = existingInvoice.getStoreCode();
	  			    String notification = notificationMessage + " " + invoiceId + " - " + storeCode;
	  			    String username = principal.getName();
	  			    String displayName = getDisplayNameByUsername(username);

	  		  

	  		    // Create a new AdminComments
	  		    InvoiceComments comment = new InvoiceComments();
	  		    comment.setInvoice(existingInvoice);
	  		    comment.setAddedBy(displayName + "(Admin)");
	  		    comment.setAdminComments(commentText);
	  		    comment.setNotification(notification);
	  		    comment.setRequestStatus(requestStatus); // Set "yes" if true, "no" otherwise
	  		    comment.setReason("Regular Comment");
	  		    comment.setDateAdded(new Date());
	  		    comment.setTimeAdded(new Date());

	  		    invoiceCommentsService.saveComment(comment);
	  		    
	  		    List<String> usernamesBasedOnStorecode = invoiceService.getUsernamesBasedOnStoreCode("ROLE_CLIENT_SUPPORT", storeCode);
	  		    System.out.println("Usernames based on storecode: " + usernamesBasedOnStorecode);

	  		    // Get emails based on usernames
	  		    List<String> emailsBasedOnUsernames = invoiceService.getEmailsBasedOnUsernames(usernamesBasedOnStorecode);
	  		    System.out.println("Emails based on usernames: " + emailsBasedOnUsernames);

	  		    // Call this in your controller method
	  		    invoiceService.sendEmailNotification(emailsBasedOnUsernames, notification, commentText);  
	  		    // Handle requestStatus as needed


	  		    // Redirect to the support view page
	  		    return "redirect:/viewinvoice";
	  		}

	    
	    
	    @GetMapping("/capture/{id}")
	    public String capturepopup(@PathVariable Long id,Model model) {
	    	model.addAttribute("invoice", invoiceService.getInvoiceById(id));
	    	return "invoicecapturepaypop";
	    }
	    
	    @PostMapping("/captureComment/{id}")
		public String saveComments(@PathVariable Long id,
		                           @ModelAttribute("invoiceComments") InvoiceComments invoiceComments,
		                           @RequestParam("commentText") String commentText,
		                           @RequestParam(value = "requestStatus", defaultValue = "false") boolean requestStatus,
		                           Model model,Principal principal) {

		    // Retrieve the AddSupportAdmin entity
		    Invoice existingInvoice = invoiceService.getInvoiceById(id);

	    	existingInvoice.setInvoiceStatus("Paid");
	    	invoiceService.saveInvoice(existingInvoice);
		    
	    	  String notificationMessage = "Admin has changed the status to paid for Invoice ID:";
			    String supportRequestId = existingInvoice.getInvoiceNumber();
			    String storeCode = existingInvoice.getStoreCode();
			    String notification = notificationMessage + " " + supportRequestId + " - " + storeCode;
			    String username = principal.getName();
			    String displayName = getDisplayNameByUsername(username);

		  

		    // Create a new AdminComments
		    InvoiceComments comment = new InvoiceComments();
		    comment.setInvoice(existingInvoice);
		    comment.setAddedBy(displayName + "(Admin)");
		    comment.setAdminComments(commentText);
		    comment.setNotification(notification);
		    comment.setRequestStatus(requestStatus); // Set "yes" if true, "no" otherwise
		    comment.setReason("Admin marked as Paid");
		    comment.setDateAdded(new Date());
		    comment.setTimeAdded(new Date());

		    invoiceCommentsService.saveComment(comment);
		    
		    List<String> usernamesBasedOnStorecode = invoiceService.getUsernamesBasedOnStoreCode("ROLE_CLIENT_SUPPORT", storeCode);
		    System.out.println("Usernames based on storecode: " + usernamesBasedOnStorecode);

		    // Get emails based on usernames
		    List<String> emailsBasedOnUsernames = invoiceService.getEmailsBasedOnUsernames(usernamesBasedOnStorecode);
		    System.out.println("Emails based on usernames: " + emailsBasedOnUsernames);

		    // Call this in your controller method
		    invoiceService.sendEmailNotification(emailsBasedOnUsernames, notification, commentText);  
		    // Handle requestStatus as needed


		    // Redirect to the support view page
		    return "redirect:/viewinvoice";
		}


	    
	    @GetMapping("/uncapture/{id}")
	    public String uncapturepopup(@PathVariable Long id,Model model) {
	    	model.addAttribute("invoice", invoiceService.getInvoiceById(id));
	    	return "invoiceuncapturepaypop";
	    }
	    
	    @PostMapping("/uncaptureComment/{id}")
		public String uncaptureComments(@PathVariable Long id,
		                           @ModelAttribute("invoiceComments") InvoiceComments invoiceComments,
		                           @RequestParam("commentText") String commentText,
		                           @RequestParam(value = "requestStatus", defaultValue = "false") boolean requestStatus,
		                           Model model, Principal principal) {

		    // Retrieve the AddSupportAdmin entity
		    Invoice existingInvoice = invoiceService.getInvoiceById(id);

	    	existingInvoice.setInvoiceStatus("Pending");
	    	invoiceService.saveInvoice(existingInvoice);
		    
	    	 String notificationMessage = "Admin has changed the status to pending for Invoice ID:";
			    String supportRequestId = existingInvoice.getInvoiceNumber();
			    String storeCode = existingInvoice.getStoreCode();
			    String notification = notificationMessage + " " + supportRequestId + " - " + storeCode;
			    String userName = principal.getName();
			    String displayName = getDisplayNameByUsername(userName);
		  

		    // Create a new AdminComments
		    InvoiceComments comment = new InvoiceComments();
		    comment.setInvoice(existingInvoice);
		    comment.setAddedBy(displayName +"(Admin)");
		    comment.setNotification(notification);
		    comment.setAdminComments(commentText);
		    comment.setRequestStatus(requestStatus); // Set "yes" if true, "no" otherwise
		    comment.setReason("Admin marked as Pending");
		    comment.setDateAdded(new Date());
		    comment.setTimeAdded(new Date());

		    invoiceCommentsService.saveComment(comment);
		    
		    List<String> usernamesBasedOnStorecode = invoiceService.getUsernamesBasedOnStoreCode("ROLE_CLIENT_SUPPORT", storeCode);
		    System.out.println("Usernames based on storecode: " + usernamesBasedOnStorecode);

		    // Get emails based on usernames
		    List<String> emailsBasedOnUsernames = invoiceService.getEmailsBasedOnUsernames(usernamesBasedOnStorecode);
		    System.out.println("Emails based on usernames: " + emailsBasedOnUsernames);

		    // Call this in your controller method
		    invoiceService.sendEmailNotification(emailsBasedOnUsernames, notification, commentText);  
		    // Handle requestStatus as needed


		    // Redirect to the support view page
		    return "redirect:/viewinvoice";
		}
	    
	   /* @PostMapping("/saveCommentToClient")
	    public String saveComments(
	            @RequestParam("id") Long id,
	            @RequestParam("commentText") String commentText,
	            @RequestParam("clientVisible") String clientVisible,
	            @RequestParam("requestStatus") String requestStatus
	    ) {
	        invoiceService.saveCommentAndStatusById(id, commentText, clientVisible, requestStatus);

	        return "redirect:/viewinvoice";
	    }*/
	    
	    /*@RequestMapping(value = {"/viewinvoices", "/viewinvoices"}, method = {RequestMethod.GET, RequestMethod.POST})
	    public String listAndSearchInvoice(
	            @RequestParam(value = "search", required = false) String search,
	            @RequestParam(value = "invoiceType", required = false) String invoiceType,
	            Model model) {

	        // Retrieve all distinct invoice types for the filter dropdown
	        List<String> distinctInvoiceTypes = invoiceRepository.findDistinctInvoiceType();
	        model.addAttribute("distinctInvoiceTypes", distinctInvoiceTypes);

	        // Retrieve all distinct invoice numbers for pre-populating the dropdown
	        List<String> invoiceNumbers = invoiceRepository.findDistinctInvoiceNumbers();
	        model.addAttribute("invoiceNumbers", invoiceNumbers);

	        // Retrieve all invoices from the service as a List
	        List<Invoice> invoices;

	        if (search != null && !search.isEmpty()) {
	            // If search parameter is provided, filter by search term
	            invoices = invoiceRepository.findBySearchTerm(search);
	        } else if (invoiceType != null && !invoiceType.isEmpty()) {
	            // If invoiceType parameter is provided, filter by invoice type
	            invoices = invoiceRepository.findByInvoiceType(invoiceType);
	        } else {
	            // Otherwise, retrieve all invoices
	            invoices = invoiceRepository.findByActiveStatusTrue();
	        }

	        // Add the list of invoices, search parameter, and selected invoiceType to the model
	        model.addAttribute("invoices", invoices);
	        model.addAttribute("search", search);
	        model.addAttribute("selectedInvoiceType", invoiceType);

	        return "clientinvoiceslist"; 
	    }*/

	    @RequestMapping(value = {"/viewinvoices", "/viewinvoices"}, method = {RequestMethod.GET, RequestMethod.POST})
	    public String listAndSearchInvoice(
	            @RequestParam(value = "search", required = false) String search,
	            @RequestParam(value = "invoiceType", required = false) String invoiceType,
	            Model model,
	            Principal principal // Add Principal parameter to retrieve logged-in user information
	    ) {
	        // Retrieve the currently logged-in user's username
	        String username = principal.getName();

	        // Retrieve the user's store code based on their username
	        String userStoreCode = userService.getUserStoreCodeByUserName(username); // Replace userService with your actual service

	        // Retrieve all distinct invoice types for the filter dropdown
	        List<String> distinctInvoiceTypes = invoiceRepository.findDistinctInvoiceType();
	        model.addAttribute("distinctInvoiceTypes", distinctInvoiceTypes);

	        // Retrieve all distinct invoice numbers for pre-populating the dropdown
	        List<String> invoiceNumbers = invoiceRepository.findDistinctInvoiceNumbers();
	        model.addAttribute("invoiceNumbers", invoiceNumbers);

	        // Retrieve all invoices from the service as a List based on user's store code and active status true
	        List<Invoice> invoices;

	        if (search != null && !search.isEmpty()) {
	            // If search parameter is provided, filter by search term
	            invoices = invoiceRepository.findBySearchTermAndStoreCodeAndActiveStatusTrue(search, userStoreCode);
	        } else if (invoiceType != null && !invoiceType.isEmpty()) {
	            // If invoiceType parameter is provided, filter by invoice type
	            invoices = invoiceRepository.findByInvoiceTypeAndStoreCodeAndActiveStatusTrue(invoiceType, userStoreCode);
	        } else {
	            // Otherwise, retrieve all invoices for the user's store code with active status true
	            invoices = invoiceRepository.findByStoreCodeAndActiveStatusTrue(userStoreCode);
	        }

	        // Add the list of invoices, search parameter, and selected invoiceType to the model
	        model.addAttribute("invoices", invoices);
	        model.addAttribute("search", search);
	        model.addAttribute("selectedInvoiceType", invoiceType);

	        return "clientinvoiceslist"; 
	    }

	    
		 private List<String> base64ImageList; // Declare it as a class field

		    @GetMapping("/invoiceDetails/{id}")
		    public String viewInvoicesDetails(@PathVariable Long id, Model model) {
		        // Your existing code to populate addSupportAdmin and supportFilesList
		    	 Invoice invoice = invoiceService.getInvoiceById(id);
		 	    
		 	   List<InvoiceComments> invoiceCommentsList = invoice.getComments();

		        model.addAttribute("invoice", invoice);
		        model.addAttribute("invoiceCommentsList", invoiceCommentsList);

		        return "invoiceadmin";
		    }
		    
		 
		    
		    @GetMapping("/invoiceDetail/{id}")
		    public String viewImagesDetails(@PathVariable Long id, Model model) {
		        // Your existing code to populate addSupportAdmin and supportFilesList
		    	 Invoice invoice = invoiceService.getInvoiceById(id);
		    	

		 	    // Get the list of SupportFiles associated with the AddSupportAdmin
		 	    List<InvoiceFile> invoiceFilesList = invoice.getInvoiceFiles();

		 	    // Add the AddSupportAdmin and SupportFiles list to the model
		 	    model.addAttribute("invoice", invoice);

		        // Convert SupportFiles data to Base64 and add to the model
		        base64ImageList = new ArrayList<>();
		        for (InvoiceFile invoiceFile : invoiceFilesList) {
		            if (invoiceFile.getFileData() != null) {
		                String base64Image = Base64.getEncoder().encodeToString(invoiceFile.getFileData());
		                base64ImageList.add(base64Image);
		            } else {
		                base64ImageList.add(""); // or null, depending on your preference
		            }
		        }

		        model.addAttribute("invoice", invoice);
		        model.addAttribute("base64ImageList", base64ImageList);

		        return "viewfiles-invoiceAdmin";
		    }


		    @GetMapping("/downloaded/{id}")
		    public ResponseEntity<ByteArrayResource> downloadImage(@PathVariable Integer id) {
		        // Assuming id is the index of the image in the list

		        if (id >= 0 && id < base64ImageList.size()) {
		            String base64Image = base64ImageList.get(id);
		            byte[] imageBytes = Base64.getDecoder().decode(base64Image);

		            ByteArrayResource resource = new ByteArrayResource(imageBytes);

		            HttpHeaders headers = new HttpHeaders();
		            headers.setContentDispositionFormData("attachment", "image.png");
		            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

		            return ResponseEntity.ok()
		                    .headers(headers)
		                    .contentLength(imageBytes.length)
		                    .body(resource);
		        } else {
		            return ResponseEntity.notFound().build();
		        }
		    }
		    
		    @GetMapping("/clientinvoiceDetails/{id}")
		    public String clientviewInvoicesDetails(@PathVariable Long id, Model model) {
		    	 Invoice invoice = invoiceService.getInvoiceById(id);
			    	

			 	    // Get the list of SupportFiles associated with the AddSupportAdmin
			 	    List<InvoiceFile> invoiceFilesList = invoice.getInvoiceFiles();
			 	    
			 	   List<InvoiceComments> invoiceCommentsList = invoice.getComments().stream()
			 		        .filter(InvoiceComments::getRequestStatus) // Filter by getRequestStatus = true
			 		        .collect(Collectors.toList());


			 	    // Add the AddSupportAdmin and SupportFiles list to the model
			 	    model.addAttribute("invoice", invoice);

			        // Convert SupportFiles data to Base64 and add to the model
			        base64ImageList = new ArrayList<>();
			        for (InvoiceFile invoiceFile : invoiceFilesList) {
			            if (invoiceFile.getFileData() != null) {
			                String base64Image = Base64.getEncoder().encodeToString(invoiceFile.getFileData());
			                base64ImageList.add(base64Image);
			            } else {
			                base64ImageList.add(""); // or null, depending on your preference
			            }
			        }


		        model.addAttribute("invoice", invoice);
		        model.addAttribute("base64ImageList", base64ImageList);
		        model.addAttribute("invoiceCommentsList", invoiceCommentsList);

		        return "invoiceclient";
		    }

		   /* @GetMapping("/invoicedownload/{id}")
		    public ResponseEntity<ByteArrayResource> clientdownloadImage(@PathVariable Integer id) {
		        // Assuming id is the index of the image in the list

		        if (id >= 0 && id < base64ImageList.size()) {
		            String base64Image = base64ImageList.get(id);
		            byte[] imageBytes = Base64.getDecoder().decode(base64Image);

		            ByteArrayResource resource = new ByteArrayResource(imageBytes);

		            HttpHeaders headers = new HttpHeaders();
		            headers.setContentDispositionFormData("attachment", "image.png");
		            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

		            return ResponseEntity.ok()
		                    .headers(headers)
		                    .contentLength(imageBytes.length)
		                    .body(resource);
		        } else {
		            return ResponseEntity.notFound().build();
		        }
		    }*/
		    
		    @GetMapping("/invoice/delete/{id}")
		    public String deleteInvoice(@PathVariable Long id) {
		        invoiceService.deleteInvoiceById(id);
		        return "redirect:/viewinvoice";
		    }
		    
		    @Autowired
		    private InvoiceFileRepository invoiceFileRepository;
		    
		    @GetMapping("/invoicedownload/{id}")
		    public ResponseEntity<ByteArrayResource> downloadImages(@PathVariable Integer id) {
		        // Assuming id is the index of the image in the list

		        if (id >= 0 && id < base64ImageList.size()) {
		            String base64Image = base64ImageList.get(id);
		            byte[] imageBytes = Base64.getDecoder().decode(base64Image);

		            ByteArrayResource resource = new ByteArrayResource(imageBytes);

		            HttpHeaders headers = new HttpHeaders();
		            headers.setContentDispositionFormData("attachment", "image.png");
		            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

		            return ResponseEntity.ok()
		                    .headers(headers)
		                    .contentLength(imageBytes.length)
		                    .body(resource);
		        } else {
		            return ResponseEntity.notFound().build();
		        }
		    }    
}
