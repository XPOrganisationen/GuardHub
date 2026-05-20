package com.guardhub;

import com.guardhub.city.City;
import com.guardhub.client.Client;
import com.guardhub.client.ClientRepository;
import com.guardhub.client.ClientServiceImpl;
import com.guardhub.exceptions.EntityDoesNotExistException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTests {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    @Test
    void findAllClientsReturnsClientsFromRepository() {
        City city = new City("Copenhagen");


        List<Client> clients = List.of(
                new Client("example-street 24", city, "example1@example.com", null, "Example Example1", "12131415"),
                new Client("example-street 25", city, "example2@example.com", null, "Example Example2", "12131416")
        );

        when(clientRepository.findAll()).thenReturn(clients);

        List<Client> result = clientService.findAll();

        assertEquals(2, result.size());
        assertEquals("Example Example1", result.getFirst().getName());

        verify(clientRepository, times(1)).findAll();
    }

    @Test
    void deleteClientDeletesIfClientExists() {
        Long clientId = 1L;

        when(clientRepository.existsById(clientId)).thenReturn(true);

        clientService.deleteClient(clientId);

        verify(clientRepository, times(1)).existsById(clientId);
        verify(clientRepository, times(1)).deleteById(clientId);
    }

    @Test
    void deleteClientThrowsIfClientDoesNotExist() {
        Long clientId = 99L;

        when(clientRepository.existsById(clientId)).thenReturn(false);

        assertThrows(EntityDoesNotExistException.class,
                () -> clientService.deleteClient(clientId));

        verify(clientRepository, times(1)).existsById(clientId);
        verify(clientRepository, never()).deleteById(any());
    }
}