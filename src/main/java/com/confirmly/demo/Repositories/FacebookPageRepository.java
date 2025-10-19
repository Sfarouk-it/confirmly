package com.confirmly.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.confirmly.demo.model.FacebookPage;

public interface FacebookPageRepository extends JpaRepository<FacebookPage, Long> {
    
}
