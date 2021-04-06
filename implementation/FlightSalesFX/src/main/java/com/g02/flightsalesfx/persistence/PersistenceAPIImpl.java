package com.g02.flightsalesfx.persistence;

import com.g02.flightsalesfx.businessEntities.*;
import com.g02.flightsalesfx.businessLogic.OptionManagerImpl;
import com.g02.flightsalesfx.businessLogic.SeatManagerImpl;

public class PersistenceAPIImpl implements PersistenceAPI, PersistenceApiImplementationProvider {

    private EmployeeStorageServiceImpl employeeStorageService;
    private PlaneStorageService planeStorageService;
    private SeatStorageService seatStorageService;
    private SeatOptionsStorageServiceImpl seatOptionStorageService;
    private AirportStorageService airportStorageService;
    private RouteStorageService routeStorageService;

    @Override
    public EmployeeStorageService getEmployeeStorageService(EmployeeManager employeeManager) {
        if (employeeStorageService == null)
            employeeStorageService = new EmployeeStorageServiceImpl(employeeManager);
        return employeeStorageService;
    }

    @Override
    public PlaneStorageService getPlaneStorageService(PlaneManager planeManager) {
        if (planeStorageService == null) {
            planeStorageService = new PlaneStorageServiceImpl(planeManager);
        }
        return planeStorageService;
    }

    @Override
    public SeatStorageService getSeatStorageService(SeatManager seatManager) {
        if (seatStorageService == null) {
            seatStorageService = new SeatStorageServiceImpl(seatManager);
        }
        return seatStorageService;
    }

    @Override
    public SeatOptionsStorageService getSeatOptionStorageService(OptionManager optionManager) {
        if (seatOptionStorageService == null) {
            seatOptionStorageService = new SeatOptionsStorageServiceImpl(optionManager);
        }
        return seatOptionStorageService;
    }

    @Override
    public AirportStorageService getAirportStorageService(AirportManager airportManager) {
        if(airportStorageService == null) {
            airportStorageService = new AirportStorageServiceImpl(airportManager);
        }

        return airportStorageService;
    }

    @Override
    public RouteStorageService getRouteStorageService(RouteManager routeManager) {
        if (routeStorageService == null) {
            routeStorageService = new RouteStorageServiceImpl(routeManager);
        }
        return routeStorageService;
    }
}
