package org.g02.flightsalesfx.businessLogic;

import org.g02.flightsalesfx.businessEntities.Airport;
import org.g02.flightsalesfx.businessEntities.Route;
import org.g02.flightsalesfx.businessEntities.RouteManager;
import org.g02.flightsalesfx.persistence.RouteStorageService;

public class RouteManagerImpl implements RouteManager {

    private RouteStorageService routeStorageService;

    public void setRouteStorageService(RouteStorageService routeStorageService) {
        this.routeStorageService = routeStorageService;
    }

    @Override
    public Route createRoute(Airport departure, Airport arrival) {
        return new RouteImpl(departure, arrival);
    }

    @Override
    public void editRoute(Route toEdit, Airport newDep, Airport newArr) {
        if(newDep != null){
            toEdit.setDepartureAirport(newDep);
        }
        if(newArr != null){
            toEdit.setArrivalAirport(newArr);
        }

    }


}
