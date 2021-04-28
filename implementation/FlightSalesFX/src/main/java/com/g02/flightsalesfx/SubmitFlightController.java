package com.g02.flightsalesfx;

import com.g02.flightsalesfx.businessEntities.Airport;
import com.g02.flightsalesfx.businessEntities.Plane;
import com.g02.flightsalesfx.businessEntities.SalesOfficer;
import com.g02.flightsalesfx.gui.PlaneTable;
import com.g02.flightsalesfx.helpers.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.g02.flightsalesfx.App.setRoot;

public class SubmitFlightController implements Controller {

    @FXML
    private TextField planeSearchBar;

    @FXML
    private AnchorPane planeTablePane;

    @FXML
    private TextField flightPrice;

    @FXML
    private Button saveFlightButton;

    @FXML
    private Button exitFlightButton;

    @FXML
    private TextField flightNumberTextField;

    private List<Plane> selectedPlanes;
    private PlaneTable planeTable;
    private Plane selectedPlane = null;

    static public boolean helperIsPlaneAvailable(Plane searchedPlane, LocalDateTime searchedTime, Airport searchAirport) {
        var flightsArr = App.businessLogicAPI.getAllFlights(s -> s.getPlane().equals(searchedPlane));
        var flightsDev = App.businessLogicAPI.getAllFlights(s -> s.getPlane().equals(searchedPlane));
        /*
        remove all flight that are not by the plane searched for
        remove all flights that are in air at the time
        if in air return null
        retrace the flight order and select the airport the plane is at
         */
        if (flightsArr.isEmpty()) {
            return true;
        }
        flightsArr = flightsArr.stream().filter(s -> s.getArrival().isBefore(searchedTime)).collect(Collectors.toList());
        flightsDev = flightsDev.stream().filter(s -> s.getDeparture().isAfter(searchedTime)).collect(Collectors.toList());
        if (flightsArr.isEmpty() && flightsDev.isEmpty()) {
            return false;
        }
        if (flightsArr.isEmpty()) {
            var flightDepMin = flightsDev.stream().min((s1, s2) -> s1.getDeparture().compareTo(s2.getDeparture())).get();
            return flightDepMin.getRoute().getDepartureAirport().equals(searchAirport);
        }
        if (flightsDev.isEmpty()) {
            var flightArrMax = flightsArr.stream().max((s1, s2) -> s1.getArrival().compareTo(s2.getArrival())).get();
            return flightArrMax.getRoute().getArrivalAirport().equals(searchAirport);
        }

        var flightArrMax = flightsArr.stream().max((s1, s2) -> s1.getArrival().compareTo(s2.getArrival())).get();
        var flightDepMin = flightsDev.stream().min((s1, s2) -> s1.getDeparture().compareTo(s2.getDeparture())).get();
        return (flightDepMin.getRoute().getDepartureAirport().equals(flightArrMax.getRoute().getArrivalAirport())) && searchAirport.equals(flightDepMin.getRoute().getDepartureAirport());
    }

    public void initialize() {
        selectedPlanes = App.businessLogicAPI.getAllPlanes(route -> true);

        planeSearchBar.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            updatePlanes(newValue);
        }));

        createPlaneTableWithData(selectedPlanes);


    }

    private void createPlaneTableWithData(List<Plane> planes) {
        planeTablePane.getChildren().remove(planeTable);
        var extendedRoute = CreateFlightController.getExtendedRoute();
        planes = planes.stream().filter(s -> helperIsPlaneAvailable(s, extendedRoute.getDepartureDateWithTime(), extendedRoute.getSelectedRoute().getDepartureAirport())).collect(Collectors.toList());
        planeTable = new PlaneTable(planes, (event, row) -> {
            if (!row.isEmpty()) {
                Plane rowData = row.getItem();
                if (event.getClickCount() == 1) {
                    System.out.println("Selected Plane: " + rowData.toString());
                    selectedPlane = rowData;
                }
            }
        });
        planeTablePane.getChildren().add(planeTable);

    }

    private void updatePlanes(String term) {
        String lowerTerm = term.toLowerCase();
        selectedPlanes = App.businessLogicAPI.getAllPlanes(plane -> {
            if (plane.toString().toLowerCase().contains(lowerTerm)) {
                return true;
            }
            return false;
        });
        createPlaneTableWithData(selectedPlanes);

    }

    @FXML
    void exit() throws IOException {
        setRoot("home");
    }

    @FXML
    void saveFlight(ActionEvent event) throws IOException {
        double price = -1;
        boolean conversionOK = true;
        try {
            if (flightPrice.getText().contains(",")) {
                price = Integer.valueOf(flightPrice.getText().split(",")[0]) + (Double.valueOf(flightPrice.getText().split(",")[1]) / 100);
            } else {
                price = Double.valueOf(flightPrice.getText());
            }

        } catch (Exception e) {
            if (e.getClass().equals(NumberFormatException.class)) {
                conversionOK = false;
            }
        }

        var plane = selectedPlane;
        var creator = (SalesOfficer) App.employee;                  // ToDo: verify that only officer can register new flights

        // content of previous scene
        var extendedRoute = CreateFlightController.getExtendedRoute();
        var route = extendedRoute.getSelectedRoute();
        var depDateTime = extendedRoute.getDepartureDateWithTime();
        var arrDateTime = extendedRoute.getArrivalDateWithTime();

        if (creator != null && depDateTime != null && arrDateTime != null && route != null && plane != null && price != -1 && conversionOK) {

            var flightCreated = App.businessLogicAPI.createFlightFromUI(creator, depDateTime, arrDateTime, route, plane, price);

            if (flightCreated) {

                exit();

            } else {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error saving flight");
                alert.setContentText("There was an error while saving the created flight. Try again!");
                alert.showAndWait();

            }

        } else {
            System.out.println("Alarm");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Check your inputs");
            alert.setContentText("Check, that all inputs have correct values and a plane is selected.");
            alert.showAndWait();

        }


    }

}
