package org.g02.flightsalesfx;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableRow;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.g02.flightsalesfx.businessEntities.*;
import org.g02.flightsalesfx.businessLogic.*;
import org.g02.flightsalesfx.persistence.BookingStorageService;
import org.g02.flightsalesfx.persistence.PersistenceAPI;
import org.g02.flightsalesfx.persistence.TicketStorageService;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.testfx.assertions.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
public class CancelBookingTest {

    private Stage stage;
    private SalesEmployee se = new SalesEmployeeImpl("SalesEmployee 1" ,"se@aviatech.de", "se1");
    private SalesOfficer so = new SalesOfficerImpl("so1", "so@so.de", "so1");
    private Booking cancelableBooking;
    private Booking uncancelableBooking;
    private Booking uncancelableBooking1;
    @Mock
    private BusinessLogicAPI businessLogicAPI;
    @Mock
    private PersistenceAPI persistenceAPI;
    @Mock
    private TicketStorageService ticketStorageService;
    @Mock
    private BookingStorageService bookingStorageService;


    @AfterAll
    static void afterAll(FxRobot robot){
        App.employee= new SalesOfficerImpl("Peter", "peter@gmx.de", "peterIstDerBeste");
        App.inRootTab = 0;
        App.setRoot("home");
    }

    @Start
    void start(Stage stage) throws IOException {
        businessLogicAPI = Mockito.mock(BusinessLogicAPI.class);
        bookingStorageService = Mockito.mock(BookingStorageService.class);
        ticketStorageService = Mockito.mock(TicketStorageService.class);
        persistenceAPI = Mockito.mock(PersistenceAPI.class);


        List<Airport> airports = new ArrayList<Airport>();
        for(int i = 0; i < 5 ; i++){
            airports.add(new AirportImpl("Airport"+i,"City"+i,"Country"+i));
        }
        List<Route> routes = new ArrayList<Route>();
        for(int i = 0; i < airports.size(); i++){
            for(int y = 0; y < airports.size(); y++){
                if(y!=i){
                    routes.add(new RouteImpl(airports.get(i), airports.get(y)));
                }
            }
        }
        List<Seat> seats = new ArrayList<Seat>();
        List<Plane> planes = new ArrayList<Plane>();
        for(int i = 0; i < 6 ; i++){
            Plane p = new PlaneImpl("Aircraft"+i,"Type"+i,"Manuf"+i);
            for(int row = 0; row < 24 ; row++){
                for(int col = 0; col < 6; col++){
                    Seat s = new SeatImpl(row, col);
                    p.addSeat(s);
                    seats.add(s);
                }
            }
            planes.add(p);
        }
        List<Flight> mockedFlights = new ArrayList<Flight>();
        Flight flightDepartIn2M = new FlightImpl(SalesOfficerImpl.of(so), 2, LocalDateTime.now().plusMinutes(2), LocalDateTime.now().plusMinutes(2).plusHours(3), routes.get(1), planes.get(0), 42.42 );
        Flight flightDepart1MAgo = new FlightImpl(SalesOfficerImpl.of(so), 1, LocalDateTime.now().minusMinutes(1), LocalDateTime.now().minusMinutes(1).plusHours(3), routes.get(1), planes.get(0), 42.42 );
        Flight flightDepartIn1D = new FlightImpl(SalesOfficerImpl.of(so), 1, LocalDateTime.now().plusDays(1).plusMinutes(1), LocalDateTime.now().plusDays(1).plusHours(3), routes.get(1), planes.get(0), 42.42 );
        mockedFlights.add(flightDepartIn2M);
        mockedFlights.add(flightDepart1MAgo);
        mockedFlights.add(flightDepartIn1D);
        List<Ticket> tickets = new ArrayList<Ticket>();
        SeatOption[] noSeatOptions = new SeatOptionImpl[0];
        FlightOption[] noFlightOptions = new FlightOptionImpl[0];
        Ticket t = new TicketImpl(flightDepartIn1D, flightDepartIn1D.getPlane().getAllSeats().get(0), "fName", "lName", noSeatOptions);
        tickets.add(t);
        Ticket[] ts = {t};
        cancelableBooking = new BookingImpl(1,
                se,
                flightDepartIn1D,
                ts,
                noFlightOptions,
                "test1@gmail.com",
                LocalDateTime.of(2021, 4, 10, 10, 0),
                123.99
                );
        t = new TicketImpl(flightDepart1MAgo, flightDepart1MAgo.getPlane().getAllSeats().get(0), "fName", "lName", noSeatOptions);
        tickets.add(t);
        Ticket[] ts1 = {t};
        uncancelableBooking = new BookingImpl(2,
                se,
                flightDepart1MAgo,
                ts1,
                noFlightOptions,
                "test2@gmail.com",
                LocalDateTime.of(2021, 4, 10, 10, 0),
                123.99
                );
        t = new TicketImpl(flightDepartIn2M, flightDepartIn2M.getPlane().getAllSeats().get(0), "fName", "lName", noSeatOptions);
        tickets.add(t);
        Ticket[] ts2 = {t};
        uncancelableBooking1 = new BookingImpl(3,
                se,
                flightDepartIn2M,
                ts2,
                noFlightOptions,
                "test3@gmail.com",
                LocalDateTime.of(2021, 4, 10, 10, 0),
                123.99
                );

        List<Booking> bookings = List.of(cancelableBooking, uncancelableBooking, uncancelableBooking1);

        var bookingManager = new BookingManagerImpl();
        var ticketManager = new TicketManagerImpl();

        bookingManager.setBookingStorageService(bookingStorageService);
        ticketManager.setTicketStorageService(ticketStorageService);

        Mockito.when(businessLogicAPI.getBookingManager()).thenReturn(bookingManager);
        Mockito.when(businessLogicAPI.getTicketManager()).thenReturn(ticketManager);
        Mockito.when(businessLogicAPI.getAllBookings(any())).thenReturn(bookings.stream()
                .filter(booking -> booking.getFlight().getDeparture().isAfter(LocalDateTime.now().plusHours(23).plusMinutes(59)))
                .collect(Collectors.toUnmodifiableList()));
        Mockito.when(persistenceAPI.getBookingStorageService(bookingManager)).thenReturn(bookingStorageService);
        Mockito.when(persistenceAPI.getTicketStorageService(ticketManager)).thenReturn(ticketStorageService);

        var app = new App();
        app.start(stage);
        App.businessLogicAPI = businessLogicAPI;
        App.persistenceAPI = persistenceAPI;
        App.employee = new SalesEmployeeImpl("Test", "Test", "Test");
        App.setRoot("salesEmployeeHome");
        this.stage = stage;
    }

