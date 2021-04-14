package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.businessEntities.Flight;
import com.g02.flightsalesfx.businessEntities.FlightManager;
import com.g02.flightsalesfx.businessEntities.ReoccurringFlight;
import com.g02.flightsalesfx.businessEntities.ReoccurringFlightManager;
import com.g02.flightsalesfx.persistence.FlightStorageService;

public class ReoccurringFlightManagerImpl implements ReoccurringFlightManager {

    private FlightStorageService reoccurringFlightStorageService;

    @Override
    public ReoccurringFlight createRoccurringFlight(Flight flight, int interval) {
        return new ReoccurringFlightImpl(flight, interval);
    }

    public void setReoccurringFlightStorageService(FlightManagerImpl flightManager) {
        this.reoccurringFlightStorageService = flightManager.getFlightStorageService();
    }
}
