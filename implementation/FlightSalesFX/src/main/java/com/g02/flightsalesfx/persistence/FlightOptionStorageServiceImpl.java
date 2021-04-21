package com.g02.flightsalesfx.persistence;

import com.g02.btfdao.dao.Dao;
import com.g02.flightsalesfx.businessEntities.FlightOption;
import com.g02.flightsalesfx.businessEntities.OptionManager;
import com.g02.flightsalesfx.businessLogic.FlightOptionImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FlightOptionStorageServiceImpl implements FlightOptionStorageService {
    private final OptionManager optionManager;
    private final Dao<FlightOptionImpl> dao;

    public FlightOptionStorageServiceImpl(OptionManager optionManager, Dao<FlightOptionImpl> dao) {
        this.optionManager = optionManager;
        this.dao = dao;
    }

    @Override
    public FlightOption add(FlightOption flightOption) {
        try {
            var ret = dao.insert(new FlightOptionImpl(flightOption.getName(), flightOption.getMaxAvailability(), flightOption.getPrice()));
            return ret.size()>0?ret.get(0):null;
        } catch (IllegalAccessException | NoSuchFieldException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<FlightOption> getAll() {
        try {
            var ret= dao.getAll();
            return new ArrayList<>(ret);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return List.of();
    }
}
