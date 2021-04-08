package com.g02.flightsalesfx.persistence;

import com.g02.flightsalesfx.businessEntities.Airport;
import com.g02.flightsalesfx.businessEntities.AirportManager;
import com.g02.flightsalesfx.businessLogic.AirportImpl;

import java.util.ArrayList;
import java.util.List;

public class AirportStorageServiceImpl implements AirportStorageService {

    private final List<Airport> airports;

    public AirportStorageServiceImpl(AirportManager airportManager) {
        airports = new ArrayList<>();
    }


    @Override
    public void add(Airport airport) {
        airports.add(airport);
    }

    @Override
    public List<Airport> getAll() {
        return airports;
    }
}
