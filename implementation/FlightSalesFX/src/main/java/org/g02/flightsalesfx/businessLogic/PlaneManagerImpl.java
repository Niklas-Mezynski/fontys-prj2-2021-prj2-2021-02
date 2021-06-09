package org.g02.flightsalesfx.businessLogic;

import org.g02.flightsalesfx.businessEntities.Plane;
import org.g02.flightsalesfx.businessEntities.PlaneManager;
import org.g02.flightsalesfx.businessEntities.Seat;
import org.g02.flightsalesfx.persistence.PlaneStorageService;

import java.util.List;

public class PlaneManagerImpl implements PlaneManager {

    private PlaneStorageService planeStorageService;

    @Override
    public Plane create(String name, String manufacturer, String type) {
        return new PlaneImpl(name, type, manufacturer);
    }

    @Override
    public Plane create(String name, String manufacturer, String type, List<Seat> seats) {
        var plane = create(name, manufacturer, type);
        plane.addAllSeats(seats);
        return plane;
    }

    @Override
    public List<Plane> getAll() {
        return planeStorageService.getAll();
    }

    @Override
    public Plane add(Plane plane) {
        return planeStorageService.add(plane);
    }

    @Override
    public Plane update(Plane plane) {
        return planeStorageService.update(plane);
    }

    public void setPlaneStorageService(PlaneStorageService planeStorageService) {
        this.planeStorageService = planeStorageService;
    }
}
