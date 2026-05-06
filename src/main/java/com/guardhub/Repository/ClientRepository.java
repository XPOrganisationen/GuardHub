package com.guardhub.Repository;

import com.guardhub.Model.Client;
import com.guardhub.Enum.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ClientRepository extends JpaRepository <Client, Long> {
    Client findClientByClientName(String clientName);

    List<Client> findAllClientsWithClientName(String clientName);
    List<Client> findAllClientsWithCity(City city);
}
