package com.confirmly.demo.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.confirmly.demo.model.Seller;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {
    // Find sellers by shop name
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<Seller> findByUsername(String username);
    Optional<Seller> findByEmail(String email);
    Seller findByShopName(String shopName);

}