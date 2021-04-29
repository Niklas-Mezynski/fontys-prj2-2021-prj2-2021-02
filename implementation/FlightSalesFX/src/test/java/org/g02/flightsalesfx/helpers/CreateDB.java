package org.g02.flightsalesfx.helpers;

import org.g02.btfdao.dao.Dao;
import org.g02.btfdao.dao.PGJDBCUtils;
import org.g02.btfdao.dao.Savable;
import org.g02.flightsalesfx.persistence.PersistenceAPIImpl;
import org.g02.flightsalesfx.businessLogic.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.sql.SQLException;
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
        Dao.createAllTables(simpledao.getConnection(),databaseSQL);


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
    void t03insertAirports() {
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
    }
}
