package com.project.Fabo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.Fabo.entity.Admin;


public interface AdminRepository extends JpaRepository<Admin, String>{
	
	@Query("SELECT c FROM Admin c WHERE " +
	        "LOWER(c.userName) LIKE %:searchTerm% OR " +
	        "LOWER(c.adminName) LIKE %:searchTerm% OR " +
	        "LOWER(c.email) LIKE %:searchTerm% OR " +
	        "LOWER(c.phoneNumber) LIKE %:searchTerm% OR " +
	        "LOWER(c.displayName) LIKE %:searchTerm% OR " +
	        "LOWER(c.concatenatedRoleNames) LIKE %:searchTerm%")
	List<Admin> findBySearchTerm(@Param("searchTerm") String searchTerm);

	void deleteAllByEmail(String email);

	boolean existsByUserName(String userName);

	Optional<Admin> findByEmail(String email);

	List<Admin> findByConcatenatedRoleNamesContaining(String accesses);

	List<Admin> findByActiveStatusTrue();

	Optional<Admin> findByUserName(String userName);

}
