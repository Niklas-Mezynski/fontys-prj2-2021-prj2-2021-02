package com.g02.flightsalesfx;

import com.g02.flightsalesfx.businessEntities.Flight;
import com.g02.flightsalesfx.businessEntities.Route;
import com.g02.flightsalesfx.gui.FlightTable;
import com.g02.flightsalesfx.gui.RouteTable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Locale;
import java.util.function.Predicate;

public class salesEmployeeHomeController {



    @FXML
    private MenuItem createPlane;

    @FXML
    private VBox flightVBox;

    @FXML
    private TextField departureField;

    @FXML
    private TextField arrivalField;



    private FlightTable flightTable;

    public void initialize(){

        createOrUpdateRouteTable(v -> true);
        createSearchFunctionality();

    }

    public void createSearchFunctionality(){
        departureField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            createOrUpdateRouteTable(f -> {
                String depField = newValue.toLowerCase();
                String arrField = arrivalField.getText().toLowerCase();
                String dep = f.getRoute().getDepartureAirport().toString().toLowerCase();
                String arr = f.getRoute().getArrivalAirport().toString().toLowerCase();
                return dep.contains(depField)&&arr.contains(arrField);
            });
        }));
        arrivalField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            createOrUpdateRouteTable(f -> {
                String depField = departureField.getText().toLowerCase();
                String arrField = newValue.toLowerCase();
                String dep = f.getRoute().getDepartureAirport().toString().toLowerCase();
                String arr = f.getRoute().getArrivalAirport().toString().toLowerCase();
                return dep.contains(depField)&&arr.contains(arrField);
            });
        }));

    }

    public void createOrUpdateRouteTable(Predicate<Flight> pr){

        flightVBox.getChildren().remove(flightTable);


        var allFlights = App.businessLogicAPI.getAllFlights(pr);

        flightTable = new FlightTable(allFlights, (event, row) -> {
            if (!row.isEmpty()) {
                Flight rowData = row.getItem();
                /*if (event.getClickCount() == 2) {
                    System.out.println("Double click on: " + rowData.getName());
                } else */
                if (event.getClickCount() == 2 && event.isControlDown()) {
                    System.out.println("Ctrl + click on: " + rowData.toString());
                    row.getTableView().refresh();
                }
            }
        });
        flightVBox.getChildren().add(flightTable);
        flightTable.setMinWidth(flightVBox.getWidth());
    }

    @FXML
    void cancelBookingPressed(ActionEvent event) throws IOException {
        App.setRoot("cancelBooking");
    }

    @FXML
    void createBookingPressed(ActionEvent event) throws IOException {
        App.setRoot("createBooking");
    }

    @FXML
    void gotoCreatePlane(ActionEvent event) {

    }

}
