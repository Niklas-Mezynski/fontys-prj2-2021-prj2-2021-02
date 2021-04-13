package com.g02.flightsalesfx;

import com.g02.flightsalesfx.businessEntities.Flight;
import com.g02.flightsalesfx.businessEntities.Plane;
import com.g02.flightsalesfx.businessEntities.Seat;
import com.g02.flightsalesfx.gui.FlightTable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    @FXML
    private HBox seatHBox;


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
                    seatsTab.setDisable(false);
                    selectedFlightText.setText("FlightNo: "+rowData.getFlightNumber()+"; From: "+rowData.getRoute().getDepartureAirport().toString()+"; To: "+rowData.getRoute().getArrivalAirport().toString()+"; On: "+rowData.getDeparture().toString());
                    availableSeatsText.setText(rowData.getPlane().getSeatCount()+"");
                    row.getTableView().refresh();
                    createSeatMap();

                }
            }
        });
        flightVBox.getChildren().add(flightTable);
        flightTable.setMinWidth(flightVBox.getWidth());
    }

    public void createSeatMap(){
        Plane currentPlane = selectedFlight.getPlane();
        List<Seat> seatsOfPlane = currentPlane.getAllSeats();
        int rows = seatsOfPlane.stream().mapToInt(Seat::getRowNumber).max().orElse(-1) + 1;
        Map<Integer, List<Seat>> seatMap = new HashMap<Integer, List<Seat>>();

        for(int i = 0; i < rows; i++){
            int currentRow = i;
            Comparator<Seat> comparator = (Seat s1, Seat s2) -> s1.getSeatNumber()- s2.getSeatNumber();
            List<Seat> row = seatsOfPlane.stream().filter(seat -> seat.getRowNumber()==currentRow).sorted(comparator).collect(Collectors.toUnmodifiableList());
            seatMap.put(i, row);
            VBox rowBox = new VBox();
            seatsOfPlane.stream().filter(seat -> seat.getRowNumber()==currentRow).sorted(comparator).forEach(seat -> {
                var addButton = new SeatBookButton(seat);

                rowBox.getChildren().add(addButton);
            });

            seatHBox.getChildren().add(rowBox);
        }


    }

    public class SeatBookButton extends Button {

        private final Seat s;
        private boolean available = true;

        public SeatBookButton(Seat s) {
            this.s = s;
            System.out.println(s.toString());
            this.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
            this.setFont(Font.font("Source Code Pro Semibold"));
            setOnAction(actionEvent -> {

            });
            updateText();
            App.businessLogicAPI.getAllBookings(booking -> true);
        }


        /**
         * Updates the text and converts row and seat number to a better readable string consisting of
         */
        void updateText() {
            var i = s.getSeatNumber();
            var i1 = s.getRowNumber() + 1;
            String s = String.format("%02d", i1);
            if (i < 26) {
                s += String.valueOf((char) (i + 65));
            } else {
                s += String.valueOf((char) ((i / 26) + 64));
                s += String.valueOf((char) (i + 65 - 26));
            }
//            if (currentSelected != null && options.contains(currentSelected)) {
//                //s += ": X";
//                this.setStyle("-fx-text-fill: #007698; ");
//            } else {
//                this.setStyle("");
//            }
            this.setText(s);
        }


    }

    @FXML
    void abortButtonPressed(ActionEvent event) throws IOException {
        App.setRoot("salesEmployeeHome");
    }

}
