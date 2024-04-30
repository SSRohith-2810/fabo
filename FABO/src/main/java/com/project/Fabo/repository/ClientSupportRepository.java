package com.project.Fabo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.Fabo.entity.ClientSupport;

public interface ClientSupportRepository extends JpaRepository<ClientSupport, Long>{

	List<ClientSupport> findAll();

	List<ClientSupport> findByActiveStatusTrue();

	List<ClientSupport> findByStatus(String statusDropdown);

	@Query("SELECT c FROM ClientSupport c WHERE " +
	        "LOWER(c.storeName) LIKE %:searchTerm% OR " +
	        "LOWER(c.storeCode) LIKE %:searchTerm% OR " +
	        "LOWER(c.supportRequestType) LIKE %:searchTerm% OR " +
	        "LOWER(c.storeContact) LIKE %:searchTerm% OR " +
	        "LOWER(c.issueSubject) LIKE %:searchTerm% OR " +
	        "LOWER(c.issueDescription) LIKE %:searchTerm% OR " +
	        "LOWER(c.status) LIKE %:searchTerm% OR " +
	        "LOWER(c.externalComments) LIKE %:searchTerm% OR " +
	        "LOWER(c.commentsToAdmin) LIKE %:searchTerm%")
	List<ClientSupport> findBySearchTerm(@Param("searchTerm") String searchTerm);

	List<ClientSupport> findBySupportRequestTypeContaining(String supportRequesttype);
	
	@Query("SELECT c FROM ClientSupport c WHERE " +
	        "(LOWER(c.storeName) LIKE %:searchTerm% OR " +
	        "LOWER(c.storeCode) LIKE %:searchTerm% OR " +
	        "LOWER(c.supportRequestType) LIKE %:searchTerm% OR " +
	        "LOWER(c.storeContact) LIKE %:searchTerm% OR " +
	        "LOWER(c.issueSubject) LIKE %:searchTerm% OR " +
	        "LOWER(c.issueDescription) LIKE %:searchTerm% OR " +
	        "LOWER(c.status) LIKE %:searchTerm% OR " +
	        "LOWER(c.externalComments) LIKE %:searchTerm% OR " +
	        "LOWER(c.commentsToAdmin) LIKE %:searchTerm%) AND " +
	        "c.storeCode = :storeCode") // Add condition for userStoreCode
	List<ClientSupport> findBySearchTermAndStoreCode(@Param("searchTerm") String searchTerm, @Param("storeCode") String userStoreCode);


	List<ClientSupport> findBySupportRequestTypeAndStoreCode(String supportRequesttype, String userStoreCode);

	List<ClientSupport> findBySupportRequestTypeContainingAndStoreCode(String supportRequesttype, String userStoreCode);

	List<ClientSupport> findByStatusAndStoreCode(String statusDropdown, String userStoreCode);

	List<ClientSupport> findByStoreCode(String userStoreCode);

	long countByStatusAndStoreCode(String string, String storeCode);

	long countByStoreCode(String userStoreCode);
}
