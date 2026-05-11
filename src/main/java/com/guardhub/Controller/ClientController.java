package com.guardhub.Controller;

import com.guardhub.Enum.City;
import com.guardhub.Model.Client;
import com.guardhub.Service.ClientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/clients/")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public List<Client> getAllClients() {
        return this.clientService.findAll();
    }

    @GetMapping("{clientId}")
    public Client getClientById(@PathVariable Long clientId) {
        return this.clientService.findById(clientId);
    }

    @GetMapping("by-name/{clientName}")
    public List<Client> getClientByName(@PathVariable String clientName) {
        return this.clientService.findAllWithName(clientName);
    }

    @GetMapping("by-city/{city}")
    public List<Client> getClientByCity(@PathVariable City city) {
        return this.clientService.findAllWithCity(city);
    }

    @PostMapping
    public Client addClient(@RequestBody Client client) {
        return this.clientService.addClient(client);
    }

    @PutMapping
    public Client updateClient(@RequestBody Client client) {
        return this.clientService.updateClient(client);
    }

    @DeleteMapping("{clientId}")
    public void deleteClient(@PathVariable Long clientId) {
        this.clientService.deleteClient(clientId);
    }
}
