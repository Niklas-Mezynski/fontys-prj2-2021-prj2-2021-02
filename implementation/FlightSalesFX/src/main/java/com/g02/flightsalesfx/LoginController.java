package com.g02.flightsalesfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField username;

    @FXML
    private TextField password;

    @FXML
    private Button loginButton;

    @FXML
    void login(ActionEvent event) throws java.io.IOException {
        var employee = App.businessLogicAPI.login(username.getText(), password.getText());
        if (employee != null) {
            App.employee = employee;
            App.setRoot("home");
        }
    }

}
