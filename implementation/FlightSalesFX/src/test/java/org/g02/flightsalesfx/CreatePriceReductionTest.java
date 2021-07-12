package org.g02.flightsalesfx;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.testfx.assertions.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
public class CreatePriceReductionTest {
    private Stage stage;
    CreatePriceReductions cPR;
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
        cPR=new CreatePriceReductions();
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
        var pri1=new StaticPriceReductionImpl("Winter Sale",LocalDateTime.of(2021,10,10,0,0),LocalDateTime.of(2020,10,10,0,0),true,10);
        var pri2=new StaticPriceReductionImpl("Summer Sale",LocalDateTime.of(2021,10,20,0,0),LocalDateTime.of(2020,10,20,0,0),false,50);
        priceReductions.add(pri1);
        priceReductions.add(pri2);
        Mockito.when(businessLogicAPI.getAllPriceReductions(any())).thenReturn(priceReductions);
        Mockito.when(businessLogicAPI.getAllPriceReductions()).thenReturn(priceReductions);
        PriceReduction pr1=new StaticPriceReductionImpl("Winter Sale",LocalDateTime.of(2021,10,10,0,0),LocalDateTime.of(2020,10,10,0,0),true,10);
        PriceReduction pr2=new StaticPriceReductionImpl("Summer Sale",LocalDateTime.of(2021,10,20,0,0),LocalDateTime.of(2020,10,20,0,0),false,50);
        Mockito.when(businessLogicAPI.createPriceReductionFromUI(pr1)).thenReturn(true);
        Mockito.when(businessLogicAPI.createPriceReductionFromUI(pr2)).thenReturn(true);
        var app = new App();
        app.start(stage);
        App.businessLogicAPI = businessLogicAPI;
        App.setRoot("home");
        this.stage = stage;
    }
    @Test
    public void createPriceReduction(FxRobot test){
        ArrayList<String[]> list=new ArrayList<>();
        list.add(new String[]{"Winter Sale","10,10/20","10.10\\2021","true","10"});
        list.add(new String[]{"Summer Sale","20,10/20","20.10\\2021","false","50"});
        list.add(new String[]{"Error","","","",""});
        for(int i=0;i<3;i++) {
            String[] str=list.get(i);
            test.clickOn(test.lookup("#redName").queryAs(TextField.class));
            test.write(str[0]);
            Node v = test.lookup("#startDate").query();
            test.clickOn(v);
            test.write(str[1]);
            v = test.lookup("#endDate").query();
            test.clickOn(v);
            test.write(str[2]);
            v = test.lookup("#isPercent").query();
            if(str[3].equals("true")) {
                test.clickOn(v);
            }
            v = test.lookup("#redPrice").query();
            test.clickOn(v);
            test.write(str[4]);
            v = test.lookup("#endHour").query();
            test.clickOn(v);
            test.push(KeyCode.DOWN);
            v = test.lookup("#endMin").query();
            test.clickOn(v);
            test.push(KeyCode.DOWN);
            v = test.lookup("#startHour").query();
            test.clickOn(v);
            test.push(KeyCode.DOWN);
            v = test.lookup("#startMin").query();
            test.clickOn(v);
            test.push(KeyCode.DOWN);
            test.push(KeyCode.ENTER);
            v=test.lookup("#addReduction").queryAs(Button.class);
            test.moveTo(v);
            test.clickOn(v);
            test.clickOn(v);
            if(!str[0].equals("Error")){
                var T=test.lookup("#redName").queryAs(TextField.class);
                assertThat(T.getText()).isEmpty();
            }
            else {
                Node dialogPane = test.lookup(".dialog-pane").queryAs(DialogPane.class);
                var are_you_sure = test.from(dialogPane).lookup((Text t) -> t.getText().startsWith("There was an error while saving "));
                assertThat(are_you_sure.queryAll()).isNotEmpty();
                for (Button queryAll : test.from(dialogPane).lookup((Node node) -> node instanceof Button).queryAllAs(Button.class)) {
                    System.out.println(queryAll.getText());
                    if (queryAll.getText().equals("OK")) {
                        test.clickOn(queryAll);
                    }
                }
            }
        }
    }
}
