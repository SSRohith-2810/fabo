package com.project.Fabo.service.impl;

import java.util.HashMap;
import java.util.Map;

public class RoleMappingServiceImpl {
	private final Map<Long, String> roleMap = new HashMap<>();

    public void RoleMappingService() {
        // Initialize the role mapping
        roleMap.put(2L, "Support");
        roleMap.put(3L, "Products");
        roleMap.put(4L, "Creatives");
        roleMap.put(5L, "Accounts");
        roleMap.put(6L, "Support");
        roleMap.put(7L, "Products");
        roleMap.put(8L, "Creatives");
        roleMap.put(9L, "Accounts");
    }

    public String getRoleNameById(Long roleId) {
        return roleMap.getOrDefault(roleId, "Role Not Found");
    } 
}
