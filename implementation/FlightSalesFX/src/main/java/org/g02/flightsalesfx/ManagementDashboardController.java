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
    private ProgressBar progressBar;

    @FXML
    void initialize() {
        greetingLabel.setText(greetingLabel.getText().replace("XXX", App.employee != null ? App.employee.getName() : "Madam/Sir"));
    }

    @FXML
    void viewRevenueRoute() {
        App.setRoot("viewKPIs");
        final Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        final KeyValue kv = new KeyValue(progressBar.progressProperty(), 1);
        final KeyFrame kf = new KeyFrame(Duration.millis(1000), kv);
        timeline.getKeyFrames().add(kf);
        timeline.play();
    }

    @FXML
    void viewEmployeeNumbers() {
        App.setRoot("viewEmpNumbers");
    }


}