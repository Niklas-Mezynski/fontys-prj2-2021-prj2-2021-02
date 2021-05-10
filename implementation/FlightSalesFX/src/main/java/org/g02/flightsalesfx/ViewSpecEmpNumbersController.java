package org.g02.flightsalesfx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.g02.flightsalesfx.businessEntities.Booking;
import org.g02.flightsalesfx.businessLogic.SalesEmployeeImpl;
import org.g02.flightsalesfx.helpers.Bundle;
import org.g02.flightsalesfx.helpers.Controller;

import java.util.List;
import java.util.OptionalDouble;

public class ViewSpecEmpNumbersController implements Controller {

    @FXML
    private Label titleLabel;

    @FXML
    private AnchorPane numberStatsView;

    @FXML
    private TextField totalRevenueField;

    @FXML
    private TextField noBookings;

    @FXML
    private TextField avgTickets;

    SalesEmployeeImpl emp;

    @Override
    public void init(Bundle bundle) {
        emp = (SalesEmployeeImpl) bundle.get("emp");
        titleLabel.setText("Stats for employee '" + emp.getName() + "'");

        List<Booking> allBookings = App.businessLogicAPI.getAllBookings(booking -> booking.getSalesEmployee().equals(emp));

        //Calculating totalRevenue
        double sum = allBookings.stream()
                .mapToDouble(booking -> booking.getFlight().getPrice()) //TODO get booking price instead of flight price
                .sum();
        totalRevenueField.setText(String.format("%.2f", sum) + "€");

        //Calculating No. of Bookings
        long numOfBookings = allBookings.size();
        noBookings.setText(String.valueOf(numOfBookings));

        //Calculating avgNumberOfTicketsPerBooking
        OptionalDouble avgTicketAmount = allBookings.stream()
                .mapToInt(booking -> booking.getTickets().size())
                .average();
        avgTickets.setText(String.valueOf(avgTicketAmount.orElse(0)));
    }

    @FXML
    void switchView() {

    }

    @FXML
    void backToMenu() {
        App.setRoot("managementDashboard");
    }
}
