package com.g02.flightsalesfx.persistence;

import com.g02.btfdao.dao.Dao;
import com.g02.flightsalesfx.businessEntities.Plane;
import com.g02.flightsalesfx.businessEntities.PlaneManager;
import com.g02.flightsalesfx.businessLogic.PlaneImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

public class PlaneStorageServiceImpl implements PlaneStorageService {

    private final Dao<PlaneImpl> dao;

    public PlaneStorageServiceImpl(PlaneManager planeManager, Dao<PlaneImpl> dao) {
        this.dao = dao;
    }

    @Override
    public Plane add(Plane plane) {
        var planeImpl=new PlaneImpl(plane.getName(), plane.getType(), plane.getManufacturer());
        try {
            var a = dao.insert(planeImpl);
            return a.size()>0?a.get(0):null;
        } catch (IllegalAccessException | NoSuchFieldException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Plane> getAll() {
        try {
            var all = dao.getAll();
            return new ArrayList<>(all);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return List.of();
    }
}
