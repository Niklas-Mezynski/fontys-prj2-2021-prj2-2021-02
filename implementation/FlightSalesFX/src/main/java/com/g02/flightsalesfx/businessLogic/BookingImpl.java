package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.businessEntities.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookingImpl implements Booking {

    private SalesEmployee se;
    private Flight flight;
    private Ticket[] tickets = new Ticket[0];
    private FlightOption[] flightOptions;
    private String eMail;


    public BookingImpl(SalesEmployee se, Flight flight, FlightOption[] bookedFlightOptions, String eMail){
        this.se = se;
        this.flight = flight;
        this.flightOptions = bookedFlightOptions;
        this.eMail = eMail;
    }

    @Override
    public SalesEmployee getSalesEmployee() {
        return se;
    }

    @Override
    public List<Ticket> getTickets() {
        return Arrays.asList(tickets);
    }

    @Override
    public List<FlightOption> getBookedFlightOptions() {
        return Arrays.asList(flightOptions);
    }

    @Override
    public String getCustomerEmail() {
        return eMail;
    }

    @Override
    public Flight getFlight() {
        return this.flight;
    }

    @Override
    public void addTicket(Ticket t) {
        List<Ticket> ticketList = new ArrayList<>(Arrays.asList(tickets));
        ticketList.add(t);
        tickets = ticketList.toArray(Ticket[]::new);
    }
}
