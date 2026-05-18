package com.guardhub.client;

import com.guardhub.user.User;

import java.util.List;

public interface ClientService {
    public Client findById(Long clientId);
    public List<Client> findAll();
    public List<Client> findAllByName(String name);
    public Client addClient(Client client);
    public Client updateClient(Long clientId, Client client);
    public void deleteClient(Long clientId);
}
