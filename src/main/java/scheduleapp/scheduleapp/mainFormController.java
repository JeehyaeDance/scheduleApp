package scheduleapp.scheduleapp;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Appointment;
import model.Customer;

import java.net.URL;
import java.util.ResourceBundle;

public class mainFormController implements Initializable {
    public TableView appointmentsTable;
    public TableColumn appointmentIdCol;
    public TableColumn titleCol;
    public TableColumn descriptionCol;
    public TableColumn locationCol;
    public TableColumn contactCol;
    public TableColumn typeCol;
    public TableColumn startDateCol;
    public TableColumn endDateCol;
    public TableColumn customerCol;
    public TableColumn userCol;
    private ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    private ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    public TableView customersTable;
    public TableColumn customerTableIdCol;
    public TableColumn customerNameCol;
    public TableColumn addressCol;
    public TableColumn postalCodeCol;
    public TableColumn phoneCol;
    public TableColumn createdDateCol;

    public void openAppointmentsTab(Event event) {
    }

    public void addNewAppointment(ActionEvent actionEvent) {
    }

    public void modifyAppointment(ActionEvent actionEvent) {
    }

    public void deleteAppointment(ActionEvent actionEvent) {
    }

    public void openCustomersTab(Event event) {
    }

    public void addNewCustomer(ActionEvent actionEvent) {
    }

    public void modifyCustomer(ActionEvent actionEvent) {
    }

    public void deleteCustomer(ActionEvent actionEvent) {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        allCustomers = JDBC.getAllCustomers();
        allAppointments = JDBC.getAllAppointments();

        customersTable.setItems(allCustomers);
        customerTableIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("fullAddress"));
        postalCodeCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        createdDateCol.setCellValueFactory(new PropertyValueFactory<>("createdDate"));

        appointmentsTable.setItems(allAppointments);
        appointmentIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endDateCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        userCol.setCellValueFactory(new PropertyValueFactory<>("userName"));
        customerCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));


    }
}
