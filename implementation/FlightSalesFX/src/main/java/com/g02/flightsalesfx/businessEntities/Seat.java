package com.g02.flightsalesfx.businessEntities;

import java.util.List;

public interface Seat extends Comparable<Seat> {

    int getRowNumber();

    int getSeatNumber();

    void addSeatOption(SeatOption so);

    void addAllSeatOptions(List<? extends SeatOption> seatOptionList);

    default int compareTo(Seat o) {
        if(this.getRowNumber() == o.getRowNumber()){
            return this.getSeatNumber()-o.getSeatNumber();
        }
        return this.getRowNumber()-o.getRowNumber();
    }

}
