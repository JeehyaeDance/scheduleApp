package model;

import helper.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Contact {
    private int id;
    private String name;
    private String email;

    public Contact (int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public static Contact getById(int contactId) {
        PreparedStatement statement;
        ResultSet res;
        String query = "SELECT * FROM `contacts` WHERE `Contact_ID` =?";
        Contact contact = null;

        try {
            statement = JDBC.connection.prepareStatement(query);
            statement.setString(1, String.valueOf(contactId));

            res = statement.executeQuery();

            if(res.next())
            {
                contact = new Contact(
                        Integer.valueOf(res.getString("Contact_ID")),
                        res.getString("Contact_Name"),
                        res.getString("Email")
                );
            }
        } catch (SQLException ex) {
            System.out.println("error from Contact.getById: " + ex);
        }
        return contact;
    }

    public String getName() {
        return name;
    }
}
