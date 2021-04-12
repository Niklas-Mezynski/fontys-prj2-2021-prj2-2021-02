package com.g02.flightsalesfx.persistence;

import com.g02.flightsalesfx.businessEntities.FlightOption;

import java.util.List;

public interface FlightOptionStorageService {
    boolean add(FlightOption flightOption);

    List<FlightOption> getAll();
}
