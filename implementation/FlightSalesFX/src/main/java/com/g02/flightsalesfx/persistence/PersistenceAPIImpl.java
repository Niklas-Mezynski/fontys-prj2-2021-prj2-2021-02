package com.g02.flightsalesfx.persistence;

import com.g02.btfdao.dao.DaoFactory;
import com.g02.btfdao.utils.PGJDBCUtils;
import com.g02.flightsalesfx.businessEntities.*;
import com.g02.flightsalesfx.businessLogic.PlaneImpl;
import com.g02.flightsalesfx.businessLogic.RouteImpl;
import com.g02.flightsalesfx.businessLogic.*;

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
        if(ticketStorageService == null)
            ticketStorageService = new TicketStorageServiceImpl(ticketManager);
        return ticketStorageService;
    }

    @Override
    public BookingStorageService getBookingStorageService(BookingManager bookingManager) {
        if(bookingStorageService == null)
            bookingStorageService = new BookingStorageServiceImpl(bookingManager);
        return bookingStorageService;
    }

    private DaoFactory daoFactory;

    public PersistenceAPIImpl() {
        var simpledao = PGJDBCUtils.getDataSource("simpledao");
        assert simpledao != null: "No datasource";
        daoFactory = new DaoFactory(simpledao);
    }

    @Override
    public EmployeeStorageService getEmployeeStorageService(EmployeeManager employeeManager) {
        if (employeeStorageService == null) {
            try {
                employeeStorageService = new EmployeeStorageServiceImpl(employeeManager, daoFactory.createDao(SalesEmployeeImpl.class), daoFactory.createDao(SalesOfficerImpl.class), daoFactory.createDao(SalesManagerImpl.class));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
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
            try {
                seatOptionStorageService = new SeatOptionsStorageServiceImpl(optionManager, daoFactory.createDao(SeatOptionImpl.class));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return seatOptionStorageService;
    }

    @Override
    public FlightOptionStorageService getFlightOptionStorageService(OptionManager optionManager) {
        if (flightOptionStorageService == null) {
            try {
                flightOptionStorageService = new FlightOptionStorageServiceImpl(optionManager, daoFactory.createDao(FlightOptionImpl.class));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return flightOptionStorageService;
    }

    @Override
    public AirportStorageService getAirportStorageService(AirportManager airportManager) {
        if(airportStorageService == null) {
            try {
                airportStorageService = new AirportStorageServiceImpl(airportManager, daoFactory.createDao(AirportImpl.class));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return airportStorageService;
    }

    @Override
    public RouteStorageService getRouteStorageService(RouteManager routeManager) {
        if (routeStorageService == null) {
            try {
                routeStorageService = new RouteStorageServiceImpl(routeManager, daoFactory.createDao(RouteImpl.class));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return routeStorageService;
    }

    @Override
    public PriceReductionStorageService getPriceReductionStorageService(PriceReductionManager priceReductionManager) {
        if(priceReductionStorageService == null) {
            try {
                priceReductionStorageService = new PriceReductionStorageServiceImpl(priceReductionManager, daoFactory.createDao(StaticPriceReductionImpl.class), daoFactory.createDao(DynamicPriceReductionImpl.class));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return priceReductionStorageService;
    }

    @Override
    public FlightStorageService getFlightStorageService(FlightManager flightManager) {
        if(flightStorageService == null) {
            try {
                flightStorageService = new FlightStorageServiceImpl(flightManager, daoFactory.createDao(FlightImpl.class));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return flightStorageService;
    }
}
