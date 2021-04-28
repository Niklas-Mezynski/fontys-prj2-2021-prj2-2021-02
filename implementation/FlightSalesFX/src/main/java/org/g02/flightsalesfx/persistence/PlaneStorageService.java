package org.g02.flightsalesfx.persistence;

import org.g02.flightsalesfx.businessEntities.Plane;

import java.util.List;

public interface PlaneStorageService {

    public Plane add(Plane plane);

    public List<Plane> getAll();

    boolean delete(Plane plane);

    Plane update(Plane plane);
}
