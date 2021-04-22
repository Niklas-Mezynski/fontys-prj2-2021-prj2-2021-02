package com.g02.flightsalesfx.businessLogic;

import com.g02.btfdao.annotations.ForeignKey;
import com.g02.btfdao.annotations.PrimaryKey;
import com.g02.btfdao.annotations.TableName;
import com.g02.btfdao.utils.Savable;
import com.g02.flightsalesfx.businessEntities.*;

import java.util.Arrays;
import java.util.List;

@TableName("tickets")
public class TicketImpl implements Ticket, Savable {

    @PrimaryKey(autogen = true)
    public int id;
    @ForeignKey("com.g02.flightsalesfx.businessLogic.FlightImpl")
    public Flight flight;
    @ForeignKey("com.g02.flightsalesfx.businessLogic.SeatImpl")
    public Seat seat;



    @ForeignKey("com.g02.flightsalesfx.businessLogic.SeatOptionImpl")
    public SeatOption[] seatOptions;
    public String paxFName;
    public String paxLName;


    public TicketImpl(Flight flight, Seat seat,  String firstName, String lastName, SeatOption[] seatOptions){
        this.flight = flight;
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
    public Seat getSeat() {
        return seat;
    }

    @Override
    public List<SeatOption> getBookedSeatOption() {
        return Arrays.asList(seatOptions.clone());
    }

    @Override
    public String getFirstName() {
        return this.paxFName;
    }

    @Override
    public String getLastName() {
        return this.paxLName;
    }

    @Override
    public SeatOption[] getSeatOptions() {
        return seatOptions;
    }
}
