package com.g02.flightsalesfx;

import com.g02.flightsalesfx.businessEntities.*;
import com.g02.flightsalesfx.businessLogic.*;
import javafx.scene.control.Spinner;
import javafx.scene.text.Text;
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
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.testfx.assertions.api.Assertions.assertThat;
@ExtendWith(ApplicationExtension.class)
public class BookingTest {

    private Stage stage;
    @Mock
    private BusinessLogicAPI businessLogicAPI;


    @AfterAll
    static void afterAll(FxRobot test) throws IOException {
        App.employee= new SalesOfficerImpl("Peter", "peter@gmx.de", "peterIstDerBeste");
        App.inRootTab = 0;
        App.setRoot("home");
    }

    @Start
    void start(Stage stage) throws IOException {
        businessLogicAPI = Mockito.mock(BusinessLogicAPI.class);
        List<Flight> flights = new ArrayList<>();
        var r=new RouteImpl(new AirportImpl("AirportA","AirportA","AirportA"),new AirportImpl("AirportB","AirportB","AirportB"));
        var p=new PlaneImpl("PlaneA","PlaneA","PlaneA");
        List<Seat> seats=new ArrayList<>();
        seats.add(new SeatImpl(0,0));
        seats.add(new SeatImpl(0,1));
        seats.add(new SeatImpl(0,2));
        seats.add(new SeatImpl(0,3));
        seats.add(new SeatImpl(1,0));
        seats.add(new SeatImpl(1,1));
        seats.add(new SeatImpl(1,2));
        seats.add(new SeatImpl(1,3));
        seats.add(new SeatImpl(2,0));
        seats.add(new SeatImpl(2,1));
        seats.add(new SeatImpl(2,2));
        seats.add(new SeatImpl(2,3));
        p.addAllSeats(seats);
        var f=new FlightImpl(new SalesOfficerImpl("OfficerA","OfficerA","OfficerA"),1, LocalDateTime.of(2020,12,1,12,0),LocalDateTime.of(2020,12,2,12,0),r,p,100);
        List<FlightOption> flOp=new ArrayList<>();
        flOp.add(new FlightOptionImpl("Tea",4,10));
        flOp.add(new FlightOptionImpl("Ramen",4,10));
        f.addAllFlightOptions(flOp);
        flights.add(f);
        Mockito.when(businessLogicAPI.getAllFlights(any())).thenReturn(flights);
        Mockito.when(businessLogicAPI.getBookingManager()).thenReturn(new BookingManagerImpl());
        Mockito.when(businessLogicAPI.getTicketManager()).thenReturn(new TicketManagerImpl());
        Mockito.when(businessLogicAPI.addBookingFromUI(any())).thenReturn(true);
        Mockito.when(businessLogicAPI.addTicketFromUI(any())).thenReturn(true);

        var app = new App();
        app.start(stage);
        App.businessLogicAPI = businessLogicAPI;
        App.employee=new SalesEmployeeImpl("Test","Test","Test");
        App.setRoot("salesEmployeeHome");
        this.stage = stage;
    }



    @BeforeEach
    void goToCreateRoute(FxRobot test) {
        test.clickOn(test.lookup("#createBookingButton").queryButton());
    }


    @Test
    void createRoute(FxRobot test) {
        var v=test.lookup(node -> ((Text)node).getText().contains("AirportA")).query();
        test.clickOn(v);
        v=test.lookup(n->((Text)n).getText().contains("Seat All")).query();
        test.clickOn(v);
        var buttons=test.lookup(n->n instanceof Button).queryAllAs(Button.class);
        buttons=buttons.stream().filter(s->s.getText().contains("01")).collect(Collectors.toSet());
        buttons.stream().forEach(test::clickOn);
        v=test.lookup(node -> ((Text)node).getText().contains("Passenger")).query();
        test.clickOn(v);
        var tf=test.lookup(t->t instanceof TextField).queryAllAs(TextField.class);
        tf.stream().filter(t->t.getPromptText().contains("First")).forEach(t->test.clickOn(t).write("Best"));
        tf.stream().filter(t->t.getPromptText().contains("Last")).forEach(t->test.clickOn(t).write("Guy"));
        tf.stream().filter(t->t.getPromptText().contains("Mail")).forEach(t->test.clickOn(t).write("First@Last.de"));
        v=test.lookup(n->((Text)n).getText().equals("Flight Options")).query();
        test.clickOn(v);
        var spinners=test.lookup(t->t instanceof Spinner).queryAllAs(Spinner.class);
        spinners.stream().forEach(s-> test.clickOn(s.getChildrenUnmodifiable().get(1)));
        v=test.lookup(node -> ((Text)node).getText().equals("Confirmation")).query();
        test.clickOn(v);
        test.clickOn(test.lookup("#createBooking").queryButton());


    }
}
