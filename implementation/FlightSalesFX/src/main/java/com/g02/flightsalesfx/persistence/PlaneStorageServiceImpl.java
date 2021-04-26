package com.g02.flightsalesfx.persistence;

import com.g02.btfdao.dao.Dao;
import com.g02.flightsalesfx.businessEntities.Plane;
import com.g02.flightsalesfx.businessEntities.PlaneManager;
import com.g02.flightsalesfx.businessLogic.PlaneImpl;

import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.List;

public class PlaneStorageServiceImpl implements PlaneStorageService {

    private final Dao<PlaneImpl> dao;

    public PlaneStorageServiceImpl(PlaneManager planeManager, Dao<PlaneImpl> dao) {
        this.dao = dao;
    }

    @Override
    public Plane add(Plane plane) {
        var planeImpl = new PlaneImpl(plane.getName(), plane.getType(), plane.getManufacturer());
        planeImpl.addAllSeats(plane.getAllSeats());
        try {
            var a = dao.insert(planeImpl);
            return a.isPresent()? a.get() : null;
        } catch ( SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Plane> getAll() {
        try {
            var all = dao.getAll();
            return new ArrayList<>(all);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return List.of();
    }

    @Override
    public boolean delete(Plane plane) {
        var planeImpl = new PlaneImpl(plane.getId(), plane.getName(), plane.getType(), plane.getManufacturer());
        planeImpl.addAllSeats(plane.getAllSeats());
        try {
            dao.remove(planeImpl);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Plane update(Plane plane) {
        var planeImpl = new PlaneImpl(plane.getId(), plane.getName(), plane.getType(), plane.getManufacturer());
        planeImpl.addAllSeats(plane.getAllSeats());
        try {
            var update = dao.update(planeImpl);
            return update;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
