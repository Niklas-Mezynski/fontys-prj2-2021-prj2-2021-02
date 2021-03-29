package com.g02.flightsalesfx;

import com.g02.flightsalesfx.businessEntities.Plane;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class HomeController {

    @FXML
    public VBox planesListVBox;


    public void initialize() {
        var all = App.persistenceAPI.getPlaneStorageService(App.businessLogicAPI.getPlaneManager()).getAll();
        all.forEach(plane -> planesListVBox.getChildren().add(getLabel(plane)));
    }

    @FXML
    public void gotoCreatePlane() {
        try {
            App.setRoot("createPlane");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Label getLabel(Plane plane) {
        class PlaneLabel extends Label {
            public PlaneLabel(Plane s) {
                super(s.toString());
            }
        }
        return new PlaneLabel(plane);
    }

}
