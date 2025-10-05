package com.confirmly.demo.Repositories;

import org.springframework.stereotype.Repository;
import com.confirmly.demo.model.Business;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {

    
}
