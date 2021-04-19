package com.g02.flightsalesfx.persistence;

import com.g02.btfdao.dao.Dao;
import com.g02.flightsalesfx.businessEntities.Airport;
import com.g02.flightsalesfx.businessEntities.AirportManager;
import com.g02.flightsalesfx.businessLogic.AirportImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AirportStorageServiceImpl implements AirportStorageService {

//    private final List<Airport> airports;
    private final Dao<AirportImpl> dao;

    public AirportStorageServiceImpl(AirportManager airportManager, Dao<AirportImpl> dao) {
//        airports = new ArrayList<>();
        this.dao=dao;
    }


    @Override
    public Airport add(Airport airport) {
        try {
            var ret= dao.insert((AirportImpl)airport);
            return ret.size()>0?ret.get(0):null;
        } catch (IllegalAccessException | NoSuchFieldException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Airport> getAll() {
        try {
            List<AirportImpl> all = dao.getAll();
            return new ArrayList<>(all);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return List.of();
    }
}
