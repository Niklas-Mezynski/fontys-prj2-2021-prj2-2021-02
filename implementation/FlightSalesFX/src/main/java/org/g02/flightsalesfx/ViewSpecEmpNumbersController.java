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
import org.g02.flightsalesfx.businessEntities.SalesEmployee;
import org.g02.flightsalesfx.businessLogic.SalesEmployeeImpl;
import org.g02.flightsalesfx.helpers.Bundle;
import org.g02.flightsalesfx.helpers.Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
        List<Booking> allBokings = App.businessLogicAPI.getAllBookings(booking -> true);
        List<Booking> allBookingsByEmp = allBokings.stream().filter(booking -> booking.getSalesEmployee().equals(emp)).collect(Collectors.toList());

        //Calculating totalRevenue
        double sum = allBookingsByEmp.stream()
                .mapToDouble(booking -> booking.getFlight().getPrice()) //TODO get booking price instead of flight price
                .sum();
        totalRevenueField.setText(String.format("%.2f", sum) + "€");

        //Calculating No. of Bookings
        long numOfBookings = allBookingsByEmp.size();
        noBookings.setText(String.valueOf(numOfBookings));

        //Calculating avgNumberOfTicketsPerBooking
        OptionalDouble avgTicketAmount = allBookingsByEmp.stream()
                .mapToInt(booking -> booking.getTickets().size())
                .average();
        avgTickets.setText(String.format("%.2f",avgTicketAmount.orElse(0)));


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

        //Iterating through each month of last year
        for (int month = 1; month <= 12; month++) {
            int cMonth = month;

            //Predicate to get the date interval right
            Predicate<Booking> pred = (booking -> booking.getBookingDate().isAfter(startDate.plusMonths(cMonth - 1)) && booking.getBookingDate().isBefore(startDate.plusMonths(cMonth)));
            double revenueThisMonth = sumRevenue(allBookingsByEmp, booking -> true, pred);
            monthSeries.getData().add(new XYChart.Data<>(month, revenueThisMonth));
        }

        XYChart.Series<Number, Number> avgMonthSeries = new XYChart.Series();
        avgMonthSeries.setName("Average Employee");
        List<SalesEmployee> allSalesEmps = allBokings.stream().map(Booking::getSalesEmployee).distinct().collect(Collectors.toList());
        for (int month = 1; month <= 12; month++) {
            int cMonth = month;
            List<Double> revenues = new ArrayList<>();
            for (SalesEmployee se: allSalesEmps) {
                Predicate<Booking> pred1 = (booking -> booking.getSalesEmployee().equals(se));
                Predicate<Booking> pred2 = (booking -> booking.getBookingDate().isAfter(startDate.plusMonths(cMonth - 1)) && booking.getBookingDate().isBefore(startDate.plusMonths(cMonth)));

                double revenueThisMonth = sumRevenue(allBokings, pred1, pred2);

                revenues.add(revenueThisMonth);
            }
            OptionalDouble average = revenues.stream().mapToDouble(Double::valueOf).average();
            avgMonthSeries.getData().add(new XYChart.Data<>(month, average.orElse(0)));
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

    //Helper method to calculate the sum of revenues
    private double sumRevenue(List<Booking> bookings, Predicate<Booking> predicate1, Predicate<Booking> predicate2) {
        return bookings.stream()
                .filter(predicate1)
                .filter(predicate2)
                .mapToDouble(booking -> booking.getFlight().getPrice())
                .sum();
    }
}
