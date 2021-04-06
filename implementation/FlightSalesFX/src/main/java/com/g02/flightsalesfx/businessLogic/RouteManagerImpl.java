package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.businessEntities.Airport;
import com.g02.flightsalesfx.businessEntities.Route;
import com.g02.flightsalesfx.businessEntities.RouteManager;
import com.g02.flightsalesfx.persistence.RouteStorageService;

public class RouteManagerImpl implements RouteManager {

    private RouteStorageService routeStorageService;

    public void setRouteStorageService(RouteStorageService routeStorageService) {
        this.routeStorageService = routeStorageService;
    }

    @Override
    public Route createRoute(Airport departure, Airport arrival) {
        return new RouteImpl(departure, arrival);
    }
}
