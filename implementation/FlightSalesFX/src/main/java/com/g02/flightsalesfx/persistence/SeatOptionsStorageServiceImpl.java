package com.g02.flightsalesfx.persistence;

import com.g02.btfdao.dao.Dao;
import com.g02.flightsalesfx.businessEntities.OptionManager;
import com.g02.flightsalesfx.businessEntities.SeatOption;
import com.g02.flightsalesfx.businessLogic.FlightImpl;
import com.g02.flightsalesfx.businessLogic.SeatOptionImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author anato
 */
public class SeatOptionsStorageServiceImpl implements SeatOptionsStorageService{
    private final Dao<SeatOptionImpl> dao;
    
    public SeatOptionsStorageServiceImpl(OptionManager optionMgr, Dao<SeatOptionImpl> dao){

        this.dao = dao;
    }

    @Override
    public SeatOption add(SeatOption s) {
        try {
            var ret= dao.insert((SeatOptionImpl)s);
            return ret.size()>0?ret.get(0):null;
        } catch (IllegalAccessException | NoSuchFieldException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<SeatOption> getAll() {
        try {
            var all = dao.getAll();
            return new ArrayList<>(all);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return List.of();
    }
    
}
