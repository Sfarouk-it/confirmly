package com.confirmly.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.confirmly.demo.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
