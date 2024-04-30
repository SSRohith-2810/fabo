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
import com.project.Fabo.entity.AdminProductComments;
import com.project.Fabo.entity.Client;
import com.project.Fabo.entity.ClientProduct;
import com.project.Fabo.entity.Invoice;
import com.project.Fabo.entity.InvoiceFile;
import com.project.Fabo.entity.ProductFiles;
import com.project.Fabo.entity.SupportFiles;
import com.project.Fabo.repository.AddProductAdminRepository;
import com.project.Fabo.repository.ClientRepository;
import com.project.Fabo.repository.ProductFilesRepository;
import com.project.Fabo.service.AddProductAdminService;
import com.project.Fabo.service.AddSupportAdminService;
import com.project.Fabo.service.AdminProductCommentService;
import com.project.Fabo.service.AdminService;
import com.project.Fabo.service.ClientProductService;
import com.project.Fabo.service.ClientService;
import com.project.Fabo.service.ProductMirrorService;



@Controller
public class AdminProductController {
	
	private AddProductAdminService addProductAdminService;

	private AddProductAdminRepository addProductAdminRepository;
	
	private ClientRepository clientRepository;
	
	private ProductMirrorService productMirrorService;
	
	private ClientService clientService;
	
	private ClientProductService clientProductService;
	
	private AdminProductCommentService adminProductCommentService;
	
	private AddSupportAdminService addSupportAdminService;
	
	private AdminService adminService;

	public AdminProductController(AddProductAdminService addProductAdminService,
			AddProductAdminRepository addProductAdminRepository, ClientRepository clientRepository,
			ProductMirrorService productMirrorService, ClientService clientService,
			ClientProductService clientProductService, AdminProductCommentService adminProductCommentService,
			AddSupportAdminService addSupportAdminService, AdminService adminService, List<String> base64ImageList) {
		super();
		this.addProductAdminService = addProductAdminService;
		this.addProductAdminRepository = addProductAdminRepository;
		this.clientRepository = clientRepository;
		this.productMirrorService = productMirrorService;
		this.clientService = clientService;
		this.clientProductService = clientProductService;
		this.adminProductCommentService = adminProductCommentService;
		this.addSupportAdminService = addSupportAdminService;
		this.adminService = adminService;
		this.base64ImageList = base64ImageList;
	}

	@GetMapping("/addproduct")
	public String addProductAdmin(Model model) {
		List<String> activeStoreCodes = addProductAdminRepository.findActiveStoreCodes();
		List<Client> clients;
		clients = clientRepository.findAll();
		AddProductAdmin addProductAdmin = new AddProductAdmin();
		model.addAttribute("addProductAdmin",addProductAdmin);
		model.addAttribute("storeCodes", activeStoreCodes);
		model.addAttribute("clients", clients);
		return "addproductadmin";
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
	
	@PostMapping("/createProductAdmin")
	public String saveProductAdmin(@ModelAttribute("addProductAdmin") AddProductAdmin addProductAdmin,
			@RequestParam("shippingAddress") String shippingAddress,
	        @RequestParam("storeName") String storeName,@RequestParam("phoneNumber") String storeContact,
	        @RequestParam(value = "files", required = false) List<MultipartFile> files, Principal principal, @RequestParam("Date")String Date) {
		
	    addProductAdmin.setStatus("New");
	    addProductAdmin.setStoreContact(storeContact);
	    
	    String username = principal.getName();
	    String displayName = getDisplayNameByUsername(username);
	    addProductAdmin.setUploadedBy(displayName);
	    
	    System.out.println("Number of files received: " + (files != null ? files.size() : 0));
	    
	    // Handle file uploads
	    addProductAdminService.saveAddProductAdmin(addProductAdmin);
	    
	    productMirrorService.addProductRecordToBothSides(addProductAdmin);

	    ClientProduct clientProduct = clientProductService.getClientProductById(addProductAdmin.getId());


	    
        List<ProductFiles> adminProductFiles = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                try {
                	ProductFiles adminProductFile = new ProductFiles();
                	adminProductFile.setFileData(file.getBytes());
                	adminProductFile.setAddProductAdmin(addProductAdmin);
                	adminProductFile.setClientProduct(clientProduct);
                	adminProductFiles.add(adminProductFile);
                } catch (IOException e) {
                    // Handle the IOException as needed
                    e.printStackTrace(); // Log the exception or throw a custom exception
                }
            }
        }

