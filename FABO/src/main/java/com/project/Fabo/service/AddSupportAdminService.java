package com.project.Fabo.service;


import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.project.Fabo.entity.AddSupportAdmin;


public interface AddSupportAdminService {

	AddSupportAdmin saveAddSupportAdmin(AddSupportAdmin addSupportAdmin);

	AddSupportAdmin getAddSupportAdminById(Long id);

	void saveCommentAndStatusById(Long id, String commentText, String clientVisible, String requestStatus);

	void deleteAddSupportAdminById(Long id);

	List<String> getUsernamesBasedOnStoreCode(String string, String storeCode);

	List<String> getEmailsBasedOnUsernames(List<String> usernamesBasedOnStorecode);

	void sendEmailNotification(AddSupportAdmin addSupportAdmin, List<String> emailsBasedOnUsernames,
			List<MultipartFile> files, String notification);

	void sendEmailNotifications(List<String> emailsBasedOnUsernames, String notification, String commentText);

}
