package com.g02.flightsalesfx.persistence;

import com.g02.flightsalesfx.businessEntities.Booking;
import com.g02.flightsalesfx.businessEntities.BookingManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookingStorageServiceImpl implements BookingStorageService{
    private List<Booking> bookings;

    public BookingStorageServiceImpl(BookingManager bookingManager) {
        bookings = new ArrayList<Booking>();
    }

    @Override
    public boolean add(Booking booking) {
        bookings.add(booking);
        return bookings.contains(booking);
    }

    @Override
    public List<Booking> getAll() {
        return bookings.stream().collect(Collectors.toUnmodifiableList());
    }

    @Override
    public void remove(Booking selectedBooking) {
        bookings.remove(selectedBooking);
    }
}
