package com.g02.flightsalesfx.persistence;

import com.g02.btfdao.dao.Dao;
import com.g02.flightsalesfx.businessEntities.Flight;
import com.g02.flightsalesfx.businessEntities.FlightManager;
import com.g02.flightsalesfx.businessLogic.AirportImpl;
import com.g02.flightsalesfx.businessLogic.FlightImpl;
import com.g02.flightsalesfx.businessLogic.SalesOfficerImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FlightStorageServiceImpl implements FlightStorageService{

    private final Dao<FlightImpl> dao;

    public FlightStorageServiceImpl(FlightManager flightManager, Dao<FlightImpl> dao) {
        this.dao = dao;
    }

    @Override
    public Flight add(Flight flight) {
        var createdby=new SalesOfficerImpl(flight.getCreatedBy().getName(),flight.getCreatedBy().getEmail(),flight.getCreatedBy().getPassword());
        var flightImpl=new FlightImpl(createdby,flight.getFlightNumber(),flight.getDeparture(),flight.getArrival(), flight.getRoute(), flight.getPlane(), flight.getPrice());
        try {
            var ret= dao.insert(flightImpl);
            return ret.size()>0?ret.get(0):null;
        } catch (IllegalAccessException | NoSuchFieldException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Flight remove(Flight flight) {
        var createdby=new SalesOfficerImpl(flight.getCreatedBy().getName(),flight.getCreatedBy().getEmail(),flight.getCreatedBy().getPassword());
        var flightImpl=new FlightImpl(createdby,flight.getFlightNumber(),flight.getDeparture(),flight.getArrival(), flight.getRoute(), flight.getPlane(), flight.getPrice());
        try {
            var ret= dao.remove(flightImpl);
            return ret.size()>0?ret.get(0):null;
        } catch (IllegalAccessException | NoSuchFieldException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Flight> getAll() {
        try {
            var all = dao.getAll();
            return new ArrayList<>(all);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return List.of();
    }
}
