package com.g02.flightsalesfx;

import com.g02.flightsalesfx.businessEntities.Booking;
import com.g02.flightsalesfx.businessEntities.Route;
import com.g02.flightsalesfx.helpers.Bundle;
import com.g02.flightsalesfx.helpers.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.List;

public class ViewSpecificRoutesKPIsController implements Controller {

    @FXML
    private Label titleLabel;

    @FXML
    private TextField totalRevenueField;

    private Route selectedRoute;

    @FXML
    void initialize() {
    }

    @Override
    public void init(Bundle bundle) {
        this.selectedRoute = (Route) bundle.get("route");
        titleLabel.setText("Stats for Route: " +selectedRoute.getDepartureAirport().getName() + " -> " + selectedRoute.getArrivalAirport().getName());

        List<Booking> bookings = App.businessLogicAPI.getAllBookings(booking -> booking.getFlight().getRoute().equals(selectedRoute));
        double sumRevenue = bookings.stream().
                flatMap(booking -> booking.getTickets().stream())
                .mapToDouble(ticket -> ticket.getFlight().getPrice()) //TODO later have to include prices of different SeatOptions etc... (Maybe directly save the final price of a booking in the object?)
                .sum();

        totalRevenueField.setText(String.format("%.2f", sumRevenue) + "€");
    }
}
