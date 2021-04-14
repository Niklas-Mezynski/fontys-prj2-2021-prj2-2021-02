package com.g02.flightsalesfx.businessEntities;

import java.util.List;

public interface Plane {

    /**
     * @return Name of the Plane
     */
    String getName();

    /**
     * @return Manufacturer of the Plane
     */
    String getManufacturer();

    /**
     * @return Type of the Plane
     */
    String getType();

    int getSeatCount();

    int getRowCount();

    /**
     * Adds a Seat to the Plane
     * @param s The Seat to add
     */
    void addSeat(Seat s);

    /**
     * Adds all Seats in a List to the Plane
     * @param seatList A List of Seats to add
     */
    void addAllSeats(List<? extends Seat> seatList);

    /**
     * @return The Number of Rows that this Plane has
     */
    int getRows();

    List<Seat> getAllSeats();
}
