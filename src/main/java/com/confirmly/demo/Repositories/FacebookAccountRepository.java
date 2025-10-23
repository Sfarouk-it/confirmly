package com.confirmly.demo.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.confirmly.demo.model.FacebookAccount;

public interface FacebookAccountRepository extends JpaRepository<FacebookAccount, Long> {
    Optional<FacebookAccount> findByFacebookId(String facebookId);
}
