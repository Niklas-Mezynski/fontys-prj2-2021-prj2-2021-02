package com.g02.flightsalesfx.businessEntities;

import java.util.List;

public interface Seat {

    int getRowNumber();

    int getSeatNumber();

    void addSeatOption(SeatOption so);

    void addAllSeatOptions(List<? extends SeatOption> seatOptionList);

}
