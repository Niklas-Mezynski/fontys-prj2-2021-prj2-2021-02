package com.g02.flightsalesfx;

import com.g02.flightsalesfx.businessEntities.Airport;
import com.g02.flightsalesfx.businessEntities.Employee;
import com.g02.flightsalesfx.businessLogic.AirportImpl;
import com.g02.flightsalesfx.businessLogic.BusinessLogicAPI;
import com.g02.flightsalesfx.businessLogic.SalesEmployeeImpl;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.testfx.assertions.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
public class RouteUITest {

    private Stage stage;
    @Mock
    private BusinessLogicAPI businessLogicAPI;

    @Start
    void start(Stage stage) throws IOException {
        businessLogicAPI = Mockito.mock(BusinessLogicAPI.class);
        List<Airport> airports = new ArrayList<>();
        airports.add(new AirportImpl("DUS", "Düsseldorf", "Germany"));
        airports.add(new AirportImpl("BER", "Berlin", "Germany"));
        airports.add(new AirportImpl("FRA", "Frankfurt", "Germany"));
        Mockito.when(businessLogicAPI.getAllAirports(any())).thenReturn(airports);
        var app = new App();
        app.start(stage);
        businessLogicAPI = Mockito.mock(BusinessLogicAPI.class);
        App.businessLogicAPI = businessLogicAPI;
        App.setRoot("home");
        this.stage = stage;
    }
    @BeforeEach
    void goToCreateRoute(FxRobot test){
        var v=test.lookup(".tab-pane > .tab-header-area > .headers-region > .tab").nth(1).query();
        test.clickOn(v);
        test.clickOn(test.lookup("#goToCreateRoute").queryAs(Button.class));
    }
    @Test
    void createRoute(FxRobot test){
        var searchDep = test.lookup("#searchDep").queryAs(TextField.class);
        test.clickOn(searchDep);
        test.write("BER");
        ListView lv=test.lookup("#listDep").queryAs(ListView.class);
        assertThat(lv.getItems().toArray().length==1).isTrue();
        test.clickOn(test.lookup("#searchArr").queryAs(TextField.class)).write("R");
        assertThat(test.lookup("#listArr").queryAs(ListView.class).getItems().toArray().length==2).isTrue();
    }

}
