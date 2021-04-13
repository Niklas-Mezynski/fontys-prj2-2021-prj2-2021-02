package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.businessEntities.*;

import java.util.List;

public class BookingImpl implements Booking {

    private SalesEmployee se;
    private Flight flight;
    private List<Ticket> tickets;
    private List<FlightOption> flightOptions;


    public BookingImpl(SalesEmployee se, Flight flight, List<Ticket> tickets, List<FlightOption> bookedFlightOptions){
        this.se = se;
        this.flight = flight;
        this.tickets = tickets;
        this.flightOptions = bookedFlightOptions;
    }

    @Override
    public SalesEmployee getSalesEmployee() {
        return null;
    }

    @Override
    public List<Ticket> getTickets() {
        return null;
    }

    @Override
    public List<FlightOption> getBookedFlightOptions() {
        return null;
    }

    @Override
    public String getCustomerEmail() {
        return null;
    }
}
