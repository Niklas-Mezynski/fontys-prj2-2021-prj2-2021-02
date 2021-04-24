package com.g02.flightsalesfx.persistence;

import com.g02.flightsalesfx.businessEntities.Plane;
import com.g02.flightsalesfx.businessLogic.PlaneImpl;

import java.util.List;

public interface PlaneStorageService {

    public Plane add(Plane plane);

    public List<Plane> getAll();

    Plane delete(Plane plane);

    Plane update(Plane plane);
}
