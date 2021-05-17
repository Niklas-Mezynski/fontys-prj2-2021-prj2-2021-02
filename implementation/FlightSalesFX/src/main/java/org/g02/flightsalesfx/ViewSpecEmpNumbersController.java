package org.g02.flightsalesfx;

import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.temporal.ChronoUnit;
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


        /*
            Here the diagram is calculated
         */
        LocalDateTime startDate = LocalDateTime.now().minusMonths(12);
        final NumberAxis xAxis = new NumberAxis(1, 12, 1);
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Months beginning from " + startDate.toLocalDate());
        yAxis.setLabel("Revenue in €");
        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        XYChart.Series<Number, Number> monthSeries = new XYChart.Series();
        monthSeries.setName(emp.getName());

        for (int month = 1; month <= 12; month++) {
            int cMonth = month;

            double revenueThisMonth = allBookings.stream()
                    .filter(booking -> booking.getBookingDate().isAfter(startDate.plusMonths(cMonth - 1)) &&  booking.getBookingDate().isBefore(startDate.plusMonths(cMonth)))
                    .mapToDouble(booking -> booking.getFlight().getPrice())
                    .sum();
            monthSeries.getData().add(new XYChart.Data<>(month, revenueThisMonth));
        }

        chart.getData().add(monthSeries);
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
