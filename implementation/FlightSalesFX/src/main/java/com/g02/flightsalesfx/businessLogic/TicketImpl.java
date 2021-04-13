package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.businessEntities.*;

import java.util.List;

public class TicketImpl implements Ticket {

    private Flight flight;
    private Booking booking;
    private Seat seat;
    private List<SeatOption> seatOptions;
    private String paxFName;
    private String paxLName;

    public TicketImpl(Flight flight, Seat seat, Booking booking, String firstName, String lastName, List<SeatOption> seatOptions){
        this.flight = flight;
        this.booking = booking;
        this.seat = seat;
        this.seatOptions = seatOptions;
        this.paxFName = firstName;
        this.paxLName = lastName;

    }

    @Override
    public Flight getFlight() {
        return flight;
    }

    @Override
    public Booking getBooking() {
        return booking;
    }

    @Override
    public Seat getSeat() {
        return seat;
    }

    @Override
    public List<SeatOption> getBookedSeatOption() {
        return seatOptions;
    }

    @Override
    public String getFirstName() {
        return this.paxFName;
    }

    @Override
    public String getLastName() {
        return this.paxLName;
    }
}
