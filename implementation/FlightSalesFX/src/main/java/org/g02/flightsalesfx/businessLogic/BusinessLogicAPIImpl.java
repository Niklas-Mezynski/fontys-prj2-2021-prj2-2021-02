package org.g02.flightsalesfx.businessLogic;

import org.g02.flightsalesfx.CreatePlaneController;
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
import java.util.*;
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

    private static boolean validatePassword(String originalPassword, String storedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String[] parts = storedPassword.split(":");
        int iterations = Integer.parseInt(parts[0]);
        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);

        PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = skf.generateSecret(spec).getEncoded();

        int diff = hash.length ^ testHash.length;
        for (int i = 0; i < hash.length && i < testHash.length; i++) {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }


    private static String generateStrongPasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
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
        if (ticketManager == null) {
            ticketManager = new TicketManagerImpl();
            ticketManager.setTicketStorageService(persistenceAPI.getTicketStorageService(ticketManager));
        }
        return ticketManager;
    }

    @Override
    public BookingManager getBookingManager() {
        if (bookingManager == null) {
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
        if (airportManager == null) {
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
        if (priceReductionManager == null) {
            priceReductionManager = new PriceReductionManagerImpl();
            priceReductionManager.setPriceReductionStorageService(persistenceAPI.getPriceReductionStorageService(priceReductionManager));
        }
        return priceReductionManager;
    }

    @Override
    public FlightManager getFlightManager() {
        if (flightManager == null) {
            flightManager = new FlightManagerImpl();
            flightManager.setFlightStorageService(persistenceAPI.getFlightStorageService(flightManager));
        }
        return flightManager;
    }

    @Override
    public ReoccurringFlightManager getReoccurringFlightManager() {
        //todo: review to verify consistency
        if (reoccurringFlightManager == null) {
            reoccurringFlightManager = new ReoccurringFlightManagerImpl();
            reoccurringFlightManager.setReoccurringFlightStorageService(flightManager);
        }
        return reoccurringFlightManager;
    }

    @Override
    public Employee login(String email, String password) {
        var employeeStorageService = persistenceAPI.getEmployeeStorageService(getEmployeeManager());
        var first = employeeStorageService.get(email);
        if (first.isEmpty()) {
            var examplehash = "1000:61a54968a3c8f1eb1dc1173c64f2e6d9:3ce71ef9a8359b266ce8135b4a753ce5d3411a5ee151d315312943f104c35339d190d9aa3328ab462661a339bc597dfffd0e14043b60e0bcffdbe261f1dc2ddc";
            try {
                boolean matched = validatePassword(password, examplehash); //This may seem useless, but this prevents a side-channel attack
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                e.printStackTrace();
            }
            return null;
        }
        var employee = first.get();

        //Check hashed PW in DB against entered one
        try {
            boolean matched = validatePassword(password, employee.getPassword());
            return matched ? employee : null;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Plane createPlane(String name, String type, String manufacturer, List<Seat> seats) {
        var planeManager = getPlaneManager();
        var plane = planeManager.create(name, manufacturer, type);
        plane.addAllSeats(seats);
        return planeManager.add(plane);
    }

    @Override
    public Plane createPlaneFromUI(String name, String type, String manufacturer, List<CreatePlaneController.SeatButton> seats) {
        return createPlane(name, type, manufacturer, convertSeatButtonToSeat(seats));
    }

    @Override
    public List<Plane> getAllPlanes(Predicate<Plane> predicate) {
        var planeManager = getPlaneManager();
        var all = planeManager.getAll();
        System.out.println(all);
        var planeStream = all.stream().filter(predicate);
        return planeStream.collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<Plane> getAllPlanes() {
        return getAllPlanes(s->true);
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
        return routeStorageService.add(route) != null;
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
    public boolean createFlightFromUI(SalesOfficer creator, LocalDateTime dep, LocalDateTime arr, Route route, Plane plane, double price, List<? extends FlightOption> flightOptions) {
        var flight = getFlightManager().createFlight(creator, dep, arr, route, plane, price);
        flight.addAllFlightOptions(flightOptions);

        System.out.println(flight);

        var flightStorageService = persistenceAPI.getFlightStorageService(getFlightManager());
        return flightStorageService.add(flight) != null;
    }

    @Override
    public boolean createFlightFromUI(Flight flight) {
        var f = getFlightManager().createFlight(flight.getCreatedBy(), flight.getDeparture(), flight.getArrival(), flight.getRoute(), flight.getPlane(), flight.getPrice());
        if (flight.getSalesProcessStatus()) {
            f.startSalesProcess();
        }

        var flightStorageService = persistenceAPI.getFlightStorageService(getFlightManager());
        return flightStorageService.add(flight) != null;
    }



    @Override
    public boolean createPriceReductionFromUI(PriceReduction reduction) {
        System.out.println(reduction);
        var reductionStorageService = persistenceAPI.getPriceReductionStorageService(getPriceReductionManager());
        return reductionStorageService.add(reduction)!=null;
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
    public void createAirportFromUI(String name, String city, String country) {
        var airport = getAirportManager().createAirport(name, city, country);
        persistenceAPI.getAirportStorageService(getAirportManager()).add(airport);
    }

    @Override
    public List<Flight> getAllFlights(Predicate<Flight> predicate) {
        var all = persistenceAPI.getFlightStorageService(flightManager).getAll();
        return all.stream().filter(predicate).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<Flight> getAllFlights() {
        return getAllFlights(a->true);
    }


    @Override
    public List<PriceReduction> getAllPriceReductions(){
        return getAllPriceReductions(a->true);
    }

    @Override
    public List<PriceReduction> getAllPriceReductions(Predicate<PriceReduction> predicate){
        var all = persistenceAPI.getPriceReductionStorageService(priceReductionManager).getAll();
        return all.stream().filter(predicate).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public boolean addFlightOptionFromUI(String name, int maxAvailable, double price, Flight flight) {
        var flightOption = getOptionManager().createFlightOption(name, maxAvailable, price);
        flight.addFlightOption(flightOption);
        return persistenceAPI.getFlightStorageService(getFlightManager()).update(flight) != null;
    }

    @Override
    public boolean addBookingFromUI(Booking booking) {
        BookingImpl b = persistenceAPI.getBookingStorageService(getBookingManager()).add(booking);
        return b != null;
    }

    @Override
    public boolean addTicketFromUI(Ticket t) {
        TicketImpl tr = persistenceAPI.getTicketStorageService(getTicketManager()).add(t);
        return tr != null;
    }

    @Override
    public boolean deletePlane(Plane oldPlane) {
        return persistenceAPI.getPlaneStorageService(getPlaneManager()).delete(oldPlane);
    }

    private List<Seat> convertSeatButtonToSeat(List<CreatePlaneController.SeatButton> seats) {
        return seats.stream()
                .map(s -> {
                    var ret = new SeatImpl(s.row(), s.column());
                    ret.addAllSeatOptions(s.options.stream().map(CreatePlaneController.SeatOptionBox::getSeatOption).collect(Collectors.toList()));
                    return (Seat) ret;
                }).sorted().collect(Collectors.toList());
    }

    @Override
    public Plane updatePlane(Plane oldPlane, String name, String type, String manufacturer, List<Seat> collect) {
        var plane = new PlaneImpl(oldPlane.getId(), name, type, manufacturer);
        plane.addAllSeats(collect);
        return getPlaneManager().update(plane);
    }

    @Override
    public Plane updatePlaneFromUI(Plane oldPlane, String name, String type, String manufacturer, List<CreatePlaneController.SeatButton> collect) {
        return updatePlane(oldPlane, name, type, manufacturer, convertSeatButtonToSeat(collect));
    }

    @Override
    public Flight updateFlight(FlightImpl oldFlight, LocalDateTime dep, LocalDateTime arr, double price, boolean salesprocess) {
        var flightImpl = oldFlight;
        System.out.println(flightImpl.getFlightNumber());
        if (salesprocess) {
            flightImpl.startSalesProcess();
        } else {
            flightImpl.stopSalesProcess();
        }
        return persistenceAPI.getFlightStorageService(getFlightManager()).update(flightImpl);
    }

    @Override
    public List<Employee> getAllEmployees(Predicate<Employee> predicate) {
        var all = persistenceAPI.getEmployeeStorageService(employeeManager).getAll();
        return all.stream().filter(predicate).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public double sumRevenue(List<Booking> list, Predicate<Booking> predicate) {
        return list.stream()
                .filter(predicate)
                .mapToDouble(Booking::getBookingPrice)
                .sum();
    }

    @Override
    public double totalRevenueByEmp(SalesEmployee se) {
        return sumRevenue(getAllBookings(booking -> booking.getSalesEmployee().equals(se)), booking -> true);
    }

    @Override
    public int totalNumOfBookingsByAnEmployee(SalesEmployee se) {
        return getAllBookings(booking -> booking.getSalesEmployee().equals(se)).size();
    }

    @Override
    public double avgNumOfTicketsPerBooking(SalesEmployee se) {
        OptionalDouble average = getAllBookings(booking -> booking.getSalesEmployee().equals(se)).stream()
                .mapToInt(booking -> booking.getTickets().size())
                .average();
        return average.orElse(0);
    }

    @Override
    public LinkedHashMap<LocalDateTime, Double> getMonthlyRevenue(SalesEmployee se, LocalDateTime startDate) {
        LinkedHashMap<LocalDateTime, Double> map = new LinkedHashMap<>();
        List<Booking> allBookingsByEmp = getAllBookings(booking -> booking.getSalesEmployee().equals(se));
        while (startDate.isBefore(LocalDateTime.now().minusDays(1))) {

            LocalDateTime currentDate = startDate;
            Predicate<Booking> pred = (booking -> booking.getBookingDate().isAfter(currentDate) && booking.getBookingDate().isBefore(currentDate.plusMonths(1)));
            double revenueThisMonth = sumRevenue(allBookingsByEmp, pred);
            map.put(startDate, revenueThisMonth);
            startDate = startDate.plusMonths(1);
        }
        return map;
    }

    @Override
    public LinkedHashMap<LocalDateTime, Double> getAvgMonthlyRevenues(LocalDateTime startDate) {
        LinkedHashMap<LocalDateTime, Double> map = new LinkedHashMap<>();
        List<SalesEmployee> allEmployees = getAllEmployees(employee -> employee instanceof SalesEmployee).stream()
                .map(employee -> (SalesEmployee) employee).collect(Collectors.toList());
        List<Booking> allBookings = getAllBookings(booking -> true);

        while (startDate.isBefore(LocalDateTime.now().minusDays(1))) {
            List<Double> revenueByEachEmpThisMonth = new ArrayList<>();
            for (Employee emp : allEmployees) {
                List<Booking> allBookingsByEmp = allBookings.stream().filter(booking -> booking.getSalesEmployee().equals(emp)).collect(Collectors.toList());
                LocalDateTime currentDate = startDate;
                Predicate<Booking> pred = (booking -> booking.getBookingDate().isAfter(currentDate) && booking.getBookingDate().isBefore(currentDate.plusMonths(1)));
                double revenueThisMonth = sumRevenue(allBookingsByEmp, pred);
                revenueByEachEmpThisMonth.add(revenueThisMonth);
            }
            OptionalDouble average = revenueByEachEmpThisMonth.stream().mapToDouble(Double::doubleValue).average();
            map.put(startDate, average.orElse(0));
            startDate = startDate.plusMonths(1);
        }
        return map;
    }

    @Override
    public double totalRevenueByRoute(Route route) {
        return sumRevenue(getAllBookings(booking -> booking.getFlight().getRoute().equals(route)), booking -> true);
    }

    @Override
    public double sumOfAClassesRevenue(Route route, String className) {
        return getAllBookings(booking -> booking.getFlight().getRoute().equals(route)).stream()
                .flatMap(booking -> booking.getTickets().stream())
                .flatMap(ticket -> Arrays.stream(ticket.getSeatOptions()))
                .filter(seatOption -> seatOption.getName().toLowerCase().contains(className.toLowerCase()))
                .mapToDouble(Option::getPrice)
                .sum();
    }

    @Override
    public Optional<Booking> getFirstBookingOfARoute(Route route) {
        return getAllBookings(booking -> booking.getFlight().getRoute().equals(route)).stream()
                .min((b1, b2) -> b1.getBookingDate().compareTo(b2.getBookingDate()));
    }

    @Override
    public double getRevenueForSpecificRouteInOneYear(Route route, int year) {
        return getAllBookings(booking -> booking.getFlight().getRoute().equals(route) && booking.getBookingDate().getYear() == year)
                .stream()
                .mapToDouble(Booking::getBookingPrice)
                .sum();
    }


    private static byte[] fromHex(String hex) throws NoSuchAlgorithmException {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    private static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    private static String toHex(byte[] array) throws NoSuchAlgorithmException {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }

    public String genPWHash(String pw) {
        try {
            return generateStrongPasswordHash(pw);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return pw;
    }
}
