package com.project.Fabo.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.project.Fabo.entity.Client;
import com.project.Fabo.repository.ClientRepository;
import com.project.Fabo.service.ClientService;

@Service
public class ClientServiceImpl implements ClientService{

	private ClientRepository clientRepository;
	
	public ClientServiceImpl(ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}

	@Override
	public List<Client> getAllClients() {
		return clientRepository.findAll();
	}

	@Override
	public Client saveClient(Client client) {
		return clientRepository.save(client);
	}

	@Override
	public Client getClientById(Long id) {
		return clientRepository.findById(id).get();
	}

	@Override
	public Client updateClient(Client client) {
		return clientRepository.save(client);
	}

	@Override
	public void deleteClientById(Long id) {
		Optional<Client> clientOptional = clientRepository.findById(id);
		
		 if (clientOptional.isPresent()) {
	            Client client = clientOptional.get();
	            client.setActiveStatus(false); // Marking as inactive

	            clientRepository.save(client);
	        } else {
	            throw new IllegalArgumentException("Client ID not found: " + id);
	        }	
	}
	
    
    public List<Client> getDistinctStatesAndCities() {
        List<Client> allLocations = clientRepository.findAll();

        return allLocations.stream()
            .map(location -> {
                location.setState(location.getState().toLowerCase().trim());
                location.setCity(location.getCity().toLowerCase().trim());
                return location;
            })
            .distinct()
            .collect(Collectors.toList());
    }

	@Override
	public Optional<Client> getClientByEmail(String email) {
		return clientRepository.findByEmail(email);
	}

	@Override
	public Client getClientByStoreCode(String storeCode) {
		return clientRepository.findByStoreCode(storeCode);
	}

	@Override
	public boolean isStoreCodeDuplicate(String storeCode) {
	    // Check if the store code already exists in the database
	    Client existingClient = clientRepository.findByStoreCode(storeCode);
	    return existingClient != null;
	}

    
}
