package model;

import helper.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class Country {
    private int id;
    private String country;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;

    public Country(int id, String country, LocalDateTime createDate, String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy) {
        this.id = id;
        this.country = country;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public static Country getById (int countryId) {
        PreparedStatement statement;
        ResultSet res;
        String query = "SELECT * FROM `countries` WHERE `Country_ID` =?";
        Country country = null;

        try {
            statement = JDBC.connection.prepareStatement(query);
            statement.setString(1, String.valueOf(countryId));

            res = statement.executeQuery();

            if(res.next())
            {
                country = new Country(
                        Integer.valueOf(res.getString("Country_ID")),
                        res.getString("Country"),
                        res.getTimestamp("Create_Date").toLocalDateTime(),
                        res.getString("Created_By"),
                        res.getTimestamp("Last_Update").toLocalDateTime(),
                        res.getString("Last_Updated_By")
                        );
            }
        } catch (SQLException ex) {
            System.out.println("error from Country.getById: " + ex);
        }
        return country;
    }

    public String getCountry() {
        return country;
    }

    public int getId() { return id; }

    @Override
    public String toString(){
        return ("#" + Integer.toString(id) + " " + country);
    }
}
