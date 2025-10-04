package com.confirmly.demo.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.confirmly.demo.Repositories.SellerRepository;
import com.confirmly.demo.model.Seller;

@Service
public class SellerDetailsService implements UserDetailsService {

    private final SellerRepository sellerRepository;

    public SellerDetailsService(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        Seller seller = sellerRepository.findByUsername(identifier)
                .orElseGet(() -> sellerRepository.findByEmail(identifier)
                        .orElseThrow(() -> new UsernameNotFoundException("Seller not found")));

        return org.springframework.security.core.userdetails.User.builder()
                .username(seller.getUsername()) 
                .password(seller.getPassword()) 
                .roles("SELLER")
                .build();
    }
}