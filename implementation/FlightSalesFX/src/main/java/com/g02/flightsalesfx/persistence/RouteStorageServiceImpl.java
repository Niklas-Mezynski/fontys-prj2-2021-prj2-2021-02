package com.g02.flightsalesfx.persistence;

import com.g02.flightsalesfx.businessEntities.Airport;
import com.g02.flightsalesfx.businessEntities.Route;
import com.g02.flightsalesfx.businessEntities.RouteManager;
import com.g02.flightsalesfx.businessLogic.AirportImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RouteStorageServiceImpl implements RouteStorageService {
    private final List<Route> routes;

    public RouteStorageServiceImpl(RouteManager om) {
        this.routes = new ArrayList<>();
    }

    @Override
    public boolean add(Route route) {
        routes.add(route);
        return true;
    }

    @Override
    public List<Route> getAll() {
        return Collections.unmodifiableList(routes);
    }
}
