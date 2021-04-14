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
    private List<Plane> planes;

    public PlaneStorageServiceImpl(PlaneManager planeManager, Dao<PlaneImpl> dao) {
        this.dao = dao;
        planes = new ArrayList<>();
    }

    @Override
    public boolean add(Plane plane) {
        if (!(plane instanceof PlaneImpl)) {
            return false;
        }
        try {
            dao.insert((PlaneImpl)plane);
        } catch (IllegalAccessException | NoSuchFieldException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public List<Plane> getAll() {
        return unmodifiableList(planes);
    }
}
