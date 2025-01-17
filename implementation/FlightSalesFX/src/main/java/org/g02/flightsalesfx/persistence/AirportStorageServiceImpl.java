package org.g02.flightsalesfx.persistence;

import org.g02.btfdao.dao.Dao;
import org.g02.flightsalesfx.businessEntities.Airport;
import org.g02.flightsalesfx.businessEntities.AirportManager;
import org.g02.flightsalesfx.businessLogic.AirportImpl;

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
            var ret= dao.insert(new AirportImpl(airport.getName(), airport.getCity(), airport.getCountry()));
            return ret.isPresent()?ret.get():null;
        } catch ( SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Airport> getAll() {
        try {
            List<AirportImpl> all = dao.getAll();
            return new ArrayList<>(all);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return List.of();
    }
}
