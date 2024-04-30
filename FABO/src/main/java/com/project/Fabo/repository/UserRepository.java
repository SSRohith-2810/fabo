package com.project.Fabo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.Fabo.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByUserName(String userName);

	List<User> findAllByUserName(String email);

	void deleteAllByUserName(String email);

	void deleteByUserName(String email);

	User findByPassword(String username);

	boolean existsByUserName(String userName);
	
	 @Query("SELECT u FROM User u JOIN UsersRoles ur ON u.id = ur.user.id JOIN Role r ON ur.role.id = r.id WHERE r.name = :name")
	 List<User> findByName(@Param("name") String name);

	User findByToken(String token);




}
