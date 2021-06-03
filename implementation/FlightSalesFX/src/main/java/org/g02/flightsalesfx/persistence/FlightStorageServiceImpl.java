package org.g02.flightsalesfx.persistence;

import org.g02.btfdao.dao.Dao;
import org.g02.flightsalesfx.businessEntities.Flight;
import org.g02.flightsalesfx.businessEntities.FlightManager;
import org.g02.flightsalesfx.businessLogic.FlightImpl;
import org.g02.flightsalesfx.businessLogic.SalesOfficerImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FlightStorageServiceImpl implements FlightStorageService{

    private final Dao<FlightImpl> dao;

    public FlightStorageServiceImpl(FlightManager flightManager, Dao<FlightImpl> dao) {
        this.dao = dao;
    }

    @Override
    public Flight add(Flight flight) {
//        var createdby=SalesOfficerImpl.of(flight.getCreatedBy());
//        var flightImpl=new FlightImpl(createdby,flight.getFlightNumber(),flight.getDeparture(),flight.getArrival(), null, null, flight.getPrice());
        var flightImpl=FlightImpl.of(flight);
        try {
            var ret= dao.insert(flightImpl);
            if (ret.isPresent()){
//                var flightret=ret.get(0);
//                flightret.plane= PlaneImpl.of(flight.getPlane());
//                flightret.route= RouteImpl.of(flight.getRoute());
//                var flightreto=dao.update(flightret);
                return ret.get();
            }
            return null;
        } catch ( SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean remove(Flight flight) {
//        var createdby=new SalesOfficerImpl(flight.getCreatedBy().getName(),flight.getCreatedBy().getEmail(),flight.getCreatedBy().getPassword());
        var flightImpl=FlightImpl.of(flight);
        try {
            dao.remove(flightImpl);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public List<Flight> getAll() {
        try {
            var all = dao.getAll();
            return new ArrayList<>(all);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return List.of();
    }

    @Override
    public Flight update(Flight flight) {
        FlightImpl flightImpl = FlightImpl.of(flight);
        try {
            var update = dao.update(flightImpl);
            return update;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
