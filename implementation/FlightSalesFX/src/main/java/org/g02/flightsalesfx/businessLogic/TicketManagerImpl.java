package org.g02.flightsalesfx.businessLogic;

import org.g02.flightsalesfx.businessEntities.*;
import org.g02.flightsalesfx.persistence.TicketStorageService;

public class TicketManagerImpl implements TicketManager {
    private TicketStorageService ticketStorageService;

    @Override
    public Ticket createTicket(Flight flight, Seat seat, String passengerFName, String passengerLName, SeatOption[] bookedSeatOptions) {
        return new TicketImpl(flight, seat,  passengerFName, passengerLName, bookedSeatOptions);
    }

    public void setTicketStorageService(TicketStorageService ticketStorageService) {
        this.ticketStorageService = ticketStorageService;
    }
}
