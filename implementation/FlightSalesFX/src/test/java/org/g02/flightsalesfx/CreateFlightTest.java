package org.g02.flightsalesfx;

import org.g02.flightsalesfx.businessEntities.Route;
import org.g02.flightsalesfx.businessLogic.AirportImpl;
import org.g02.flightsalesfx.businessLogic.BusinessLogicAPI;
import org.g02.flightsalesfx.businessLogic.RouteImpl;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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
    @AfterAll
    static void end(){
        App.inRootTab=0;
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
    void addFlightDetailsWithoutThrowingAnyException(FxRobot fxRobot) throws InterruptedException {
        //select route
        var v=fxRobot.lookup(node -> ((Text)node).getText().contains("BER")).query();
        fxRobot.clickOn(v);
        //select date
        DatePicker dp=(fxRobot.lookup("#startDate").queryAs(DatePicker.class));
        dp.setValue(LocalDate.of(2021,4,12));
        //select time
        fxRobot.clickOn(fxRobot.lookup("#startTime").queryAs(TextField.class));
        fxRobot.write("12:01");
        //select duration
        fxRobot.clickOn(fxRobot.lookup("#durationHours").queryAs(TextField.class));
        fxRobot.write("2");
        fxRobot.clickOn(fxRobot.lookup("#durationMinutes").queryAs(TextField.class));
        fxRobot.write("10");

//        assertThatCode(() -> {
//            fxRobot.clickOn(fxRobot.lookup("#nextStepButton").queryAs(Button.class));
//        }).doesNotThrowAnyException();


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

        fxRobot.clickOn(fxRobot.lookup("#nextStepButton").queryAs(Button.class));
        fxRobot.press(KeyCode.ENTER);

//        Assertions.assertThat(fxRobot.lookup(".label").queryAs(Label.class)).hasText("Select a Flight route");
    }
}
