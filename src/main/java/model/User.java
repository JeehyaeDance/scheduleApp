package model;

import helper.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class User {
    private int id;
    private String name;
    private String password;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;

    public User(int id, String name, String password, LocalDateTime createDate, String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public static User getById(int userId) {
        PreparedStatement statement;
        ResultSet res;
        String query = "SELECT * FROM `users` WHERE `User_ID` =?";
        User user = null;

        try {
            statement = JDBC.connection.prepareStatement(query);
            statement.setString(1, String.valueOf(userId));

            res = statement.executeQuery();

            if(res.next())
            {
                user = new User(
                        Integer.valueOf(res.getString("User_ID")),
                        res.getString("User_Name"),
                        res.getString("Password"),
                        res.getTimestamp("Create_Date").toLocalDateTime(),
                        res.getString("Created_By"),
                        res.getTimestamp("Last_Update").toLocalDateTime(),
                        res.getString("Last_Updated_By")
                );
            }
        } catch (SQLException ex) {
            System.out.println("error from User.getById: " + ex);
        }
        return user;
    }

    public String getName() {
        return name;
    }
}
