package org.g02.flightsalesfx.persistence;

import org.g02.flightsalesfx.businessEntities.*;

public interface PersistenceAPI {

    TicketStorageService getTicketStorageService(TicketManager ticketManager);

    BookingStorageService getBookingStorageService(BookingManager bookingManager);

    EmployeeStorageService getEmployeeStorageService(EmployeeManager employeeManager);

    PlaneStorageService getPlaneStorageService(PlaneManager planeManager);

    SeatStorageService getSeatStorageService(SeatManager seatManager);

    SeatOptionsStorageService getSeatOptionStorageService(OptionManager optionManager);

    FlightOptionStorageService getFlightOptionStorageService(OptionManager optionManager);

    AirportStorageService getAirportStorageService(AirportManager airportManager);

    RouteStorageService getRouteStorageService(RouteManager routeManager);

    PriceReductionStorageService getPriceReductionStorageService(PriceReductionManager PriceReductionManager);

    FlightStorageService getFlightStorageService(FlightManager flightManager);

}
