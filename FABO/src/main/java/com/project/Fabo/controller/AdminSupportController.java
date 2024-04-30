 package com.project.Fabo.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

import com.project.Fabo.entity.AddProductAdmin;
import com.project.Fabo.entity.AddSupportAdmin;
import com.project.Fabo.entity.Admin;
import com.project.Fabo.entity.AdminComments;
import com.project.Fabo.entity.SupportFiles;
import com.project.Fabo.entity.User;
import com.project.Fabo.entity.Client;
import com.project.Fabo.entity.ClientProduct;
import com.project.Fabo.entity.ClientSupport;
import com.project.Fabo.entity.ClientUser;
import com.project.Fabo.entity.ProductFiles;
import com.project.Fabo.repository.ClientRepository;
import com.project.Fabo.repository.ClientUserRepository;
import com.project.Fabo.repository.SupportFilesRepository;
import com.project.Fabo.service.AddSupportAdminService;
import com.project.Fabo.service.ClientService;
import com.project.Fabo.service.ClientSupportService;
import com.project.Fabo.service.ClientUserService;
import com.project.Fabo.service.AdminCommentService;
import com.project.Fabo.service.AdminService;
import com.project.Fabo.service.SupportMirrorService;
import com.project.Fabo.service.UserService;
import com.project.Fabo.repository.AddSupportAdminRepository;


@Controller
public class AdminSupportController {
	
	private AddSupportAdminService addSupportAdminService;

	private AddSupportAdminRepository addSupportAdminRepository;
	
	private ClientRepository clientRepository;
	
	private SupportMirrorService supportMirrorService;
	
	private ClientService clientService;
	
	private ClientSupportService clientSupportService;
	
	private AdminCommentService admincommentService;
	
	private AdminService adminService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ClientUserService clientUserService;
	
	@Autowired
	private ClientUserRepository clientUserRepository;


	


	public AdminSupportController(AddSupportAdminService addSupportAdminService,
			AddSupportAdminRepository addSupportAdminRepository, ClientRepository clientRepository,
			SupportMirrorService supportMirrorService, ClientService clientService,
			ClientSupportService clientSupportService, AdminCommentService admincommentService,
			AdminService adminService, UserService userService, ClientUserService clientUserService,
			ClientUserRepository clientUserRepository, List<String> base64ImageList) {
		super();
		this.addSupportAdminService = addSupportAdminService;
		this.addSupportAdminRepository = addSupportAdminRepository;
		this.clientRepository = clientRepository;
		this.supportMirrorService = supportMirrorService;
		this.clientService = clientService;
		this.clientSupportService = clientSupportService;
		this.admincommentService = admincommentService;
		this.adminService = adminService;
		this.userService = userService;
		this.clientUserService = clientUserService;
		this.clientUserRepository = clientUserRepository;
		this.base64ImageList = base64ImageList;
	}

