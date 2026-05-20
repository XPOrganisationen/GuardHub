package com.guardhub;

import com.guardhub.city.CityRepository;
import com.guardhub.city.City;
import com.guardhub.client.Client;
import com.guardhub.client.ClientRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class ClientRepositoryTests {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CityRepository cityRepository;

    @Test
    public void findAllClientsReturnsClients() {
        City city = new City("Copenhagen");

        cityRepository.save(city);

        Client client = new Client("example-street 24", city, "example@example.com", null, "Example Example", "12131415");

        clientRepository.save(client);

        List<Client> clients = clientRepository.findAll();

        Assertions.assertEquals(1, clients.size());
        Assertions.assertEquals("Example Example", clients.getFirst().getName());
    }

    @Test
    public void findAllByNameContainingIgnoreCaseFindsMatchingUsers() {
        City city1 = new City("Copenhagen");
        City city2 = new City("Odense");
        City city3 = new City("Esbjerg");

        cityRepository.saveAll(List.of(city1, city2, city3));

        Client client1 = new Client("example-street 24", city1, "example1@example.com", null, "Example Example", "12131415");
        Client client2 = new Client("example-street 25", city2, "example2@example.com", null, "Example Example", "12131416");
        Client client3 = new Client("example-street 26", city3, "example3@example.com", null, "Exception Exception3", "12131417");

        clientRepository.saveAll(List.of(client1, client2, client3));


        List<Client> result = clientRepository.findAllByNameContainingIgnoreCase("Example");

        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.stream().allMatch(user -> user.getName().equals("Example Example")));
    }
}
