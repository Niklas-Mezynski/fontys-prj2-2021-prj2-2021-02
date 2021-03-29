package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.businessEntities.SeatOption;
import com.g02.flightsalesfx.businessEntities.OptionManager;
import com.g02.flightsalesfx.persistence.PlaneStorageService;
import com.g02.flightsalesfx.persistence.SeatOptionsStorageService;


public class OptionManagerImpl implements OptionManager {

    private SeatOptionsStorageService seatOptionStorageManager;

    @Override
    public SeatOption createSeatOption(String name) {
        return new SeatOptionImpl(name);
    }

    public void setSeatOptionStorageService(SeatOptionsStorageService planeStorageService) {
        this.seatOptionStorageManager = planeStorageService;
    }
}

