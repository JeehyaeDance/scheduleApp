package scheduleapp.scheduleapp;

import helper.JDBC;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Appointment;
import model.Book;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
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
    private ObservableList<Customer> allCustomers = Book.getAllCustomers();
    private ObservableList<Appointment> allAppointments = Book.getAllAppointments();
    public TableView customersTable;
    public TableColumn customerTableIdCol;
    public TableColumn customerNameCol;
    public TableColumn addressCol;
    public TableColumn postalCodeCol;
    public TableColumn phoneCol;
    public TableColumn createdDateCol;

    public void addNewAppointment(ActionEvent actionEvent) throws IOException {
        loginFormController.loadNewScene(actionEvent, "addModifyAppt.fxml", 1050, 700);
    }

    public void modifyAppointment(ActionEvent actionEvent) throws IOException {
        Appointment selectedAppointment = (Appointment) appointmentsTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null)
            return;
        addModifyApptController.setSelectedAppointment(selectedAppointment);
        loginFormController.loadNewScene(actionEvent, "addModifyAppt.fxml", 1050, 700);
    }

    public void deleteAppointment(ActionEvent actionEvent) {
    }

    public void addNewCustomer(ActionEvent actionEvent) throws IOException {
        loginFormController.loadNewScene(actionEvent, "addModifyCustomer.fxml", 1050, 700);
    }

    public void modifyCustomer(ActionEvent actionEvent) throws IOException {
        Customer selectedCustomer = (Customer) customersTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null)
            return;
        addModifyCustomerController.setModifyCustomer(selectedCustomer);
        loginFormController.loadNewScene(actionEvent, "addModifyCustomer.fxml", 1050, 700);
    }

    public void deleteCustomer(ActionEvent actionEvent) {
        Customer selectedCustomer = (Customer) customersTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null)
            return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you really want to delete this customer?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            JDBC.deleteCustomer(selectedCustomer);
            boolean deleteResult = Book.deleteCustomer(selectedCustomer);
            if(!deleteResult){
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setContentText("delete was not successful.");
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
