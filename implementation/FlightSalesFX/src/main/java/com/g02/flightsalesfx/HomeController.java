package com.g02.flightsalesfx;

import com.g02.flightsalesfx.businessEntities.Plane;
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
        var all = App.businessLogicAPI.getAllPlanes(plane -> true);
//        all.forEach(plane -> planesListVBox.getChildren().add(getLabel(plane)));
//        planesListVBox.getChildren().add();
        var planeTable = new PlaneTable(all, (event, row) -> {
            if (!row.isEmpty()) {
                Plane rowData = row.getItem();
                /*if (event.getClickCount() == 2) {
                    System.out.println("Double click on: " + rowData.getName());
                } else */
                if (event.getClickCount() == 1) {
                    System.out.println("Normal click on: " + rowData.getName());
                    App.businessLogicAPI.viewPlane(rowData);
                }
            }
        });
        planesListVBox.getChildren().add(planeTable);
        planeTable.setMinWidth(planePane.getPrefWidth());
    }

    @FXML
    public void gotoCreatePlane() throws IOException {
        App.setRoot("createPlane");
    }


}
