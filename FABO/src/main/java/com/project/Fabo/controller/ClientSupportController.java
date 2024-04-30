package com.project.Fabo.controller;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.project.Fabo.entity.AddSupportAdmin;
import com.project.Fabo.entity.Admin;
import com.project.Fabo.entity.AdminComments;
import com.project.Fabo.entity.Client;
import com.project.Fabo.entity.ClientProduct;
import com.project.Fabo.entity.ClientSupport;
import com.project.Fabo.entity.ClientUser;
import com.project.Fabo.entity.ProductFiles;
import com.project.Fabo.entity.SupportFiles;
import com.project.Fabo.entity.User;
import com.project.Fabo.repository.ClientRepository;
import com.project.Fabo.repository.ClientSupportRepository;
import com.project.Fabo.repository.ClientUserRepository;
import com.project.Fabo.repository.SupportFilesRepository;
import com.project.Fabo.service.AddSupportAdminService;
import com.project.Fabo.service.AdminCommentService;
import com.project.Fabo.service.ClientSupportService;
import com.project.Fabo.service.ClientUserService;
import com.project.Fabo.service.SupportMirrorService;
import com.project.Fabo.service.UserService;

@Controller
public class ClientSupportController {
	@Autowired
	private ClientSupportService clientSupportService;
	
	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private ClientSupportRepository clientSupportRepository;
	
	@Autowired
	private SupportMirrorService supportMirrorService;
	
	@Autowired
	private AddSupportAdminService addSupportAdminService;
	
	@Autowired
	private AdminCommentService admincommentService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private ClientUserService clientUserService;
	
	@Autowired
	private ClientUserRepository clientUserRepository;


	public ClientSupportController() {
	        // Default constructor
	    }
	

	@GetMapping("/clientsupport")
	public String createSupportByClient(Model model, Principal principal) {
	    String username = principal.getName();
	    
	    // Assuming you have a method to fetch the store code for the logged-in user
	    String userStoreCode = clientUserRepository.findStoreCodeByUserName(username);
	    
	    // Retrieve the client record using the user's store code
	    Client client = clientRepository.findByStoreCode(userStoreCode);
	    
	    ClientSupport clientSupport = new ClientSupport();
	    model.addAttribute("clientSupport", clientSupport);
	    
	    // Add the client record to the model to autofill store code, store name, and store contact
	    model.addAttribute("client", client);
	    
	    // Add the current date
	    model.addAttribute("Date", LocalDate.now());
	    
	    return "clientaddsupport";
	}


	 private String getDisplayNameByUsername(String username) {
	        Optional<ClientUser> clientUser = clientUserService.findByUserName(username);

	        if (clientUser.isPresent()) {
	            return clientUser.get().getDisplayName();
	       } else {
	            // Handle the case when no matching user is found
	            return "Unknown User";
	        }
	    }

