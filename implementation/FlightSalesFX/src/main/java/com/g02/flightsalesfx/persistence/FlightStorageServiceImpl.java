package com.g02.flightsalesfx.persistence;

import com.g02.flightsalesfx.businessEntities.Flight;
import com.g02.flightsalesfx.businessEntities.FlightManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FlightStorageServiceImpl implements FlightStorageService{

    private List<Flight> flightList;

    public FlightStorageServiceImpl(FlightManager flightManager) {
        flightList = new ArrayList<>();
    }

    @Override
    public boolean add(Flight flight) {
        flightList.add(flight);
        return true;
    }

    @Override
    public List<Flight> getAll() {
        return Collections.unmodifiableList(flightList);
    }
}
