package com.g02.flightsalesfx.persistence;

import com.g02.flightsalesfx.businessEntities.FlightOption;
import com.g02.flightsalesfx.businessEntities.OptionManager;

import java.util.ArrayList;
import java.util.List;

public class FlightOptionStorageServiceImpl implements FlightOptionStorageService {
    private OptionManager optionManager;
    private List<FlightOption> flightOptions;

    public FlightOptionStorageServiceImpl (OptionManager optionManager) {
        this.optionManager = optionManager;
        this.flightOptions = new ArrayList<>();
    }

    @Override
    public boolean add(FlightOption flightOption) {
        this.flightOptions.add(flightOption);
        return true;
    }

    @Override
    public List<FlightOption> getAll() {
        return flightOptions;
    }
}
