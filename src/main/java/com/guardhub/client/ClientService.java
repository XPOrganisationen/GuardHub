package com.guardhub.client;

import java.util.List;

public interface ClientService {
    public Client findById(Long clientId);
    public List<Client> findAll();
    public Client addClient(Client client);
    public Client updateClient(Long clientId, Client client);
    public void deleteClient(Long clientId);
}
