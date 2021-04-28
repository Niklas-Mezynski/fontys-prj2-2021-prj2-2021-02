package org.g02.flightsalesfx.businessEntities;

public interface BookingManager {

    public Booking createBooking(SalesEmployee se, Flight flight, Ticket[] tickets, FlightOption[] bookedFlightOptions, String eMail);

}
