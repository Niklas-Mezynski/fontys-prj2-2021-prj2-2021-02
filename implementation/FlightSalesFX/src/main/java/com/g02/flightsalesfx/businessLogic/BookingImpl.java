package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.businessEntities.*;

import java.util.ArrayList;
import java.util.List;

public class BookingImpl implements Booking {

    private SalesEmployee se;
    private Flight flight;
    private List<Ticket> tickets = new ArrayList<Ticket>();
    private List<FlightOption> flightOptions;


    public BookingImpl(SalesEmployee se, Flight flight, List<FlightOption> bookedFlightOptions){
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
        tickets.add(t);
    }
}
