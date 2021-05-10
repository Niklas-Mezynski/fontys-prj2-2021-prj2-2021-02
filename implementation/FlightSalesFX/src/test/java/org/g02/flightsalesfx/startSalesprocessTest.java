package org.g02.flightsalesfx;

import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.assertj.core.api.ThrowableAssert;
import org.g02.flightsalesfx.businessEntities.Flight;
import org.g02.flightsalesfx.businessEntities.Route;
import org.g02.flightsalesfx.businessLogic.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(ApplicationExtension.class)
public class startSalesprocessTest {
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

        //sampleData
        List<Flight> flights = new ArrayList<>();
        SalesOfficerImpl so = new SalesOfficerImpl("Officer", "officermail", "123");
        flights.add(new FlightImpl(so, 4, LocalDateTime.MIN, LocalDateTime.now(), new RouteImpl(new AirportImpl("DUS", "Düsseldorf", "Germany"), new AirportImpl("BER", "Berlin", "Germany")), new PlaneImpl(2, "flieger", "Lufthansa", "A380"), 20));
        Mockito.when(businessLogicAPI.getAllFlights(any())).thenReturn(flights);

        var app = new App();
        app.start(stage);
        App.businessLogicAPI = businessLogicAPI;
        App.setRoot("home");
        this.stage = stage;
    }
    @AfterAll
    static void end(){
        App.inRootTab=0;
    }

    @BeforeEach
    void goToTab(FxRobot fxRobot) {
        var x = fxRobot.lookup("#flightsTab").query();
        fxRobot.clickOn(x);
    }

    @Test
    void enableButtonTest(FxRobot fxRobot) {
        var v=fxRobot.lookup(node -> ((Text)node).getText().contains("4")).query();
        fxRobot.clickOn(v);

        fxRobot.clickOn(fxRobot.lookup("#enableSalesprocess").queryAs(Button.class));
    }
}
