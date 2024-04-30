package com.project.Fabo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.Fabo.entity.Invoice;

@Service
public interface InvoiceService {

List<Invoice> getAllInvoices();
	
	Invoice saveInvoice(Invoice Invoice);
	
	Invoice getInvoiceById(Long id);
	
	Invoice updateInvoice(Invoice Invoice);
	
	void deleteInvoiceById(Long id);

	List<String> getUsernamesBasedOnStoreCode(String string, String storeCode);

	List<String> getEmailsBasedOnUsernames(List<String> usernamesBasedOnStorecode);

	void sendEmailNotification(List<String> emailsBasedOnUsernames, String notification, String commentText);

	void sendEmailNotification(Invoice invoice, List<String> emailsBasedOnUsernames, List<MultipartFile> files);

}
