package com.g02.flightsalesfx;

import com.g02.flightsalesfx.gui.PlaneTable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class HomeController {

    @FXML
    public VBox planesListVBox;


    public void initialize() {
        var all = App.persistenceAPI.getPlaneStorageService(App.businessLogicAPI.getPlaneManager()).getAll();
//        all.forEach(plane -> planesListVBox.getChildren().add(getLabel(plane)));
//        planesListVBox.getChildren().add();
        var planeTable = new PlaneTable(all);
        planesListVBox.getChildren().add(planeTable);
        planeTable.setMinWidth(planePane.getPrefWidth());
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