    @BeforeEach
    void goToCancelRoute(FxRobot test) {
        test.clickOn(test.lookup("#cancelBookingButton").queryButton());
    }

    @Test
    void onlyCancellableBookingsInTable(FxRobot test){
        var v = test.lookup(node -> ((Text) node).getText().contains("test1")).query();
        assertThat(v).isNotNull();
        var uncancellable = test.lookup(node -> ((Text) node).getText().contains("test2")).queryAll();
        assertThat(uncancellable).isEmpty();
        var uncancellable1 = test.lookup(node -> ((Text) node).getText().contains("test3")).queryAll();
        assertThat(uncancellable).isEmpty();
    }

    @Test
    void cancelBookingTest(FxRobot test){
        var v = test.lookup(node -> ((Text) node).getText().contains("test1")).query();
        test.clickOn(v);
        var buttons = test.lookup(node -> node instanceof Button).queryAllAs(Button.class);
        var cancelbutton = buttons.stream().filter(btn -> btn.getText().toLowerCase(Locale.ROOT).contains("cancel selected ticket")).findFirst().get();
        test.clickOn(cancelbutton);
        Node dialogPane = test.lookup(".dialog-pane").queryAs(DialogPane.class);
        for(Button buttonInPane : test.from(dialogPane).lookup((Node node) -> node instanceof Button).queryAllAs(Button.class)){
            if(buttonInPane.getText().equals(ButtonType.YES.getText())){
                test.clickOn(buttonInPane);
            }
        }

        verify(bookingStorageService, times(1)).remove(cancelableBooking);
    }

    @Test
    void doNotCancelBookingIfNotConfirmed(FxRobot test){
        var v = test.lookup(node -> ((Text) node).getText().contains("test1")).query();
        test.clickOn(v);
        var buttons = test.lookup(node -> node instanceof Button).queryAllAs(Button.class);
        var cancelbutton = buttons.stream().filter(btn -> btn.getText().toLowerCase(Locale.ROOT).contains("cancel selected ticket")).findFirst().get();
        test.clickOn(cancelbutton);
        Node dialogPane = test.lookup(".dialog-pane").queryAs(DialogPane.class);
        for(Button buttonInPane : test.from(dialogPane).lookup((Node node) -> node instanceof Button).queryAllAs(Button.class)){
            if(buttonInPane.getText().equals(ButtonType.NO.getText())){
                test.clickOn(buttonInPane);
            }
        }

        verify(bookingStorageService, times(0)).remove(any());
    }

    @Test
    void throwNotificationIfNoBookingIsSelected(FxRobot test){
        var buttons = test.lookup(node -> node instanceof Button).queryAllAs(Button.class);
        var cancelbutton = buttons.stream().filter(btn -> btn.getText().toLowerCase(Locale.ROOT).contains("cancel selected ticket")).findFirst().get();
        test.clickOn(cancelbutton);

        Node dialogPane = test.lookup(".dialog-pane").queryAs(DialogPane.class);
        var message = test.from(dialogPane).lookup((Text t) -> t.getText().toLowerCase(Locale.ROOT).startsWith("Please select a booking to be cancelled, then press the Button".toLowerCase(Locale.ROOT)));
        assertThat(message.queryAll()).isNotEmpty();
        for(Button buttonInPane : test.from(dialogPane).lookup((Node node) -> node instanceof Button).queryAllAs(Button.class)){
            if(buttonInPane.getText().equals(ButtonType.OK.getText())){
                test.clickOn(buttonInPane);
            }
        }

    }


}
