package com.g02.flightsalesfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

public class LoginController{

    @FXML
    private TextField username;

    @FXML
    private TextField password;

    @FXML
    private Button loginButton;


    public void initialize() {
        username.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER){
                try{
                    login(null);
                }catch(Exception exception){

                }
            }
        });
        password.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER){
                try{
                    login(null);
                }catch(Exception exception){

                }
            }
        });
    }

    @FXML
    void login(ActionEvent event) throws java.io.IOException {
        var employee = App.businessLogicAPI.login(username.getText(), password.getText());
        if (employee != null) {
            App.employee = employee;
            App.setRoot("home");
        }
    }

}
