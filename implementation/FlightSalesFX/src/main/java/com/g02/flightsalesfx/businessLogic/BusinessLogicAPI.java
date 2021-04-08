package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.CreatePlaneController;
import com.g02.flightsalesfx.businessEntities.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;

public interface BusinessLogicAPI {

    public EmployeeManager getEmployeeManager();

    public PlaneManager getPlaneManager();

    public SeatManager getSeatManager();

    public OptionManager getOptionManager();

    public AirportManager getAirportManager();

    public RouteManager getRouteManager();

    public PriceReductionManager getPriceReductionManager();

    public FlightManager getFlightManager();

    public Employee login(String email, String password);

    public boolean createPlaneFromUI(String name, String type, String manufacturer, List<Seat> seats);

    public boolean createRouteFromUI(Airport departure, Airport arrival);

    //todo: createFlightFromUI and add params
    //public boolean createFlightFromUI();

    public List<Plane> getAllPlanes(Predicate<Plane> predicate);

    public List<Route> getAllRoutes(Predicate<Route> predicate);

    void viewPlane(Plane plane);

    public List<Airport> getAllAirports(Predicate<Airport> predicate);

    public boolean createFlightFromUI(SalesOfficer creator, int fNumber, LocalDateTime dep, LocalDateTime arr, Route route, Plane plane);

}
