package com.project.Fabo.controller;

import java.security.Principal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.Fabo.entity.Client;
import com.project.Fabo.entity.ClientProduct;
import com.project.Fabo.entity.ClientSupport;
import com.project.Fabo.entity.Invoice;
import com.project.Fabo.repository.AddProductAdminRepository;
import com.project.Fabo.repository.AddSupportAdminRepository;
import com.project.Fabo.repository.ClientProductRepository;
import com.project.Fabo.repository.ClientRepository;
import com.project.Fabo.repository.ClientSupportRepository;
import com.project.Fabo.repository.InvoiceRepository;
import com.project.Fabo.service.UserService;

@Controller
public class DashboardController {
	
	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private AddSupportAdminRepository addSupportAdminRepository;
	
	@Autowired
	private AddProductAdminRepository addProductAdminRepository;
	
	@Autowired
	private ClientSupportRepository clientSupportRepository;
	
	@Autowired
	private ClientProductRepository clientProductRepository;
	
	@Autowired
	private InvoiceRepository invoiceRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AddSupportAdminRepository supportAdminRepository;
	
	@Autowired
	private AddProductAdminRepository productAdminRepository;
	
	@GetMapping("superadmin.dashboard")
	public String superAdminDashBoard(@RequestParam(name = "getStore", required = false) String getStore, Model model) {
		   if (getStore != null && !getStore.isEmpty()) {
		       String storeCode = getStore.split(" - ")[0];
		       double totalAmount = invoiceRepository.getTotalAmountOfInvoices(storeCode);
		       double totalPaidAmount = invoiceRepository.getTotalAmountOfPaidInvoices(storeCode);
		       double totalPendingAmount = invoiceRepository.getTotalAmountOfPendingInvoices(storeCode);
			        String formattedTotalAmount = formatAmount(totalAmount);
			        String formattedTotalPaidAmount = formatAmount(totalPaidAmount);
			        String formattedTotalPendingAmount = formatAmount(totalPendingAmount);
				        model.addAttribute("totalAmount", formattedTotalAmount);
				        model.addAttribute("totalPaidAmount", formattedTotalPaidAmount);
				        model.addAttribute("totalPendingAmount", formattedTotalPendingAmount);
		        
		        long totalSupportRequests = supportAdminRepository.getTotalSupportRequests(storeCode);
		        long closedSupportRequests = supportAdminRepository.getCountOfClosedSupportRequests(storeCode);
		        long reOpenAndNewSupportRequests = supportAdminRepository.getCountOfNewAndReOpenSupportRequests(storeCode);
			        model.addAttribute("totalSupportRequests", totalSupportRequests);
			        model.addAttribute("closedSupportRequests", closedSupportRequests);
			        model.addAttribute("reOpenAndNewSupportRequests", reOpenAndNewSupportRequests);

		        long totalProductRequests = productAdminRepository.getTotalProductRequests(storeCode);
		        long closedProductRequests = productAdminRepository.getCountOfClosedProductRequests(storeCode);
		        long closedAndInProgressProductRequests = productAdminRepository.getCountOfClosedAndInProgressProductRequests(storeCode);
			        model.addAttribute("totalProductRequests", totalProductRequests);
			        model.addAttribute("closedProductRequests", closedProductRequests);
			        model.addAttribute("closedAndInProgressProductRequests", closedAndInProgressProductRequests);

	        return "superadmin.dashboard";
	    } else {
	        // Show all data without filtering
	        double totalAmount = invoiceRepository.getTotalAmountOfInvoices();
	        double totalPaidAmount = invoiceRepository.getTotalAmountOfPaidInvoices();
	        double totalPendingAmount = invoiceRepository.getTotalAmountOfPendingInvoices();
		        String formattedTotalAmount = formatAmount(totalAmount);
		        String formattedTotalPaidAmount = formatAmount(totalPaidAmount);
			    String formattedTotalPendingAmount = formatAmount(totalPendingAmount);
			        model.addAttribute("totalAmount", formattedTotalAmount);
			        model.addAttribute("totalPaidAmount", formattedTotalPaidAmount);
			        model.addAttribute("totalPendingAmount", formattedTotalPendingAmount);

	        long totalSupportRequests = supportAdminRepository.getTotalSupportRequests();
	        long closedSupportRequests = supportAdminRepository.getCountOfClosedSupportRequests();
	        long reOpenAndNewSupportRequests = supportAdminRepository.getCountOfNewAndReOpenSupportRequests();
		        model.addAttribute("totalSupportRequests", totalSupportRequests);
		        model.addAttribute("closedSupportRequests", closedSupportRequests);
		        model.addAttribute("reOpenAndNewSupportRequests", reOpenAndNewSupportRequests);

	        long totalProductRequests = productAdminRepository.getTotalProductRequests();
	        long closedProductRequests = productAdminRepository.getCountOfClosedProductRequests();
	        long closedAndInProgressProductRequests = productAdminRepository.getCountOfClosedAndInProgressProductRequests();
		        model.addAttribute("totalProductRequests", totalProductRequests);
		        model.addAttribute("closedProductRequests", closedProductRequests);
		        model.addAttribute("closedAndInProgressProductRequests", closedAndInProgressProductRequests);

	        List<Client> clients;
	        clients = clientRepository.findAll();
	        model.addAttribute("clients", clients);
 
	        return "superadmin.dashboard";
	    }
	}
	
