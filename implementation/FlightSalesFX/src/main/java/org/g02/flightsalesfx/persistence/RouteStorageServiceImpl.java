package org.g02.flightsalesfx.persistence;

import org.g02.btfdao.dao.Dao;
import org.g02.flightsalesfx.businessEntities.Route;
import org.g02.flightsalesfx.businessEntities.RouteManager;
import org.g02.flightsalesfx.businessLogic.AirportImpl;
import org.g02.flightsalesfx.businessLogic.RouteImpl;

import java.sql.SQLException;
import java.util.ArrayList;
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
