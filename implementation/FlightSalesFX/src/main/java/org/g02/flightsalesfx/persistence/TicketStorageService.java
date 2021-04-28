package org.g02.flightsalesfx.persistence;

import org.g02.flightsalesfx.businessEntities.Ticket;
import org.g02.flightsalesfx.businessLogic.TicketImpl;

import java.util.List;

public interface TicketStorageService {

    TicketImpl add(Ticket ticket);

    List<Ticket> getAll();

    boolean remove(Ticket ticket);
}
