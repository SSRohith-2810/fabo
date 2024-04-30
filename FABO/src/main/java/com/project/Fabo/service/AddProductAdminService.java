package com.project.Fabo.service;


import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.project.Fabo.entity.AddProductAdmin;

public interface AddProductAdminService {

	AddProductAdmin saveAddProductAdmin(AddProductAdmin addProductAdmin);

	AddProductAdmin getAddProductAdminById(Long id);

	void saveCommentAndStatusById(Long id, String commentText, String clientVisible, String requestStatus);

	void deleteAddProductAdminById(Long id);

	List<String> getUsernamesBasedOnStoreCode(String string, String storeCode);

	List<String> getEmailsBasedOnUsernames(List<String> usernamesBasedOnStorecode);

	void sendEmailNotifications(List<String> emailsBasedOnUsernames, String notification, String commentText);

	void sendEmailNotification(AddProductAdmin addProductAdmin, List<String> emailsBasedOnUsernames,
			List<MultipartFile> files);

	
}
 