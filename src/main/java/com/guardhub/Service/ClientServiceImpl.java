package com.guardhub.Service;

import com.guardhub.Exceptions.EntityDoesNotExistException;
import com.guardhub.Model.Client;
import com.guardhub.Enum.City;
import com.guardhub.Repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public List<Client> findAllWithName(String clientName) {
        return clientRepository.findAllClientsWithClientName(clientName);
    }

    @Override
    public List<Client> findAllWithCity(City city) {
        return clientRepository.findAllClientsWithCity(city);
    }

    @Override
    public Client findById(Long clientId) {
        var client =  clientRepository.findById(clientId);
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new EntityDoesNotExistException("No Client found with ID " + clientId));
    }

    @Override
    public Client findClientByClientName(String clientName) {
        return clientRepository.findClientByClientName(clientName);
    }

    @Override
    public List<City> findAllCities() {
        return clientRepository.findAllCities();
    }

    @Override
    public Client addClient(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public Client updateClient(Client client) {
        if (!clientRepository.existsById(client.getClientId())) {
            throw new EntityDoesNotExistException("No client found with ID " + client.getClientId());
        }

        return clientRepository.save(client);
    }

    @Override
    public void deleteClient(Long clientId) {
        if (!clientRepository.existsById(clientId)) {
            throw new EntityDoesNotExistException("No client found with ID " + clientId);
        }

        clientRepository.deleteById(clientId);
    }



}
