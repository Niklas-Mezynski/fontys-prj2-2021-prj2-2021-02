package com.g02.flightsalesfx.persistence;

import com.g02.flightsalesfx.businessEntities.*;
import com.g02.flightsalesfx.businessLogic.OptionManagerImpl;
import com.g02.flightsalesfx.businessLogic.PlaneManagerImpl;
import com.g02.flightsalesfx.businessLogic.SeatManagerImpl;

public interface PersistenceAPI {

    EmployeeStorageService getEmployeeStorageService(EmployeeManager employeeManager);

    PlaneStorageService getPlaneStorageService(PlaneManager planeManager);

    SeatStorageService getSeatStorageService(SeatManager seatManager);

    SeatOptionsStorageService getSeatOptionStorageService(OptionManager optionManager);

    AirportStorageService getAirportStorageService(AirportManager airportManager);

    RouteStorageService getRouteStorageService(RouteManager routeManager);

    PriceReductionStorageService getPriceReductionStorageService(PriceReductionManager PriceReductionManager);

    FlightStorageService getFlightStorageService(FlightManager flightManager);

}
