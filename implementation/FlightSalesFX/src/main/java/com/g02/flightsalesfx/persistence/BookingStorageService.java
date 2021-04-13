package com.g02.flightsalesfx.persistence;

import com.g02.flightsalesfx.businessEntities.Booking;

import java.util.List;

public interface BookingStorageService {

    void add(Booking booking);

    List<Booking> getAll();
}
