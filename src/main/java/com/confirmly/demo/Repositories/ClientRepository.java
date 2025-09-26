package com.confirmly.demo.Repositories;


import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.confirmly.demo.model.Client;


@Repository
public interface ClientRepository extends JpaRepository<Client, String> {
}
