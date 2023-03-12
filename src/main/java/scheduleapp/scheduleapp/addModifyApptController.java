package scheduleapp.scheduleapp;

import helper.JDBC;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
    public TextField idInput;
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
    ZoneId targetZoneId = ZoneId.of("America/New_York");
    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-M-yyyy hh:mm a");

    public static void setLoggedinUser (User user) {
        loggedinUser = user;
    }
    public static void setSelectedAppointment (Appointment appointment) {
        selectedAppointment = appointment;
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
        LocalDateTime startDt = LocalDateTime.parse((CharSequence) startTime, format);
        LocalDateTime endDt = LocalDateTime.parse((CharSequence) endTime, format);

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

        ObservableList<Appointment> appointmentsForThisCustomer = JDBC.getAppointmentsByCustomer(customer);

        if (appointmentsForThisCustomer.size() > 0) {
            String overlapMessage = checkTimeOverlap(startDt, endDt, appointmentsForThisCustomer, selectedAppointment);
            if (overlapMessage.length() > 0) {
                errorMessage.setText(overlapMessage);
                return;
            }
        }

        if (selectedAppointment == null) {
            Appointment newAppointment = JDBC.addNewAppointment(title, description, location, type, contact, customer, user, startDt, endDt);
            Book.addAppointment(newAppointment);
        } else {
            int replaceIndex = Book.getAllAppointments().indexOf(selectedAppointment);
            Appointment updatedAppointment = JDBC.modifyAppointment(selectedAppointment.getId(), loggedinUser, title, description, location, type, contact, customer, user, startDt, endDt);
            Book.updateAppointment(replaceIndex, updatedAppointment);
        }
        toMainForm(actionEvent);
    }

    public void cancelForm(ActionEvent actionEvent) throws IOException {
        toMainForm(actionEvent);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        contactCombo.setItems(allContact);
        customerCombo.setItems(allCustomer);
        userCombo.setItems(allUser);

        if (selectedAppointment == null) {
            addModifyAppointmentLabel.setText("Add New Appointment");
            dateCal.setValue(LocalDate.now());
            customerCombo.setPromptText("Choose customer for this appointment");
            contactCombo.setPromptText("Choose contact for this appointment");
            userCombo.getSelectionModel().select(loggedinUser);
            fillAvailableTime();
        } else {
            addModifyAppointmentLabel.setText("Modify Appointment");
            String id = String.valueOf(selectedAppointment.getId());
            String title = selectedAppointment.getTitle();
            String desc = selectedAppointment.getDescription();
            String type = selectedAppointment.getType();
            String location = selectedAppointment.getLocation();
            Contact contact = selectedAppointment.contact();
            User user = selectedAppointment.user();
            Customer customer = selectedAppointment.customer();
            LocalDate date = selectedAppointment.getStart().toLocalDate();
            LocalDateTime start = selectedAppointment.getStart();
            LocalDateTime end = selectedAppointment.getEnd();

            idInput.setText(id);
            titleInput.setText(title);
            descriptionInput.setText(desc);
            typeInput.setText(type);
            locationInput.setText(location);
            contactCombo.getSelectionModel().select(contact);
            userCombo.getSelectionModel().select(user);
            customerCombo.getSelectionModel().select(customer);
            dateCal.setValue(date);
            fillAvailableTime();
            startCombo.getSelectionModel().select(start.format(format));
            endCombo.getSelectionModel().select(end.format(format));
        }
    }

    private void fillAvailableTime() {
        startCombo.getItems().clear();
        endCombo.getItems().clear();
        LocalDate localDate = dateCal.getValue();
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

    public String checkTimeOverlap (LocalDateTime start, LocalDateTime end, ObservableList<Appointment> appointments, Appointment prevAppointment) {
        StringBuilder overlapTimes = new StringBuilder("");

        for(int i = 0; i < appointments.size(); ++i) {
            Appointment currentAppt = appointments.get(i);
            if (currentAppt.getId() != prevAppointment.getId()) {
                LocalDateTime apptStart = currentAppt.getStart();
                LocalDateTime apptEnd = currentAppt.getEnd();

                boolean startWithinRange = (start.isAfter(apptStart) || start.isEqual(apptStart)) && start.isBefore(apptEnd);
                boolean endWithinRange = end.isAfter(apptStart) && (end.isBefore(apptEnd) || end.isEqual(apptEnd));
                boolean isRangeCover = (start.isBefore(apptStart) || start.isEqual(apptStart)) && (end.isAfter(apptEnd) || end.isEqual(apptEnd));
                boolean hasOverlap = startWithinRange || endWithinRange || isRangeCover;

                if (hasOverlap == true) {
                    overlapTimes.append("There is a appointment overlap. (" + apptStart.format(format) + " ~ " + apptEnd.format(format) + ")");
                    overlapTimes.append(System.lineSeparator());
                }
            }
        }
        return overlapTimes.toString();
    }
}
