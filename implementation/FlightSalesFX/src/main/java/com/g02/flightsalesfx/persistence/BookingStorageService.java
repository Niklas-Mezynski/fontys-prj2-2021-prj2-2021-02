package com.g02.flightsalesfx.persistence;

import com.g02.flightsalesfx.businessEntities.Booking;

import java.util.List;

public interface BookingStorageService {

    boolean add(Booking booking);

    List<Booking> getAll();

    void remove(Booking selectedBooking);
}
