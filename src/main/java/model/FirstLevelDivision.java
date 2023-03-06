package model;

import helper.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class FirstLevelDivision   {
    private int id;
    private String division;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;
    private int countryId;

    public FirstLevelDivision (int id, String division, LocalDateTime createDate, String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy, int countryId) {
        this.id = id;
        this.division = division;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.countryId = countryId;
    }

    public static FirstLevelDivision getById(int firstLevelDivisionId) {
        PreparedStatement statement;
        ResultSet res;
        String query = "SELECT * FROM `first_level_divisions` WHERE `Division_ID` =?";
        FirstLevelDivision division = null;

        try {
            statement = JDBC.connection.prepareStatement(query);
            statement.setString(1, String.valueOf(firstLevelDivisionId));

            res = statement.executeQuery();

            if(res.next())
            {
                division = new FirstLevelDivision(
                        Integer.valueOf(res.getString("Division_ID")),
                        res.getString("Division"),
                        res.getTimestamp("Create_Date").toLocalDateTime(),
                        res.getString("Created_By"),
                        res.getTimestamp("Last_Update").toLocalDateTime(),
                        res.getString("Last_Updated_By"),
                        Integer.valueOf(res.getString("COUNTRY_ID"))
                    );
            }
        } catch (SQLException ex) {
            System.out.println("error from FirstLevelDivision.getById: " + ex);
        }
        return division;
    }

    public Country country () {
        return Country.getById(this.countryId);
    }

    public String getDivision() {
        return division;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString(){
        return ("#" + Integer.toString(id) + " " + division);
    }
}
