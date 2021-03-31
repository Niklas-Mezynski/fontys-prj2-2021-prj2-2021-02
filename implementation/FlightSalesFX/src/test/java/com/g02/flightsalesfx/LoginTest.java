package com.g02.flightsalesfx;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.IOException;

import static org.testfx.assertions.api.Assertions.*;


@ExtendWith(ApplicationExtension.class)
public class LoginTest {

    static {
        if (Boolean.getBoolean("headless")) {
            System.setProperty("testfx.robot", "glass");
            System.setProperty("testfx.headless", "true");
            System.setProperty("prism.order", "sw");
            System.setProperty("prism.text", "t2k");
        }
    }
    
    private Stage stage;

    @Start
    void start(Stage stage) throws IOException {
        var app = new App();
        app.start(stage);
        this.stage = stage;
    }

    @Test
    void testLoginSuccess(FxRobot fxRobot) {
        assertThat(stage.getTitle()).isEqualTo("Flight Ticket Sales");
        assertThat(fxRobot.lookup("#titleLabel").queryAs(Label.class)).hasText("Login");
        var username = "peter@gmx.de";
        fxRobot.lookup("#username").queryAs(TextField.class).setText(username);
        var password = "peterIstDerBeste";
        fxRobot.lookup("#password").queryAs(TextField.class).setText(password);
        fxRobot.clickOn(fxRobot.lookup("#loginButton").queryButton());
        assertThat(App.employee).isNotNull();
        assertThat(App.employee.getEmail()).isEqualTo(username);
        assertThat(App.employee.getPassword()).isEqualTo(password);
    }

    @Test
    void testLoginNoSuccess(FxRobot test) {
        assertThat(stage.getTitle()).isEqualTo("Flight Ticket Sales");
        assertThat(test.lookup("#titleLabel").queryAs(Label.class)).hasText("Login");
        var username = "urselmann@gmx.de";
        test.lookup("#username").queryAs(TextField.class).setText(username);
        var password = "urselIstDieBeste";
        test.lookup("#password").queryAs(TextField.class).setText(password);
        test.lookup("#loginButton").queryButton().fire();
        assertThat(App.employee).isNull();
    }
}
