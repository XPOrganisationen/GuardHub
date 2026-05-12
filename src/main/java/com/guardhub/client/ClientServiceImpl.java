package com.guardhub.client;

import com.guardhub.exceptions.EntityDoesNotExistException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Client findById(Long clientId) {
        return clientRepository.findById(clientId).orElseThrow(() -> new EntityDoesNotExistException("No client with ID " + clientId + " exists"));
    }

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public Client addClient(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public Client updateClient(Long clientId, Client client) {
        if (!clientRepository.existsById(clientId)) {
            throw new EntityDoesNotExistException("No client with ID " + clientId + " exists");
        }

        return clientRepository.save(client);
    }

    @Override
    public void deleteClient(Long clientId) {
        if (!clientRepository.existsById(clientId)) {
            throw new EntityDoesNotExistException("No client with ID " + clientId + " exists");
        }

        clientRepository.deleteById(clientId);
    }
}
