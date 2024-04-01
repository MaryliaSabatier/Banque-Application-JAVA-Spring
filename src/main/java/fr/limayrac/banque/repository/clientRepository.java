package fr.limayrac.banque.repository;

import org.springframework.stereotype.Repository;

import fr.limayrac.banque.model.Client;

import org.springframework.data.repository.CrudRepository;

@Repository
public interface clientRepository extends CrudRepository<Client, Integer>
{
    
}
