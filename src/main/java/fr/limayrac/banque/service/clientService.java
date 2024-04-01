package fr.limayrac.banque.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.limayrac.banque.model.*;
import fr.limayrac.banque.repository.clientRepository;
import lombok.Data;
import java.util.*;

@Data
@Service
public class clientService {
    @Autowired
    private clientRepository clientRepository;

    public Optional<Client> getClient(final Integer id)
    {
        return clientRepository.findById(id);
    }

    public Iterable<Client> getClients()
    {
        return clientRepository.findAll();
    }

    public void deleteClient(final Integer id)
    {
        clientRepository.deleteById(id);
    }

    public Client saveClient(Client client)
    {
        return clientRepository.save(client);
    }
}
