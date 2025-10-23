package com.confirmly.demo.Services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.confirmly.demo.DTO.facebookDTOs.FacebookUserInfo;
import com.confirmly.demo.Repositories.FacebookAccountRepository;
import com.confirmly.demo.model.FacebookAccount;

@Service
public class FacebookAccountsService {

    @Autowired
    private FacebookAccountRepository facebookAccountRepository;

    public Optional<FacebookAccount> getFacebookAccountByFacebookId(String facebookId) {
        return facebookAccountRepository.findByFacebookId(facebookId);
    }


    public FacebookAccount savefacebookAccount(FacebookUserInfo userInfo ,String accessToken, String longLivedToken, Long tokenExpiresAt) {
        FacebookAccount account = new FacebookAccount();
        account.setFacebookId(userInfo.getId());
        account.setName(userInfo.getName());
        account.setEmail(userInfo.getEmail());
        account.setAccessToken(accessToken);
        account.setLongLivedToken(longLivedToken);
        account.setTokenExpiresAt(System.currentTimeMillis() + (tokenExpiresAt * 1000));

        return facebookAccountRepository.save(account);
    }
}
