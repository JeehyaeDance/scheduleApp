package scheduleapp.scheduleapp;

import helper.JDBC;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Book;
import model.Country;
import model.Customer;
import model.FirstLevelDivision;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class addModifyCustomerController implements Initializable {
    private static Customer selectedCustomer = null;
    public TextField addressInput;
    public TextField idInput;
    private ObservableList<FirstLevelDivision> allDivisions = Book.getAllDivisions();
    private ObservableList<Country> allCountries = Book.getAllCountries();
    public Label addModifyCustomerLabel;
    public TextField customerNameInput;
    public TextField phoneNumberInput;
    public ComboBox countryCombo;
    public TextField postalCodeInput;
    public ComboBox divisionCombo;
    public Label errorMessage;

    /** static method to set a customer for modify.
     * @param customer the customer to set for modify
     */
    public static void setModifyCustomer (Customer customer) {
        selectedCustomer = customer;
    }

    public void submitForm(ActionEvent actionEvent) throws IOException {
        StringBuilder errors = new StringBuilder("");

        String name = customerNameInput.getText();
        String phone = phoneNumberInput.getText();
        FirstLevelDivision selectedDivision = (FirstLevelDivision) divisionCombo.getSelectionModel().getSelectedItem();
        String postalCode = postalCodeInput.getText();
        String address = addressInput.getText();

        if (name.isBlank()) {
            errors.append("Customer Name: null");
            errors.append(System.lineSeparator());
        }
        if (phone.isBlank()) {
            errors.append("Phone number: null");
            errors.append(System.lineSeparator());
        }
        if (postalCode.isBlank()) {
            errors.append("Postal Code: null");
            errors.append(System.lineSeparator());
        }
        if (address.isBlank()) {
            errors.append("Address: null");
            errors.append(System.lineSeparator());
        }
        if (selectedDivision == null) {
            errors.append("First Level Division: null");
            errors.append(System.lineSeparator());
        }

        if (errors.length() > 0) {
            errorMessage.setText(errors.toString());
            return;
        }

        if (selectedCustomer == null) {
            Customer newCustomer = JDBC.addNewCustomer(name, address, postalCode, phone, selectedDivision.getId());
            Book.addCustomer(newCustomer);
        } else {
            int replaceIndex = Book.getAllCustomers().indexOf(selectedCustomer);
            Customer updatedCustomer = JDBC.modifyCustomer(name, address, postalCode, phone, selectedDivision.getId(), selectedCustomer.getId());
            Book.updateCustomer(replaceIndex, updatedCustomer);
        }
        toMainForm(actionEvent);
    }

    public void cancelForm(ActionEvent actionEvent) throws IOException {
        toMainForm(actionEvent);
    }

    /**  This method reloads a scene with the main form. */
    public void toMainForm(ActionEvent actionEvent) throws IOException {
        loginFormController.loadNewScene(actionEvent, "mainForm.fxml", 1050, 700 );
        selectedCustomer = null;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        countryCombo.setItems(allCountries);
        divisionCombo.setItems(allDivisions);
        divisionCombo.setPromptText("Select division here");
        divisionCombo.setVisibleRowCount(5);

        if (selectedCustomer == null) {
            countryCombo.getSelectionModel().selectFirst();
        } else {
            String id = String.valueOf(selectedCustomer.getId());
            String name = selectedCustomer.getName();
            String address = selectedCustomer.getAddress();
            String phone = selectedCustomer.getPhone();
            String postalCode = selectedCustomer.getPostalCode();

            idInput.setText(id);
            customerNameInput.setText(name);
            addressInput.setText(address);
            phoneNumberInput.setText(phone);
            postalCodeInput.setText(postalCode);
            countryCombo.getSelectionModel().select(selectedCustomer.firstLevelDivision().country());
            divisionCombo.getSelectionModel().select(selectedCustomer.firstLevelDivision());
        }
    }

    public void onSelectCountry(ActionEvent actionEvent) {
        divisionCombo.getSelectionModel().clearSelection();
        Country selectedCountry = (Country) countryCombo.getSelectionModel().getSelectedItem();
        allDivisions = JDBC.getSelectiveDivisions(selectedCountry.getId());
        divisionCombo.setItems(allDivisions);
    }
}
