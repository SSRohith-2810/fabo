package com.project.Fabo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.Fabo.entity.AddProductAdmin;
 

public interface AddProductAdminRepository extends JpaRepository<AddProductAdmin, Long>{
	 List<AddProductAdmin> findByActiveStatusTrue();

	List<AddProductAdmin> findByProductRequestTypeContaining(String invoicedropdown);

	List<AddProductAdmin> findByStatus(String statusDropdown);

	List<AddProductAdmin> findByProductRequestTypeContainingAndStatus(String invoicedropdown, String statusDropdown);
	
	@Query("SELECT a FROM AddProductAdmin a WHERE " +
	        "LOWER(a.storeName) LIKE %:searchTerm% OR " +
	        "LOWER(a.storeCode) LIKE %:searchTerm% OR " +
	        "LOWER(a.productRequestType) LIKE %:searchTerm% OR " +
	        "LOWER(a.storeContact) LIKE %:searchTerm% OR " +
	        "LOWER(a.issueSubject) LIKE %:searchTerm% OR " +
	        "LOWER(a.issueDescription) LIKE %:searchTerm% OR " +
	        "LOWER(a.status) LIKE %:searchTerm% OR " +
	        "LOWER(a.internalComments) LIKE %:searchTerm% OR " +
	        "LOWER(a.commentsToClient) LIKE %:searchTerm% OR " +
	        "LOWER(a.commentsFromClient) LIKE %:searchTerm%")
	List<AddProductAdmin> findBySearchTerm(@Param("searchTerm") String searchTerm);

	@Query("SELECT DISTINCT a.storeCode FROM AddProductAdmin a WHERE a.activeStatus=true") 
	List<String> findDistinctStoreCodes();

	@Query("SELECT DISTINCT c.storeCode FROM Client c WHERE c.activeStatus = true")
	 List<String> findActiveStoreCodes();

	long countByStatus(String string);

	long countByStoreCode(String storeCode);

			@Query("SELECT COUNT(p) FROM AddProductAdmin p")
			long getTotalProductRequests();    
		@Query("SELECT COUNT(p) FROM AddProductAdmin p WHERE p.status = 'new'")
			long getCountOfClosedProductRequests();    
		@Query("SELECT COUNT(p) FROM AddProductAdmin p WHERE p.status IN ('closed', 're-open', 'in-progress')")
			long getCountOfClosedAndInProgressProductRequests();
		
		@Query("SELECT COUNT(pr) FROM AddProductAdmin pr WHERE pr.storeCode = :storeCode")
			long getTotalProductRequests(@Param("storeCode") String storeCode);
		@Query("SELECT COUNT(pr) FROM AddProductAdmin pr WHERE pr.storeCode = :storeCode AND pr.status = 'New'")
			long getCountOfClosedProductRequests(@Param("storeCode") String storeCode);
		@Query("SELECT COUNT(pr) FROM AddProductAdmin pr WHERE pr.storeCode = :storeCode AND pr.status IN ('Closed', 'Re-Open', 'In-Progress')")
			long getCountOfClosedAndInProgressProductRequests(@Param("storeCode") String storeCode);

}
