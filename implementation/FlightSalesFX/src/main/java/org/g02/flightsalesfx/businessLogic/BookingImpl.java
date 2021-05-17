package org.g02.flightsalesfx.businessLogic;

import org.g02.btfdao.annotations.ForeignKey;
import org.g02.btfdao.annotations.PrimaryKey;
import org.g02.btfdao.annotations.TableName;
import org.g02.btfdao.dao.Savable;
import org.g02.flightsalesfx.businessEntities.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@TableName("bookings")
public class BookingImpl implements Booking, Savable {

    @PrimaryKey(autogen = true)
    public int id;
    @ForeignKey("com.g02.flightsalesfx.businessLogic.SalesEmployeeImpl")
    public SalesEmployeeImpl se;
    @ForeignKey("com.g02.flightsalesfx.businessLogic.FlightImpl")
    public FlightImpl flight;
    @ForeignKey("com.g02.flightsalesfx.businessLogic.TicketImpl")
    public TicketImpl[] tickets = new TicketImpl[0];
    @ForeignKey("com.g02.flightsalesfx.businessLogic.FlightOptionImpl")
    public FlightOptionImpl[] flightOptions;
    public String eMail;
    public LocalDateTime bookingDate;


    public BookingImpl(SalesEmployee se, Flight flight, Ticket[] tickets, FlightOption[] bookedFlightOptions, String eMail, LocalDateTime bookingDate){
        this.se = SalesEmployeeImpl.of(se);
        this.flight = FlightImpl.of(flight);
        this.tickets = Arrays.asList(tickets).stream().map(ticket -> TicketImpl.of(ticket)).toArray(TicketImpl[]::new);
        this.flightOptions = Arrays.asList(bookedFlightOptions).stream().map(bfo -> FlightOptionImpl.of(bfo)).toArray(FlightOptionImpl[]::new);
        this.eMail = eMail;
        this.bookingDate = bookingDate;
    }
    public static BookingImpl of(Booking b){

        return new BookingImpl(SalesEmployeeImpl.of(b.getSalesEmployee()), FlightImpl.of(b.getFlight()), b.getTickets().toArray(Ticket[]::new), b.getBookedFlightOptions().toArray(FlightOption[]::new), b.getCustomerEmail(), b.getBookingDate());
    }

    private BookingImpl() {}

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
    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    @Override
    public void addTicket(Ticket t) {
        List<TicketImpl> ticketList = new ArrayList<>(Arrays.asList(tickets));
        ticketList.add(TicketImpl.of(t));
        tickets = ticketList.toArray(TicketImpl[]::new);
    }

    @Override
    public void removeTicket(Ticket ticket) {
        List<TicketImpl> ticketList = new ArrayList<>(Arrays.asList(tickets));
        ticketList.remove(TicketImpl.of(ticket));
        tickets = ticketList.toArray(TicketImpl[]::new);
    }
}
