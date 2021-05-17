package org.g02.flightsalesfx;

import javafx.scene.control.*;
import org.g02.flightsalesfx.businessEntities.Flight;
import org.g02.flightsalesfx.businessEntities.Plane;
import org.g02.flightsalesfx.businessEntities.Route;
import org.g02.flightsalesfx.businessLogic.FlightImpl;
import org.g02.flightsalesfx.gui.FlightTable;
import org.g02.flightsalesfx.gui.PlaneTable;
import org.g02.flightsalesfx.gui.RouteTable;
import org.g02.flightsalesfx.helpers.Bundle;
import org.g02.flightsalesfx.helpers.Controller;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
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

    FlightTable flightTable;

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
        flightTable = new FlightTable(allFlights, (event, row) -> {
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

    /**
     * Requires a selected flight (clicked on by User). Else, throw exception.
     *
     * Checks if the salesprocess for this flight has already started. If it has, it raises an alert
     * and offers an option to stop the salesprocess or to cancel the process.
     *
     * If the salesprocess has not been started yet, the selected flights is getting updated using the businesslogicAPI
     * that uses the persistence layer and the DAO to finalize the process.
     *
     * @throws IOException
     */
    @FXML
    public void enableSalesprocess() throws IOException {
        if (selectedFlight != null) {
            //get selected flight from db -> useful to avoid any errors/bugs when changing the same object multiple times
            final List<Flight> selectedFlightFromDBAsList = App.businessLogicAPI.getAllFlights(f -> f.getFlightNumber() == selectedFlight.getFlightNumber());

            if(selectedFlightFromDBAsList.size() == 1) {
                if(selectedFlightFromDBAsList.get(0).getSalesProcessStatus()) {
                    var buttonPressed = handleStartedSalesprocess();
                    if(buttonEqualsOk(buttonPressed)) {
                        App.businessLogicAPI.updateFlight((FlightImpl) selectedFlight, selectedFlight.getDeparture(), selectedFlight.getArrival(), selectedFlight.getPrice(), false);
                        flightTable.refreshTable();
                    }
                    return;
                }
                var buttonPressed = handleNotStartedSalesprocess(selectedFlightFromDBAsList.get(0));
                if(buttonEqualsOk(buttonPressed)) {
                    App.businessLogicAPI.updateFlight((FlightImpl) selectedFlight, selectedFlight.getDeparture(), selectedFlight.getArrival(), selectedFlight.getPrice(), true);
                }
            } else {
                dataErrorAlert();
                return;
            }
        } else {
            throw new IOException();
        }

        //refresh the table to apply changes in UI
        flightTable.refreshTable();
    }

    /**
     * HELPER
     *
     * Appearing alertmessage that offers the opportunity to stop the salesprocess.
     *
     * @return button pressed by user
     */
    Optional<ButtonType> handleStartedSalesprocess() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION,
                "The Salesprocess for the selected flight has been started earlier. \n" +
                        "Do you want to stop the salesprocess?",
                ButtonType.OK,
                ButtonType.CANCEL);
        alert.setTitle("Salesprocess is started");
        return alert.showAndWait();
    }

    /**
     * HELPER
     *
     * Appearing alertmessage that offers the opportunity to confirm the start of the salesprocess.
     *
     * @param selectedFlight selected flight received from the database
     *
     * @return button pressed by user
     */
    Optional<ButtonType> handleNotStartedSalesprocess(Flight selectedFlight) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Do you want to proceed and start the salesprocess for this flight? \n" + selectedFlight.toString(),
                ButtonType.OK,
                ButtonType.CANCEL
        );
        alert.setTitle("Confirm to start Salesprocess");
        return alert.showAndWait();
    }

    /**
     * HELPER
     *
     * @param decision the pressed button
     * @return true, if the pressed button is of type "OK"
     */
    boolean buttonEqualsOk(Optional<ButtonType> decision) {
        return decision.get() == (ButtonType.OK);
    }

    /**
     * Error message that informs about issues with data received from database
     */
    void dataErrorAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Problems during processing selectedflight information.");
        alert.setContentText("There occurred an issue while retrieving the requested data.");
        alert.showAndWait();
    }
}
