package org.g02.flightsalesfx;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import org.g02.flightsalesfx.businessEntities.Booking;
import org.g02.flightsalesfx.businessEntities.Route;
import org.g02.flightsalesfx.helpers.Bundle;
import org.g02.flightsalesfx.helpers.Controller;

import java.time.LocalDate;
import java.util.Optional;

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

        //Calculate the total revenue for this route
        double sumRevenue = App.businessLogicAPI.totalRevenueByRoute(selectedRoute);
        totalRevenueField.setText(String.format("%.2f", sumRevenue) + "€");

        //Calculate revenue for Tickets with SeatOption "Business Class"
        double sumBusinessClass = App.businessLogicAPI.sumOfAClassesRevenue(selectedRoute, "business");
        businessClassRevenue.setText(String.format("%.2f", sumBusinessClass) + "€");

        // Calculate revenue for Tickets with SeatOption "First Class"
        double sumFirstClass = App.businessLogicAPI.sumOfAClassesRevenue(selectedRoute, "first");
        firstClassRevenue.setText(String.format("%.2f", sumFirstClass) + "€");

        /*
        *
        * Here the Chart is created
        *
         */

        //Creating the NumberAxis
        Optional<Booking> firstBooking = App.businessLogicAPI.getFirstBookingOfARoute(selectedRoute);
        if (firstBooking.isEmpty()) {
            chartPane.setCenter(new Label("No bookings for this route"));
            return;
        }
        int minBookingYear = firstBooking.get().getBookingDate().getYear();
        int currentYear = LocalDate.now().getYear();
        final NumberAxis xAxis = new NumberAxis(minBookingYear - 1, currentYear + 1, 1);
        final NumberAxis yAxis = new NumberAxis();
        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        XYChart.Series<Number, Number> routeSeries = new XYChart.Series();
        routeSeries.setName(selectedRoute.toString());

        for (int year = minBookingYear; year <= currentYear; year++ ) {
            double sumRevenueOneYear = App.businessLogicAPI.getRevenueForSpecificRouteInOneYear(selectedRoute, year);
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
