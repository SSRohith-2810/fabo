package com.project.Fabo.service;

import java.util.List;

public interface RolesTableService {

	String getConcatenatedRoleNamesByUserName(String userName, List<Long> roleIds);

	

}
