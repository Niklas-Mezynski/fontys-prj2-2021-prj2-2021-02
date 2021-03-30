package com.g02.flightsalesfx;

import com.sun.tools.javac.Main;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.IOException;

import static org.testfx.assertions.api.Assertions.assertThat;


@ExtendWith(ApplicationExtension.class)
public class LoginTest {

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
        fxRobot.lookup("#loginButton").queryButton().fire();
        assertThat(App.employee).isNotNull();
        assertThat(App.employee.getEmail()).isEqualTo(username);
        assertThat(App.employee.getPassword()).isEqualTo(password);
    }
}
