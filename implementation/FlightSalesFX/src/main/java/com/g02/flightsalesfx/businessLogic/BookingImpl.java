package com.g02.flightsalesfx.businessLogic;

import com.g02.btfdao.annotations.ForeignKey;
import com.g02.btfdao.annotations.PrimaryKey;
import com.g02.btfdao.annotations.TableName;
import com.g02.btfdao.utils.Savable;
import com.g02.flightsalesfx.businessEntities.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@TableName("bookings")
public class BookingImpl implements Booking, Savable {

    @PrimaryKey(autogen = true)
    public int id;
    @ForeignKey("com.g02.flightsalesfx.businessLogic.SalesEmployeeImpl")
    public SalesEmployee se;
    @ForeignKey("com.g02.flightsalesfx.businessLogic.FlightImpl")
    public Flight flight;
    @ForeignKey("com.g02.flightsalesfx.businessLogic.TicketImpl")
    public Ticket[] tickets = new Ticket[0];
    @ForeignKey("com.g02.flightsalesfx.businessLogic.FlightOptionImpl")
    public FlightOption[] flightOptions;
    public String eMail;


    public BookingImpl(SalesEmployee se, Flight flight, Ticket[] tickets, FlightOption[] bookedFlightOptions, String eMail){
        this.se = se;
        this.flight = flight;
        this.tickets = tickets;
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

    @Override
    public void removeTicket(Ticket ticket) {
        List<Ticket> ticketList = new ArrayList<>(Arrays.asList(tickets));
        ticketList.remove(ticket);
        tickets = ticketList.toArray(Ticket[]::new);
    }
}
