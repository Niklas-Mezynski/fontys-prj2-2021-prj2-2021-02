package com.g02.flightsalesfx.businessEntities;

import java.util.List;

public interface Plane {

    String getName();

    String getManufacturer();

    String getType();

    void addSeat(Seat s);

    void addAllSeats(List<? extends Seat> seatList);
}
