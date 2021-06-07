package org.g02.flightsalesfx.businessLogic;

import org.g02.flightsalesfx.businessEntities.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    public List<Plane> getAllPlanes();

    public List<Route> getAllRoutes(Predicate<Route> predicate);

    void viewPlane(Plane plane);

    public List<Airport> getAllAirports(Predicate<Airport> predicate);

    boolean createFlightFromUI(SalesOfficer creator, LocalDateTime dep, LocalDateTime arr, Route route, Plane plane, double price, List<? extends FlightOption> flightOptions);

    public boolean createFlightFromUI(Flight flight);

    public boolean createReoccurringFlightFromUI(Flight flight, Duration interval);

    public void createAirportFromUI(String name, String city, String country);

    public boolean createPriceReductionFromUI(PriceReduction priceReduction);

    public List<Flight> getAllFlights(Predicate<Flight> predicate);

    public List<Flight> getAllFlights();

    public List<PriceReduction> getAllPriceReductions(Predicate<PriceReduction> predicate);

    public List<PriceReduction> getAllPriceReductions();

    boolean addFlightOptionFromUI(String name, int maxAvailable, double price, Flight flight);

    boolean addBookingFromUI(Booking b);

    boolean addTicketFromUI(Ticket ticket);

    boolean deletePlane(PlaneImpl oldPlane);

    Plane updatePlane(PlaneImpl oldPlane, String name, String type, String manufacturer, List<Seat> collect);

    Flight updateFlight(FlightImpl oldFlight, LocalDateTime dep, LocalDateTime arr, double price, boolean salesprocess);

    public List<Employee> getAllEmployees(Predicate<Employee> predicate);

    /**
     * Calculates the revenue of a booking list, you can add a predicate to filter the list
     */
    double sumRevenue(List<Booking> list, Predicate<Booking> predicate);

    /**
     * Calculates the revenue an employee has made each month beginning from {@code startDate}
     *
     * @return An ordered HashMap with the dates as key and the revenue as value
     */
    LinkedHashMap<LocalDateTime, Double> getMonthlyRevenue(SalesEmployee se, LocalDateTime startDate);

    /**
     * Calculates the average revenue all employees have made each month beginning from {@code startDate}
     *
     * @return An ordered HashMap with the dates as key and the average revenue as value
     */
    LinkedHashMap<LocalDateTime, Double> getAvgMonthlyRevenues(LocalDateTime startDate);

    double totalRevenueByEmp(SalesEmployee se);

    int totalNumOfBookingsByAnEmployee(SalesEmployee se);

    double avgNumOfTicketsPerBooking(SalesEmployee se);

    double totalRevenueByRoute(Route route);

    double sumOfAClassesRevenue(Route route, String className);

    double getRevenueForSpecificRouteInOneYear(Route route, int year);

    Optional<Booking> getFirstBookingOfARoute(Route route);
}
