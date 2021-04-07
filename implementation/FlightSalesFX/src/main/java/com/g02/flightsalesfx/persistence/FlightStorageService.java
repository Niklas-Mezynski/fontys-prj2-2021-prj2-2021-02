package com.g02.flightsalesfx.persistence;

import com.g02.flightsalesfx.businessEntities.Flight;

import java.util.List;

public interface FlightStorageService {

    public boolean add(Flight flight);

    public List<Flight> getAll();
}
