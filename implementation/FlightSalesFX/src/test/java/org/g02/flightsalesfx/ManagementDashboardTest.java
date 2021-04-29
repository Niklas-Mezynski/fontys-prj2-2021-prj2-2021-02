package org.g02.flightsalesfx;

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

        //SampleData Booking
        List<Booking> bookings = new ArrayList<>();
        var se = new SalesEmployeeImpl("", "", "");
        var so = new SalesOfficerImpl("", "", "");
        var flight = new FlightImpl(so, 123, LocalDateTime.now(), LocalDateTime.now().plusMinutes(10), route, null, 50);
        Ticket[] tickets = new Ticket[0];
        FlightOption[] flightOptions = new FlightOption[0];
        bookings.add(new BookingImpl(se, flight, tickets , flightOptions, "no mail"));

        Mockito.when(businessLogicAPI.getAllBookings(any())).thenReturn(bookings);

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

//    @BeforeEach
//    void goToTab(FxRobot fxRobot) {
//        var x = fxRobot.lookup("#flightsTab").query();
//        fxRobot.clickOn(x);
//        fxRobot.clickOn(fxRobot.lookup("#goToCreateFlight").queryAs(Button.class));
//    }

    @Test
    void tManagementDashButtonTest(FxRobot fxRobot) throws InterruptedException {
        assertThatCode( () -> {
            fxRobot.clickOn(fxRobot.lookup("#viewRevenueRoute").queryAs(Button.class));
        }).doesNotThrowAnyException();
    }

    @Test
    void tRouteSelection(FxRobot fxRobot) throws InterruptedException {
        fxRobot.clickOn(fxRobot.lookup("#viewRevenueRoute").queryAs(Button.class));

        //Click on the route in the ListView
        var routeItem = fxRobot.lookup(node -> ((Text) node).getText().contains("DUS")).query();
        fxRobot.clickOn(routeItem);

        //Click on "viewKPIs" Button
        fxRobot.clickOn(fxRobot.lookup(node -> ((Button) node).getText().contains("View KPI")).queryAs(Button.class));

        var totalRevenueField= fxRobot.lookup("#totalRevenueField").queryAs(TextField.class);
        var totalRevenueString = totalRevenueField.getText();

//        double totalRevenue = Double.parseDouble(totalRevenueString.split("€")[0]);
        double totalRevenue = Double.parseDouble(totalRevenueString.split("€")[0].split(",")[0]);

        assertThat(totalRevenue).isEqualTo(50);

    }
}
