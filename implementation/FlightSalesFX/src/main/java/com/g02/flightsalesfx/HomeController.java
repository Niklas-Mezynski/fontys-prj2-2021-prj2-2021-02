package com.g02.flightsalesfx;

import com.g02.flightsalesfx.gui.PlaneTable;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class HomeController {

    @FXML
    public VBox planesListVBox;
    @FXML
    public AnchorPane planePane;
    public MenuItem createPlane;


    public void initialize() {
        var all = App.persistenceAPI.getPlaneStorageService(App.businessLogicAPI.getPlaneManager()).getAll();
//        all.forEach(plane -> planesListVBox.getChildren().add(getLabel(plane)));
//        planesListVBox.getChildren().add();
        var planeTable = new PlaneTable(all);
        planesListVBox.getChildren().add(planeTable);
        planeTable.setMinWidth(planePane.getPrefWidth());
    }

    @FXML
    public void gotoCreatePlane() throws IOException {
        App.setRoot("createPlane");
    }


}
