package com.g02.flightsalesfx;

import com.g02.flightsalesfx.businessEntities.Employee;
import com.g02.flightsalesfx.businessLogic.BusinessLogicAPI;
import com.g02.flightsalesfx.businessLogic.BusinessLogicAPIImpl;
import com.g02.flightsalesfx.businessLogic.SalesEmployeeImpl;
import com.g02.flightsalesfx.businessLogic.SalesOfficerImpl;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.bytebuddy.matcher.ElementMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
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

    @Mock
    private BusinessLogicAPI businessLogicAPI;

    @Start
    void start(Stage stage) throws IOException {
        var app = new App();
        app.start(stage);
        businessLogicAPI = Mockito.mock(BusinessLogicAPI.class);
        App.businessLogicAPI = businessLogicAPI;
        this.stage = stage;
    }

    @Test
    void testLoginSuccess(FxRobot fxRobot) {
        assertThat(stage.getTitle()).isEqualTo("Flight Ticket Sales");
        assertThat(fxRobot.lookup("#titleLabel").queryAs(Label.class)).hasText("Login");
        var username = "peter@gmx.de";
        var textField = fxRobot.lookup("#username").queryAs(TextField.class);
        fxRobot.clickOn(textField);
        fxRobot.write(username);
        var password = "peterIstDerBeste";
        fxRobot.lookup("#password").queryAs(TextField.class).setText(password);
        var employee = new SalesOfficerImpl("Peter", "peter@gmx.de", "peterIstDerBeste");
        Mockito.when(businessLogicAPI.login(any(), any())).thenReturn(employee);
        fxRobot.clickOn(fxRobot.lookup("#loginButton").queryButton());
        Mockito.verify(businessLogicAPI).login(any(), any());
    }
}