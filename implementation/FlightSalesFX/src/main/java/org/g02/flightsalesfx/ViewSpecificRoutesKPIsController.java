package org.g02.flightsalesfx;

import javafx.scene.layout.BorderPane;
import org.g02.flightsalesfx.businessEntities.Booking;
import org.g02.flightsalesfx.businessEntities.Option;
import org.g02.flightsalesfx.businessEntities.Route;
import org.g02.flightsalesfx.businessEntities.SeatOption;
import org.g02.flightsalesfx.helpers.Bundle;
import org.g02.flightsalesfx.helpers.Controller;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Stream;

public class ViewSpecificRoutesKPIsController implements Controller {


    @FXML
    private Label titleLabel;

    @FXML
    private AnchorPane numberStatsView;

    @FXML
    private TextField totalRevenueField;

    @FXML
    private TextField businessClassRevenue;

    @FXML
    private TextField firstClassRevenue;

    @FXML
    private AnchorPane chartView;

    @FXML
    private BorderPane chartPane;

    private Route selectedRoute;

    @FXML
    void initialize() {
    }

    @Override
    public void init(Bundle bundle) {
        this.selectedRoute = (Route) bundle.get("route");
        titleLabel.setText("Stats for Route: " +selectedRoute.getDepartureAirport().getName() + " -> " + selectedRoute.getArrivalAirport().getName());

        //Get all Bookings for the specified route
        List<Booking> bookings = App.businessLogicAPI.getAllBookings(booking -> booking.getFlight().getRoute().equals(selectedRoute));

        //Calculate the total revenue for this route
        double sumRevenue = App.businessLogicAPI.sumRevenue(bookings, booking -> true);

        totalRevenueField.setText(String.format("%.2f", sumRevenue) + "€");

        //Calculate revenue for Tickets with SeatOption "Business Class"
        double sumBusinessClass = bookings.stream()
                .flatMap(booking -> booking.getTickets().stream())
                .flatMap(ticket -> Arrays.stream(ticket.getSeatOptions()))
                .filter(seatOption -> seatOption.getName().toLowerCase().contains("business") && seatOption.getName().toLowerCase().contains("class"))
                .mapToDouble(Option::getPrice)
                .sum();

        businessClassRevenue.setText(String.format("%.2f", sumBusinessClass) + "€");

        // Calculate revenue for Tickets with SeatOption "First Class"
        double sumFirstClass = bookings.stream()
                .flatMap(booking -> booking.getTickets().stream())
                .flatMap(ticket -> Arrays.stream(ticket.getSeatOptions()))
                .filter(seatOption -> seatOption.getName().toLowerCase().contains("first") && seatOption.getName().toLowerCase().contains("class"))
                .mapToDouble(Option::getPrice)
                .sum();

        firstClassRevenue.setText(String.format("%.2f", sumBusinessClass) + "€");


        /*
        *
        * Here the Chart is created
        *
         */

        //Creating the NumberAxis
        OptionalInt minBookingYear = bookings.stream().mapToInt(booking -> booking.getBookingDate().getYear()).min();
        int currentYear = LocalDate.now().getYear();
        final NumberAxis xAxis = new NumberAxis(minBookingYear.orElse(2010) - 1, currentYear + 1, 1);
        final NumberAxis yAxis = new NumberAxis();
        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        XYChart.Series<Number, Number> routeSeries = new XYChart.Series();
        routeSeries.setName(selectedRoute.toString());

        for (int year = minBookingYear.orElse(2010); year <= currentYear; year++ ) {
            int finalYear = year;
            double sumRevenueOneYear = bookings.stream()
                    .filter(booking -> booking.getBookingDate().getYear() == finalYear)
                    .mapToDouble(Booking::getBookingPrice)
                    .sum();
            routeSeries.getData().add(new XYChart.Data(year, sumRevenueOneYear));
        }

        //Add everything to the area chart
        chart.getData().add(routeSeries);
        chart.setMinWidth(chartView.getPrefWidth());
        chart.setMinHeight(chartView.getPrefHeight());
        chartPane.setCenter(chart);
    }

    @FXML
    void switchView() {
        if (numberStatsView.isVisible()) {
            numberStatsView.visibleProperty().set(false);
            chartView.visibleProperty().set(true);
        } else {
            chartView.visibleProperty().set(false);
            numberStatsView.visibleProperty().set(true);
        }
    }

    @FXML
    void backToMenu() {
        App.setRoot("managementDashboard");
    }
}
