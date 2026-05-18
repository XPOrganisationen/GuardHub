package com.guardhub;

import com.guardhub.city.City;
import com.guardhub.city.CityRepository;
import com.guardhub.client.Client;
import com.guardhub.client.ClientRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;

@DataJpaTest
public class ClientRepositoryTests {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CityRepository cityRepository;

    @BeforeEach
    public void setUp() {
        City city1 = new City("Copenhagen");
        City city2 = new City("Odense");
        City city3 = new City("Esbjerg");
        cityRepository.save(city1);
        cityRepository.save(city2);
        cityRepository.save(city3);

        Client client1 = new Client("ExampleRoad1, ExampleCity1, ExampleCountry1", city1, "a1@example.com", null, "ExampleCompany1", "12345678");
        Client client2 = new Client("ExampleRoad2, ExampleCity2, ExampleCountry2", city2, "a2@example.com", null, "ExampleCompany2", "22345678");
        Client client3 = new Client("ExampleRoad3, ExampleCity3, ExampleCountry3", city3, "a3@example.com", null, "ExampleCompany3", "32345678");
        clientRepository.save(client1);
        clientRepository.save(client2);
        clientRepository.save(client3);
    }

    @Test
    public void findAllFindsAllClients() {
        List<Client> clients = clientRepository.findAll();

        Assertions.assertEquals(3, clients.size());
    }

}
