package org.g02.flightsalesfx.businessEntities;

import org.g02.flightsalesfx.businessLogic.PlaneImpl;

import java.io.Serializable;
import java.util.List;

public interface Plane extends Serializable {

    int getId();

    /**
     * @return Name of the Plane
     */
    String getName();

    List<Seat> getAllSeats();

    /**
     * @return Manufacturer of the Plane
     */
    String getManufacturer();

    /**
     * @return Type of the Plane
     */
    String getType();

    /**
     * @return The Number of Seats that this Plane has
     */
    int getSeatCount();

    /**
     * @return The Number of Rows that this Plane has
     */
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
    Plane addAllSeats(List<? extends Seat> seatList);


}
