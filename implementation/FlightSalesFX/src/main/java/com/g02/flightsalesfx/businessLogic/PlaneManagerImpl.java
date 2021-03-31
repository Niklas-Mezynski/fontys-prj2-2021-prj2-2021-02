package com.g02.flightsalesfx.businessLogic;

import java.util.List;

import com.g02.flightsalesfx.businessEntities.Plane;
import com.g02.flightsalesfx.businessEntities.PlaneManager;
import com.g02.flightsalesfx.businessEntities.Seat;
import com.g02.flightsalesfx.persistence.PlaneStorageService;

public class PlaneManagerImpl implements PlaneManager {

    private PlaneStorageService planeStorageService;

    @Override
    public Plane createPlane(String name, String manufacturer, String type) {
        return new PlaneImpl(name, manufacturer, type);
    }

    public Plane createPlane(String name, String manufacturer, String type, List<? extends Seat> seatList) {
        Plane newPlane = new PlaneImpl(name, manufacturer, type);
        newPlane.addAllSeats(seatList);
        
        return newPlane;
    }

    public void setPlaneStorageService(PlaneStorageService planeStorageService) {
        this.planeStorageService = planeStorageService;
    }
}
