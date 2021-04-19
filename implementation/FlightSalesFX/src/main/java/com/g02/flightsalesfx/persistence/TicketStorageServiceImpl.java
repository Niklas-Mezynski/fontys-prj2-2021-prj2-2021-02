package com.g02.flightsalesfx.persistence;

import com.g02.flightsalesfx.businessEntities.Ticket;
import com.g02.flightsalesfx.businessEntities.TicketManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TicketStorageServiceImpl implements TicketStorageService{

    private List<Ticket> tickets;

    public TicketStorageServiceImpl(TicketManager ticketManager) {
        tickets = new ArrayList<Ticket>();
    }

    @Override
    public boolean add(Ticket ticket) {
        tickets.add(ticket);
        return tickets.contains(ticket);
    }

    @Override
    public List<Ticket> getAll() {
        return tickets.stream().collect(Collectors.toUnmodifiableList());
    }
}
