package org.g02.flightsalesfx.businessLogic;

import org.g02.btfdao.annotations.ForeignKey;
import org.g02.btfdao.annotations.PrimaryKey;
import org.g02.btfdao.annotations.TableName;
import org.g02.btfdao.dao.Savable;
import org.g02.flightsalesfx.businessEntities.Flight;
import org.g02.flightsalesfx.businessEntities.Seat;
import org.g02.flightsalesfx.businessEntities.SeatOption;
import org.g02.flightsalesfx.businessEntities.Ticket;

import java.util.Arrays;
import java.util.List;

@TableName("tickets")
public class TicketImpl implements Ticket, Savable {

    @PrimaryKey(autogen = true)
    public int id;
    @ForeignKey("com.g02.flightsalesfx.businessLogic.FlightImpl")
    public FlightImpl flight;
    @ForeignKey("com.g02.flightsalesfx.businessLogic.SeatImpl")
    public SeatImpl seat;



    @ForeignKey("com.g02.flightsalesfx.businessLogic.SeatOptionImpl")
    public SeatOptionImpl[] seatOptions;
    public String paxFName;
    public String paxLName;


    public TicketImpl(Flight flight, Seat seat, String firstName, String lastName, SeatOption[] seatOptions){
        this.flight = FlightImpl.of(flight);
        this.seat = SeatImpl.of(seat);
        this.seatOptions = Arrays.asList(seatOptions).stream().map(so -> SeatOptionImpl.of(so)).toArray(SeatOptionImpl[]::new);
        this.paxFName = firstName;
        this.paxLName = lastName;

    }

    private TicketImpl() {}

    public static TicketImpl of(Ticket t){
        var ticket = new TicketImpl(t.getFlight(), t.getSeat(), t.getFirstName(), t.getLastName(), t.getSeatOptions());
        ticket.id=t.getID();
        return ticket;
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
    public int getID() {
        return id;
    }

    @Override
    public SeatOption[] getSeatOptions() {
        if (seatOptions == null) {
            seatOptions = new SeatOptionImpl[0];
        }
        return seatOptions;
    }
}
