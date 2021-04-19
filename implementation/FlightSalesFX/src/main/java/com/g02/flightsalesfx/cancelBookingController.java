package com.g02.flightsalesfx;


import com.g02.flightsalesfx.businessEntities.Booking;
import com.g02.flightsalesfx.businessEntities.Flight;
import com.g02.flightsalesfx.businessEntities.Seat;
import com.g02.flightsalesfx.businessEntities.Ticket;
import com.g02.flightsalesfx.gui.BookingTable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
        import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class cancelBookingController {

    @FXML
    private TextField flightNumberTextField;

    @FXML
    private TextField eMailTextField;

    @FXML
    private AnchorPane bookingFlightPane;

    private BookingTable bTable;

    private Booking selectedBooking;


    public void initialize(){
        List<Booking> bookings = App.businessLogicAPI.getAllBookings(booking -> booking.getFlight().getDeparture().isAfter(LocalDateTime.now()));
        createOrUpdateBookingTable(bookings);
        createSearchFunctionality();
    }

    public void createOrUpdateBookingTable(List<Booking> bookings){
        if(bTable != null){
            bookingFlightPane.getChildren().remove(bTable);
        }

        bTable = new BookingTable(bookings, (event, row) -> {
            if (!row.isEmpty()) {
                Booking rowData = row.getItem();

                if (event.getClickCount() == 1) {
                    this.selectedBooking = rowData;
                }
            }
        });
        bookingFlightPane.getChildren().add(bTable);
    }

    @FXML
    void abortButtonPressed(ActionEvent event) throws IOException {
        App.setRoot("salesEmployeeHome");
    }

    @FXML
    void cancelTicketPressed(ActionEvent event) {
        if(selectedBooking == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText("No booking selected");
            alert.setContentText("Please select a booking to be cancelled, then press the Button");
            alert.showAndWait();
        }else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you really want to cancel the selected booking?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();

            ButtonType result = alert.getResult();
            if(ButtonType.YES.equals(result)){
                List<Ticket> tickets = selectedBooking.getTickets();

                tickets.forEach(ticket -> {
                    selectedBooking.removeTicket(ticket);
                    App.persistenceAPI.getTicketStorageService(App.businessLogicAPI.getTicketManager()).remove(ticket);
                });

                App.persistenceAPI.getBookingStorageService(App.businessLogicAPI.getBookingManager()).remove(selectedBooking);
                createOrUpdateBookingTable(App.businessLogicAPI.getAllBookings(booking -> booking.getFlight().getDeparture().isAfter(LocalDateTime.now()) && booking.getCustomerEmail().contains(this.eMailTextField.getText()) && (booking.getFlight().getFlightNumber()+"").contains(this.flightNumberTextField.getText())));
            }
            if(ButtonType.NO.equals(result)){
            }
        }

    }

    @FXML
    void searchFieldInputDetected(ActionEvent event) {
    }

    public void createSearchFunctionality(){
        this.eMailTextField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            createOrUpdateBookingTable(App.businessLogicAPI.getAllBookings(booking -> booking.getFlight().getDeparture().isAfter(LocalDateTime.now()) && booking.getCustomerEmail().contains(newValue) && (booking.getFlight().getFlightNumber()+"").contains(this.flightNumberTextField.getText())));
        }));
        this.flightNumberTextField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            createOrUpdateBookingTable(App.businessLogicAPI.getAllBookings(booking -> booking.getFlight().getDeparture().isAfter(LocalDateTime.now()) && booking.getCustomerEmail().contains(this.eMailTextField.getText()) && (booking.getFlight().getFlightNumber()+"").contains(newValue)));
        }));

    }

}