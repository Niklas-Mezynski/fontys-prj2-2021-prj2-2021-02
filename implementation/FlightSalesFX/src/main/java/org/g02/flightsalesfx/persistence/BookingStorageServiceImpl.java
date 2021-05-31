package org.g02.flightsalesfx.persistence;

import org.g02.btfdao.dao.Dao;
import org.g02.flightsalesfx.businessEntities.Booking;
import org.g02.flightsalesfx.businessEntities.BookingManager;
import org.g02.flightsalesfx.businessEntities.FlightOption;
import org.g02.flightsalesfx.businessLogic.BookingImpl;
import org.g02.flightsalesfx.businessLogic.TicketImpl;

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
        BookingImpl bImpl = BookingImpl.of(booking);
        try {

            return dao.insert(new BookingImpl(booking.getSalesEmployee(), booking.getFlight(), booking.getTickets().toArray(TicketImpl[]::new), booking.getBookedFlightOptions().toArray(FlightOption[]::new), booking.getCustomerEmail(), booking.getBookingDate(), booking.getBookingPrice() ))

                    .orElse(null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Booking> getAll() {
        try {
            var all = dao.getAll();
            return new ArrayList<>(all);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return List.of();
    }

    @Override
    public boolean remove(Booking booking) {
        List<BookingImpl> bookings = null;
        BookingImpl bImpl = BookingImpl.of(booking);
        try {

            dao.remove(BookingImpl.of(booking));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