        // Associate files with the invoice
        addProductAdmin.setAdminProductFiles(adminProductFiles);
        
        addProductAdminService.saveAddProductAdmin(addProductAdmin);
        
        String notificationMessage = "Admin has created a new product request:";
	    String productRequestId = addProductAdmin.getProductRequestId();
	    String storeCode = addProductAdmin.getStoreCode();
	    String notification = notificationMessage + " " + productRequestId + " - " + storeCode;
	    
	    List<String> usernamesBasedOnStorecode = addSupportAdminService.getUsernamesBasedOnStoreCode("ROLE_CLIENT_SUPPORT", storeCode);
	    System.out.println("Usernames based on storecode: " + usernamesBasedOnStorecode);
	    
	    // After saving the addProductAdmin, set the formatted product request ID based on the generated ID
	    addProductAdmin.setFormattedProductNumber(addProductAdmin.getId());
	    // Save the addProductAdmin again to update the product request ID
	    addProductAdminService.saveAddProductAdmin(addProductAdmin);
	    
	    // Get emails based on usernames
	    List<String> emailsBasedOnUsernames = addSupportAdminService.getEmailsBasedOnUsernames(usernamesBasedOnStorecode);
	    System.out.println("Emails based on usernames: " + emailsBasedOnUsernames);

	    // Call this in your controller method
	    addProductAdminService.sendEmailNotification(addProductAdmin, emailsBasedOnUsernames, files);  

	    

