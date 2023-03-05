package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.Customer;

import java.sql.*;
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
                int customerId = Integer.parseInt(res.getString("Customer_ID"));
                String customerName = res.getString("Customer_Name");
                String address = res.getString("Address");
                String postalCode = res.getString("Postal_code");
                String phone = res.getString("Phone");
                LocalDateTime createDate = res.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = res.getString("Created_By");
                LocalDateTime lastUpdate = res.getTimestamp("Last_Update").toLocalDateTime();
                String lastUpdatedBy = res.getString("Last_Updated_By");
                int divisionId = Integer.parseInt(res.getString("Division_ID"));

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
                int id = Integer.parseInt(res.getString("Appointment_ID"));
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
                int customerId = Integer.parseInt(res.getString("Customer_ID"));
                int userId = Integer.parseInt(res.getString("User_ID"));
                int contactId = Integer.parseInt(res.getString("Contact_ID"));


                newAppointment = new Appointment(id, title, description, location, type, start, end, createDate,createdBy,lastUpdate,lastUpdatedBy, customerId, userId, contactId);
                appointments.add(newAppointment);
            }

        } catch (SQLException ex){
            System.out.println("error from getAllCustomers: " + ex);
        }
        return appointments;
    }
}
