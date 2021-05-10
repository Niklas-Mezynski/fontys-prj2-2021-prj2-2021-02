package org.g02.flightsalesfx.businessLogic;

import org.g02.flightsalesfx.businessEntities.*;
import org.g02.flightsalesfx.persistence.BookingStorageService;

public class BookingManagerImpl implements BookingManager {

    private BookingStorageService bookingStorageService;

    @Override
    public Booking createBooking(SalesEmployee se, Flight flight, Ticket[] tickets, FlightOption[] bookedFlightOptions, String eMail) {
        return new BookingImpl(se, flight, tickets, bookedFlightOptions, eMail);
    }

    public void setBookingStorageService(BookingStorageService bookingStorageService) {
        this.bookingStorageService = bookingStorageService;
    }

}