	    return "redirect:/viewproduct";
	}
	
	 @GetMapping("/productviewpop/{id}")
	    public String viewClientInfo(@PathVariable Long id, Model model) {
	        AddProductAdmin addProductAdmin = addProductAdminService.getAddProductAdminById(id);
 
	        if (addProductAdmin != null) {
	            // Fetch associated Client information based on the storeCode in the Invoice
	            Client client = clientService.getClientByStoreCode(addProductAdmin.getStoreCode());

	            // Add the Invoice and Client information to the model
	            model.addAttribute("addProductAdmin", addProductAdmin);
	            model.addAttribute("client", client);
	        }

	        return "productviewpop";
	    }

	@GetMapping("/viewproduct")
	public String filterProductList(
	        @RequestParam(value = "invoicedropdown", required = false) String invoicedropdown,
	        @RequestParam(value = "statusDropdown", required = false) String statusDropdown,
	        @RequestParam(value = "searchTerm", required = false) String searchTerm,
	        Model model
	) {
	    List<AddProductAdmin> addProductAdmins;

	    if (searchTerm != null && !searchTerm.isEmpty()) {
	        // Filter by search term across multiple fields
	        addProductAdmins = addProductAdminRepository.findBySearchTerm(searchTerm);
	        model.addAttribute("searchTerm", searchTerm);
	    } else if (invoicedropdown != null && !invoicedropdown.isEmpty()) {
	        // Filter by invoice dropdown
	        if ("All".equalsIgnoreCase(invoicedropdown)) {
	            addProductAdmins = addProductAdminRepository.findByActiveStatusTrue();
	        } else {
	            addProductAdmins = addProductAdminRepository.findByProductRequestTypeContaining(invoicedropdown);
	        }
	        model.addAttribute("selectedType", invoicedropdown);
	    } else if (statusDropdown != null && !statusDropdown.isEmpty()) {
	        // Filter by status dropdown
	        if ("All".equalsIgnoreCase(statusDropdown)) {
	            addProductAdmins = addProductAdminRepository.findByActiveStatusTrue();
	        } else {
	            addProductAdmins = addProductAdminRepository.findByStatus(statusDropdown);
	        }
	        model.addAttribute("selected", statusDropdown);
	    } else {
	        // No filters applied, return all records
	        addProductAdmins = addProductAdminRepository.findByActiveStatusTrue();
	    }  
	    List<String> storeCodes = addProductAdminRepository.findDistinctStoreCodes();
	    model.addAttribute("storeCodes", storeCodes);

	    model.addAttribute("addProductAdmins", addProductAdmins);

	    return "productlist";
	}

	@GetMapping("/productmoveinprogresspop/{id}")
	public String inProgress(@PathVariable Long id,Model model) {
		model.addAttribute("addProductAdmin", addProductAdminService.getAddProductAdminById(id));
		
		return "productmoveinprogresspop";
	}
	
	@PostMapping("/inProgressproduct/{id}")
	public String saveInprogressComments(@PathVariable Long id,
	                           @ModelAttribute("adminProductComments") AdminProductComments adminProductComments,
	                           @RequestParam("commentText") String commentText,
	                           Model model, Principal principal) {

	    // Retrieve the AddProductAdmin entity
	    AddProductAdmin existingAddProductAdmin = addProductAdminService.getAddProductAdminById(id);
	    
	    ClientProduct existingClientProduct = clientProductService.getClientProductById(id);
	    existingAddProductAdmin.setStatus("In-Progress");
	    existingClientProduct.setStatus("In-Progress");
		   addProductAdminService.saveAddProductAdmin(existingAddProductAdmin);
		   
		    String notificationMessage = "Admin has changed the status to In-progress for product request ID:";
		    String productRequestId = existingAddProductAdmin.getProductRequestId();
		    String storeCode = existingAddProductAdmin.getStoreCode();
		    String notification = notificationMessage + " " + productRequestId + " - " + storeCode;
		    System.out.println(storeCode);
		    String username = principal.getName();
		    String displayName = getDisplayNameByUsername(username);

	    // Create a new AdminProductComments
	    AdminProductComments comment = new AdminProductComments();
	    comment.setAddProductAdmin(existingAddProductAdmin);
	    comment.setClientProduct(existingClientProduct);
	    comment.setAdminProductComments(commentText);
	    comment.setNotification(notification);
	    comment.setAddedBy(displayName + "(Admin)");
	    comment.setRequestStatus(true);
	    comment.setReason("Admin opted to change status to In-progress");
	    comment.setDateAdded(new Date());
	    comment.setTimeAdded(new Date());

	    // Set the productRequestId indirectly
	    comment.setProductRequestId(existingAddProductAdmin.getProductRequestId());

	    // Save the comment
	    adminProductCommentService.saveComment(comment);

	    // Handle requestStatus as needed
	    
	    List<String> usernamesBasedOnStorecode = addProductAdminService.getUsernamesBasedOnStoreCode("ROLE_CLIENT_PRODUCTS", storeCode);
	    System.out.println("Usernames based on storecode: " + usernamesBasedOnStorecode);

	    // Get emails based on usernames
	    List<String> emailsBasedOnUsernames = addProductAdminService.getEmailsBasedOnUsernames(usernamesBasedOnStorecode);
	    System.out.println("Emails based on usernames: " + emailsBasedOnUsernames);

	    // Call this in your controller method
	    addProductAdminService.sendEmailNotifications(emailsBasedOnUsernames, notification, commentText);  
	    // Handle requestStatus as needed

	    // Redirect to the Product view page
	    return "redirect:/viewproduct";
	}
	
	@GetMapping("/closeproductlistpop/{id}")
	public String closed(@PathVariable Long id,Model model) {
		model.addAttribute("addProductAdmin", addProductAdminService.getAddProductAdminById(id));
		  
		return "closeproductlistpop";
	}
	
	@PostMapping("/closepopproduct/{id}")
	public String saveCloseComments(@PathVariable Long id,
	                           @ModelAttribute("adminProductComments") AdminProductComments adminProductComments,
	                           @RequestParam("commentText") String commentText,
	                           Model model, Principal principal) {

	    // Retrieve the AddProductAdmin entity
	    AddProductAdmin existingAddProductAdmin = addProductAdminService.getAddProductAdminById(id);
	    
	    ClientProduct existingClientProduct = clientProductService.getClientProductById(id);
	    existingAddProductAdmin.setStatus("Closed");
	    existingClientProduct.setStatus("Closed");
		   addProductAdminService.saveAddProductAdmin(existingAddProductAdmin);
		   
		   String notificationMessage = "Admin has closed the product request ID:";
		    String productRequestId = existingAddProductAdmin.getProductRequestId();
		    String storeCode = existingAddProductAdmin.getStoreCode();
		    String notification = notificationMessage + " " + productRequestId + " - " + storeCode;
		    String username = principal.getName();
		    String displayName = getDisplayNameByUsername(username);

	    // Create a new AdminProductComments
	    AdminProductComments comment = new AdminProductComments();
	    comment.setAddProductAdmin(existingAddProductAdmin);
	    comment.setClientProduct(existingClientProduct);
	    comment.setAdminProductComments(commentText);
	    comment.setAddedBy(displayName + "(Admin)");
	    comment.setNotification(notification);
	    comment.setRequestStatus(true);
	    comment.setReason("Admin opted to close the product request");
	    comment.setDateAdded(new Date());
	    comment.setTimeAdded(new Date());

	    // Set the productRequestId indirectly
	    comment.setProductRequestId(existingAddProductAdmin.getProductRequestId());

	    // Save the comment
	    adminProductCommentService.saveComment(comment);

	    // Handle requestStatus as needed
	    List<String> usernamesBasedOnStorecode = addSupportAdminService.getUsernamesBasedOnStoreCode("ROLE_CLIENT_PRODUCTS", storeCode);
	    System.out.println("Usernames based on storecode: " + usernamesBasedOnStorecode);

	    // Get emails based on usernames
	    List<String> emailsBasedOnUsernames = addSupportAdminService.getEmailsBasedOnUsernames(usernamesBasedOnStorecode);
	    System.out.println("Emails based on usernames: " + emailsBasedOnUsernames);

	    // Call this in your controller method
	    addProductAdminService.sendEmailNotifications(emailsBasedOnUsernames, notification, commentText);  
	    // Handle requestStatus as needed


	    // Redirect to the product view page
	    return "redirect:/viewproduct";
	}

	
	@GetMapping("/reopenproductpop/{id}")
	public String reopen(@PathVariable Long id,Model model) {
		model.addAttribute("addProductAdmin", addProductAdminService.getAddProductAdminById(id));
		
		return "reopenproductpop";
	}
	
	@PostMapping("/reOpenproducts/{id}")
	public String saveReopenComments(@PathVariable Long id,
	                           @ModelAttribute("adminProductComments") AdminProductComments adminProductComments,
	                           @RequestParam("commentText") String commentText,
	                           Model model, Principal principal) {

	    // Retrieve the AddProductAdmin entity
	    AddProductAdmin existingAddProductAdmin = addProductAdminService.getAddProductAdminById(id);
	    
	    ClientProduct existingClientProduct = clientProductService.getClientProductById(id);
	    existingAddProductAdmin.setStatus("Re-Open");
	    existingClientProduct.setStatus("Re-Open");
		   addProductAdminService.saveAddProductAdmin(existingAddProductAdmin);
		   
		   String notificationMessage = "Admin has re-opened the product request ID:";
		    String productRequestId = existingAddProductAdmin.getProductRequestId();
		    String storeCode = existingAddProductAdmin.getStoreCode();
		    String notification = notificationMessage + " " + productRequestId + " - " + storeCode;
		    String username = principal.getName();
		    String displayName = getDisplayNameByUsername(username);

	    // Create a new AdminProductComments
	    AdminProductComments comment = new AdminProductComments();
	    comment.setAddProductAdmin(existingAddProductAdmin);
	    comment.setClientProduct(existingClientProduct);
	    comment.setAdminProductComments(commentText);
	    comment.setAddedBy(displayName + "(Admin)");
	    comment.setNotification(notification);
	    comment.setRequestStatus(true);
	    comment.setReason("Admin opted to Re-open the product request");
	    comment.setDateAdded(new Date());
	    comment.setTimeAdded(new Date());

	    // Set the productRequestId indirectly
	    comment.setProductRequestId(existingAddProductAdmin.getProductRequestId());

	    // Save the comment
	    adminProductCommentService.saveComment(comment);

	    // Handle requestStatus as needed
	    List<String> usernamesBasedOnStorecode = addSupportAdminService.getUsernamesBasedOnStoreCode("ROLE_CLIENT_PRODUCTS", storeCode);
	    System.out.println("Usernames based on storecode: " + usernamesBasedOnStorecode);

	    // Get emails based on usernames
	    List<String> emailsBasedOnUsernames = addSupportAdminService.getEmailsBasedOnUsernames(usernamesBasedOnStorecode);
	    System.out.println("Emails based on usernames: " + emailsBasedOnUsernames);

	    // Call this in your controller method
	    addProductAdminService.sendEmailNotifications(emailsBasedOnUsernames, notification, commentText);  
	    // Handle requestStatus as needed


	    // Redirect to the product view page
	    return "redirect:/viewproduct";
	}
	
	
	@GetMapping("/productcaputurepop/{id}")
	public String viewProductCommentsPop(@PathVariable Long id,Model model) {
		model.addAttribute("addProductAdmin", addProductAdminService.getAddProductAdminById(id));
		return "productcaputurepop";
	}
	
	@PostMapping("/saveCommentsproduct/{id}")
	public String saveComments(@PathVariable Long id,
	                           @ModelAttribute("adminProductComments") AdminProductComments adminProductComments,
	                           @RequestParam("commentText") String commentText,
	                           @RequestParam(value = "requestStatus", defaultValue = "false") boolean requestStatus,
	                           Model model, Principal principal) {

	    // Retrieve the AddProductAdmin entity
	    AddProductAdmin existingAddProductAdmin = addProductAdminService.getAddProductAdminById(id);
	    
	    ClientProduct existingClientProduct = clientProductService.getClientProductById(id);
	    
	    
		   String notificationMessage = "Admin has made some comments on product request ID:";
		    String productRequestId = existingAddProductAdmin.getProductRequestId();
		    String storeCode = existingAddProductAdmin.getStoreCode();
		    String notification = notificationMessage + " " + productRequestId + " - " + storeCode;
		    String username = principal.getName();
		    String displayName = getDisplayNameByUsername(username);

	    // Create a new AdminProductComments
	    AdminProductComments comment = new AdminProductComments();
	    comment.setAddProductAdmin(existingAddProductAdmin);
	    comment.setClientProduct(existingClientProduct);
	    comment.setAdminProductComments(commentText);
	    comment.setAddedBy(displayName + "(Admin)");
	    comment.setNotification(notification);
	    comment.setRequestStatus(requestStatus);
	    comment.setReason("Regular Comment");
	    comment.setDateAdded(new Date());
	    comment.setTimeAdded(new Date());

	    // Set the productRequestId indirectly
	    comment.setProductRequestId(existingAddProductAdmin.getProductRequestId());

	    // Save the comment
	    adminProductCommentService.saveComment(comment);

	    // Handle requestStatus as needed
	    List<String> usernamesBasedOnStorecode = addSupportAdminService.getUsernamesBasedOnStoreCode("ROLE_CLIENT_PRODUCTS", storeCode);
	    System.out.println("Usernames based on storecode: " + usernamesBasedOnStorecode);

	    // Get emails based on usernames
	    List<String> emailsBasedOnUsernames = addSupportAdminService.getEmailsBasedOnUsernames(usernamesBasedOnStorecode);
	    System.out.println("Emails based on usernames: " + emailsBasedOnUsernames);

	    // Call this in your controller method
	    addProductAdminService.sendEmailNotifications(emailsBasedOnUsernames, notification, commentText);  
	    // Handle requestStatus as needed


	    // Redirect to the product view page
	    return "redirect:/viewproduct";
	}

	 private List<String> base64ImageList;
	 
	@GetMapping("/productsviewpop/{id}")
	public String viewProductComment(@PathVariable Long id, Model model) {
		 AddProductAdmin addProductAdmin = addProductAdminService.getAddProductAdminById(id);
	 	    
	 	   List<AdminProductComments> adminProductCommentsList = addProductAdmin.getComments();

	 	    // Add the AddSupportAdmin and SupportFiles list to the model
	 	    model.addAttribute("addProductAdmin", addProductAdmin);
	        model.addAttribute("adminProductCommentsList", adminProductCommentsList);
		return "productadmin";
	}
	
	 @GetMapping("/saveProductImages/{id}")
	    public String saveProductImages(@PathVariable Long id, Model model) {
	        // Retrieve AddSupportAdmin by ID
	        AddProductAdmin addProductAdmin = addProductAdminService.getAddProductAdminById(id);

	        // Get the list of SupportFiles associated with the AddSupportAdmin
	        List<ProductFiles> productFilesList = addProductAdmin.getAdminProductFiles();

	        // Add AddSupportAdmin to the model
	        model.addAttribute("addProductAdmin", addProductAdmin);

	        // Prepare a list to hold image details (Base64 data, ID, AddSupportAdmin ID)
	        List<Map<String, Object>> imageDetailsList = new ArrayList<>();

	        // Convert SupportFiles data to Base64 and add along with IDs to the list
	        for (ProductFiles productFile : productFilesList) {
	            if (productFile.getFileData() != null) {
	                Map<String, Object> imageDetails = new HashMap<>();
	                String base64Image = Base64.getEncoder().encodeToString(productFile.getFileData());
	                imageDetails.put("base64Data", base64Image);
	                imageDetails.put("imageId", productFile.getId());
	                imageDetails.put("addProductAdminId", id);
	                imageDetailsList.add(imageDetails);
	            }
	        }

	        // Add the list of image details to the model
	        model.addAttribute("imageDetailsList", imageDetailsList);

	        return "viewfiles-productAdmin";
	    }
	

	 @PostMapping("/uploadImagesAdminProduct/{id}")
	    public String saveImages(@PathVariable Long id,
	                              @RequestParam(value = "files", required = false) List<MultipartFile> files, Model model) {
	    	 AddProductAdmin existingProductAdmin = addProductAdminService.getAddProductAdminById(id);
	    	 model.addAttribute("existingProductAdmin",existingProductAdmin);
	    	 
	    	 ClientProduct existingProductClient = clientProductService.getClientProductById(id);
	    	 model.addAttribute("existingProductClient",existingProductClient);

	    	 List<ProductFiles> adminProductFiles = new ArrayList<>();
	         if (files != null && !files.isEmpty()) {
	             for (MultipartFile file : files) {
	                 try {
	                 	ProductFiles adminProductFile = new ProductFiles();
	                 	adminProductFile.setFileData(file.getBytes());
	                 	adminProductFile.setAddProductAdmin(existingProductAdmin);
	                 	adminProductFile.setClientProduct(existingProductClient);
	                 	adminProductFiles.add(adminProductFile);
	                 } catch (IOException e) {
	                     // Handle the IOException as needed
	                     e.printStackTrace(); // Log the exception or throw a custom exception
	                 }
	             }
	         }
	         existingProductAdmin.setAdminProductFiles(adminProductFiles);

	         addProductAdminService.saveAddProductAdmin(existingProductAdmin);

	       
	        return "redirect:/saveProductImages/{id}";
	    }
	   

}
