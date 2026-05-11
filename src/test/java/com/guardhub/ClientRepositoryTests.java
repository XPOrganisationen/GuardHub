package com.guardhub;

import com.guardhub.Enum.City;
import com.guardhub.Model.Client;
import com.guardhub.Repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
public class ClientRepositoryTests {
    @Autowired
    private ClientRepository clientRepository;

    @Test
    void findAllByBeginningOfClientName() {
        Client NovoNordic = clientRepository.findById(1L).get();
        List<Client> clients = clientRepository.findAllClientsWithClientName("Novo");
        org.junit.jupiter.api.Assertions.assertEquals(NovoNordic, clients.getFirst());
        org.junit.jupiter.api.Assertions.assertEquals(1, clients.size());
    }

    @Test
    void findAllByEndOfClientName() {
        Client NovoNordic = clientRepository.findById(1L).get();
        List<Client> clients = clientRepository.findAllClientsWithClientName("Nordic");
        org.junit.jupiter.api.Assertions.assertEquals(NovoNordic, clients.getFirst());
        org.junit.jupiter.api.Assertions.assertEquals(1, clients.size());
    }

    @Test
    void findAllByClientNameReturnsEmptyList() {
        org.junit.jupiter.api.Assertions.assertEquals(List.of(), clientRepository.findAllClientsWithClientName("knjdasjka"));
    }

    @Test
    void findAllByCity() {
        Client NovoNordic = clientRepository.findById(1L).get();
        List<Client> clients = clientRepository.findAllClientsWithCity(City.COPENHAGEN);
        org.junit.jupiter.api.Assertions.assertEquals(NovoNordic, clients.getFirst());
        org.junit.jupiter.api.Assertions.assertEquals(1, clients.size());
    }

    @Test
    public void findAllCitiesReturnsAllCitiesFromTestDb() {
        List<City> expectedCities =  List.of(
                City.COPENHAGEN,
                City.ROSKILDE,
                City.ODENSE,
                City.AARHUS,
                City.AALBORG,
                City.ESBJERG
        );

        org.junit.jupiter.api.Assertions.assertTrue(clientRepository.findAllCities().containsAll(expectedCities));
    }
}



