package com.g02.flightsalesfx;

import com.g02.flightsalesfx.businessEntities.SalesManager;
import com.g02.flightsalesfx.businessLogic.SalesEmployeeImpl;
import com.g02.flightsalesfx.businessLogic.SalesManagerImpl;
import com.g02.flightsalesfx.businessLogic.SalesOfficerImpl;
import com.g02.flightsalesfx.helpers.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

public class LoginController implements Controller {

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


            if(App.employee.getClass().equals(SalesOfficerImpl.class)){//username & password :"a"
                System.out.println("SalesOfficer");
                App.setRoot("home");
            }
            if(App.employee.getClass().equals(SalesEmployeeImpl.class)){//username & password :"c"
                System.out.println("SalesEmployee");
                App.setRoot("salesEmployeeHome");
            }
            if(App.employee.getClass().equals(SalesManagerImpl.class)){//username & password :"b"
                System.out.println("SalesManager");
                App.setRoot("managementDashboard");
            }
        }
    }

}
