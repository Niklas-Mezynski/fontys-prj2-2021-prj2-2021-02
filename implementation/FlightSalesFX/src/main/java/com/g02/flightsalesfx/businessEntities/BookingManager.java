package com.g02.flightsalesfx.businessEntities;

import com.g02.flightsalesfx.persistence.BookingStorageService;

import java.util.List;

public interface BookingManager {

    public Booking createBooking(SalesEmployee se, Flight flight, List<Ticket> tickets , List<FlightOption> bookedFlightOptions);

}
