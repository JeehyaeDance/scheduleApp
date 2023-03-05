package model;

import helper.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public Customer(int id, String name, String address, String postalCode, String phone,LocalDateTime createdDate, String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy, int divisionId) {
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

    public FirstLevelDivision firstLevelDivision() {
        return FirstLevelDivision.getById(this.divisionId);
    }

    public static Customer getById(int customerId) {
        PreparedStatement statement;
        ResultSet res;
        String query = "SELECT * FROM `customers` WHERE `Customer_ID` =?";
        Customer customer = null;

        try {
            statement = JDBC.connection.prepareStatement(query);
            statement.setString(1, String.valueOf(customerId));

            res = statement.executeQuery();

            if(res.next())
            {
                customer = new Customer(
                        Integer.valueOf(res.getString("Customer_ID")),
                        res.getString("Customer_Name"),
                        res.getString("Address"),
                        res.getString("Postal_Code"),
                        res.getString("Phone"),
                        res.getTimestamp("Create_Date").toLocalDateTime(),
                        res.getString("Created_By"),
                        res.getTimestamp("Last_Update").toLocalDateTime(),
                        res.getString("Last_Updated_By"),
                        Integer.valueOf(res.getString("Division_ID"))
                );
            }
        } catch (SQLException ex) {
            System.out.println("error from Customer.getById: " + ex);
        }
        return customer;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFullAddress() {
        FirstLevelDivision division = firstLevelDivision();
        return address + ", " + division.getDivision() + ", " + division.country().getCountry();
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

}
