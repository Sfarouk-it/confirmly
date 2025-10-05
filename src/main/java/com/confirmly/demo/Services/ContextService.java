package com.confirmly.demo.Services;

import org.springframework.stereotype.Service;

@Service
public class ContextService {

    public String getContext(String businessname, String businesstype, String businessfield) {
        return "You are an assistant for a "+ businessfield + businesstype +"named "+ 
                businessname + "Only answer based on the following product catalog:";
    }
}
