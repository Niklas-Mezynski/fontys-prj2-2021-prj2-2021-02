package com.g02.flightsalesfx.persistence;

import com.g02.flightsalesfx.businessEntities.Flight;

import java.util.List;

public interface FlightStorageService {

    public Flight add(Flight flight);

    public List<Flight> getAll();

    public boolean remove(Flight flight);

    public Flight update(Flight flight);

}
