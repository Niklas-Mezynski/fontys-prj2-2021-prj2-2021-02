package org.g02.flightsalesfx.helpers;

import org.g02.btfdao.dao.Dao;
import org.g02.btfdao.dao.PGJDBCUtils;
import org.g02.btfdao.dao.Savable;
import org.g02.flightsalesfx.businessEntities.*;
import org.g02.flightsalesfx.persistence.PersistenceAPIImpl;
import org.g02.flightsalesfx.businessLogic.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Disabled
@TestMethodOrder(MethodOrderer.MethodName.class)
public class CreateDB {

    @Test
    void t01name() throws NoSuchFieldException, SQLException, ClassNotFoundException {
        List<Class<? extends Savable>> databaseSQL = List.of(
                AirportImpl.class,
                DynamicPriceReductionImpl.class,
                FlightImpl.class,
                FlightOptionImpl.class,
                PlaneImpl.class,
                ReoccurringFlightImpl.class,
                RouteImpl.class,
                StaticPriceReductionImpl.class,
                SalesOfficerImpl.class,
                SalesManagerImpl.class,
                SalesEmployeeImpl.class,
                SeatOptionImpl.class,
                SeatImpl.class,
                TicketImpl.class,
                BookingImpl.class
        );
        //System.out.println(queryBuilder.createTablesCreateStatement(databaseSQL));
        var simpledao = PGJDBCUtils.getDataSource("simpledao");
        assert simpledao != null: "No datasource";
        var connection=simpledao.getConnection();
        Dao.createAllTables(connection,databaseSQL);
        connection.close();


    }

    @Test
    void t02insertEmployee(){
        var persistenceAPI = new PersistenceAPIImpl();
        var bb= new BusinessLogicAPIImpl(persistenceAPI);
        var employeeStorageService = persistenceAPI.getEmployeeStorageService(new EmployeeManagerImpl());
        System.out.println(employeeStorageService.add(new SalesEmployeeImpl("Nils","b",bb.genPWHash("b"))));
        System.out.println(employeeStorageService.add(new SalesEmployeeImpl("","",bb.genPWHash(""))));
        System.out.println(employeeStorageService.add(new SalesEmployeeImpl("SalesEmployee","e",bb.genPWHash(""))));
        System.out.println(employeeStorageService.add(new SalesOfficerImpl("SalesOfficer","o",bb.genPWHash(""))));
        System.out.println(employeeStorageService.add(new SalesManagerImpl("SalesManager","m",bb.genPWHash(""))));
    }

    @Test
    void t03insertAirportsAndRoutes() {
        var persistenceAPI = new PersistenceAPIImpl();
        var airportStorageService = persistenceAPI.getAirportStorageService(new AirportManagerImpl());
        System.out.println(airportStorageService.add(new AirportImpl("BRE","Bremen","Germany")));
        System.out.println(airportStorageService.add(new AirportImpl("FCN","Cuxhaven","Germany")));
        System.out.println(airportStorageService.add(new AirportImpl("DRS","Dresden","Germany")));
        System.out.println(airportStorageService.add(new AirportImpl("HHN","Frankfurt-Hahn","Germany")));
        System.out.println(airportStorageService.add(new AirportImpl("HAM","Hamburg","Germany")));
        System.out.println(airportStorageService.add(new AirportImpl("DUS", "Düsseldorf", "Germany")));
        System.out.println(airportStorageService.add(new AirportImpl("BER", "Berlin", "Germany")));
        System.out.println(airportStorageService.add(new AirportImpl("FRA", "Frankfurt", "Germany")));

        var routeStorageService = persistenceAPI.getRouteStorageService(new RouteManagerImpl());
        List<Airport> all = airportStorageService.getAll();
        System.out.println(routeStorageService.add(new RouteImpl(all.get(0), all.get(1))));
        System.out.println(routeStorageService.add(new RouteImpl(all.get(1), all.get(2))));
        System.out.println(routeStorageService.add(new RouteImpl(all.get(2), all.get(3))));

    }

    @Test
    void t04insertPlanes() {
        var persistenceAPI = new PersistenceAPIImpl();
        var planeStorageService = persistenceAPI.getPlaneStorageService(new PlaneManagerImpl());
        PlaneImpl plane = new PlaneImpl("Flugzeug", "B418", "Airbus");
        List<SeatImpl> seats = new ArrayList<>();
        seats.add(new SeatImpl(0, 0));
        seats.add(new SeatImpl(0, 1));
        seats.add(new SeatImpl(1, 0));
        seats.add(new SeatImpl(1, 1));
        plane.addAllSeats(seats);
        System.out.println(planeStorageService.add(plane));
    }