	@PostMapping("/clientsupportview")
	public String saveClientSupport(@ModelAttribute("clientSupport") ClientSupport clientSupport,
			@RequestParam("storeName") String storeName,@RequestParam("phoneNumber") String storeContact,
			 @RequestParam(value = "files", required = false)  List<MultipartFile> files, Principal principal, @RequestParam("Date") String Date) {
		clientSupport.setStatus("New");
		clientSupport.setCommentsToAdmin("No Comments");
		clientSupport.setExternalComments("No Comments");
		clientSupport.setStoreContact(storeContact);
		
		
		String username = principal.getName();
		String displayName = getDisplayNameByUsername(username);
		clientSupport.setUploadedBy(displayName);
		
		clientSupportService.saveClientSupport(clientSupport);
		  supportMirrorService.addSupportRecordToBothSide(clientSupport);
		  AddSupportAdmin addSupportAdmin = addSupportAdminService.getAddSupportAdminById(clientSupport.getId());
		 List<SupportFiles> adminSupportFiles = new ArrayList<>();

	        if (files != null && !files.isEmpty()) {
	            for (MultipartFile file : files) {
	                try {
	                	SupportFiles adminSupportFile = new SupportFiles();
	                	adminSupportFile.setFileData(file.getBytes());
	                	adminSupportFile.setAddSupportAdmin(addSupportAdmin);
	                	adminSupportFile.setClientSupport(clientSupport);
	                	adminSupportFiles.add(adminSupportFile);
	                } catch (IOException e) {
	                    // Handle the IOException as needed
	                    e.printStackTrace(); // Log the exception or throw a custom exception
	                }
	            }
	        }

	        // Associate files with the invoice
	        clientSupport.setAdminSupportFiles(adminSupportFiles);
		clientSupportService.saveClientSupport(clientSupport);
		
		 String notificationMessage = "Client has opened a new support request with ID: ";
		    String supportRequestId = clientSupport.getSupportRequestId();
		    String storeCode = clientSupport.getStoreCode();
		    String notification = notificationMessage + " " + supportRequestId + " - " + storeCode;
		
		
		    List<User> usersWithAdminProductRole = userService.getUsersByName("ROLE_ADMIN_SUPPORT");
		    List<String> usernames = usersWithAdminProductRole.stream().map(User::getUserName).collect(Collectors.toList());
		    System.out.println("Usernames based on ROLE_ADMIN_PRODUCT: " + usernames);

		    List<String> emailsBasedOnUsernamesFromUsers = clientSupportService.getEmailsBasedOnUsernamesFromUsers(usernames);
		    System.out.println("Emails based on usernames from Users: " + emailsBasedOnUsernamesFromUsers);

		    List<String> emailsBasedOnUsernamesFromAdmins = clientSupportService.getEmailsBasedOnUsernamesFromAdmins(usernames);
		    System.out.println("Emails based on usernames from Admins: " + emailsBasedOnUsernamesFromAdmins);
		    	
		    // After saving the addProductAdmin, set the formatted product request ID based on the generated ID
		    addSupportAdmin.setFormattedSupportNumber(addSupportAdmin.getId());
		    // Save the addProductAdmin again to update the product request ID
		    addSupportAdminService.saveAddSupportAdmin(addSupportAdmin);

		    
		    addSupportAdminService.sendEmailNotification(addSupportAdmin, emailsBasedOnUsernamesFromAdmins,files,  notification);

		return "redirect:/viewclientsupport";
	}
	
	 
	
	/*@GetMapping("/viewclientsupport")
	public String filtersWithList(
	    @RequestParam(value = "supportReqyestType", required = false) String supportRequesttype,
	    @RequestParam(value = "statusDropdown", required = false) String statusDropdown,
	    @RequestParam(value = "searchTerm", required = false) String searchTerm,
	    Model model
	) {
	    List<ClientSupport> clientSupports;

	    if (searchTerm != null && !searchTerm.isEmpty()) {
	        // Filter by search term across multiple fields
	        clientSupports = clientSupportRepository.findBySearchTerm(searchTerm);
	        model.addAttribute("searchTerm", searchTerm);
	    } else if (supportRequesttype != null && !supportRequesttype.isEmpty()) {
	        // Filter by invoice dropdown
	        if ("All".equalsIgnoreCase(supportRequesttype)) {
	            clientSupports = clientSupportRepository.findByActiveStatusTrue();
	        } else {
	            clientSupports = clientSupportRepository.findBySupportRequestTypeContaining(supportRequesttype);
	        }
	        model.addAttribute("selectedType", supportRequesttype);
	    } else if (statusDropdown != null && !statusDropdown.isEmpty()) {
	        // Filter by status dropdown
	        if ("All".equalsIgnoreCase(statusDropdown)) {
	            clientSupports = clientSupportRepository.findByActiveStatusTrue();
	        } else {
	            clientSupports = clientSupportRepository.findByStatus(statusDropdown);
	        }
	        model.addAttribute("selected", statusDropdown);
	    } else {
	        // No filters applied, return all records
	        clientSupports = clientSupportRepository.findByActiveStatusTrue();
	    }
	    model.addAttribute("clientSupports", clientSupports);
	    return "clientsupportlist";
	}*/
	
