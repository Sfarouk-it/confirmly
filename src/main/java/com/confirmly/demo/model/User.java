package com.confirmly.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import jakarta.persistence.*;

// Base class
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Constructors
    public User() {}
    

    // Getters and setters
    public Long getId() { return id; }
}
