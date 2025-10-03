package com.confirmly.demo.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.confirmly.demo.Repositories.SellerRepository;
import com.confirmly.demo.model.Seller;

@Service
public class SellerService {

    @Autowired
    private final SellerRepository sellerRepository;
    
    private final BCryptPasswordEncoder passwordEncoder;

    public SellerService(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Seller registerSeller(String username, String email, String password) throws Exception {
        if (sellerRepository.existsByUsername(username) || sellerRepository.existsByEmail(email)) {
            throw new Exception("Username or email already exists");
        }

        String hashedPassword = passwordEncoder.encode(password);

        Seller seller = new Seller(username, email, hashedPassword);
        return sellerRepository.save(seller);
    }
}
