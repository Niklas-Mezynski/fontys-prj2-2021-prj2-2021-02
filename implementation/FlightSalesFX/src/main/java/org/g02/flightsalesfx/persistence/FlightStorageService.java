package org.g02.flightsalesfx.persistence;

import org.g02.flightsalesfx.businessEntities.Flight;
import org.g02.flightsalesfx.businessLogic.FlightImpl;

import java.util.List;

public interface FlightStorageService {

    public Flight add(Flight flight);

    public List<Flight> getAll();

    public boolean remove(Flight flight);

    public Flight update(Flight flight);
    public Flight update(FlightImpl flight);

}
