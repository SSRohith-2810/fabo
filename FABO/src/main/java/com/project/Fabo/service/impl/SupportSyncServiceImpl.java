package com.project.Fabo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.Fabo.entity.AddSupportAdmin;
import com.project.Fabo.entity.ClientSupport;
import com.project.Fabo.repository.AddSupportAdminRepository;
import com.project.Fabo.repository.ClientSupportRepository;
import com.project.Fabo.service.SupportMirrorService;

@Service
public class SupportSyncServiceImpl implements SupportMirrorService{

	@Autowired
    private AddSupportAdminRepository addSupportAdminRepository;

    @Autowired
    private ClientSupportRepository clientSupportRepository;

    public void addSupportRecordToBothSides(AddSupportAdmin addSupportAdmin) {
        // Save the record in admin side
    	addSupportAdminRepository.save(addSupportAdmin);

        // Create a corresponding record for client side
        ClientSupport clientSupport = new ClientSupport();
        clientSupport.setStoreName(addSupportAdmin.getStoreName());
        clientSupport.setStoreCode(addSupportAdmin.getStoreCode());
        clientSupport.setSupportRequestType(addSupportAdmin.getSupportRequestType());
        clientSupport.setStoreContact(addSupportAdmin.getStoreContact());
        clientSupport.setDate(addSupportAdmin.getDate()); // Or set the appropriate date
        clientSupport.setIssueSubject(addSupportAdmin.getIssueSubject());
        clientSupport.setIssueDescription(addSupportAdmin.getIssueDescription());
        clientSupport.setStatus(addSupportAdmin.getStatus());
        clientSupport.setSupportRequestId(addSupportAdmin.getSupportRequestId());
        clientSupport.setActiveStatus(addSupportAdmin.isActiveStatus());
        clientSupport.setExternalComments(addSupportAdmin.getCommentsToClient());
        clientSupport.setStatus(addSupportAdmin.getStatus());
        clientSupport.setUploadedBy(addSupportAdmin.getUploadedBy());        // Save the record in client side
        clientSupportRepository.save(clientSupport);
    }
    
    public void addSupportRecordToBothSide(ClientSupport clientSupport) {
    	
    	clientSupportRepository.save(clientSupport);
    	
    	AddSupportAdmin addSupportAdmin = new AddSupportAdmin();
    	
    	addSupportAdmin.setStoreName(clientSupport.getStoreName());
    	addSupportAdmin.setStoreCode(clientSupport.getStoreCode());
    	addSupportAdmin.setSupportRequestType(clientSupport.getSupportRequestType());
    	addSupportAdmin.setStoreContact(clientSupport.getStoreContact());
    	addSupportAdmin.setDate(clientSupport.getDate());
         addSupportAdmin.setIssueSubject(clientSupport.getIssueSubject());
         addSupportAdmin.setIssueDescription(clientSupport.getIssueDescription());
         addSupportAdmin.setStatus(clientSupport.getStatus());
         addSupportAdmin.setSupportRequestId(clientSupport.getSupportRequestId());
         addSupportAdmin.setActiveStatus(clientSupport.isActiveStatus());
         addSupportAdmin.setCommentsFromClient(clientSupport.getExternalComments());
         addSupportAdmin.setStatus(clientSupport.getStatus());
         addSupportAdmin.setUploadedBy(clientSupport.getUploadedBy());

         // Save the updated record in AddSupportAdmin
         addSupportAdminRepository.save(addSupportAdmin);
    }
    
}
