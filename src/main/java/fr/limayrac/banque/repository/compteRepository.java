package fr.limayrac.banque.repository;

import org.springframework.stereotype.Repository;

import fr.limayrac.banque.model.Compte;

import org.springframework.data.repository.CrudRepository;

@Repository
public interface compteRepository extends CrudRepository<Compte, Integer>
{
    
}
