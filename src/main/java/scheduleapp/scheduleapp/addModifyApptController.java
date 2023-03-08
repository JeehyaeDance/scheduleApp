package scheduleapp.scheduleapp;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class addModifyApptController implements Initializable {
    public DatePicker dateCal;
    public ComboBox startCombo;
    public ComboBox endCombo;
    private ObservableList<Contact> allContact = Book.getAllContacts();
    private ObservableList<Customer> allCustomer = Book.getAllCustomers();
    private ObservableList<User> allUser = Book.getAllUsers();
    private static Appointment selectedAppointment = null;
    private static User loggedinUser = null;
    public Label addModifyAppointmentLabel;
    public TextField titleInput;
    public TextField descriptionInput;
    public TextField locationInput;
    public TextField typeInput;
    public Label errorMessage;
    public ComboBox contactCombo;
    public ComboBox customerCombo;
    public ComboBox userCombo;

    public static void setLoggedinUser (User user) {
        loggedinUser = user;
    }

    /**  This method reloads a scene with the main form. */
    public void toMainForm(ActionEvent actionEvent) throws IOException {
        loginFormController.loadNewScene(actionEvent, "mainForm.fxml", 1050, 700 );
        selectedAppointment = null;
    }

    public void submitForm(ActionEvent actionEvent) throws IOException {
        StringBuilder errors = new StringBuilder("");

        String title = titleInput.getText();
        String description = descriptionInput.getText();
        String location = locationInput.getText();
        String type = typeInput.getText();
        Contact contact = (Contact) contactCombo.getSelectionModel().getSelectedItem();
        Customer customer = (Customer) customerCombo.getSelectionModel().getSelectedItem();
        User user = (User) userCombo.getSelectionModel().getSelectedItem();
        Object startTime = startCombo.getSelectionModel().getSelectedItem();
        Object endTime = endCombo.getSelectionModel().getSelectedItem();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-M-yyyy hh:mm a");
        LocalDateTime startDt = LocalDateTime.parse((CharSequence) startTime, format);
        LocalDateTime endDt = LocalDateTime.parse((CharSequence) endTime, format);

        System.out.println(startTime);
        if (title.isBlank()) {
            errors.append("Customer Name is empty");
            errors.append(System.lineSeparator());
        }
        if (description.isBlank()) {
            errors.append("Phone number is empty");
            errors.append(System.lineSeparator());
        }
        if (location.isBlank()) {
            errors.append("Postal Code is empty");
            errors.append(System.lineSeparator());
        }
        if (type.isBlank()) {
            errors.append("Address is empty");
            errors.append(System.lineSeparator());
        }
        if (contact == null) {
            errors.append("Contact is empty");
            errors.append(System.lineSeparator());
        }
        if (customer == null) {
            errors.append("Customer is empty");
            errors.append(System.lineSeparator());
        }
        if (startTime == null) {
            errors.append("Start time is empty");
            errors.append(System.lineSeparator());
        }
        if (endTime == null) {
            errors.append("End time is empty");
            errors.append(System.lineSeparator());
        }

        if (errors.length() > 0) {
            errorMessage.setText(errors.toString());
            return;
        }
        if (startDt.isAfter(endDt) || startDt.isEqual(endDt)) {
            errors.append("start time is must be before end time");
            errors.append(System.lineSeparator());
            errorMessage.setText(errors.toString());
            return;
        }

        if (selectedAppointment == null) {
            Appointment newAppointment = JDBC.addNewAppointment(title, description, location, type, contact, customer, user, startDt, endDt);
            Book.addAppointment(newAppointment);
        } else {
//            int replaceIndex = Book.getAllCustomers().indexOf(selectedCustomer);
//            Customer updatedCustomer = JDBC.modifyCustomer(name, address, postalCode, phone, selectedDivision.getId(), selectedCustomer.getId());
//            Book.updateCustomer(replaceIndex, updatedCustomer);
        }
        toMainForm(actionEvent);
    }

    public void cancelForm(ActionEvent actionEvent) throws IOException {
        toMainForm(actionEvent);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateCal.setValue(LocalDate.now());
        fillAvailableTime();
        contactCombo.setItems(allContact);
        customerCombo.setItems(allCustomer);
        userCombo.setItems(allUser);
        userCombo.getSelectionModel().select(loggedinUser);
        customerCombo.setPromptText("Choose customer for this appointment");
        contactCombo.setPromptText("Choose contact for this appointment");
    }

    private void fillAvailableTime() {
        startCombo.getItems().clear();
        endCombo.getItems().clear();
        LocalDate localDate = dateCal.getValue();
        ZoneId targetZoneId = ZoneId.of("America/New_York");
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-M-yyyy hh:mm a");
        ZoneId userZoneId = ZonedDateTime.now().getZone();
        ZonedDateTime estStart = ZonedDateTime.of(localDate,LocalTime.of(8,0), targetZoneId);
        ZonedDateTime estEnd = ZonedDateTime.of(localDate,LocalTime.of(22,0), targetZoneId);
        LocalDateTime localStart = estStart.withZoneSameInstant(userZoneId).toLocalDateTime();
        LocalDateTime localEnd = estEnd.withZoneSameInstant(userZoneId).toLocalDateTime();

        while(localStart.isBefore(localEnd.plusSeconds(1))){
            startCombo.getItems().add(localStart.format(format));
            endCombo.getItems().add(localStart.format(format));
            localStart = localStart.plusMinutes(30);
        }
    }

    public void onSelectDate(ActionEvent actionEvent) {
        fillAvailableTime();
    }
}
