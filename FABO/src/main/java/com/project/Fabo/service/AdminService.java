package com.project.Fabo.service;

import java.util.List;
import java.util.Optional;

import com.project.Fabo.entity.Admin;

public interface AdminService {
	
	List<Admin> getAllAdmins();

	Admin saveAdmin(Admin admin);

	Admin getAdminById(String userName);

	Admin updateAdmin(Admin Admin);

	void deleteAdminByUserName(String userName);

	void addAdminAndRoles(Admin admin, List<Long> roleIds);

	void removeAdminAndAssociationsByUserName(String userName);

	boolean isUsernameDuplicate(String username);

	String getEmailByUserName(String username);

	void updateConcatenatedRolesByUserName(String userName, String concatenatedRoleNames);

	Optional<Admin> findByUserName(String username);



}
