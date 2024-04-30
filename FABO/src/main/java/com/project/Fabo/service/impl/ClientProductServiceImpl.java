package com.project.Fabo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.project.Fabo.entity.AddProductAdmin;
import com.project.Fabo.entity.ClientProduct;
import com.project.Fabo.repository.AddProductAdminRepository;
import com.project.Fabo.repository.ClientProductRepository;
import com.project.Fabo.service.AdminService;
import com.project.Fabo.service.ClientProductService;
import com.project.Fabo.service.UserService;


@Service
public class ClientProductServiceImpl implements ClientProductService{
	
	private ClientProductRepository clientProductRepository;
	
	private AddProductAdminRepository addProductAdminRepository;
	
private UserService userService;
	
	private AdminService adminService;	

	

	public ClientProductServiceImpl(ClientProductRepository clientProductRepository,
			AddProductAdminRepository addProductAdminRepository, UserService userService, AdminService adminService) {
		super();
		this.clientProductRepository = clientProductRepository;
		this.addProductAdminRepository = addProductAdminRepository;
		this.userService = userService;
		this.adminService = adminService;
	}

	@Value("${max.file.size}") // Define this property in your application.properties or application.yml
	    private long maxFileSize;



	@Override
	public ClientProduct saveClientProduct(ClientProduct clientProduct) {
		ClientProduct savedclientProduct = clientProductRepository.save(clientProduct);
		savedclientProduct.setFormattedProductNumber(savedclientProduct.getId());
		return clientProductRepository.save(clientProduct);
	}

	@Override
	public ClientProduct getClientProductById(Long id) {
		return clientProductRepository.findById(id).get();
	}

	@Override
	public ClientProduct updateAdmin(ClientProduct existingClientProduct) {
		return clientProductRepository.save(existingClientProduct);
	}

	 @Override
	    public void saveComment(Long id, String comment) {
	        // Retrieve client support entity by ID
	        Optional<ClientProduct> optionalClientProduct = clientProductRepository.findById(id);
	        Optional<AddProductAdmin> optionalAddProductAdmin = addProductAdminRepository.findById(id);

	        // Check if the entity exists and save the comment
	        optionalClientProduct.ifPresent(clientProduct -> {
	            clientProduct.setCommentsToAdmin(comment);
	            clientProductRepository.save(clientProduct);
	        });
	        
	        optionalAddProductAdmin.ifPresent(addProductAdmin -> {
	            addProductAdmin.setCommentsFromClient(comment);
	            addProductAdminRepository.save(addProductAdmin);
	        });
	    }

	@Override
	public void saveCloseComment(Long id, String closeComment, String close) {
		
		 Optional<ClientProduct> optionalClientProduct = clientProductRepository.findById(id);
	        Optional<AddProductAdmin> optionalAddProductAdmin = addProductAdminRepository.findById(id);

	        // Check if the entity exists and save the comment
	        optionalClientProduct.ifPresent(clientProduct -> {
	            clientProduct.setCommentsToAdmin(closeComment);
	            clientProduct.setStatus(close);
	            clientProductRepository.save(clientProduct);
	        });
	        
	        optionalAddProductAdmin.ifPresent(addProductAdmin -> {
	            addProductAdmin.setCommentsFromClient(closeComment);
	            addProductAdmin.setStatus(close);
	            addProductAdminRepository.save(addProductAdmin);
	        });
		
	}


	public void deleteClientProductById(Long id) {
	    Optional<ClientProduct> clientproductOptional = clientProductRepository.findById(id);

	    if (clientproductOptional.isPresent()) {
	        ClientProduct clientProduct = clientproductOptional.get();
	        clientProduct.setActiveStatus(false); // Marking as inactive

	        clientProductRepository.save(clientProduct);
	    } else {
	        throw new IllegalArgumentException("Client Product ID not found: " + id);
	    }
	}

	public List<String> getEmailsBasedOnUsernamesFromUsers(List<String> usernames) {
	    List<String> emails = new ArrayList<>();
	    for (String username : usernames) {
	        String email = userService.getEmailByUserName(username); 
	        if (email != null && !email.isEmpty()) {
	            emails.add(email);
	        }
	    }
	    return emails;
	}

	public List<String> getEmailsBasedOnUsernamesFromAdmins(List<String> usernames) {
	    List<String> emails = new ArrayList<>();
	    for (String username : usernames) {
	        String email = adminService.getEmailByUserName(username);
	        if (email != null && !email.isEmpty()) {
	            emails.add(email);
	        }
	    }
	    return emails;
	}
}
