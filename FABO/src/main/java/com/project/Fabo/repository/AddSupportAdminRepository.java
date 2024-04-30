package com.project.Fabo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.Fabo.entity.AddSupportAdmin;

public interface AddSupportAdminRepository extends JpaRepository<AddSupportAdmin, Long>{
	 List<AddSupportAdmin> findByActiveStatusTrue();

	List<AddSupportAdmin> findBySupportRequestTypeContaining(String invoicedropdown);

	List<AddSupportAdmin> findByStatus(String statusDropdown);

	List<AddSupportAdmin> findBySupportRequestTypeContainingAndStatus(String invoicedropdown, String statusDropdown);
	
	@Query("SELECT a FROM AddSupportAdmin a WHERE " +
	        "LOWER(a.storeName) LIKE %:searchTerm% OR " +
	        "LOWER(a.storeCode) LIKE %:searchTerm% OR " +
	        "LOWER(a.supportRequestType) LIKE %:searchTerm% OR " +
	        "LOWER(a.storeContact) LIKE %:searchTerm% OR " +
	        "LOWER(a.issueSubject) LIKE %:searchTerm% OR " +
	        "LOWER(a.issueDescription) LIKE %:searchTerm% OR " +
	        "LOWER(a.status) LIKE %:searchTerm% OR " +
	        "LOWER(a.internalComments) LIKE %:searchTerm% OR " +
	        "LOWER(a.commentsToClient) LIKE %:searchTerm% OR " +
	        "LOWER(a.commentsFromClient) LIKE %:searchTerm%")
	List<AddSupportAdmin> findBySearchTerm(@Param("searchTerm") String searchTerm);
	
	@Query("SELECT DISTINCT a.storeCode FROM AddSupportAdmin a WHERE a.activeStatus = true")
	List<String> findDistinctStoreCodes();
	
	 @Query("SELECT DISTINCT c.storeCode FROM Client c WHERE c.activeStatus = true")
	 List<String> findActiveStoreCodes();

	long countByStoreCode(String storeCode);

	long countByStatus(String string);
	
	@Query("SELECT COUNT(s) FROM AddSupportAdmin s")
    long getTotalSupportRequests();
 @Query("SELECT COUNT(s) FROM AddSupportAdmin s WHERE s.status = 'closed'")
    long getCountOfClosedSupportRequests();
 @Query("SELECT COUNT(s) FROM AddSupportAdmin s WHERE s.status IN ('new', 're-open')")
    long getCountOfNewAndReOpenSupportRequests();
 
 @Query("SELECT COUNT(sr) FROM AddSupportAdmin sr WHERE sr.storeCode = :storeCode")
 	long getTotalSupportRequests(@Param("storeCode") String storeCode);
 @Query("SELECT COUNT(sr) FROM AddSupportAdmin sr WHERE sr.storeCode = :storeCode AND sr.status = 'Closed'")
 	long getCountOfClosedSupportRequests(@Param("storeCode") String storeCode);
 @Query("SELECT COUNT(sr) FROM AddSupportAdmin sr WHERE sr.storeCode = :storeCode AND sr.status IN ('New', 'In-Progress', 'Re-Open')")
 	long getCountOfNewAndReOpenSupportRequests(@Param("storeCode") String storeCode);
	
}
