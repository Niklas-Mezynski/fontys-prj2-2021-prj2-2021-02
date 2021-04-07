package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.businessEntities.*;
import com.g02.flightsalesfx.persistence.FlightStorageService;

import java.time.LocalDateTime;

public class FlightManagerImpl implements FlightManager {

    private FlightStorageService flightStorageService;

    @Override
    public Flight createFlight(SalesOfficer creator, int fNumber, LocalDateTime dep, LocalDateTime arr, Route route, Plane plane, double price) {
        return new FlightImpl(creator, fNumber, dep, arr, route, plane, price);
    }

    public void setFlightStorageService(FlightStorageService flightStorage) {
        this.flightStorageService = flightStorage;
    }
}
