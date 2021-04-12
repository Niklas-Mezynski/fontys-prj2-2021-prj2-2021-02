package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.businessEntities.Flight;
import com.g02.flightsalesfx.businessEntities.FlightOption;

public class FlightOptionImpl implements FlightOption {
    private String name;
    private Flight flight;
    private int maxAvailable;

    public FlightOptionImpl (String name, Flight flight, int maxAvailable) {
        this.name = name;
        this.flight = flight;
        this.maxAvailable = maxAvailable;
    }

    /**
     * @return How often this FlightOption is currently available
     */
    @Override
    public int getCurrentAvailability() {
        //TODO (should this be saved or calculated by subtracting the already booked FOs from the maxAvailable)
        return 0;
    }

    /**
     * @return How often this FlightOption was available at the start of the Sales Process
     */
    @Override
    public int getMaxAvailability() {
        return maxAvailable;
    }

    /**
     * @return The Flight that this FlightOption belongs to
     */
    @Override
    public Flight getFlight() {
        return flight;
    }

    /**
     * @return The Name of this Option
     */
    @Override
    public String getName() {
        return name;
    }
}
