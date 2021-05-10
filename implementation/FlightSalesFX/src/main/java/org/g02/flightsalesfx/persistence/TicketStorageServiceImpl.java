package org.g02.flightsalesfx.persistence;

import org.g02.btfdao.dao.Dao;
import org.g02.flightsalesfx.businessEntities.Ticket;
import org.g02.flightsalesfx.businessEntities.TicketManager;
import org.g02.flightsalesfx.businessLogic.TicketImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TicketStorageServiceImpl implements TicketStorageService{

    private final Dao<TicketImpl> dao;

    public TicketStorageServiceImpl(TicketManager ticketManager, Dao<TicketImpl> dao) {
        this.dao = dao;
    }

    @Override
    public TicketImpl add(Ticket ticket) {
        List<TicketImpl> tickets = null;
        try {
            var opt= dao.insert(new TicketImpl(ticket.getFlight(), ticket.getSeat(), ticket.getFirstName(), ticket.getLastName(), ticket.getSeatOptions() ));
            return opt.orElse(null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(tickets.size() == 1){
            return tickets.get(0);
        }else{
            return null;
        }
    }

    @Override
    public List<Ticket> getAll() {
        try {
            var all = dao.getAll();
            return new ArrayList<>(all);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return List.of();
    }

    @Override
    public boolean remove(Ticket ticket) {
        try {
            dao.remove(TicketImpl.of(ticket));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
