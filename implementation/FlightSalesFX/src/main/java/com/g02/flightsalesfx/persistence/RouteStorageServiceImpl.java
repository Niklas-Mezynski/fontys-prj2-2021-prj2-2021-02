package com.g02.flightsalesfx.persistence;

import com.g02.btfdao.dao.Dao;
import com.g02.btfdao.dao.Savable;
import com.g02.flightsalesfx.businessEntities.Route;
import com.g02.flightsalesfx.businessEntities.RouteManager;
import com.g02.flightsalesfx.businessLogic.AirportImpl;
import com.g02.flightsalesfx.businessLogic.RouteImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RouteStorageServiceImpl implements RouteStorageService {
    private final RouteManager rm;

    private final Dao<RouteImpl> dao;

    public RouteStorageServiceImpl(RouteManager om, Dao<RouteImpl> dao) {
        this.rm = om;
        this.dao = dao;
    }

    @Override
    public Route add(Route route) {
        var routeImpl=new RouteImpl(AirportImpl.of(route.getDepartureAirport()), AirportImpl.of(route.getArrivalAirport()));
        try {
            var ret = dao.insert(routeImpl);
            return ret.isPresent()? ret.get() : null;
        } catch ( SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Route> getAll() {
        var ret = new ArrayList<Route>();
        try {
            ret.addAll(dao.getAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
