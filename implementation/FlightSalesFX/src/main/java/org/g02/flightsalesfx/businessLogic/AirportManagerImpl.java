package org.g02.flightsalesfx.businessLogic;

import org.g02.flightsalesfx.businessEntities.Airport;
import org.g02.flightsalesfx.businessEntities.AirportManager;
import org.g02.flightsalesfx.persistence.AirportStorageService;

public class AirportManagerImpl implements AirportManager {

    private AirportStorageService airportStorageService;

    @Override
    public Airport createAirport(String name, String city, String country) {
        return new AirportImpl(name, city, country);
    }

    public void setAirportStorageService(AirportStorageService airportStorage) {
        this.airportStorageService = airportStorage;
    }
}
