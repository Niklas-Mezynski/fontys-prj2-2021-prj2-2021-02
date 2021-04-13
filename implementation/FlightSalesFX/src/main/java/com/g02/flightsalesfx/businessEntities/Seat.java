package com.g02.flightsalesfx.businessEntities;

import java.util.List;

public interface Seat extends Comparable<Seat> {

    /**
     * @return The Row Number of this Seat
     */
    int getRowNumber();

    /**
     * @return The Seat Number of this Seat
     */
    int getSeatNumber();

    /**
     * Adds a SeatOption to this Seat
     * @param so The SeatOption to add to this Seat
     */
    void addSeatOption(SeatOption so);

    /**
     * Adds all SeatOptions from a List to this Seat
     * @param seatOptionList The List of SeatOptions to add
     */
    void addAllSeatOptions(List<? extends SeatOption> seatOptionList);

    /**
     * @param o The other Seat to compare to
     * @return Whether the Seat comes before or after the other one, ranked by Row Number first, and then Seat Number
     */
    default int compareTo(Seat o) {
        if(this.getRowNumber() == o.getRowNumber()){
            return this.getSeatNumber()-o.getSeatNumber();
        }
        return this.getRowNumber()-o.getRowNumber();
    }

    List<SeatOption> getSeatOptions();

}
