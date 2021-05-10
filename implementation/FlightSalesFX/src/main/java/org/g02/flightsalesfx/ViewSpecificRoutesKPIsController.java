package org.g02.flightsalesfx;

import javafx.scene.layout.BorderPane;
import org.g02.flightsalesfx.businessEntities.Booking;
import org.g02.flightsalesfx.businessEntities.Route;
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
import java.util.List;
import java.util.OptionalInt;

public class ViewSpecificRoutesKPIsController implements Controller {


    @FXML
    private Label titleLabel;

    @FXML
    private AnchorPane numberStatsView;

    @FXML
    private TextField totalRevenueField;

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

        //Calculate the total revenue for this route
        List<Booking> bookings = App.businessLogicAPI.getAllBookings(booking -> booking.getFlight().getRoute().equals(selectedRoute));
        double sumRevenue = bookings.stream()
//                .flatMap(booking -> booking.getTickets().stream())
                .mapToDouble(booking -> booking.getFlight().getPrice()) //TODO later have to include prices of different SeatOptions etc... (Maybe directly save the final price of a booking in the object?)
                .sum();

        totalRevenueField.setText(String.format("%.2f", sumRevenue) + "€");



        //Creating the NumberAxis
        OptionalInt minBookingYear = bookings.stream().mapToInt(booking -> booking.getFlight().getDeparture().getYear()).min();
        int currentYear = LocalDate.now().getYear();
        final NumberAxis xAxis = new NumberAxis(minBookingYear.orElse(2010) - 1, currentYear + 1, 1);
        final NumberAxis yAxis = new NumberAxis();
        AreaChart<Number, Number> areaChart = new AreaChart<Number, Number>(xAxis, yAxis);
        XYChart.Series<Number, Number> routeSeries = new XYChart.Series();
        routeSeries.setName(selectedRoute.toString());

//        Sample Data
//        routeSeries.getData().add(new XYChart.Data(2010, 55000.0));
//        routeSeries.getData().add(new XYChart.Data(2011, 59000.0));
//        routeSeries.getData().add(new XYChart.Data(2012, 70000.0));
//        routeSeries.getData().add(new XYChart.Data(2013, 62000.0));

        //Calculate XYChart.Data for each year
        for (int year = minBookingYear.orElse(2010); year <= currentYear; year++ ) {
            int finalYear = year;
            double sumRevenueOneYear = bookings.stream()
                    .filter(booking -> booking.getFlight().getDeparture().getYear() == finalYear)
                    .flatMap(booking -> booking.getTickets().stream())
                    .mapToDouble(ticket -> ticket.getFlight().getPrice()) //TODO later have to include prices of different SeatOptions etc... (Maybe directly save the final price of a booking in the object?)
                    .sum();
            routeSeries.getData().add(new XYChart.Data(year, sumRevenueOneYear));
        }

        //Add everything to the area chart
        areaChart.getData().add(routeSeries);
        areaChart.setMinWidth(chartView.getPrefWidth());
        areaChart.setMinHeight(chartView.getPrefHeight());
        System.out.println(chartView.getPrefWidth());
        chartPane.setCenter(areaChart);
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
