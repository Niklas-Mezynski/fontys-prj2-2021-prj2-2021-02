package com.g02.flightsalesfx.persistence;

import com.g02.btfdao.dao.Dao;
import com.g02.flightsalesfx.businessEntities.Booking;
import com.g02.flightsalesfx.businessEntities.BookingManager;
import com.g02.flightsalesfx.businessEntities.FlightOption;
import com.g02.flightsalesfx.businessLogic.BookingImpl;
import com.g02.flightsalesfx.businessLogic.TicketImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookingStorageServiceImpl implements BookingStorageService{

    private final Dao<BookingImpl> dao;

    public BookingStorageServiceImpl(BookingManager bookingManager, Dao<BookingImpl> dao) {
        this.dao = dao;
    }

    @Override
    public BookingImpl add(Booking booking) {
        List<BookingImpl> bookings = null;
        try {
            bookings = dao.insert(new BookingImpl(booking.getSalesEmployee(), booking.getFlight(), booking.getTickets().toArray(TicketImpl[]::new), booking.getBookedFlightOptions().toArray(FlightOption[]::new), booking.getCustomerEmail() ));
        } catch (IllegalAccessException | NoSuchFieldException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        if(bookings.size() == 1){
            return bookings.get(0);
        }else{
            return null;
        }
    }

    @Override
    public List<Booking> getAll() {
        try {
            var all = dao.getAll();
            return new ArrayList<>(all);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return List.of();
    }

    @Override
    public boolean remove(Booking booking) {
        List<BookingImpl> bookings = null;
        try {
            bookings = dao.remove(new BookingImpl(booking.getSalesEmployee(), booking.getFlight(), booking.getTickets().toArray(TicketImpl[]::new), booking.getBookedFlightOptions().toArray(FlightOption[]::new), booking.getCustomerEmail() ));
        } catch (IllegalAccessException | NoSuchFieldException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return bookings.size() == 1;
    }
}
