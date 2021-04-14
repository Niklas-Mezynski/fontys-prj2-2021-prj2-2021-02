package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.businessEntities.Flight;
import com.g02.flightsalesfx.businessEntities.FlightOption;

public class FlightOptionImpl implements FlightOption {
    private String name;
    private int maxAvailable;
    private double price;

    public FlightOptionImpl (String name, int maxAvailable, double price) {
        this.name = name;
        this.maxAvailable = maxAvailable;
        this.price = price;
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

    @Override
    public double getPrice() {
        return price;
    }

    /**
     * @return The Name of this Option
     */
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Max available: " + maxAvailable + ", Price: " + price + "€";
    }
}