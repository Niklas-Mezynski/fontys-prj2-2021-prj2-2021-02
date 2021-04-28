package org.g02.flightsalesfx.persistence;

import org.g02.flightsalesfx.businessEntities.Flight;

import java.util.List;

public interface FlightStorageService {

    public Flight add(Flight flight);

    public List<Flight> getAll();

    public boolean remove(Flight flight);

}
