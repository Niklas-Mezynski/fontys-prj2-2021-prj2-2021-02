package com.g02.flightsalesfx;

import com.g02.flightsalesfx.businessEntities.Flight;
import com.g02.flightsalesfx.businessEntities.Route;
import com.g02.flightsalesfx.businessLogic.AirportImpl;
import com.g02.flightsalesfx.businessLogic.BusinessLogicAPI;
import com.g02.flightsalesfx.businessLogic.FlightImpl;
import com.g02.flightsalesfx.businessLogic.RouteImpl;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.Start;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

public class HomeControllerTest {

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
        //todo: fill in sample-flight
        flights.add();
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

    //todo: testing
}
