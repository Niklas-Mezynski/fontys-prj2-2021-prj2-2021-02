package com.g02.flightsalesfx;

import com.g02.flightsalesfx.businessEntities.Airport;
import com.g02.flightsalesfx.businessEntities.Route;
import com.g02.flightsalesfx.businessLogic.AirportImpl;
import com.g02.flightsalesfx.businessLogic.BusinessLogicAPI;
import com.g02.flightsalesfx.businessLogic.RouteImpl;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
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
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.InstanceOfAssertFactories.LOCAL_DATE;
import static org.assertj.core.api.InstanceOfAssertFactories.LOCAL_DATE_TIME;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(ApplicationExtension.class)
public class CreateFlightTest {

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
        routes.add( new RouteImpl(new AirportImpl("DUS", "Düsseldorf", "Germany"), new AirportImpl("BER", "Berlin", "Germany")));
        Mockito.when(businessLogicAPI.getAllRoutes(any())).thenReturn(routes);

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
    }

    /*@Test
    void createFlightOpens(FxRobot fxRobot) {
        assertThat(stage.getTitle()).isEqualTo("Flight Ticket Sales");
        Assertions.assertThat(fxRobot.lookup(".label").queryAs(Label.class)).doesNotHaveText("Create new flight");
        Assertions.assertThat(fxRobot.lookup("#goToCreateFlight").queryAs(Button.class)).hasText("Create new flight");
        //fxRobot.clickOn(fxRobot.lookup("#goToCreateFlight").queryAs(Button.class));
    }*/

    @Test
    void addFlightDetailsWithoutThrowingAnyException(FxRobot fxRobot) {
        //select route
        fxRobot.clickOn(fxRobot.lookup("#routeScrollPane").queryAs(ScrollPane.class));
        fxRobot.press(KeyCode.ENTER);
        //select date
        fxRobot.clickOn(fxRobot.lookup("#startDate").queryAs(DatePicker.class));
        fxRobot.write(LocalDate.now().toString());
        //select time
        fxRobot.clickOn(fxRobot.lookup("#startTime").queryAs(TextField.class));
        fxRobot.write("12:01");
        //select duration
        fxRobot.clickOn(fxRobot.lookup("#durationHours").queryAs(TextField.class));
        fxRobot.write("2");
        fxRobot.clickOn(fxRobot.lookup("#durationMinutes").queryAs(TextField.class));
        fxRobot.write("10");

        assertThatCode(() -> {
            fxRobot.clickOn(fxRobot.lookup("#nextStepButton").queryAs(Button.class));
        })
                .doesNotThrowAnyException();
    }

    @Test
    void exceptionWhenMissingFlightDetail(FxRobot fxRobot) {
        //route left out

        //select date
        DatePicker dp=(fxRobot.lookup("#startDate").queryAs(DatePicker.class));
        dp.setValue(LocalDate.of(2021,4,12));
//        fxRobot.write("29.04.2021");
        //select time
        fxRobot.clickOn(fxRobot.lookup("#startTime").queryAs(TextField.class));
        fxRobot.write("12:01");
        //select duration
        fxRobot.clickOn(fxRobot.lookup("#durationHours").queryAs(TextField.class));
        fxRobot.write("2");
        fxRobot.clickOn(fxRobot.lookup("#durationMinutes").queryAs(TextField.class));
        fxRobot.write("10");

        assertThatCode(() -> {
            fxRobot.clickOn(fxRobot.lookup("#nextStepButton").queryAs(Button.class));
        })
                .isInstanceOf(IOException.class);
    }
}
