package scheduleapp.scheduleapp;

import helper.JDBC;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class loginFormController {

    private static Stage stage;
    public TextField userNameInput;
    public TextField passwordInput;
    public Label errorMessage;
    public Label languageLabel;
    public Button loginButton;

    public void handleLoginClick(ActionEvent actionEvent) throws IOException {
        String userName = userNameInput.getText();
        String password = passwordInput.getText();
        if(userName == null | password == null)
            return;
        Boolean loginStatus = JDBC.checkLogin(userName, password);

        if(loginStatus == true) {
            loadNewScene(actionEvent, "mainForm.fxml", 1050, 700);
        }
    }

    /**  This method helps load a new scene. */
    public static void loadNewScene(ActionEvent event, String fileName, int width, int height) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fileName));
        Scene scene = new Scene(fxmlLoader.load(), width, height);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
