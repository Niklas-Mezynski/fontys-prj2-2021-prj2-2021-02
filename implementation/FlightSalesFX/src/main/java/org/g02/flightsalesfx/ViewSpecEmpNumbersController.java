package org.g02.flightsalesfx;

import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import org.g02.flightsalesfx.businessEntities.Booking;
import org.g02.flightsalesfx.businessLogic.SalesEmployeeImpl;
import org.g02.flightsalesfx.helpers.Bundle;
import org.g02.flightsalesfx.helpers.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

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

    @FXML
    private AnchorPane diagramView;

    @FXML
    private BorderPane diagramPane;

    @FXML
    private Button switchButton;

    SalesEmployeeImpl emp;

    @Override
    public void init(Bundle bundle) {
        emp = (SalesEmployeeImpl) bundle.get("emp");
        titleLabel.setText("Stats for employee '" + emp.getName() + "'");

        /*
            Here the number statistics are calculated
         */

        //Calculating totalRevenue
        double sum = App.businessLogicAPI.totalRevenueByEmp(emp);
        totalRevenueField.setText(String.format("%.2f", sum) + "€");

        //Calculating No. of Bookings
        long numOfBookings = App.businessLogicAPI.totalNumOfBookingsByAnEmployee(emp);
        noBookings.setText(String.valueOf(numOfBookings));

        //Calculating avgNumberOfTicketsPerBooking
        double avgTicketAmount = App.businessLogicAPI.avgNumOfTicketsPerBooking(emp);
        avgTickets.setText(String.format("%.2f", avgTicketAmount));


        /*
            Here the diagram is calculated
         */

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        LocalDateTime startDate = LocalDateTime.now().minusMonths(12);
        xAxis.setLabel("Month start date");
        yAxis.setLabel("Revenue in €");
        LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);

        //1. Revenue for spec Employee
        XYChart.Series<String, Number> monthSeries = new XYChart.Series();
        monthSeries.setName(emp.getName());

        Map<LocalDateTime, Double> monthlyRevenue = App.businessLogicAPI.getMonthlyRevenue(emp, LocalDateTime.now().minusMonths(12));
        for (Map.Entry<LocalDateTime, Double> entry : monthlyRevenue.entrySet()) {
            String formattedDate = entry.getKey().format(DateTimeFormatter.ofPattern("dd.MM.yy"));
            monthSeries.getData().add(new XYChart.Data<>(formattedDate, entry.getValue()));
        }


        //2. Revenue for average Employee
        XYChart.Series<String, Number> avgMonthSeries = new XYChart.Series();
        avgMonthSeries.setName("Average Employee");

        Map<LocalDateTime, Double> avgMonthlyRevenues = App.businessLogicAPI.getAvgMonthlyRevenues(LocalDateTime.now().minusMonths(12));
        for (Map.Entry<LocalDateTime, Double> entry : avgMonthlyRevenues.entrySet()) {
            String formattedDate = entry.getKey().format(DateTimeFormatter.ofPattern("dd.MM.yy"));
            avgMonthSeries.getData().add(new XYChart.Data<>(formattedDate, entry.getValue()));
        }

        chart.getData().add(monthSeries);
        chart.getData().add(avgMonthSeries);
        chart.setMinWidth(diagramView.getPrefWidth());
        chart.setMinHeight(diagramView.getPrefHeight());
        diagramPane.setCenter(chart);
    }

    @FXML
    void switchView() {
        if (!diagramView.isVisible()) {
            diagramView.setVisible(true);
            numberStatsView.setVisible(false);
            switchButton.setText("Show number statistics");

        } else {
            diagramView.setVisible(false);
            numberStatsView.setVisible(true);
            switchButton.setText("See revenue in last 12 months");
        }
    }

    @FXML
    void backToMenu() {
        App.setRoot("managementDashboard");
    }
}
