package com.confirmly.demo.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "businesses")
public class Business {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String businessname;
    private String businesstype;
    private String businessfield;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FacebookAccount> facebookAccounts;

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FacebookPage> facebookPages;


    public Business() {}

    public Business(String businessname, String businesstype, String businessfield) {
        this.businessname = businessname;
        this.businesstype = businesstype;
        this.businessfield = businessfield;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBusinessname() {
        return businessname;
    }
    
    public void setBusinessname(String businessname) {
        this.businessname = businessname;
    }

    public String getBusinesstype() {
        return businesstype;
    }

    public void setBusinesstype(String businesstype) {
        this.businesstype = businesstype;
    }

    public String getBusinessfield() {
        return businessfield;
    }

    public void setBusinessfield(String businessfield) {
        this.businessfield = businessfield;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public List<FacebookAccount> getFacebookAccounts() {
        return facebookAccounts;
    }

    public void setFacebookAccounts(List<FacebookAccount> facebookAccounts) {
        this.facebookAccounts = facebookAccounts;
    }

    public void addFacebookAccount(FacebookAccount facebookAccount) {
        facebookAccounts.add(facebookAccount);
    }


}