	@GetMapping("/viewclientsupport")
	public String filtersWithList(
	    @RequestParam(value = "supportReqyestType", required = false) String supportRequesttype,
	    @RequestParam(value = "statusDropdown", required = false) String statusDropdown,
	    @RequestParam(value = "searchTerm", required = false) String searchTerm,
	    Model model,
	    Principal principal // Add Principal parameter to retrieve logged-in user information
	) {
	    String username = principal.getName(); // Retrieve the currently logged-in user's email

	    // Retrieve the user's store code based on their email (assuming you have a method to do this)
	    String userStoreCode = userService.getUserStoreCodeByUserName(username); // Replace userService with your actual service

	    List<ClientSupport> clientSupports;

	    if (searchTerm != null && !searchTerm.isEmpty()) {
	        // Filter by search term across multiple fields
	        clientSupports = clientSupportRepository.findBySearchTermAndStoreCode(searchTerm, userStoreCode);
	        model.addAttribute("searchTerm", searchTerm);
	    } else if (supportRequesttype != null && !supportRequesttype.isEmpty()) {
	        // Filter by support request type and store code
	        if ("All".equalsIgnoreCase(supportRequesttype)) {
	            clientSupports = clientSupportRepository.findBySupportRequestTypeAndStoreCode(supportRequesttype, userStoreCode);
	        } else {
	            clientSupports = clientSupportRepository.findBySupportRequestTypeContainingAndStoreCode(supportRequesttype, userStoreCode);
	        }
	        model.addAttribute("selectedType", supportRequesttype);
	    } else if (statusDropdown != null && !statusDropdown.isEmpty()) {
	        // Filter by status dropdown and store code
	        if ("All".equalsIgnoreCase(statusDropdown)) {
	            clientSupports = clientSupportRepository.findByStatusAndStoreCode(statusDropdown, userStoreCode);
	        } else {
	            clientSupports = clientSupportRepository.findByStatusAndStoreCode(statusDropdown, userStoreCode);
	        }
	        model.addAttribute("selected", statusDropdown);
	    } else {
	        // No filters applied, return all records for the user's store code
	        clientSupports = clientSupportRepository.findByStoreCode(userStoreCode);
	    }
	    model.addAttribute("clientSupports", clientSupports);
	    System.out.println("Number of clientSupports populated: " + clientSupports.size());
	    return "clientsupportlist";
	}


	@GetMapping("/viewsupportclient/{id}")
	public String viewSupportdetails(@PathVariable Long id, Model model) {
		model.addAttribute("clientSupport", clientSupportService.getClientSupportById(id));
		return "clientsupportcapturecomentpop";
	}
	
	@PostMapping("/saveComment/{id}")
	public String saveComments(@PathVariable Long id,
	                           @ModelAttribute("adminComments") AdminComments adminComments,
	                           @RequestParam("commentText") String commentText,
	                           Model model, Principal principal) {

	    // Retrieve the AddSupportAdmin entity
	    AddSupportAdmin existingAddSupportAdmin = addSupportAdminService.getAddSupportAdminById(id);
	    
	    ClientSupport existingClientSupport = clientSupportService.getClientSupportById(id);
	    
	    String notificationMessage = "Client has made some comments on support request ID:";
	    String supportRequestId = existingClientSupport.getSupportRequestId();
	    String storeCode = existingAddSupportAdmin.getStoreCode();
	    String notification = notificationMessage + " " + supportRequestId + " - " + storeCode;
	    
	    String userName= principal.getName();
	    String displayName = getDisplayNameByUsername(userName);
	    
	    // Create a new AdminComments
	    AdminComments comment = new AdminComments();
	    comment.setAddSupportAdmin(existingAddSupportAdmin);
	    comment.setClientSupport(existingClientSupport);
	    comment.setAdminComments(commentText);
	    comment.setNotification(notification);
	    comment.setStoreCode(existingClientSupport.getStoreCode());
	    comment.setAddedBy(displayName+"(Client)");
	    comment.setRequestStatus(true); // Set "yes" if true, "no" otherwise
	    comment.setReason("Regular Comment");
	    comment.setDateAdded(new Date());
	    comment.setTimeAdded(new Date());

	    // Set the supportRequestId indirectly
	    comment.setSupportRequestId(existingClientSupport.getSupportRequestId());

	    // Save the comment
	    admincommentService.saveComment(comment);
	    
	 // Assuming you have an instance of NotificationService injected into your controller

	    List<User> usersWithAdminSupportRole = userService.getUsersByName("ROLE_ADMIN_SUPPORT");
	    List<String> usernames = usersWithAdminSupportRole.stream().map(User::getUserName).collect(Collectors.toList());
	    System.out.println("Usernames based on ROLE_ADMIN_SUPPORT: " + usernames);

	    List<String> emailsBasedOnUsernamesFromUsers = clientSupportService.getEmailsBasedOnUsernamesFromUsers(usernames);
	    System.out.println("Emails based on usernames from Users: " + emailsBasedOnUsernamesFromUsers);

	    List<String> emailsBasedOnUsernamesFromAdmins = clientSupportService.getEmailsBasedOnUsernamesFromAdmins(usernames);
	    System.out.println("Emails based on usernames from Admins: " + emailsBasedOnUsernamesFromAdmins);

	    addSupportAdminService.sendEmailNotifications(emailsBasedOnUsernamesFromAdmins, notification, commentText);

	    
	    

	    // Redirect to the support view page
	    return "redirect:/viewclientsupport";
	}
	
