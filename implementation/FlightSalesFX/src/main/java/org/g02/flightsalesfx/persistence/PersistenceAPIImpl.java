package org.g02.flightsalesfx.persistence;

import org.g02.btfdao.dao.Dao;
import org.g02.btfdao.dao.PGJDBCUtils;
import org.g02.flightsalesfx.businessLogic.PlaneImpl;
import org.g02.flightsalesfx.businessLogic.RouteImpl;
import org.g02.flightsalesfx.businessEntities.*;
import org.g02.flightsalesfx.businessLogic.*;

import java.sql.Connection;
import java.sql.SQLException;

public class PersistenceAPIImpl implements PersistenceAPI, PersistenceApiImplementationProvider {

    private EmployeeStorageServiceImpl employeeStorageService;
    private PlaneStorageService planeStorageService;
    private SeatStorageService seatStorageService;
    private SeatOptionsStorageServiceImpl seatOptionStorageService;
    private FlightOptionStorageServiceImpl flightOptionStorageService;
    private AirportStorageService airportStorageService;
    private RouteStorageService routeStorageService;
    private PriceReductionStorageService priceReductionStorageService;
    private FlightStorageService flightStorageService;
    private TicketStorageService ticketStorageService;
    private BookingStorageService bookingStorageService;


    @Override
    public TicketStorageService getTicketStorageService(TicketManager ticketManager) {
        if(ticketStorageService == null) {
            ticketStorageService = new TicketStorageServiceImpl(ticketManager, new Dao<>(TicketImpl.class, connection));
        }
        return ticketStorageService;
    }

    @Override
    public BookingStorageService getBookingStorageService(BookingManager bookingManager) {
        if(bookingStorageService == null) {
            bookingStorageService = new BookingStorageServiceImpl(bookingManager, new Dao<>(BookingImpl.class, connection));
        }
        return bookingStorageService;
    }

    private Connection connection;

    public PersistenceAPIImpl() {
        var simpledao = PGJDBCUtils.getDataSource("simpledao");
        assert simpledao != null: "No datasource";
        try {
            connection=simpledao.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public EmployeeStorageService getEmployeeStorageService(EmployeeManager employeeManager) {
        if (employeeStorageService == null) {
            employeeStorageService = new EmployeeStorageServiceImpl(
                    employeeManager,
                    new Dao<>(SalesEmployeeImpl.class,connection),
                    new Dao<>(SalesOfficerImpl.class,connection),
                    new Dao<>(SalesManagerImpl.class,connection));
        }
        return employeeStorageService;
    }

    @Override
    public PlaneStorageService getPlaneStorageService(PlaneManager planeManager) {
        if (planeStorageService == null) {
            var dao = new Dao<>(PlaneImpl.class,connection);
            planeStorageService = new PlaneStorageServiceImpl(planeManager, dao);
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
            seatOptionStorageService = new SeatOptionsStorageServiceImpl(optionManager, new Dao<>(SeatOptionImpl.class,connection));
        }
        return seatOptionStorageService;
    }

    @Override
    public FlightOptionStorageService getFlightOptionStorageService(OptionManager optionManager) {
        if (flightOptionStorageService == null) {
            flightOptionStorageService = new FlightOptionStorageServiceImpl(optionManager, new Dao<>(FlightOptionImpl.class,connection));
        }
        return flightOptionStorageService;
    }

    @Override
    public AirportStorageService getAirportStorageService(AirportManager airportManager) {
        if(airportStorageService == null) {
            airportStorageService = new AirportStorageServiceImpl(airportManager, new Dao<>(AirportImpl.class,connection));
        }

        return airportStorageService;
    }

    @Override
    public RouteStorageService getRouteStorageService(RouteManager routeManager) {
        if (routeStorageService == null) {
            routeStorageService = new RouteStorageServiceImpl(routeManager, new Dao<>(RouteImpl.class,connection));
        }
        return routeStorageService;
    }

    @Override
    public PriceReductionStorageService getPriceReductionStorageService(PriceReductionManager priceReductionManager) {
        if(priceReductionStorageService == null) {
            priceReductionStorageService = new PriceReductionStorageServiceImpl(
                    priceReductionManager,
                    new Dao<>(StaticPriceReductionImpl.class,connection),
                    new Dao<>(DynamicPriceReductionImpl.class,connection));
        }
        return priceReductionStorageService;
    }

    @Override
    public FlightStorageService getFlightStorageService(FlightManager flightManager) {
        if(flightStorageService == null) {
            flightStorageService = new FlightStorageServiceImpl(flightManager, new Dao<>(FlightImpl.class,connection));
        }
        return flightStorageService;
    }
}
