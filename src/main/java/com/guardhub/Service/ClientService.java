package com.guardhub.Service;

import com.guardhub.Enum.City;
import com.guardhub.Model.Client;

import java.util.List;

public interface ClientService {
    public List<Client> findAll();
    public List<Client> findAllWithName(String clientName);
    public List<Client> findAllWithCity(City city);

    public Client findClientByClientName(String clientName);
    public Client findById(Long clientId);
    public Client addClient(Client client);
    public Client updateClient(Client client);
    public void deleteClient(Long clientId);
}
