package com.project.Fabo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.Fabo.entity.ClientProduct;


public interface ClientProductRepository extends JpaRepository<ClientProduct, Long>{

	List<ClientProduct> findAll();

	List<ClientProduct> findByActiveStatusTrue();

	List<ClientProduct> findByStatus(String statusDropdown);

	List<ClientProduct> findByProductRequestTypeContaining(String productRequesttype);

	@Query("SELECT c FROM ClientProduct c WHERE " +
	        "(LOWER(c.storeName) LIKE %:searchTerm% OR " +
	        "LOWER(c.storeCode) LIKE %:searchTerm% OR " +
	        "LOWER(c.productRequestType) LIKE %:searchTerm% OR " +
	        "LOWER(c.storeContact) LIKE %:searchTerm% OR " +
	        "LOWER(c.issueSubject) LIKE %:searchTerm% OR " +
	        "LOWER(c.issueDescription) LIKE %:searchTerm% OR " +
	        "LOWER(c.status) LIKE %:searchTerm% OR " +
	        "LOWER(c.externalComments) LIKE %:searchTerm% OR " +
	        "LOWER(c.commentsToAdmin) LIKE %:searchTerm%) AND " +
	        "c.storeCode = :storeCode") // Add condition for userStoreCode
	List<ClientProduct> findBySearchTermAndStoreCode(@Param("searchTerm") String searchTerm, @Param("storeCode") String userStoreCode);

	List<ClientProduct> findByActiveStatusAndStoreCode(boolean b, String userStoreCode);

	List<ClientProduct> findByProductRequestTypeContainingAndStoreCode(String productRequesttype, String userStoreCode);

	List<ClientProduct> findByStatusAndStoreCode(String statusDropdown, String userStoreCode);

	long countByStatusAndStoreCode(String string, String storeCode);

	long countByActiveStatusAndStoreCode(boolean b, String userStoreCode);
}
