package com.g02.flightsalesfx;

import com.g02.flightsalesfx.businessEntities.Flight;
import com.g02.flightsalesfx.businessEntities.Plane;
import com.g02.flightsalesfx.businessEntities.Route;
import com.g02.flightsalesfx.gui.FlightTable;
import com.g02.flightsalesfx.gui.PlaneTable;
import com.g02.flightsalesfx.gui.RouteTable;
import com.g02.flightsalesfx.helpers.Bundle;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.function.Predicate;

public class HomeController {

    @FXML
    public VBox planesListVBox;
    @FXML
    public AnchorPane planePane;
    public MenuItem createPlane;
    @FXML
    public VBox routeListVBox;
    @FXML
    public AnchorPane routePane;
    @FXML
    public TabPane tabPane;

    @FXML
    private VBox flightVBox;
    @FXML
    private AnchorPane flightPane;
    @FXML
    private CheckBox showDisabled;

    private RouteTable routeTable;

    public void initialize() {
        tabPane.getSelectionModel().select(App.inRootTab);
        var all = App.businessLogicAPI.getAllPlanes(plane -> true);
//        all.forEach(plane -> planesListVBox.getChildren().add(getLabel(plane)));
//        planesListVBox.getChildren().add();
        var planeTable = new PlaneTable(all, (event, row) -> {
            if (!row.isEmpty()) {
                Plane rowData = row.getItem();
                if (event.getClickCount() == 2) {
                    System.out.println("Double click on: " + rowData.getName());
                    var bundle = new Bundle();
                    bundle.add("edit", true);
                    bundle.add("plane", rowData);
                    App.setRoot("createPlane", bundle);
                } else if (event.getClickCount() == 1) {
                    System.out.println("Normal click on: " + rowData.getName());
                    App.businessLogicAPI.viewPlane(rowData);
                }
            }
        });
//        tabPane.getSelectionModel().select(App.homeControllerTab);
        planesListVBox.getChildren().add(planeTable);
        planeTable.setMinWidth(planePane.getPrefWidth());

        //List all Routes
        createOrUpdateRouteTable(v -> true);


        var allFlights = App.businessLogicAPI.getAllFlights(f -> true);
        System.out.println(allFlights);
        var flightTable = new FlightTable(allFlights, (event, row) -> {
            Flight selectedFlight = row.getItem();
            System.out.println("Clicked on: " + selectedFlight);
            if (event.getClickCount() == 2) {
                EditFlightController.selectedFlight = selectedFlight;
                App.inRootTab = 2;
                App.setRoot("editFlight");
            }
        });

        flightPane.getChildren().add(flightTable);
        flightTable.setMinWidth(flightPane.getPrefWidth());

    }

    public void createOrUpdateRouteTable(Predicate<Route> pr) {

        routeListVBox.getChildren().remove(routeTable);


        var allRoutes = App.businessLogicAPI.getAllRoutes(pr);

        routeTable = new RouteTable(allRoutes, (event, row) -> {
            if (!row.isEmpty()) {
                Route rowData = row.getItem();
                /*if (event.getClickCount() == 2) {
                    System.out.println("Double click on: " + rowData.getName());
                } else */
                if (event.getClickCount() == 2 && event.isControlDown()) {
                    System.out.println("Ctrl + click on: " + rowData.toString() + rowData.getEnabled());
                    rowData.toggleEnable();
                    row.getTableView().refresh();
                    //Todo save change in PersistanceLayer
                }
            }
        });
        routeListVBox.getChildren().add(routeTable);
        routeTable.setMinWidth(routePane.getPrefWidth());
    }

    @FXML
    public void showDisabledUpdated() {
        if (showDisabled.isSelected()) {
            createOrUpdateRouteTable(r -> true);
        } else {
            createOrUpdateRouteTable(r -> r.getEnabled());
        }
    }


    @FXML
    public void gotoCreatePlane() throws IOException {
        App.inRootTab = 0;
        App.setRoot("createPlane");
    }

    @FXML
    public void goToCreateRoute() throws IOException {
        App.inRootTab = 1;
        App.setRoot("createRoute");
    }

    @FXML
    public void goToCreateFlight() throws IOException {
        App.inRootTab = 2;
        App.setRoot("createFlight");
    }

}
