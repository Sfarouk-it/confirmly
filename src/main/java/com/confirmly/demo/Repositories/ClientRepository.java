package com.confirmly.demo.Repositories;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.confirmly.demo.model.Client;


@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    // Find clients by loyalty points
    List<Client> findByLoyaltyPointsGreaterThan(int points);
}