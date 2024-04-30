package com.project.Fabo.service;

import java.util.List;
import java.util.Optional;

import com.project.Fabo.entity.Client;

public interface ClientService {
	List<Client> getAllClients();
	
	Client saveClient(Client Client);
	
	Client getClientById(Long id);
	
	Client updateClient(Client Client);
	
	void deleteClientById(Long id);

	Optional<Client> getClientByEmail(String email);

	Client getClientByStoreCode(String storeCode);

	boolean isStoreCodeDuplicate(String storeCode);
}
