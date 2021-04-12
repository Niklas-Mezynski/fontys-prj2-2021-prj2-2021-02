package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.businessEntities.Flight;
import com.g02.flightsalesfx.businessEntities.ReoccurringFlight;
import com.g02.flightsalesfx.businessEntities.ReoccurringFlightManager;

public class ReoccurringFlightManagerImpl implements ReoccurringFlightManager {

    @Override
    public ReoccurringFlight createRoccurringFlight(Flight flight, int interval) {
        return new ReoccurringFlightImpl(flight, interval);
    }
}
