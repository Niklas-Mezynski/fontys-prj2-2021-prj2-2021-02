package com.g02.flightsalesfx.helpers;

import com.g02.btfdao.queries.QueryBuilder;
import com.g02.flightsalesfx.businessLogic.EmployeeManagerImpl;
import com.g02.flightsalesfx.businessLogic.SalesEmployeeImpl;
import com.g02.flightsalesfx.businessLogic.SalesManagerImpl;
import com.g02.flightsalesfx.businessLogic.SalesOfficerImpl;
import com.g02.flightsalesfx.persistence.EmployeeStorageService;
import com.g02.flightsalesfx.persistence.PersistenceAPIImpl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.sql.SQLFeatureNotSupportedException;

public class CreateDB {

    @Test
    void name() throws NoSuchFieldException, SQLFeatureNotSupportedException, ClassNotFoundException {
        var queryBuilder = new QueryBuilder();
        var datebaseSQL = queryBuilder.createDatabaseSQL(new String[]{
                "com.g02.flightsalesfx.businessLogic.AirportImpl",
                "com.g02.flightsalesfx.businessLogic.DynamicPriceReductionImpl",
                "com.g02.flightsalesfx.businessLogic.FlightImpl",
                "com.g02.flightsalesfx.businessLogic.FlightOptionImpl",
                "com.g02.flightsalesfx.businessLogic.PlaneImpl",
                "com.g02.flightsalesfx.businessLogic.ReoccurringFlightImpl",
                "com.g02.flightsalesfx.businessLogic.RouteImpl",
                "com.g02.flightsalesfx.businessLogic.StaticPriceReductionImpl",
                "com.g02.flightsalesfx.businessLogic.SalesOfficerImpl",
                "com.g02.flightsalesfx.businessLogic.SalesManagerImpl",
                "com.g02.flightsalesfx.businessLogic.SalesEmployeeImpl",
                "com.g02.flightsalesfx.businessLogic.SeatOptionImpl",
                "com.g02.flightsalesfx.businessLogic.SeatImpl",
        });
        System.out.println(datebaseSQL);
    }
    @Disabled
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
}