	@GetMapping("/close/{id}")
	public String closed(@PathVariable Long id,Model model) {
		model.addAttribute("clientSupport", clientSupportService.getClientSupportById(id));
		return "closesupportrequestpop";
	}
	
	@PostMapping("/closesupportrequest/{id}")
	public String saveCloseComments(@PathVariable Long id,
	                           @ModelAttribute("adminComments") AdminComments adminComments,
	                           @RequestParam("commentText") String commentText,
	                           Model model, Principal principal) {

	    // Retrieve the AddSupportAdmin entity
	    AddSupportAdmin existingAddSupportAdmin = addSupportAdminService.getAddSupportAdminById(id);
	    
	    ClientSupport existingClientSupport = clientSupportService.getClientSupportById(id);
	    
		existingClientSupport.setStatus("Closed");
		existingAddSupportAdmin.setStatus("Closed");
		   clientSupportService.saveClientSupport(existingClientSupport);
		   
		   String notificationMessage = "Client has closed the support request ID:";
		    String supportRequestId = existingClientSupport.getSupportRequestId();
		    String storeCode = existingAddSupportAdmin.getStoreCode();
		    String notification = notificationMessage + " " + supportRequestId + " - " + storeCode;
		    
		    String userName = principal.getName();
		    String displayName = getDisplayNameByUsername(userName);


	    // Create a new AdminComments
	    AdminComments comment = new AdminComments();
	    comment.setAddSupportAdmin(existingAddSupportAdmin);
	    comment.setClientSupport(existingClientSupport);
	    comment.setAddedBy(displayName+"(Client)");
	    comment.setNotification(notification);
	    comment.setStoreCode(existingClientSupport.getStoreCode());
	    comment.setAdminComments(commentText);
	    comment.setRequestStatus(true);
	    comment.setReason("client opted to close the support request");
	    comment.setDateAdded(new Date());
	    comment.setTimeAdded(new Date());

	    // Set the supportRequestId indirectly
	    comment.setSupportRequestId(existingAddSupportAdmin.getSupportRequestId());
	    
	    // Save the comment
	    admincommentService.saveComment(comment);

	    // Handle requestStatus as needed
	    List<User> usersWithAdminSupportRole = userService.getUsersByName("ROLE_ADMIN_SUPPORT");
	    List<String> usernames = usersWithAdminSupportRole.stream().map(User::getUserName).collect(Collectors.toList());
	    System.out.println("Usernames based on ROLE_ADMIN_SUPPORT: " + usernames);

	    List<String> emailsBasedOnUsernamesFromUsers = clientSupportService.getEmailsBasedOnUsernamesFromUsers(usernames);
	    System.out.println("Emails based on usernames from Users: " + emailsBasedOnUsernamesFromUsers);

	    List<String> emailsBasedOnUsernamesFromAdmins = clientSupportService.getEmailsBasedOnUsernamesFromAdmins(usernames);
	    System.out.println("Emails based on usernames from Admins: " + emailsBasedOnUsernamesFromAdmins);

	    addSupportAdminService.sendEmailNotifications(emailsBasedOnUsernamesFromAdmins, notification, commentText);

	    // Redirect to the support view page
	    return "redirect:/viewclientsupport";
	}

