package org.g02.flightsalesfx.businessLogic;

import org.g02.flightsalesfx.persistence.PersistenceAPI;
import org.g02.flightsalesfx.businessEntities.*;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
        var first=employeeStorageService.get(email);
        if (first.isEmpty()) {
            var examplehash="1000:61a54968a3c8f1eb1dc1173c64f2e6d9:3ce71ef9a8359b266ce8135b4a753ce5d3411a5ee151d315312943f104c35339d190d9aa3328ab462661a339bc597dfffd0e14043b60e0bcffdbe261f1dc2ddc";
            try {
                boolean matched = validatePassword(password, examplehash); //This may seem useless, but this prevents a side-channel attack
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                e.printStackTrace();
            }
            return null;
        }
        var employee=first.get();

        //Check hashed PW in DB against entered one
        try {
            boolean matched = validatePassword(password, employee.getPassword());
            return matched?employee:null;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Plane createPlaneFromUI(String name, String type, String manufacturer, List<Seat> seats) {
        var plane = getPlaneManager().createPlane(name, manufacturer, type);
        plane.addAllSeats(seats.stream().sorted().collect(Collectors.toList()));
        System.out.println(plane);
        var planeStorageService = persistenceAPI.getPlaneStorageService(getPlaneManager());
        return planeStorageService.add(plane);
    }

    @Override
    public List<Plane> getAllPlanes(Predicate<Plane> predicate) {
        var all = persistenceAPI.getPlaneStorageService(planeManager).getAll();
        System.out.println(all);
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
        return routeStorageService.add(route)!=null;
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
    public boolean createFlightFromUI(SalesOfficer creator, LocalDateTime dep, LocalDateTime arr, Route route, Plane plane, double price) {
        var flight = getFlightManager().createFlight(creator, dep, arr, route, plane, price);
        System.out.println(flight);

        var flightStorageService = persistenceAPI.getFlightStorageService(getFlightManager());
        return flightStorageService.add(flight)!=null;
    }

    @Override
    public boolean createFlightFromUI(Flight flight) {
        var f = getFlightManager().createFlight(flight.getCreatedBy(), flight.getDeparture(), flight.getArrival(), flight.getRoute(), flight.getPlane(), flight.getPrice());

        var flightStorageService = persistenceAPI.getFlightStorageService(getFlightManager());
        return flightStorageService.add(flight)!=null;
    }

    @Override
    public boolean createReoccurringFlightFromUI(Flight flight, Duration interval) {
        var reOccurFlight = getReoccurringFlightManager().createRoccurringFlight(flight, interval);
        System.out.println(reOccurFlight);

        var flightStorageService = persistenceAPI.getFlightStorageService(getFlightManager());
        if(flightStorageService.remove(flight)) {
            return flightStorageService.add(reOccurFlight)!=null;
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
        return persistenceAPI.getFlightOptionStorageService(getOptionManager()).add(flightOption)!=null;
    }

    @Override
    public boolean addBookingFromUI(Booking booking){
        BookingImpl b = persistenceAPI.getBookingStorageService(getBookingManager()).add(booking);
        return b != null;
    }

    @Override
    public boolean addTicketFromUI(Ticket t){
        TicketImpl tr = persistenceAPI.getTicketStorageService(getTicketManager()).add(t);
        return tr != null ;
    }

    @Override
    public boolean deletePlane(PlaneImpl oldPlane) {
        return persistenceAPI.getPlaneStorageService(getPlaneManager()).delete(oldPlane);
    }

    @Override
    public Plane updatePlane(PlaneImpl oldPlane, String name, String type, String manufacturer, List<Seat> collect) {
        var plane = new PlaneImpl(oldPlane.getId(), name, type, manufacturer);
        plane.addAllSeats(collect);
        return persistenceAPI.getPlaneStorageService(getPlaneManager()).update(plane);
    }

    @Override
    public Flight updateFlight(FlightImpl oldFlight, LocalDateTime dep, LocalDateTime arr, double price, boolean salesprocess) {
        var flight = new FlightImpl(oldFlight.getFlightNumber(), dep, arr, price, salesprocess);
        return persistenceAPI.getFlightStorageService(getFlightManager()).update(flight);
    }

    private static boolean validatePassword(String originalPassword, String storedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        String[] parts = storedPassword.split(":");
        int iterations = Integer.parseInt(parts[0]);
        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);

        PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = skf.generateSecret(spec).getEncoded();

        int diff = hash.length ^ testHash.length;
        for(int i = 0; i < hash.length && i < testHash.length; i++)
        {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }

    private static byte[] fromHex(String hex) throws NoSuchAlgorithmException
    {
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i<bytes.length ;i++)
        {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }
    private static String generateStrongPasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }

    private static byte[] getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }
    private static String toHex(byte[] array) throws NoSuchAlgorithmException
    {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
        {
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        }else{
            return hex;
        }
    }
    public String genPWHash(String pw){
        try {
            return generateStrongPasswordHash(pw);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return pw;
    }
}
