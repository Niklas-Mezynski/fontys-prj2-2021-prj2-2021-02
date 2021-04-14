package com.g02.flightsalesfx.persistence;

import com.g02.btfdao.dao.Dao;
import com.g02.btfdao.dao.DaoFactory;
import com.g02.btfdao.utils.PGJDBCUtils;
import com.g02.flightsalesfx.businessEntities.*;
import com.g02.flightsalesfx.businessLogic.OptionManagerImpl;
import com.g02.flightsalesfx.businessLogic.PlaneImpl;
import com.g02.flightsalesfx.businessLogic.SeatManagerImpl;

import java.sql.SQLException;
import java.util.List;

public class PersistenceAPIImpl implements PersistenceAPI, PersistenceApiImplementationProvider {

    private EmployeeStorageServiceImpl employeeStorageService;
    private PlaneStorageService planeStorageService;
    private SeatStorageService seatStorageService;
    private SeatOptionsStorageServiceImpl seatOptionStorageService;
    private AirportStorageService airportStorageService;
    private RouteStorageService routeStorageService;
    private PriceReductionStorageService priceReductionStorageService;
    private FlightStorageService flightStorageService;

    private DaoFactory daoFactory;

    public PersistenceAPIImpl() {
        var simpledao = PGJDBCUtils.getDataSource("simpledao");
        daoFactory = new DaoFactory(simpledao);
    }

    @Override
    public EmployeeStorageService getEmployeeStorageService(EmployeeManager employeeManager) {
        if (employeeStorageService == null)
            employeeStorageService = new EmployeeStorageServiceImpl(employeeManager);
        return employeeStorageService;
    }

    @Override
    public PlaneStorageService getPlaneStorageService(PlaneManager planeManager) {
        if (planeStorageService == null) {
            try {
                var dao = daoFactory.createDao(PlaneImpl.class);
                planeStorageService = new PlaneStorageServiceImpl(planeManager, dao);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return planeStorageService;
    }

    @Override
    public SeatStorageService getSeatStorageService(SeatManager seatManager) {
        if (seatStorageService == null) {
            seatStorageService = new SeatStorageServiceImpl(seatManager);
        }
        return seatStorageService;
    }

    @Override
    public SeatOptionsStorageService getSeatOptionStorageService(OptionManager optionManager) {
        if (seatOptionStorageService == null) {
            seatOptionStorageService = new SeatOptionsStorageServiceImpl(optionManager);
        }
        return seatOptionStorageService;
    }

    @Override
    public AirportStorageService getAirportStorageService(AirportManager airportManager) {
        if(airportStorageService == null) {
            airportStorageService = new AirportStorageServiceImpl(airportManager);
        }

        return airportStorageService;
    }

    @Override
    public RouteStorageService getRouteStorageService(RouteManager routeManager) {
        if (routeStorageService == null) {
            routeStorageService = new RouteStorageServiceImpl(routeManager);
        }
        return routeStorageService;
    }

    @Override
    public PriceReductionStorageService getPriceReductionStorageService(PriceReductionManager priceReductionManager) {
        if(priceReductionStorageService == null) {
            priceReductionStorageService = new PriceReductionStorageServiceImpl(priceReductionManager);
        }
        return priceReductionStorageService;
    }

    @Override
    public FlightStorageService getFlightStorageService(FlightManager flightManager) {
        if(flightStorageService == null) {
            flightStorageService = new FlightStorageServiceImpl(flightManager);
        }
        return flightStorageService;
    }
}
