package com.g02.flightsalesfx.helpers;

import com.g02.btfdao.dao.Dao;
import com.g02.btfdao.dao.Savable;
import com.g02.btfdao.queries.QueryBuilder;
import com.g02.btfdao.queries.QueryExecutor;
import com.g02.btfdao.dao.PGJDBCUtils;
import com.g02.flightsalesfx.businessEntities.Route;
import com.g02.flightsalesfx.businessEntities.SeatOption;
import com.g02.flightsalesfx.businessLogic.*;
import com.g02.flightsalesfx.persistence.AirportStorageService;
import com.g02.flightsalesfx.persistence.EmployeeStorageService;
import com.g02.flightsalesfx.persistence.PersistenceAPIImpl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.List;

@Disabled
public class CreateDB {

    @Test
    void name() throws NoSuchFieldException, SQLException, ClassNotFoundException {
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
                SeatImpl.class
        );
        //System.out.println(queryBuilder.createTablesCreateStatement(databaseSQL));
        var simpledao = PGJDBCUtils.getDataSource("simpledao");
        assert simpledao != null: "No datasource";
        Dao.createAllTables(simpledao.getConnection(),databaseSQL);


    }

    @Test
    void insertEmployee(){
        var persistenceAPI = new PersistenceAPIImpl();
        var employeeStorageService = persistenceAPI.getEmployeeStorageService(new EmployeeManagerImpl());
        System.out.println(employeeStorageService.add(new SalesEmployeeImpl("Nils","b","b")));
        System.out.println(employeeStorageService.add(new SalesEmployeeImpl("","","")));
        System.out.println(employeeStorageService.add(new SalesEmployeeImpl("SalesEmployee","e","")));
        System.out.println(employeeStorageService.add(new SalesOfficerImpl("SalesOfficer","o","")));
        System.out.println(employeeStorageService.add(new SalesManagerImpl("SalesManager","m","")));
    }

    @Test
    void insertAirports() {
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
