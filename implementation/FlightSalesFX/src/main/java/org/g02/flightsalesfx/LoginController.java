package org.g02.flightsalesfx;

import org.g02.flightsalesfx.businessLogic.SalesEmployeeImpl;
import org.g02.flightsalesfx.businessLogic.SalesManagerImpl;
import org.g02.flightsalesfx.businessLogic.SalesOfficerImpl;
import org.g02.flightsalesfx.helpers.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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


            if(App.employee.getClass().equals(SalesOfficerImpl.class)){
                System.out.println("SalesOfficer");
                App.setRoot("home");
            }
            if(App.employee.getClass().equals(SalesEmployeeImpl.class)){
                System.out.println("SalesEmployee");
                App.setRoot("salesEmployeeHome");
            }
            if(App.employee.getClass().equals(SalesManagerImpl.class)){
                System.out.println("SalesManager");
                App.setRoot("home");//todo change to SalesManagerHome
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login-Error");
            alert.setHeaderText("Wrong email-address or password entered");
            alert.setContentText("Please check your inputs and try again");
            alert.showAndWait();
        }
    }

}
