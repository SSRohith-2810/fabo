package com.project.Fabo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.Fabo.entity.User;
import com.project.Fabo.entity.UsersRoles;

public interface UserRolesRepository extends JpaRepository<UsersRoles, Long> {

	List<UsersRoles> findByUser(User user);

	void deleteByUserIn(List<User> findAllByUserName);

	List<UsersRoles> findByRoleId(Long roleId);


}
