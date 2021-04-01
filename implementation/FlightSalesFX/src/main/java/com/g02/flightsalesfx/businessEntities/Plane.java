package com.g02.flightsalesfx.businessEntities;

import java.util.List;

public interface Plane {

    String getName();

    String getManufacturer();

    String getType();

    int getSeatCount();

    int getRowCount();

    void addSeat(Seat s);

    void addAllSeats(List<? extends Seat> seatList);
}
