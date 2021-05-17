package org.g02.flightsalesfx.businessEntities;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface BookingManager {

    public Booking createBooking(SalesEmployee se, Flight flight, Ticket[] tickets, FlightOption[] bookedFlightOptions, String eMail, LocalDateTime booking);

}
