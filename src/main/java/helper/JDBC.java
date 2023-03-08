package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class JDBC {
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER";
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String userName = "sqlUser";
    private static final String password = "Passw0rd!";
    public static Connection connection;

    public static void openConnection() {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(jdbcUrl, userName, password);
            System.out.println("connection successful!");
        }
        catch (Exception e) {
            System.out.println("connection error: " + e.getMessage());
        }
    }

    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("connection closed!");
        } catch (Exception e) {
            System.out.println("connection error: " + e.getMessage());
        }
    }

    public static boolean checkLogin(String userName, String password)
    {
        PreparedStatement statement;
        ResultSet res;
        boolean checkUser = false;
        String query = "SELECT * FROM `users` WHERE `User_Name` =?";

        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, userName);

            res = statement.executeQuery();

            if(res.next())
            {
                if(password.equals(res.getString("Password"))) {
                    checkUser = true;
                }
            }
        } catch (SQLException ex) {
            System.out.println("login error: " + ex);
        }
        return checkUser;
    }

    public static User getUser(String userName)
    {
        PreparedStatement statement;
        ResultSet res;
        User user = null;
        String query = "SELECT * FROM `users` WHERE `User_Name` =?";

        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, userName);

            res = statement.executeQuery();

            if(res.next()) {
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
            System.out.println("getUser error: " + ex);
        }
        return user;
    }

    public static ObservableList<Customer> getAllCustomers() {
        PreparedStatement statement;
        ResultSet res;
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        Customer newCustomer = null;

        String query = "SELECT * FROM `customers`";

        try {
            statement = connection.prepareStatement(query);
            res = statement.executeQuery();

            while(res.next()){
                int customerId = res.getInt("Customer_ID");
                String customerName = res.getString("Customer_Name");
                String address = res.getString("Address");
                String postalCode = res.getString("Postal_code");
                String phone = res.getString("Phone");
                LocalDateTime createDate = res.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = res.getString("Created_By");
                LocalDateTime lastUpdate = res.getTimestamp("Last_Update").toLocalDateTime();
                String lastUpdatedBy = res.getString("Last_Updated_By");
                int divisionId = res.getInt("Division_ID");

                newCustomer = new Customer(customerId, customerName, address, postalCode, phone,createDate,createdBy,lastUpdate,lastUpdatedBy, divisionId);
                customers.add(newCustomer);
            }

        } catch (SQLException ex){
            System.out.println("error from getAllCustomers: " + ex);
        }
        return customers;
    }

    public static ObservableList<Appointment> getAllAppointments() {
        PreparedStatement statement;
        ResultSet res;
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        Appointment newAppointment = null;

        String query = "SELECT * FROM `appointments`";

        try {
            statement = connection.prepareStatement(query);
            res = statement.executeQuery();

            while(res.next()){
                int id = res.getInt("Appointment_ID");
                String title = res.getString("Title");
                String description = res.getString("Description");
                String location = res.getString("Location");
                String type = res.getString("Type");
                LocalDateTime start = res.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = res.getTimestamp("End").toLocalDateTime();
                LocalDateTime createDate = res.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = res.getString("Created_By");
                LocalDateTime lastUpdate = res.getTimestamp("Last_Update").toLocalDateTime();
                String lastUpdatedBy = res.getString("Last_Updated_By");
                int customerId = res.getInt("Customer_ID");
                int userId = res.getInt("User_ID");
                int contactId = res.getInt("Contact_ID");


                newAppointment = new Appointment(id, title, description, location, type, start, end, createDate,createdBy,lastUpdate,lastUpdatedBy, customerId, userId, contactId);
                appointments.add(newAppointment);
            }

        } catch (SQLException ex){
            System.out.println("error from getAllCustomers: " + ex);
        }
        return appointments;
    }

    public static ObservableList<FirstLevelDivision> getSelectiveDivisions(int selectedCountryId) {
        PreparedStatement statement;
        ResultSet res;
        ObservableList<FirstLevelDivision> divisions = FXCollections.observableArrayList();
        FirstLevelDivision newDivision = null;

        String query = "SELECT * FROM `first_level_divisions` WHERE COUNTRY_ID =?";

        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, String.valueOf(selectedCountryId));
            res = statement.executeQuery();

            while(res.next()){
                int id = res.getInt("Division_ID");
                String division = res.getString("Division");
                LocalDateTime createDate = res.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = res.getString("Created_By");
                LocalDateTime lastUpdate = res.getTimestamp("Last_Update").toLocalDateTime();
                String lastUpdatedBy = res.getString("Last_Updated_By");
                int countryId = res.getInt("COUNTRY_ID");

                newDivision = new FirstLevelDivision(id, division, createDate,createdBy,lastUpdate,lastUpdatedBy, countryId);
                divisions.add(newDivision);
            }

        } catch (SQLException ex){
            System.out.println("error from getAllDivisions: " + ex);
        }
        return divisions;
    }

    public static ObservableList<Country> getAllCountries() {
        PreparedStatement statement;
        ResultSet res;
        ObservableList<Country> countries = FXCollections.observableArrayList();
        Country newCountry = null;

        String query = "SELECT * FROM `countries`";

        try {
            statement = connection.prepareStatement(query);
            res = statement.executeQuery();

            while(res.next()){
                int id = res.getInt("Country_ID");
                String country = res.getString("Country");
                LocalDateTime createDate = res.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = res.getString("Created_By");
                LocalDateTime lastUpdate = res.getTimestamp("Last_Update").toLocalDateTime();
                String lastUpdatedBy = res.getString("Last_Updated_By");

                newCountry = new Country(id, country, createDate,createdBy,lastUpdate,lastUpdatedBy);
                countries.add(newCountry);
            }

        } catch (SQLException ex){
            System.out.println("error from getAllCountries: " + ex);
        }
        return countries;
    }

    public static Customer addNewCustomer(String name, String address, String postalCode, String phone, int divisionId) {
        PreparedStatement insertStatement;
        ResultSet res;
        Customer newCustomer = null;

        String insertQuery = "INSERT INTO `customers` (Customer_Name, Address, Postal_code, Phone, Division_ID) VALUES(?,?,?,?,?)";
        try {
            insertStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            insertStatement.setString(1, name);
            insertStatement.setString(2, address);
            insertStatement.setString(3, postalCode);
            insertStatement.setString(4, phone);
            insertStatement.setInt(5, divisionId);
            insertStatement.execute();

            res = insertStatement.getGeneratedKeys();

            res.next();
            int createdCustomerId = res.getInt(1);

            String getQuery = "SELECT * FROM `customers` WHERE `Customer_ID` =?";
            PreparedStatement getStatement = connection.prepareStatement(getQuery);
            getStatement.setInt(1, createdCustomerId);
            res = getStatement.executeQuery();

            if(res.next()) {
                LocalDateTime createDate = res.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = res.getString("Created_By");
                LocalDateTime lastUpdate = res.getTimestamp("Last_Update").toLocalDateTime();
                String lastUpdatedBy = res.getString("Last_Updated_By");

                newCustomer = new Customer(createdCustomerId, name, address, postalCode, phone, createDate,createdBy,lastUpdate,lastUpdatedBy, divisionId);
            }
        }
        catch(SQLException ex) {
            System.out.println("error from addNewCustomer: " + ex);
        }
        return newCustomer;
    }

    public static Customer modifyCustomer(String name, String address, String postalCode, String phone, int divisionId, int customerId) {
        ResultSet res;
        Customer updatedCustomer = null;

        String updateQuery = "UPDATE `customers` SET Customer_Name=?, Address=?, Postal_code=?, Phone=?, Division_ID=?, Last_Update=NOW() " +
                "WHERE `Customer_ID` =?";
        try {
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setString(1, name);
            updateStatement.setString(2, address);
            updateStatement.setString(3, postalCode);
            updateStatement.setString(4, phone);
            updateStatement.setInt(5, divisionId);
            updateStatement.setInt(6, customerId);
            updateStatement.execute();

            String getQuery = "SELECT * FROM `customers` WHERE `Customer_ID` =?";
            PreparedStatement getStatement = connection.prepareStatement(getQuery);
            getStatement.setInt(1, customerId);
            res = getStatement.executeQuery();

            if(res.next()) {
                LocalDateTime createDate = res.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = res.getString("Created_By");
                LocalDateTime lastUpdate = res.getTimestamp("Last_Update").toLocalDateTime();
                String lastUpdatedBy = res.getString("Last_Updated_By");

                updatedCustomer = new Customer(customerId, name, address, postalCode, phone, createDate,createdBy,lastUpdate,lastUpdatedBy, divisionId);
            }
        }
        catch(SQLException ex) {
            System.out.println("error from modifyCustomer: " + ex);
        }
        return updatedCustomer;
    }

    public static void deleteCustomer(Customer selectedCustomer) {
        ResultSet res;
        String deleteQuery = "DELETE FROM `customers` WHERE `Customer_ID` =?";
        try {
            PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
            deleteStatement.setInt(1, selectedCustomer.getId());
            deleteStatement.execute();
        }
        catch(SQLException ex) {
            System.out.println("error from deleteCustomer: " + ex);
        }
    }

    public static ObservableList<Contact> getAllContacts() {
        PreparedStatement statement;
        ResultSet res;
        ObservableList<Contact> contacts = FXCollections.observableArrayList();
        Contact newContact = null;

        String query = "SELECT * FROM `contacts`";

        try {
            statement = connection.prepareStatement(query);
            res = statement.executeQuery();

            while(res.next()){
                int id = res.getInt("Contact_ID");
                String name = res.getString("Contact_Name");
                String email = res.getString("Email");

                newContact = new Contact(id, name, email);
                contacts.add(newContact);
            }
        } catch (SQLException ex){
            System.out.println("error from getAllContacts: " + ex);
        }
        return contacts;
    }

    public static ObservableList<User> getAllUsers() {
        PreparedStatement statement;
        ResultSet res;
        ObservableList<User> users = FXCollections.observableArrayList();
        User newUser = null;

        String query = "SELECT * FROM `users`";

        try {
            statement = connection.prepareStatement(query);
            res = statement.executeQuery();
            while(res.next()){
                newUser = new User(
                        Integer.valueOf(res.getString("User_ID")),
                        res.getString("User_Name"),
                        res.getString("Password"),
                        res.getTimestamp("Create_Date").toLocalDateTime(),
                        res.getString("Created_By"),
                        res.getTimestamp("Last_Update").toLocalDateTime(),
                        res.getString("Last_Updated_By")
                );
                users.add(newUser);
            }
        }
        catch (SQLException ex){
            System.out.println("error from getAllUsers: " + ex);
        }
        return users;
    }

    public static Appointment addNewAppointment(String title, String description, String location, String type, Contact contact, Customer customer, User user, LocalDateTime startDt, LocalDateTime endDt) {
        PreparedStatement insertStatement;
        ResultSet res;
        Appointment newAppointment = null;

        String insertQuery = "INSERT INTO `appointments` (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) VALUES(?,?,?,?,?,?,?,?,?)";
        try {
            insertStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            insertStatement.setString(1, title);
            insertStatement.setString(2, description);
            insertStatement.setString(3, location);
            insertStatement.setString(4, type);
            insertStatement.setTimestamp(5, Timestamp.valueOf(startDt));
            insertStatement.setTimestamp(6, Timestamp.valueOf(endDt));
            insertStatement.setInt(7, customer.getId());
            insertStatement.setInt(8, user.getId());
            insertStatement.setInt(9, contact.getId());
            insertStatement.execute();

            res = insertStatement.getGeneratedKeys();

            res.next();
            int createdApptId = res.getInt(1);

            String getQuery = "SELECT * FROM `appointments` WHERE `Appointment_ID` =?";
            PreparedStatement getStatement = connection.prepareStatement(getQuery);
            getStatement.setInt(1, createdApptId);
            res = getStatement.executeQuery();

            if(res.next()) {
                LocalDateTime createDate = res.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = res.getString("Created_By");
                LocalDateTime lastUpdate = res.getTimestamp("Last_Update").toLocalDateTime();
                String lastUpdatedBy = res.getString("Last_Updated_By");

                newAppointment = new Appointment(createdApptId, title, description, location, type, startDt, endDt, createDate,createdBy,lastUpdate,lastUpdatedBy, customer.getId(), user.getId(), contact.getId());
            }
        }
        catch(SQLException ex) {
            System.out.println("error from addNewAppointment: " + ex);
        }
        return newAppointment;
    }
}