    @Test
    void t05insertFlight() {
        var persistenceAPI = new PersistenceAPIImpl();
        var flightStorageService = persistenceAPI.getFlightStorageService(new FlightManagerImpl());
        System.out.println(flightStorageService.add(new FlightImpl(
                (SalesOfficerImpl) persistenceAPI.getEmployeeStorageService(new EmployeeManagerImpl()).get("o").get(),
                LocalDateTime.of(2018, 6, 5, 12, 30),
                LocalDateTime.of(2018, 6, 5, 14, 30),
                persistenceAPI.getRouteStorageService(new RouteManagerImpl()).getAll().get(0),
                persistenceAPI.getPlaneStorageService(new PlaneManagerImpl()).getAll().get(0),
                85.99
        )));
        System.out.println(flightStorageService.add(new FlightImpl(
                (SalesOfficerImpl) persistenceAPI.getEmployeeStorageService(new EmployeeManagerImpl()).get("o").get(),
                LocalDateTime.of(2019, 6, 5, 12, 30),
                LocalDateTime.of(2019, 6, 5, 14, 30),
                persistenceAPI.getRouteStorageService(new RouteManagerImpl()).getAll().get(0),
                persistenceAPI.getPlaneStorageService(new PlaneManagerImpl()).getAll().get(0),
                75.99
        )));
        System.out.println(flightStorageService.add(new FlightImpl(
                (SalesOfficerImpl) persistenceAPI.getEmployeeStorageService(new EmployeeManagerImpl()).get("o").get(),
                LocalDateTime.of(2020, 6, 5, 12, 30),
                LocalDateTime.of(2020, 6, 5, 14, 30),
                persistenceAPI.getRouteStorageService(new RouteManagerImpl()).getAll().get(0),
                persistenceAPI.getPlaneStorageService(new PlaneManagerImpl()).getAll().get(0),
                85.99
        )));
        System.out.println(flightStorageService.add(new FlightImpl(
                (SalesOfficerImpl) persistenceAPI.getEmployeeStorageService(new EmployeeManagerImpl()).get("o").get(),
                LocalDateTime.of(2021, 6, 5, 12, 30),
                LocalDateTime.of(2021, 6, 5, 14, 30),
                persistenceAPI.getRouteStorageService(new RouteManagerImpl()).getAll().get(0),
                persistenceAPI.getPlaneStorageService(new PlaneManagerImpl()).getAll().get(0),
                95.99
        )));
    }

    @Test
    void t06insertBookings() {
        var persistenceAPI = new PersistenceAPIImpl();
        var bookingStorageService = persistenceAPI.getBookingStorageService(new BookingManagerImpl());

        var flight = persistenceAPI.getFlightStorageService(new FlightManagerImpl()).getAll().get(0);
        TicketImpl ticket = new TicketImpl(flight, flight.getPlane().getAllSeats().get(0), "Hans", "Peter", new SeatOption[0]);
        TicketImpl[] tickets = {ticket};

        System.out.println(bookingStorageService.add(new BookingImpl(
                (SalesEmployeeImpl) persistenceAPI.getEmployeeStorageService(new EmployeeManagerImpl()).get("e").get(),
                flight,
                tickets,
                new FlightOption[0],
                "hans@gmail.com"
        )));


        flight = persistenceAPI.getFlightStorageService(new FlightManagerImpl()).getAll().get(1);
        ticket = new TicketImpl(flight, flight.getPlane().getAllSeats().get(0), "Hans", "Peter", new SeatOption[0]);
        tickets = new TicketImpl[]{ticket};

        System.out.println(bookingStorageService.add(new BookingImpl(
                (SalesEmployeeImpl) persistenceAPI.getEmployeeStorageService(new EmployeeManagerImpl()).get("e").get(),
                flight,
                tickets,
                new FlightOption[0],
                "hans@gmail.com"
        )));


        flight = persistenceAPI.getFlightStorageService(new FlightManagerImpl()).getAll().get(2);
        ticket = new TicketImpl(flight, flight.getPlane().getAllSeats().get(0), "Hans", "Peter", new SeatOption[0]);
        tickets = new TicketImpl[]{ticket};

        System.out.println(bookingStorageService.add(new BookingImpl(
                (SalesEmployeeImpl) persistenceAPI.getEmployeeStorageService(new EmployeeManagerImpl()).get("e").get(),
                flight,
                tickets,
                new FlightOption[0],
                "hans@gmail.com"
        )));


        flight = persistenceAPI.getFlightStorageService(new FlightManagerImpl()).getAll().get(3);
        ticket = new TicketImpl(flight, flight.getPlane().getAllSeats().get(0), "Hans", "Peter", new SeatOption[0]);
        tickets = new TicketImpl[]{ticket};

        System.out.println(bookingStorageService.add(new BookingImpl(
                (SalesEmployeeImpl) persistenceAPI.getEmployeeStorageService(new EmployeeManagerImpl()).get("e").get(),
                flight,
                tickets,
                new FlightOption[0],
                "hans@gmail.com"
        )));
    }
}
