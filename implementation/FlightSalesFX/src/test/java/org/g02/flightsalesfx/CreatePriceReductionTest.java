package org.g02.flightsalesfx;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.g02.flightsalesfx.businessEntities.Airport;
import org.g02.flightsalesfx.businessEntities.Flight;
import org.g02.flightsalesfx.businessEntities.PriceReduction;
import org.g02.flightsalesfx.businessLogic.*;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(ApplicationExtension.class)
public class CreatePriceReductionTest {
    private Stage stage;
    @Mock
    private BusinessLogicAPI businessLogicAPI;

    @AfterAll
    static void afterAll(FxRobot test) throws IOException {
        var b = test.lookup("#exitButton").query();
        test.clickOn(b);
        var v = test.lookup("#planesTab").query();
        test.clickOn(v);
        App.inRootTab = 0;
        App.setRoot("home");
    }

    @BeforeEach
    void goToCreateRoute(FxRobot test) {
        var v = test.lookup("#priceRedTab").query();
        test.clickOn(v);
        test.clickOn(test.lookup("#goToPriceReductions").queryAs(Button.class));
    }

    @Start
    void start(Stage stage) throws IOException {
        businessLogicAPI = Mockito.mock(BusinessLogicAPI.class);
        List<Flight> flights=new ArrayList<>();
        var a1=new AirportImpl("DUS", "Düsseldorf", "Germany");
        var a2=new AirportImpl("BER","Berlin","Germany");
        var r1=new RouteImpl(a1,a2);
        var r2=new RouteImpl(a2,a1);
        var t1=LocalDateTime.of(2020,12,12,12,0);
        var t2=t1.plusDays(2);
        var s1=new SalesOfficerImpl("Tester","e1","p1");
        var p1=new PlaneImpl("A1","A2","A3");
        var flight1=new FlightImpl(s1,1,t1,t2,r1,p1,100);
        var flight2=new FlightImpl(s1,2,t1.plusDays(3),t2.plusDays(3),r2,p1,100);
        flights.add(flight1);
        flights.add(flight2);
        Mockito.when(businessLogicAPI.getAllFlights()).thenReturn(flights);
        Mockito.when(businessLogicAPI.getAllFlights(any())).thenReturn(flights);
        List<PriceReduction> priceReductions=new ArrayList<>();
        var pr1=new StaticPriceReductionImpl("Summer Sale",t2.plusYears(10),t1.minusYears(10),true,10);
        var pr2=new StaticPriceReductionImpl("Spring Sale",t2.plusYears(10),t1.minusYears(10),false,100);
        priceReductions.add(pr1);
        priceReductions.add(pr2);
        Mockito.when(businessLogicAPI.getAllPriceReductions(any())).thenReturn(priceReductions);
        Mockito.when(businessLogicAPI.getAllPriceReductions()).thenReturn(priceReductions);
        Mockito.when(businessLogicAPI.createPriceReductionFromUI(any())).thenReturn(true);
        var app = new App();
        app.start(stage);
        App.businessLogicAPI = businessLogicAPI;
        App.setRoot("home");
        this.stage = stage;
    }
    @Test
    public void createPriceReduction(FxRobot test){
        test.clickOn(test.lookup("#redName").queryAs(TextField.class));
        test.write("Winter Sale");
        Node v=test.lookup("#startDate").query();
        test.clickOn(v);
        test.write("10.10.20");
        v=test.lookup("#endDate").query();
        test.clickOn(v);
        test.write("10.10.25");
        v=test.lookup("#isPercent").query();
        test.clickOn(v);
        test.push(KeyCode.ENTER);
        v=test.lookup("#redPrice").query();
        test.clickOn(v);
        test.write("10");
        v=test.lookup("#startHour").query();
        test.clickOn(v);
        Node s=test.lookup(n -> ((Text)n).getText().contains("00")).query();
        test.clickOn(s);
        v=test.lookup("#endHour").query();
        test.clickOn(v);
        s=test.lookup(n -> ((Text)n).getText().contains("00")).query();
        test.clickOn(s);
        v=test.lookup("#startMin").query();
        test.clickOn(v);
        s=test.lookup(n -> ((Text)n).getText().contains("00")).query();
        test.clickOn(s);
        v=test.lookup("#endMin").query();
        test.clickOn(v);
        s=test.lookup(n -> ((Text)n).getText().contains("00")).query();
        test.clickOn(s);
        v=test.lookup("#addReduction").query();
        test.clickOn(v);
//        var v=test.lookup(node -> ((Text)node).getText().contains("AirportA")).query();
//        test.clickOn(v);
    }
}
