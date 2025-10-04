package com.confirmly.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.confirmly.demo.model.Messages;

@Repository
public interface MessageRepository extends JpaRepository<Messages, Long> {
}