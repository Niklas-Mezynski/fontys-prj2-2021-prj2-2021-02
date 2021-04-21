package com.g02.flightsalesfx;

import com.g02.flightsalesfx.businessEntities.Airport;
import com.g02.flightsalesfx.businessEntities.Plane;
import com.g02.flightsalesfx.businessEntities.Route;
import com.g02.flightsalesfx.businessLogic.*;
import com.g02.flightsalesfx.gui.RouteTable;
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
import org.mockito.stubbing.OngoingStubbing;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.service.query.PointQuery;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.InstanceOfAssertFactories.LOCAL_DATE;
import static org.assertj.core.api.InstanceOfAssertFactories.LOCAL_DATE_TIME;
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
        Mockito.when(businessLogicAPI.createFlightFromUI(new SalesOfficerImpl("ABC","DEF","GHI"), 10, LocalDateTime.of(LocalDate.of(2021,4,12), LocalTime.of(12,01)), LocalDateTime.of(LocalDate.of(2021,4,12), LocalTime.of(14,11)), new RouteImpl(new AirportImpl("DUS", "Düsseldorf", "Germany"), new AirportImpl("BER", "Berlin", "Germany")), new PlaneImpl("A420","413","Airbus"), 10)).thenReturn(true);
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

    @Test
    void check(FxRobot fxRobot) {
        var v7=fxRobot.lookup(node -> ((Text)node).getText().contains("A420")).query();
        fxRobot.clickOn(v7);
        fxRobot.clickOn(fxRobot.lookup("#flightPrice").queryAs(TextField.class)).write("10");
        fxRobot.clickOn(fxRobot.lookup("#flightNumberTextField").queryAs(TextField.class)).write("10");
        fxRobot.clickOn(fxRobot.lookup("#saveFlightButton").queryButton());
    }
    @AfterAll
    static void end(){
        App.inRootTab=0;
    }
}