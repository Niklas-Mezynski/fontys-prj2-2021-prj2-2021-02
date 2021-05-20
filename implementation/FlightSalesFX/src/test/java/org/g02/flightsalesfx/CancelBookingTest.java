package org.g02.flightsalesfx;

import javafx.stage.Stage;
import org.g02.flightsalesfx.businessEntities.*;
import org.g02.flightsalesfx.businessLogic.*;
import org.junit.jupiter.api.AfterAll;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.Start;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CancelBookingTest {

    private Stage s;
    private SalesEmployee se = new SalesEmployeeImpl("SalesEmployee 1" ,"se@aviatech.de", "se1");
    private SalesOfficer so = new SalesOfficerImpl("so1", "so@so.de", "so1");
    private Booking cancelableBooking;
    private Booking uncancelableBooking;
    private Booking uncancelableBooking1;
    @Mock
    private BusinessLogicAPI businessLogicAPI;

    @AfterAll
    static void afterAll(FxRobot robot){
        App.employee= new SalesOfficerImpl("Peter", "peter@gmx.de", "peterIstDerBeste");
        App.inRootTab = 0;
        App.setRoot("home");
    }

    @Start
    void start(Stage stage) throws IOException {
        businessLogicAPI = Mockito.mock(BusinessLogicAPI.class);



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
        Flight flightDepartIn1D = new FlightImpl(SalesOfficerImpl.of(so), 1, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(3), routes.get(1), planes.get(0), 42.42 );
        mockedFlights.add(flightDepartIn2M);
        mockedFlights.add(flightDepart1MAgo);
        mockedFlights.add(flightDepartIn1D);
        List<Ticket> tickets = new ArrayList<Ticket>();
        List<Booking> bookings = new ArrayList<Booking>();
        SeatOption[] noSeatOptions = new SeatOptionImpl[0];
        FlightOption[] noFlightOptions = new FlightOptionImpl[0];
        Ticket t = new TicketImpl(flightDepartIn1D, flightDepartIn1D.getPlane().getAllSeats().get(0), "fName", "lName", noSeatOptions);
        tickets.add(t);
        Ticket[] ts = {t};
        cancelableBooking = new BookingImpl(se,
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
        uncancelableBooking = new BookingImpl(se,
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
        uncancelableBooking1 = new BookingImpl(se,
                flightDepartIn2M,
                ts2,
                noFlightOptions,
                "test3@gmail.com",
                LocalDateTime.of(2021, 4, 10, 10, 0),
                123.99
                );


    }

}
