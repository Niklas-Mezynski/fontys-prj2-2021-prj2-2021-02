package com.g02.flightsalesfx;

import com.g02.flightsalesfx.businessEntities.Booking;
import com.g02.flightsalesfx.businessEntities.Route;
import com.g02.flightsalesfx.helpers.Bundle;
import com.g02.flightsalesfx.helpers.Controller;
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
        double sumRevenue = bookings.stream().
                flatMap(booking -> booking.getTickets().stream())
                .mapToDouble(ticket -> ticket.getFlight().getPrice()) //TODO later have to include prices of different SeatOptions etc... (Maybe directly save the final price of a booking in the object?)
                .sum();

        totalRevenueField.setText(String.format("%.2f", sumRevenue) + "€");



        //Fill Line Chart with data
        OptionalInt minBookingYear = bookings.stream().mapToInt(booking -> booking.getFlight().getDeparture().getYear()).min();
        int currentYear = LocalDate.now().getYear();
        final NumberAxis xAxis = new NumberAxis(minBookingYear.orElse(2010) - 1, currentYear + 1, 1);
        final NumberAxis yAxis = new NumberAxis();
        AreaChart<Number, Number> areaChart = new AreaChart<Number, Number>(xAxis, yAxis);
        XYChart.Series routeSeries = new XYChart.Series();
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


        areaChart.getData().add(routeSeries);
        chartView.getChildren().add(areaChart);
        areaChart.setMinWidth(chartView.getWidth());
        areaChart.setMinHeight(chartView.getHeight());
    }

    @FXML
    void switchView() {
        if (numberStatsView.visibleProperty().get()) {
            numberStatsView.visibleProperty().set(false);
            chartView.visibleProperty().set(true);
        } else {
            chartView.visibleProperty().set(false);
            numberStatsView.visibleProperty().set(true);
        }
    }
}
