package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.CreatePlaneController;
import com.g02.flightsalesfx.businessEntities.*;
import com.g02.flightsalesfx.persistence.PersistenceAPI;

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

    public BusinessLogicAPIImpl(PersistenceAPI persistenceAPI) {
        this.persistenceAPI = persistenceAPI;
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

}
