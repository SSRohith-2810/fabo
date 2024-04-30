package com.project.Fabo.service;

import java.util.List;

import com.project.Fabo.entity.ClientProduct;

public interface ClientProductService {

	ClientProduct saveClientProduct(ClientProduct clientProduct);

	ClientProduct getClientProductById(Long id);

	ClientProduct updateAdmin(ClientProduct existingClientProduct);

	void saveComment(Long id, String comment);

	void saveCloseComment(Long id, String closeComment, String close);

	void deleteClientProductById(Long id);

	List<String> getEmailsBasedOnUsernamesFromUsers(List<String> usernames);

	List<String> getEmailsBasedOnUsernamesFromAdmins(List<String> usernames);



} 
