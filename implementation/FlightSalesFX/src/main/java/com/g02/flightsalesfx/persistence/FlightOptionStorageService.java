package com.g02.flightsalesfx.persistence;

import com.g02.flightsalesfx.businessEntities.FlightOption;

import java.util.List;

public interface FlightOptionStorageService {
    FlightOption add(FlightOption flightOption);

    List<FlightOption> getAll();
}
