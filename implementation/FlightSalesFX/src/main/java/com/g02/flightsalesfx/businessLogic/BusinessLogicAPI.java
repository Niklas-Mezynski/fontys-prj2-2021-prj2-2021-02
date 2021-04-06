package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.CreatePlaneController;
import com.g02.flightsalesfx.businessEntities.*;

import java.util.List;
import java.util.function.Predicate;

public interface BusinessLogicAPI {

    public EmployeeManager getEmployeeManager();

    public PlaneManager getPlaneManager();

    public SeatManager getSeatManager();

    public OptionManager getOptionManager();

    public AirportManager getAirportManager();

    public RouteManager getRouteManager();

    public Employee login(String email, String password);

    public boolean createPlaneFromUI(String name, String type, String manufacturer, List<Seat> seats);

    public List<Plane> getAllPlanes(Predicate<Plane> predicate);

    void viewPlane(Plane plane);

    public List<Airport> getAllAirports(Predicate<Airport> predicate);

    public List<Route> getAllRoutes(Predicate<Route> predicate);
}
