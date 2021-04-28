package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.businessEntities.*;
import com.g02.flightsalesfx.persistence.FlightStorageService;

import java.time.LocalDateTime;

public class FlightManagerImpl implements FlightManager {

    private FlightStorageService flightStorageService;

    @Override
    public Flight createFlight(SalesOfficer creator, LocalDateTime dep, LocalDateTime arr, Route route, Plane plane, double price) {
        if (!(creator instanceof SalesOfficerImpl)) return null;
        return new FlightImpl((SalesOfficerImpl) creator, dep, arr, route, plane, price);
    }

    public void setFlightStorageService(FlightStorageService flightStorage) {
        this.flightStorageService = flightStorage;
    }

    //todo: check if persistence is working with this solutuion
    //Following lines are added to save/work with reocurring flights

    public FlightStorageService getFlightStorageService() {
        return this.flightStorageService;
    }
}
