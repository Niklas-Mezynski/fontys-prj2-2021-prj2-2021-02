package com.g02.flightsalesfx.persistence;

import com.g02.flightsalesfx.businessEntities.Airport;

import java.util.List;

public interface AirportStorageService {
    public Airport add (Airport airport);

    public List<Airport> getAll();
}
