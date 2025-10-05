package com.confirmly.demo.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.confirmly.demo.DTO.BusinessDTO;
import com.confirmly.demo.Repositories.BusinessRepository;
import com.confirmly.demo.model.Business;

@Service
public class BusinessService {

    @Autowired
    private BusinessRepository businessRepository;

    public Business saveBusiness(BusinessDTO business) {
        Business newBusiness = new Business(business.getBusinessname(), business.getBusinesstype(), business.getBusinessfield());
        return businessRepository.save(newBusiness);

    }
}

