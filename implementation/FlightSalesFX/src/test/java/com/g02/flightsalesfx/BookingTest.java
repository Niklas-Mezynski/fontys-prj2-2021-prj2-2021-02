package com.g02.flightsalesfx;

import com.g02.flightsalesfx.businessEntities.Airport;
import com.g02.flightsalesfx.businessEntities.Flight;
import com.g02.flightsalesfx.businessLogic.*;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.mockito.Mockito;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.Start;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
public class BookingTest {

    private Stage stage;
    @Mock
    private BusinessLogicAPI businessLogicAPI;


    @AfterAll
    static void afterAll(FxRobot test) throws IOException {
//        var b = test.lookup("#buttonExit").query();
//        test.clickOn(b);
//        var v = test.lookup("#planesTab").query();
//        test.clickOn(v);
//        App.inRootTab = 0;
//        App.setRoot("home");
    }

    @Start
    void start(Stage stage) throws IOException {
        businessLogicAPI = Mockito.mock(BusinessLogicAPI.class);
        List<Flight> flights = new ArrayList<>();
        var r=new RouteImpl(new AirportImpl("A","A","A"),new AirportImpl("B","B","B"));
        var p=new PlaneImpl("A","A","A");
        flights.add(new FlightImpl(new SalesOfficerImpl("A","A","A"),1, LocalDateTime.of(2020,12,1,12,0),LocalDateTime.of(2020,12,2,12,0),r,p,100));
        Mockito.when(businessLogicAPI.getAllFlights(any())).thenReturn(flights);
        var app = new App();
        app.start(stage);
        App.businessLogicAPI = businessLogicAPI;
        App.setRoot("salesEmployeeHome");
        this.stage = stage;
    }



    @BeforeEach
    void goToCreateRoute(FxRobot test) {
        test.clickOn(test.lookup("#createBookingButton").queryButton());
    }


    @Test
    void createRoute(FxRobot test) {
//        var searchDep = test.lookup("#searchDep").queryAs(TextField.class);
//        test.clickOn(searchDep);
//        test.write("BER");
//        ListView lv = test.lookup("#listDep").queryAs(ListView.class);
//        assertThat(lv.getItems().toArray().length).isEqualTo(1);
//        test.clickOn(test.lookup("#searchArr").queryAs(TextField.class)).write("R");
//        assertThat(test.lookup("#listArr").queryAs(ListView.class).getItems().toArray().length).isEqualTo(2);
//        Mockito.verify(businessLogicAPI).getAllPlanes(any());
    }
}
