package com.confirmly.demo.DTO;

public class BusinessDTO {

    private Long id;
    private String businessname;
    private String businesstype;
    private String businessfield;

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
}
