package org.g02.flightsalesfx.persistence;

import org.g02.btfdao.dao.Dao;
import org.g02.flightsalesfx.businessEntities.FlightOption;
import org.g02.flightsalesfx.businessEntities.OptionManager;
import org.g02.flightsalesfx.businessLogic.FlightOptionImpl;

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
            return ret.isPresent()?ret.get():null;
        } catch ( SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<FlightOption> getAll() {
        try {
            var ret= dao.getAll();
            return new ArrayList<>(ret);
        } catch ( SQLException e) {
            e.printStackTrace();
        }
        return List.of();
    }
}
