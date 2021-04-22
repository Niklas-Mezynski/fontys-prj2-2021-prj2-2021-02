package com.g02.flightsalesfx.persistence;

import com.g02.flightsalesfx.businessEntities.Booking;
import com.g02.flightsalesfx.businessLogic.BookingImpl;

import java.util.List;

public interface BookingStorageService {

    BookingImpl add(Booking booking);

    List<Booking> getAll();

    boolean remove(Booking selectedBooking);
}
