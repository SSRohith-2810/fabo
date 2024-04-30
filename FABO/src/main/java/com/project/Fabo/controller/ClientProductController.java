package com.project.Fabo.controller;

import java.io.IOException;
import java.security.Principal;
import java.sql.Timestamp;
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

import com.project.Fabo.entity.AddProductAdmin;
import com.project.Fabo.entity.Admin;
import com.project.Fabo.entity.AdminProductComments;
import com.project.Fabo.entity.Client;
import com.project.Fabo.entity.ClientProduct;
import com.project.Fabo.entity.ClientSupport;
import com.project.Fabo.entity.ClientUser;
import com.project.Fabo.entity.ProductFiles;
import com.project.Fabo.entity.User;
import com.project.Fabo.repository.ClientProductRepository;
import com.project.Fabo.repository.ClientRepository;
import com.project.Fabo.repository.ClientUserRepository;
import com.project.Fabo.repository.ProductFilesRepository;
import com.project.Fabo.service.AddProductAdminService;
import com.project.Fabo.service.AdminProductCommentService;
import com.project.Fabo.service.ClientProductService;
import com.project.Fabo.service.ClientUserService;
import com.project.Fabo.service.ProductMirrorService;
import com.project.Fabo.service.UserService;



@Controller
public class ClientProductController {
	
	private ClientProductService clientProductService;
	
	private ClientRepository clientRepository;
	
	private ClientProductRepository clientProductRepository;
	
	private ProductMirrorService productMirrorService;
	
	private AddProductAdminService addProductAdminService;
	
	private AdminProductCommentService adminProductCommentService;
	
	private UserService userService;
	
	@Autowired
	private ClientUserService clientUserService;
	
	@Autowired
	private ClientUserRepository clientUserRepository;
	
	


	public ClientProductController(ClientProductService clientProductService, ClientRepository clientRepository,
			ClientProductRepository clientProductRepository, ProductMirrorService productMirrorService,
			AddProductAdminService addProductAdminService, AdminProductCommentService adminProductCommentService,
			UserService userService, List<String> base64ImageList) {
		super();
		this.clientProductService = clientProductService;
		this.clientRepository = clientRepository;
		this.clientProductRepository = clientProductRepository;
		this.productMirrorService = productMirrorService;
		this.addProductAdminService = addProductAdminService;
		this.adminProductCommentService = adminProductCommentService;
		this.userService = userService;
		this.base64ImageList = base64ImageList;
	}
	
