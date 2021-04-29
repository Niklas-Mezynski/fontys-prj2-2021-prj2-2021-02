package org.g02.flightsalesfx.persistence;

import org.g02.btfdao.dao.Dao;
import org.g02.flightsalesfx.businessEntities.OptionManager;
import org.g02.flightsalesfx.businessEntities.SeatOption;
import org.g02.flightsalesfx.businessLogic.SeatOptionImpl;

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
            var ret= dao.insert(SeatOptionImpl.of(s));
            return ret.isPresent()?ret.get():null;
        } catch ( SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<SeatOption> getAll() {
        try {
            var all = dao.getAll();
            return new ArrayList<>(all);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return List.of();
    }
    
}
