package com.g02.flightsalesfx.persistence;

import com.g02.flightsalesfx.businessEntities.Plane;
import com.g02.flightsalesfx.businessEntities.PlaneManager;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

public class PlaneStorageServiceImpl implements PlaneStorageService {

    private List<Plane> planes;

    public PlaneStorageServiceImpl(PlaneManager planeManager) {
        planes = new ArrayList<>();
    }

    @Override
    public void add(Plane plane) {
        planes.add( plane );
    }

    @Override
    public List<Plane> getAll() {
        return unmodifiableList(planes);
    }
}
