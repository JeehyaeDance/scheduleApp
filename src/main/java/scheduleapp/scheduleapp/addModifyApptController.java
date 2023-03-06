package scheduleapp.scheduleapp;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class addModifyApptController implements Initializable {
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
    public TextField startInput;
    public TextField endInput;
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

    public void submitForm(ActionEvent actionEvent) {
        StringBuilder errors = new StringBuilder("");

        String title = titleInput.getText();
        String description = descriptionInput.getText();
        String location = locationInput.getText();
        String type = typeInput.getText();

        if (title.isBlank()) {
            errors.append("Customer Name: null");
            errors.append(System.lineSeparator());
        }
        if (description.isBlank()) {
            errors.append("Phone number: null");
            errors.append(System.lineSeparator());
        }
        if (location.isBlank()) {
            errors.append("Postal Code: null");
            errors.append(System.lineSeparator());
        }
        if (type.isBlank()) {
            errors.append("Address: null");
            errors.append(System.lineSeparator());
        }

        if (errors.length() > 0) {
            errorMessage.setText(errors.toString());
            return;
        }
    }

    public void cancelForm(ActionEvent actionEvent) throws IOException {
        toMainForm(actionEvent);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        contactCombo.setItems(allContact);
        customerCombo.setItems(allCustomer);
        userCombo.setItems(allUser);
        userCombo.getSelectionModel().select(loggedinUser);
        customerCombo.setPromptText("Choose customer for this appointment");
        contactCombo.setPromptText("Choose contact for this appointment");
    }
}