	@GetMapping("/clientaddproduct")
	public String createSupportByClient(Model model, Principal principal) {
	    String username = principal.getName();
	    
	    // Assuming you have a method to fetch the store code for the logged-in user
	    String userStoreCode = clientUserRepository.findStoreCodeByUserName(username);
	    
	    // Retrieve the client record using the user's store code
	    Client client = clientRepository.findByStoreCode(userStoreCode);
	    
	    ClientProduct clientProduct = new ClientProduct();
		model.addAttribute("clientProduct",clientProduct);
	    
	    // Add the client record to the model to autofill store code, store name, and store contact
	    model.addAttribute("client", client);
	    
	    // Add the current date
	    model.addAttribute("Date", LocalDate.now());
	    
	    return "clientaddproduct";
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
	
	@PostMapping("/clientproductviewproduct")
	public String saveClientProduct(@ModelAttribute("clientProduct") ClientProduct clientProduct,
			@RequestParam("storeName") String storeName, @RequestParam("phoneNumber") String storeContact,@RequestParam("shippingAddress")String shippingAddress,
			 @RequestParam(value = "files", required = false)  List<MultipartFile> files, Principal principal, @RequestParam("Date") String Date) {
		clientProduct.setStatus("New");
		clientProduct.setCommentsToAdmin("No Comments");
		clientProduct.setExternalComments("No Comments");
		clientProduct.setStoreContact(storeContact);
		
		String username = principal.getName();		
		String displayName = getDisplayNameByUsername(username);
	    clientProduct.setUploadedBy(displayName);
		
		clientProductService.saveClientProduct(clientProduct);
		productMirrorService.addProductRecordToBothSide(clientProduct);
		  AddProductAdmin addProductAdmin = addProductAdminService.getAddProductAdminById(clientProduct.getId());
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
	        clientProduct.setAdminProductFiles(adminProductFiles);
		clientProductService.saveClientProduct(clientProduct);
		
		 String notificationMessage = "Client has opened a new product request with ID: ";
		    String productRequestId = clientProduct.getProductRequestId();
		    String storeCode = clientProduct.getStoreCode();
		    String notification = notificationMessage + " " + productRequestId + " - " + storeCode;
		    
		    List<User> usersWithAdminProductRole = userService.getUsersByName("ROLE_ADMIN_PRODUCTS");
		    List<String> usernames = usersWithAdminProductRole.stream().map(User::getUserName).collect(Collectors.toList());
		    System.out.println("Usernames based on ROLE_ADMIN_PRODUCT: " + usernames);

		    List<String> emailsBasedOnUsernamesFromUsers = clientProductService.getEmailsBasedOnUsernamesFromUsers(usernames);
		    System.out.println("Emails based on usernames from Users: " + emailsBasedOnUsernamesFromUsers);

		    List<String> emailsBasedOnUsernamesFromAdmins = clientProductService.getEmailsBasedOnUsernamesFromAdmins(usernames);
		    System.out.println("Emails based on usernames from Admins: " + emailsBasedOnUsernamesFromAdmins);
		    
		    // After saving the addProductAdmin, set the formatted product request ID based on the generated ID
		    clientProduct.setFormattedProductNumber(clientProduct.getId());
		    // Save the addProductAdmin again to update the product request ID
		    clientProductService.saveClientProduct(clientProduct);
		    
		    addProductAdminService.sendEmailNotification(addProductAdmin, emailsBasedOnUsernamesFromAdmins, files);

		
		
		return "redirect:/clientproductview";
	}
	
	/*@GetMapping("/clientproductview")
	public String filtersWithList(
	    @RequestParam(value = "productRequestType", required = false) String productRequesttype,
	    @RequestParam(value = "statusDropdown", required = false) String statusDropdown,
	    @RequestParam(value = "searchTerm", required = false) String searchTerm,
	    Model model
	) {
	    List<ClientProduct> clientProducts;

	    if (searchTerm != null && !searchTerm.isEmpty()) {
	        // Filter by search term across multiple fields
	        clientProducts = clientProductRepository.findBySearchTerm(searchTerm);
	        model.addAttribute("searchTerm", searchTerm);
	    } else if (productRequesttype != null && !productRequesttype.isEmpty()) {
	        // Filter by invoice dropdown
	        if ("All".equalsIgnoreCase(productRequesttype)) {
	            clientProducts = clientProductRepository.findByActiveStatusTrue();
	        } else {
	            clientProducts = clientProductRepository.findByProductRequestTypeContaining(productRequesttype);
	        }
	        model.addAttribute("selectedType", productRequesttype);
	    } else if (statusDropdown != null && !statusDropdown.isEmpty()) {
	        // Filter by status dropdown
	        if ("All".equalsIgnoreCase(statusDropdown)) {
	            clientProducts = clientProductRepository.findByActiveStatusTrue();
	        } else {
	            clientProducts = clientProductRepository.findByStatus(statusDropdown);
	        }
	        model.addAttribute("selected", statusDropdown);
	    } else {
	        // No filters applied, return all records
	        clientProducts = clientProductRepository.findByActiveStatusTrue();
	    }
	    model.addAttribute("clientProducts", clientProducts);
	    return "clientproductlist";
	}*/
	
	@GetMapping("/clientproductview")
	public String filtersWithList(
	    @RequestParam(value = "productRequestType", required = false) String productRequesttype,
	    @RequestParam(value = "statusDropdown", required = false) String statusDropdown,
	    @RequestParam(value = "searchTerm", required = false) String searchTerm,
	    Model model,
	    Principal principal // Add Principal parameter to retrieve logged-in user information
	) {
	    String username = principal.getName(); // Retrieve the currently logged-in user's email

	    // Retrieve the user's store code based on their email
	    String userStoreCode = userService.getUserStoreCodeByUserName(username); // Replace userService with your actual service

	    List<ClientProduct> clientProducts;

	    if (searchTerm != null && !searchTerm.isEmpty()) {
	        // Filter by search term across multiple fields and store code
	        clientProducts = clientProductRepository.findBySearchTermAndStoreCode(searchTerm, userStoreCode);
	        model.addAttribute("searchTerm", searchTerm);
	    } else if (productRequesttype != null && !productRequesttype.isEmpty()) {
	        // Filter by product request type and store code
	        if ("All".equalsIgnoreCase(productRequesttype)) {
	            clientProducts = clientProductRepository.findByActiveStatusAndStoreCode(true, userStoreCode);
	        } else {
	            clientProducts = clientProductRepository.findByProductRequestTypeContainingAndStoreCode(productRequesttype, userStoreCode);
	        }
	        model.addAttribute("selectedType", productRequesttype);
	    } else if (statusDropdown != null && !statusDropdown.isEmpty()) {
	        // Filter by status dropdown and store code
	        if ("All".equalsIgnoreCase(statusDropdown)) {
	            clientProducts = clientProductRepository.findByActiveStatusAndStoreCode(true, userStoreCode);
	        } else {
	            clientProducts = clientProductRepository.findByStatusAndStoreCode(statusDropdown, userStoreCode);
	        }
	        model.addAttribute("selected", statusDropdown);
	    } else {
	        // No filters applied, return all records for the user's store code
	        clientProducts = clientProductRepository.findByActiveStatusAndStoreCode(true, userStoreCode);
	    }
	    model.addAttribute("clientProducts", clientProducts);
	    return "clientproductlist";
	}


	@GetMapping("/viewproductclientproduct/{id}")
	public String viewProductdetails(@PathVariable Long id, Model model) {
		model.addAttribute("clientProduct", clientProductService.getClientProductById(id));
		return "clientproductcapturecompop";
	}
	
	@PostMapping("/saveCommentproduct/{id}")
	public String saveComments(@PathVariable Long id,
	                           @ModelAttribute("adminProductComments") AdminProductComments adminProductComments,
	                           @RequestParam("commentText") String commentText,
	                           Model model, Principal principal) {

	    // Retrieve the AddProductAdmin entity
	    AddProductAdmin existingAddProductAdmin = addProductAdminService.getAddProductAdminById(id);
	    
	    ClientProduct existingClientProduct = clientProductService.getClientProductById(id);
	    
	    String notificationMessage = "Client has made some comments on product request ID:";
	    String productRequestId = existingAddProductAdmin.getProductRequestId();
	    String storeCode = existingAddProductAdmin.getStoreCode();
	    String notification = notificationMessage + " " + productRequestId + " - " + storeCode;
	    
	    String username = principal.getName();
	    String displayName = getDisplayNameByUsername(username);

	    // Create a new AdminComments
	    AdminProductComments comment = new AdminProductComments();
	    comment.setAddProductAdmin(existingAddProductAdmin);
	    comment.setClientProduct(existingClientProduct);
	    comment.setAdminProductComments(commentText);
	    comment.setAddedBy(displayName + "(Client)");
	    comment.setNotification(notification);
	    comment.setRequestStatus(true); // Set "yes" if true, "no" otherwise
	    comment.setReason("Regular Comment");
	    comment.setDateAdded(new Date());
	    comment.setTimeAdded(new Date());

	    // Set the productRequestId indirectly
	    comment.setProductRequestId(existingClientProduct.getProductRequestId());

	    // Save the comment
	    adminProductCommentService.saveComment(comment);
	    
	    List<User> usersWithAdminProductRole = userService.getUsersByName("ROLE_ADMIN_PRODUCTS");
	    List<String> usernames = usersWithAdminProductRole.stream().map(User::getUserName).collect(Collectors.toList());
	    System.out.println("Usernames based on ROLE_ADMIN_PRODUCT: " + usernames);

	    List<String> emailsBasedOnUsernamesFromUsers = clientProductService.getEmailsBasedOnUsernamesFromUsers(usernames);
	    System.out.println("Emails based on usernames from Users: " + emailsBasedOnUsernamesFromUsers);

	    List<String> emailsBasedOnUsernamesFromAdmins = clientProductService.getEmailsBasedOnUsernamesFromAdmins(usernames);
	    System.out.println("Emails based on usernames from Admins: " + emailsBasedOnUsernamesFromAdmins);

	    addProductAdminService.sendEmailNotifications(emailsBasedOnUsernamesFromAdmins, notification, commentText);

	    // Redirect to the support view page
	    return "redirect:/clientproductview";
	}
	
	@GetMapping("/closeproduct/{id}")
	public String closed(@PathVariable Long id,Model model) {
		model.addAttribute("clientProduct", clientProductService.getClientProductById(id));
		
		return "closeproductreqpop";
	}
	
	@PostMapping("/closeproductrequestproduct/{id}")
	public String saveCloseComments(@PathVariable Long id,
	                           @ModelAttribute("adminProductComments") AdminProductComments adminProductComments,
	                           @RequestParam("commentText") String commentText,
	                           Model model, Principal principal) {

	    // Retrieve the AddProductAdmin entity
	    AddProductAdmin existingAddProductAdmin = addProductAdminService.getAddProductAdminById(id);
	    
	    ClientProduct existingClientProduct = clientProductService.getClientProductById(id);
		existingClientProduct.setStatus("Closed");
		existingAddProductAdmin.setStatus("Closed");
		   clientProductService.saveClientProduct(existingClientProduct);
		   
		   
		    String notificationMessage = "Client has closed the support request ID:";
		    String productRequestId = existingAddProductAdmin.getProductRequestId();
		    String storeCode = existingAddProductAdmin.getStoreCode();
		    String notification = notificationMessage + " " + productRequestId + " - " + storeCode;

		    String userName = principal.getName();
		    String displayName = getDisplayNameByUsername(userName);

	    // Create a new AdminProductComments
	    AdminProductComments comment = new AdminProductComments();
	    comment.setAddProductAdmin(existingAddProductAdmin);
	    comment.setClientProduct(existingClientProduct);
	    comment.setAddedBy(displayName +"(Client)");
	    comment.setAdminProductComments(commentText);
	    comment.setRequestStatus(true);
	    comment.setNotification(notification);
	    comment.setReason("client opted to close the product request");
	    comment.setDateAdded(new Date());
	    comment.setTimeAdded(new Date());

	    // Set the productRequestId indirectly
	    comment.setProductRequestId(existingAddProductAdmin.getProductRequestId());
	    
	    // Save the comment
	    adminProductCommentService.saveComment(comment);

	    // Handle requestStatus as needed
	    List<User> usersWithAdminProductRole = userService.getUsersByName("ROLE_ADMIN_PRODUCTS");
	    List<String> usernames = usersWithAdminProductRole.stream().map(User::getUserName).collect(Collectors.toList());
	    System.out.println("Usernames based on ROLE_ADMIN_PRODUCT: " + usernames);

	    List<String> emailsBasedOnUsernamesFromUsers = clientProductService.getEmailsBasedOnUsernamesFromUsers(usernames);
	    System.out.println("Emails based on usernames from Users: " + emailsBasedOnUsernamesFromUsers);

	    List<String> emailsBasedOnUsernamesFromAdmins = clientProductService.getEmailsBasedOnUsernamesFromAdmins(usernames);
	    System.out.println("Emails based on usernames from Admins: " + emailsBasedOnUsernamesFromAdmins);

	    addProductAdminService.sendEmailNotifications(emailsBasedOnUsernamesFromAdmins, notification, commentText);


	    // Redirect to the Product view page
	    return "redirect:/clientproductview";
	}

	private List<String> base64ImageList; // Declare it as a class field

    @GetMapping("/clientproductDetailsproduct/{id}")
    public String viewProductDetails(@PathVariable Long id, Model model) {
        // Your existing code to populate addProductAdmin and ProductFilesList
    	 ClientProduct clientProduct = clientProductService.getClientProductById(id);
 	    
 	   List<AdminProductComments> adminProductCommentsList = clientProduct.getComments().stream()
 		        .filter(AdminProductComments::getRequestStatus) // Filter by getRequestStatus = true
 		        .collect(Collectors.toList());
 	   
 	    // Add the AddProductAdmin and ProductFiles list to the model
 	    model.addAttribute("ClientProduct", clientProduct);

        model.addAttribute("clientProduct", clientProduct);
        model.addAttribute("adminProductCommentsList", adminProductCommentsList);

        return "productclient";
    }
    
    
 /*   @GetMapping("/saveClientProductimages/{id}")
    public String saveClientProductimages(@PathVariable Long id, Model model) {
        // Your existing code to populate addProductAdmin and ProductFilesList
    	 ClientProduct clientProduct = clientProductService.getClientProductById(id);

 	    // Get the list of ProductFiles associated with the AddProductAdmin
 	    List<ProductFiles> productFilesList = clientProduct.getAdminProductFiles();
 	    
 	  

 	    // Add the AddProductAdmin and ProductFiles list to the model
 	    model.addAttribute("ClientProduct", clientProduct);

        // Convert ProductFiles data to Base64 and add to the model
        base64ImageList = new ArrayList<>();
        for (ProductFiles productFile : productFilesList) {
            if (productFile.getFileData() != null) {
                String base64Image = Base64.getEncoder().encodeToString(productFile.getFileData());
                base64ImageList.add(base64Image);
            } else {
                base64ImageList.add(""); // or null, depending on your preference
            }
        }

        model.addAttribute("clientProduct", clientProduct);
        model.addAttribute("base64ImageList", base64ImageList);
      

        return "viewfiles-productClient";
    }
    
    @PostMapping("/uploadImagesClientProduct/{id}")
    public String saveImages(@PathVariable Long id,
                              @RequestParam(value = "files", required = false) List<MultipartFile> files, Model model) {
    	 ClientProduct existingClientProduct = clientProductService.getClientProductById(id);
    	 model.addAttribute("existingClientProduct", existingClientProduct);

    	 AddProductAdmin addProductAdmin = addProductAdminService.getAddProductAdminById(id);
    	 
    	 List<ProductFiles> clientProductFiles = new ArrayList<>();
         if (files != null && !files.isEmpty()) {
             for (MultipartFile file : files) {
                 try {
                 	ProductFiles clientProductFile = new ProductFiles();
                 	clientProductFile.setFileData(file.getBytes());
                 	clientProductFile.setAddProductAdmin(addProductAdmin);
                 	clientProductFile.setClientProduct(existingClientProduct);
                 	clientProductFiles.add(clientProductFile);
                 } catch (IOException e) {
                     // Handle the IOException as needed
                     e.printStackTrace(); // Log the exception or throw a custom exception
                 }
             }
         }
         existingClientProduct.setAdminProductFiles(clientProductFiles);

         clientProductService.saveClientProduct(existingClientProduct);

       
        return "redirect:/saveClientProductimages/{id}";
    }
    
    
    
    @Autowired
	private ProductFilesRepository productFilesRepository;
	 @GetMapping("/download/clientProduct/{id}")
	    public ResponseEntity<ByteArrayResource> downloadProductFile(@PathVariable Long id) {
	        // Find the SupportFiles entity by id
	        Optional<ProductFiles> productFilesOptional = productFilesRepository.findById(id);
	        
	        if (productFilesOptional.isPresent()) {
	            ProductFiles productFiles = productFilesOptional.get();
	            
	            ByteArrayResource resource = new ByteArrayResource(productFiles.getFileData());
	            
	            String filename = "file_" + id + "_" + System.currentTimeMillis() + ".png"; // Generate filename dynamically
	            
	            HttpHeaders headers = new HttpHeaders();
	            headers.setContentDispositionFormData("attachment", filename);
	            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	            
	            return ResponseEntity.ok()
	                    .headers(headers)
	                    .contentLength(productFiles.getFileData().length)
	                    .body(resource);
	        } else {
	            return ResponseEntity.notFound().build();
	        }
	    }
	    */
    
    @GetMapping("/saveClientProductImages/{id}")
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


 @PostMapping("/uploadImagesClientProduct/{id}")
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
    

 @Autowired
	private ProductFilesRepository productFilesRepository;
	 @GetMapping("/download/clientproduct/{id}")
	    public ResponseEntity<ByteArrayResource> downloadProductFile(@PathVariable Long id) {
	        // Find the SupportFiles entity by id
	        Optional<ProductFiles> productFilesOptional = productFilesRepository.findById(id);
	        
	        if (productFilesOptional.isPresent()) {
	            ProductFiles productFiles = productFilesOptional.get();
	            
	            ByteArrayResource resource = new ByteArrayResource(productFiles.getFileData());
	            
	            String filename = "file_" + id + "_" + System.currentTimeMillis() + ".png"; // Generate filename dynamically
	            
	            HttpHeaders headers = new HttpHeaders();
	            headers.setContentDispositionFormData("attachment", filename);
	            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	            
	            return ResponseEntity.ok()
	                    .headers(headers)
	                    .contentLength(productFiles.getFileData().length)
	                    .body(resource);
	        } else {
	            return ResponseEntity.notFound().build();
	        }
	    }


}
