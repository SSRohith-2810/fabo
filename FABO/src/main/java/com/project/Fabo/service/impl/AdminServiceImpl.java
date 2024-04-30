package com.project.Fabo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.Fabo.entity.Admin;
import com.project.Fabo.entity.Role;
import com.project.Fabo.entity.User;
import com.project.Fabo.entity.UsersRoles;
import com.project.Fabo.repository.AdminRepository;
import com.project.Fabo.repository.RoleRepository;
import com.project.Fabo.repository.UserRepository;
import com.project.Fabo.repository.UserRolesRepository;
import com.project.Fabo.service.AdminService;

import jakarta.transaction.Transactional;

@Service
public class AdminServiceImpl implements AdminService{
	
	private AdminRepository adminRepository;
	
	private UserRepository userRepository;
	
	private RoleRepository roleRepository;

	@Autowired
	private UserRolesRepository userRolesRepository;

	public AdminServiceImpl(AdminRepository adminRepository, UserRepository userRepository,
			RoleRepository roleRepository, UserRolesRepository userRolesRepository) {
		this.adminRepository = adminRepository;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.userRolesRepository = userRolesRepository;
	}

	@Override
	public List<Admin> getAllAdmins() {
		return adminRepository.findAll();
	}

	@Override
	public Admin saveAdmin(Admin admin) {
		return adminRepository.save(admin);
	}

	@Override
	public Admin getAdminById(String userName) {
		return adminRepository.findById(userName).get();
	}

	@Override
	public Admin updateAdmin(Admin admin) {
		return adminRepository.save(admin);
	}

	@Transactional
	public void addAdminAndRoles(Admin admin, List<Long> roleIds) {
	    User existingUser = userRepository.findByUserName(admin.getUserName());

	    if (existingUser != null) {
	        // If user exists, remove existing roles
	        List<UsersRoles> usersRoles = userRolesRepository.findByUser(existingUser);
	        userRolesRepository.deleteAll(usersRoles);
	    } else {
	        // Create a new user if it doesn't exist
	        User newUser = new User();
	        newUser.setToken(admin.getToken());
	        newUser.setUserName(admin.getUserName());
	        userRepository.save(newUser);
	    }

	    // Save ClientUser details
	    Admin savedClientUser = adminRepository.save(admin);

	    // Assign roles to either existing or newly created User
	    User user = existingUser != null ? existingUser : userRepository.findByUserName(admin.getUserName());

	    for (Long roleId : roleIds) {
	        Role role = roleRepository.findById(roleId).orElse(null);
	        if (role != null) {
	            UsersRoles usersRole = new UsersRoles();
	            usersRole.setUser(user);
	            usersRole.setRole(role);
	            userRolesRepository.save(usersRole);
	        }
	    }
	}
	 
	@Transactional
	public void removeAdminAndAssociationsByUserName(String userName) {
	    // Find users by email
	    List<User> users = userRepository.findAllByUserName(userName);

	    for (User user : users) {
	        // Find associations by user
	        List<UsersRoles> usersRoles = userRolesRepository.findByUser(user);
	        userRolesRepository.deleteAll(usersRoles);
	    }

	    // Delete clientUsers by email
	    adminRepository.deleteAllByEmail(userName);
	}
	
	@Override
	public void deleteAdminByUserName(String userName) {
		Optional<Admin> adminOptional = adminRepository.findByUserName(userName);

        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            admin.setActiveStatus(false); // Marking as inactive

            adminRepository.save(admin);
        } else {
            throw new IllegalArgumentException("Admin ID not found: " + userName);
        }
	}

	@Override
	public void updateConcatenatedRolesByUserName(String userName, String concatenatedRoleNames) {
		 Optional<Admin> optionalAdmin = adminRepository.findByUserName(userName);

		    optionalAdmin.ifPresent(admin -> {
		        admin.setConcatenatedRoleNames(concatenatedRoleNames);
		        adminRepository.save(admin);
		    });
		
	}

	@Override
	public boolean isUsernameDuplicate(String userName) {
		// TODO Auto-generated method stub
		return adminRepository.existsByUserName(userName);
	}

	@Override
	public String getEmailByUserName(String username) {
	    Optional<Admin> adminOptional = adminRepository.findByUserName(username);
	    return adminOptional.map(Admin::getEmail).orElse(null);
	}

	@Override
	public Optional<Admin> findByUserName(String username) {
	    // Assuming you have a repository or service to interact with your database
	    Optional<Admin> admin = adminRepository.findByUserName(username);
	    return admin;
	}




}
