package com.g02.flightsalesfx.persistence;

import com.g02.flightsalesfx.businessEntities.Booking;
import com.g02.flightsalesfx.businessEntities.Ticket;

import java.util.List;

public interface TicketStorageService {

    void add(Ticket ticket);

    List<Ticket> getAll();
}