	private List<String> base64ImageList; // Declare it as a class field

    @GetMapping("/clientsupportDetails/{id}")
    public String viewSupportDetails(@PathVariable Long id, Model model) {
        // Your existing code to populate addSupportAdmin and supportFilesList
    	 ClientSupport clientSupport = clientSupportService.getClientSupportById(id);
 
 	   List<AdminComments> adminCommentsList = clientSupport.getComments().stream()
 		        .filter(AdminComments::getRequestStatus) // Filter by getRequestStatus = true
 		        .collect(Collectors.toList());


 	    // Add the AddSupportAdmin and SupportFiles list to the model
 	    model.addAttribute("ClientSupport", clientSupport);

        model.addAttribute("clientSupport", clientSupport);
        model.addAttribute("adminCommentsList", adminCommentsList);

        return "supportclient";
    }
    
    
 /*   @GetMapping("/saveClientSupportimages/{id}")
    public String saveClientSupportimages(@PathVariable Long id, Model model) {
        // Your existing code to populate addSupportAdmin and supportFilesList
    	 ClientSupport clientSupport = clientSupportService.getClientSupportById(id);

 	    // Get the list of SupportFiles associated with the AddSupportAdmin
 	    List<SupportFiles> supportFilesList = clientSupport.getAdminSupportFiles();

 	    // Add the AddSupportAdmin and SupportFiles list to the model
 	    model.addAttribute("ClientSupport", clientSupport);

        // Convert SupportFiles data to Base64 and add to the model
        base64ImageList = new ArrayList<>();
        for (SupportFiles supportFile : supportFilesList) {
            if (supportFile.getFileData() != null) {
                String base64Image = Base64.getEncoder().encodeToString(supportFile.getFileData());
                base64ImageList.add(base64Image);
            } else {
                base64ImageList.add(""); // or null, depending on your preference
            }
        }

        model.addAttribute("clientSupport", clientSupport);
        model.addAttribute("base64ImageList", base64ImageList);

        return "viewfiles-supportClient";
    }
    
    @PostMapping("/uploadImagesClientSupport/{id}")
    public String saveImages(@PathVariable Long id,
                              @RequestParam(value = "files", required = false) List<MultipartFile> files, Model model) {
    	 ClientSupport existingClientSupport = clientSupportService.getClientSupportById(id);
    	 model.addAttribute("existingClientSupport", existingClientSupport);
    	 
    	  AddSupportAdmin addSupportAdmin = addSupportAdminService.getAddSupportAdminById(existingClientSupport.getId());

    	 List<SupportFiles> clientSupportFiles = new ArrayList<>();
         if (files != null && !files.isEmpty()) {
             for (MultipartFile file : files) {
                 try {
                 	SupportFiles clientSupportFile = new SupportFiles();
                 	clientSupportFile.setFileData(file.getBytes());
                 	clientSupportFile.setAddSupportAdmin(addSupportAdmin);
                 	clientSupportFile.setClientSupport(existingClientSupport);
                 	clientSupportFiles.add(clientSupportFile);
                 } catch (IOException e) {
                     // Handle the IOException as needed
                     e.printStackTrace(); // Log the exception or throw a custom exception
                 }
             }
         }
         existingClientSupport.setAdminSupportFiles(clientSupportFiles);

         clientSupportService.saveClientSupport(existingClientSupport);

       
        return "redirect:/saveClientSupportimages/{id}";
    }
    
    @Autowired
    private SupportFilesRepository supportFilesRepository;
    
    
    @GetMapping("/download/clientSupport/{id}")
    public ResponseEntity<ByteArrayResource> downloadSupportFile(@PathVariable Long id) {
        // Find the SupportFiles entity by id
        Optional<SupportFiles> supportFilesOptional = supportFilesRepository.findById(id);
        
        if (supportFilesOptional.isPresent()) {
            SupportFiles supportFiles = supportFilesOptional.get();
            
            ByteArrayResource resource = new ByteArrayResource(supportFiles.getFileData());
            
            String filename = "file_" + id + "_" + System.currentTimeMillis() + ".png"; // Generate filename dynamically
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(supportFiles.getFileData().length)
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }*/
    
    
    @GetMapping("/saveClientSupportImages/{id}")
    public String saveSupportImages(@PathVariable Long id, Model model) {
        // Retrieve AddSupportAdmin by ID
        AddSupportAdmin addSupportAdmin = addSupportAdminService.getAddSupportAdminById(id);

        // Get the list of SupportFiles associated with the AddSupportAdmin
        List<SupportFiles> supportFilesList = addSupportAdmin.getAdminSupportFiles();

        // Add AddSupportAdmin to the model
        model.addAttribute("addSupportAdmin", addSupportAdmin);

        // Prepare a list to hold image details (Base64 data, ID, AddSupportAdmin ID)
        List<Map<String, Object>> imageDetailsList = new ArrayList<>();

        // Convert SupportFiles data to Base64 and add along with IDs to the list
        for (SupportFiles supportFile : supportFilesList) {
            if (supportFile.getFileData() != null) {
                Map<String, Object> imageDetails = new HashMap<>();
                String base64Image = Base64.getEncoder().encodeToString(supportFile.getFileData());
                imageDetails.put("base64Data", base64Image);
                imageDetails.put("imageId", supportFile.getId());
                imageDetails.put("addSupportAdminId", id);
                imageDetailsList.add(imageDetails);
            }
        }

        // Add the list of image details to the model
        model.addAttribute("imageDetailsList", imageDetailsList);

        return "viewfiles-supportClient";
    }


    
    @PostMapping("/uploadImagesClientSupport/{id}")
    public String saveImages(@PathVariable Long id,
                              @RequestParam(value = "files", required = false) List<MultipartFile> files, Model model) {
    	 AddSupportAdmin existingSupportAdmin = addSupportAdminService.getAddSupportAdminById(id);
    	 model.addAttribute("existingSupportAdmin",existingSupportAdmin);
    	 
    	 ClientSupport existingSupportClient = clientSupportService.getClientSupportById(id);
    	 model.addAttribute("existingSupportClient",existingSupportClient);

    	 List<SupportFiles> adminSupportFiles = new ArrayList<>();
         if (files != null && !files.isEmpty()) {
             for (MultipartFile file : files) {
                 try {
                 	SupportFiles adminSupportFile = new SupportFiles();
                 	adminSupportFile.setFileData(file.getBytes());
                 	adminSupportFile.setAddSupportAdmin(existingSupportAdmin);
                 	adminSupportFile.setClientSupport(existingSupportClient);
                 	adminSupportFiles.add(adminSupportFile);
                 } catch (IOException e) {
                     // Handle the IOException as needed
                     e.printStackTrace(); // Log the exception or throw a custom exception
                 }
             }
         }
         existingSupportAdmin.setAdminSupportFiles(adminSupportFiles);

         addSupportAdminService.saveAddSupportAdmin(existingSupportAdmin);

       
        return "redirect:/saveClientSupportImages/{id}";
    }
    
    
    @Autowired
    private SupportFilesRepository supportFilesRepository;
    
    @GetMapping("/download/clientsupport/{id}")
    public ResponseEntity<ByteArrayResource> downloadSupportFile(@PathVariable Long id) {
        // Find the SupportFiles entity by id
        Optional<SupportFiles> supportFilesOptional = supportFilesRepository.findById(id);
        
        if (supportFilesOptional.isPresent()) {
            SupportFiles supportFiles = supportFilesOptional.get();
            
            ByteArrayResource resource = new ByteArrayResource(supportFiles.getFileData());
            
            String filename = "file_" + id + "_" + System.currentTimeMillis() + ".png"; // Generate filename dynamically
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(supportFiles.getFileData().length)
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    

}
