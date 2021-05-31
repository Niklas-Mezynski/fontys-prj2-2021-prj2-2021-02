package org.g02.flightsalesfx;


import org.g02.flightsalesfx.businessLogic.SalesOfficerImpl;
import org.g02.flightsalesfx.helpers.Controller;
import org.g02.flightsalesfx.persistence.RouteStorageServiceImpl;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;

public class ManagementDashboardController implements Controller {

    @FXML
    private Label greetingLabel;

    @FXML
    void initialize() {
        greetingLabel.setText(greetingLabel.getText().replace("XXX", App.employee != null ? App.employee.getName() : ""));
    }

    @FXML
    void viewRevenueRoute() {
        App.setRoot("viewKPIs");
    }

    @FXML
    void viewEmployeeNumbers() {
        App.setRoot("viewEmpNumbers");
    }


}
