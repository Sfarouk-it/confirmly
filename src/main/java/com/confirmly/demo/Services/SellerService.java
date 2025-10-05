package com.confirmly.demo.Services;

import java.util.Optional;

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

    public Optional<Seller> getSellerByFacebookId(String facebookId) {
        return sellerRepository.findByFacebookId(facebookId);
    }

    public Seller registerSeller(String username, String email, String password) throws Exception {
        if (sellerRepository.existsByUsername(username) || sellerRepository.existsByEmail(email)) {
            throw new Exception("Username or email already exists");
        }

        String hashedPassword = passwordEncoder.encode(password);

        Seller seller = new Seller(username, email, hashedPassword);
        return sellerRepository.save(seller);
    }

    public Seller registerSellerbyFacebook(String username, String email, String FacebookId) throws Exception {
        
        Optional<Seller> existingSeller = sellerRepository.findByUsername(username);

        if (existingSeller.isEmpty()) {
            existingSeller = sellerRepository.findByEmail(email);
        }

        if (existingSeller.isEmpty()) {
            existingSeller = sellerRepository.findByFacebookId(FacebookId);
        }

        if (existingSeller.isPresent()) {
            return existingSeller.get(); 
        }

        Seller seller = new Seller();
        seller.setEmail(email);
        seller.setUsername(username);
        seller.setFacebookId(FacebookId);

        return sellerRepository.save(seller);
    }

}
