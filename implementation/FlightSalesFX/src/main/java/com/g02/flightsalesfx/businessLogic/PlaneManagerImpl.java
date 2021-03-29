package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.businessEntities.Plane;
import com.g02.flightsalesfx.businessEntities.PlaneManager;
import com.g02.flightsalesfx.persistence.PlaneStorageService;

public class PlaneManagerImpl implements PlaneManager {

    private PlaneStorageService planeStorageService;

    @Override
    public Plane createPlane(String name, String manufacturer, String type) {
        return new PlaneImpl(name, manufacturer, type);
    }

    public void setPlaneStorageService(PlaneStorageService planeStorageService) {
        this.planeStorageService = planeStorageService;
    }
}
