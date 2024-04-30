package com.project.Fabo.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.Fabo.entity.Client;

public interface ClientRepository extends JpaRepository<Client, Long>{
    
    List<Client> findByStateAndCity(String state, String city);
    
    List<Client> findByState(String state);

    List<Client> findByCity(String city);
    
    @Query("SELECT c FROM Client c WHERE " +
            "(LOWER(c.state) = LOWER(:state) or :state = 'All') and " +
            "(LOWER(c.storeName) LIKE %:searchTerm% OR " +
            "LOWER(c.city) LIKE %:searchTerm% OR " +
            "c.storeCode LIKE %:searchTerm% OR " +
            "LOWER(c.ownerContact) LIKE %:searchTerm% OR " +
            "c.storeContact LIKE %:searchTerm%)")
    List<Client> findBySearchTerm(@Param("searchTerm") String searchTerm, @Param("state") String state);
    
    @Query("SELECT DISTINCT c.state FROM Client c WHERE c.activeStatus = true")
    List<String> findDistinctStates();
    
    @Query("SELECT DISTINCT c.city FROM Client c WHERE c.activeStatus = true")
    List<String> findDistinctCities();
    
	Optional<Client> findByEmail(String email);

	Client findByStoreCode(String storeCode);

	List<Client> findByActiveStatusTrue();
	
	
}
