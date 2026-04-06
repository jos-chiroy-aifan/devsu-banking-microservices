package com.devsu.customer.repository;

import com.devsu.customer.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByClientId(String clientId);

    Optional<Client> findByIdentification(String identification);

    boolean existsByClientId(String clientId);

    boolean existsByIdentification(String identification);
}