package com.g02.flightsalesfx;


import com.g02.flightsalesfx.businessLogic.SalesOfficerImpl;
import com.g02.flightsalesfx.helpers.Controller;
import com.g02.flightsalesfx.persistence.RouteStorageServiceImpl;
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
        greetingLabel.setText(greetingLabel.getText().replace("XXX", App.employee != null ? "Mr./Mrs. " + App.employee.getName() : "Madam/Sir"));


    }

    @FXML
    void viewRevenueRoute() {

//        final Timeline timeline = new Timeline();
//        timeline.setCycleCount(Timeline.INDEFINITE);
//        final KeyValue kv = new KeyValue(progressBar.progressProperty(), 1);
//        final KeyFrame kf = new KeyFrame(Duration.millis(1000), kv);
//        timeline.getKeyFrames().add(kf);
//        timeline.play();
        App.setRoot("viewKPIs");

    }


}
