package com.confirmly.demo.Services;

import org.springframework.beans.factory.annotation.Autowired;

import com.confirmly.demo.DTO.facebookDTOs.FacebookPageDTO;
import com.confirmly.demo.Repositories.FacebookPageRepository;
import com.confirmly.demo.model.FacebookAccount;
import com.confirmly.demo.model.FacebookPage;

public class FacebookPagesService {

    @Autowired
    private FacebookPageRepository facebookPageRepository;

    public FacebookPage getpagebypageID(String pageId) {
        FacebookPage page = facebookPageRepository.findById(Long.parseLong(pageId)).orElse(null);
        return page;
    }

    public FacebookPage saveFacebookPage(FacebookPageDTO pagedto, FacebookAccount facebookAccount) {
        FacebookPage page = new FacebookPage();
        
        page.setPageId(pagedto.getPageId());
        page.setPageName(pagedto.getName());
        page.setPageAccessToken(pagedto.getAccessToken());
        page.setCategory(pagedto.getCategory());
        page.setTasks(pagedto.getTasks());
        page.setFacebookAccount(facebookAccount);
        
        return facebookPageRepository.save(page);
    }
}
