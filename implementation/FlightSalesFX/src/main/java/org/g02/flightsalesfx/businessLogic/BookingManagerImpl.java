package org.g02.flightsalesfx.businessLogic;

import org.g02.flightsalesfx.businessEntities.*;
import org.g02.flightsalesfx.persistence.BookingStorageService;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class BookingManagerImpl implements BookingManager {

    private BookingStorageService bookingStorageService;

    @Override
    public Booking createBooking(SalesEmployee se, Flight flight, Ticket[] tickets, FlightOption[] bookedFlightOptions, String eMail, LocalDateTime booking) {
        return new BookingImpl(se, flight, tickets, bookedFlightOptions, eMail, booking);
    }

    public void setBookingStorageService(BookingStorageService bookingStorageService) {
        this.bookingStorageService = bookingStorageService;
    }

}
