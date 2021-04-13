package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.businessEntities.*;
import com.g02.flightsalesfx.persistence.BookingStorageService;

import java.util.List;

public class BookingManagerImpl implements BookingManager {

    private BookingStorageService bookingStorageService;

    @Override
    public Booking createBooking(SalesEmployee se, Flight flight, List<Ticket> tickets, List<FlightOption> bookedFlightOptions) {
        return new BookingImpl(se, flight, tickets, bookedFlightOptions);
    }

    public void setBookingStorageService(BookingStorageService bookingStorageService) {
        this.bookingStorageService = bookingStorageService;
    }

}
