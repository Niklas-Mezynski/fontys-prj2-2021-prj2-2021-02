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
        airports.add(new AirportImpl("DUS", "Düsseldorf", "Germany"));
        airports.add(new AirportImpl("BER", "Berlin", "Germany"));
        airports.add(new AirportImpl("FRA", "Frankfurt", "Germany"));
        airports.add(new AirportImpl("FRA", "Frankfurt", "Germany"));
        airports.add(new AirportImpl("FRA", "Frankfurt", "Germany"));
        airports.add(new AirportImpl("FRA", "Frankfurt", "Germany"));
        airports.add(new AirportImpl("FRA", "Frankfurt", "Germany"));
        airports.add(new AirportImpl("FRA", "Frankfurt", "Germany"));
        airports.add(new AirportImpl("FRA", "Frankfurt", "Germany"));
        airports.add(new AirportImpl("FRA", "Frankfurt", "Germany"));
        airports.add(new AirportImpl("FRA", "Frankfurt", "Germany"));
        airports.add(new AirportImpl("FRA", "Frankfurt", "Germany"));
        airports.add(new AirportImpl("FRA", "Frankfurt", "Germany"));
        airports.add(new AirportImpl("FRA", "Frankfurt", "Germany"));
        airports.add(new AirportImpl("FRA", "Frankfurt", "Germany"));
        airports.add(new AirportImpl("FRA", "Frankfurt", "Germany"));
        airports.add(new AirportImpl("FRA", "Frankfurt", "Germany"));
        airports.add(new AirportImpl("FRA", "Frankfurt", "Germany"));
        airports.add(new AirportImpl("FRA", "Frankfurt", "Germany"));
        airports.add(new AirportImpl("FRA", "Frankfurt", "Germany"));
        airports.add(new AirportImpl("FRA", "Frankfurt", "Germany"));
        airports.add(new AirportImpl("FRA", "Frankfurt", "Germany"));
        airports.add(new AirportImpl("FRA", "Frankfurt", "Germany"));
        airports.add(new AirportImpl("FRA", "Frankfurt", "Germany"));
        airports.add(new AirportImpl("FRA", "Frankfurt", "Germany"));
        airports.add(new AirportImpl("FRA", "Frankfurt", "Germany"));
        airports.add(new AirportImpl("FRA", "Frankfurt", "Germany"));
        airports.add(new AirportImpl("FRA", "Frankfurt", "Germany"));
        airports.add(new AirportImpl("FRA", "Frankfurt", "Germany"));
        airports.add(new AirportImpl("FRA", "Frankfurt", "Germany"));
        airports.add(new AirportImpl("FRA", "Frankfurt", "Germany"));
        airports.add(new AirportImpl("FRA", "Frankfurt", "Germany"));
        airports.add(new AirportImpl("FRA", "Frankfurt", "Germany"));
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
