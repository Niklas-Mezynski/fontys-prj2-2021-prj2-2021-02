package com.g02.flightsalesfx.persistence;

import com.g02.btfdao.dao.Dao;
import com.g02.flightsalesfx.businessEntities.Ticket;
import com.g02.flightsalesfx.businessEntities.TicketManager;
import com.g02.flightsalesfx.businessLogic.TicketImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TicketStorageServiceImpl implements TicketStorageService{

    private final Dao<TicketImpl> dao;

    public TicketStorageServiceImpl(TicketManager ticketManager, Dao<TicketImpl> dao) {
        this.dao = dao;
    }

    @Override
    public boolean add(Ticket ticket) {
        List<TicketImpl> tickets = null;
        try {
            tickets = dao.insert(new TicketImpl(ticket.getFlight(), ticket.getSeat(), ticket.getFirstName(), ticket.getLastName(), ticket.getSeatOptions() ));
        } catch (IllegalAccessException | NoSuchFieldException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return tickets.size() == 1;
    }

    @Override
    public List<Ticket> getAll() {
        try {
            var all = dao.getAll();
            return new ArrayList<>(all);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return List.of();
    }

    @Override
    public boolean remove(Ticket ticket) {
        List<TicketImpl> tickets = null;
        try {
            tickets = dao.remove(new TicketImpl(ticket.getFlight(), ticket.getSeat(), ticket.getFirstName(), ticket.getLastName(), ticket.getSeatOptions() ));
        } catch (IllegalAccessException | NoSuchFieldException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return tickets.size() == 1;
    }
}
