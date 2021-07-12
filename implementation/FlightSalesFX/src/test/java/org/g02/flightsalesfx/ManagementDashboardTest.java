package org.g02.flightsalesfx;

import org.assertj.core.api.SoftAssertions;
import org.g02.flightsalesfx.businessEntities.*;
import org.g02.flightsalesfx.businessLogic.*;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.InstanceOfAssertFactories.LOCAL_DATE;
import static org.assertj.core.api.InstanceOfAssertFactories.LOCAL_DATE_TIME;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(ApplicationExtension.class)
public class ManagementDashboardTest {

    static {
        if (Boolean.getBoolean("headless")) {
            System.setProperty("testfx.robot", "glass");
            System.setProperty("testfx.headless", "true");
            System.setProperty("prism.order", "sw");
            System.setProperty("prism.text", "t2k");
        }
    }

    private Stage stage;
    @Mock
    private BusinessLogicAPI businessLogicAPI;

    @Start
    void start(Stage stage) throws IOException {
        businessLogicAPI = Mockito.mock(BusinessLogicAPI.class);

        //sampleData Route
        List<Route> routes = new ArrayList<>();
        var route = new RouteImpl(new AirportImpl("DUS", "Düsseldorf", "Germany"), new AirportImpl("BER", "Berlin", "Germany"));
        routes.add( route);
        Mockito.when(businessLogicAPI.getAllRoutes(any())).thenReturn(routes);

        var se = new SalesEmployeeImpl("Snens", "email@gmail.com", "");
        var so = new SalesOfficerImpl("", "", "");
//        //SampleData Booking
//        List<Booking> bookings = new ArrayList<>();
//        var flight = new FlightImpl(so, 123, LocalDateTime.now(), LocalDateTime.now().plusMinutes(10), route, null, 50);
//        Ticket[] tickets = new Ticket[]{new TicketImpl(flight, new SeatImpl(0,0), "Peter", "Gockel", new SeatOption[0])};
//        FlightOption[] flightOptions = new FlightOption[0];
//        bookings.add(new BookingImpl(se, flight, tickets , flightOptions, "no mail", LocalDateTime.of(2021, 4, 10, 20, 0), 50.00));

        Mockito.when(businessLogicAPI.getAllEmployees(any())).thenReturn(List.of(se, so));
        Mockito.when(businessLogicAPI.totalRevenueByRoute(route)).thenReturn(60.0);
        Mockito.when(businessLogicAPI.sumOfAClassesRevenue(route, "business")).thenReturn(40.0);
        Mockito.when(businessLogicAPI.sumOfAClassesRevenue(route, "first")).thenReturn(80.0);
        Mockito.when(businessLogicAPI.totalRevenueByEmp(se)).thenReturn(50.0);
        Mockito.when(businessLogicAPI.totalNumOfBookingsByAnEmployee(se)).thenReturn(1);
        Mockito.when(businessLogicAPI.avgNumOfTicketsPerBooking(se)).thenReturn(1.0);

        var app = new App();
        app.start(stage);
        App.businessLogicAPI = businessLogicAPI;
        App.setRoot("managementDashboard");
        this.stage = stage;
    }
    @AfterAll
    static void end(){
        App.inRootTab=0;
    }

    @Test
    void t01ManagementDashButtonTest(FxRobot fxRobot) throws InterruptedException {
        assertThatCode( () -> {
            fxRobot.clickOn(fxRobot.lookup("#viewRevenueRoute").queryAs(Button.class));
        }).doesNotThrowAnyException();
    }

    @Test
    void t02RouteSelection(FxRobot fxRobot) throws InterruptedException {
        fxRobot.clickOn(fxRobot.lookup("#viewRevenueRoute").queryAs(Button.class));

        //Click on the route in the ListView
        var routeItem = fxRobot.lookup(node -> ((Text) node).getText().contains("DUS")).query();
        fxRobot.clickOn(routeItem);

        //Click on "viewKPIs" Button
        fxRobot.clickOn(fxRobot.lookup(node -> ((Button) node).getText().contains("View KPI")).queryAs(Button.class));

        var totalRevenueField= fxRobot.lookup("#totalRevenueField").queryAs(TextField.class);
        var businessClassRevenueField= fxRobot.lookup("#businessClassRevenue").queryAs(TextField.class);
        var firstClassRevenueField= fxRobot.lookup("#firstClassRevenue").queryAs(TextField.class);

        SoftAssertions.assertSoftly((s) -> {
            s.assertThat(TestUtil.getDoubleConsideringLocale(totalRevenueField.getText().split("€")[0])).isEqualTo(60);
            s.assertThat(TestUtil.getDoubleConsideringLocale(businessClassRevenueField.getText().split("€")[0])).isEqualTo(40);
            s.assertThat(TestUtil.getDoubleConsideringLocale(firstClassRevenueField.getText().split("€")[0])).isEqualTo(80);
        });
        assertThat(TestUtil.getDoubleConsideringLocale(totalRevenueField.getText().split("€")[0])).isEqualTo(60);
    }

    @Test
    void t03EmployeeKPIsTest(FxRobot fxRobot) {
        fxRobot.clickOn(fxRobot.lookup(node -> ((Button) node).getText().contains("Employee")).queryAs(Button.class));

        //Click on the Emp in the ListView and Than "View Stats"
        var empItem = fxRobot.lookup(node -> ((Text) node).getText().contains("Snens")).query();
        fxRobot.clickOn(empItem);
        fxRobot.clickOn(fxRobot.lookup(node -> ((Button) node).getText().contains("View")).queryAs(Button.class));

        var totalRevField = fxRobot.lookup("#totalRevenueField").queryAs(TextField.class);
        var noBookingsField = fxRobot.lookup("#noBookings").queryAs(TextField.class);
        var avgTicketsField = fxRobot.lookup("#avgTickets").queryAs(TextField.class);

        SoftAssertions.assertSoftly( s -> {
            s.assertThat(TestUtil.getDoubleConsideringLocale(totalRevField.getText().split("€")[0])).isEqualTo(50);
            s.assertThat(TestUtil.getIntConsideringLocale(noBookingsField.getText())).isEqualTo(1);
            s.assertThat(TestUtil.getDoubleConsideringLocale(avgTicketsField.getText())).isEqualTo(1);
        });
    }


}
