package model;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Book {
    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    private static ObservableList<FirstLevelDivision> allDivisions = FXCollections.observableArrayList();
    private static ObservableList<Country> allCountries = FXCollections.observableArrayList();

    static {
        allCustomers = JDBC.getAllCustomers();
        allAppointments = JDBC.getAllAppointments();
        allCountries = JDBC.getAllCountries();
        allDivisions = JDBC.getSelectiveDivisions(1);
    }

    public static void addCustomer(Customer newCustomer) {
        allCustomers.add(newCustomer);
    }
    public static void updateCustomer(int index, Customer updatedCustomer) { allCustomers.set(index, updatedCustomer); }
    public static boolean deleteCustomer(Customer customerToDelete) { return allCustomers.remove(customerToDelete); }
    public static ObservableList<Customer> getAllCustomers() { return allCustomers; }
    public static ObservableList<Appointment> getAllAppointments() { return allAppointments; }
    public static ObservableList<FirstLevelDivision> getAllDivisions() { return allDivisions; }
    public static ObservableList<Country> getAllCountries() { return allCountries; }
}