	@GetMapping("/supportadmindashboard")
	public String superadmin1(@RequestParam(name = "storeinfo", required = false) String storeInfo, Model model) {
	    // Retrieve the supportAdminCount by default
	    long supportAdminCount = addSupportAdminRepository.count();
	    model.addAttribute("supportAdminCount", supportAdminCount);

	    long closedSupportCount = addSupportAdminRepository.countByStatus("Closed");
	    model.addAttribute("closedSupportCount", closedSupportCount);

	    long pendingSupportCount = supportAdminCount - closedSupportCount;
	    model.addAttribute("pendingSupportCount", pendingSupportCount);

	    // If a storeInfo is provided, search for addSupportAdmin records by storeCode
	    if (storeInfo != null && !storeInfo.isEmpty()) {
	        String storeCode = storeInfo.split(" - ")[0];

	        long count = addSupportAdminRepository.countByStoreCode(storeCode);
	        model.addAttribute("supportAdminCount", count);

	        long closedCount = clientSupportRepository.countByStatusAndStoreCode("Closed", storeCode);
	        model.addAttribute("closedSupportCount", closedCount);

	        long pendingCount = count - closedCount; // Calculate pending count based on the filtered supportAdminCount and closedCount
	        model.addAttribute("pendingSupportCount", pendingCount);
	    }

	    // Retrieve the clients list
	    List<Client> clients = clientRepository.findAll();
	    model.addAttribute("clients", clients);

	    return "supportadmindashboard";
	} 
	
	private String formatAmount(double amount) {
	    NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
	    return numberFormat.format(amount) + "/-";
	}

	
	@GetMapping("/accountadmindashboard")
	public String superadmin2(@RequestParam(name = "storeinfo", required = false) String storeInfo,Model model) {
		if (storeInfo != null && !storeInfo.isEmpty()) {
		       String storeCode = storeInfo.split(" - ")[0];
		       double totalAmount = invoiceRepository.getTotalAmountOfInvoices(storeCode);
		       double totalPaidAmount = invoiceRepository.getTotalAmountOfPaidInvoices(storeCode);
		       double totalPendingAmount = invoiceRepository.getTotalAmountOfPendingInvoices(storeCode);
			        String formattedTotalAmount = formatAmount(totalAmount);
			        String formattedTotalPaidAmount = formatAmount(totalPaidAmount);
			        String formattedTotalPendingAmount = formatAmount(totalPendingAmount);
				        model.addAttribute("totalAmount", formattedTotalAmount);
				        model.addAttribute("totalPaidAmount", formattedTotalPaidAmount);
				        model.addAttribute("totalPendingAmount", formattedTotalPendingAmount);
				        return "accountadmindashboard";
		}
	 else {
		 double totalAmount = invoiceRepository.getTotalAmountOfInvoices();
	        double totalPaidAmount = invoiceRepository.getTotalAmountOfPaidInvoices();
	        double totalPendingAmount = invoiceRepository.getTotalAmountOfPendingInvoices();
		        String formattedTotalAmount = formatAmount(totalAmount);
		        String formattedTotalPaidAmount = formatAmount(totalPaidAmount);
			    String formattedTotalPendingAmount = formatAmount(totalPendingAmount);
			        model.addAttribute("totalAmount", formattedTotalAmount);
			        model.addAttribute("totalPaidAmount", formattedTotalPaidAmount);
			        model.addAttribute("totalPendingAmount", formattedTotalPendingAmount);
		
				        }
		List<Client> clients;
		clients = clientRepository.findAll();
		model.addAttribute("clients", clients);
		return "accountadmindashboard";
	}
	
	@GetMapping("/productadmindashboard")
	public String superadmin3(@RequestParam(name = "storeinfo", required = false) String storeInfo,Model model) {
		
		  // Retrieve the supportAdminCount by default
	    long productAdminCount = addProductAdminRepository.count();
	    model.addAttribute("productAdminCount", productAdminCount);

	    long closedProductCount = addProductAdminRepository.countByStatus("Closed");
	    model.addAttribute("closedProductCount", closedProductCount);

	    long pendingProductCount = productAdminCount - closedProductCount;
	    model.addAttribute("pendingProductCount", pendingProductCount);

	    // If a storeInfo is provided, search for addSupportAdmin records by storeCode
	    if (storeInfo != null && !storeInfo.isEmpty()) {
	        String storeCode = storeInfo.split(" - ")[0];

	        long count = addProductAdminRepository.countByStoreCode(storeCode);
	        model.addAttribute("productAdminCount", count);

	        long closedCount = clientProductRepository.countByStatusAndStoreCode("Closed", storeCode);
	        model.addAttribute("closedProductCount", closedCount);

	        long pendingCount = count - closedCount; // Calculate pending count based on the filtered supportAdminCount and closedCount
	        model.addAttribute("pendingProductCount", pendingCount);
	    }

	    // Retrieve the clients list
	    List<Client> clients = clientRepository.findAll();
	    model.addAttribute("clients", clients);

		return "productadmindashboard";
	}
	
