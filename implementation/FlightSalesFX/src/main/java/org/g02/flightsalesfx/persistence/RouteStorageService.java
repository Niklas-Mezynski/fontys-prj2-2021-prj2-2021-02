package org.g02.flightsalesfx.persistence;


import org.g02.flightsalesfx.businessEntities.Route;

import java.util.List;

public interface RouteStorageService {
    Route add(Route route);

    List<Route> getAll();
}
