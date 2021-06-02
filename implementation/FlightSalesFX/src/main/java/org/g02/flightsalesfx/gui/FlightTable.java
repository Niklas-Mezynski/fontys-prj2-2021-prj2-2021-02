package org.g02.flightsalesfx.gui;

import org.g02.flightsalesfx.businessEntities.Flight;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;

import java.util.List;

public class FlightTable extends TableView<Flight>{
    public FlightTable (List<Flight> flights, RowClickHandler<Flight> clickHandler) {

        TableColumn<Flight, String> flightNumber = new TableColumn("Flight_No");
        flightNumber.setCellValueFactory(v -> {return new SimpleStringProperty(v.getValue().getFlightNumber()+"");});
        TableColumn<Flight, String> departAirport = new TableColumn("Dep.-Airport");
        departAirport.setCellValueFactory(v -> {return new SimpleStringProperty(v.getValue().getRoute().getDepartureAirport().getCity());});
        TableColumn<Flight, String> departureTime = new TableColumn("Dep.-Time");
        departureTime.setCellValueFactory(v -> {return new SimpleStringProperty(v.getValue().getDeparture().toString());});
        TableColumn<Flight, String> arrivalAirport = new TableColumn("Arr.-Airport");
        arrivalAirport.setCellValueFactory(v -> {return new SimpleStringProperty(v.getValue().getRoute().getArrivalAirport().getCity());});
        TableColumn<Flight, String> arrivalTime = new TableColumn("Arr.-Time");
        arrivalTime.setCellValueFactory(v -> {return new SimpleStringProperty(v.getValue().getArrival().toString());});
        TableColumn<Flight, String> planeType = new TableColumn("Type");
        planeType.setCellValueFactory(v -> {return new SimpleStringProperty(v.getValue().getPlane().getType());});
        TableColumn<Flight, String> planeID = new TableColumn("P.-ID");
        planeID.setCellValueFactory(v -> {return new SimpleStringProperty(v.getValue().getPlane().getName());});
        TableColumn<Flight, String> salesProcessStatus = new TableColumn("Salesprocess");
        salesProcessStatus.setCellValueFactory(v -> {return new SimpleStringProperty(displaySalesprocessAsString(v.getValue().getSalesProcessStatus()));});

        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        getItems().addAll(flights);
        getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        getColumns().addAll(flightNumber, departAirport, departureTime, arrivalAirport, arrivalTime, planeType, planeID, salesProcessStatus);
        setRowFactory(fltTableView -> {
            TableRow<Flight> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                clickHandler.click(mouseEvent, row);
            });
            return row ;
        });
    }

    String displaySalesprocessAsString(Boolean value) {
        if(value) {
            return "started";
        }
        return "stopped";
    }
}