	@GetMapping("/clientsupportadmindash")
	public String superadmin5(Model model,Principal principal) {
		 List<ClientSupport> clientSupports;
		 
		  String username =principal.getName();
		  
		  String userStoreCode=userService.getUserStoreCodeByUserName(username);
		  
		  long clientSupportCount =clientSupportRepository.countByStoreCode(userStoreCode);
		  model.addAttribute("clientSupportCount", clientSupportCount);
		  
		  long clientSupportResolved =clientSupportRepository.countByStatusAndStoreCode("Closed",userStoreCode);
		  model.addAttribute("clientSupportResolved", clientSupportResolved);
		 
		  long pendingSupportRequests=clientSupportCount-clientSupportResolved;
		  model.addAttribute("pendingSupportRequests", pendingSupportRequests); 
		
		return "clientsupportadmindash";
	}

	@GetMapping("/clientaccountadmindash")
	public String superadmin4( Model model, Principal principal) {
		 List<Invoice> invoices;
		 String username = principal.getName();

	        // Retrieve the user's store code based on their username
	        String userStoreCode = userService.getUserStoreCodeByUserName(username); // Replace userService with your actual service

		 
		 invoices = invoiceRepository.findByStoreCodeAndActiveStatusTrue(userStoreCode);
		 
		// Calculate the total sum of invoiceAmounts for the user's store code
		 double totalInvoiceAmount = 0.0;

		 for (Invoice invoice : invoices) {
			    // Remove comma and then convert invoice amount from String to double
			    String amountStr = invoice.getInvoiceAmount().replace(",", "");
			    double invoiceAmount = Double.parseDouble(amountStr);
			    // Add the converted invoice amount to the total
			    totalInvoiceAmount += invoiceAmount;
			}
		 
		 DecimalFormat decimalFormat = new DecimalFormat("#,###");
		 String formattedTotalInvoiceAmount = decimalFormat.format(totalInvoiceAmount);


	        // Add the total sum of amounts to the model
	        model.addAttribute("formattedTotalInvoiceAmount",  formattedTotalInvoiceAmount);
	        double totalPaidInvoiceAmount = 0.0;

	        for (Invoice invoice : invoices) {
	            if (invoice.getInvoiceStatus().equalsIgnoreCase("paid")) {
	                // Remove comma and then convert invoice amount from String to double
	                String amountStr = invoice.getInvoiceAmount().replace(",", "");
	                double invoiceAmount = Double.parseDouble(amountStr);
	                // Add the converted invoice amount to the total for paid invoices
	                totalPaidInvoiceAmount += invoiceAmount;
	           }
	        }
// Format the total paid invoice amount
	        DecimalFormat decimalFormat1 = new DecimalFormat("#,###");
	        String formattedTotalPaidInvoiceAmount = decimalFormat1.format(totalPaidInvoiceAmount);
	        formattedTotalPaidInvoiceAmount = formattedTotalPaidInvoiceAmount != null ? formattedTotalPaidInvoiceAmount : "0";
	         model.addAttribute("formattedTotalPaidInvoiceAmount",  formattedTotalPaidInvoiceAmount);
	      
	         
	      double pendingAmount=totalInvoiceAmount-totalPaidInvoiceAmount;
	      System.out.println(pendingAmount);
	     DecimalFormat decimalFormat3 = new DecimalFormat("#,###");
	     String formattedPendingInvoiceAmount = decimalFormat3.format(pendingAmount);
	     model.addAttribute("formattedPendingInvoiceAmount",formattedPendingInvoiceAmount);
	     

		
		return "clientaccountadmindash";
	}

	@GetMapping("/clientproductadmindash")
	public String superadmin6(Model model,Principal principal) {

		 // Add Principal parameter to retrieve logged-in user information
		    String username = principal.getName(); // Retrieve the currently logged-in user's email
		    
           // Retrieve the user's store code based on their email
		    String userStoreCode = userService.getUserStoreCodeByUserName(username); // Repl
		    long clientProductCount = clientProductRepository.countByActiveStatusAndStoreCode(true, userStoreCode);
		    
		    // Add the client products count to the model
		    model.addAttribute("clientProductCount", clientProductCount);
		   
		    long closedClientProductCount = clientProductRepository.countByStatusAndStoreCode("Closed", userStoreCode);
		    
		    // Add the closed client products count to the model
		    model.addAttribute("closedClientProductCount", closedClientProductCount);
		    
		    long pendingClientProductCount=(clientProductCount)-(closedClientProductCount);
		    model.addAttribute("pendingClientProductCount",pendingClientProductCount);
   
		return "clientproductadmindash";
	}


}


