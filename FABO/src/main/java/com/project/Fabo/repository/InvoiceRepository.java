package com.project.Fabo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.Fabo.entity.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long>{
	
	
	List<Invoice> findById(long id);
	
	 @Query("SELECT DISTINCT c.storeCode FROM Client c")
	List<String> findDistinctStoreCodes(); 
    
 // Custom JPQL query to find invoices by searching for a term in various fields
 	@Query("SELECT i FROM Invoice i WHERE " +
 	       "LOWER(i.invoiceType) LIKE %:searchTerm% OR " +
 	       "LOWER(i.storeCode) LIKE %:searchTerm% OR " +
 	       "LOWER(i.storeName) LIKE %:searchTerm% OR " +
 	       "LOWER(i.invoiceAmount) LIKE %:searchTerm% OR " +
 	       "LOWER(i.invoiceStatus) LIKE %:searchTerm% AND i.activeStatus = true")
 	List<Invoice> findBySearchTerm(@Param("searchTerm") String searchTerm);

 	// Method to find distinct invoice types
 	@Query("SELECT DISTINCT i.invoiceType FROM Invoice i WHERE i.activeStatus = true")
 	List<String> findDistinctInvoiceType();

 	// Filter for invoice type
 	@Query("SELECT i FROM Invoice i WHERE i.invoiceType = :invoiceType AND i.activeStatus = true")
 	List<Invoice> findByInvoiceType(@Param("invoiceType") String invoiceType);

 	// Retrieve all invoices with active status
 	List<Invoice> findByActiveStatusTrue();

 	// Filter for distinct invoice numbers
 	@Query("SELECT DISTINCT i.invoiceNumber FROM Invoice i WHERE i.activeStatus = true")
 	List<String> findDistinctInvoiceNumbers();
 	
 	@Query("SELECT i FROM Invoice i WHERE " +
 	       "LOWER(i.invoiceType) LIKE %:searchTerm% OR " +
 	       "LOWER(i.storeCode) LIKE %:searchTerm% OR " +
 	       "LOWER(i.storeName) LIKE %:searchTerm% OR " +
 	       "LOWER(i.invoiceAmount) LIKE %:searchTerm% OR " +
 	       "LOWER(i.invoiceStatus) LIKE %:searchTerm% OR " +
 	       "LOWER(i.invoiceNumber) LIKE %:searchTerm% AND i.activeStatus = true AND " +
 	       "i.storeCode = :storeCode")
 	List<Invoice> findBySearchTermAndStoreCodeAndActiveStatusTrue(String searchTerm, @Param("storeCode") String userStoreCode);

	List<Invoice> findByInvoiceTypeAndStoreCodeAndActiveStatusTrue(String invoiceType, String userStoreCode);

	List<Invoice> findByStoreCodeAndActiveStatusTrue(String userStoreCode);
	
	 @Query("SELECT DISTINCT c.storeCode FROM Client c WHERE c.activeStatus = true")
	 List<String> findActiveStoreCodes();
	 
	 @Query("SELECT DISTINCT i.storeCode FROM Invoice i WHERE i.activeStatus = true")
	 List<String> findDistinctActiveStoreCodes();
	 
	 @Query("SELECT COALESCE(SUM(CAST(REPLACE(i.invoiceAmount, ',', '') AS double)), 0) FROM Invoice i")
	 double getTotalAmountOfInvoices();
	 
	 @Query("SELECT COALESCE(SUM(CAST(REPLACE(i.invoiceAmount, ',', '') AS double)), 0) FROM Invoice i WHERE i.invoiceStatus = 'paid'")
	 double getTotalAmountOfPaidInvoices();
	 
	 @Query("SELECT COALESCE(SUM(CAST(REPLACE(i.invoiceAmount, ',', '') AS double)), 0) FROM Invoice i WHERE i.invoiceStatus = 'pending'")
	 double getTotalAmountOfPendingInvoices();
	 
	 @Query("SELECT COALESCE(SUM(CAST(REPLACE(i.invoiceAmount, ',', '') AS double)), 0) FROM Invoice i WHERE i.storeCode = :storeCode")
	 Double getTotalAmountOfInvoices(@Param("storeCode") String storeCode);

	 @Query("SELECT COALESCE(SUM(CAST(REPLACE(i.invoiceAmount, ',', '') AS double)), 0) FROM Invoice i WHERE i.storeCode = :storeCode AND i.invoiceStatus = 'Paid'")
	 Double getTotalAmountOfPaidInvoices(@Param("storeCode") String storeCode);

	 @Query("SELECT COALESCE(SUM(CAST(REPLACE(i.invoiceAmount, ',', '') AS double)), 0) FROM Invoice i WHERE i.storeCode = :storeCode AND i.invoiceStatus = 'Pending'")
	 Double getTotalAmountOfPendingInvoices(@Param("storeCode") String storeCode);

}
