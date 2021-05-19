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
import java.util.Optional;

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

    private TicketImpl(){}


    public TicketImpl(Flight flight, Seat seat, String firstName, String lastName, SeatOption[] seatOptions){
        this.flight = FlightImpl.of(flight);
        this.seat = SeatImpl.of(seat);
        this.seatOptions = new SeatOptionImpl[0];
        if(seatOptions != null)
        this.seatOptions = Arrays.asList(seatOptions).stream().map(so -> SeatOptionImpl.of(so)).toArray(SeatOptionImpl[]::new);
        this.paxFName = firstName;
        this.paxLName = lastName;

    }
<<<<<<< HEAD
=======

    private TicketImpl() {}

    public static TicketImpl of(Ticket t){
>>>>>>> master

    public TicketImpl(int id, Flight flight, Seat seat, String firstName, String lastName, SeatOption[] seatOptions){
        this.flight = FlightImpl.of(flight);
        this.seat = SeatImpl.of(seat);
        this.id = id;
        this.seatOptions = new SeatOptionImpl[0];
        if(seatOptions != null)
            this.seatOptions = Arrays.asList(seatOptions).stream().map(so -> SeatOptionImpl.of(so)).toArray(SeatOptionImpl[]::new);
        this.paxFName = firstName;
        this.paxLName = lastName;

    }

    public static TicketImpl of(Ticket t){
        SeatOption[] so = new SeatOption[0];
        if(t.getSeatOptions() != null){
            so = t.getSeatOptions();
        }
        if(t.getId().isPresent())
            return new TicketImpl(t.getId().get(), FlightImpl.of(t.getFlight()), SeatImpl.of(t.getSeat()), t.getFirstName(), t.getLastName(), so);

        return new TicketImpl(FlightImpl.of(t.getFlight()), SeatImpl.of(t.getSeat()), t.getFirstName(), t.getLastName(), so);
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
        if (seatOptions == null) {
            seatOptions = new SeatOptionImpl[0];
        }
        return seatOptions;
    }

    @Override
    public Optional<Integer> getId() {
        if(this.id != 0){
            return  Optional.of(id);

        }

        return Optional.empty();
    }
}
