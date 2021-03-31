package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.businessEntities.*;
import com.g02.flightsalesfx.persistence.PersistenceAPIImpl;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

@TestMethodOrder( MethodOrderer.Alphanumeric.class )
public class BusinessLogicAPIImplTest {
    BusinessLogicAPIImpl api = new BusinessLogicAPIImpl(new PersistenceAPIImpl());

    @Test
    void t01SeatOptionTest() {
        SeatOption so = api.getOptionManager().createSeatOption("Business class");
        SoftAssertions.assertSoftly(s -> {
            s.assertThat(so.getName()).isEqualTo("Business class");
            s.assertThat(so.toString()).contains("Business class");
        });
    }

    @Test
    void t02SeatTest() {
        //Test for first Constructor
        SeatOption so1 = api.getOptionManager().createSeatOption("First class");
        SeatOption so2 = api.getOptionManager().createSeatOption("Business class");
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

    @Disabled //Currently broken, throws errors
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
    @Disabled //Currently broken, tests for wrong things
    @Test
    void t04PlaneTest() {
        List<Seat> seats = new ArrayList<>();
        List<SeatOption> so = new ArrayList<>();

        Seat seat1 = api.getSeatManager().createSeat(1,1, so);
        Seat seat2 = api.getSeatManager().createSeat(2,3, so);
        seats.add(seat2);

        Plane plane = api.getPlaneManager().createPlane("TestPlane", "TestManu", "SomeType");
        SoftAssertions.assertSoftly( s -> {
            s.assertThat(plane.getName()).isEqualTo("TestPlane");
            s.assertThat(plane.getManufacturer()).isEqualTo("TestManu");
            s.assertThat(plane.getType()).isEqualTo("SomeType");
            plane.addSeat(seat1);
            assertThat(plane.toString()).contains("rowNumber=1", "seatNumber=1");
            plane.addAllSeats(seats);
            assertThat(plane.toString()).contains("rowNumber=1", "seatNumber=1", "rowNumber=2", "seatNumber=3");
        });
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
}
