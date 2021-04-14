package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.businessEntities.*;
import com.g02.flightsalesfx.persistence.PersistenceAPIImpl;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.ref.SoftReference;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//@TestMethodOrder( MethodOrderer.Alphanumeric.class )
public class BusinessLogicAPIImplTest {
    BusinessLogicAPIImpl api = new BusinessLogicAPIImpl(new PersistenceAPIImpl());

    @Test
    void t01SeatOptionTest() {
        SeatOption so = api.getOptionManager().createSeatOption("Business class", 99.99);
        SoftAssertions.assertSoftly(s -> {
            s.assertThat(so.getName()).isEqualTo("Business class");
            s.assertThat(so.toString()).contains("Business class");
        });
    }

    @Test
    void t02SeatTest() {
        //Test for first Constructor
        SeatOption so1 = api.getOptionManager().createSeatOption("First class", 150);
        SeatOption so2 = api.getOptionManager().createSeatOption("Business class", 99.99);
        List<SeatOption> so = new ArrayList<>();

        Seat seat = api.getSeatManager().createSeat(1,2, so);
        SoftAssertions.assertSoftly( s -> {
            s.assertThat(seat.getRowNumber()).isEqualTo(1);
            s.assertThat(seat.getSeatNumber()).isEqualTo(2);
            seat.addSeatOption(so1);
            s.assertThat(seat.toString()).contains("First class");
            so.add(so2);
            seat.addAllSeatOptions(so);
            s.assertThat(seat.toString()).contains("First class", "Business class");
        });

    }

//    @Disabled //Currently broken, throws errors
    @ParameterizedTest
    @CsvSource( {
            //RowNo1, SeatNo1, RowNo2, SeatNo2, expected
            "1, 1, 2, 2, -1"
    } )
    public void t03SeatCompareTo(int RowNo1, int SeatNo1, int RowNo2, int SeatNo2, int expected) {
        List<SeatOption> so = new ArrayList<>();
        Seat seat1 = api.getSeatManager().createSeat(RowNo1,SeatNo1, so);
        Seat seat2 = api.getSeatManager().createSeat(RowNo2,SeatNo2, so);
        assertThat(Integer.signum(seat1.compareTo(seat2))).isEqualTo(expected);
    }

    @Test
    void t04PlaneTest() {
        List<Seat> seats = new ArrayList<>();
        seats.add(new SeatImpl(0, 0));
        seats.add(new SeatImpl(0, 1));
        api.createPlaneFromUI("D-ABCH", "A380", "Airbus", seats);

        var planes = api.persistenceAPI.getPlaneStorageService(api.getPlaneManager()).getAll();
        var plane = new PlaneImpl("D-ABCH", "A380", "Airbus");
        plane.addSeat(new SeatImpl(0, 0));
        plane.addSeat(new SeatImpl(0, 1));
        var plane1 = planes.get(0);
        assertThat(plane1.getRowCount()).isEqualTo(1);
        assertThat(plane1.getSeatCount()).isEqualTo(2);
        assertThat(plane1).isEqualTo(plane);
    }

    @Test
    void t05EmployeeTest() {
        SalesEmployee salesEmployee = api.getEmployeeManager().createSalesEmployee("Peter", "peter@gmail.com", "peter123");
        SalesManager salesManager = api.getEmployeeManager().createSalesManager("Max", "max@gmail.com", "max123");
        SalesOfficer salesOfficer = api.getEmployeeManager().createSalesOfficer("Huhn", "huhn@gmail.com", "huhn123");

        SoftAssertions.assertSoftly( s -> {
            s.assertThat(salesEmployee.getName()).isEqualTo("Peter");
            s.assertThat(salesManager.getName()).isEqualTo("Max");
            s.assertThat(salesOfficer.getName()).isEqualTo("Huhn");

            s.assertThat(salesEmployee.getEmail()).isEqualTo("peter@gmail.com");
            s.assertThat(salesManager.getEmail()).isEqualTo("max@gmail.com");
            s.assertThat(salesOfficer.getEmail()).isEqualTo("huhn@gmail.com");

            s.assertThat(salesEmployee.getPassword()).isEqualTo("peter123");
            s.assertThat(salesManager.getPassword()).isEqualTo("max123");
            s.assertThat(salesOfficer.getPassword()).isEqualTo("huhn123");
        });
    }

    @ParameterizedTest
    @CsvSource({
            "Peter, peter@gmx.de, peterIstDerBeste, Peter, peter@gmx.de, peterIstDerBeste, false",
            "Peter, peter@gmx.de, peterIstDerBeste, Franz, notexisting@gmx.de, franzIstDerBeste, true",
    })
    void t06Login(String refName, String refEmail, String refPassword, String compName, String compEmail, String compPassword, boolean isNull) {
        Employee employee = new SalesEmployeeImpl(refName, refEmail, refPassword);
        api.persistenceAPI.getEmployeeStorageService(api.getEmployeeManager()).add(employee);
        var peterIstDerBeste = api.login(compEmail, compPassword);
        assertThat(peterIstDerBeste == null).isEqualTo(isNull);
    }

    @Test
    void t07Airport() {
        Airport airport = api.getAirportManager().createAirport("DUS", "Düsseldorf", "Germany");
        SoftAssertions.assertSoftly( s -> {
            s.assertThat(airport.getName()).isEqualTo("DUS");
            s.assertThat(airport.getCity()).isEqualTo("Düsseldorf");
            s.assertThat(airport.getCountry()).isEqualTo("Germany");
        });
    }

    @Test
    void t08Route() {
        Airport a1 = api.getAirportManager().createAirport("DUS", "Düsseldorf", "Germany");
        Airport a2 = api.getAirportManager().createAirport("BER", "Berlin", "Germany");

        Route route = api.getRouteManager().createRoute(a1, a2);

        SoftAssertions.assertSoftly( s -> {
            s.assertThat(route.getDepartureAirport()).isEqualTo(a1);
            s.assertThat(route.getArrivalAirport()).isEqualTo(a2);
        });
    }

    @Test
    void t09Flight() {
        Airport a1 = api.getAirportManager().createAirport("DUS", "Düsseldorf", "Germany");
        Airport a2 = api.getAirportManager().createAirport("BER", "Berlin", "Germany");
        Route route = api.getRouteManager().createRoute(a1, a2);

        Flight f1 = api.getFlightManager().createFlight(new SalesOfficerImpl("Huhn", "huhn@gmail.com", "huhn123"), 123, LocalDateTime.MIN, LocalDateTime.now(), route, new PlaneImpl("D-ABCH", "A380", "Airbus"), 0.70);

        SoftAssertions.assertSoftly( s -> {
            s.assertThat(f1.getArrival().equals(LocalDateTime.now()));
            s.assertThat(f1.getCreatedBy().getName().equals("Huhn"));
            s.assertThat(f1.getFlightNumber() == 123);
            s.assertThat(f1.getPlane().getName().equals("D-ABCH"));
            s.assertThat(f1.getPrice() == 0.70);
            s.assertThat(f1.getRoute().equals(route));
        });
    }
}
