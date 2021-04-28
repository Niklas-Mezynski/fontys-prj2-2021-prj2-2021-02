package org.g02.flightsalesfx.businessLogic;

import org.g02.flightsalesfx.businessEntities.Plane;
import org.g02.flightsalesfx.businessEntities.PlaneManager;
import org.g02.flightsalesfx.persistence.PlaneStorageService;

public class PlaneManagerImpl implements PlaneManager {

    private PlaneStorageService planeStorageService;

    @Override
    public Plane createPlane(String name, String manufacturer, String type) {
        return new PlaneImpl(name, type, manufacturer);
    }

   /* public Plane createPlane(String name, String manufacturer, String type, List<? extends Seat> seatList) {
        Plane newPlane = new PlaneImpl(name, type, manufacturer);
        newPlane.addAllSeats(seatList);
        
        return newPlane;
    }*/

    public void setPlaneStorageService(PlaneStorageService planeStorageService) {
        this.planeStorageService = planeStorageService;
    }
}
