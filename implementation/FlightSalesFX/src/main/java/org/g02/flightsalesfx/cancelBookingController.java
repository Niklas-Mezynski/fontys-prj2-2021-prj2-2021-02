package org.g02.flightsalesfx;


import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.g02.flightsalesfx.businessEntities.Booking;
import org.g02.flightsalesfx.businessEntities.Ticket;
import org.g02.flightsalesfx.gui.BookingTable;
import org.g02.flightsalesfx.helpers.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
        import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class cancelBookingController implements Controller {

    @FXML
    private TextField flightNumberTextField;

    @FXML
    private TextField eMailTextField;

    @FXML
    private AnchorPane bookingFlightPane;

    @FXML
    private VBox seatsVBox;

    @FXML
    private Label priceTag;

    @FXML
    private Label bookingEmailLabel;

    @FXML
    private Label bookingDateLabel;


    private BookingTable bTable;

    private Booking selectedBooking;


    public void initialize(){
        List<Booking> bookings = App.businessLogicAPI.getAllBookings(booking -> booking.getFlight().getDeparture().isAfter(LocalDateTime.now()));
        createOrUpdateBookingTable(bookings);
        createSearchFunctionality();
    }

    /**
     * creates an overview of bookings
     * @param bookings list of bookings to be included in the table
     */
    public void createOrUpdateBookingTable(List<Booking> bookings){
        if(bTable != null){
            bookingFlightPane.getChildren().remove(bTable);
        }

        bTable = new BookingTable(bookings, (event, row) -> {
            if (!row.isEmpty()) {
                Booking rowData = row.getItem();

                if (event.getClickCount() == 1) {
                    this.selectedBooking = rowData;
                    showOverviewForBooking(rowData);
                }
            }
        });
        bookingFlightPane.getChildren().add(bTable);
    }

    void showOverviewForBooking(Booking b){
        double price = b.getBookingPrice();
        priceTag.setText(price +"€");
        seatsVBox.getChildren().clear();
        var tickets = b.getTickets();
        for(Ticket t: tickets){
            StringBuilder ticketStr = new StringBuilder();
            ticketStr.append(t.getLastName()+", ");
            ticketStr.append(t.getFirstName()+", ");
            ticketStr.append(createBookingController.seatToText(t.getSeat())+", ");
            seatsVBox.getChildren().add(new Label(ticketStr.toString()));

        }
        bookingEmailLabel.setText(b.getCustomerEmail());
        bookingDateLabel.setText(b.getBookingDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }

    /**
     * when abort button is pressed you are redirected to previous page
     * @param event
     * @throws IOException
     */
    @FXML
    void abortButtonPressed(ActionEvent event) throws IOException {
        App.setRoot("salesEmployeeHome");
    }

    /**
     * Method gets called when "Cancel" Button is pressed.
     * Confirmation is required to cancel a booking
     * @param event
     */
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
                App.persistenceAPI.getBookingStorageService(App.businessLogicAPI.getBookingManager()).remove(selectedBooking);
                List<Ticket> tickets = selectedBooking.getTickets();

                tickets.forEach(ticket -> {
                    selectedBooking.removeTicket(ticket);
                    App.persistenceAPI.getTicketStorageService(App.businessLogicAPI.getTicketManager()).remove(ticket);
                });


                createOrUpdateBookingTable(App.businessLogicAPI.getAllBookings(booking -> booking.getFlight().getDeparture().isAfter(LocalDateTime.now()) && booking.getCustomerEmail().contains(this.eMailTextField.getText()) && (booking.getFlight().getFlightNumber()+"").contains(this.flightNumberTextField.getText())));
            }
            if(ButtonType.NO.equals(result)){
            }
        }

    }

    @FXML
    void searchFieldInputDetected(ActionEvent event) {
    }

    /**
     * enables the Testfields to act as search fields
     */
    public void createSearchFunctionality(){
        this.eMailTextField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            createOrUpdateBookingTable(App.businessLogicAPI.getAllBookings(booking -> booking.getFlight().getDeparture().isAfter(LocalDateTime.now()) && booking.getCustomerEmail().contains(newValue) && (booking.getFlight().getFlightNumber()+"").contains(this.flightNumberTextField.getText())));
        }));
        this.flightNumberTextField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            createOrUpdateBookingTable(App.businessLogicAPI.getAllBookings(booking -> booking.getFlight().getDeparture().isAfter(LocalDateTime.now()) && booking.getCustomerEmail().contains(this.eMailTextField.getText()) && (booking.getFlight().getFlightNumber()+"").contains(newValue)));
        }));

    }

}