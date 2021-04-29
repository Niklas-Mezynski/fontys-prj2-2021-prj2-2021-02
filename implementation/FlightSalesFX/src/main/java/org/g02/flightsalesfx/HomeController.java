package org.g02.flightsalesfx;

import javafx.scene.control.Button;
import org.g02.flightsalesfx.businessEntities.Flight;
import org.g02.flightsalesfx.businessEntities.Plane;
import org.g02.flightsalesfx.businessEntities.Route;
import org.g02.flightsalesfx.gui.FlightTable;
import org.g02.flightsalesfx.gui.PlaneTable;
import org.g02.flightsalesfx.gui.RouteTable;
import org.g02.flightsalesfx.helpers.Bundle;
import org.g02.flightsalesfx.helpers.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

public class HomeController implements Controller {

    @FXML
    public VBox planesListVBox;
    @FXML
    public AnchorPane planePane;
    public MenuItem createPlane;
    @FXML
    private Button enableSalesprocess;
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
    private Flight selectedFlight;

    public void initialize() {
        enableSalesprocess.setDisable(true);
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
        planeTable.setId("planeTable");
        planesListVBox.getChildren().add(planeTable);
        System.out.println("planeTable inserted");
        planeTable.setMinWidth(planePane.getPrefWidth());

        //List all Routes
        createOrUpdateRouteTable(v -> true);


        var allFlights = App.businessLogicAPI.getAllFlights(f -> true);
        System.out.println(allFlights);
        var flightTable = new FlightTable(allFlights, (event, row) -> {
            selectedFlight = row.getItem();
            System.out.println("Clicked on: " + selectedFlight);
            if (event.getClickCount() == 1) {
                enableSalesprocess.setDisable(false);
            }
            if (event.getClickCount() == 2) {
                EditFlightController.selectedFlight = selectedFlight;
                App.inRootTab = 2;
                App.setRoot("editFlight");
            }
        });

        flightVBox.getChildren().add(flightTable);
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

    /*
     * return void.
     *
     * Method to start the salesprocess of a selectedFlight. The selected object is
     * first removed and then added again but in a modified version, by pressing the Button (UI).
     *
     */
    @FXML
    public void enableSalesprocess() throws IOException {
        //todo: popup
        //requires: flightobject (db-issues)

        if (selectedFlight != null) {
            final List<Flight> currentFlights = App.businessLogicAPI.getAllFlights(f -> f.getFlightNumber() == selectedFlight.getFlightNumber());
            System.out.println("got flight");
            if (!currentFlights.isEmpty()) {
                if (currentFlights.size() == 1) {
                    var flightOfList = currentFlights.get(0);
                    System.out.println("salesprocess started: " + selectedFlight);
                    //todo
                    //implement update persistently

                    //workaround:
                    if(App.persistenceAPI.getFlightStorageService(App.businessLogicAPI.getFlightManager()).remove(currentFlights.get(0))) {
                        if(selectedFlight.equals(flightOfList)) {
                            selectedFlight.startSalesProcess();
                            System.out.println("salesprocess started: " + selectedFlight);
                            App.persistenceAPI.getFlightStorageService(App.businessLogicAPI.getFlightManager()).add(selectedFlight);
                            System.out.println("applied to db");
                            
                        } else {
                            System.out.println("selected flight is not equal to the one received from the db.");
                        }
                    }
                    //Update is not working (dao)
                    //App.businessLogicAPI.updateFlight(FlightImpl.of(flightOfList), selectedFlight.getDeparture(), selectedFlight.getArrival(), selectedFlight.getPrice(), selectedFlight.getSalesProcessStatus());
                }
            } else {    // too many flights received OR no flights
                System.out.println("received not exactly one flight");
            }
        } else {    // nothing received from db
            //todo insert error
            System.out.println("empty set");
        }
    }

}
