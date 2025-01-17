package org.g02.flightsalesfx.businessLogic;

import org.assertj.core.api.SoftAssertions;
import org.g02.flightsalesfx.businessEntities.*;
import org.g02.flightsalesfx.persistence.BookingStorageService;
import org.g02.flightsalesfx.persistence.EmployeeStorageService;
import org.g02.flightsalesfx.persistence.PersistenceAPI;
import org.g02.flightsalesfx.persistence.RouteStorageService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

public class ManagementDashboardLogicTest {

    private BusinessLogicAPI businessLogicAPI;

    private SalesEmployee se = new SalesEmployeeImpl("Snens", "email@gmail.com", "");
    private SalesEmployee se2 = new SalesEmployeeImpl("Peter", "PetersEmail@gmail.com", "");

    Route route = new RouteImpl(new AirportImpl("DUS", "Düsseldorf", "Germany"), new AirportImpl("BER", "Berlin", "Germany"));

    @BeforeEach
    void start() {
        PersistenceAPI persistenceAPI = Mockito.mock(PersistenceAPI.class);
        BookingStorageService bookingStorageService = Mockito.mock(BookingStorageService.class);
        EmployeeStorageService employeeStorageService = Mockito.mock(EmployeeStorageService.class);
        RouteStorageService routeStorageService = Mockito.mock(RouteStorageService.class);

        //SampleData Booking
        List<Booking> bookings = new ArrayList<>();

        var so = new SalesOfficerImpl("", "", "");
        var flight = new FlightImpl(so, 123, LocalDateTime.now(), LocalDateTime.now().plusMinutes(10), route, null, 50);
        Ticket[] tickets = new Ticket[]{new TicketImpl(flight, new SeatImpl(0, 0), "Peter", "Gockel", new SeatOption[0])};
        Ticket[] tickets2 = new Ticket[]{new TicketImpl(flight, new SeatImpl(0, 0), "Peter", "Gockel", new SeatOption[]{new SeatOptionImpl("Business class", 35.0)})};
        Ticket[] tickets3 = new Ticket[]{new TicketImpl(flight, new SeatImpl(0, 0), "Peter", "Gockel", new SeatOption[0]), new TicketImpl(flight, new SeatImpl(0, 0), "Peter", "Gockel", new SeatOption[0])};
        FlightOption[] flightOptions = new FlightOption[0];
        bookings.add(new BookingImpl(se, flight, tickets, flightOptions, "mail@mail.com", LocalDateTime.of(2021, 4, 10, 20, 0), 50.00));
        bookings.add(new BookingImpl(se, flight, tickets2, flightOptions, "mail@mail.com", LocalDateTime.of(2021, 4, 8, 20, 0), 80.00));
        bookings.add(new BookingImpl(se2, flight, tickets, flightOptions, "mail@mail.com", LocalDateTime.of(2021, 4, 9, 20, 0), 70.00));
        bookings.add(new BookingImpl(se, flight, tickets3, flightOptions, "mail@mail.com", LocalDateTime.of(2021, 5, 20, 16, 0), 60.00));
        bookings.add(new BookingImpl(se, flight, tickets, flightOptions, "mail@mail.com", LocalDateTime.of(2020, 7, 12, 20, 0), 30.00));

        Mockito.when(bookingStorageService.getAll()).thenReturn(bookings);
        Mockito.when(employeeStorageService.getAll()).thenReturn(List.of(se, se2, so));
        Mockito.when(routeStorageService.getAll()).thenReturn(List.of(route));

        Mockito.when(persistenceAPI.getBookingStorageService(any())).thenReturn(bookingStorageService);
        Mockito.when(persistenceAPI.getEmployeeStorageService(any())).thenReturn(employeeStorageService);
        Mockito.when(persistenceAPI.getRouteStorageService(any())).thenReturn(routeStorageService);
        businessLogicAPI = BusinessLogicImplementationProvider.getImplementation(persistenceAPI);
    }

    @Test
    void t01totalRevenueByEmp() {
        assertThat(businessLogicAPI.totalRevenueByEmp(se)).isEqualTo(220);
    }

    @Test
    void t02totalNumOfBookingsByAnEmployee() {
        assertThat(businessLogicAPI.totalNumOfBookingsByAnEmployee(se)).isEqualTo(4);
    }

    @Test
    void t03avgNumOfTicketsPerBooking() {
        assertThat(businessLogicAPI.avgNumOfTicketsPerBooking(se)).isEqualTo(1.25);
    }

    @Test
    void t04getMonthlyRevenue() {
        LocalDateTime date = LocalDateTime.of(2021, 1, 1, 0, 0);
        Map<LocalDateTime, Double> monthlyRevenue = businessLogicAPI.getMonthlyRevenue(se, date);
        SoftAssertions.assertSoftly((s) -> {
            s.assertThat(monthlyRevenue.get(date.plusMonths(2))).isEqualTo(0); //March
            s.assertThat(monthlyRevenue.get(date.plusMonths(3))).isEqualTo(130); //April
            s.assertThat(monthlyRevenue.get(date.plusMonths(4))).isEqualTo(60); //May
        });
    }

    @Test
    void t05getAvgMonthlyRevenues() {
        LocalDateTime date = LocalDateTime.of(2021, 1, 1, 0, 0);
        Map<LocalDateTime, Double> monthlyRevenue = businessLogicAPI.getAvgMonthlyRevenues(date);
        SoftAssertions.assertSoftly((s) -> {
            s.assertThat(monthlyRevenue.get(date.plusMonths(2))).isEqualTo(0); //March
            s.assertThat(monthlyRevenue.get(date.plusMonths(3))).isEqualTo(200.0 / 2); //April
            s.assertThat(monthlyRevenue.get(date.plusMonths(4))).isEqualTo(60.0 / 2); //May
        });
    }

    @Test
    void t06totalRevenueByRoute() {
        assertThat(businessLogicAPI.totalRevenueByRoute(route)).isEqualTo(290);
    }

    @Test
    void t07sumOfAClassesRevenue() {
        assertThat(businessLogicAPI.sumOfAClassesRevenue(route, "business")).isEqualTo(35);
    }

    @Test
    void t08getFirstBookingOfARoute() {
        Optional<Booking> firstBookingOfARoute = businessLogicAPI.getFirstBookingOfARoute(route);
        assertThat(firstBookingOfARoute.isPresent()).isTrue();
        assertThat(firstBookingOfARoute.get().getBookingDate().getYear()).isEqualTo(2020);
    }

    @Test
    void t09getRevenueForSpecificRouteInOneYear() {
        assertThat(businessLogicAPI.getRevenueForSpecificRouteInOneYear(route, 2021)).isEqualTo(260);
    }


}
