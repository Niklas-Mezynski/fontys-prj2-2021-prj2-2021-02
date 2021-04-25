package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.businessEntities.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;

public interface BusinessLogicAPI {

    public List<Booking> getAllBookings(Predicate<Booking> predicate);

    public List<Ticket> getAllTickets(Predicate<Ticket> predicate);

    public TicketManager getTicketManager();

    public BookingManager getBookingManager();

    public EmployeeManager getEmployeeManager();

    public PlaneManager getPlaneManager();

    public SeatManager getSeatManager();

    public OptionManager getOptionManager();

    public AirportManager getAirportManager();

    public RouteManager getRouteManager();

    public PriceReductionManager getPriceReductionManager();

    public FlightManager getFlightManager();

    public ReoccurringFlightManager getReoccurringFlightManager();

    public Employee login(String email, String password);

    public Plane createPlaneFromUI(String name, String type, String manufacturer, List<Seat> seats);

    public boolean createRouteFromUI(Airport departure, Airport arrival);

    public List<Plane> getAllPlanes(Predicate<Plane> predicate);

    public List<Route> getAllRoutes(Predicate<Route> predicate);

    void viewPlane(Plane plane);

    public List<Airport> getAllAirports(Predicate<Airport> predicate);

    public boolean createFlightFromUI(SalesOfficer creator, int fNumber, LocalDateTime dep, LocalDateTime arr, Route route, Plane plane, double price);

    public boolean createFlightFromUI(Flight flight);

    public boolean createReoccurringFlightFromUI(Flight flight, Duration interval);

    public void createAirportFromUI(String name, String city, String country);

    public List<Flight> getAllFlights(Predicate<Flight> predicate);

    boolean addFlightOptionFromUI(String name, int maxAvailable, double price, Flight flight);

    boolean addBookingFromUI(Booking b);

    boolean addTicketFromUI(Ticket ticket);

    boolean deletePlane(PlaneImpl oldPlane);

    Plane updatePlane(PlaneImpl oldPlane, String name, String type, String manufacturer, List<Seat> collect);
}
