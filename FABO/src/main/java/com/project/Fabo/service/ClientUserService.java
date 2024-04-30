package com.project.Fabo.service;

import java.util.List;
import java.util.Optional;

import com.project.Fabo.entity.ClientUser;

public interface ClientUserService {
	
	List<ClientUser> getAllUsers();
	
	ClientUser saveUser(ClientUser ClientUser);
	
	ClientUser getClientUserById(String userName);
	
	ClientUser updateUser(ClientUser ClientUser);
	
	void addClientUserAndRoles(ClientUser clientUser, List<Long> roleIds);

	void removeClientUserAndAssociationsByUserName(String userName);

	boolean isUsernameDuplicate(String username);

	Optional<ClientUser> getClientUserByEmail(String email);

	void deleteClientUserByUserName(String userName);

	List<String> getUserNamesByStoreCode(List<String> usernames, String string);

	String getEmailByUserName(String username);

	void updateConcatenatedRolesByUserName(String userName, String concatenatedRoleNames);

	Optional<ClientUser> findByUserName(String username);

}