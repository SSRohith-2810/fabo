package com.project.Fabo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.Fabo.entity.AddProductAdmin;
import com.project.Fabo.entity.ClientProduct;
import com.project.Fabo.repository.AddProductAdminRepository;
import com.project.Fabo.repository.ClientProductRepository;
import com.project.Fabo.service.ProductMirrorService;



@Service
public class ProductSyncServiceImpl implements ProductMirrorService{

	@Autowired
    private AddProductAdminRepository addProductAdminRepository;

    @Autowired
    private ClientProductRepository clientProductRepository;

    public void addProductRecordToBothSides(AddProductAdmin addProductAdmin) {
        // Save the record in admin side
    	addProductAdminRepository.save(addProductAdmin);

        // Create a corresponding record for client side
        ClientProduct clientProduct = new ClientProduct();
        clientProduct.setStoreName(addProductAdmin.getStoreName());
        clientProduct.setStoreCode(addProductAdmin.getStoreCode());
        clientProduct.setProductRequestType(addProductAdmin.getProductRequestType());
        clientProduct.setStoreContact(addProductAdmin.getStoreContact());
        clientProduct.setDate(addProductAdmin.getDate()); // Or set the appropriate date
        clientProduct.setIssueSubject(addProductAdmin.getIssueSubject());
        clientProduct.setIssueDescription(addProductAdmin.getIssueDescription());
        clientProduct.setStatus(addProductAdmin.getStatus());
        clientProduct.setProductRequestId(addProductAdmin.getProductRequestId());
        clientProduct.setActiveStatus(addProductAdmin.isActiveStatus());
        clientProduct.setExternalComments(addProductAdmin.getCommentsToClient());
        clientProduct.setUploadedBy(addProductAdmin.getUploadedBy());
        clientProduct.setShippingAddress(addProductAdmin.getShippingAddress());

        // Save the record in client side
        clientProductRepository.save(clientProduct);
    }
    
    public void addProductRecordToBothSide(ClientProduct clientProduct) {
    	
    	clientProductRepository.save(clientProduct);
    	
    	AddProductAdmin addProductAdmin = new AddProductAdmin();
    	
    	addProductAdmin.setStoreName(clientProduct.getStoreName());
    	addProductAdmin.setStoreCode(clientProduct.getStoreCode());
    	addProductAdmin.setProductRequestType(clientProduct.getProductRequestType());
    	addProductAdmin.setStoreContact(clientProduct.getStoreContact());
    	addProductAdmin.setDate(clientProduct.getDate());
         addProductAdmin.setIssueSubject(clientProduct.getIssueSubject());
         addProductAdmin.setIssueDescription(clientProduct.getIssueDescription());
         addProductAdmin.setStatus(clientProduct.getStatus());
         addProductAdmin.setProductRequestId(clientProduct.getProductRequestId());
         addProductAdmin.setActiveStatus(clientProduct.isActiveStatus());
         addProductAdmin.setCommentsFromClient(clientProduct.getExternalComments());
         addProductAdmin.setUploadedBy(clientProduct.getUploadedBy());
         addProductAdmin.setShippingAddress(clientProduct.getShippingAddress());

         // Save the updated record in AddProductAdmin
         addProductAdminRepository.save(addProductAdmin);
    }
    
}
