package com.g02.flightsalesfx;

import com.g02.flightsalesfx.businessLogic.BusinessLogicAPI;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
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

import java.io.IOException;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

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
        var app = new App();
        app.start(stage);
        businessLogicAPI = Mockito.mock(BusinessLogicAPI.class);
        App.businessLogicAPI = businessLogicAPI;
        App.setRoot("home");
        this.stage = stage;
    }

    @BeforeEach
    void goToTab(FxRobot fxRobot) {

        var x = fxRobot.lookup("#flightsTab").query();
        fxRobot.clickOn(x);
    }

    @Test
    void createFlightOpens(FxRobot fxRobot) {
        assertThat(stage.getTitle()).isEqualTo("Flight Ticket Sales");
        Assertions.assertThat(fxRobot.lookup(".label").queryAs(Label.class)).doesNotHaveText("Create new flight");
        Assertions.assertThat(fxRobot.lookup("#goToCreateFlight").queryAs(Button.class)).hasText("Create new flight");
        fxRobot.clickOn(fxRobot.lookup("#goToCreateFlight").queryAs(Button.class));
    }

    @Test
    void addFlightDetails(FxRobot fxRobot) {
        //search for route and select it
        fxRobot.clickOn(fxRobot.lookup("#routeSearchBar").queryAs(TextField.class));
        fxRobot.write("Bremen cuxhaven");
        fxRobot.clickOn(fxRobot.lookup("#routeTablePane").queryAs(AnchorPane.class).getChildren().get(0));

        
    }
}
