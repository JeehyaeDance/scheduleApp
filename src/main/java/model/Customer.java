package model;

import java.time.LocalDateTime;

public class Customer {
    private int id;
    private String name;
    private String address;
    private String postalCode;
    private String phone;
    private LocalDateTime createdDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;
    private int divisionId;

    public Customer(int id, String name, String address, String postalCode, String phone,LocalDateTime createdDate, String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy, int divisionId ) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.divisionId = divisionId;
    }

    public void updateCustomer(String name, String address, String postalCode, String phone) {
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
    }

}
