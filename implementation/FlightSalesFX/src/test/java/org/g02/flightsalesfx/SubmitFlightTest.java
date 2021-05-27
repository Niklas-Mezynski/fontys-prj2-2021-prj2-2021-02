package org.g02.flightsalesfx;

import org.g02.flightsalesfx.businessEntities.Plane;
import org.g02.flightsalesfx.businessEntities.Route;
import org.g02.flightsalesfx.businessLogic.*;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(ApplicationExtension.class)
public class SubmitFlightTest {

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
        List<Route> routes = new ArrayList<>();
        routes.add(new RouteImpl(new AirportImpl("DUS", "Düsseldorf", "Germany"), new AirportImpl("BER", "Berlin", "Germany")));
        List<Plane> planes=new ArrayList<>();
        planes.add(new PlaneImpl("A420","413","Airbus"));
        Mockito.when(businessLogicAPI.getAllRoutes(any())).thenReturn(routes);
        Mockito.when(businessLogicAPI.getAllPlanes(any())).thenReturn(planes);
        Mockito.when(businessLogicAPI.createFlightFromUI(new SalesOfficerImpl("ABC","DEF","GHI"), LocalDateTime.of(LocalDate.of(2021,4,12), LocalTime.of(12,01)), LocalDateTime.of(LocalDate.of(2021,4,12), LocalTime.of(14,11)), new RouteImpl(new AirportImpl("DUS", "Düsseldorf", "Germany"), new AirportImpl("BER", "Berlin", "Germany")), new PlaneImpl("A420","413","Airbus"), 10,List.of())).thenReturn(true);
        App.employee=new SalesOfficerImpl("ABC","DEF","GHI");

        var app = new App();
        app.start(stage);
        App.businessLogicAPI = businessLogicAPI;
        App.setRoot("home");
        this.stage = stage;
    }

    @BeforeEach
    void goToTab(FxRobot fxRobot) {
        var x = fxRobot.lookup("#flightsTab").query();
        fxRobot.clickOn(x);
        fxRobot.clickOn(fxRobot.lookup("#goToCreateFlight").queryAs(Button.class));
        var v7=fxRobot.lookup(node -> ((Text)node).getText().contains("BER")).query();
        fxRobot.clickOn(v7);
        DatePicker dp=(fxRobot.lookup("#startDate").queryAs(DatePicker.class));
        dp.setValue(LocalDate.of(2021,4,12));
        fxRobot.clickOn(fxRobot.lookup("#startTime").queryAs(TextField.class)).write("12:01");
        fxRobot.clickOn(fxRobot.lookup("#durationHours").queryAs(TextField.class)).write("2");
        fxRobot.clickOn(fxRobot.lookup("#durationMinutes").queryAs(TextField.class)).write("10");
        fxRobot.clickOn(fxRobot.lookup("#nextStepButton").queryAs(Button.class));
    }

    @Disabled
    @Test
    void check(FxRobot fxRobot) {
        var v7=fxRobot.lookup(node -> ((Text)node).getText().contains("A420")).query();
        fxRobot.clickOn(v7);
        fxRobot.clickOn(fxRobot.lookup("#flightPrice").queryAs(TextField.class)).write("10");
        fxRobot.clickOn(fxRobot.lookup("#saveFlightButton").queryButton());
    }
    @AfterAll
    static void end(){
        App.inRootTab=0;
    }
}