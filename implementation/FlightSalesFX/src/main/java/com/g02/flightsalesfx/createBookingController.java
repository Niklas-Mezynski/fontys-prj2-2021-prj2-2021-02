package com.g02.flightsalesfx;

import com.g02.flightsalesfx.businessEntities.Flight;
import com.g02.flightsalesfx.gui.FlightTable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.function.Predicate;

public class createBookingController {

    @FXML
    private Tab flightTab;

    @FXML
    private VBox flightVBox;

    @FXML
    private TextField departureField;

    @FXML
    private TextField arrivalField;

    @FXML
    private Tab paxTab;

    @FXML
    private Tab seatsTab;

    @FXML
    private Tab seatOptionsTab;

    @FXML
    private Tab flightOptionsTab;

    @FXML
    private Text selectedFlightText;

    @FXML
    private Text availableSeatsText;


    private FlightTable flightTable;

    private Flight selectedFlight = null;

    public void initialize(){

        createOrUpdateRouteTable(v -> true);
        createSearchFunctionality();

        paxTab.setDisable(true);
        seatsTab.setDisable(true);
        seatOptionsTab.setDisable(true);
        flightOptionsTab.setDisable(true);

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

                if (event.getClickCount() == 1) {
                    System.out.println("Selected FLight: " + rowData.toString());
                    selectedFlight = rowData;
                    paxTab.setDisable(false);
                    selectedFlightText.setText("FlightNo: "+rowData.getFlightNumber()+"; From: "+rowData.getRoute().getDepartureAirport().toString()+"; To: "+rowData.getRoute().getArrivalAirport().toString()+"; On: "+rowData.getDeparture().toString());
                    availableSeatsText.setText(rowData.getPlane().getSeatCount()+"");
                    row.getTableView().refresh();

                }
            }
        });
        flightVBox.getChildren().add(flightTable);
        flightTable.setMinWidth(flightVBox.getWidth());
    }

    @FXML
    void abortButtonPressed(ActionEvent event) throws IOException {
        App.setRoot("salesEmployeeHome");
    }

}
