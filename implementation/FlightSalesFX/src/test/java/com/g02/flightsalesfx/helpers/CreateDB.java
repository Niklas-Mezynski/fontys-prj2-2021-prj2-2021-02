package com.g02.flightsalesfx.helpers;

import com.g02.btfdao.queries.QueryBuilder;
import org.junit.jupiter.api.Test;

import java.sql.SQLFeatureNotSupportedException;

public class CreateDB {

    @Test
    void name() throws NoSuchFieldException, SQLFeatureNotSupportedException, ClassNotFoundException {
        var queryBuilder = new QueryBuilder();
        var datebaseSQL = queryBuilder.createDatabaseSQL(new String[]{
                "com.g02.flightsalesfx.businessLogic.PlaneImpl",
                "com.g02.flightsalesfx.businessLogic.SeatImpl",
                "com.g02.flightsalesfx.businessLogic.SeatOptionImpl",
                "com.g02.flightsalesfx.businessLogic.AirportImpl",
                "com.g02.flightsalesfx.businessLogic.RouteImpl",
                "com.g02.flightsalesfx.businessLogic.StaticPriceReductionImpl",
                "com.g02.flightsalesfx.businessLogic.SalesOfficerImpl",
                "com.g02.flightsalesfx.businessLogic.SalesManagerImpl",
                "com.g02.flightsalesfx.businessLogic.SalesEmployeeImpl",
                "com.g02.flightsalesfx.businessLogic.FlightImpl",
                "com.g02.flightsalesfx.businessLogic.ReoccurringFlightImpl",
                "com.g02.flightsalesfx.businessLogic.FlightOptionImpl",
        });
        System.out.println(datebaseSQL);
    }
}
