package com.project.Fabo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.Fabo.entity.ClientUser;

public interface ClientUserRepository extends JpaRepository<ClientUser, String>{
	
	
	@Query("SELECT DISTINCT c.storeCode FROM ClientUser c WHERE c.activeStatus = true ")
    List<String> findDistinctStoreCodes();
	
	@Query("SELECT DISTINCT c.storeCode FROM ClientUser c WHERE c.activeStatus = true")
	List<String> findDistinctActiveStoreCodes();


    List<ClientUser> findByStoreCode(String storeCode);
    
    List<ClientUser> findByActiveStatusTrue();

    @Query("SELECT c FROM ClientUser c WHERE " +
            "LOWER(c.email) LIKE %:searchTerm% OR " +
            "LOWER(c.storeCode) LIKE %:searchTerm% OR " +
            "LOWER(c.storeName) LIKE %:searchTerm% OR " +
            "LOWER(c.userName) LIKE %:searchTerm% OR " +
            "LOWER(c.displayName) LIKE %:searchTerm% OR " +
            "LOWER(c.phoneNumber) LIKE %:searchTerm% OR " +
            "LOWER(c.concatenatedRoleNames) LIKE %:searchTerm%"
           )
	List<ClientUser> findBySearchTerm(@Param("searchTerm") String searchTerm);
    
	Object save(List<String> accesses);

	void deleteByEmail(String email);

	void deleteAllByEmail(String email);

	Optional<ClientUser> findByEmail(String email);

	boolean existsByUserName(String username);
	
	 @Query("SELECT cu.userName FROM ClientUser cu")
	List<String> getAllUserNames();

	List<ClientUser> findByConcatenatedRoleNamesContaining(String accesses);
	
	@Query("SELECT DISTINCT c.storeName FROM ClientUser c")
	List<String> findDistinctStoreNames();

	List<ClientUser> findByStoreName(String storeName);

	void deleteAllByUserName(String userName);

	Optional<ClientUser> findByUserName(String username);

	Optional<ClientUser> findByUserNameAndStoreCode(String username, String storeCode);

	 @Query("SELECT cu.storeCode FROM ClientUser cu WHERE cu.userName = :userName")
	    String findStoreCodeByUserName(@Param("userName") String userName);
}