	@GetMapping("/addsupport")
	public String addSupportAdmin(Model model) {
		List<String> activeStoreCodes = addSupportAdminRepository.findActiveStoreCodes();
		List<Client> clients;
		clients = clientRepository.findAll();
		AddSupportAdmin addSupportAdmin = new AddSupportAdmin();
		model.addAttribute("addSupportAdmin",addSupportAdmin);
		model.addAttribute("storeCodes", activeStoreCodes);
		 model.addAttribute("clients", clients);
		return "addsupportadmin";
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
	
	@PostMapping("/createSupportAdmin")
	public String saveSupportAdmin(@ModelAttribute("addSupportAdmin") AddSupportAdmin addSupportAdmin,
	        @RequestParam("storeName") String storeName, @RequestParam("phoneNumber") String storeContact,
	        @RequestParam(value = "files", required = false) List<MultipartFile> files, Principal principal, @RequestParam("Date")String Date) {
	    // Set default values for internalComments, externalComments, and status
	    addSupportAdmin.setInternalComments("No comments");
	    addSupportAdmin.setCommentsToClient("No comments");
	    addSupportAdmin.setCommentsFromClient("No comments");
	    addSupportAdmin.setStatus("New");
	    addSupportAdmin.setStoreContact(storeContact);
	    
	    String username = principal.getName();
	    addSupportAdmin.setUploadedBy(username);
	    
	    String displayName = getDisplayNameByUsername(username);
        addSupportAdmin.setUploadedBy(displayName);
	    
	    // Handle file uploads
	    addSupportAdminService.saveAddSupportAdmin(addSupportAdmin);
	    
	    supportMirrorService.addSupportRecordToBothSides(addSupportAdmin);

	    ClientSupport clientSupport = clientSupportService.getClientSupportById(addSupportAdmin.getId());


	    
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
        
        // After saving the addProductAdmin, set the formatted product request ID based on the generated ID
	    addSupportAdmin.setFormattedSupportNumber(addSupportAdmin.getId());
	    // Save the addProductAdmin again to update the product request ID
	    addSupportAdminService.saveAddSupportAdmin(addSupportAdmin);
        
        // Associate files with the invoice
        addSupportAdmin.setAdminSupportFiles(adminSupportFiles);
        
        addSupportAdminService.saveAddSupportAdmin(addSupportAdmin);
        
        String notificationMessage = "Admin has created a new support request:";
	    String supportRequestId = addSupportAdmin.getSupportRequestId();
	    String storeCode = addSupportAdmin.getStoreCode();
	    String notification = notificationMessage + " " + supportRequestId + " - " + storeCode;
	    
	    List<String> usernamesBasedOnStorecode = addSupportAdminService.getUsernamesBasedOnStoreCode("ROLE_CLIENT_SUPPORT", storeCode);
	    System.out.println("Usernames based on storecode: " + usernamesBasedOnStorecode);

	    // Get emails based on usernames
	    List<String> emailsBasedOnUsernames = addSupportAdminService.getEmailsBasedOnUsernames(usernamesBasedOnStorecode);
	    System.out.println("Emails based on usernames: " + emailsBasedOnUsernames);

	    // Call this in your controller method
	    addSupportAdminService.sendEmailNotification(addSupportAdmin, emailsBasedOnUsernames, files, notification);  
	    // Handle requestStatus as needed

	    return "redirect:/viewsupport";
	}
	
	

	@GetMapping("/inprogress/{id}")
	public String inProgress(@PathVariable Long id,Model model) {
		model.addAttribute("addSupportAdmin", addSupportAdminService.getAddSupportAdminById(id));
		return "supportlistaddmininprogresspop";
	}
	
	@PostMapping("/inProgress/{id}")
	public String saveInprogressComments(@PathVariable Long id,
	                           @ModelAttribute("adminComments") AdminComments adminComments,
	                           @RequestParam("commentText") String commentText,
	                           Model model, Principal principal) {

	    // Retrieve the AddSupportAdmin entity
	    AddSupportAdmin existingAddSupportAdmin = addSupportAdminService.getAddSupportAdminById(id);
	    
	    ClientSupport existingClientSupport = clientSupportService.getClientSupportById(id);
	    
		   existingAddSupportAdmin.setStatus("In-Progress");
		   existingClientSupport.setStatus("In-Progress");
		   addSupportAdminService.saveAddSupportAdmin(existingAddSupportAdmin);
		   
		   String notificationMessage = "Admin has changed the status to In-progress for support request ID:";
		    String supportRequestId = existingAddSupportAdmin.getSupportRequestId();
		    String storeCode = existingAddSupportAdmin.getStoreCode();
		    String notification = notificationMessage + " " + supportRequestId + " - " + storeCode;
		    String username = principal.getName();
		    String displayName = getDisplayNameByUsername(username);

	    // Create a new AdminComments
	    AdminComments comment = new AdminComments();
	    comment.setAddSupportAdmin(existingAddSupportAdmin);
	    comment.setClientSupport(existingClientSupport);
	    comment.setAddedBy(displayName + "(Admin)");
	    comment.setNotification(notification);
	    comment.setStoreCode(existingClientSupport.getStoreCode());
	    comment.setAdminComments(commentText);
	    comment.setRequestStatus(true);
	    comment.setReason("Opted to change status to In-Progress");
	    comment.setDateAdded(new Date());
	    comment.setTimeAdded(new Date());

	    // Set the supportRequestId indirectly
	    comment.setSupportRequestId(existingAddSupportAdmin.getSupportRequestId());

	    // Save the comment
	    admincommentService.saveComment(comment);
	    
	    List<String> usernamesBasedOnStorecode = addSupportAdminService.getUsernamesBasedOnStoreCode("ROLE_CLIENT_SUPPORT", storeCode);
	    System.out.println("Usernames based on storecode: " + usernamesBasedOnStorecode);

	    // Get emails based on usernames
	    List<String> emailsBasedOnUsernames = addSupportAdminService.getEmailsBasedOnUsernames(usernamesBasedOnStorecode);
	    System.out.println("Emails based on usernames: " + emailsBasedOnUsernames);

	    // Call this in your controller method
	    addSupportAdminService.sendEmailNotifications(emailsBasedOnUsernames, notification, commentText);  
	    // Handle requestStatus as needed

	    // Redirect to the support view page
	    return "redirect:/viewsupport";
	}
	
	@GetMapping("/closed/{id}")
	public String closed(@PathVariable Long id,Model model) {
		model.addAttribute("addSupportAdmin", addSupportAdminService.getAddSupportAdminById(id));
		return "closesupportlistpop";
	}
	
	@PostMapping("/closepop/{id}")
	public String saveCloseComments(@PathVariable Long id,
	                           @ModelAttribute("adminComments") AdminComments adminComments,
	                           @RequestParam("commentText") String commentText,
	                           Model model, Principal principal) {

	    // Retrieve the AddSupportAdmin entity
	    AddSupportAdmin existingAddSupportAdmin = addSupportAdminService.getAddSupportAdminById(id);
	    
	    ClientSupport existingClientSupport = clientSupportService.getClientSupportById(id);
	    
	    String notificationMessage = "Admin has closed the support request ID:";
	    String supportRequestId = existingAddSupportAdmin.getSupportRequestId();
	    String storeCode = existingAddSupportAdmin.getStoreCode();
	    String notification = notificationMessage + " " + supportRequestId + " - " + storeCode;
	    
	    String userName = principal.getName();
	    String displayName = getDisplayNameByUsername(userName);
		  
		   existingAddSupportAdmin.setStatus("Closed");
		   existingClientSupport.setStatus("Closed");
		   addSupportAdminService.saveAddSupportAdmin(existingAddSupportAdmin);


	    // Create a new AdminComments
	    AdminComments comment = new AdminComments();
	    comment.setAddSupportAdmin(existingAddSupportAdmin);
	    comment.setClientSupport(existingClientSupport);
	    comment.setAddedBy(displayName + "(Admin)");
	    comment.setNotification(notification);
	    comment.setStoreCode(existingClientSupport.getStoreCode());
	    comment.setAdminComments(commentText);
	    comment.setRequestStatus(true);
	    comment.setReason("opted to close the support request");
	    comment.setDateAdded(new Date());
	    comment.setTimeAdded(new Date());

	    // Set the supportRequestId indirectly
	    comment.setSupportRequestId(existingAddSupportAdmin.getSupportRequestId());
	    
	    // Save the comment
	    admincommentService.saveComment(comment);
	    
	  /*  List<User> usersWithAccountsRole = userService.getUsersByName("ROLE_CLIENT_SUPPORT");
	     List<String> usernames = usersWithAccountsRole.stream().map(User::getUserName).collect(Collectors.toList());
	     List<String> usernamesBasedOnStorecode = clientUserService.getUserNamesByStoreCode(usernames, existingClientSupport.getStoreCode());
	     System.out.println("Usernames based on storecode: " + usernamesBasedOnStorecode);
		    
	     List<String> emailsBasedOnUsernames = getEmailsByUsernames(usernamesBasedOnStorecode);
	     System.out.println("Emails based on usernames: " + emailsBasedOnUsernames);
	    
	 // Call this in your controller method
	    addSupportAdminService.sendEmailNotification(emailsBasedOnUsernames, notification);*/

	    // Handle requestStatus as needed
	    
	    List<String> usernamesBasedOnStorecode = addSupportAdminService.getUsernamesBasedOnStoreCode("ROLE_CLIENT_SUPPORT", storeCode);
	    System.out.println("Usernames based on storecode: " + usernamesBasedOnStorecode);

	    // Get emails based on usernames
	    List<String> emailsBasedOnUsernames = addSupportAdminService.getEmailsBasedOnUsernames(usernamesBasedOnStorecode);
	    System.out.println("Emails based on usernames: " + emailsBasedOnUsernames);

	    // Call this in your controller method
	    addSupportAdminService.sendEmailNotifications(emailsBasedOnUsernames, notification, commentText);  

	    // Redirect to the support view page
	    return "redirect:/viewsupport";
	}

	
	@GetMapping("/reopen/{id}")
	public String reopen(@PathVariable Long id,Model model) {
		model.addAttribute("addSupportAdmin", addSupportAdminService.getAddSupportAdminById(id));
		return "reopensupportpop";
	}
	
	@PostMapping("/reOpen/{id}")
	public String saveReopenComments(@PathVariable Long id,
	                           @ModelAttribute("adminComments") AdminComments adminComments,
	                           @RequestParam("commentText") String commentText,
	                           Model model, Principal principal) {

	    // Retrieve the AddSupportAdmin entity
	    AddSupportAdmin existingAddSupportAdmin = addSupportAdminService.getAddSupportAdminById(id);
	    
	    ClientSupport existingClientSupport = clientSupportService.getClientSupportById(id);
	    
		   existingAddSupportAdmin.setStatus("Re-Open");
		   existingClientSupport.setStatus("Re-Open");
		   addSupportAdminService.saveAddSupportAdmin(existingAddSupportAdmin);
		   
		   String notificationMessage = "Admin has re-opened the support request ID:";
		    String supportRequestId = existingAddSupportAdmin.getSupportRequestId();
		    String storeCode = existingAddSupportAdmin.getStoreCode();
		    String notification = notificationMessage + " " + supportRequestId + " - " + storeCode;
		    
		    String userName = principal.getName();
		    String displayName = getDisplayNameByUsername(userName);
		    
	    // Create a new AdminComments
	    AdminComments comment = new AdminComments();
	    comment.setAddSupportAdmin(existingAddSupportAdmin);
	    comment.setClientSupport(existingClientSupport);
	    comment.setAddedBy(displayName +"(Admin)");
	    comment.setNotification(notification);
	    comment.setStoreCode(existingClientSupport.getStoreCode());
	    comment.setAdminComments(commentText);
	    comment.setRequestStatus(true);
	    comment.setReason("Opted to Re-open the support request");
	    comment.setDateAdded(new Date());
	    comment.setTimeAdded(new Date());

	    // Set the supportRequestId indirectly
	    comment.setSupportRequestId(existingAddSupportAdmin.getSupportRequestId());

	    // Save the comment
	    admincommentService.saveComment(comment);
	    
	    List<String> usernamesBasedOnStorecode = addSupportAdminService.getUsernamesBasedOnStoreCode("ROLE_CLIENT_SUPPORT", storeCode);
	    System.out.println("Usernames based on storecode: " + usernamesBasedOnStorecode);

	    // Get emails based on usernames
	    List<String> emailsBasedOnUsernames = addSupportAdminService.getEmailsBasedOnUsernames(usernamesBasedOnStorecode);
	    System.out.println("Emails based on usernames: " + emailsBasedOnUsernames);

	    // Call this in your controller method
	    addSupportAdminService.sendEmailNotifications(emailsBasedOnUsernames, notification, commentText);  

	    // Handle requestStatus as needed

	    // Redirect to the support view page
	    return "redirect:/viewsupport";
	}
	
	
	@GetMapping("/supportlistaddmincapurepop/{id}")
	public String viewSupportCommentsPop(@PathVariable Long id,Model model) {
		model.addAttribute("addSupportAdmin", addSupportAdminService.getAddSupportAdminById(id));
		return "supportlistaddmincapurepop";
	}
	
	@PostMapping("/saveComments/{id}")
	public String saveComments(@PathVariable Long id,
	                           @ModelAttribute("adminComments") AdminComments adminComments,
	                           @RequestParam("commentText") String commentText,
	                           @RequestParam(value = "requestStatus", defaultValue = "false") boolean requestStatus,
	                           Model model, Principal principal) {

	    // Retrieve the AddSupportAdmin entity
	    AddSupportAdmin existingAddSupportAdmin = addSupportAdminService.getAddSupportAdminById(id);
	    
	    ClientSupport existingClientSupport = clientSupportService.getClientSupportById(id);
	    
	    String notificationMessage = "Admin has made some comments on support request ID:";
	    String supportRequestId = existingAddSupportAdmin.getSupportRequestId();
	    String storeCode = existingAddSupportAdmin.getStoreCode();
	    String notification = notificationMessage + " " + supportRequestId + " - " + storeCode;
		
	    String userName = principal.getName();
	    String displayName = getDisplayNameByUsername(userName);

	    // Create a new AdminComments
	    AdminComments comment = new AdminComments();
	    comment.setAddSupportAdmin(existingAddSupportAdmin);
	    comment.setClientSupport(existingClientSupport);
	    comment.setAddedBy(displayName +"(Admin)");
	    comment.setNotification(notification);
	    comment.setStoreCode(existingClientSupport.getStoreCode());
	    comment.setAdminComments(commentText);
	    comment.setRequestStatus(requestStatus); // Set "yes" if true, "no" otherwise
	    comment.setReason("Regular Comment");
	    comment.setDateAdded(new Date());
	    comment.setTimeAdded(new Date());

	    // Set the supportRequestId indirectly
	    comment.setSupportRequestId(existingAddSupportAdmin.getSupportRequestId());

	    // Save the comment
	    admincommentService.saveComment(comment);
	    
	    List<String> usernamesBasedOnStorecode = addSupportAdminService.getUsernamesBasedOnStoreCode("ROLE_CLIENT_SUPPORT", storeCode);
	    System.out.println("Usernames based on storecode: " + usernamesBasedOnStorecode);

	    // Get emails based on usernames
	    List<String> emailsBasedOnUsernames = addSupportAdminService.getEmailsBasedOnUsernames(usernamesBasedOnStorecode);
	    System.out.println("Emails based on usernames: " + emailsBasedOnUsernames);

	    // Call this in your controller method
	    addSupportAdminService.sendEmailNotifications(emailsBasedOnUsernames, notification, commentText);  


	    // Redirect to the support view page
	    return "redirect:/viewsupport";
	}
	
	// Method to get emails by usernames using ClientUser entity
	public List<String> getEmailsByUsernames(List<String> usernames) {
	    return usernames.stream()
	            .map(username -> {
	                Optional<ClientUser> clientUserOptional = clientUserRepository.findByUserName(username);
	                return clientUserOptional.map(ClientUser::getEmail).orElse(null); // Extract email from ClientUser entity
	            })
	            .filter(Objects::nonNull) // Filter out null emails
	            .collect(Collectors.toList());
	}



	
	@GetMapping("/viewsupportadmindts/comment/{id}")
	public String viewSupportComment(@PathVariable Long id, Model model) {
		model.addAttribute("addSupportAdmin", addSupportAdminService.getAddSupportAdminById(id));
		return "supportlistpop";
	}
	
	/*@GetMapping("/supportDetails/{id}")
	public String viewSupportDetails(@PathVariable Long id, Model model) {
	    // Get AddSupportAdmin entity by ID
	    AddSupportAdmin addSupportAdmin = addSupportAdminService.getAddSupportAdminById(id);

	    // Get the list of SupportFiles associated with the AddSupportAdmin
	    List<SupportFiles> supportFilesList = addSupportAdmin.getAdminSupportFiles();

	    // Add the AddSupportAdmin and SupportFiles list to the model
	    model.addAttribute("addSupportAdmin", addSupportAdmin);
	    
	    // Convert SupportFiles data to Base64 and add to the model
	    List<String> base64ImageList = new ArrayList<>();
	    for (SupportFiles supportFile : supportFilesList) {
	        if (supportFile.getFileData() != null) {
	            String base64Image = Base64.getEncoder().encodeToString(supportFile.getFileData());
	            base64ImageList.add(base64Image);
	        } else {
	            base64ImageList.add(""); // or null, depending on your preference
	        }
	    }
	    model.addAttribute("base64ImageList", base64ImageList);

	    return "viewsupportadmindts";
	}*/
	
	 private List<String> base64ImageList; // Declare it as a class field

	    @GetMapping("/supportDetails/{id}")
	    public String viewSupportDetails(@PathVariable Long id, Model model) {
	        // Your existing code to populate addSupportAdmin and supportFilesList
	    	 AddSupportAdmin addSupportAdmin = addSupportAdminService.getAddSupportAdminById(id);
	 	    
	 	   List<AdminComments> adminCommentsList = addSupportAdmin.getComments();

	 	    // Add the AddSupportAdmin and SupportFiles list to the model
	 	    model.addAttribute("addSupportAdmin", addSupportAdmin);
	        model.addAttribute("adminCommentsList", adminCommentsList);

	        return "supportadmin";
	    }
	    
	    
	   /* @GetMapping("/saveSupportImages/{id}")
	    public String saveSupportImages(@PathVariable Long id, Model model) {
	        // Your existing code to populate addSupportAdmin and supportFilesList
	    	 AddSupportAdmin addSupportAdmin = addSupportAdminService.getAddSupportAdminById(id);

	 	    // Get the list of SupportFiles associated with the AddSupportAdmin
	 	    List<SupportFiles> supportFilesList = addSupportAdmin.getAdminSupportFiles();
	 	    

	 	    // Add the AddSupportAdmin and SupportFiles list to the model
	 	    model.addAttribute("addSupportAdmin", addSupportAdmin);

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

	        model.addAttribute("addSupportAdmin", addSupportAdmin);
	        model.addAttribute("base64ImageList", base64ImageList);

	        return "viewfiles-supportAdmin";
	    }
	    */
	    
	    @GetMapping("/saveSupportImages/{id}")
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

	        return "viewfiles-supportAdmin";
	    }


	    
	    @PostMapping("/uploadImagesAdminSupport/{id}")
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

	       
	        return "redirect:/saveSupportImages/{id}";
	    }
	    
	    
	    @Autowired
	    private SupportFilesRepository supportFilesRepository;
	    
	    @GetMapping("/download/support/{id}")
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

	    
	    
	
	
	 @GetMapping("/deleterequest/{id}")
	    public String deleteSupportAdmin(@PathVariable Long id) {
	        addSupportAdminService.deleteAddSupportAdminById(id);
	        clientSupportService.deleteClientSuppportById(id);
	        return "redirect:/viewsupport";
	    }
	 

}
