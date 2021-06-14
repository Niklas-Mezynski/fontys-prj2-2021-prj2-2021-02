package org.g02.flightsalesfx.businessLogic;

import org.g02.flightsalesfx.businessEntities.*;
import org.g02.flightsalesfx.persistence.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.SoftAssertions;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class BookingBusinessLogicTest {

    @Mock
    private PersistenceAPI persistenceAPI;

    @Mock
    private TicketStorageService ticketStorageService;

    @Mock
    private BookingStorageService bookingStorageService;

    @Mock
    private EmployeeStorageService employeeStorageService;

    @Mock
    private PlaneStorageService planeStorageService;

    @Mock
    private SeatStorageService seatStorageService;

    @Mock
    private SeatOptionsStorageService seatOptionsStorageService;

    @Mock
    private FlightOptionStorageService flightOptionStorageService;

    @Mock
    private AirportStorageService airportStorageService;

    @Mock
    private RouteStorageService routeStorageService;

    @Mock
    private PriceReductionStorageService priceReductionStorageService;

    @Mock
    private FlightStorageService flightStorageService;

    Airport airportA;
    Airport airportB;
    Route r;
    Plane p;

    Seat seat1A;
    Seat seat1B;
    Seat seat1C;
    Seat seat1D;
    Seat seat2A;
    Seat seat2B;
    Seat seat2C;
    Seat seat2D;
    Seat seat3A;
    Seat seat3B;
    Seat seat3C;
    Seat seat3D;

    SeatOption so;

    SalesOfficerImpl soff;
    SalesEmployee se;

    FlightImpl f;

    FlightOptionImpl  flightOption1;
    FlightOptionImpl  flightOption2;

    @BeforeEach
    public void setup(){

        List<Ticket> tickets = new ArrayList<>();
        List<Booking> bookings = new ArrayList<>();
        List<Employee> employees = new ArrayList<>();
        List<Plane> planes = new ArrayList<>();//
        List<Seat> seats = new ArrayList<>();//
        List<SeatOption> seatOptions = new ArrayList<>();//
        List<Flight> flights = new ArrayList<>();//
        List<Airport> airports = new ArrayList<>();//
        List<Route> routes = new ArrayList<>();//
        List<PriceReduction> priceReductions = new ArrayList<>();
        List<FlightOption> flightOptions = new ArrayList<>();//





        airportA = new AirportImpl("AirportA", "AirportA", "AirportA");
        airports.add(airportA);
        airportB = new AirportImpl("AirportB", "AirportB", "AirportB");
        airports.add(airportB);

        r = new RouteImpl(airportA, airportB);
        routes.add(r);

        p = new PlaneImpl("PlaneA", "PlaneA", "PlaneA");
        planes.add(p);

        seat1A = new SeatImpl(0, 0);
        seat1B = new SeatImpl(0, 1);
        seat1C = new SeatImpl(0, 2);
        seat1D = new SeatImpl(0, 3);
        seat2A = new SeatImpl(1, 0);
        seat2B = new SeatImpl(1, 1);
        seat2C = new SeatImpl(1, 2);
        seat2D = new SeatImpl(1, 3);
        seat3A = new SeatImpl(2, 0);
        seat3B = new SeatImpl(2, 1);
        seat3C = new SeatImpl(2, 2);
        seat3D = new SeatImpl(2, 3);


        seats.add(seat1A);
        seats.add(seat1B);
        seats.add(seat1C);
        seats.add(seat1D);
        seats.add(seat2A);
        seats.add(seat2B);
        seats.add(seat2C);
        seats.add(seat2D);
        seats.add(seat3A);
        seats.add(seat3B);
        seats.add(seat3C);
        seats.add(seat3D);

        so = new SeatOptionImpl("First Row", 12.99);
        seatOptions.add(so);

        seats.stream().filter(seat -> seat.getRowNumber() == 0).forEach(seat -> seat.addSeatOption(so));
        p.addAllSeats(seats);

        soff = new SalesOfficerImpl("OfficerA", "OfficerA", "OfficerA");
        employees.add(soff);

        se = new SalesEmployeeImpl("EmployeeA", "EmployeeA", "EmployeeA");
        employees.add(se);

        f = new FlightImpl(soff , 1, LocalDateTime.of(2020, 12, 1, 12, 0), LocalDateTime.of(2020, 12, 2, 12, 0), r, p, 100);


        flightOption1 = new FlightOptionImpl("Tea", 4, 10);
        flightOption2 = new FlightOptionImpl("Ramen", 4, 10);


        flightOptions.add(flightOption1);
        flightOptions.add(flightOption2);
        f.addAllFlightOptions(flightOptions);
        flights.add(f);


        SeatOption[] seatOptionsToBook = new SeatOption[0];
        TicketImpl[] ticketsToBook = {new TicketImpl(f, seat1C, "Peter","Lustig", seatOptionsToBook),
                new TicketImpl(f, seat1D, "Peter","Lustig", seatOptionsToBook)};
        FlightOption[] flightOptionsToBook = {flightOption1,flightOption2};
        BookingImpl bookingToInsert = new BookingImpl(se, f, ticketsToBook, flightOptionsToBook, "test@mail.de", LocalDateTime.of(2020, 11, 1, 12, 0), 123.12);

        bookings.add(bookingToInsert);
        for(Ticket t : ticketsToBook){
            tickets.add(t);
        }

        ticketStorageService = Mockito.mock(TicketStorageService.class);
        bookingStorageService = Mockito.mock(BookingStorageService.class);
        employeeStorageService = Mockito.mock(EmployeeStorageService.class);
        planeStorageService = Mockito.mock(PlaneStorageService.class);
        seatStorageService = Mockito.mock(SeatStorageService.class);
        seatOptionsStorageService = Mockito.mock(SeatOptionsStorageService.class);
        flightStorageService = Mockito.mock(FlightStorageService.class);
        airportStorageService = Mockito.mock(AirportStorageService.class);
        routeStorageService = Mockito.mock(RouteStorageService.class);
        priceReductionStorageService = Mockito.mock(PriceReductionStorageService.class);
        flightOptionStorageService = Mockito.mock(FlightOptionStorageService.class);
        persistenceAPI = Mockito.mock(PersistenceAPI.class);

        Mockito.when(ticketStorageService.getAll()).thenReturn(tickets);
        Mockito.when(bookingStorageService.getAll()).thenReturn(bookings);
        Mockito.when(employeeStorageService.getAll()).thenReturn(employees);
        Mockito.when(planeStorageService.getAll()).thenReturn(planes);
        //Mockito.when(seatStorageService.getAll()).thenReturn();
        Mockito.when(seatOptionsStorageService.getAll()).thenReturn(seatOptions);
        Mockito.when(flightStorageService.getAll()).thenReturn(flights);
        Mockito.when(airportStorageService.getAll()).thenReturn(airports);
        Mockito.when(routeStorageService.getAll()).thenReturn(routes);
        Mockito.when(priceReductionStorageService.getAll()).thenReturn(priceReductions);
        Mockito.when(flightOptionStorageService.getAll()).thenReturn(flightOptions);

        Mockito.when(persistenceAPI.getTicketStorageService(Mockito.any())).thenReturn(ticketStorageService);
        Mockito.when(persistenceAPI.getBookingStorageService(Mockito.any())).thenReturn(bookingStorageService);
        Mockito.when(persistenceAPI.getEmployeeStorageService(Mockito.any())).thenReturn(employeeStorageService);
        Mockito.when(persistenceAPI.getPlaneStorageService(Mockito.any())).thenReturn(planeStorageService);
        Mockito.when(persistenceAPI.getSeatStorageService(Mockito.any())).thenReturn(seatStorageService);
        Mockito.when(persistenceAPI.getSeatOptionStorageService(Mockito.any())).thenReturn(seatOptionsStorageService);
        Mockito.when(persistenceAPI.getFlightStorageService(Mockito.any())).thenReturn(flightStorageService);
        Mockito.when(persistenceAPI.getAirportStorageService(Mockito.any())).thenReturn(airportStorageService);
        Mockito.when(persistenceAPI.getRouteStorageService(Mockito.any())).thenReturn(routeStorageService);
        Mockito.when(persistenceAPI.getPriceReductionStorageService(Mockito.any())).thenReturn(priceReductionStorageService);
        Mockito.when(persistenceAPI.getFlightOptionStorageService(Mockito.any())).thenReturn(flightOptionStorageService);
    }

    @Test
    public void t01bookingBeforeDepartPasses(){
        BusinessLogicAPIImpl b = new BusinessLogicAPIImpl(persistenceAPI);
        SeatOption[] seatOptions = new SeatOption[0];
        TicketImpl[] ticketsToBook = {new TicketImpl(f, seat1A, "Peter","Lustig", seatOptions),
                new TicketImpl(f, seat1B, "Peter","Lustig", seatOptions)};
        FlightOption[] flightOptionsToBook = new FlightOption[0];
        BookingImpl bookingToInsert = new BookingImpl(se, f, ticketsToBook, flightOptionsToBook, "test@mail.de", LocalDateTime.of(2020, 11, 1, 12, 0), 123.12);

        Mockito.when(bookingStorageService.add(Mockito.any())).thenReturn(bookingToInsert);

        assertThat(b.addBookingFromUI(bookingToInsert)).isEqualTo(true);
        Mockito.verify(bookingStorageService, Mockito.times(1)).add(bookingToInsert);

    }

    @Test
    public void t02bookingAfterDepartFails(){
        BusinessLogicAPIImpl b = new BusinessLogicAPIImpl(persistenceAPI);
        SeatOption[] seatOptions = new SeatOption[0];
        TicketImpl[] ticketsToBook = {new TicketImpl(f, seat1A, "Peter","Lustig", seatOptions),
                new TicketImpl(f, seat1B, "Peter","Lustig", seatOptions)};
        FlightOption[] flightOptionsToBook = new FlightOption[0];
        BookingImpl bookingToInsert = new BookingImpl(se, f, ticketsToBook, flightOptionsToBook, "test@mail.de", LocalDateTime.of(2021, 11, 1, 12, 0), 123.12);

        Mockito.when(bookingStorageService.add(Mockito.any())).thenReturn(bookingToInsert);

        assertThat(b.addBookingFromUI(bookingToInsert)).isEqualTo(false);
        Mockito.verify(bookingStorageService, Mockito.times(0)).add(Mockito.any());

    }

    @Test
    public void t03bookingForAvailableSeatPasses(){
        BusinessLogicAPIImpl b = new BusinessLogicAPIImpl(persistenceAPI);
        SeatOption[] seatOptions = new SeatOption[0];
        TicketImpl[] ticketsToBook = {new TicketImpl(f, seat1A, "Peter","Lustig", seatOptions),
                new TicketImpl(f, seat1B, "Peter","Lustig", seatOptions)};
        FlightOption[] flightOptionsToBook = new FlightOption[0];
        BookingImpl bookingToInsert = new BookingImpl(se, f, ticketsToBook, flightOptionsToBook, "test@mail.de", LocalDateTime.of(2020, 11, 1, 12, 0), 123.12);

        Mockito.when(bookingStorageService.add(Mockito.any())).thenReturn(bookingToInsert);

        assertThat(b.addBookingFromUI(bookingToInsert)).isEqualTo(true);
        Mockito.verify(bookingStorageService, Mockito.times(1)).add(bookingToInsert);

    }

    @Test
    public void t04bookingForBookedSeatFails(){
        BusinessLogicAPIImpl b = new BusinessLogicAPIImpl(persistenceAPI);
        SeatOption[] seatOptions = new SeatOption[0];
        TicketImpl[] ticketsToBook = {new TicketImpl(f, seat1C, "Peter","Lustig", seatOptions),
                new TicketImpl(f, seat1B, "Peter","Lustig", seatOptions)};
        FlightOption[] flightOptionsToBook = new FlightOption[0];
        BookingImpl bookingToInsert = new BookingImpl(se, f, ticketsToBook, flightOptionsToBook, "test@mail.de", LocalDateTime.of(2020, 11, 1, 12, 0), 123.12);

        Mockito.when(bookingStorageService.add(Mockito.any())).thenReturn(bookingToInsert);

        assertThat(b.addBookingFromUI(bookingToInsert)).isEqualTo(false);
        Mockito.verify(bookingStorageService, Mockito.times(0)).add(Mockito.any());

    }

    @Test
    public void t05bookingWithValidQuantityOfFlightOptionsPasses(){
        BusinessLogicAPIImpl b = new BusinessLogicAPIImpl(persistenceAPI);
        SeatOption[] seatOptions = new SeatOption[0];
        TicketImpl[] ticketsToBook = {new TicketImpl(f, seat1A, "Peter","Lustig", seatOptions),
                new TicketImpl(f, seat1B, "Peter","Lustig", seatOptions)};
        FlightOption[] flightOptionsToBook = {flightOption1,flightOption1,flightOption1,flightOption2,flightOption2,flightOption2};
        BookingImpl bookingToInsert = new BookingImpl(se, f, ticketsToBook, flightOptionsToBook, "test@mail.de", LocalDateTime.of(2020, 11, 1, 12, 0), 123.12);

        Mockito.when(bookingStorageService.add(Mockito.any())).thenReturn(bookingToInsert);

        assertThat(b.addBookingFromUI(bookingToInsert)).isEqualTo(true);
        Mockito.verify(bookingStorageService, Mockito.times(1)).add(bookingToInsert);

    }

    @Test
    public void t06bookingWithInvalidQuantityOfFlightOptionsFails(){
        BusinessLogicAPIImpl b = new BusinessLogicAPIImpl(persistenceAPI);
        SeatOption[] seatOptions = new SeatOption[0];
        TicketImpl[] ticketsToBook = {new TicketImpl(f, seat1A, "Peter","Lustig", seatOptions),
                new TicketImpl(f, seat1B, "Peter","Lustig", seatOptions)};
        FlightOption[] flightOptionsToBook = {flightOption1,flightOption1,flightOption1,flightOption1,flightOption2,flightOption2,flightOption2,flightOption2};
        BookingImpl bookingToInsert = new BookingImpl(se, f, ticketsToBook, flightOptionsToBook, "test@mail.de", LocalDateTime.of(2020, 11, 1, 12, 0), 123.12);

        Mockito.when(bookingStorageService.add(Mockito.any())).thenReturn(bookingToInsert);

        assertThat(b.addBookingFromUI(bookingToInsert)).isEqualTo(false);
        Mockito.verify(bookingStorageService, Mockito.times(0)).add(Mockito.any());

    }

    @Test
    public void t07bookingWithInvalidQuantityOfFlightOptionsFails(){
        BusinessLogicAPIImpl b = new BusinessLogicAPIImpl(persistenceAPI);
        SeatOption[] seatOptions = new SeatOption[0];
        TicketImpl[] ticketsToBook = {new TicketImpl(f, seat1A, "Peter","Lustig", seatOptions),
                new TicketImpl(f, seat1B, "Peter","Lustig", seatOptions)};
        FlightOption[] flightOptionsToBook = {flightOption1,flightOption1,flightOption1,flightOption1,flightOption1};
        BookingImpl bookingToInsert = new BookingImpl(se, f, ticketsToBook, flightOptionsToBook, "test@mail.de", LocalDateTime.of(2020, 11, 1, 12, 0), 123.12);

        Mockito.when(bookingStorageService.add(Mockito.any())).thenReturn(bookingToInsert);

        assertThat(b.addBookingFromUI(bookingToInsert)).isEqualTo(false);
        Mockito.verify(bookingStorageService, Mockito.times(0)).add(Mockito.any());

    }

    @Test
    public void t08bookingWithUnavailableSeatOptionsFails(){
        BusinessLogicAPIImpl b = new BusinessLogicAPIImpl(persistenceAPI);
        SeatOption[] seatOptions1 = {new SeatOptionImpl("Not for this Seat", 123.23)};
        SeatOption[] seatOptions = new SeatOption[0];
        TicketImpl[] ticketsToBook = {new TicketImpl(f, seat1A, "Peter","Lustig", seatOptions1),
                new TicketImpl(f, seat1B, "Peter","Lustig", seatOptions)};
        FlightOption[] flightOptionsToBook = new FlightOption[0];
        BookingImpl bookingToInsert = new BookingImpl(se, f, ticketsToBook, flightOptionsToBook, "test@mail.de", LocalDateTime.of(2020, 11, 1, 12, 0), 123.12);

        Mockito.when(bookingStorageService.add(Mockito.any())).thenReturn(bookingToInsert);

        assertThat(b.addBookingFromUI(bookingToInsert)).isEqualTo(false);
        Mockito.verify(bookingStorageService, Mockito.times(0)).add(Mockito.any());

    }

    @Test
    public void t09bookingWithAvailableSeatOptionsPasses(){
        BusinessLogicAPIImpl b = new BusinessLogicAPIImpl(persistenceAPI);
        SeatOption[] seatOptions1 = {so};
        SeatOption[] seatOptions = new SeatOption[0];
        TicketImpl[] ticketsToBook = {new TicketImpl(f, seat1A, "Peter","Lustig", seatOptions1),
                new TicketImpl(f, seat1B, "Peter","Lustig", seatOptions)};
        FlightOption[] flightOptionsToBook = new FlightOption[0];
        BookingImpl bookingToInsert = new BookingImpl(se, f, ticketsToBook, flightOptionsToBook, "test@mail.de", LocalDateTime.of(2020, 11, 1, 12, 0), 123.12);

        Mockito.when(bookingStorageService.add(Mockito.any())).thenReturn(bookingToInsert);

        assertThat(b.addBookingFromUI(bookingToInsert)).isEqualTo(true);
        Mockito.verify(bookingStorageService, Mockito.times(1)).add(bookingToInsert);

    }

    @Test
    public void t10bookingWithSeatOfForeignPlane(){
        BusinessLogicAPIImpl b = new BusinessLogicAPIImpl(persistenceAPI);
        SeatOption[] seatOptions = new SeatOption[0];
        Seat unknownSeat = new SeatImpl(123,123);
        TicketImpl[] ticketsToBook = {new TicketImpl(f, unknownSeat, "Peter","Lustig", seatOptions),
                new TicketImpl(f, seat1B, "Peter","Lustig", seatOptions)};
        FlightOption[] flightOptionsToBook = new FlightOption[0];
        BookingImpl bookingToInsert = new BookingImpl(se, f, ticketsToBook, flightOptionsToBook, "test@mail.de", LocalDateTime.of(2020, 11, 1, 12, 0), 123.12);

        Mockito.when(bookingStorageService.add(Mockito.any())).thenReturn(bookingToInsert);

        assertThat(b.addBookingFromUI(bookingToInsert)).isEqualTo(false);
        Mockito.verify(bookingStorageService, Mockito.times(0)).add(Mockito.any());

    }

}
