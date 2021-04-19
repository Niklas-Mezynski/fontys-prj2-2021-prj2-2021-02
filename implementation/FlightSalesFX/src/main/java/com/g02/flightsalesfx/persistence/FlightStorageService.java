package com.g02.flightsalesfx.persistence;

import com.g02.flightsalesfx.businessEntities.Flight;

import java.util.List;

public interface FlightStorageService {

    public Flight add(Flight flight);

    public List<Flight> getAll();

    public Flight remove(Flight flight);

}
