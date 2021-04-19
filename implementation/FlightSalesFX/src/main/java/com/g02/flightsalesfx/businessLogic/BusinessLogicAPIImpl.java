package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.CreatePlaneController;
import com.g02.flightsalesfx.businessEntities.*;
import com.g02.flightsalesfx.persistence.PersistenceAPI;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BusinessLogicAPIImpl implements BusinessLogicAPI {

    final PersistenceAPI persistenceAPI;
    private EmployeeManagerImpl employeeManager;
    private PlaneManagerImpl planeManager;
    private SeatManagerImpl seatManager;
    private OptionManagerImpl optionManager;
    private AirportManagerImpl airportManager;
    private RouteManagerImpl routeManager;
    private PriceReductionManagerImpl priceReductionManager;
    private FlightManagerImpl flightManager;
    private TicketManagerImpl ticketManager;
    private BookingManagerImpl bookingManager;
    private ReoccurringFlightManagerImpl reoccurringFlightManager;

    public BusinessLogicAPIImpl(PersistenceAPI persistenceAPI) {
        this.persistenceAPI = persistenceAPI;
    }

    @Override
    public List<Booking> getAllBookings(Predicate<Booking> predicate) {
        return persistenceAPI.getBookingStorageService(bookingManager).getAll().stream().filter(predicate).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<Ticket> getAllTickets(Predicate<Ticket> predicate) {
        return persistenceAPI.getTicketStorageService(ticketManager).getAll().stream().filter(predicate).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public TicketManager getTicketManager() {
        if(ticketManager == null){
            ticketManager = new TicketManagerImpl();
            ticketManager.setTicketStorageService(persistenceAPI.getTicketStorageService(ticketManager));
        }
        return ticketManager;
    }

    @Override
    public BookingManager getBookingManager() {
        if(bookingManager == null){
            bookingManager = new BookingManagerImpl();
            bookingManager.setBookingStorageService(persistenceAPI.getBookingStorageService(bookingManager));
        }
        return bookingManager;
    }

    @Override
    public EmployeeManager getEmployeeManager() {
        if (employeeManager == null) {
            employeeManager = new EmployeeManagerImpl();
            employeeManager.setEmployeeStorageService(persistenceAPI.getEmployeeStorageService(employeeManager));
        }
        return employeeManager;
    }

    @Override
    public PlaneManager getPlaneManager() {
        if (planeManager == null) {
            planeManager = new PlaneManagerImpl();
            planeManager.setPlaneStorageService(persistenceAPI.getPlaneStorageService(planeManager));
        }
        return planeManager;
    }

    @Override
    public SeatManager getSeatManager() {
        if (seatManager == null) {
            seatManager = new SeatManagerImpl();
            seatManager.setSeatStorageService(persistenceAPI.getSeatStorageService(seatManager));
        }
        return seatManager;
    }

    @Override
    public OptionManager getOptionManager() {
        if (optionManager == null) {
            optionManager = new OptionManagerImpl();
            optionManager.setSeatOptionStorageService(persistenceAPI.getSeatOptionStorageService(optionManager));
            optionManager.setFlightOptionStorageService(persistenceAPI.getFlightOptionStorageService(optionManager));
        }
        return optionManager;
    }

    @Override
    public AirportManager getAirportManager() {
        if(airportManager == null) {
            airportManager = new AirportManagerImpl();
            airportManager.setAirportStorageService(persistenceAPI.getAirportStorageService(airportManager));
        }

        return airportManager;
    }

    @Override
    public RouteManager getRouteManager() {
        if(routeManager == null) {
            routeManager = new RouteManagerImpl();
            routeManager.setRouteStorageService(persistenceAPI.getRouteStorageService(routeManager));
        }

        return routeManager;
    }

    @Override
    public PriceReductionManager getPriceReductionManager() {
        if(priceReductionManager == null) {
            priceReductionManager = new PriceReductionManagerImpl();
            priceReductionManager.setPriceReductionStorageService(persistenceAPI.getPriceReductionStorageService(priceReductionManager));
        }
        return priceReductionManager;
    }


    @Override
    public FlightManager getFlightManager() {
        if(flightManager == null) {
            flightManager = new FlightManagerImpl();
            flightManager.setFlightStorageService(persistenceAPI.getFlightStorageService(flightManager));
        }
        return flightManager;
    }

    @Override
    public ReoccurringFlightManager getReoccurringFlightManager() {
        //todo: review to verify consistency
        if(reoccurringFlightManager == null) {
            reoccurringFlightManager = new ReoccurringFlightManagerImpl();
            reoccurringFlightManager.setReoccurringFlightStorageService(flightManager);
        }
        return reoccurringFlightManager;
    }

    @Override
    public Employee login(String email, String password) {
        var employeeStorageService = persistenceAPI.getEmployeeStorageService(getEmployeeManager());
        var first = employeeStorageService.getAll().stream().filter(employee -> employee.getEmail().equals(email) && employee.getPassword().equals(password)).findFirst();
        if (first.isEmpty()) {
            return null;
        }
        return first.get();
    }

    @Override
    public boolean createPlaneFromUI(String name, String type, String manufacturer, List<Seat> seats) {
        var plane = getPlaneManager().createPlane(name, manufacturer, type);
        plane.addAllSeats(seats.stream().sorted().collect(Collectors.toList()));
        System.out.println(plane);
        var planeStorageService = persistenceAPI.getPlaneStorageService(getPlaneManager());
        return planeStorageService.add(plane);
    }

    @Override
    public List<Plane> getAllPlanes(Predicate<Plane> predicate) {
        var all = persistenceAPI.getPlaneStorageService(planeManager).getAll();
        var planeStream = all.stream().filter(predicate);
        return planeStream.collect(Collectors.toUnmodifiableList());
    }

    @Override
    public void viewPlane(Plane plane) {
        // Todo
    }

    @Override
    public boolean createRouteFromUI(Airport departure, Airport arrival) {
        var route = getRouteManager().createRoute(departure, arrival);
        System.out.println(route);

        var routeStorageService = persistenceAPI.getRouteStorageService(getRouteManager());
        return routeStorageService.add(route);
    }

    @Override
    public List<Airport> getAllAirports(Predicate<Airport> predicate) {
        var all = persistenceAPI.getAirportStorageService(airportManager).getAll();
        return all.stream().filter(predicate).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<Route> getAllRoutes(Predicate<Route> predicate) {
        var all = persistenceAPI.getRouteStorageService(routeManager).getAll();
        return all.stream().filter(predicate).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public boolean createFlightFromUI(SalesOfficer creator, int fNumber, LocalDateTime dep, LocalDateTime arr, Route route, Plane plane, double price) {
        var flight = getFlightManager().createFlight(creator, fNumber, dep, arr, route, plane, price);
        System.out.println(flight);

        var flightStorageService = persistenceAPI.getFlightStorageService(getFlightManager());
        return flightStorageService.add(flight);
    }

    @Override
    public boolean createFlightFromUI(Flight flight) {
        var f = getFlightManager().createFlight(flight.getCreatedBy(), flight.getFlightNumber(), flight.getDeparture(), flight.getArrival(), flight.getRoute(), flight.getPlane(), flight.getPrice());

        var flightStorageService = persistenceAPI.getFlightStorageService(getFlightManager());
        return flightStorageService.add(flight);
    }

    @Override
    public boolean createReoccurringFlightFromUI(Flight flight, int interval) {
        var reOccurFlight = getReoccurringFlightManager().createRoccurringFlight(flight, interval);
        System.out.println(reOccurFlight);

        var flightStorageService = persistenceAPI.getFlightStorageService(getFlightManager());
        if(flightStorageService.remove(flight)) {
            return flightStorageService.add(reOccurFlight);
        }
        return false;
    }

    @Override
    public void createAirportFromUI(String name, String city, String country){
        var airport = getAirportManager().createAirport(name, city, country);
        persistenceAPI.getAirportStorageService(getAirportManager()).add(airport);
    }

    @Override
    public List<Flight> getAllFlights(Predicate<Flight> predicate) {
        var all = persistenceAPI.getFlightStorageService(flightManager).getAll();
        return all.stream().filter(predicate).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public boolean addFlightOptionFromUI(String name, int maxAvailable, double price, Flight flight) {
        var flightOption = getOptionManager().createFlightOption(name, maxAvailable, price);
        flight.addFlightOption(flightOption);
        return persistenceAPI.getFlightOptionStorageService(getOptionManager()).add(flightOption);
    }

    @Override
    public boolean addBookingFromUI(Booking booking){
        return persistenceAPI.getBookingStorageService(getBookingManager()).add(booking);
    }

    @Override
    public boolean addTicketFromUI(Ticket t){
        return  persistenceAPI.getTicketStorageService(getTicketManager()).add(t);
    }


}
