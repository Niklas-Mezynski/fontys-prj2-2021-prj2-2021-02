package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.businessEntities.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookingImpl implements Booking {

    private SalesEmployee se;
    private Flight flight;
    private Ticket[] tickets;
    private FlightOption[] flightOptions;


    public BookingImpl(SalesEmployee se, Flight flight, FlightOption[] bookedFlightOptions){
        this.se = se;
        this.flight = flight;
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

    @Override
    public Flight getFlight() {
        return this.flight;
    }

    @Override
    public void addTicket(Ticket t) {
        List<Ticket> ticketList = Arrays.asList();
        ticketList.add(t);
        tickets = (Ticket[])ticketList.toArray();
    }
}
