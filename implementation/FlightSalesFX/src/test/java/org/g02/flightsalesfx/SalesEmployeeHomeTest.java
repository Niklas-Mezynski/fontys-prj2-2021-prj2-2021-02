package org.g02.flightsalesfx;

import javafx.scene.Node;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.g02.flightsalesfx.businessEntities.*;
import org.g02.flightsalesfx.businessLogic.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(ApplicationExtension.class)
public class SalesEmployeeHomeTest {

    private Stage stage;

    @Mock
    private BusinessLogicAPI businessLogicAPI;

    private SalesOfficerImpl salesOfficer = new SalesOfficerImpl("Peter", "peter@gmx.de", "irgendwas");

    private Airport airport1 = new AirportImpl("DUS", "Düsseldorf", "Germany");
    private Airport airport2 = new AirportImpl("BER", "Berlin", "Germany");
    private Airport airport3 = new AirportImpl("LAX", "Los Angeles", "USA");

    private Route route1 = new RouteImpl(airport1, airport2);
    private Route route2 = new RouteImpl(airport2, airport3);
    private Route route3 = new RouteImpl(airport1, airport3);

    private Plane plane1 = new PlaneImpl("D-ABCD", "A", "A");
    private Plane plane2 = new PlaneImpl("D-BCDE", "B", "B");

    private LocalDateTime localDateTime1 = LocalDateTime.of(2021, 4, 28, 13, 0, 0);
    private LocalDateTime localDateTime2 = LocalDateTime.of(2021, 4, 29, 13, 0, 0);
    private LocalDateTime localDateTime3 = LocalDateTime.of(2021, 4, 30, 13, 0, 0);

    private Flight flight1 = new FlightImpl(salesOfficer, localDateTime1, localDateTime2, route1, plane1, 20);
    private Flight flight2 = new FlightImpl(salesOfficer, localDateTime1, localDateTime2, route2, plane1, 20);
    private Flight flight3 = new FlightImpl(salesOfficer, localDateTime1, localDateTime2, route3, plane2, 20);

    private List<Flight> flights = List.of(flight1, flight2, flight3);

    @Start
    void start(Stage stage) throws IOException {
        var app = new App();
        app.start(stage);
        businessLogicAPI = Mockito.mock(BusinessLogicAPI.class);
        App.businessLogicAPI = businessLogicAPI;
        Mockito.when(businessLogicAPI.getAllFlights(any())).thenReturn(flights);
        App.setRoot("salesEmployeeHome");
        this.stage = stage;
    }

    @Disabled
    @Test
    void t01DepartureFilter(FxRobot fxRobot) {
        var textField = fxRobot.lookup("#departureField").queryAs(TextField.class);
        fxRobot.clickOn(textField);
        fxRobot.write("Düssel");
        fxRobot.rootNode(fxRobot.lookup((Node node) -> node instanceof TableView).query());
        fxRobot.press(KeyCode.ENTER);
        var lookup = fxRobot.lookup((Node node) -> node instanceof TableRow).queryAs(TableRow.class);
    }
}
