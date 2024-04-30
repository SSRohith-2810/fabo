package com.project.Fabo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.project.Fabo.repository.RolesTableRepository;
import com.project.Fabo.service.RolesTableService;

@Service
public class RolesTableServiceImpl implements RolesTableService{
	
	private RolesTableRepository rolesTableRepository;



public RolesTableServiceImpl(RolesTableRepository rolesTableRepository) {
		this.rolesTableRepository = rolesTableRepository;
	}


public String getConcatenatedRoleNamesByUserName(String userName, List<Long> roleIds) { 
    List<String> roleNames = new ArrayList<>();
    for (Long roleId : roleIds) {
        String roleName = rolesTableRepository.findRoleNameById(roleId);
        if (roleName != null) {
            roleNames.add(roleName);
        }
    }
    return String.join(", ", roleNames);
}




/*
@Override
public List<ClientUser> getClientUsersWithConcatenatedRoleNames() {
    List<ClientUser> clientUsers = clientUserRepository.findAll(); // Fetch all client users

    // Iterate through each client user and set their concatenated role names
    for (ClientUser clientUser : clientUsers) {
        List<Long> roleIds = clientUser.getRoleIds(); // Retrieve role IDs for the client user
        String concatenatedRoleNames = getConcatenatedRoleNames(roleIds); // Assuming you have a method to generate concatenated role names
        clientUser.setConcatenatedRoleNames(concatenatedRoleNames); // Set concatenated role names to each client user
    }

    return clientUsers;
}*/

}
