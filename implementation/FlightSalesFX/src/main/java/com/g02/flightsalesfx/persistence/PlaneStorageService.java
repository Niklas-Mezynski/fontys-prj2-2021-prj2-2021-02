package com.g02.flightsalesfx.persistence;

import com.g02.flightsalesfx.businessEntities.Plane;

import java.util.List;

public interface PlaneStorageService {

    public boolean add(Plane plane);

    public List<Plane> getAll();

}
