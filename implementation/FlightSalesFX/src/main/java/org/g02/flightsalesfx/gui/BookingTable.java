package org.g02.flightsalesfx.gui;

import org.g02.flightsalesfx.businessEntities.Booking;
import org.g02.flightsalesfx.businessEntities.Route;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;

import java.util.List;

public class BookingTable extends TableView<Booking>{

    public BookingTable (List<Booking> bookings, RowClickHandler<Booking> clickHandler) {

        TableColumn<Booking,String> bookingNr = new TableColumn("Booking Nr");
        bookingNr.setCellValueFactory(param -> {
            Booking b = param.getValue();
            return new SimpleStringProperty(b.getID().get()+"");
        });
        TableColumn<Booking,String> flightColumn = new TableColumn("FlightNo");
        flightColumn.setCellValueFactory(param -> {
            Booking b = param.getValue();
            return new SimpleStringProperty(b.getFlight().getFlightNumber()+"");
        });
        TableColumn<Booking,String> emailColumn = new TableColumn("E-Mail");
        emailColumn.setCellValueFactory(param -> {
            Booking b = param.getValue();
            return new SimpleStringProperty(b.getCustomerEmail());
        });
        TableColumn<Booking,String> priceColumn = new TableColumn("Price Paid");
        priceColumn.setCellValueFactory(param -> {
            Booking b = param.getValue();
            return new SimpleStringProperty(b.getBookingPrice()+"€");
        });
        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        getSelectionModel().setSelectionMode(SelectionMode.SINGLE);






        getItems().addAll(bookings);
        getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        getColumns().addAll(bookingNr, flightColumn, emailColumn, priceColumn);
        setRowFactory(bookTableView -> {
            TableRow<Booking> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                clickHandler.click(mouseEvent, row);
            });
            return row ;
        });
    }
}



