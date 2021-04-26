package com.g02.flightsalesfx.persistence;

import com.g02.flightsalesfx.businessEntities.Booking;
import com.g02.flightsalesfx.businessEntities.Ticket;
import com.g02.flightsalesfx.businessLogic.TicketImpl;

import java.util.List;

public interface TicketStorageService {

    TicketImpl add(Ticket ticket);

    List<Ticket> getAll();

    boolean remove(Ticket ticket);
}
