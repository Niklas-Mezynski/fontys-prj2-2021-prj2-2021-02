package org.g02.flightsalesfx.businessLogic;

import org.g02.flightsalesfx.businessEntities.FlightOption;
import org.g02.flightsalesfx.businessEntities.SeatOption;
import org.g02.flightsalesfx.businessEntities.OptionManager;
import org.g02.flightsalesfx.persistence.FlightOptionStorageService;
import org.g02.flightsalesfx.persistence.SeatOptionsStorageService;


public class OptionManagerImpl implements OptionManager {

    private SeatOptionsStorageService seatOptionStorageManager;
    private FlightOptionStorageService flightOptionStorageService;

    @Override
    public SeatOption createSeatOption(String name, double price) {
        return new SeatOptionImpl(name, price);
    }

    @Override
    public FlightOption createFlightOption(String name, int maxAvailable, double price) {
        return new FlightOptionImpl(name, maxAvailable, price);
    }

    public void setSeatOptionStorageService(SeatOptionsStorageService planeStorageService) {
        this.seatOptionStorageManager = planeStorageService;
    }

    public void setFlightOptionStorageService(FlightOptionStorageService flightOptionStorageService) {
        this.flightOptionStorageService = flightOptionStorageService;
    }
}

