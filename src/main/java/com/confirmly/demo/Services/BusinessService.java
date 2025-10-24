package com.confirmly.demo.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.confirmly.demo.DTO.BusinessDTO;
import com.confirmly.demo.Repositories.BusinessRepository;
import com.confirmly.demo.model.Business;
import com.confirmly.demo.model.Seller;

@Service
public class BusinessService {

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private SellerService sellerService;

    public Business saveBusiness(BusinessDTO business ,String username) {
        Seller seller = sellerService.getSellerByUsername(username).orElse(null);
        if (seller == null) {
            seller = sellerService.getSellerByEmail(username).orElse(null);
        }
        if (seller == null) {
            throw new RuntimeException("Seller not found with username: " + username);
        }
        Business newBusiness = new Business(business.getBusinessname(), business.getBusinesstype(), business.getBusinessfield());

        seller.addBusiness(newBusiness);
        
        return businessRepository.save(newBusiness);
    }

    public Business getBusinessById(Long id) {
        return businessRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Business not found with id: " + id));
    }
}

