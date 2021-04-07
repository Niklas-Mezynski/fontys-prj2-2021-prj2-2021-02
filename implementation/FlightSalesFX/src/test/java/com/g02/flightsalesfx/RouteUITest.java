package com.g02.flightsalesfx;

import com.g02.flightsalesfx.businessEntities.Airport;
import com.g02.flightsalesfx.businessLogic.AirportImpl;
import com.g02.flightsalesfx.businessLogic.BusinessLogicAPI;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.testfx.assertions.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
public class RouteUITest {

    private Stage stage;
    @Mock
    private BusinessLogicAPI businessLogicAPI;

    @AfterAll
    static void afterAll(FxRobot test) throws IOException {
        var b = test.lookup("#buttonExit").query();
        test.clickOn(b);
        var v = test.lookup("#planesTab").query();
        test.clickOn(v);
        App.inRootTab = 0;
        App.setRoot("home");
    }

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
        App.businessLogicAPI = businessLogicAPI;
        App.setRoot("home");
        this.stage = stage;
    }

    @BeforeEach
    void goToCreateRoute(FxRobot test) {
        var v = test.lookup("#routesTab").query();
        test.clickOn(v);
        test.clickOn(test.lookup("#goToCreateRoute").queryAs(Button.class));
    }

    @Test
    void createRoute(FxRobot test) {
        var searchDep = test.lookup("#searchDep").queryAs(TextField.class);
        test.clickOn(searchDep);
        test.write("BER");
        ListView lv = test.lookup("#listDep").queryAs(ListView.class);
        assertThat(lv.getItems().toArray().length).isEqualTo(1);
        test.clickOn(test.lookup("#searchArr").queryAs(TextField.class)).write("R");
        assertThat(test.lookup("#listArr").queryAs(ListView.class).getItems().toArray().length).isEqualTo(2);
        Mockito.verify(businessLogicAPI).getAllPlanes(any());
    }
}
