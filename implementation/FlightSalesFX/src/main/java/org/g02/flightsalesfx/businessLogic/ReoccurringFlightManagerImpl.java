package org.g02.flightsalesfx.businessLogic;

import org.g02.flightsalesfx.businessEntities.Flight;
import org.g02.flightsalesfx.businessEntities.ReoccurringFlight;
import org.g02.flightsalesfx.businessEntities.ReoccurringFlightManager;
import org.g02.flightsalesfx.persistence.FlightStorageService;

import java.time.Duration;

public class ReoccurringFlightManagerImpl implements ReoccurringFlightManager {

    private FlightStorageService reoccurringFlightStorageService;

    @Override
    public ReoccurringFlight createRoccurringFlight(Flight flight, Duration interval) {
        return new ReoccurringFlightImpl(flight, interval);
    }

    public void setReoccurringFlightStorageService(FlightManagerImpl flightManager) {
        this.reoccurringFlightStorageService = flightManager.getFlightStorageService();
    }
}
