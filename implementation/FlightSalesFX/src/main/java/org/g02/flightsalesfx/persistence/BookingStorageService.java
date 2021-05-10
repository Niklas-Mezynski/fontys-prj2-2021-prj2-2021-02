package org.g02.flightsalesfx.persistence;

import org.g02.flightsalesfx.businessEntities.Booking;
import org.g02.flightsalesfx.businessLogic.BookingImpl;

import java.util.List;

public interface BookingStorageService {

    BookingImpl add(Booking booking);

    List<Booking> getAll();

    boolean remove(Booking selectedBooking);
}
