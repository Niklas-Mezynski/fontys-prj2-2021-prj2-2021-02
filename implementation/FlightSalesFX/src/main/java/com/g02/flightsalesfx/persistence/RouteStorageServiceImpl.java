package com.g02.flightsalesfx.persistence;

import com.g02.flightsalesfx.businessEntities.Route;
import com.g02.flightsalesfx.businessEntities.RouteManager;

import java.util.ArrayList;
import java.util.List;

public class RouteStorageServiceImpl implements RouteStorageService {
    private final List<Route> routes;

    public RouteStorageServiceImpl(RouteManager om) {
        this.routes = new ArrayList<>();
    }

    @Override
    public void add(Route route) {
        routes.add(route);
    }

    @Override
    public List<Route> getAll() {
        return routes;
    }
}